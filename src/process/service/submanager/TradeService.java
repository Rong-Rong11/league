package process.service.submanager;

import java.time.LocalDate;
import java.util.ArrayList;

import config.FinanceConfiguration;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;
import process.repositery.TeamRepositery;
import process.service.tradetools.EvaluateTradeSatisfaction;
import process.service.tradetools.TradeFinder;
import process.service.tradetools.TradeGenerator;
import process.simulator.TradeSimulator;

public abstract class TradeService {

    private final TeamRepositery teamRepositery = TeamRepositery.getInstance();
    private final double salaryCap;
    private final double luxuryTaxLine;
    private final TradeSimulator tradeSimulator = new TradeSimulator();
    private final TradeFinder tradeFinder;

    public TradeService(double salaryCap, double luxuryTaxLine) {
        this.salaryCap = salaryCap;
        this.luxuryTaxLine = luxuryTaxLine;
        tradeFinder = new TradeFinder(salaryCap);
    }

    public final void simulateTrade(LocalDate date, int month) {
        if (!canSimulateTradePeriod(date)) {
            return;
        }

        for (Team teamA : teamRepositery.getAllTeams()) {
            simulateTradesForTeam(teamA, date, month);
        }
    }

    protected abstract boolean canSimulateTradePeriod(LocalDate date);

    protected abstract boolean isSatisfied(EvaluateTradeSatisfaction evaluateTradeSatisfaction);

    protected abstract boolean canTryTradeAtDate(EvaluateTradeSatisfaction evaluateTradeSatisfaction, LocalDate date);

    protected abstract void recordTrade(Team teamA, Team teamB, Player playerAToTrade, Player playerBToTrade,
            LocalDate date);

    protected abstract boolean isSeasonTrade();

    private void simulateTradesForTeam(Team teamA, LocalDate date, int month) {
        int tradeAttempts = 0;
        EvaluateTradeSatisfaction evaluateTradeSatisfaction = new EvaluateTradeSatisfaction(teamA);

        while (canTeamTryTrade(teamA, date, tradeAttempts, evaluateTradeSatisfaction)) {
            tradeAttempts++;
            Team teamB = findTradePartner(teamA);
            if (teamB == null) {
                continue;
            }
            Player playerAToTrade = selectPlayerToTrade(teamA);
            Player playerBToTrade = selectPlayerToTrade(teamB);

            if (playerAToTrade == null || playerBToTrade == null) {
                continue;
            }
            ArrayList<Player> playersAAfterTrade = buildUpdatedRoster(teamA, playerAToTrade, playerBToTrade);
            ArrayList<Player> playersBAfterTrade = buildUpdatedRoster(teamB, playerBToTrade, playerAToTrade);
            if (validateTrade(teamA, teamB, playersAAfterTrade, playersBAfterTrade, month)) {
                recordTrade(teamA, teamB, playerAToTrade, playerBToTrade, date);
            }
        }
    }

    private boolean canTeamTryTrade(Team teamA, LocalDate date, int tradeAttempts,
            EvaluateTradeSatisfaction evaluateTradeSatisfaction) {
        if (isSatisfied(evaluateTradeSatisfaction)) {
            return false;
        }
        if (tradeAttempts >= FinanceConfiguration.MAX_TRADE_ATTEMPTS_PER_TEAM) {
            return false;
        }
        if (teamA.getTeamFinance().getTransferMade() > FinanceConfiguration.MAX_TRADE_PER_TEAM) {
            return false;
        }
        if (!canTryTradeAtDate(evaluateTradeSatisfaction, date)) {
            return false;
        }
        return true;
    }

    private Team findTradePartner(Team teamA) {
        return tradeFinder.findTeamForTrade(teamA, isSeasonTrade());
    }

    private Player selectPlayerToTrade(Team team) {
        return TradeGenerator.generatePlayersToTrade(team, isSeasonTrade(), salaryCap);
    }

    private ArrayList<Player> buildUpdatedRoster(Team team, Player playerToRemove, Player playerToAdd) {
        ArrayList<Player> updatedPlayers = new ArrayList<Player>(team.getCurrentPlayers().values());
        updatedPlayers.remove(playerToRemove);
        updatedPlayers.add(playerToAdd);
        return updatedPlayers;
    }

    private boolean validateTrade(Team teamA, Team teamB, ArrayList<Player> playersA, ArrayList<Player> playersB,
            int month) {
        return tradeSimulator.validateTrade(teamA, teamB, playersA, playersB, month, salaryCap, luxuryTaxLine);
    }
}

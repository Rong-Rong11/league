package process.manager.submanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import config.FinanceConfiguration;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;
import process.manager.tradetools.EvaluateTradeSatisfaction;
import process.manager.tradetools.TradeFinder;
import process.manager.tradetools.TradeGenerator;
import process.repositery.TeamRepositery;
import process.simulator.TradeSimulator;

public class TradeManager {

    private TeamRepositery teamRepositery = TeamRepositery.getInstance();
    private TreeMap<LocalDate, Trade> seasonTrades = new TreeMap<LocalDate, Trade>();
    private ArrayList<Trade> preSeasonTrade = new ArrayList<Trade>();
    private LocalDate deadLine = LocalDate.of(2026, 04, 20);
    private final int MAX_ATTEMPTS = 5;
    private double salaryCap;
    private double luxuryTaxLine;
    private TradeSimulator tradeSimulator = new TradeSimulator();
    private TradeFinder tradeFinder;

    public TradeManager(double salaryCap, double luxuryTaxLine) {
        this.salaryCap = salaryCap;
        this.luxuryTaxLine = luxuryTaxLine;
        tradeFinder = new TradeFinder(salaryCap);
    }

    public void simulatePreSeasonTrade(int month) {
        simulateTrade(false, FinanceConfiguration.PRESEASON_TRADE, month);
    }

    public void simulateSeasonTrade(LocalDate date, int month) {
        simulateTrade(true, date, month);
    }

    private void simulateTrade(boolean season, LocalDate date, int month) {
        if (!canSimulateTradePeriod(season, date)) {
            return;
        }

        for (Team teamA : teamRepositery.getAllTeams()) {
            simulateTradesForTeam(teamA, season, date, month);
        }
    }

    private boolean canSimulateTradePeriod(boolean season, LocalDate date) {
        if (!season) {
            return true;
        }
        return !date.isAfter(deadLine);
    }

    private void simulateTradesForTeam(Team teamA, boolean season, LocalDate date, int month) {
        int tradeAttempts = 0;
        EvaluateTradeSatisfaction evaluateTradeSatisfaction = new EvaluateTradeSatisfaction(teamA);

        while (canTeamTryTrade(teamA, season, date, tradeAttempts, evaluateTradeSatisfaction)) {
            tradeAttempts++;
            Team teamB = findTradePartner(teamA, season);
            if (teamB == null) {
                continue;
            }
            Player playerAToTrade = selectPlayerToTrade(teamA, season);
            Player playerBToTrade = selectPlayerToTrade(teamB, season);

            if (playerAToTrade == null || playerBToTrade == null) {
                continue;
            }
            ArrayList<Player> playersAAfterTrade = buildUpdatedRoster(teamA, playerAToTrade, playerBToTrade);
            ArrayList<Player> playersBAfterTrade = buildUpdatedRoster(teamB, playerBToTrade, playerAToTrade);
            if (validateTrade(teamA, teamB, playersAAfterTrade, playersBAfterTrade, month)) {
                recordTrade(teamA, teamB, playerAToTrade, playerBToTrade, season, date);
            }
        }
    }

    private boolean canTeamTryTrade(Team teamA, boolean season, LocalDate date, int tradeAttempts,
            EvaluateTradeSatisfaction evaluateTradeSatisfaction) {
        if (evaluateTradeSatisfaction.isSatisfied(season)) {
            return false;
        }
        if (tradeAttempts >= MAX_ATTEMPTS) {
            return false;
        }
        if (teamA.getTeamFinance().getTransferMade() > FinanceConfiguration.MAX_TRADE_PER_TEAM) {
            return false;
        }
        if (season && !evaluateTradeSatisfaction.shouldTryTrade(date, deadLine)) {
            return false;
        }
        return true;
    }

    private Team findTradePartner(Team teamA, boolean season) {
        return tradeFinder.findTeamForTrade(teamA, season);
    }

    private Player selectPlayerToTrade(Team team, boolean season) {
        return TradeGenerator.generatePlayersToTrade(team, season, salaryCap);
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

    private void recordTrade(Team teamA, Team teamB, Player playerAToTrade, Player playerBToTrade, boolean season,
            LocalDate date) {
        if (season) {
            seasonTrades.put(date, new Trade(playerAToTrade, teamA, playerBToTrade, teamB, date));
        } else {
            preSeasonTrade
                    .add(new Trade(playerAToTrade, teamA, playerBToTrade, teamB, FinanceConfiguration.PRESEASON_TRADE));
        }
    }
}

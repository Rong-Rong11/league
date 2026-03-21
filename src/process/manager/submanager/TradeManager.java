package process.manager.submanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import config.FinanceConfiguration;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;
import data.team.finance.TeamFinance;
import data.team.finance.transfer.TeamTransferStrategy;
import process.manager.tradetools.TradeFinder;
import process.repositery.TeamRepositery;
import process.simulator.TradeSimulator;
import process.visitor.teamtransfer.PreSeasonPlayerToTradeVisitor;
import process.visitor.teamtransfer.PreSeasonTradeSatisfactionVisitor;
import process.visitor.teamtransfer.SeasonPlayerToTradeVisitor;
import process.visitor.teamtransfer.SeasonTradeSatisfactionVisitor;

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
        if (season) {
            if (date.isAfter(deadLine)) {
                return;
            }
        }
        for (Team teamA : teamRepositery.getAllTeams()) {
            int tradeAttempts = 0;
            while (!isSatisfied(teamA.getTeamFinance(), season) && tradeAttempts < MAX_ATTEMPTS) {
                tradeAttempts++;
                if (teamA.getTeamFinance().getTransferMade() > FinanceConfiguration.MAX_TRADE_PER_TEAM) {
                    break;
                }
                if (season) {
                    if (!shouldTryTrade(teamA, date)) {
                        break;
                    }
                }
                Team teamB = tradeFinder.findTeamForTrade(teamA, season);
                if (teamB == null) {
                    continue;
                }

                ArrayList<Player> playersA = new ArrayList<Player>(teamA.getPlayers().values());
                ArrayList<Player> playersB = new ArrayList<Player>(teamB.getPlayers().values());

                Player playerBToTrade = generatePlayersToTrade(teamB, season);
                Player playerAToTrade = generatePlayersToTrade(teamA, season);
                if (playerAToTrade == null || playerBToTrade == null) {
                    continue;
                }

                playersA.remove(playerAToTrade);
                playersA.add(playerBToTrade);

                playersB.remove(playerBToTrade);
                playersB.add(playerAToTrade);

                if (tradeSimulator.validateTrade(teamA, teamB, playersA, playersB, month, salaryCap, luxuryTaxLine)) {
                    if (season) {
                        seasonTrades.put(date, new Trade(playerAToTrade, teamA, playerBToTrade, teamB, date));
                    } else {
                        preSeasonTrade.add(
                                new Trade(playerAToTrade, teamA, playerBToTrade, teamB,
                                        FinanceConfiguration.PRESEASON_TRADE));
                    }

                }
            }

        }
    }

    private boolean isSatisfied(TeamFinance teamFinance, boolean season) {
        TeamTransferStrategy teamTransferStrategy = teamFinance.getTeamTransferStrategy();
        int transferMade = teamFinance.getTransferMade();
        if (season) {
            SeasonTradeSatisfactionVisitor visitor = new SeasonTradeSatisfactionVisitor(transferMade,
                    teamTransferStrategy.getSeasonIntent());
            return teamFinance.getTeamTransferStrategy().accept(visitor);
        } else {
            PreSeasonTradeSatisfactionVisitor preSeasonTradeSatisfactionVisitor = new PreSeasonTradeSatisfactionVisitor(
                    transferMade);
            return teamTransferStrategy.accept(preSeasonTradeSatisfactionVisitor);
        }
    }

    private Player generatePlayersToTrade(Team team, boolean season) {
        if (season) {
            TeamTransferStrategy teamTransferStrategy = team.getTeamFinance().getTeamTransferStrategy();
            SeasonPlayerToTradeVisitor seasonPlayerToTradeVisitor = new SeasonPlayerToTradeVisitor(team,
                    teamTransferStrategy.getSeasonIntent(), salaryCap);
            return teamTransferStrategy.accept(seasonPlayerToTradeVisitor);
        } else {
            TeamTransferStrategy teamTransferStrategy = team.getTeamFinance().getTeamTransferStrategy();
            PreSeasonPlayerToTradeVisitor preSeasonPlayerToTradeVisitor = new PreSeasonPlayerToTradeVisitor(team);
            return teamTransferStrategy.accept(preSeasonPlayerToTradeVisitor);
        }

    }

    private boolean shouldTryTrade(Team team, LocalDate date) {
        double performance = team.getTeamPerformance().getPerformanceRating();
        double deadlineFactor = 1.0;

        if (date.plusDays(15).isAfter(deadLine)) {
            deadlineFactor = 1.5;
        }

        if (performance < 0.4) {
            return Math.random() < (0.6 * deadlineFactor);
        }
        if (performance > 0.7 && team.getTeamFinance().getTeamTransferStrategy().isAllIn()) {
            return Math.random() < (0.5 * deadlineFactor);
        }
        return Math.random() < (0.2 * deadlineFactor);
    }

}

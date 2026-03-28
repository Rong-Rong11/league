package process.manager.tradetools;

import config.FinanceConfiguration;
import data.team.Team;
import data.team.finance.transfer.TeamTransferStrategy;
import process.repositery.TeamRepositery;
import process.utilitary.TeamUtilitary;
import process.visitor.teamtransfer.EvaluateSeasonIntentVisitor;

public class TradeFinder {

    private TeamRepositery teamRepositery = TeamRepositery.getInstance();
    private double salaryCap;

    public TradeFinder(double salaryCap) {
        super();
        this.salaryCap = salaryCap;
    }

    public Team findTeamForTrade(Team teamA, boolean season) {
        for (Team teamB : teamRepositery.getAllTeams()) {
            if (teamB.equals(teamA)) {
                continue;
            }
            if (teamB.getCurrentPlayers().isEmpty()) {
                continue;
            }
            if (season) {
                if (teamB.getTeamFinance().getTransferMade() >= FinanceConfiguration.MAX_TRADE_PER_TEAM) {
                    continue;
                }
            }
            boolean compatible = isTradeCompatible(teamA, teamB, season);
            double random = Math.random();

            if (compatible) {
                if (random < 0.70) {
                    return teamB;
                }
            } else {
                if (random < 0.05) {
                    return teamB;
                }
            }
        }
        return null;
    }

    private boolean isTradeCompatible(Team teamA, Team teamB, boolean season) {
        TeamTransferStrategy strategyA = teamA.getTeamFinance().getTeamTransferStrategy();
        TeamTransferStrategy strategyB = teamB.getTeamFinance().getTeamTransferStrategy();
        if (isSelling(teamA, strategyA, season) && isBuying(teamB, strategyB, season)) {
            return true;
        }
        if (isSelling(teamB, strategyB, season) && isBuying(teamA, strategyA, season)) {
            return true;
        }
        if (isStable(strategyA.getSeasonIntent()) || isStable(strategyB.getSeasonIntent())) {

            return false;
        }
        if (!TeamUtilitary.getTeamSportProfile(teamA).equals(TeamUtilitary.getTeamSportProfile(teamB))) {
            return true;
        }

        return false;
    }

    private boolean isStable(String seasonIntent) {
        return seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_STABLE);
    }

    private boolean isSelling(Team team, TeamTransferStrategy teamTransferStrategy, boolean season) {
        if (season) {
            String seasonIntent = evaluateSeasonIntent(team, teamTransferStrategy);
            teamTransferStrategy.setSeasonIntent(seasonIntent);
            return seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER);
        }
        return teamTransferStrategy.isRebuild() || teamTransferStrategy.isSalaryDump();
    }

    private boolean isBuying(Team team, TeamTransferStrategy teamTransferStrategy, boolean season) {
        if (season) {
            String seasonIntent = evaluateSeasonIntent(team, teamTransferStrategy);
            teamTransferStrategy.setSeasonIntent(seasonIntent);
            return seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_BUYER);
        }
        return teamTransferStrategy.isAllIn() ||
                teamTransferStrategy.isSmallAdjust() ||
                teamTransferStrategy.isBalanced();
    }

    private String evaluateSeasonIntent(Team team, TeamTransferStrategy teamTransferStrategy) {
        EvaluateSeasonIntentVisitor evaluateSeasonIntentVisitor = new EvaluateSeasonIntentVisitor(team, salaryCap);
        return teamTransferStrategy.accept(evaluateSeasonIntentVisitor);
    }
}

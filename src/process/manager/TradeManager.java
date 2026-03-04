package process.manager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import config.FinanceConfiguration;
import config.FinancialPolicy;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;
import data.team.finance.transfer.TeamTransferStrategy;
import process.repositery.TeamRepositery;
import process.simulator.TradeSimulator;
import process.utilitary.TeamUtilitary;
import process.visitor.PreSeasonTradeSatisfactionVisitor;
import process.visitor.teamtransfer.EvaluateSeasonIntentVisitor;
import process.visitor.teamtransfer.PreSeasonPlayerToTradeVisitor;
import process.visitor.teamtransfer.SeasonPlayerToTradeVisitor;
import process.visitor.teamtransfer.SeasonTradeSatisfactionVisitor;

public class TradeManager {

	private TeamRepositery teamRepositery = TeamRepositery.getInstance();
	private TreeMap<LocalDate, Trade> seasonTrades = new TreeMap<LocalDate, Trade>();
	private ArrayList<Trade> preSeasonTrade = new ArrayList<Trade>();
	private LocalDate deadLine;
	private final int MAX_ATTEMPTS = 5;
	private double salaryCap;

	public TradeManager(double salaryCap) {
		this.salaryCap = salaryCap;
	}

	public void simulatePreSeasonTrade() {
		simulateTrade(false, FinanceConfiguration.PRESEASON_TRADE);
	}

	public void simulateSeasonTrade(LocalDate date) {
		simulateTrade(true, date);
	} // mettre dans le constructeur

	private void simulateTrade(boolean season, LocalDate date) {
		if (season) {
			if (date.isAfter(deadLine)) {
				return;
			}
		}
		int tradeAttempts = 0;
		for (Team teamA : teamRepositery.getAllTeams()) {
			if (teamA.getTeamFinance().getTransferMade() > FinanceConfiguration.MAX_TRADE_PER_TEAM) {
				continue;
			}
			if (season) {
				if (!shouldTryTrade(teamA, date)) {
					continue;
				}
			}
			Team teamB = findTeamForTrade(teamA, season);
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

			if (TradeSimulator.validateTrade(teamA, teamB, playersA, playersB, 0)) {
				if (season) {
					seasonTrades.put(date, new Trade(playerAToTrade, teamA, playerBToTrade, teamB, date));
				} else {
					preSeasonTrade.add(
							new Trade(playerAToTrade, teamA, playerBToTrade, teamB, FinanceConfiguration.PRESEASON_TRADE));
				}

			}
			if (isSatisfied(teamA, season) || tradeAttempts >= MAX_ATTEMPTS) {
				continue;
			}
			tradeAttempts++;

		}
	}

	private boolean isSatisfied(Team team, boolean season) {
		if (season) {
			SeasonTradeSatisfactionVisitor visitor = new SeasonTradeSatisfactionVisitor(
					team.getTeamFinance().getTransferMade(), team.getTeamPerformance().getPerformanceRating());
			return team.getTeamFinance().getTeamTransferStrategy().accept(visitor);
		} else {
			PreSeasonTradeSatisfactionVisitor preSeasonTradeSatisfactionVisitor = new PreSeasonTradeSatisfactionVisitor(
					team.getTeamFinance().getTransferMade());
			TeamTransferStrategy teamTransferStrategy = team.getTeamFinance().getTeamTransferStrategy();
			return teamTransferStrategy.accept(preSeasonTradeSatisfactionVisitor);
		}
	}

	private Player generatePlayersToTrade(Team team, boolean season) {
		if (season) {
			TeamTransferStrategy teamTransferStrategy = team.getTeamFinance().getTeamTransferStrategy();
			SeasonPlayerToTradeVisitor seasonPlayerToTradeVisitor = new SeasonPlayerToTradeVisitor(team, this);
			return teamTransferStrategy.accept(seasonPlayerToTradeVisitor);
		} else {
			TeamTransferStrategy teamTransferStrategy = team.getTeamFinance().getTeamTransferStrategy();
			PreSeasonPlayerToTradeVisitor preSeasonPlayerToTradeVisitor = new PreSeasonPlayerToTradeVisitor(team, this);
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
		if (performance > 0.7 && team.getTeamFinance().getTeamTransferStrategy().getName()
				.equals(FinancialPolicy.TRANSFER_STRATEGY_ALL_IN)) {
			return Math.random() < (0.5 * deadlineFactor);
		}
		return Math.random() < (0.2 * deadlineFactor);
	}

	private Team findTeamForTrade(Team teamA, boolean season) {
		for (Team teamB : teamRepositery.getAllTeams()) {
			if (teamB.equals(teamA)) {
				continue;
			}
			if (teamB.getPlayers().isEmpty()) {
				continue;
			}
			if (season) {
				if (teamB.getTeamFinance().getTransferMade() >= FinanceConfiguration.MAX_TRADE_PER_TEAM) {
					continue;
				}
			}
			if (!isTradeCompatible(teamA, teamB, season)) {
				double random = Math.random();
				if (random < 0.15) {
					continue;
				}
				return teamB;
			}
			double random = Math.random();
			if (random < 0.7) {
				continue;
			}
			return teamB;
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

		if (!TeamUtilitary.getTeamSportProfile(teamA).equals(TeamUtilitary.getTeamSportProfile(teamB))) {
			return true;
		}
		return false;
	}

	private boolean isSelling(Team team, TeamTransferStrategy teamTransferStrategy, boolean season) {
		if (season) {
			return evaluateSeasonIntent(team, teamTransferStrategy)
					.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER);
		}
		return teamTransferStrategy.getName().equals(FinancialPolicy.TRANSFER_STRATEGY_REBUILD) ||
				teamTransferStrategy.getName().equals(FinancialPolicy.TRANSFER_STRATEGY_SALARY_DUMP);
	}

	private boolean isBuying(Team team, TeamTransferStrategy teamTransferStrategy, boolean season) {
		if (season) {
			return evaluateSeasonIntent(team, teamTransferStrategy).equals(FinanceConfiguration.SEASON_TRADE_INTENT_BUYER);
		}
		return teamTransferStrategy.getName().equals(FinancialPolicy.TRANSFER_STRATEGY_ALL_IN) ||
				teamTransferStrategy.getName().equals(FinancialPolicy.TRANSFER_STRATEGY_SMALL_ADJUST) ||
				teamTransferStrategy.getName().equals(FinancialPolicy.TRANSFER_STRATEGY_BALANCED);
	}

	private String evaluateSeasonIntent(Team team, TeamTransferStrategy teamTransferStrategy) {
		EvaluateSeasonIntentVisitor evaluateSeasonIntentVisitor = new EvaluateSeasonIntentVisitor(team, salaryCap);
		return teamTransferStrategy.accept(evaluateSeasonIntentVisitor);
	}

}

package process.service.trade.selection;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import data.team.Team;
import data.team.finance.transfer.TeamTransferStrategy;
import log.LoggerUtility;
import process.repository.TeamRepository;
import process.utility.TeamUtility;
import process.visitor.teamtransfer.EvaluateSeasonIntentVisitor;

public class TradePartnerFinder {
	private static final Logger logger = LoggerUtility.getLogger(TradePartnerFinder.class, "text");

	private TeamRepository teamRepository = TeamRepository.getInstance();
	private double salaryCap;

	public TradePartnerFinder(double salaryCap) {
		super();
		this.salaryCap = salaryCap;
		logger.debug("Trade partner finder initialized with salary cap " + salaryCap);
	}

	public Team findTeamForTrade(Team teamA, boolean season) {
		if (teamA == null) {
			logger.warn("Unable to find trade partner because team is null");
			return null;
		}

		logger.debug("Searching trade partner for " + teamA.getName());

		for (Team teamB : teamRepository.getAllTeams()) {
			if (teamB == null) {
				logger.warn("Skipping null team while searching trade partner for " + teamA.getName());
				continue;
			}

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
					logger.debug("Found compatible trade partner " + teamB.getName() + " for " + teamA.getName());
					return teamB;
				}
			} else {
				if (random < 0.05) {
					logger.debug("Found non-compatible trade partner by random chance " + teamB.getName()
							+ " for " + teamA.getName());
					return teamB;
				}
			}
		}

		logger.trace("No trade partner found for " + teamA.getName());
		return null;
	}

	private boolean isTradeCompatible(Team teamA, Team teamB, boolean season) {
		if (teamA == null || teamB == null) {
			logger.warn("Returning false trade compatibility because one team is null");
			return false;
		}

		TeamTransferStrategy strategyA = teamA.getTeamFinance().getBehavior().getTeamTransferStrategy();
		TeamTransferStrategy strategyB = teamB.getTeamFinance().getBehavior().getTeamTransferStrategy();

		if (isSelling(teamA, strategyA, season) && isBuying(teamB, strategyB, season)) {
			logger.trace("Trade compatible because " + teamA.getName() + " is selling and " + teamB.getName()
					+ " is buying");
			return true;
		}
		if (isSelling(teamB, strategyB, season) && isBuying(teamA, strategyA, season)) {
			logger.trace("Trade compatible because " + teamB.getName() + " is selling and " + teamA.getName()
					+ " is buying");
			return true;
		}
		if (isStable(strategyA.getSeasonIntent()) || isStable(strategyB.getSeasonIntent())) {
			return false;
		}
		if (!TeamUtility.getTeamSportProfile(teamA).equals(TeamUtility.getTeamSportProfile(teamB))) {
			logger.trace("Trade compatible because teams have different sport profiles");
			return true;
		}

		return false;
	}

	private boolean isStable(String seasonIntent) {
		if (seasonIntent == null) {
			logger.warn("Returning false stable intent because season intent is null");
			return false;
		}

		return seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_STABLE);
	}

	private boolean isSelling(Team team, TeamTransferStrategy teamTransferStrategy, boolean season) {
		if (team == null || teamTransferStrategy == null) {
			logger.warn("Returning false selling status because team or transfer strategy is null");
			return false;
		}

		if (season) {
			String seasonIntent = evaluateSeasonIntent(team, teamTransferStrategy);
			teamTransferStrategy.setSeasonIntent(seasonIntent);
			return seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER);
		}

		return teamTransferStrategy.isRebuild() || teamTransferStrategy.isSalaryDump();
	}

	private boolean isBuying(Team team, TeamTransferStrategy teamTransferStrategy, boolean season) {
		if (team == null || teamTransferStrategy == null) {
			logger.warn("Returning false buying status because team or transfer strategy is null");
			return false;
		}

		if (season) {
			String seasonIntent = evaluateSeasonIntent(team, teamTransferStrategy);
			teamTransferStrategy.setSeasonIntent(seasonIntent);
			return seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_BUYER);
		}

		return teamTransferStrategy.isAllIn()
				|| teamTransferStrategy.isSmallAdjust()
				|| teamTransferStrategy.isBalanced();
	}

	private String evaluateSeasonIntent(Team team, TeamTransferStrategy teamTransferStrategy) {
		EvaluateSeasonIntentVisitor evaluateSeasonIntentVisitor = new EvaluateSeasonIntentVisitor(team, salaryCap);
		String seasonIntent = teamTransferStrategy.accept(evaluateSeasonIntentVisitor);

		logger.trace("Evaluated season trade intent for " + team.getName() + ": " + seasonIntent);

		return seasonIntent;
	}
}

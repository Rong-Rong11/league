package process.service.trade;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import data.player.Player;
import data.team.Team;
import log.LoggerUtility;
import process.repository.TeamRepository;
import process.service.trade.evaluation.TradeSatisfactionEvaluator;
import process.service.trade.execution.TradeProcessor;
import process.service.trade.selection.TradePartnerFinder;
import process.service.trade.selection.TradePlayerSelector;

public abstract class TradeService {
	private static final Logger logger = LoggerUtility.getLogger(TradeService.class, "text");

	private final TeamRepository teamRepository = TeamRepository.getInstance();
	private final double salaryCap;
	private final double luxuryTaxLine;
	private final TradeProcessor tradeProcessor = new TradeProcessor();
	private final TradePartnerFinder tradePartnerFinder;

	public TradeService(double salaryCap, double luxuryTaxLine) {
		this.salaryCap = salaryCap;
		this.luxuryTaxLine = luxuryTaxLine;
		tradePartnerFinder = new TradePartnerFinder(salaryCap);
		logger.debug("Trade service initialized with salary cap "
				+ salaryCap
				+ " and luxury tax line "
				+ luxuryTaxLine);
	}

	public final void simulateTrade(LocalDate date, int month) {
		if (!canSimulateTradePeriod(date)) {
			logger.debug("Skipping trade simulation because trade period is not available for " + date);
			return;
		}

		logger.info("Simulating trades for " + date + " month " + month);
		int processedTeams = 0;
		for (Team teamA : teamRepository.getAllTeams()) {
			simulateTradesForTeam(teamA, date, month);
			processedTeams++;
		}
		logger.info("Trade simulation completed for " + processedTeams + " teams");
	}

	protected abstract boolean canSimulateTradePeriod(LocalDate date);

	protected abstract boolean isSatisfied(TradeSatisfactionEvaluator tradeSatisfactionEvaluator);

	protected abstract boolean canTryTradeAtDate(TradeSatisfactionEvaluator tradeSatisfactionEvaluator, LocalDate date);

	protected abstract void recordTrade(Team teamA, Team teamB, Player playerAToTrade, Player playerBToTrade,
			LocalDate date);

	protected abstract boolean isSeasonTrade();

	private void simulateTradesForTeam(Team teamA, LocalDate date, int month) {
		if (teamA == null) {
			logger.warn("Skipping trade simulation because team is null");
			return;
		}
		logger.debug("Simulating trades for team " + teamA.getName());
		int tradeAttempts = 0;
		TradeSatisfactionEvaluator tradeSatisfactionEvaluator = new TradeSatisfactionEvaluator(teamA);

		while (canTeamTryTrade(teamA, date, tradeAttempts, tradeSatisfactionEvaluator)) {
			tradeAttempts++;
			logger.trace("Trade attempt " + tradeAttempts + " for " + teamA.getName());
			Team teamB = findTradePartner(teamA);
			if (teamB == null) {
				logger.trace("No trade partner found for " + teamA.getName());
				continue;
			}
			logger.trace("Trade partner found for " + teamA.getName() + ": " + teamB.getName());
			Player playerAToTrade = selectPlayerToTrade(teamA);
			Player playerBToTrade = selectPlayerToTrade(teamB);

			if (playerAToTrade == null || playerBToTrade == null) {
				logger.trace("Skipping trade attempt because one selected player is null");
				continue;
			}
			logger.debug("Evaluating trade "
					+ playerAToTrade.getName()
					+ " from "
					+ teamA.getName()
					+ " for "
					+ playerBToTrade.getName()
					+ " from "
					+ teamB.getName());
			ArrayList<Player> playersAAfterTrade = buildUpdatedRoster(teamA, playerAToTrade, playerBToTrade);
			ArrayList<Player> playersBAfterTrade = buildUpdatedRoster(teamB, playerBToTrade, playerAToTrade);
			if (validateTrade(teamA, teamB, playersAAfterTrade, playersBAfterTrade, month)) {
				logger.info("Trade validated between " + teamA.getName() + " and " + teamB.getName());
				recordTrade(teamA, teamB, playerAToTrade, playerBToTrade, date);
			} else {
				logger.trace("Trade rejected between " + teamA.getName() + " and " + teamB.getName());
			}
		}
		logger.debug("Trade simulation ended for " + teamA.getName() + " after " + tradeAttempts + " attempts");
	}

	private boolean canTeamTryTrade(Team teamA, LocalDate date, int tradeAttempts,
			TradeSatisfactionEvaluator tradeSatisfactionEvaluator) {
		if (isSatisfied(tradeSatisfactionEvaluator)) {
			logger.trace("Team " + teamA.getName() + " will not trade because it is satisfied");
			return false;
		}
		if (tradeAttempts >= FinanceConfiguration.MAX_TRADE_ATTEMPTS_PER_TEAM) {
			logger.trace("Team " + teamA.getName() + " reached maximum trade attempts");
			return false;
		}
		if (teamA.getTeamFinance().getTransferMade() >= FinanceConfiguration.MAX_TRADE_PER_TEAM) {
			logger.trace("Team " + teamA.getName() + " reached maximum trades made");
			return false;
		}
		if (!canTryTradeAtDate(tradeSatisfactionEvaluator, date)) {
			logger.trace("Team " + teamA.getName() + " cannot try trade at date " + date);
			return false;
		}
		return true;
	}

	private Team findTradePartner(Team teamA) {
		return tradePartnerFinder.findTeamForTrade(teamA, isSeasonTrade());
	}

	private Player selectPlayerToTrade(Team team) {
		return TradePlayerSelector.selectPlayerToTrade(team, isSeasonTrade(), salaryCap);
	}

	private ArrayList<Player> buildUpdatedRoster(Team team, Player playerToRemove, Player playerToAdd) {
		ArrayList<Player> updatedPlayers = new ArrayList<Player>(team.getCurrentPlayers().values());
		updatedPlayers.remove(playerToRemove);
		updatedPlayers.add(playerToAdd);
		logger.trace("Built updated roster for " + team.getName() + " with " + updatedPlayers.size() + " players");
		return updatedPlayers;
	}

	private boolean validateTrade(Team teamA, Team teamB, ArrayList<Player> playersA, ArrayList<Player> playersB,
			int month) {
		boolean validTrade = tradeProcessor.processTrade(teamA, teamB, playersA, playersB, month, salaryCap, luxuryTaxLine);
		logger.trace("Trade validation result between "
				+ teamA.getName()
				+ " and "
				+ teamB.getName()
				+ " is "
				+ validTrade);
		return validTrade;
	}
}

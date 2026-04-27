package process.service.trade.regularseason;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import config.CalendarConfiguration;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;
import log.LoggerUtility;
import process.service.trade.TradeService;
import process.service.trade.evaluation.TradeSatisfactionEvaluator;

public class RegularSeasonTradeService extends TradeService {
	private static final Logger logger = LoggerUtility.getLogger(RegularSeasonTradeService.class, "text");

	private final TreeMap<LocalDate, ArrayList<Trade>> seasonTrades = new TreeMap<LocalDate, ArrayList<Trade>>();

	public RegularSeasonTradeService(double salaryCap, double luxuryTaxLine) {
		super(salaryCap, luxuryTaxLine);
		logger.debug("Regular season trade service initialized");
	}

	@Override
	protected boolean canSimulateTradePeriod(LocalDate date) {
		if (date == null) {
			logger.warn("Returning false trade period simulation because date is null");
			return false;
		}

		boolean canSimulate = !date.isAfter(CalendarConfiguration.TRADE_DEADLINE);
		logger.trace("Regular season trade period simulation at " + date + ": " + canSimulate);

		return canSimulate;
	}

	@Override
	protected boolean isSatisfied(TradeSatisfactionEvaluator tradeSatisfactionEvaluator) {
		if (tradeSatisfactionEvaluator == null) {
			logger.warn("Returning true regular season trade satisfaction because evaluator is null");
			return true;
		}

		return tradeSatisfactionEvaluator.isSatisfied(true);
	}

	@Override
	protected boolean canTryTradeAtDate(TradeSatisfactionEvaluator tradeSatisfactionEvaluator, LocalDate date) {
		if (tradeSatisfactionEvaluator == null || date == null) {
			logger.warn("Returning false trade attempt because evaluator or date is null");
			return false;
		}

		return tradeSatisfactionEvaluator.shouldTryTrade(date, CalendarConfiguration.TRADE_DEADLINE);
	}

	@Override
	protected void recordTrade(Team teamA, Team teamB, Player playerAToTrade, Player playerBToTrade, LocalDate date) {
		if (teamA == null || teamB == null || playerAToTrade == null || playerBToTrade == null || date == null) {
			logger.warn("Skipping regular season trade record because team, player or date is null");
			return;
		}

		ArrayList<Trade> tradesAtDate = seasonTrades.get(date);
		if (tradesAtDate == null) {
			logger.trace("Creating regular season trade list for date " + date);
			tradesAtDate = new ArrayList<Trade>();
			seasonTrades.put(date, tradesAtDate);
		}

		tradesAtDate.add(new Trade(playerAToTrade, teamA, playerBToTrade, teamB, date));

		logger.debug("Recorded regular season trade between " + teamA.getName() + " and " + teamB.getName()
				+ " at " + date);
		logger.trace("Regular season trade recorded: " + playerAToTrade.getName() + " for "
				+ playerBToTrade.getName());
	}

	@Override
	protected boolean isSeasonTrade() {
		return true;
	}

	@Override
	public ArrayList<Trade> getTrades() {
		ArrayList<Trade> regularSeasonTrades = new ArrayList<Trade>();
		for (ArrayList<Trade> tradesAtDate : seasonTrades.values()) {
			regularSeasonTrades.addAll(tradesAtDate);
		}
		return regularSeasonTrades;
	}
}

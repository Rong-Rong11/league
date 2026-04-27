package process.service.trade.preseason;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;
import log.LoggerUtility;
import process.service.trade.TradeService;
import process.service.trade.evaluation.TradeSatisfactionEvaluator;

public class PreSeasonTradeService extends TradeService {
	private static final Logger logger = LoggerUtility.getLogger(PreSeasonTradeService.class, "text");

	private final ArrayList<Trade> preSeasonTrades = new ArrayList<Trade>();

	public PreSeasonTradeService(double salaryCap, double luxuryTaxLine) {
		super(salaryCap, luxuryTaxLine);
		logger.debug("Preseason trade service initialized");
	}

	@Override
	protected boolean canSimulateTradePeriod(LocalDate date) {
		return true;
	}

	@Override
	protected boolean isSatisfied(TradeSatisfactionEvaluator tradeSatisfactionEvaluator) {
		if (tradeSatisfactionEvaluator == null) {
			logger.warn("Returning true preseason trade satisfaction because evaluator is null");
			return true;
		}

		return tradeSatisfactionEvaluator.isSatisfied(false);
	}

	@Override
	protected boolean canTryTradeAtDate(TradeSatisfactionEvaluator tradeSatisfactionEvaluator, LocalDate date) {
		return true;
	}

	@Override
	protected void recordTrade(Team teamA, Team teamB, Player playerAToTrade, Player playerBToTrade, LocalDate date) {
		if (teamA == null || teamB == null || playerAToTrade == null || playerBToTrade == null) {
			logger.warn("Skipping preseason trade record because team or player is null");
			return;
		}

		preSeasonTrades
				.add(new Trade(playerAToTrade, teamA, playerBToTrade, teamB, FinanceConfiguration.PRESEASON_TRADE));

		logger.debug("Recorded preseason trade between " + teamA.getName() + " and " + teamB.getName());
		logger.trace("Preseason trade recorded: " + playerAToTrade.getName() + " for " + playerBToTrade.getName());
	}

	@Override
	protected boolean isSeasonTrade() {
		return false;
	}
}

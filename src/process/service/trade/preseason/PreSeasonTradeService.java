package process.service.trade.preseason;

import java.time.LocalDate;
import java.util.ArrayList;

import config.FinanceConfiguration;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;
import process.service.trade.TradeService;
import process.service.trade.evaluation.TradeSatisfactionEvaluator;

public class PreSeasonTradeService extends TradeService {

	private final ArrayList<Trade> preSeasonTrades = new ArrayList<Trade>();

	public PreSeasonTradeService(double salaryCap, double luxuryTaxLine) {
		super(salaryCap, luxuryTaxLine);
	}

	@Override
	protected boolean canSimulateTradePeriod(LocalDate date) {
		return true;
	}

	@Override
	protected boolean isSatisfied(TradeSatisfactionEvaluator tradeSatisfactionEvaluator) {
		return tradeSatisfactionEvaluator.isSatisfied(false);
	}

	@Override
	protected boolean canTryTradeAtDate(TradeSatisfactionEvaluator tradeSatisfactionEvaluator, LocalDate date) {
		return true;
	}

	@Override
	protected void recordTrade(Team teamA, Team teamB, Player playerAToTrade, Player playerBToTrade, LocalDate date) {
		preSeasonTrades
				.add(new Trade(playerAToTrade, teamA, playerBToTrade, teamB, FinanceConfiguration.PRESEASON_TRADE));
	}

	@Override
	protected boolean isSeasonTrade() {
		return false;
	}
}

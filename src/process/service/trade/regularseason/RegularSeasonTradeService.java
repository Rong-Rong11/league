package process.service.trade.regularseason;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import config.CalendarConfiguration;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;
import process.service.trade.TradeService;
import process.service.trade.evaluation.TradeSatisfactionEvaluator;

public class RegularSeasonTradeService extends TradeService {

	private final TreeMap<LocalDate, ArrayList<Trade>> seasonTrades = new TreeMap<LocalDate, ArrayList<Trade>>();

	public RegularSeasonTradeService(double salaryCap, double luxuryTaxLine) {
		super(salaryCap, luxuryTaxLine);
	}

	@Override
	protected boolean canSimulateTradePeriod(LocalDate date) {
		return !date.isAfter(CalendarConfiguration.TRADE_DEADLINE);
	}

	@Override
	protected boolean isSatisfied(TradeSatisfactionEvaluator tradeSatisfactionEvaluator) {
		return tradeSatisfactionEvaluator.isSatisfied(true);
	}

	@Override
	protected boolean canTryTradeAtDate(TradeSatisfactionEvaluator tradeSatisfactionEvaluator, LocalDate date) {
		return tradeSatisfactionEvaluator.shouldTryTrade(date, CalendarConfiguration.TRADE_DEADLINE);
	}

	@Override
	protected void recordTrade(Team teamA, Team teamB, Player playerAToTrade, Player playerBToTrade, LocalDate date) {
		ArrayList<Trade> tradesAtDate = seasonTrades.get(date);
		if (tradesAtDate == null) {
			tradesAtDate = new ArrayList<Trade>();
			seasonTrades.put(date, tradesAtDate);
		}
		tradesAtDate.add(new Trade(playerAToTrade, teamA, playerBToTrade, teamB, date));
	}

	@Override
	protected boolean isSeasonTrade() {
		return true;
	}
}

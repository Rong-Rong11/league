package process.service.trade;

import java.time.LocalDate;
import java.util.TreeMap;

import config.CalendarConfiguration;
import data.finance.transfer.Trade;
import data.player.Player;
import data.team.Team;

public class RegularSeasonTradeService extends TradeService {

	private final TreeMap<LocalDate, Trade> seasonTrades = new TreeMap<LocalDate, Trade>();

	public RegularSeasonTradeService(double salaryCap, double luxuryTaxLine) {
		super(salaryCap, luxuryTaxLine);
	}

	@Override
	protected boolean canSimulateTradePeriod(LocalDate date) {
		return !date.isAfter(CalendarConfiguration.TRADE_DEADLINE);
	}

	@Override
	protected boolean isSatisfied(EvaluateTradeSatisfaction evaluateTradeSatisfaction) {
		return evaluateTradeSatisfaction.isSatisfied(true);
	}

	@Override
	protected boolean canTryTradeAtDate(EvaluateTradeSatisfaction evaluateTradeSatisfaction, LocalDate date) {
		return evaluateTradeSatisfaction.shouldTryTrade(date, CalendarConfiguration.TRADE_DEADLINE);
	}

	@Override
	protected void recordTrade(Team teamA, Team teamB, Player playerAToTrade, Player playerBToTrade, LocalDate date) {
		seasonTrades.put(date, new Trade(playerAToTrade, teamA, playerBToTrade, teamB, date));
	}

	@Override
	protected boolean isSeasonTrade() {
		return true;
	}
}

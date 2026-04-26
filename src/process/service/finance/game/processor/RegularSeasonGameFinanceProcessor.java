package process.service.finance.game.processor;

import data.finance.GameStat;
import data.finance.budget.FinanceSeasonMoment;
import data.league.League;
import process.service.finance.game.expense.GameExpenseCalculator;
import process.service.finance.game.expense.RegularSeasonGameExpenseCalculator;
import process.service.finance.game.revenue.GameRevenueCalculator;
import process.service.finance.game.revenue.RegularSeasonGameRevenueCalculator;

public class RegularSeasonGameFinanceProcessor extends GameFinanceProcessor {

	public RegularSeasonGameFinanceProcessor(League league) {
	  super(league);
	}

	@Override
	protected GameRevenueCalculator createRevenueCalculator(League league, GameStat gameStat) {
	  return new RegularSeasonGameRevenueCalculator(league, gameStat);
	}

	@Override
	protected GameExpenseCalculator createExpenseCalculator(GameStat gameStat) {
	  return new RegularSeasonGameExpenseCalculator(gameStat);
	}

	@Override
	protected FinanceSeasonMoment getSeasonMoment() {
	  return FinanceSeasonMoment.REGULAR_SEASON;
	}
}

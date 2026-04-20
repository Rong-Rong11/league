package process.service.finance.game.processor;

import data.finance.GameStat;
import data.finance.budget.FinanceSeasonMoment;
import data.league.League;
import data.league.PlayoffRound;
import process.service.finance.game.GameExpenseCalculator;
import process.service.finance.game.GameRevenueCalculator;
import process.service.finance.game.PlayoffGameExpenseCalculator;
import process.service.finance.game.PlayoffGameRevenueCalculator;

public class PlayoffGameFinanceProcessor extends GameFinanceProcessor {

	private PlayoffRound round;

	public PlayoffGameFinanceProcessor(League league, PlayoffRound round) {
	  super(league);
	  this.round = round;
	}

	@Override
	protected GameRevenueCalculator createRevenueCalculator(League league, GameStat gameStat) {
	  return new PlayoffGameRevenueCalculator(league, gameStat, round);
	}

	@Override
	protected GameExpenseCalculator createExpenseCalculator(GameStat gameStat) {
	  return new PlayoffGameExpenseCalculator(gameStat, round);
	}

	@Override
	protected FinanceSeasonMoment getSeasonMoment() {
	  return FinanceSeasonMoment.PLAYOFF;
	}

	public PlayoffRound getRound() {
	  return round;
	}

}

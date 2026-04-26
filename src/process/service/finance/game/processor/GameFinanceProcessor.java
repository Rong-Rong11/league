package process.service.finance.game.processor;

import java.time.LocalDate;
import java.util.HashMap;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.finance.budget.FinanceSeasonMoment;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import log.LoggerUtility;
import process.service.finance.game.expense.GameExpenseCalculator;
import process.service.finance.game.revenue.GameRevenueCalculator;
import process.utility.FinanceUtility;

public abstract class GameFinanceProcessor {
	private static final Logger logger = LoggerUtility.getLogger(GameFinanceProcessor.class, "text");

	private League league;
	private HashMap<Game, GameStat> gameStats = new HashMap<Game, GameStat>();

	public GameFinanceProcessor(League league) {
		this.league = league;
	}

	public final void calculateGame(Game game, LocalDate date, int month) {
		if (game == null) {
			logger.warn("Skipping game finance calculation because game is null");
			return;
		}
		if (date == null) {
			logger.warn("Skipping game finance calculation because date is null");
			return;
		}
		GameStat gameStat = new GameStat(game);
		FinanceSeasonMoment seasonMoment = getSeasonMoment();
		logger.debug("Calculating game finance for "
				+ getGameLabel(game)
				+ " on "
				+ date
				+ " month "
				+ month
				+ " during "
				+ seasonMoment);

		GameRevenueCalculator revenueCalculator = createRevenueCalculator(league, gameStat);
		logger.trace("Calculating game revenue for " + getGameLabel(game));
		revenueCalculator.calculateGameRevenue(game, date);
		logger.trace("Adding game revenue to budgets for " + getGameLabel(game));
		FinanceUtility.addGameRevenue(game, gameStat, month, seasonMoment);

		GameExpenseCalculator expenseCalculator = createExpenseCalculator(gameStat);
		logger.trace("Calculating game expenses for " + getGameLabel(game));
		expenseCalculator.calculateGameExpenses(game);
		logger.trace("Adding game expenses to budgets for " + getGameLabel(game));
		FinanceUtility.addGameExpense(game, gameStat, month, seasonMoment);

		gameStats.put(game, gameStat);
		logger.debug("Game finance calculated and stored for " + getGameLabel(game));
	}

	protected abstract GameRevenueCalculator createRevenueCalculator(League league, GameStat gameStat);

	protected abstract GameExpenseCalculator createExpenseCalculator(GameStat gameStat);

	protected abstract FinanceSeasonMoment getSeasonMoment();

	public GameStat getGameStat(Game game) {
		GameStat gameStat = gameStats.get(game);
		if (gameStat == null) {
			logger.trace("No game stat found for " + getGameLabel(game));
		}
		return gameStat;
	}

	private String getGameLabel(Game game) {
		if (game == null || game.getGameContext() == null) {
			return "<none>";
		}
		Team awayTeam = game.getGameContext().getAwayTeam();
		Team homeTeam = game.getGameContext().getHomeTeam();
		String awayName = awayTeam == null ? "<unknown>" : awayTeam.getName();
		String homeName = homeTeam == null ? "<unknown>" : homeTeam.getName();
		return awayName + " at " + homeName;
	}

}

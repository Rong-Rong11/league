package process.service.finance.game.processor;

import java.time.LocalDate;
import java.util.HashMap;

import data.finance.GameStat;
import data.finance.budget.FinanceSeasonMoment;
import data.league.League;
import data.sport.setup.Game;
import process.service.finance.game.GameExpenseCalculator;
import process.service.finance.game.GameRevenueCalculator;
import process.utility.FinanceUtility;

public abstract class GameFinanceProcessor {
    private League league;
    private HashMap<Game, GameStat> gameStats = new HashMap<Game, GameStat>();

    public GameFinanceProcessor(League league) {
        this.league = league;
    }

    public final void calculateGame(Game game, LocalDate date, int month) {
        GameStat gameStat = new GameStat(game);
        FinanceSeasonMoment seasonMoment = getSeasonMoment();

        GameRevenueCalculator revenueCalculator = createRevenueCalculator(league, gameStat);
        revenueCalculator.calculateGameRevenue(game, date);
        FinanceUtility.addGameRevenue(game, gameStat, month, seasonMoment);

        GameExpenseCalculator expenseCalculator = createExpenseCalculator(gameStat);
        expenseCalculator.calculateGameExpenses(game);
        FinanceUtility.addGameExpense(game, gameStat, month, seasonMoment);

        gameStats.put(game, gameStat);
    }

    protected abstract GameRevenueCalculator createRevenueCalculator(League league, GameStat gameStat);

    protected abstract GameExpenseCalculator createExpenseCalculator(GameStat gameStat);

    protected abstract FinanceSeasonMoment getSeasonMoment();

    public GameStat getGameStat(Game game) {
        return gameStats.get(game);
    }

}

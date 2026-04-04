package process.service.finance.tools.game.processor;

import java.time.LocalDate;
import java.util.HashMap;

import data.finance.GameStat;
import data.finance.budget.FinanceSeasonMoment;
import data.sport.setup.Game;
import process.service.finance.tools.game.GameExpenseCalculator;
import process.service.finance.tools.game.GameRevenueCalculator;
import process.utility.FinanceUtilitary;

public abstract class GameFinanceProcessor {
    private HashMap<Game, GameStat> gameStats = new HashMap<Game, GameStat>();

    public final void calculateGame(Game game, LocalDate date, int month) {
        GameStat gameStat = new GameStat(game);
        FinanceSeasonMoment seasonMoment = getSeasonMoment();

        GameRevenueCalculator revenueCalculator = createRevenueCalculator(gameStat);
        revenueCalculator.calculateGameRevenue(game, date);
        FinanceUtilitary.addGameRevenue(game, gameStat, month, seasonMoment);

        GameExpenseCalculator expenseCalculator = createExpenseCalculator(gameStat);
        expenseCalculator.calculateGameExpenses(game);
        FinanceUtilitary.addGameExpense(game, gameStat, month, seasonMoment);

        gameStats.put(game, gameStat);
    }

    protected abstract GameRevenueCalculator createRevenueCalculator(GameStat gameStat);

    protected abstract GameExpenseCalculator createExpenseCalculator(GameStat gameStat);

    protected abstract FinanceSeasonMoment getSeasonMoment();

    public GameStat getGameStat(Game game) {
        return gameStats.get(game);
    }

}

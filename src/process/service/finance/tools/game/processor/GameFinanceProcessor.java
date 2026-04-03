package process.service.finance.tools.game.processor;

import java.time.LocalDate;
import java.util.HashMap;

import data.finance.GameStat;
import data.sport.setup.Game;
import process.service.finance.tools.game.GameExpenseCalculator;
import process.service.finance.tools.game.GameRevenueCalculator;
import process.utility.FinanceUtilitary;

public abstract class GameFinanceProcessor {
    private HashMap<Game, GameStat> gameStats = new HashMap<Game, GameStat>();

    public final void calculateGame(Game game, LocalDate date, int month) {
        GameStat gameStat = new GameStat(game);

        GameRevenueCalculator revenueCalculator = createRevenueCalculator(gameStat);
        revenueCalculator.calculateGameRevenue(game, date);
        FinanceUtilitary.addGameRevenue(game, gameStat, month);

        GameExpenseCalculator expenseCalculator = createExpenseCalculator(gameStat);
        expenseCalculator.calculateGameExpenses(game);
        FinanceUtilitary.addGameExpense(game, gameStat, month);

        gameStats.put(game, gameStat);
    }

    protected abstract GameRevenueCalculator createRevenueCalculator(GameStat gameStat);

    protected abstract GameExpenseCalculator createExpenseCalculator(GameStat gameStat);

    public GameStat getGameStat(Game game) {
        return gameStats.get(game);
    }

}

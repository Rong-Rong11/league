package process.service.finance.tools;

import java.time.LocalDate;
import java.util.HashMap;

import data.finance.GameStat;
import data.sport.setup.Game;
import process.utility.FinanceUtilitary;

public class GameFinanceProcessor {
    private HashMap<Game, GameStat> gameStats = new HashMap<Game, GameStat>();

    public void calculateGame(Game game, LocalDate date, int month) {
        GameStat gameStat = new GameStat(game);

        GameRevenueSimulator gameRevenueSimulator = new GameRevenueSimulator(gameStat);
        gameRevenueSimulator.calculateGameRevenue(game, date);
        FinanceUtilitary.addGameRevenue(game, gameStat, month);

        GameExpenseSimulator gameExpenseSimulator = new GameExpenseSimulator(gameStat);
        gameExpenseSimulator.calculateGameExpenses(game);
        FinanceUtilitary.addGameExpense(game, gameStat, month);

        gameStats.put(game, gameStat);
    }

    public GameStat getGameStat(Game game) {
        return gameStats.get(game);
    }
}

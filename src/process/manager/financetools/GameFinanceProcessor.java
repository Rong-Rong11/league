/*
 * Decompiled with CFR 0.152.
 */
package process.manager.financetools;

import data.finance.GameStat;
import data.sport.setup.Game;
import java.time.LocalDate;
import java.util.HashMap;
import process.manager.financetools.GameExpenseSimulator;
import process.manager.financetools.GameRevenueSimulator;
import process.utilitary.FinanceUtilitary;

public class GameFinanceProcessor {
    private HashMap<Game, GameStat> gameStats = new HashMap();

    public void calculateGame(Game game, LocalDate localDate, int n) {
        GameStat gameStat = new GameStat(game);
        GameRevenueSimulator gameRevenueSimulator = new GameRevenueSimulator(gameStat);
        gameRevenueSimulator.calculateGameRevenue(game, localDate);
        FinanceUtilitary.addGameRevenue(game, gameStat, n);
        GameExpenseSimulator gameExpenseSimulator = new GameExpenseSimulator(gameStat);
        gameExpenseSimulator.calculateGameExpenses(game);
        FinanceUtilitary.addGameExpense(game, gameStat, n);
        this.gameStats.put(game, gameStat);
    }

    public GameStat getGameStat(Game game) {
        return this.gameStats.get(game);
    }
}

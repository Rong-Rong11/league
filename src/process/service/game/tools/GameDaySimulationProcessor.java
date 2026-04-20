package process.service.game.tools;

import java.time.LocalDate;

import data.calendar.GameDay;
import data.sport.setup.Game;
import process.service.finance.FinanceManager;
import process.simulator.GameSimulator;

public abstract class GameDaySimulationProcessor {

	protected GameSimulator gameSimulator;
	protected FinanceManager financeManager;

	public GameDaySimulationProcessor(GameSimulator gameSimulator, FinanceManager financeManager) {
	  this.gameSimulator = gameSimulator;
	  this.financeManager = financeManager;
	}

	public final void simulateGameDay(GameDay gameDay, LocalDate date, int month) {
	  for (Game game : gameDay.getGames()) {
		 gameSimulator.simulateGame(game);
		 applyFinance(game, date, month);
		 afterGame(game, date);
	  }
	  afterGameDay(gameDay, date, month);
	  gameDay.setSimulated(true);
	}

	protected abstract void applyFinance(Game game, LocalDate date, int month);

	protected abstract void afterGame(Game game, LocalDate date);

	protected abstract void afterGameDay(GameDay gameDay, LocalDate date, int month);
}

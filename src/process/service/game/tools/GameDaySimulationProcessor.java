package process.service.game.tools;

import java.time.LocalDate;

import org.apache.log4j.Logger;

import data.calendar.GameDay;
import data.sport.setup.Game;
import log.LoggerUtility;
import process.service.finance.FinanceManager;
import process.simulator.GameSimulator;

public abstract class GameDaySimulationProcessor {
	private static final Logger logger = LoggerUtility.getLogger(GameDaySimulationProcessor.class, "text");

	protected GameSimulator gameSimulator;
	protected FinanceManager financeManager;

	public GameDaySimulationProcessor(GameSimulator gameSimulator, FinanceManager financeManager) {
		this.gameSimulator = gameSimulator;
		this.financeManager = financeManager;
		logger.debug("Game day simulation processor initialized");
	}

	public final void simulateGameDay(GameDay gameDay, LocalDate date, int month) {
		if (gameDay == null) {
			logger.warn("Skipping game day simulation because game day is null");
			return;
		}
		if (date == null) {
			logger.warn("Skipping game day simulation because date is null");
			return;
		}
		logger.debug("Simulating game day " + date + " with " + gameDay.getGames().size() + " games");
		for (Game game : gameDay.getGames()) {
			if (game == null) {
				logger.warn("Skipping game simulation because game is null");
				continue;
			}
			logger.trace("Simulating game on " + date);
			gameSimulator.simulateGame(game);
			logger.trace("Applying game finance on " + date);
			applyFinance(game, date, month);
			logger.trace("Applying post-game processing on " + date);
			afterGame(game, date);
		}
		logger.trace("Applying post-game-day processing for " + date);
		afterGameDay(gameDay, date, month);
		gameDay.setSimulated(true);
		logger.debug("Game day simulation completed for " + date);
	}

	protected abstract void applyFinance(Game game, LocalDate date, int month);

	protected abstract void afterGame(Game game, LocalDate date);

	protected abstract void afterGameDay(GameDay gameDay, LocalDate date, int month);
}

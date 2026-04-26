package test.usage;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import data.calendar.GameDay;
import data.sport.live.LiveMatchState;
import data.sport.setup.Game;
import process.orchestrator.manager.SimulationManager;
import test.support.TestSupport;

public class TestLiveUsage {

	private SimulationManager simulationManager;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
		simulationManager = new SimulationManager();
		simulationManager.startSeason();
	}

	@Test
	public void shouldLetUserRequestLiveMatchFromCalendarDay() {
		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		GameDay gameDay = simulationManager.getGameDay(firstGameDayDate);
		assertNotNull(gameDay);
		assertFalse(gameDay.getGames().isEmpty());

		Game selectedGame = gameDay.getGames().get(0);
		assertFalse(simulationManager.isLiveMatchAvailable(selectedGame));

		boolean available = simulationManager.makeLiveMatchAvailable(selectedGame, firstGameDayDate);

		assertTrue(available);
		assertTrue(simulationManager.isLiveMatchAvailable(selectedGame));
		assertNotNull(simulationManager.getGameStat(selectedGame));
		assertTrue(selectedGame.isDisplayed());
	}

	@Test
	public void shouldLetUserPlayLiveMatchAfterAvailability() {
		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		Game selectedGame = simulationManager.getGameDay(firstGameDayDate).getGames().get(0);

		assertTrue(simulationManager.makeLiveMatchAvailable(selectedGame, firstGameDayDate));

		simulationManager.setLiveGame(selectedGame);
		simulationManager.startLiveMatch();
		for (int index = 0; index < 8; index++) {
			simulationManager.tickLiveMatch();
		}

		LiveMatchState currentState = simulationManager.getCurrentLiveState();
		assertTrue(simulationManager.isLiveMatchRunning());
		assertTrue(currentState.getHomePoints() >= 0);
		assertTrue(currentState.getAwayPoints() >= 0);
	}
}

package test.usage;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import data.sport.live.LiveMatchState;
import data.sport.setup.Game;
import process.orchestrator.manager.SimulationManager;
import test.support.TestSupport;

public class TestAdvancedLiveUsage {

	private SimulationManager simulationManager;
	private Game selectedGame;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
		simulationManager = new SimulationManager();
		simulationManager.startSeason();

		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		selectedGame = simulationManager.getGameDay(firstGameDayDate).getGames().get(0);
		assertTrue(simulationManager.makeLiveMatchAvailable(selectedGame, firstGameDayDate));
		simulationManager.setLiveGame(selectedGame);
	}

	@Test
	public void shouldLetUserPauseAndResumeLiveMatch() {
		simulationManager.startLiveMatch();
		assertTrue(simulationManager.isLiveMatchRunning());

		simulationManager.pauseLiveMatch();
		assertFalse(simulationManager.isLiveMatchRunning());

		simulationManager.startLiveMatch();
		assertTrue(simulationManager.isLiveMatchRunning());
	}

	@Test
	public void shouldLetUserPlayCurrentQuarterAndResetLiveMatch() {
		simulationManager.playCurrentLiveQuarter();
		LiveMatchState afterQuarter = simulationManager.getCurrentLiveState();
		assertTrue(afterQuarter.getHomePoints() >= 0);
		assertTrue(afterQuarter.getAwayPoints() >= 0);

		simulationManager.resetLiveMatch();
		LiveMatchState resetState = simulationManager.getCurrentLiveState();
		assertFalse(simulationManager.isLiveMatchRunning());
		assertEquals(0, resetState.getHomePoints());
		assertEquals(0, resetState.getAwayPoints());
	}
}

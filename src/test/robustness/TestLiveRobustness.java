package test.robustness;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import data.sport.live.LiveMatchState;
import data.sport.setup.Game;
import process.orchestrator.manager.SimulationManager;
import test.support.TestSupport;

public class TestLiveRobustness {

	private SimulationManager simulationManager;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
		simulationManager = new SimulationManager();
	}

	@Test
	public void shouldHandleLiveCommandsSafelyWithoutSelectedGame() {
		simulationManager.startLiveMatch();
		simulationManager.tickLiveMatch();
		simulationManager.pauseLiveMatch();
		simulationManager.playCurrentLiveQuarter();
		simulationManager.resetLiveMatch();

		LiveMatchState state = simulationManager.getCurrentLiveState();
		assertNotNull(state);
		assertFalse(simulationManager.isLiveMatchRunning());
		assertEquals("Q-", state.getQuarterLabel());
		assertEquals("--:--", state.getQuarterTimeText());
	}

	@Test
	public void shouldRejectLiveAvailabilityWhenGameOrDateIsMissing() {
		assertFalse(simulationManager.makeLiveMatchAvailable(null, LocalDate.of(2025, 10, 21)));

		Game unsimulatedGame = TestSupport.createInterConferenceGame(simulationManager.getTeams().get(0),
				simulationManager.getTeams().get(1));
		assertFalse(simulationManager.makeLiveMatchAvailable(unsimulatedGame, null));
		assertFalse(simulationManager.makeLiveMatchAvailable(unsimulatedGame, LocalDate.of(2025, 10, 21)));
	}

	@Test
	public void shouldIgnoreRepeatedPauseResetAndRestartOnLiveMatch() {
		simulationManager.startSeason();
		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		Game selectedGame = simulationManager.getGameDay(firstGameDayDate).getGames().get(0);
		assertTrue(simulationManager.makeLiveMatchAvailable(selectedGame, firstGameDayDate));

		simulationManager.setLiveGame(selectedGame);
		simulationManager.startLiveMatch();
		simulationManager.pauseLiveMatch();
		simulationManager.pauseLiveMatch();
		simulationManager.resetLiveMatch();
		simulationManager.resetLiveMatch();
		simulationManager.startLiveMatch();
		simulationManager.startLiveMatch();

		LiveMatchState state = simulationManager.getCurrentLiveState();
		assertNotNull(state);
		assertTrue(state.getHomePoints() >= 0);
		assertTrue(state.getAwayPoints() >= 0);
	}

	@Test
	public void shouldStayStableWhenSwitchingBetweenUnavailableAndAvailableGames() {
		simulationManager.startSeason();

		Game unavailableGame = TestSupport.createInterConferenceGame(simulationManager.getTeams().get(0),
				simulationManager.getTeams().get(1));
		simulationManager.setLiveGame(unavailableGame);
		LiveMatchState unavailableState = simulationManager.getCurrentLiveState();
		assertEquals("Match non disponible.", unavailableState.getCenterMessage());

		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		Game availableGame = simulationManager.getGameDay(firstGameDayDate).getGames().get(0);
		assertTrue(simulationManager.makeLiveMatchAvailable(availableGame, firstGameDayDate));

		simulationManager.setLiveGame(availableGame);
		LiveMatchState availableState = simulationManager.getCurrentLiveState();
		assertNotNull(availableState);
		assertEquals("Clique sur Play pour lancer le match.", availableState.getCenterMessage());
	}

	@Test
	public void shouldHandleNullGameSelectionAfterUsingLiveMatch() {
		simulationManager.startSeason();
		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		Game availableGame = simulationManager.getGameDay(firstGameDayDate).getGames().get(0);
		assertTrue(simulationManager.makeLiveMatchAvailable(availableGame, firstGameDayDate));

		simulationManager.setLiveGame(availableGame);
		simulationManager.startLiveMatch();
		simulationManager.setLiveGame(null);

		LiveMatchState state = simulationManager.getCurrentLiveState();
		assertFalse(simulationManager.isLiveMatchRunning());
		assertEquals("Aucun match selectionne.", state.getCenterMessage());
		assertEquals("Q-", state.getQuarterLabel());
	}

	@Test
	public void shouldRemainStableAfterFinishingThenRestartingLivePlayback() {
		simulationManager.startSeason();
		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		Game availableGame = simulationManager.getGameDay(firstGameDayDate).getGames().get(0);
		assertTrue(simulationManager.makeLiveMatchAvailable(availableGame, firstGameDayDate));

		simulationManager.setLiveGame(availableGame);
		simulationManager.playCurrentLiveQuarter();
		simulationManager.playCurrentLiveQuarter();
		simulationManager.playCurrentLiveQuarter();
		simulationManager.playCurrentLiveQuarter();

		assertFalse(simulationManager.isLiveMatchRunning());
		assertEquals("FIN", simulationManager.getCurrentLiveState().getQuarterLabel());

		simulationManager.startLiveMatch();
		assertTrue(simulationManager.isLiveMatchRunning());

		simulationManager.resetLiveMatch();
		assertFalse(simulationManager.isLiveMatchRunning());
		assertEquals("Q1", simulationManager.getCurrentLiveState().getQuarterLabel());
	}

	@Test
	public void shouldStayStableAfterManyLiveTicksWhenPausedOrFinished() {
		simulationManager.startSeason();
		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		Game availableGame = simulationManager.getGameDay(firstGameDayDate).getGames().get(0);
		assertTrue(simulationManager.makeLiveMatchAvailable(availableGame, firstGameDayDate));

		simulationManager.setLiveGame(availableGame);
		simulationManager.startLiveMatch();
		for (int index = 0; index < 50; index++) {
			simulationManager.tickLiveMatch();
		}

		simulationManager.pauseLiveMatch();
		for (int index = 0; index < 20; index++) {
			simulationManager.tickLiveMatch();
		}

		LiveMatchState state = simulationManager.getCurrentLiveState();
		assertNotNull(state);
		assertTrue(state.getQuarterLabel() != null && !state.getQuarterLabel().isEmpty());
	}

	@Test
	public void shouldSupportSwitchingLiveGamesMultipleTimes() {
		simulationManager.startSeason();
		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		Game firstGame = simulationManager.getGameDay(firstGameDayDate).getGames().get(0);
		assertTrue(simulationManager.makeLiveMatchAvailable(firstGame, firstGameDayDate));

		LocalDate secondGameDayDate = simulationManager.getNextGameDay(firstGameDayDate.plusDays(1));
		assertNotNull(secondGameDayDate);
		Game secondGame = simulationManager.getGameDay(secondGameDayDate).getGames().get(0);
		assertTrue(simulationManager.makeLiveMatchAvailable(secondGame, secondGameDayDate));

		simulationManager.setLiveGame(firstGame);
		simulationManager.startLiveMatch();
		simulationManager.setLiveGame(secondGame);
		simulationManager.startLiveMatch();
		simulationManager.setLiveGame(firstGame);

		LiveMatchState state = simulationManager.getCurrentLiveState();
		assertNotNull(state);
		assertNotNull(state.getHomeTeam());
		assertNotNull(state.getAwayTeam());
	}
}

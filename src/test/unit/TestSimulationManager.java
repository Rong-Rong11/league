package test.unit;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.calendar.GameDay;
import data.sport.live.LiveMatchState;
import data.sport.setup.Game;
import data.team.Team;
import process.orchestrator.manager.SimulationManager;
import process.repository.DivisionRepository;
import process.repository.PlayerRepository;
import process.repository.TeamRepository;
import test.support.TestSupport;

public class TestSimulationManager {

	private SimulationManager simulationManager;
	private Team homeTeam;
	private Team awayTeam;

	@Before
	public void setUp() {
		PlayerRepository.getInstance().clear();
		TeamRepository.getInstance().clear();
		DivisionRepository.getInstance().clear();
		simulationManager = new SimulationManager();
		ArrayList<Team> teams = simulationManager.getTeams();
		homeTeam = teams.get(0);
		awayTeam = teams.get(1);
	}

	@Test
	public void shouldExposeUninitializedSeasonSafely() {
		assertFalse(simulationManager.isSeasonInitialized());
		assertNull(simulationManager.getGameDay(LocalDate.now()));
		assertTrue(simulationManager.getSeasonCalendar().isEmpty());
		assertNull(simulationManager.getCalendarDisplayDate(LocalDate.now()));
	}

	@Test
	public void shouldInitializeSeasonAndCalendarOnStartSeason() {
		simulationManager.startSeason();

		assertTrue(simulationManager.isSeasonInitialized());
		assertFalse(simulationManager.getSeasonCalendar().isEmpty());
		assertEquals(simulationManager.getRegularSeasonStartDate(), simulationManager.getCurrentDate());
	}

	@Test
	public void shouldFindNextAndPreviousGameDaysInCustomCalendar() {
		LocalDate firstDate = LocalDate.of(2025, 10, 21);
		LocalDate secondDate = LocalDate.of(2025, 10, 24);
		GameDay firstGameDay = new GameDay(firstDate);
		firstGameDay.addGame(TestSupport.createInterConferenceGame(homeTeam, awayTeam));
		GameDay secondGameDay = new GameDay(secondDate);
		secondGameDay.addGame(TestSupport.createInterConferenceGame(awayTeam, homeTeam));
		TestSupport.setRegularSeasonCalendar(simulationManager.getLeague(), firstGameDay, secondGameDay);

		assertTrue(simulationManager.isSeasonInitialized());
		assertEquals(firstDate, simulationManager.getNextGameDay(firstDate));
		assertEquals(secondDate, simulationManager.getNextGameDay(firstDate.plusDays(1)));
		assertEquals(firstDate, simulationManager.getPreviousGameDay(secondDate.minusDays(1)));
		assertEquals(secondDate, simulationManager.getPreviousGameDay(secondDate));
	}

	@Test
	public void shouldReturnDisplayDateBasedOnCurrentDisplayedState() {
		LocalDate firstDate = LocalDate.of(2025, 10, 21);
		LocalDate secondDate = LocalDate.of(2025, 10, 24);
		GameDay firstGameDay = new GameDay(firstDate);
		firstGameDay.addGame(TestSupport.createInterConferenceGame(homeTeam, awayTeam));
		GameDay secondGameDay = new GameDay(secondDate);
		secondGameDay.addGame(TestSupport.createInterConferenceGame(awayTeam, homeTeam));
		TestSupport.setRegularSeasonCalendar(simulationManager.getLeague(), firstGameDay, secondGameDay);

		assertEquals(firstDate, simulationManager.getCalendarDisplayDate(firstDate));

		simulationManager.displayGameDay(firstDate);

		assertEquals(secondDate, simulationManager.getCalendarDisplayDate(firstDate));
	}

	@Test
	public void shouldDisplayGameDayAndWeek() {
		LocalDate firstDate = LocalDate.of(2025, 10, 21);
		LocalDate secondDate = LocalDate.of(2025, 10, 24);
		Game firstGame = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		Game secondGame = TestSupport.createInterConferenceGame(awayTeam, homeTeam);
		GameDay firstGameDay = new GameDay(firstDate);
		firstGameDay.addGame(firstGame);
		GameDay secondGameDay = new GameDay(secondDate);
		secondGameDay.addGame(secondGame);
		TestSupport.setRegularSeasonCalendar(simulationManager.getLeague(), firstGameDay, secondGameDay);

		simulationManager.displayGameDay(firstDate);
		simulationManager.displayWeek(firstDate);

		assertTrue(firstGameDay.isDisplayed());
		assertTrue(secondGameDay.isDisplayed());
		assertTrue(firstGame.isDisplayed());
		assertTrue(secondGame.isDisplayed());
	}

	@Test
	public void shouldHandleLiveMatchAvailabilityChecks() {
		assertFalse(simulationManager.makeLiveMatchAvailable(null, LocalDate.of(2025, 10, 21)));
		assertFalse(
				simulationManager.makeLiveMatchAvailable(TestSupport.createInterConferenceGame(homeTeam, awayTeam), null));

		Game simulatedGame = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		TestSupport.markGameAsSimulated(simulatedGame);

		assertTrue(simulationManager.makeLiveMatchAvailable(simulatedGame, LocalDate.of(2025, 10, 21)));
	}

	@Test
	public void shouldExposeCurrentLiveState() {
		LiveMatchState state = simulationManager.getCurrentLiveState();

		assertNotNull(state);
		assertEquals("Q-", state.getQuarterLabel());
		assertEquals("--:--", state.getQuarterTimeText());
	}
}

package test.robustness;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import data.calendar.GameDay;
import data.team.Team;
import process.orchestrator.manager.SimulationManager;
import test.support.TestSupport;

public class TestSimulationRobustness {

	private SimulationManager simulationManager;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
		simulationManager = new SimulationManager();
	}

	@Test
	public void shouldIgnoreSimulationCallsSafelyBeforeSeasonStart() {
		simulationManager.simulateAndDisplayDay(LocalDate.of(2025, 10, 21));
		simulationManager.simulateWeek(LocalDate.of(2025, 10, 21));
		simulationManager.simulateSeasonFrom(LocalDate.of(2025, 12, 1));
		simulationManager.displayGameDay(LocalDate.of(2025, 10, 21));
		simulationManager.displayWeek(LocalDate.of(2025, 10, 21));
		simulationManager.displayCurrentSeason();

		assertFalse(simulationManager.isSeasonInitialized());
		assertTrue(simulationManager.getSeasonCalendar().isEmpty());
		assertNull(simulationManager.getGameDay(LocalDate.of(2025, 10, 21)));
	}

	@Test
	public void shouldHandleNullInputsSafelyAcrossNavigationMethods() {
		simulationManager.startSeason();

		assertNull(simulationManager.getCalendarDisplayDate(null));
		assertNull(simulationManager.getNextGameDay(null));
		assertNull(simulationManager.getPreviousGameDay(null));
		assertNull(simulationManager.getWeekStartDate(null));
		assertNull(simulationManager.getWeekDisplayDate(null));
		assertNull(simulationManager.getDisplayedDateAfterDaySimulation(null));
		assertNull(simulationManager.getDisplayedDateAfterWeekSimulation(null));
		assertNull(simulationManager.getPreviousWeekDisplayDate(null));
		assertNull(simulationManager.getNextWeekDisplayDate(null));
		assertNull(simulationManager.getGameDay(null));
		assertEquals("Semaine -", simulationManager.getWeekText(null));
	}

	@Test
	public void shouldRemainConsistentWhenStartingSeasonMultipleTimes() {
		simulationManager.startSeason();
		LocalDate firstStartDate = simulationManager.getCurrentDate();
		int firstCalendarSize = simulationManager.getSeasonCalendar().size();

		simulationManager.simulateWeek(simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate()));
		simulationManager.startSeason();

		assertTrue(simulationManager.isSeasonInitialized());
		assertEquals(simulationManager.getRegularSeasonStartDate(), simulationManager.getCurrentDate());
		assertEquals(firstStartDate, simulationManager.getCurrentDate());
		assertEquals(firstCalendarSize, simulationManager.getSeasonCalendar().size());
	}

	@Test
	public void shouldTolerateRepeatedDisplayAndSimulationOnSameDay() {
		simulationManager.startSeason();
		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		assertNotNull(firstGameDayDate);

		simulationManager.simulateAndDisplayDay(firstGameDayDate);
		simulationManager.simulateAndDisplayDay(firstGameDayDate);
		simulationManager.displayGameDay(firstGameDayDate);
		simulationManager.displayGameDay(firstGameDayDate);

		GameDay gameDay = simulationManager.getGameDay(firstGameDayDate);
		assertNotNull(gameDay);
		assertTrue(gameDay.isSimulated());
		assertTrue(gameDay.isDisplayed());
	}

	@Test
	public void shouldReturnSafeLabelsWhenTeamOrConfigurationIsMissing() {
		Team team = simulationManager.getTeams().get(0);

		assertEquals("-", simulationManager.getTeamFinancialPolicyLabel(null));
		assertEquals("-", simulationManager.getTeamMarketSizeLabel(null));
		assertNotNull(simulationManager.getTeamFinancialPolicyLabel(team));
		assertNotNull(simulationManager.getTeamMarketSizeLabel(team));
		assertFalse(simulationManager.getTeamFinancialPolicyLabel(team).isEmpty());
		assertFalse(simulationManager.getTeamMarketSizeLabel(team).isEmpty());
	}

	@Test
	public void shouldHandleOutOfBoundsDatesSafelyAfterSeasonStart() {
		simulationManager.startSeason();

		LocalDate beforeSeason = simulationManager.getRegularSeasonStartDate().minusDays(20);
		LocalDate afterSeason = simulationManager.getRegularSeasonEndDate().plusDays(20);

		assertNotNull(simulationManager.getWeekDisplayDate(beforeSeason));
		assertEquals(beforeSeason, simulationManager.getPreviousWeekDisplayDate(beforeSeason));
		assertEquals(afterSeason, simulationManager.getNextWeekDisplayDate(afterSeason));
		assertNull(simulationManager.getNextGameDay(afterSeason));
		assertNull(simulationManager.getPreviousGameDay(beforeSeason));
		assertNull(simulationManager.getGameDay(afterSeason));
	}

	@Test
	public void shouldTolerateRepeatedRegularSeasonEndingCalls() {
		simulationManager.startSeason();
		simulationManager.simulateRegularSeason();

		int firstEastQualifiedTeams = simulationManager.getLeague().getPlayoff().getQualifiedEastTeams().size();
		int firstWestQualifiedTeams = simulationManager.getLeague().getPlayoff().getQualifiedWestTeams().size();

		simulationManager.endRegularSeason();
		simulationManager.endRegularSeason();

		assertEquals(firstEastQualifiedTeams, simulationManager.getLeague().getPlayoff().getQualifiedEastTeams().size());
		assertEquals(firstWestQualifiedTeams, simulationManager.getLeague().getPlayoff().getQualifiedWestTeams().size());
		assertNotNull(simulationManager.getLeague().getPlayoff().getCurrentRound());
	}

	@Test
	public void shouldIgnoreNullDisplayCallsEvenAfterSeasonStart() {
		simulationManager.startSeason();
		int calendarSize = simulationManager.getSeasonCalendar().size();

		simulationManager.displayGameDay(null);
		simulationManager.displayWeek(null);
		simulationManager.simulateAndDisplayDay(null);
		simulationManager.simulateSeasonFrom(null);

		assertEquals(calendarSize, simulationManager.getSeasonCalendar().size());
		assertTrue(simulationManager.isSeasonInitialized());
	}

	@Test
	public void shouldStayConsistentAfterLongMixedSimulationSequence() {
		simulationManager.startSeason();
		LocalDate firstGameDay = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		assertNotNull(firstGameDay);

		simulationManager.displayWeek(firstGameDay);
		simulationManager.simulateWeek(firstGameDay);
		simulationManager.simulateAndDisplayDay(firstGameDay);
		simulationManager.simulateSeasonFrom(firstGameDay.plusDays(25));
		simulationManager.displayCurrentSeason();

		assertTrue(simulationManager.isSeasonInitialized());
		assertFalse(simulationManager.getSeasonCalendar().isEmpty());
		assertNotNull(simulationManager.getCurrentDate());
		assertNotNull(simulationManager.getCurrentWeekIndicatorDate());
		assertNotNull(simulationManager.getMatchDisplayDate());
	}

	@Test
	public void shouldHandleRepeatedDisplayCallsAcrossEntireSeason() {
		simulationManager.startSeason();

		for (int index = 0; index < 10; index++) {
			simulationManager.displayCurrentSeason();
		}

		for (GameDay gameDay : simulationManager.getSeasonCalendar().values()) {
			assertTrue(gameDay.isDisplayed());
		}
	}
}

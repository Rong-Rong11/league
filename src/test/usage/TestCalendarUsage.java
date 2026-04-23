package test.usage;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import data.calendar.GameDay;
import process.orchestrator.manager.SimulationManager;
import test.support.TestSupport;

public class TestCalendarUsage {

	private SimulationManager simulationManager;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
		simulationManager = new SimulationManager();
		simulationManager.startSeason();
	}

	@Test
	public void shouldLetUserNavigateBetweenGameDaysAndWeeks() {
		LocalDate firstGameDay = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		assertNotNull(firstGameDay);

		LocalDate weekStart = simulationManager.getWeekStartDate(firstGameDay);
		LocalDate weekDisplayDate = simulationManager.getWeekDisplayDate(weekStart);

		assertNotNull(weekStart);
		assertNotNull(weekDisplayDate);
		assertNotNull(simulationManager.getWeekText(firstGameDay));

		LocalDate nextWeekDisplayDate = simulationManager.getNextWeekDisplayDate(firstGameDay);
		assertNotNull(nextWeekDisplayDate);
		assertTrue(!nextWeekDisplayDate.isBefore(firstGameDay));

		LocalDate previousGameDay = simulationManager.getPreviousGameDay(firstGameDay);
		assertEquals(firstGameDay, previousGameDay);
	}

	@Test
	public void shouldLetUserDisplaySeasonContentForBrowsing() {
		LocalDate firstGameDay = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		assertNotNull(firstGameDay);

		simulationManager.displayGameDay(firstGameDay);
		GameDay displayedDay = simulationManager.getGameDay(firstGameDay);
		assertTrue(displayedDay.isDisplayed());

		simulationManager.displayWeek(firstGameDay);
		assertTrue(displayedDay.isDisplayed());

		simulationManager.displayCurrentSeason();
		for (GameDay gameDay : simulationManager.getSeasonCalendar().values()) {
			assertTrue(gameDay.isDisplayed());
		}
	}
}

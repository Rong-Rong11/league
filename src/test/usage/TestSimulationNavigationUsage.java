package test.usage;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import process.orchestrator.manager.SimulationManager;
import test.support.TestSupport;

public class TestSimulationNavigationUsage {

	private SimulationManager simulationManager;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
		simulationManager = new SimulationManager();
		simulationManager.startSeason();
	}

	@Test
	public void shouldLetUserComputeDisplayedDatesAfterDifferentSimulationActions() {
		LocalDate firstGameDay = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		assertNotNull(firstGameDay);

		simulationManager.simulateAndDisplayDay(firstGameDay);
		LocalDate afterDay = simulationManager.getDisplayedDateAfterDaySimulation(firstGameDay);
		assertNotNull(afterDay);

		simulationManager.simulateWeek(firstGameDay);
		LocalDate afterWeek = simulationManager.getDisplayedDateAfterWeekSimulation(firstGameDay);
		assertNotNull(afterWeek);

		simulationManager.simulateSeasonFrom(firstGameDay.plusDays(30));
		LocalDate afterSeason = simulationManager.getDisplayedDateAfterSeasonSimulation(firstGameDay);
		assertNotNull(afterSeason);
	}

	@Test
	public void shouldLetUserResumeSeasonFromMidPoint() {
		LocalDate targetDate = simulationManager.getNextGameDay(
				simulationManager.getRegularSeasonStartDate().plusDays(40));
		assertNotNull(targetDate);

		simulationManager.simulateSeasonFrom(targetDate);

		assertTrue(!simulationManager.getCurrentDate().isBefore(targetDate));
		assertNotNull(simulationManager.getCalendarDisplayDate(simulationManager.getCurrentDate()));
		assertNotNull(simulationManager.getMatchDisplayDate());
	}
}

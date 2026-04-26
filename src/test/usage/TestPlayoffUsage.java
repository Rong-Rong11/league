package test.usage;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import config.CalendarConfiguration;
import data.sport.setup.PlayoffSeries;
import process.orchestrator.manager.SimulationManager;
import test.support.TestSupport;

public class TestPlayoffUsage {

	private SimulationManager simulationManager;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
		simulationManager = new SimulationManager();
		simulationManager.startSeason();
		simulationManager.simulateRegularSeason();
	}

	@Test
	public void shouldLetUserBrowsePlayoffStateAfterRegularSeason() {
		assertNotNull(simulationManager.getLeague().getPlayoff());
		assertNotNull(simulationManager.getLeague().getPlayoff().getCurrentRound());
		assertFalse(simulationManager.getLeague().getPlayoff().getNbaCalendar().getCalendar().isEmpty());
		assertEquals(4, simulationManager.getLeague().getPlayoff().getEastFirstRound().size());
		assertEquals(4, simulationManager.getLeague().getPlayoff().getWestFirstRound().size());
	}

	@Test
	public void shouldLetUserCompletePlayoffJourney() {
		for (LocalDate date = CalendarConfiguration.PLAYOFF_DEBUT_DATE;
				!date.isAfter(CalendarConfiguration.PLAYOFF_END_DATE);
				date = date.plusDays(1)) {
			simulationManager.simulateDay(date);
		}

		assertFalse(simulationManager.getLeague().getPlayoff().getNbaFinals().isEmpty());
		PlayoffSeries finals = simulationManager.getLeague().getPlayoff().getNbaFinals().get(0);
		assertTrue(finals.isFinished());
		assertTrue(simulationManager.arePlayoffsFinished());
		assertNotNull(simulationManager.getLeague().getPlayoff().getChampion());
		assertFalse(simulationManager.getPlayoffChampionName().equals(""));
	}
}

package test.usage;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import data.calendar.GameDay;
import data.league.PlayoffRound;
import data.team.Team;
import process.orchestrator.manager.SimulationManager;
import test.support.TestSupport;

public class TestSeasonUsage {

	private SimulationManager simulationManager;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
		simulationManager = new SimulationManager();
	}

	@Test
	public void shouldLetUserConfigureTeamAndStartSeason() {
		Team selectedTeam = simulationManager.getTeams().get(0);

		simulationManager.chooseAmbitiousPolicy(selectedTeam);
		simulationManager.chooseLargeMarketSize(selectedTeam);
		simulationManager.startSeason();

		assertTrue(simulationManager.isSeasonInitialized());
		assertEquals("Ambitieuse", simulationManager.getTeamFinancialPolicyLabel(selectedTeam));
		assertEquals("Grand", simulationManager.getTeamMarketSizeLabel(selectedTeam));
		assertEquals(simulationManager.getRegularSeasonStartDate(), simulationManager.getCurrentDate());
		assertFalse(simulationManager.getSeasonCalendar().isEmpty());
	}

	@Test
	public void shouldLetUserSimulateAndDisplayAGameDayThenAWeek() {
		simulationManager.startSeason();
		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());

		simulationManager.simulateAndDisplayDay(firstGameDayDate);

		GameDay displayedDay = simulationManager.getGameDay(firstGameDayDate);
		assertNotNull(displayedDay);
		assertTrue(displayedDay.isSimulated());
		assertTrue(displayedDay.isDisplayed());
		assertFalse(displayedDay.getGames().isEmpty());
		assertTrue(displayedDay.getGames().get(0).isDisplayed());

		simulationManager.simulateWeek(firstGameDayDate);

		assertNotNull(simulationManager.getCalendarDisplayDate(simulationManager.getCurrentDate()));
		assertNotNull(simulationManager.getMatchDisplayDate());
	}

	@Test
	public void shouldLetUserReachPlayoffsBySimulatingRegularSeason() {
		simulationManager.startSeason();

		simulationManager.simulateRegularSeason();

		assertEquals(PlayoffRound.FIRST_ROUND, simulationManager.getLeague().getPlayoff().getCurrentRound());
		assertEquals(8, simulationManager.getLeague().getPlayoff().getQualifiedEastTeams().size());
		assertEquals(8, simulationManager.getLeague().getPlayoff().getQualifiedWestTeams().size());
		assertFalse(simulationManager.getLeague().getPlayoff().getNbaCalendar().getCalendar().isEmpty());
	}
}

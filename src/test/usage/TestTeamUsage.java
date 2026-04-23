package test.usage;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.calendar.GameDay;
import data.sport.setup.Game;
import data.team.Team;
import process.orchestrator.manager.SimulationManager;
import test.support.TestSupport;

public class TestTeamUsage {

	private SimulationManager simulationManager;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
		simulationManager = new SimulationManager();
	}

	@Test
	public void shouldLetUserFindTeamAndConsultBasicIdentityData() {
		Team selectedTeam = simulationManager.getTeams().get(0);
		Team foundTeam = simulationManager.getTeamByName(selectedTeam.getName());

		assertNotNull(foundTeam);
		assertEquals(selectedTeam.getName(), foundTeam.getName());
		assertNotNull(simulationManager.getConferenceName(foundTeam));
		assertNotNull(simulationManager.getDivisionName(foundTeam));
		assertNotNull(simulationManager.getTeamAbbreviation(foundTeam.getName()));
	}

	@Test
	public void shouldLetUserConsultRankingAndTeamStatsAfterSimulatedGames() {
		simulationManager.startSeason();
		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		simulationManager.simulateWeek(firstGameDayDate);

		ArrayList<Team> globalRanking = simulationManager.getGlobalRanking();
		ArrayList<Team> eastRanking = simulationManager.getEastRanking();
		ArrayList<Team> westRanking = simulationManager.getWestRanking();

		assertEquals(30, globalRanking.size());
		assertEquals(15, eastRanking.size());
		assertEquals(15, westRanking.size());

		Team rankedTeam = globalRanking.get(0);
		assertTrue(simulationManager.getTeamNumberPlayedGames(rankedTeam) >= 0);
		assertTrue(simulationManager.getTeamNumberWin(rankedTeam) >= 0);
		assertTrue(simulationManager.getTeamNumberLose(rankedTeam) >= 0);
		assertTrue(simulationManager.getTeamCurrentPayroll(rankedTeam) > 0);
	}

	@Test
	public void shouldLetUserConsultMatchFinanceAfterSimulatingADay() {
		simulationManager.startSeason();
		LocalDate firstGameDayDate = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		simulationManager.simulateAndDisplayDay(firstGameDayDate);

		GameDay gameDay = simulationManager.getGameDay(firstGameDayDate);
		assertNotNull(gameDay);
		assertFalse(gameDay.getGames().isEmpty());

		Game selectedGame = gameDay.getGames().get(0);
		assertNotNull(simulationManager.getGameStat(selectedGame));
		assertTrue(selectedGame.isDisplayed());
	}
}

package test.usage;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.team.Team;
import process.orchestrator.manager.SimulationManager;
import test.support.TestSupport;

public class TestTeamInsightsUsage {

	private SimulationManager simulationManager;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
		simulationManager = new SimulationManager();
		simulationManager.startSeason();
	}

	@Test
	public void shouldLetUserInspectCurrentSeasonTeamIndicators() {
		LocalDate firstGameDay = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		simulationManager.simulateWeek(firstGameDay);

		Team team = simulationManager.getTeams().get(0);

		assertTrue(simulationManager.getAverageNote(team) >= 0.0);
		assertTrue(simulationManager.getAveragePoints(team, true) >= 0.0);
		assertTrue(simulationManager.getAverageRebounds(team, true) >= 0.0);
		assertTrue(simulationManager.getAverageAssists(team, true) >= 0.0);
		assertTrue(simulationManager.getTeamCurrentWinStreak(team) >= 0);
		assertTrue(simulationManager.getTeamCurrentLoseStreak(team) >= 0);
		assertTrue(simulationManager.getTeamMaxWinStreak(team) >= 0);
		assertTrue(simulationManager.getTeamMaxLoseStreak(team) >= 0);
	}

	@Test
	public void shouldLetUserInspectRecentResultsAndStandings() {
		LocalDate firstGameDay = simulationManager.getNextGameDay(simulationManager.getRegularSeasonStartDate());
		simulationManager.simulateWeek(firstGameDay);

		ArrayList<Team> globalRanking = simulationManager.getGlobalRanking();
		Team rankedTeam = globalRanking.get(0);
		ArrayList<Boolean> lastResults = simulationManager.getTeamLastGamesResults(rankedTeam, 5);

		assertNotNull(lastResults);
		assertTrue(lastResults.size() <= 5);
		assertTrue(simulationManager.getTeamNumberPlayedGames(rankedTeam) >= lastResults.size());
	}
}

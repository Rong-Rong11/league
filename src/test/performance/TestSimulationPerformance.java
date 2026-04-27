package test.performance;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import process.orchestrator.manager.SimulationManager;
import process.simulator.GameSimulator;
import test.support.TestSupport;

public class TestSimulationPerformance {

	private static final double BUILD_FINANCE_MAX_MS = 500.0;
	private static final double SINGLE_GAME_MAX_MS = 200.0;
	private static final double HUNDRED_GAMES_MAX_MS = 4000.0;
	private static final double START_SEASON_MAX_MS = 5000.0;
	private static final double SIMULATE_WEEK_MAX_MS = 1500.0;
	private static final double MID_SEASON_MAX_MS = 2000.0;
	private static final double REGULAR_SEASON_MAX_MS = 2000.0;
	private static final double FULL_SEASON_MAX_MS = 8000.0;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
	}

	@Test
	public void shouldBuildLeagueAndFinanceQuickly() {
		long start = System.nanoTime();
		League league = TestSupport.buildLeagueWithFinance();
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertTrue(league.getAllTeam().size() > 0);

		TestSupport.assertBelow("build+finance", elapsedMs, BUILD_FINANCE_MAX_MS);
	}

	@Test
	public void shouldSimulateSingleGameQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		Game game = TestSupport.createInterConferenceGame(league, 0, 1);

		long start = System.nanoTime();
		new GameSimulator().simulateGame(game);
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		TestSupport.assertBelow("single game", elapsedMs, SINGLE_GAME_MAX_MS);
	}

	@Test
	public void shouldSimulateHundredGamesQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		GameSimulator gameSimulator = new GameSimulator();

		long start = System.nanoTime();
		for (int index = 0; index < 100; index++) {
			Game game = TestSupport.createInterConferenceGame(league, index % 10, (index % 10) + 1);
			gameSimulator.simulateGame(game);
		}
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		TestSupport.assertBelow("hundred games", elapsedMs, HUNDRED_GAMES_MAX_MS);
	}

	@Test
	public void shouldStartSeasonQuickly() {
		TestSupport.clearRepositories();
		SimulationManager simulationManager = new SimulationManager();

		long start = System.nanoTime();
		simulationManager.startSeason();
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertTrue(simulationManager.isSeasonInitialized());

		TestSupport.assertBelow("startSeason", elapsedMs, START_SEASON_MAX_MS);
	}

	@Test
	public void shouldSimulateWeekQuickly() {
		TestSupport.clearRepositories();
		SimulationManager simulationManager = new SimulationManager();
		simulationManager.startSeason();
		java.time.LocalDate firstGameDay = simulationManager
				.getNextGameDay(simulationManager.getRegularSeasonStartDate());

		long start = System.nanoTime();
		simulationManager.simulateWeek(firstGameDay);
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		TestSupport.assertBelow("simulateWeek", elapsedMs, SIMULATE_WEEK_MAX_MS);
	}

	@Test
	public void shouldSimulateSeasonFromMidSeasonQuickly() {
		TestSupport.clearRepositories();
		SimulationManager simulationManager = new SimulationManager();
		simulationManager.startSeason();
		java.time.LocalDate midSeasonDate = simulationManager
				.getNextGameDay(simulationManager.getRegularSeasonStartDate().plusDays(70));

		long start = System.nanoTime();
		simulationManager.simulateSeasonFrom(midSeasonDate);
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		TestSupport.assertBelow("simulateSeasonFromMid", elapsedMs, MID_SEASON_MAX_MS);
	}

	@Test
	public void shouldSimulateRegularSeasonQuickly() {
		TestSupport.clearRepositories();
		SimulationManager simulationManager = new SimulationManager();
		simulationManager.startSeason();

		long start = System.nanoTime();
		simulationManager.simulateRegularSeason();
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		TestSupport.assertBelow("simulateRegularSeason", elapsedMs, REGULAR_SEASON_MAX_MS);
	}

	@Test
	public void shouldSimulateFullSeasonAndPlayoffsQuickly() {
		TestSupport.clearRepositories();
		SimulationManager simulationManager = new SimulationManager();
		simulationManager.startSeason();

		long start = System.nanoTime();

		simulationManager.simulateRegularSeason();

		int safety = 0;
		while (!simulationManager.arePlayoffsFinished() && safety < 10) {
			simulationManager.simulateNextPlayoffRound();
			safety++;
		}

		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertTrue(simulationManager.hasPlayoffsStarted());
		assertFalse(simulationManager.getLeague().getPlayoff().getNbaFinals().isEmpty());

		PlayoffSeries finals = simulationManager.getLeague().getPlayoff().getNbaFinals().get(0);
		assertTrue(finals.isFinished());
		assertTrue(simulationManager.arePlayoffsFinished());

		TestSupport.assertBelow("fullSeasonAndPlayoffs", elapsedMs, FULL_SEASON_MAX_MS);
	}
}

package test.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import config.GameConfiguration;
import data.player.Player;
import data.sport.setup.Game;
import data.sport.setup.GameResult;
import data.team.Team;
import process.simulator.GameSimulator;
import test.support.TestSupport;

public class TestGameSimulator {

	private Team homeTeam;
	private Team awayTeam;
	private GameSimulator gameSimulator;

	@Before
	public void setUp() {
		data.league.League league = TestSupport.buildLeagueWithFinance();
		ArrayList<Team> teams = TestSupport.firstTeams(league, 2);
		homeTeam = teams.get(0);
		awayTeam = teams.get(1);
		gameSimulator = new GameSimulator();
	}

	@Test
	public void shouldPopulateQuarterResultsAndFinalScores() {
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);

		gameSimulator.simulateGame(game);

		assertNotNull(game.getQuarterResults());
		assertEquals(4, game.getQuarterResults().length);

		int homeScore = 0;
		int awayScore = 0;
		for (GameResult quarter : game.getQuarterResults()) {
			assertNotNull(quarter);
			assertNotNull(quarter.getActions());
			assertTrue(!quarter.getActions().isEmpty());
			assertEquals(GameConfiguration.END_OF_TIME_ACTION,
					quarter.getActions().get(quarter.getActions().size() - 1).getName());
			homeScore += quarter.getScorehomeTeam();
			awayScore += quarter.getScoreAwayTeam();
		}

		assertEquals(homeScore, game.getHomeFinalScore());
		assertEquals(awayScore, game.getAwayFinalScore());
	}

	@Test
	public void shouldIncrementPlayedGamesForBothTeams() {
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		int initialHomePlayed = homeTeam.getTeamPerformance().getNumberPlayedGames();
		int initialAwayPlayed = awayTeam.getTeamPerformance().getNumberPlayedGames();

		gameSimulator.simulateGame(game);

		assertEquals(initialHomePlayed + 1, homeTeam.getTeamPerformance().getNumberPlayedGames());
		assertEquals(initialAwayPlayed + 1, awayTeam.getTeamPerformance().getNumberPlayedGames());
	}

	@Test
	public void shouldUpdateCurrentSeasonAssetsForPlayersAfterSimulation() {
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);

		gameSimulator.simulateGame(game);

		boolean foundUpdatedHomePlayer = false;
		for (Player player : homeTeam.getCurrentPlayers().values()) {
			if (player.getCurrentSeasonAssets().getMinutesPlayedPerMatch() > 0) {
				foundUpdatedHomePlayer = true;
				break;
			}
		}

		boolean foundUpdatedAwayPlayer = false;
		for (Player player : awayTeam.getCurrentPlayers().values()) {
			if (player.getCurrentSeasonAssets().getMinutesPlayedPerMatch() > 0) {
				foundUpdatedAwayPlayer = true;
				break;
			}
		}

		assertTrue(foundUpdatedHomePlayer);
		assertTrue(foundUpdatedAwayPlayer);
	}
}

package test.performance;

import static org.junit.Assert.*;

import java.util.ArrayList;

import config.GameConfiguration;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.team.Team;
import process.builder.league.LeagueBuilder;
import process.service.finance.initialization.FinanceInitializer;
import test.support.TestSupport;

public class PerformanceSupport {

	public static void assertBelow(String label, double elapsedMs, double maxMs) {
		System.out.println(label + " ms=" + elapsedMs);
		assertTrue(label + " too slow: " + elapsedMs + " ms", elapsedMs < maxMs);
	}

	public static League buildLeagueWithFinance() {
		TestSupport.clearRepositories();
		League league = new LeagueBuilder().build();
		new FinanceInitializer().initializeFinance();
		return league;
	}

	public static Game createInterConferenceGame(League league, int homeIndex, int awayIndex) {
		ArrayList<Team> teams = new ArrayList<Team>(league.getAllTeam());
		return new Game(new GameContext(teams.get(homeIndex), teams.get(awayIndex),
				GameConfiguration.GAME_INTER_CONFERENCE));
	}
}

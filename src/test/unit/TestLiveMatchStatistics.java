package test.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import config.GameConfiguration;
import data.league.League;
import data.player.Player;
import data.sport.live.LiveMatchStatistics;
import data.sport.play.OffensiveTry;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;
import data.sport.setup.Game;
import data.team.Team;
import test.support.TestSupport;

public class TestLiveMatchStatistics {

	private Team homeTeam;
	private Team awayTeam;
	private Player homePlayer;
	private Player awayPlayer;
	private Game game;
	private LiveMatchStatistics statistics;

	@Before
	public void setUp() {
		League league = TestSupport.buildLeagueWithFinance();
		ArrayList<Team> teams = TestSupport.firstTeams(league, 2);
		homeTeam = teams.get(0);
		awayTeam = teams.get(1);
		homePlayer = homeTeam.getCurrentPlayers().values().iterator().next();
		awayPlayer = awayTeam.getCurrentPlayers().values().iterator().next();
		game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		statistics = new LiveMatchStatistics();
		statistics.setGame(game);
	}

	@Test
	public void shouldUpdateLiveStatisticsFromActions() {
		PointScored homeThree = new PointScored("score", 3, homePlayer, awayPlayer);
		homeThree.setOffensiveAction(new OffensiveTry(GameConfiguration.THREEPOINT));
		statistics.applyAction(homeThree);

		Rebound awayRebound = new Rebound("rebound", awayPlayer, homePlayer);
		statistics.applyAction(awayRebound);

		Turnover turnover = new Turnover("turnover", homePlayer, awayPlayer);
		statistics.applyAction(turnover);

		assertEquals(3, statistics.getHomePoints());
		assertEquals(0, statistics.getAwayPoints());
		assertEquals(1, statistics.getAwayRebounds());
		assertEquals(1, statistics.getAwayAssists());
		assertEquals(1, statistics.getAwayTurnovers());
		assertEquals("100%", statistics.getHomeFgPercent());
		assertEquals("100%", statistics.getHomeThreePercent());
		assertNotNull(statistics.getHomeBestPlayers()[0]);
		assertEquals(homePlayer, statistics.getHomeBestPlayers()[0].getPlayer());
		assertEquals(3, statistics.getHomeBestPlayers()[0].getPoints());
	}

	@Test
	public void shouldSaveAndReloadLiveState() {
		PointScored awayTwo = new PointScored("score", 2, awayPlayer, null);
		awayTwo.setOffensiveAction(new OffensiveTry(GameConfiguration.TWOPOINT));
		statistics.applyAction(awayTwo);

		LiveMatchStatistics.SavedLiveState savedState = statistics.toSavedState(3);
		statistics.reset();
		statistics.loadFromState(savedState);

		assertEquals(2, statistics.getAwayPoints());
		assertEquals("100%", statistics.getAwayFgPercent());
		assertEquals(3, savedState.getLiveActionIndex());
	}
}

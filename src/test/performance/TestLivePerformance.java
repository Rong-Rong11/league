package test.performance;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import process.service.live.LiveMatchService;
import test.support.TestSupport;

public class TestLivePerformance {

	private static final double PLAY_QUARTER_MAX_MS = 200.0;
	private static final double FULL_LIVE_PLAYBACK_MAX_MS = 500.0;

	private Team homeTeam;
	private Team awayTeam;

	@Before
	public void setUp() {
		League league = PerformanceSupport.buildLeagueWithFinance();
		ArrayList<Team> teams = TestSupport.firstTeams(league, 2);
		homeTeam = teams.get(0);
		awayTeam = teams.get(1);
	}

	@Test
	public void shouldPlayQuarterQuickly() {
		LiveMatchService liveMatchService = new LiveMatchService();
		Game game = TestSupport.createScriptedLiveGame(homeTeam, awayTeam);
		liveMatchService.setGame(game);

		long start = System.nanoTime();
		liveMatchService.playCurrentLiveQuarter();
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertTrue(liveMatchService.getCurrentState().getHomePoints() >= 0);

		PerformanceSupport.assertBelow("playCurrentQuarter", elapsedMs, PLAY_QUARTER_MAX_MS);
	}

	@Test
	public void shouldRunFullLivePlaybackQuickly() {
		LiveMatchService liveMatchService = new LiveMatchService();
		Game game = TestSupport.createScriptedLiveGame(homeTeam, awayTeam);
		liveMatchService.setGame(game);

		long start = System.nanoTime();
		liveMatchService.startLiveMatch();
		for (int index = 0; index < 12; index++) {
			liveMatchService.tickLiveMatch();
		}
		liveMatchService.playCurrentLiveQuarter();
		liveMatchService.playCurrentLiveQuarter();
		liveMatchService.playCurrentLiveQuarter();
		liveMatchService.playCurrentLiveQuarter();
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertTrue(game.isDisplayed() || liveMatchService.getCurrentState().getHomePoints() >= 0);

		PerformanceSupport.assertBelow("fullLivePlayback", elapsedMs, FULL_LIVE_PLAYBACK_MAX_MS);
	}
}

package test.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.league.League;
import data.sport.live.LiveMatchState;
import data.sport.setup.Game;
import data.team.Team;
import process.service.live.LiveMatchService;
import test.support.TestSupport;

public class TestLiveMatchService {

	private Team homeTeam;
	private Team awayTeam;
	private LiveMatchService liveMatchService;

	@Before
	public void setUp() {
		League league = TestSupport.buildLeagueWithFinance();
		ArrayList<Team> teams = TestSupport.firstTeams(league, 2);
		homeTeam = teams.get(0);
		awayTeam = teams.get(1);
		liveMatchService = new LiveMatchService();
	}

	@Test
	public void shouldReturnDefaultMessageWhenNoGameIsSelected() {
		LiveMatchState state = liveMatchService.getCurrentState();

		assertEquals("Aucun match selectionne.", state.getCenterMessage());
		assertEquals("Q-", state.getQuarterLabel());
		assertEquals("--:--", state.getQuarterTimeText());
	}

	@Test
	public void shouldReportUnavailableWhenGameIsNotSimulated() {
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);

		liveMatchService.setGame(game);
		LiveMatchState state = liveMatchService.getCurrentState();

		assertFalse(liveMatchService.isLiveMatchAvailable(game));
		assertEquals("Match non disponible.", state.getCenterMessage());
	}

	@Test
	public void shouldPlayLiveActionsAndRevealGameAtEnd() {
		Game game = TestSupport.createScriptedLiveGame(homeTeam, awayTeam);

		liveMatchService.setGame(game);
		assertTrue(liveMatchService.isLiveMatchAvailable(game));

		liveMatchService.startLiveMatch();
		assertTrue(liveMatchService.isRunning());

		for (int index = 0; index < 8; index++) {
			liveMatchService.tickLiveMatch();
		}

		LiveMatchState midState = liveMatchService.getCurrentState();
		assertTrue(midState.getHomePoints() > 0 || midState.getAwayPoints() > 0);

		liveMatchService.playCurrentLiveQuarter();
		liveMatchService.playCurrentLiveQuarter();
		liveMatchService.playCurrentLiveQuarter();
		liveMatchService.playCurrentLiveQuarter();

		LiveMatchState finalState = liveMatchService.getCurrentState();
		assertFalse(liveMatchService.isRunning());
		assertTrue(game.isDisplayed());
		assertEquals("FIN", finalState.getQuarterLabel());
		assertEquals(3, finalState.getHomePoints());
		assertEquals(2, finalState.getAwayPoints());
		assertEquals(1, finalState.getAwayRebounds());
		assertEquals(1, finalState.getAwayTurnovers());
	}

	@Test
	public void shouldResetLiveMatchStateAfterPlayback() {
		Game game = TestSupport.createScriptedLiveGame(homeTeam, awayTeam);
		liveMatchService.setGame(game);
		liveMatchService.playCurrentLiveQuarter();

		liveMatchService.resetLiveMatch();

		LiveMatchState state = liveMatchService.getCurrentState();
		assertFalse(liveMatchService.isRunning());
		assertEquals(0, state.getHomePoints());
		assertEquals(0, state.getAwayPoints());
		assertEquals("Q1", state.getQuarterLabel());
		assertEquals("12:00", state.getQuarterTimeText());
	}
}

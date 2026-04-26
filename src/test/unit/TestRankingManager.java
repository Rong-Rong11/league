package test.unit;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import config.GameConfiguration;
import data.calendar.GameDay;
import data.league.League;
import data.league.Ranking;
import data.sport.setup.Game;
import data.team.Team;
import process.service.ranking.NbaRegularSeasonTeamComparator;
import process.service.ranking.RegularSeasonRankingManager;
import process.utility.TeamUtility;
import test.support.TestSupport;

public class TestRankingManager {

	private League league;
	private Team teamA;
	private Team teamB;

	@Before
	public void setUp() {
		league = TestSupport.buildLeagueWithFinance();
		ArrayList<Team> teams = TestSupport.firstTeams(league, 2);
		teamA = teams.get(0);
		teamB = teams.get(1);
	}

	@Test
	public void shouldRankTeamWithBetterWinRateAhead() {
		teamA.getTeamPerformance().setNumberWin(10);
		teamA.getTeamPerformance().setNumberPlayedGames(12);
		teamB.getTeamPerformance().setNumberWin(8);
		teamB.getTeamPerformance().setNumberPlayedGames(12);

		ArrayList<Game> simulatedGames = new ArrayList<Game>();
		NbaRegularSeasonTeamComparator comparator = new NbaRegularSeasonTeamComparator(simulatedGames, league);
		ArrayList<Team> teams = new ArrayList<Team>();
		teams.add(teamB);
		teams.add(teamA);

		Collections.sort(teams, comparator);

		assertEquals(teamA, teams.get(0));
	}

	@Test
	public void shouldBreakTieUsingHeadToHeadResult() {
		teamA.getTeamPerformance().setNumberWin(10);
		teamA.getTeamPerformance().setNumberPlayedGames(12);
		teamB.getTeamPerformance().setNumberWin(10);
		teamB.getTeamPerformance().setNumberPlayedGames(12);

		Game headToHead = TestSupport.createGame(teamA, teamB, GameConfiguration.GAME_INTRA_CONFERENCE);
		headToHead.setHomeFinalScore(110);
		headToHead.setAwayFinalScore(100);

		ArrayList<Game> simulatedGames = new ArrayList<Game>();
		simulatedGames.add(headToHead);
		NbaRegularSeasonTeamComparator comparator = new NbaRegularSeasonTeamComparator(simulatedGames, league);
		ArrayList<Team> teams = new ArrayList<Team>();
		teams.add(teamB);
		teams.add(teamA);

		Collections.sort(teams, comparator);

		assertEquals(teamA, teams.get(0));
	}

	@Test
	public void shouldUpdateRankingAfterAddingSimulatedGameDay() {
		ArrayList<Team> westTeams = new ArrayList<Team>();
		ArrayList<Team> eastTeams = new ArrayList<Team>();

		if (TeamUtility.getConferenceOfTeam(league, teamA).equals(league.getEasternConference())) {
			eastTeams.add(teamA);
			westTeams.add(teamB);
		} else {
			westTeams.add(teamA);
			eastTeams.add(teamB);
		}

		RegularSeasonRankingManager rankingManager = new RegularSeasonRankingManager(westTeams, eastTeams);
		Game game = TestSupport.createInterConferenceGame(teamA, teamB);
		game.setHomeFinalScore(120);
		game.setAwayFinalScore(100);
		teamA.getTeamPerformance().setNumberWin(1);
		teamA.getTeamPerformance().setNumberPlayedGames(1);
		teamB.getTeamPerformance().setNumberLose(1);
		teamB.getTeamPerformance().setNumberPlayedGames(1);
		GameDay gameDay = new GameDay(LocalDate.of(2025, 10, 21));
		gameDay.addGame(game);

		rankingManager.addSimulatedGameDay(gameDay);
		Ranking ranking = rankingManager.updateRanking(league, new Ranking(),
				league.getRegularSeason().getNbaCalendar().getCalendar(),
				gameDay.getDate());

		if (TeamUtility.getConferenceOfTeam(league, teamA).equals(league.getEasternConference())) {
			assertEquals(teamA, ranking.getEastRanking().get(1));
		} else {
			assertEquals(teamA, ranking.getWestRanking().get(1));
		}
	}

	@Test
	public void shouldExposeGlobalRankingContainingTrackedTeams() {
		ArrayList<Team> westTeams = new ArrayList<Team>();
		ArrayList<Team> eastTeams = new ArrayList<Team>();

		if (TeamUtility.getConferenceOfTeam(league, teamA).equals(league.getEasternConference())) {
			eastTeams.add(teamA);
			westTeams.add(teamB);
		} else {
			westTeams.add(teamA);
			eastTeams.add(teamB);
		}

		RegularSeasonRankingManager rankingManager = new RegularSeasonRankingManager(westTeams, eastTeams);
		ArrayList<Team> globalRanking = rankingManager.getGlobalRanking(league);

		assertEquals(2, globalRanking.size());
		assertTrue(globalRanking.contains(teamA));
		assertTrue(globalRanking.contains(teamB));
	}
}

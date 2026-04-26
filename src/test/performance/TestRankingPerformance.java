package test.performance;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.league.League;
import data.league.Ranking;
import data.team.Team;
import process.builder.calendar.RegularSeasonCalendarBuilder;
import process.service.ranking.RegularSeasonRankingManager;
import process.utility.LeagueUtility;
import test.support.TestSupport;

public class TestRankingPerformance {

	private static final double UPDATE_RANKING_MAX_MS = 500.0;
	private static final double GLOBAL_RANKING_MAX_MS = 150.0;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
	}

	@Test
	public void shouldUpdateRankingAcrossBusyCalendarQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		NBACalendar calendar = new RegularSeasonCalendarBuilder(league).buildCalendar();
		ArrayList<Team> eastTeams = new ArrayList<Team>();
		ArrayList<Team> westTeams = new ArrayList<Team>();
		LeagueUtility.getConferenceTeams(league, eastTeams, westTeams);
		RegularSeasonRankingManager rankingManager = new RegularSeasonRankingManager(westTeams, eastTeams);

		int performanceBase = 10;
		for (Team team : league.getAllTeam()) {
			team.getTeamPerformance().setNumberPlayedGames(20 + performanceBase);
			team.getTeamPerformance().setNumberWin(performanceBase);
			performanceBase++;
		}

		ArrayList<GameDay> sampledGameDays = TestSupport.firstGameDays(calendar, 60);
		for (GameDay gameDay : sampledGameDays) {
			rankingManager.addSimulatedGameDay(gameDay);
		}

		long start = System.nanoTime();
		Ranking ranking = new Ranking();
		for (GameDay gameDay : sampledGameDays) {
			ranking = rankingManager.updateRanking(league, ranking, calendar.getCalendar(), gameDay.getDate());
		}
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertEquals(15, ranking.getEastRanking().size());
		assertEquals(15, ranking.getWestRanking().size());

		TestSupport.assertBelow("updateRankingAcrossCalendar", elapsedMs, UPDATE_RANKING_MAX_MS);
	}

	@Test
	public void shouldBuildGlobalRankingQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		ArrayList<Team> eastTeams = new ArrayList<Team>();
		ArrayList<Team> westTeams = new ArrayList<Team>();
		LeagueUtility.getConferenceTeams(league, eastTeams, westTeams);
		RegularSeasonRankingManager rankingManager = new RegularSeasonRankingManager(westTeams, eastTeams);

		long start = System.nanoTime();
		ArrayList<Team> globalRanking = rankingManager.getGlobalRanking(league);
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertEquals(30, globalRanking.size());

		TestSupport.assertBelow("globalRanking", elapsedMs, GLOBAL_RANKING_MAX_MS);
	}
}

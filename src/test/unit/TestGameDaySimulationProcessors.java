package test.unit;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.calendar.GameDay;
import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import process.builder.calendar.FirstRoundCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.game.tools.PlayoffGameDaySimulationProcessor;
import process.service.game.tools.RegularSeasonGameDaySimulationProcessor;
import process.service.league.TeamPopularityUpdater;
import process.service.playoff.FirstRoundPlayoffManager;
import process.service.ranking.RegularSeasonRankingManager;
import process.simulator.GameSimulator;
import process.utility.LeagueUtility;
import test.support.TestSupport;

public class TestGameDaySimulationProcessors {

	private League league;
	private Team homeTeam;
	private Team awayTeam;
	private LocalDate gameDate;
	private GameDay gameDay;
	private Game game;
	private FinanceManager financeManager;

	@Before
	public void setUp() {
		league = TestSupport.buildLeagueWithFinance();
		ArrayList<Team> teams = TestSupport.firstTeams(league, 2);
		homeTeam = teams.get(0);
		awayTeam = teams.get(1);
		gameDate = LocalDate.of(2025, 10, 21);
		game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		gameDay = new GameDay(gameDate);
		gameDay.addGame(game);
		financeManager = new FinanceManager(league);
	}

	@Test
	public void shouldSimulateRegularSeasonGameDay() {
		ArrayList<Team> eastTeams = new ArrayList<Team>();
		ArrayList<Team> westTeams = new ArrayList<Team>();
		LeagueUtility.getConferenceTeams(league, eastTeams, westTeams);
		RegularSeasonRankingManager rankingManager = new RegularSeasonRankingManager(westTeams, eastTeams);
		RegularSeasonGameDaySimulationProcessor processor = new RegularSeasonGameDaySimulationProcessor(
				league,
				new GameSimulator(),
				financeManager,
				rankingManager);

		processor.simulateGameDay(gameDay, gameDate, 1);

		assertTrue(gameDay.isSimulated());
		assertNotNull(game.getQuarterResults());
		assertNotNull(financeManager.getGameStat(game));
		assertTrue(game.getHomeFinalScore() >= 0);
		assertTrue(game.getAwayFinalScore() >= 0);
		assertEquals(15, league.getRegularSeason().getRanking().getEastRanking().size());
		assertEquals(15, league.getRegularSeason().getRanking().getWestRanking().size());
	}

	@Test
	public void shouldSimulatePlayoffGameDay() {
		PlayoffSeries playedSeries = new PlayoffSeries(homeTeam, awayTeam);
		playedSeries.addExpectedGame(game, 1);
		playedSeries.setHigherTeamWins(4);
		playedSeries.setFinished(true);

		PlayoffSeries otherSeries = new PlayoffSeries(awayTeam, homeTeam);

		league.getPlayoff().setCurrentRound(PlayoffRound.FIRST_ROUND);
		league.getPlayoff().getEastFirstRound().clear();
		league.getPlayoff().getWestFirstRound().clear();
		league.getPlayoff().getEastFirstRound().add(playedSeries);
		league.getPlayoff().getWestFirstRound().add(otherSeries);

		FirstRoundPlayoffManager playoffManager = new FirstRoundPlayoffManager(
				league,
				new FirstRoundCalendarBuilder(league),
				new PlayoffBuilder(league),
				financeManager,
				new TeamPopularityUpdater());

		PlayoffGameDaySimulationProcessor processor = new PlayoffGameDaySimulationProcessor(
				new GameSimulator(),
				financeManager,
				playoffManager,
				PlayoffRound.FIRST_ROUND);

		processor.simulateGameDay(gameDay, gameDate, 8);

		assertTrue(gameDay.isSimulated());
		assertNotNull(financeManager.getGameStat(game));
		assertTrue(playedSeries.isFinished());
	}
}

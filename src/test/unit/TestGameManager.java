package test.unit;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import process.builder.calendar.FirstRoundCalendarBuilder;
import process.builder.calendar.RegularSeasonCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.repository.DivisionRepository;
import process.repository.PlayerRepository;
import process.repository.TeamRepository;
import process.service.finance.FinanceManager;
import process.service.game.GameManager;
import process.service.league.TeamPopularityUpdater;
import test.support.TestSupport;

public class TestGameManager {

	private League league;
	private FinanceManager financeManager;
	private GameManager gameManager;
	private Team homeTeam;
	private Team awayTeam;

	@Before
	public void setUp() {
		PlayerRepository.getInstance().clear();
		TeamRepository.getInstance().clear();
		DivisionRepository.getInstance().clear();
		league = TestSupport.buildLeagueWithFinance();
		ArrayList<Team> teams = TestSupport.firstTeams(league, 2);
		homeTeam = teams.get(0);
		awayTeam = teams.get(1);
		financeManager = new FinanceManager(league);
		gameManager = new GameManager(
				league,
				financeManager,
				new RegularSeasonCalendarBuilder(league),
				new PlayoffBuilder(league),
				new FirstRoundCalendarBuilder(league),
				new TeamPopularityUpdater());
	}

	@Test
	public void shouldReturnFalseWithoutGameDay() {
		TestSupport.setRegularSeasonCalendar(league);
		boolean simulated = gameManager.simulateRegularSeasonDay(LocalDate.of(2025, 10, 21), 1);
		assertFalse(simulated);
	}

	@Test
	public void shouldSimulateRegularSeasonDayOnce() {
		LocalDate date = LocalDate.of(2025, 10, 21);
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		GameDay gameDay = new GameDay(date);
		gameDay.addGame(game);
		TestSupport.setRegularSeasonCalendar(league, gameDay);

		assertTrue(gameManager.simulateRegularSeasonDay(date, 1));
		assertFalse(gameManager.simulateRegularSeasonDay(date, 1));

		assertTrue(gameDay.isSimulated());
		assertTrue(game.getHomeFinalScore() >= 0);
		assertTrue(game.getAwayFinalScore() >= 0);
		assertEquals(game, financeManager.getGameStat(game).getGame());
	}

	@Test
	public void shouldIgnorePlayoffsWhenRoundIsNull() {
		LocalDate date = CalendarConfiguration.PLAYOFF_DEBUT_DATE;
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		GameDay gameDay = new GameDay(date);
		gameDay.addGame(game);
		TestSupport.setPlayoffCalendar(league, gameDay);

		gameManager.simulatePlayoffDay(date, 8, null);

		assertFalse(gameDay.isSimulated());
		assertTrue(financeManager.getGameStat(game) == null);
	}

	@Test
	public void shouldSimulateFirstRoundDay() {
		LocalDate date = CalendarConfiguration.PLAYOFF_DEBUT_DATE;
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		GameDay gameDay = new GameDay(date);
		gameDay.addGame(game);
		TestSupport.setPlayoffCalendar(league, gameDay);
		PlayoffSeries series = new PlayoffSeries(homeTeam, awayTeam);
		series.addExpectedGame(game, 1);
		series.setFinished(true);
		PlayoffSeries otherSeries = new PlayoffSeries(awayTeam, homeTeam);
		league.getPlayoff().getEastFirstRound().clear();
		league.getPlayoff().getWestFirstRound().clear();
		league.getPlayoff().getEastFirstRound().add(series);
		league.getPlayoff().getWestFirstRound().add(otherSeries);

		gameManager.simulatePlayoffDay(date, 8, PlayoffRound.FIRST_ROUND);

		assertTrue(gameDay.isSimulated());
		assertEquals(game, financeManager.getGameStat(game).getGame());
		assertTrue(series.isFinished());
	}
}

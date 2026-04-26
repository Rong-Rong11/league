package test.unit;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import config.CalendarConfiguration;
import config.GameConfiguration;
import data.league.League;
import data.league.Playoff;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import process.utility.CalendarUtility;
import test.support.TestSupport;

public class TestCalendarUtility {

	private League league;
	private Team homeTeam;
	private Team awayTeam;

	@Before
	public void setUp() {
		league = TestSupport.buildLeagueWithFinance();
		ArrayList<Team> teams = TestSupport.firstTeams(league, 2);
		homeTeam = teams.get(0);
		awayTeam = teams.get(1);
	}

	@Test
	public void shouldDetectImportantDays() {
		assertTrue(CalendarUtility.isWeekend(LocalDate.of(2025, 10, 25)));
		assertFalse(CalendarUtility.isWeekend(LocalDate.of(2025, 10, 27)));
		assertTrue(CalendarUtility.isImportantDay(LocalDate.of(2025, 10, 22)));
		assertTrue(CalendarUtility.isImportantDay(LocalDate.of(2025, 10, 26)));
		assertFalse(CalendarUtility.isImportantDay(LocalDate.of(2025, 10, 27)));
	}

	@Test
	public void shouldDetectSpecialDates() {
		assertTrue(CalendarUtility.isSpecialEvent(league.getRegularSeason(), CalendarConfiguration.CHRISTMAS_DAY));
		assertTrue(CalendarUtility.isSpecialEvent(league.getRegularSeason(), league.getRegularSeason().getDebutDate()));
		assertTrue(CalendarUtility.isSpecialEvent(league.getRegularSeason(), league.getRegularSeason().getEndDate()));
		assertFalse(CalendarUtility.isSpecialEvent(league.getRegularSeason(), LocalDate.of(2025, 11, 5)));
	}

	@Test
	public void shouldFindMlkDay() {
		LocalDate mlkDay = CalendarUtility.getMLKDay();

		assertNotNull(mlkDay);
		assertEquals(LocalDate.of(2026, 1, 19), mlkDay);
	}

	@Test
	public void shouldBoostPopularityForRivalryAndStars() {
		homeTeam.setCurrentPopularity(40.0);
		awayTeam.setCurrentPopularity(40.0);
		homeTeam.setRival("shared");
		awayTeam.setRival("neutral");
		Game normalGame = new Game(
				new data.sport.setup.GameContext(homeTeam, awayTeam, GameConfiguration.GAME_INTRA_DIVISION));
		double normalScore = CalendarUtility.popularityScoreGame(normalGame, LocalDate.of(2025, 10, 21));

		awayTeam.setRival("shared");
		homeTeam.setStarPlayer(homeTeam.getCurrentPlayers().values().iterator().next());
		Game rivalryGame = new Game(
				new data.sport.setup.GameContext(homeTeam, awayTeam, GameConfiguration.GAME_INTRA_DIVISION));
		double rivalryScore = CalendarUtility.popularityScoreGame(rivalryGame, LocalDate.of(2025, 10, 21));

		assertTrue(rivalryScore > normalScore);
	}

	@Test
	public void shouldDetectRivalryFromSharedName() {
		homeTeam.setRival("same");
		awayTeam.setRival("same");
		assertTrue(CalendarUtility
				.isRivalry(new data.sport.setup.GameContext(homeTeam, awayTeam, GameConfiguration.GAME_INTRA_DIVISION)));

		awayTeam.setRival("other");
		assertFalse(CalendarUtility
				.isRivalry(new data.sport.setup.GameContext(homeTeam, awayTeam, GameConfiguration.GAME_INTRA_DIVISION)));
	}

	@Test
	public void shouldGetCurrentRoundSeries() {
		Playoff playoff = league.getPlayoff();
		PlayoffSeries eastSeries = new PlayoffSeries(homeTeam, awayTeam);
		PlayoffSeries westSeries = new PlayoffSeries(awayTeam, homeTeam);
		playoff.getEastFirstRound().clear();
		playoff.getWestFirstRound().clear();
		playoff.getEastFirstRound().add(eastSeries);
		playoff.getWestFirstRound().add(westSeries);
		playoff.setCurrentRound(PlayoffRound.FIRST_ROUND);

		ArrayList<PlayoffSeries> firstRoundSeries = CalendarUtility.getCurrentRoundSeries(playoff);

		assertEquals(2, firstRoundSeries.size());
		assertTrue(firstRoundSeries.contains(eastSeries));
		assertTrue(firstRoundSeries.contains(westSeries));
	}
}

package test.performance;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import config.CalendarConfiguration;
import data.calendar.NBACalendar;
import data.league.League;
import process.builder.calendar.ConferenceFinalCalendarBuilder;
import process.builder.calendar.FirstRoundCalendarBuilder;
import process.builder.calendar.NbaFinalCalendarBuilder;
import process.builder.calendar.SemiCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.league.TeamPopularityUpdater;
import process.service.playoff.ConferenceFinalPlayoffManager;
import process.service.playoff.FirstRoundPlayoffManager;
import process.service.playoff.SemiPlayoffManager;
import test.support.TestSupport;

public class TestPlayoffPerformance {

	private static final double BUILD_FIRST_ROUND_MAX_MS = 200.0;
	private static final double BUILD_ALL_ROUNDS_MAX_MS = 300.0;
	private static final double FIRST_ROUND_CALENDAR_MAX_MS = 300.0;
	private static final double SEMI_CALENDAR_MAX_MS = 200.0;
	private static final double CONFERENCE_FINAL_CALENDAR_MAX_MS = 200.0;
	private static final double NBA_FINALS_CALENDAR_MAX_MS = 150.0;
	private static final double ADVANCE_PLAYOFF_ROUNDS_MAX_MS = 500.0;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
	}

	@Test
	public void shouldBuildFirstRoundPlayoffsQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		TestSupport.seedRegularSeasonRanking(league);
		PlayoffBuilder playoffBuilder = new PlayoffBuilder(league);

		long start = System.nanoTime();
		league.setPlayoff(playoffBuilder.buldFirstRoundPlayoffs());
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertEquals(4, league.getPlayoff().getEastFirstRound().size());
		assertEquals(4, league.getPlayoff().getWestFirstRound().size());

		TestSupport.assertBelow("buildFirstRoundPlayoffs", elapsedMs, BUILD_FIRST_ROUND_MAX_MS);
	}

	@Test
	public void shouldBuildAllPlayoffRoundsQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		PlayoffBuilder playoffBuilder = new PlayoffBuilder(league);

		long start = System.nanoTime();
		TestSupport.prepareFirstRoundPlayoffs(league);
		TestSupport.markHigherSeedAsWinner(league.getPlayoff().getEastFirstRound());
		TestSupport.markHigherSeedAsWinner(league.getPlayoff().getWestFirstRound());
		league.setPlayoff(playoffBuilder.buldSecondRoundPlayoffs());
		TestSupport.markHigherSeedAsWinner(league.getPlayoff().getEastConferenceSemis());
		TestSupport.markHigherSeedAsWinner(league.getPlayoff().getWestConferenceSemis());
		league.setPlayoff(playoffBuilder.buildConferenceFinalsPlayoffs());
		TestSupport.markHigherSeedAsWinner(league.getPlayoff().getEastConferenceFinals());
		TestSupport.markHigherSeedAsWinner(league.getPlayoff().getWestConferenceFinals());
		league.setPlayoff(playoffBuilder.buildNbaFinalsPlayoffs());
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertEquals(1, league.getPlayoff().getNbaFinals().size());

		TestSupport.assertBelow("buildAllPlayoffRounds", elapsedMs, BUILD_ALL_ROUNDS_MAX_MS);
	}

	@Test
	public void shouldBuildEachPlayoffCalendarQuickly() {
		League league = TestSupport.buildLeagueWithFinance();

		TestSupport.prepareFirstRoundPlayoffs(league);
		long firstRoundStart = System.nanoTime();
		NBACalendar firstRoundCalendar = new FirstRoundCalendarBuilder(league).buildCalendar();
		double firstRoundElapsedMs = (System.nanoTime() - firstRoundStart) / 1000000.0;

		TestSupport.prepareConferenceSemifinals(league);
		long semiStart = System.nanoTime();
		NBACalendar semiCalendar = new SemiCalendarBuilder(league, CalendarConfiguration.PLAYOFF_DEBUT_DATE)
				.buildCalendar();
		double semiElapsedMs = (System.nanoTime() - semiStart) / 1000000.0;

		TestSupport.prepareConferenceFinals(league);
		long conferenceFinalsStart = System.nanoTime();
		NBACalendar conferenceFinalsCalendar = new ConferenceFinalCalendarBuilder(league,
				CalendarConfiguration.PLAYOFF_DEBUT_DATE).buildCalendar();
		double conferenceFinalsElapsedMs = (System.nanoTime() - conferenceFinalsStart) / 1000000.0;

		TestSupport.prepareNbaFinals(league);
		long nbaFinalsStart = System.nanoTime();
		NBACalendar nbaFinalsCalendar = new NbaFinalCalendarBuilder(league, CalendarConfiguration.PLAYOFF_DEBUT_DATE)
				.buildCalendar();
		double nbaFinalsElapsedMs = (System.nanoTime() - nbaFinalsStart) / 1000000.0;

		assertFalse(firstRoundCalendar.getCalendar().isEmpty());
		assertFalse(semiCalendar.getCalendar().isEmpty());
		assertFalse(conferenceFinalsCalendar.getCalendar().isEmpty());
		assertFalse(nbaFinalsCalendar.getCalendar().isEmpty());

		TestSupport.assertBelow("firstRoundCalendar", firstRoundElapsedMs, FIRST_ROUND_CALENDAR_MAX_MS);
		TestSupport.assertBelow("semiCalendar", semiElapsedMs, SEMI_CALENDAR_MAX_MS);
		TestSupport.assertBelow("conferenceFinalCalendar", conferenceFinalsElapsedMs,
				CONFERENCE_FINAL_CALENDAR_MAX_MS);
		TestSupport.assertBelow("nbaFinalsCalendar", nbaFinalsElapsedMs, NBA_FINALS_CALENDAR_MAX_MS);
	}

	@Test
	public void shouldAdvanceManagedPlayoffRoundsQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		FinanceManager financeManager = new FinanceManager(league);
		PlayoffBuilder playoffBuilder = new PlayoffBuilder(league);
		TeamPopularityUpdater popularityUpdater = new TeamPopularityUpdater();

		TestSupport.prepareFirstRoundPlayoffs(league);
		FirstRoundPlayoffManager firstRoundManager = new FirstRoundPlayoffManager(
				league,
				new FirstRoundCalendarBuilder(league),
				playoffBuilder,
				financeManager,
				popularityUpdater);

		long start = System.nanoTime();
		firstRoundManager.advanceToNextRound(CalendarConfiguration.PLAYOFF_DEBUT_DATE);
		TestSupport.markHigherSeedAsWinner(league.getPlayoff().getEastConferenceSemis());
		TestSupport.markHigherSeedAsWinner(league.getPlayoff().getWestConferenceSemis());

		SemiPlayoffManager semiManager = new SemiPlayoffManager(
				league,
				new SemiCalendarBuilder(league, CalendarConfiguration.PLAYOFF_DEBUT_DATE),
				playoffBuilder,
				financeManager,
				popularityUpdater);
		semiManager.advanceToNextRound(CalendarConfiguration.PLAYOFF_DEBUT_DATE.plusDays(10));
		TestSupport.markHigherSeedAsWinner(league.getPlayoff().getEastConferenceFinals());
		TestSupport.markHigherSeedAsWinner(league.getPlayoff().getWestConferenceFinals());

		ConferenceFinalPlayoffManager conferenceFinalManager = new ConferenceFinalPlayoffManager(
				league,
				new ConferenceFinalCalendarBuilder(league, CalendarConfiguration.PLAYOFF_DEBUT_DATE),
				playoffBuilder,
				financeManager,
				popularityUpdater);
		conferenceFinalManager.advanceToNextRound(CalendarConfiguration.PLAYOFF_DEBUT_DATE.plusDays(20));
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertEquals(1, league.getPlayoff().getNbaFinals().size());

		TestSupport.assertBelow("advancePlayoffRounds", elapsedMs, ADVANCE_PLAYOFF_ROUNDS_MAX_MS);
	}
}

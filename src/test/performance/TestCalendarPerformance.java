package test.performance;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import data.calendar.NBACalendar;
import data.league.League;
import process.builder.calendar.RegularSeasonCalendarBuilder;
import test.support.TestSupport;

public class TestCalendarPerformance {

	private static final double REGULAR_SEASON_CALENDAR_MAX_MS = 3000.0;

	@Before
	public void setUp() {
		TestSupport.clearRepositories();
	}

	@Test
	public void shouldBuildRegularSeasonCalendarQuickly() {
		League league = TestSupport.buildLeagueWithFinance();
		RegularSeasonCalendarBuilder calendarBuilder = new RegularSeasonCalendarBuilder(league);

		long start = System.nanoTime();
		NBACalendar calendar = calendarBuilder.buildCalendar();
		double elapsedMs = (System.nanoTime() - start) / 1000000.0;

		assertTrue(!calendar.getCalendar().isEmpty());

		TestSupport.assertBelow("regularSeasonCalendar", elapsedMs, REGULAR_SEASON_CALENDAR_MAX_MS);
	}
}

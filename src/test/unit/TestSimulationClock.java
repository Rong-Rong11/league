package test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import config.CalendarConfiguration;
import process.orchestrator.interf.SimulationClock;

public class TestSimulationClock {

	private SimulationClock clock;

	@Before
	public void setUp() {
		clock = new SimulationClock(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE);
	}

	@Test
	public void shouldResetClockToSeasonStart() {
		clock.setDate(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE.plusDays(30));
		clock.refreshMonth();
		clock.refreshWeek();

		clock.reset();

		assertEquals(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE, clock.getCurrentDate());
		assertEquals(1, clock.getCurrentMonth());
		assertEquals(1, clock.getCurrentWeek());
	}

	@Test
	public void shouldComputeWeekFromElapsedDays() {
		clock.setDate(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE.plusDays(8));

		assertEquals(2, clock.computeWeek());
	}

	@Test
	public void shouldComputeMonthAcrossCalendarYearBoundary() {
		clock.setDate(LocalDate.of(2026, 1, 5));

		assertEquals(4, clock.computeMonth());
	}

	@Test
	public void shouldDetectWeekAndMonthChanges() {
		clock.setDate(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE.plusDays(10));

		assertTrue(clock.hasWeekChanged());
		assertFalse(clock.hasMonthChanged());

		clock.refreshWeek();
		clock.setDate(LocalDate.of(2025, 11, 2));

		assertTrue(clock.hasMonthChanged());
	}

	@Test
	public void shouldAdvanceOneDayAtATime() {
		LocalDate start = clock.getCurrentDate();

		clock.nextDay();

		assertEquals(start.plusDays(1), clock.getCurrentDate());
	}

	@Test
	public void shouldDetectRegularSeasonEnd() {
		clock.setDate(CalendarConfiguration.REGULAR_SEASON_END_DATE);
		assertTrue(clock.isRegularSeasonEnd());

		clock.setDate(CalendarConfiguration.REGULAR_SEASON_END_DATE.minusDays(1));
		assertFalse(clock.isRegularSeasonEnd());
	}
}

package process.builder.calendar;

import org.apache.log4j.Logger;

import data.calendar.NBACalendar;
import data.league.League;
import process.builder.calendar.schedule.ScheduleReset;
import log.LoggerUtility;

public abstract class CalendarBuilder {
	private static final Logger logger = LoggerUtility.getLogger(CalendarBuilder.class, "text");
	private ScheduleReset scheduleReset = new ScheduleReset();
	private League league;

	public CalendarBuilder(League league) {
		this.league = league;
	}

	public NBACalendar buildCalendar() {
		if (league == null) {
			logger.warn("Skipping calendar build because league is null");
			return null;
		}
		if (scheduleReset == null) {
			logger.warn("Skipping calendar build because schedule reset is null");
			return null;
		}

		String builderName = getClass().getSimpleName();
		logger.info("Building calendar with " + builderName);
		logger.debug("Resetting schedules before calendar build");
		resetSchedule();
		logger.debug("Generating games before calendar assembly");
		generateGames();
		logger.debug("Building final calendar object");
		NBACalendar newCalendar = build();
		logger.info("Calendar built successfully with " + builderName);
		return newCalendar;
	}

	protected void resetSchedule() {
		scheduleReset.initialization();
	}

	protected abstract void generateGames();

	protected abstract NBACalendar build();

	public ScheduleReset getScheduleReset() {
		return scheduleReset;
	}

	public void setScheduleReset(ScheduleReset scheduleReset) {
		this.scheduleReset = scheduleReset;
		logger.debug("Schedule reset set on " + getClass().getSimpleName());
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
		logger.debug("League set on " + getClass().getSimpleName());
	}

}

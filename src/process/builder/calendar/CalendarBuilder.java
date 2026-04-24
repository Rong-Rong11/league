package process.builder.calendar;

import data.calendar.NBACalendar;
import data.league.League;
import process.builder.calendar.tools.ScheduleReset;

public abstract class CalendarBuilder {
	private ScheduleReset scheduleReset = new ScheduleReset();
	private League league;

	public CalendarBuilder(League league) {
		this.league = league;
	}

	public NBACalendar buildCalendar() {
		resetSchedule();
		generateGames();
		NBACalendar newCalendar = build();
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
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

}

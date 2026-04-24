package process.builder.calendar;

import java.time.LocalDate;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.league.League;
import process.builder.calendar.generator.PlayoffGameGenerator;

public class FirstRoundCalendarBuilder extends PlayoffCalendarBuilder {
	public FirstRoundCalendarBuilder(League league) {
	  super(league);
	}

	@Override
	protected NBACalendar build() {
	  // TODO Auto-generated method stub
	  TreeMap<LocalDate, GameDay> playoffCalendar = new TreeMap<>();

	  LocalDate startDate = config.CalendarConfiguration.PLAYOFF_DEBUT_DATE;

	  scheduleRoundFirstFourGames(playoffCalendar, getLeague().getPlayoff().getEastFirstRound(), startDate);
	  scheduleRoundFirstFourGames(playoffCalendar, getLeague().getPlayoff().getWestFirstRound(), startDate);

	  NBACalendar newCalendar = new NBACalendar(playoffCalendar);
	  return newCalendar;
	}

	@Override
	protected void generateGames() {
	  PlayoffGameGenerator.generateFirstRoundPlayoffGames(getLeague().getPlayoff());

	}

}

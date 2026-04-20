package process.builder.calendar;

import java.time.LocalDate;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.league.League;
import process.builder.calendar.tools.GameGenerator;

public class NbaFinalCalendarBuilder extends PlayoffCalendarBuilder {

	private LocalDate roundEndDate;

	public NbaFinalCalendarBuilder(League league, LocalDate roundEndDate) {
	  super(league);
	  this.roundEndDate = roundEndDate;
	}

	@Override
	protected NBACalendar build() {
	  // TODO Auto-generated method stub
	  TreeMap<LocalDate, GameDay> playoffCalendar = new TreeMap<>();

	  LocalDate startDate = roundEndDate.plusDays(2);

	  scheduleRoundFirstFourGames(playoffCalendar, getLeague().getPlayoff().getNbaFinals(), startDate);

	  NBACalendar newCalendar = new NBACalendar(playoffCalendar);
	  return newCalendar;
	}

	@Override
	protected void generateGames() {
	  // TODO Auto-generated method stub
	  GameGenerator.generateNbaFinalsPlayoffGames(getLeague().getPlayoff());

	}

}

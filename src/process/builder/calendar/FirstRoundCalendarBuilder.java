package process.builder.calendar;

import java.time.LocalDate;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.league.League;
import log.LoggerUtility;
import process.builder.calendar.generator.PlayoffGameGenerator;

public class FirstRoundCalendarBuilder extends PlayoffCalendarBuilder {
	private static final Logger logger = LoggerUtility.getLogger(FirstRoundCalendarBuilder.class, "text");

	public FirstRoundCalendarBuilder(League league) {
		super(league);
	}

	@Override
	protected NBACalendar build() {
		logger.info("Building first round playoff calendar");
		TreeMap<LocalDate, GameDay> playoffCalendar = new TreeMap<>();

		LocalDate startDate = config.CalendarConfiguration.PLAYOFF_DEBUT_DATE;
		logger.debug("First round playoff start date set to " + startDate);

		logger.debug("Scheduling eastern first round games");
		scheduleRoundFirstFourGames(playoffCalendar, getLeague().getPlayoff().getEastFirstRound(), startDate);
		logger.debug("Scheduling western first round games");
		scheduleRoundFirstFourGames(playoffCalendar, getLeague().getPlayoff().getWestFirstRound(), startDate);

		NBACalendar newCalendar = new NBACalendar(playoffCalendar);
		logger.info("First round playoff calendar built with " + playoffCalendar.size() + " game days");
		return newCalendar;
	}

	@Override
	protected void generateGames() {
		if (getLeague() == null || getLeague().getPlayoff() == null) {
			logger.warn("Skipping first round playoff game generation because league or playoff is null");
			return;
		}

		logger.debug("Generating expected games for first round playoffs");
		PlayoffGameGenerator.generateFirstRoundPlayoffGames(getLeague().getPlayoff());

	}

}

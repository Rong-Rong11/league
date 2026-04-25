package process.builder.calendar;

import java.time.LocalDate;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.league.League;
import log.LoggerUtility;
import process.builder.calendar.generator.PlayoffGameGenerator;

public class ConferenceFinalCalendarBuilder extends PlayoffCalendarBuilder {
	private static final Logger logger = LoggerUtility.getLogger(ConferenceFinalCalendarBuilder.class, "text");

	private LocalDate roundEndDate;

	public ConferenceFinalCalendarBuilder(League league, LocalDate roundEndDate) {
		super(league);
		this.roundEndDate = roundEndDate;
	}

	@Override
	protected NBACalendar build() {
		if (roundEndDate == null) {
			logger.warn("Skipping conference finals calendar build because round end date is null");
			return null;
		}

		logger.info("Building conference finals calendar");
		TreeMap<LocalDate, GameDay> playoffCalendar = new TreeMap<>();

		LocalDate startDate = roundEndDate.plusDays(2);
		logger.debug("Conference finals start date set to " + startDate);

		logger.debug("Scheduling eastern conference finals games");
		scheduleRoundFirstFourGames(playoffCalendar, getLeague().getPlayoff().getEastConferenceFinals(), startDate);
		logger.debug("Scheduling western conference finals games");
		scheduleRoundFirstFourGames(playoffCalendar, getLeague().getPlayoff().getWestConferenceFinals(), startDate);

		NBACalendar newCalendar = new NBACalendar(playoffCalendar);
		logger.info("Conference finals calendar built with " + playoffCalendar.size() + " game days");
		return newCalendar;
	}

	@Override
	protected void generateGames() {
		if (getLeague() == null || getLeague().getPlayoff() == null) {
			logger.warn("Skipping conference finals game generation because league or playoff is null");
			return;
		}

		logger.debug("Generating expected games for conference finals round");
		PlayoffGameGenerator.generateConferenceFinalsPlayoffGames(getLeague().getPlayoff());

	}

}

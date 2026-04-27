package process.builder.calendar;

import java.time.LocalDate;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.league.League;
import log.LoggerUtility;
import process.builder.calendar.generator.PlayoffGameGenerator;

public class NbaFinalCalendarBuilder extends PlayoffCalendarBuilder {
	private static final Logger logger = LoggerUtility.getLogger(NbaFinalCalendarBuilder.class, "text");

	private LocalDate roundEndDate;

	public NbaFinalCalendarBuilder(League league, LocalDate roundEndDate) {
		super(league);
		this.roundEndDate = roundEndDate;
	}

	@Override
	protected NBACalendar build() {
		if (roundEndDate == null) {
			logger.warn("Skipping NBA finals calendar build because round end date is null");
			return null;
		}

		logger.info("Building NBA finals calendar");
		TreeMap<LocalDate, GameDay> playoffCalendar = new TreeMap<>();

		LocalDate startDate = roundEndDate.plusDays(2);
		logger.debug("NBA finals start date set to " + startDate);

		logger.debug("Scheduling NBA finals games");
		scheduleRoundFirstFourGames(playoffCalendar, getLeague().getPlayoff().getNbaFinals(), startDate);

		NBACalendar newCalendar = new NBACalendar(playoffCalendar);
		logger.info("NBA finals calendar built with " + playoffCalendar.size() + " game days");
		return newCalendar;
	}

	@Override
	protected void generateGames() {
		if (getLeague() == null || getLeague().getPlayoff() == null) {
			logger.warn("Skipping NBA finals game generation because league or playoff is null");
			return;
		}

		logger.debug("Generating expected games for NBA finals round");
		PlayoffGameGenerator.generateNbaFinalsPlayoffGames(getLeague().getPlayoff());
	}
}

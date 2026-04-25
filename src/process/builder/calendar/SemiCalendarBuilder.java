package process.builder.calendar;

import java.time.LocalDate;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.league.League;
import log.LoggerUtility;
import process.builder.calendar.generator.PlayoffGameGenerator;

public class SemiCalendarBuilder extends PlayoffCalendarBuilder {
	private static final Logger logger = LoggerUtility.getLogger(SemiCalendarBuilder.class, "text");

	private LocalDate roundEndDate;

	public SemiCalendarBuilder(League league, LocalDate roundEndDate) {
		super(league);
		this.roundEndDate = roundEndDate;
	}

	@Override
	protected NBACalendar build() {
		if (roundEndDate == null) {
			logger.warn("Skipping conference semifinals calendar build because round end date is null");
			return null;
		}

		logger.info("Building conference semifinals calendar");
		TreeMap<LocalDate, GameDay> playoffCalendar = new TreeMap<>();

		LocalDate startDate = roundEndDate.plusDays(2);
		logger.debug("Conference semifinals start date set to " + startDate);

		logger.debug("Scheduling eastern conference semifinals games");
		scheduleRoundFirstFourGames(playoffCalendar, getLeague().getPlayoff().getEastConferenceSemis(), startDate);
		logger.debug("Scheduling western conference semifinals games");
		scheduleRoundFirstFourGames(playoffCalendar, getLeague().getPlayoff().getWestConferenceSemis(), startDate);

		NBACalendar newCalendar = new NBACalendar(playoffCalendar);
		logger.info("Conference semifinals calendar built with " + playoffCalendar.size() + " game days");
		return newCalendar;
	}

	@Override
	protected void generateGames() {
		if (getLeague() == null || getLeague().getPlayoff() == null) {
			logger.warn("Skipping conference semifinals game generation because league or playoff is null");
			return;
		}

		logger.debug("Generating expected games for conference semifinals round");
		PlayoffGameGenerator.generateSecondRoundPlayoffGames(getLeague().getPlayoff());

	}

}

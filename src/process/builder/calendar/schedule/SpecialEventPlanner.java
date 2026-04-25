package process.builder.calendar.schedule;

import org.apache.log4j.Logger;

import config.CalendarConfiguration;
import data.calendar.SpecialEvent;
import data.league.RegularSeason;
import log.LoggerUtility;
import process.utility.CalendarUtility;

public class SpecialEventPlanner {
	private static final Logger logger = LoggerUtility.getLogger(SpecialEventPlanner.class, "text");

	public static void specialEventsPlacement(RegularSeason regularSeason) {
		if (regularSeason == null) {
			logger.warn("Skipping special events placement because regular season is null");
			return;
		}

		logger.info("Placing regular season special events");

		SpecialEvent christmas = new SpecialEvent(CalendarConfiguration.CHRISTMAS_DAY, "christmas");
		logger.trace("Adding special event christmas on " + CalendarConfiguration.CHRISTMAS_DAY);
		regularSeason.addSpecialEvents(christmas);

		SpecialEvent openingNight = new SpecialEvent(regularSeason.getDebutDate(), "opening night");
		logger.trace("Adding special event opening night on " + regularSeason.getDebutDate());
		regularSeason.addSpecialEvents(openingNight);

		SpecialEvent endingNight = new SpecialEvent(regularSeason.getEndDate(), "ending night");
		logger.trace("Adding special event ending night on " + regularSeason.getEndDate());
		regularSeason.addSpecialEvents(endingNight);

		SpecialEvent mlkDay = new SpecialEvent(CalendarUtility.getMLKDay(), "mlk day");
		logger.trace("Adding special event mlk day on " + CalendarUtility.getMLKDay());
		regularSeason.addSpecialEvents(mlkDay);

		logger.info("Regular season special events placed successfully");
	}
}

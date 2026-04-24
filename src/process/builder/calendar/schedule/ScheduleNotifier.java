package process.builder.calendar.schedule;

import java.time.LocalDate;

import org.apache.log4j.Logger;

import data.sport.setup.Game;
import log.LoggerUtility;

public class ScheduleNotifier {
	private static final Logger logger = LoggerUtility.getLogger(ScheduleNotifier.class, "text");

	public static void notifySchedule(LocalDate date, Game game) {
		if (date == null) {
			logger.warn("Skipping schedule notification because date is null");
			return;
		}
		if (game == null) {
			logger.warn("Skipping schedule notification because game is null");
			return;
		}

		logger.trace("Scheduling game "
				+ game.getGameContext().getHomeTeam().getName()
				+ " vs "
				+ game.getGameContext().getAwayTeam().getName()
				+ " on "
				+ date);
		game.getGameContext().setScheduled(true);
		game.getGameContext().getHomeTeam().getSchedule().scheduleGame(date, game);
		game.getGameContext().getAwayTeam().getSchedule().scheduleGame(date, game);
	}
}

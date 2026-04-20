package process.builder.calendar.tools;

import java.time.LocalDate;

import data.sport.setup.Game;

public class ScheduleNotifier {
	public static void notifySchedule(LocalDate date, Game game) {
	  game.getGameContext().setScheduled(true);
	  game.getGameContext().getHomeTeam().getSchedule().scheduleGame(date, game);
	  game.getGameContext().getAwayTeam().getSchedule().scheduleGame(date, game);
	}
}

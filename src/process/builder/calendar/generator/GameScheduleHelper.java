package process.builder.calendar.generator;

import org.apache.log4j.Logger;

import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.team.Team;
import log.LoggerUtility;

public final class GameScheduleHelper {
	private static final Logger logger = LoggerUtility.getLogger(GameScheduleHelper.class, "text");

	public static Game createGame(Team homeTeam, Team awayTeam, int gameType) {
		return new Game(new GameContext(homeTeam, awayTeam, gameType));
	}

	public static void addGameToTeam(Game game, Team team) {
		logger.trace("Adding game to team "
				+ team.getName()
				+ " schedule as "
				+ (game.getGameContext().getHomeTeam().getName().equals(team.getName()) ? "home" : "away")
				+ " game");
		if (game.getGameContext().getHomeTeam().getName().equals(team.getName())) {
			team.getSchedule().incrementNumberOfHomeGames();
		} else {
			team.getSchedule().incrementNumberOfAwayGames();
		}
		team.addGame(game);
	}
}

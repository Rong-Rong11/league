package process.builder.calendar.selector;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.sport.setup.Game;
import data.team.Team;
import log.LoggerUtility;

public class GameScheduleConstraintChecker {
	private static final Logger logger = LoggerUtility.getLogger(GameScheduleConstraintChecker.class, "text");

	public boolean canBeScheduled(Game game, LocalDate date) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();

		if (homeTeam.getSchedule().isPlayingOn(date) || awayTeam.getSchedule().isPlayingOn(date)) {
			logger.trace("Game "
					+ homeTeam.getName()
					+ " vs "
					+ awayTeam.getName()
					+ " cannot be scheduled on "
					+ date
					+ " because a team is already playing");
			return false;
		}

		int homeDays = homeTeam.getSchedule().daysSinceLastGame(date);
		int awayDays = awayTeam.getSchedule().daysSinceLastGame(date);
		if (homeDays <= 0 || awayDays <= 0) {
			logger.trace("Game "
					+ homeTeam.getName()
					+ " vs "
					+ awayTeam.getName()
					+ " cannot be scheduled on "
					+ date
					+ " because rest days are insufficient");
			return false;
		}

		return true;
	}

	public boolean conflictWithSelected(Game game, ArrayList<Game> selectedGames) {
		Team home = game.getGameContext().getHomeTeam();
		Team away = game.getGameContext().getAwayTeam();
		for (Game selected : selectedGames) {
			if (selected.getGameContext().getHomeTeam() == home ||
					selected.getGameContext().getAwayTeam() == home ||
					selected.getGameContext().getHomeTeam() == away ||
					selected.getGameContext().getAwayTeam() == away) {
				logger.trace("Game "
						+ home.getName()
						+ " vs "
						+ away.getName()
						+ " conflicts with already selected games");
				return true;
			}
		}
		return false;
	}

	public boolean playedRecentlyAgainst(Team teamA, Team teamB, LocalDate localDate, int numberOfDays) {
		LocalDate startDate = localDate.minusDays(numberOfDays);

		for (LocalDate gameDate : teamA.getSchedule().getScheduledGames().keySet()) {
			if ((gameDate.isEqual(startDate) || gameDate.isAfter(startDate)) && gameDate.isBefore(localDate)) {
				Game scheduledGame = teamA.getSchedule().getScheduledGames().get(gameDate);

				Team scheduledHome = scheduledGame.getGameContext().getHomeTeam();
				Team scheduledAway = scheduledGame.getGameContext().getAwayTeam();

				boolean sameMatchup = (scheduledHome == teamA && scheduledAway == teamB) ||
						(scheduledHome == teamB && scheduledAway == teamA);

				if (sameMatchup) {
					logger.trace("Teams "
							+ teamA.getName()
							+ " and "
							+ teamB.getName()
							+ " played recently before "
							+ localDate);
					return true;
				}
			}
		}

		return false;
	}
}

package process.builder.calendar.selector;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.sport.setup.Game;
import data.team.Team;
import data.team.calendar.Schedule;
import log.LoggerUtility;
import process.repository.TeamRepository;

public class GameCandidateFinder {
	private static final Logger logger = LoggerUtility.getLogger(GameCandidateFinder.class, "text");

	private TeamRepository teamRepository = TeamRepository.getInstance();
	private GameScheduleConstraintChecker constraintChecker;

	public GameCandidateFinder(GameScheduleConstraintChecker constraintChecker) {
		this.constraintChecker = constraintChecker;
	}

	public ArrayList<Game> getCandidates(LocalDate date) {
		logger.debug("Collecting candidate games for " + date);
		ArrayList<Game> candidates = new ArrayList<Game>();

		for (Team team : teamRepository.getAllTeams()) {
			for (Game game : getUnscheduledGames(team.getSchedule())) {
				if (!candidates.contains(game) && constraintChecker.canBeScheduled(game, date)) {
					candidates.add(game);
					logger.trace("Added candidate game "
							+ game.getGameContext().getHomeTeam().getName()
							+ " vs "
							+ game.getGameContext().getAwayTeam().getName());
				}
			}
		}
		logger.debug("Collected " + candidates.size() + " candidate games for " + date);
		return candidates;
	}

	public static ArrayList<Game> getUnscheduledGames(Schedule schedule) {
		ArrayList<Game> unscheduledGames = new ArrayList<Game>();
		for (Game game : schedule.getGames()) {
			if (!schedule.getScheduledGames().containsValue(game)) {
				unscheduledGames.add(game);
			}
		}
		return unscheduledGames;
	}

	public static int getNumberOfRemainingUnscheduledGames(Schedule schedule) {
		int remaining = 0;
		for (Game game : schedule.getGames()) {
			if (!schedule.getScheduledGames().containsValue(game)) {
				remaining++;
			}
		}
		return remaining;
	}
}

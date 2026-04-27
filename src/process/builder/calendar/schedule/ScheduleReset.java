package process.builder.calendar.schedule;

import org.apache.log4j.Logger;

import data.team.Team;
import log.LoggerUtility;
import process.repository.TeamRepository;

public class ScheduleReset {
	private static final Logger logger = LoggerUtility.getLogger(ScheduleReset.class, "text");
	private TeamRepository teamRepositery = TeamRepository.getInstance();

	public void initialization() {
		logger.debug("Resetting schedules for all teams");
		logger.debug("Resetting schedules for " + this.teamRepositery.getAllTeams().size() + " teams");
		for (Team team : this.teamRepositery.getAllTeams()) {
			if (team == null) {
				logger.warn("Skipping schedule reset because team is null");
				continue;
			}
			clearSchedule(team);
		}
		logger.debug("All team schedules reset successfully");
	}

	private void clearSchedule(Team team) {
		logger.trace("Clearing schedule counters and planned games for " + team.getName());
		team.getSchedule().setNumberOfAwayGames(0);
		team.getSchedule().setNumberOfHomeGames(0);
		team.getSchedule().setNumberOfPlayedGames(0);
		team.getSchedule().clearGames();
		team.getSchedule().clearScheduledGames();
	}
}

package process.builder.calendartools;

import data.team.Team;
import process.repositery.TeamRepositery;

public class ScheduleReset {
	private TeamRepositery teamRepositery = TeamRepositery.getInstance();

	public void initialization() {
		for (Team team : teamRepositery.getAllTeams()) {
			clearSchedule(team);
		}
	}

	private void clearSchedule(Team team) {
		team.getSchedule().setNumberOfAwayGames(0);
		team.getSchedule().setNumberOfHomeGames(0);
		team.getSchedule().setNumberOfPlayedGames(0);
		team.getSchedule().clearGames();
		team.getSchedule().clearScheduledGames();
	}
}

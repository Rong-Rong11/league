package process.builder.calendar.tools;

import data.team.Team;
import process.repository.TeamRepository;

public class ScheduleReset {
    private TeamRepository teamRepositery = TeamRepository.getInstance();

    public void initialization() {
        for (Team team : this.teamRepositery.getAllTeams()) {
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

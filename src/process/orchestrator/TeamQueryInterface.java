package process.orchestrator;

import java.util.ArrayList;

import data.team.Team;

public interface TeamQueryInterface {

   ArrayList<Team> getTeams();

   Team getTeamByName(String teamName);

   String getConferenceName(Team team);

   String getDivisionName(Team team);

   double getAverageNote(Team team);

   double getAveragePoints(Team team, boolean currentSeasonSelected);

   String getTeamAbbreviation(String teamName);

   void refreshTeamPayroll(Team team);
}

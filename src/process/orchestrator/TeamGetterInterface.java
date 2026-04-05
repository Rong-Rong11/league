package process.orchestrator;

import java.util.ArrayList;

import data.team.Team;

public interface TeamGetterInterface {

   ArrayList<Team> getTeams();

   Team getTeamByName(String teamName);

   String getConferenceName(Team team);

   String getDivisionName(Team team);

   double getAverageNote(Team team);

   double getAveragePoints(Team team, boolean currentSeasonSelected);

   String getTeamAbbreviation(String teamName);

   double getTeamCurrentPayroll(Team team);

   double getTeamCurrentWinStreak(Team team);

   double getTeamCurrentLoseStreak(Team team);

   double getTeamMaxWinStreak(Team team);

   double getTeamMaxLoseStreak(Team team);

   int getTeamNumberWin(Team team);

   int getTeamNumberLose(Team team);

   int getTeamNumberPlayedGames(Team team);

   ArrayList<Boolean> getTeamLast4GamesResults(Team team, int numberOfGames);
}

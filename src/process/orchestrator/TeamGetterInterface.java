package process.orchestrator;

import java.util.ArrayList;

import data.team.Team;

public interface TeamGetterInterface {

   ArrayList<Team> getTeams();

   ArrayList<Team> getGlobalRanking();

   ArrayList<Team> getEastRanking();

   ArrayList<Team> getWestRanking();

   Team getTeamByName(String teamName);

   String getConferenceName(Team team);

   String getDivisionName(Team team);

   double getAverageNote(Team team);

   double getAveragePoints(Team team, boolean currentSeasonSelected);

   String getTeamAbbreviation(String teamName);

   double getTeamCurrentPayroll(Team team);

   int getTeamCurrentWinStreak(Team team);

   int getTeamCurrentLoseStreak(Team team);

   int getTeamMaxWinStreak(Team team);

   int getTeamMaxLoseStreak(Team team);

   int getTeamNumberWin(Team team);

   int getTeamNumberLose(Team team);

   int getTeamNumberPlayedGames(Team team);

   ArrayList<Boolean> getTeamLastGamesResults(Team team, int numberOfGames);
}

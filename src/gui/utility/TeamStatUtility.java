package gui.utility;

import java.util.ArrayList;

import data.league.League;
import data.player.Player;
import data.sport.setup.Game;
import data.team.Team;
import process.repository.TeamRepository;

public class TeamStatUtility {

   public static double getAverageNote(Team team) {
      double total = 0;
      int count = 0;

      for (Player player : team.getCurrentPlayers().values()) {
         total += PlayerStatUtility.getDisplayedNote(player);
         count++;
      }

      if (count == 0) {
         return 0;
      }
      return total / count;
   }

   public static double getAveragePoints(Team team, boolean currentSeasonSelected) {
      double total = 0;
      int count = 0;

      for (Player player : team.getCurrentPlayers().values()) {
         total += PlayerStatUtility.getDisplayedAssets(player, currentSeasonSelected).getPointPerMatch();
         count++;
      }

      if (count == 0) {
         return 0;
      }
      return total / count;
   }

   public static double getAverageRebounds(Team team, boolean currentSeasonSelected) {
      double total = 0;
      int count = 0;

      for (Player player : team.getCurrentPlayers().values()) {
         total += PlayerStatUtility.getDisplayedAssets(player, currentSeasonSelected).getReboundPerMatch();
         count++;
      }

      if (count == 0) {
         return 0;
      }
      return total / count;
   }

   public static double getAverageAssists(Team team, boolean currentSeasonSelected) {
      double total = 0;
      int count = 0;

      for (Player player : team.getCurrentPlayers().values()) {
         total += PlayerStatUtility.getDisplayedAssets(player, currentSeasonSelected).getAssistPerMatch();
         count++;
      }

      if (count == 0) {
         return 0;
      }
      return total / count;
   }

   public static ArrayList<Boolean> getLastResults(Team team, int numberOfGames) {
      ArrayList<Boolean> results = new ArrayList<Boolean>();

      if (team == null || team.getSchedule() == null || numberOfGames <= 0) {
         return results;
      }

      for (Game game : team.getSchedule().getScheduledGames().descendingMap().values()) {
         if (game.getWinner() == null) {
            continue;
         }

         results.add(game.getWinner().equals(team));
         if (results.size() == numberOfGames) {
            break;
         }
      }
      return results;
   }

   public static int getBestWinStreak(Team team) {
      if (team == null || team.getTeamPerformance() == null) {
         return 0;
      }
      return Math.max(team.getTeamPerformance().getCurrentWinStreak(), team.getTeamPerformance().getMaxWinsStreak());
   }

   public static int getBestLoseStreak(Team team) {
      if (team == null || team.getTeamPerformance() == null) {
         return 0;
      }
      return Math.max(team.getTeamPerformance().getCurrentLoseStreak(), team.getTeamPerformance().getMaxLoseStreak());
   }

   public static Team findTeamByName(String teamName) {
      return TeamRepository.getInstance().getTeam(teamName);
   }

   public static String getConferenceName(Team team, League league) {
      if (team == null || team.getConference() == null || team.getConference().equals("")) {
         return "-";
      }
      if ("Ouest".equals(team.getConference())) {
         return "Ouest";
      }
      if ("Est".equals(team.getConference())) {
         return "Est";
      }
      return "-";
   }

   public static String getDivisionName(Team team, League league) {
      if (team == null || team.getDivision() == null || team.getDivision().equals("")) {
         return "-";
      }
      return team.getDivision();
   }
}

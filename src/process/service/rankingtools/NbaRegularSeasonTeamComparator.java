package process.service.rankingtools;

import data.league.Division;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import process.utility.TeamUtilitary;

import java.util.ArrayList;
import java.util.Comparator;

public class NbaRegularSeasonTeamComparator implements Comparator<Team> {

   private final ArrayList<Game> simulatedGames;
   private final League league;

   public NbaRegularSeasonTeamComparator(ArrayList<Game> simulatedGames, League league) {
      this.simulatedGames = simulatedGames;
      this.league = league;
   }

   @Override
   public int compare(Team teamA, Team teamB) {

      int result = Double.compare(getWinRate(teamB), getWinRate(teamA));
      if (result != 0) {
         return result;
      }

      result = Integer.compare(
            getHeadToHeadWins(teamB, teamA),
            getHeadToHeadWins(teamA, teamB));
      if (result != 0) {
         return result;
      }

      result = Boolean.compare(
            isDivisionChampion(teamB),
            isDivisionChampion(teamA));
      if (result != 0) {
         return result;
      }

      if (isSameDivision(teamA, teamB)) {
         result = Double.compare(
               getDivisionWinRate(teamB),
               getDivisionWinRate(teamA));
         if (result != 0) {
            return result;
         }
      }

      result = Double.compare(
            getConferenceWinRate(teamB),
            getConferenceWinRate(teamA));
      if (result != 0) {
         return result;
      }

      result = Integer.compare(
            getPointDifferential(teamB),
            getPointDifferential(teamA));
      if (result != 0) {
         return result;
      }

      result = Integer.compare(
            teamB.getTeamPerformance().getNumberWin(),
            teamA.getTeamPerformance().getNumberWin());
      if (result != 0) {
         return result;
      }

      return teamA.getName().compareTo(teamB.getName());
   }

   private double getWinRate(Team team) {
      int wins = team.getTeamPerformance().getNumberWin();
      int games = team.getTeamPerformance().getNumberPlayedGames();

      if (games == 0) {
         return 0.0;
      }

      return (double) wins / games;
   }

   private int getHeadToHeadWins(Team teamA, Team teamB) {
      int wins = 0;

      for (Game game : simulatedGames) {
         if (!isPlayed(game)) {
            continue;
         }

         boolean sameMatchup = (game.getGameContext().getHomeTeam().equals(teamA)
               && game.getGameContext().getAwayTeam().equals(teamB))
               || (game.getGameContext().getHomeTeam().equals(teamB)
                     && game.getGameContext().getAwayTeam().equals(teamA));

         if (!sameMatchup) {
            continue;
         }

         Team winner = getWinner(game);
         if (teamA.equals(winner)) {
            wins++;
         }
      }

      return wins;
   }

   private boolean isDivisionChampion(Team team) {
      Division division = TeamUtilitary.getDivisionOfTeam(league, team);

      if (division == null) {
         return false;
      }

      for (Team otherTeam : division.getTeams().values()) {
         if (otherTeam.equals(team)) {
            continue;
         }

         int comparison = Double.compare(getWinRate(otherTeam), getWinRate(team));
         if (comparison > 0) {
            return false;
         }

         if (comparison == 0) {
            int headToHeadComparison = Integer.compare(
                  getHeadToHeadWins(otherTeam, team),
                  getHeadToHeadWins(team, otherTeam));

            if (headToHeadComparison > 0) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean isSameDivision(Team teamA, Team teamB) {
      Division divisionA = TeamUtilitary.getDivisionOfTeam(league, teamA);
      Division divisionB = TeamUtilitary.getDivisionOfTeam(league, teamB);

      if (divisionA == null || divisionB == null) {
         return false;
      }

      return divisionA.equals(divisionB);
   }

   private double getDivisionWinRate(Team team) {
      int wins = 0;
      int games = 0;

      for (Game game : simulatedGames) {
         if (!isPlayed(game)) {
            continue;
         }

         if (!involvesTeam(game, team)) {
            continue;
         }

         Team opponent = getOpponent(game, team);
         if (opponent == null || !isSameDivision(team, opponent)) {
            continue;
         }

         games++;

         Team winner = getWinner(game);
         if (team.equals(winner)) {
            wins++;
         }
      }

      if (games == 0) {
         return 0.0;
      }

      return (double) wins / games;
   }

   private double getConferenceWinRate(Team team) {
      int wins = 0;
      int games = 0;

      for (Game game : simulatedGames) {
         if (!isPlayed(game)) {
            continue;
         }

         if (!involvesTeam(game, team)) {
            continue;
         }

         Team opponent = getOpponent(game, team);
         if (opponent == null) {
            continue;
         }

         games++;

         Team winner = getWinner(game);
         if (team.equals(winner)) {
            wins++;
         }
      }

      if (games == 0)

      {
         return 0.0;
      }

      return (double) wins / games;
   }

   private int getPointDifferential(Team team) {
      int pointsScored = 0;
      int pointsAllowed = 0;

      for (Game game : simulatedGames) {
         if (!isPlayed(game)) {
            continue;
         }

         if (game.getGameContext().getHomeTeam().equals(team)) {
            pointsScored += game.getHomeFinalScore();
            pointsAllowed += game.getAwayFinalScore();
         } else if (game.getGameContext().getAwayTeam().equals(team)) {
            pointsScored += game.getAwayFinalScore();
            pointsAllowed += game.getHomeFinalScore();
         }
      }

      return pointsScored - pointsAllowed;
   }

   private boolean involvesTeam(Game game, Team team) {
      return game.getGameContext().getHomeTeam().equals(team)
            || game.getGameContext().getAwayTeam().equals(team);
   }

   private Team getOpponent(Game game, Team team) {
      if (game.getGameContext().getHomeTeam().equals(team)) {
         return game.getGameContext().getAwayTeam();
      }

      if (game.getGameContext().getAwayTeam().equals(team)) {
         return game.getGameContext().getHomeTeam();
      }

      return null;
   }

   private Team getWinner(Game game) {
      if (!isPlayed(game)) {
         return null;
      }

      if (game.getHomeFinalScore() > game.getAwayFinalScore()) {
         return game.getGameContext().getHomeTeam();
      }

      if (game.getAwayFinalScore() > game.getHomeFinalScore()) {
         return game.getGameContext().getAwayTeam();
      }

      return null;
   }

   private boolean isPlayed(Game game) {
      return game.getHomeFinalScore() >= 0 && game.getAwayFinalScore() >= 0;
   }
}
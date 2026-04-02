package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import process.builder.PlayoffBuilder;
import process.builder.PlayoffCalendarBuilder;

public abstract class PlayoffManager {
   private League league;
   private PlayoffCalendarBuilder currentRoundCalendarBuilder;
   private PlayoffBuilder playoffBuilder;

   public PlayoffManager(League league, PlayoffCalendarBuilder currentRoundCalendarBuilder,
         PlayoffBuilder playoffBuilder) {
      this.league = league;
      this.currentRoundCalendarBuilder = currentRoundCalendarBuilder;
      this.playoffBuilder = playoffBuilder;
   }

   public void handlePlayedGame(Game game, LocalDate gameDate) {
      PlayoffSeries series = findSeriesByGame(game);
      if (series == null || series.isFinished()) {
         return;
      }

      updateSeries(series, game);

      if (series.isFinished()) {
         if (isManagedRoundFinished()) {
            advanceToNextRound(gameDate);
         }
         return;
      }

      TreeMap<LocalDate, GameDay> playoffCalendar = league.getPlayoff().getNbaCalendar().getCalendar();
      currentRoundCalendarBuilder.scheduleNextGameIfNecessary(playoffCalendar, series, gameDate);
   }

   private PlayoffSeries findSeriesByGame(Game game) {
      for (PlayoffSeries series : getManagedSeries()) {
         if (containsGame(series, game)) {
            return series;
         }
      }
      return null;
   }

   private void updateSeries(PlayoffSeries series, Game game) {
      Team winner = getWinner(game);

      if (winner == null) {
         return;
      }

      if (winner.equals(series.getHigherTeam())) {
         series.setHigherTeamWins(series.getHigherTeamWins() + 1);
      } else if (winner.equals(series.getLowerTeam())) {
         series.setLowerTeamWins(series.getLowerTeamWins() + 1);
      } else {
         return;
      }

      series.setNumberPlayedGames(series.getNumberPlayedGames() + 1);
      if (series.getHigherTeamWins() >= 4 || series.getLowerTeamWins() >= 4) {
         series.setFinished(true);
      }
   }

   private boolean isManagedRoundFinished() {
      ArrayList<PlayoffSeries> managedSeries = getManagedSeries();
      if (managedSeries.isEmpty()) {
         return false;
      }

      for (PlayoffSeries series : managedSeries) {
         if (!series.isFinished()) {
            return false;
         }
      }
      return true;
   }

   public Team getSeriesWinner(PlayoffSeries series) {
      if (!series.isFinished()) {
         return null;
      }
      if (series.getHigherTeamWins() > series.getLowerTeamWins()) {
         return series.getHigherTeam();
      }
      return series.getLowerTeam();
   }

   private Team getWinner(Game game) {
      if (game.getHomeFinalScore() == game.getAwayFinalScore()) {
         return null;
      }
      if (game.getHomeFinalScore() > game.getAwayFinalScore()) {
         return game.getGameContext().getHomeTeam();
      }
      return game.getGameContext().getAwayTeam();
   }

   private boolean containsGame(PlayoffSeries series, Game game) {
      for (Game expectedGame : series.getExpectedGames()) {
         if (expectedGame == game) {
            return true;
         }
      }
      return false;
   }

   public League getLeague() {
      return league;
   }

   public PlayoffBuilder getPlayoffBuilder() {
      return playoffBuilder;
   }

   public abstract ArrayList<PlayoffSeries> getManagedSeries();

   public abstract void advanceToNextRound(LocalDate roundEndDate);

   public PlayoffCalendarBuilder getCurrentRoundCalendarBuilder() {
      return currentRoundCalendarBuilder;
   }
}

package process.service.submanager;

import java.time.LocalDate;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.league.League;
import data.league.Playoff;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import process.builder.CalendarBuilder;

public class PlayoffManager {
   private League league;
   private CalendarBuilder calendarBuilder;

   public PlayoffManager(League league, CalendarBuilder calendarBuilder) {
      this.league = league;
      this.calendarBuilder = calendarBuilder;
   }

   public void handlePlayedGame(Game game, LocalDate gameDate) {
      PlayoffSeries series = findSeriesByGame(game);
      if (series == null || series.isFinished()) {
         return;
      }
      updateSeries(game, series);
      if (series.isFinished()) {
         tryAdvanceRound();
      } else {
         TreeMap<LocalDate, GameDay> playoffCalendar = league.getPlayoff().getNbaCalendar().getCalendar();
         calendarBuilder.scheduleNextGameIfNecessary(playoffCalendar, series, gameDate);
      }
   }

   private void tryAdvanceRound() {
      // a compléter plus tard
   }

   public void updateSeries(Game game, PlayoffSeries series) {
      Team winner = game.getWinner();

      if (winner.equals(series.getHigherTeam())) {
         series.setHigherTeamWins(series.getHigherTeamWins() + 1);
      } else if (winner.equals(series.getLowerTeam())) {
         series.setLowerTeamWins(series.getLowerTeamWins() + 1);
      }
      series.setNumberPlayedGames(series.getNumberPlayedGames() + 1);
      if (series.getHigherTeamWins() == 4 || series.getLowerTeamWins() == 4) {
         series.setFinished(true);
      }
   }

   private PlayoffSeries findSeriesByGame(Game game) {
      Playoff playoff = league.getPlayoff();
      for (PlayoffSeries series : playoff.getEastFirstRound()) {
         if (containsGame(series, game)) {
            return series;
         }
      }
      for (PlayoffSeries series : playoff.getWestFirstRound()) {
         if (containsGame(series, game)) {
            return series;
         }
      }
      return null;
   }

   private boolean containsGame(PlayoffSeries series, Game game) {
      for (Game expectedGame : series.getExpectedGames()) {
         if (expectedGame == game) {
            return true;
         }
      }
      return false;
   }

}

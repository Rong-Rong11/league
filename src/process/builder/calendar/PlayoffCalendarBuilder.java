package process.builder.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import process.builder.calendar.tools.ScheduleNotifier;

public abstract class PlayoffCalendarBuilder extends CalendarBuilder {

   public PlayoffCalendarBuilder(League league) {
      super(league);
   }

   protected void scheduleRoundFirstFourGames(TreeMap<LocalDate, GameDay> playoffCalendar,
         ArrayList<PlayoffSeries> roundSeries,
         LocalDate startDate) {

      int[] gameOffsets = { 0, 2, 4, 7 };
      int[] seriesStartOffsets = { 0, 0, 1, 1 };

      for (int seriesIndex = 0; seriesIndex < roundSeries.size(); seriesIndex++) {
         PlayoffSeries series = roundSeries.get(seriesIndex);
         Game[] expectedGames = series.getExpectedGames();

         int seriesStartOffset = (seriesIndex < seriesStartOffsets.length)
               ? seriesStartOffsets[seriesIndex]
               : seriesIndex % 2;

         for (int i = 0; i < 4; i++) {
            Game game = expectedGames[i];
            LocalDate gameDate = startDate.plusDays(seriesStartOffset + gameOffsets[i]);
            addGameToCalendar(playoffCalendar, game, gameDate);
            ScheduleNotifier.notifySchedule(gameDate, game);
         }
      }
   }

   public void scheduleNextGameIfNecessary(TreeMap<LocalDate, GameDay> playoffCalendar, PlayoffSeries series,
         LocalDate lastGameDate) {
      if (series.isFinished()) {
         return;
      }
      int nextGameIndex = series.getNumberPlayedGames();
      Game[] expectedGames = series.getExpectedGames();
      if (nextGameIndex >= expectedGames.length) {
         return;
      }
      Game nextGame = expectedGames[nextGameIndex];
      LocalDate nextDate = lastGameDate.plusDays(2);
      addGameToCalendar(playoffCalendar, nextGame, nextDate);
      ScheduleNotifier.notifySchedule(nextDate, nextGame);
   }

   protected void addGameToCalendar(TreeMap<LocalDate, GameDay> playoffCalendar, Game game, LocalDate gameDate) {
      if (!playoffCalendar.containsKey(gameDate)) {
         GameDay gameDay = new GameDay(gameDate);
         gameDay.addGame(game);
         playoffCalendar.put(gameDate, gameDay);
      } else {
         playoffCalendar.get(gameDate).addGame(game);
      }
   }

}

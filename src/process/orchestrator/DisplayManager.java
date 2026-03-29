package process.orchestrator;

import java.time.LocalDate;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.league.League;
import data.sport.setup.Game;

public class DisplayManager implements DisplayInterface {
   private League league;

   public DisplayManager(League league) {
      this.league = league;
   }

   @Override
   public void displayCurrentSeason() {
      TreeMap<LocalDate, GameDay> calendar = league.getReagularSeason().getNbaCalendar()
            .getCalendar();
      for (GameDay gameDay : calendar.values()) {
         gameDay.setDisplayed(true);
         for (Game game : gameDay.getGames()) {
            game.setDisplayed(true);
         }
      }
   }

   @Override
   public void displayWeek(LocalDate startDate) {
      if (startDate == null) {
         return;
      }
      for (int offset = 0; offset < 7; offset++) {
         displayGameDay(startDate.plusDays(offset));
      }
   }

   @Override
   public void displayGameDay(LocalDate date) {
      if (date == null) {
         return;
      }
      GameDay gameDay = league.getReagularSeason().getNbaCalendar().getCalendar().get(date);
      if (gameDay != null) {
         gameDay.setDisplayed(true);
         for (data.sport.setup.Game game : gameDay.getGames()) {
            game.setDisplayed(true);
         }
      }
   }
}

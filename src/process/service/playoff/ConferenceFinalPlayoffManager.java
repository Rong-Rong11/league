package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;

import data.league.League;
import data.sport.setup.PlayoffSeries;
import process.builder.ConferenceFinalCalendarBuilder;
import process.builder.NbaFinalCalendarBuilder;
import process.builder.PlayoffBuilder;

public class ConferenceFinalPlayoffManager extends PlayoffManager {

   public ConferenceFinalPlayoffManager(League league,
         ConferenceFinalCalendarBuilder conferenceFinalCalendarBuilder,
         PlayoffBuilder playoffBuilder) {
      super(league, conferenceFinalCalendarBuilder, playoffBuilder);
   }

   @Override
   public ArrayList<PlayoffSeries> getManagedSeries() {
      ArrayList<PlayoffSeries> managedSeries = new ArrayList<PlayoffSeries>();
      managedSeries.addAll(getLeague().getPlayoff().getEastConferenceFinals());
      managedSeries.addAll(getLeague().getPlayoff().getWestConferenceFinals());
      return managedSeries;
   }

   @Override
   public void advanceToNextRound(LocalDate roundEndDate) {
      League league = getLeague();
      league.setPlayoff(getPlayoffBuilder().buildNbaFinalsPlayoffs());
      NbaFinalCalendarBuilder nbaFinalCalendarBuilder = new NbaFinalCalendarBuilder(league, roundEndDate);
      league.getPlayoff().setNbaCalendar(nbaFinalCalendarBuilder.buildCalendar());
   }
}

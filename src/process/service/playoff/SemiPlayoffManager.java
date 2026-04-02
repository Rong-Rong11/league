package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;

import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.PlayoffSeries;
import process.builder.calendar.ConferenceFinalCalendarBuilder;
import process.builder.calendar.SemiCalendarBuilder;
import process.builder.league.PlayoffBuilder;

public class SemiPlayoffManager extends PlayoffManager {

   public SemiPlayoffManager(League league,
         SemiCalendarBuilder semiCalendarBuilder,
         PlayoffBuilder playoffBuilder) {
      super(league, semiCalendarBuilder, playoffBuilder);
   }

   @Override
   public ArrayList<PlayoffSeries> getManagedSeries() {
      ArrayList<PlayoffSeries> managedSeries = new ArrayList<PlayoffSeries>();
      managedSeries.addAll(getLeague().getPlayoff().getEastConferenceSemis());
      managedSeries.addAll(getLeague().getPlayoff().getWestConferenceSemis());
      return managedSeries;
   }

   @Override
   public void advanceToNextRound(LocalDate roundEndDate) {
      League league = getLeague();
      league.setPlayoff(getPlayoffBuilder().buildConferenceFinalsPlayoffs());
      league.getPlayoff().setCurrentRound(PlayoffRound.CONFERENCE_FINALS);
      ConferenceFinalCalendarBuilder conferenceFinalCalendarBuilder = new ConferenceFinalCalendarBuilder(league,
            roundEndDate);
      league.getPlayoff().setNbaCalendar(conferenceFinalCalendarBuilder.buildCalendar());
   }
}

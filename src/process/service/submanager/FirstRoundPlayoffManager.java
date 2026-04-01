package process.service.submanager;

import java.time.LocalDate;
import java.util.ArrayList;

import data.league.League;
import data.sport.setup.PlayoffSeries;
import process.builder.FirstRoundCalendarBuilder;
import process.builder.PlayoffBuilder;
import process.builder.SemiCalendarBuilder;

public class FirstRoundPlayoffManager extends PlayoffManager {

   public FirstRoundPlayoffManager(League league,
         FirstRoundCalendarBuilder firstRoundPlayoffCalendarBuilder,
         PlayoffBuilder playoffBuilder) {
      super(league, firstRoundPlayoffCalendarBuilder, playoffBuilder);
   }

   @Override
   public ArrayList<PlayoffSeries> getManagedSeries() {
      ArrayList<PlayoffSeries> managedSeries = new ArrayList<PlayoffSeries>();
      managedSeries.addAll(getLeague().getPlayoff().getEastFirstRound());
      managedSeries.addAll(getLeague().getPlayoff().getWestFirstRound());
      return managedSeries;
   }

   @Override
   public void advanceToNextRound(LocalDate roundEndDate) {
      League league = getLeague();
      league.setPlayoff(getPlayoffBuilder().buldSecondRoundPlayoffs());
      SemiCalendarBuilder semiCalendarBuilder = new SemiCalendarBuilder(league, roundEndDate);
      league.getPlayoff().setNbaCalendar(semiCalendarBuilder.buildCalendar());
   }
}

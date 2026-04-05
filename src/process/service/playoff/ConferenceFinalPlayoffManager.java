package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;

import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.PlayoffSeries;
import process.builder.calendar.ConferenceFinalCalendarBuilder;
import process.builder.calendar.NbaFinalCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.leaguetools.TeamPopularityUpdater;

public class ConferenceFinalPlayoffManager extends PlayoffManager {

   public ConferenceFinalPlayoffManager(League league,
         ConferenceFinalCalendarBuilder conferenceFinalCalendarBuilder,
         PlayoffBuilder playoffBuilder,
         FinanceManager financeManager,
         TeamPopularityUpdater teamPopularityUpdater) {
      super(league, conferenceFinalCalendarBuilder, playoffBuilder, financeManager, teamPopularityUpdater);
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
      league.getPlayoff().setCurrentRound(PlayoffRound.NBA_FINALS);
      NbaFinalCalendarBuilder nbaFinalCalendarBuilder = new NbaFinalCalendarBuilder(league, roundEndDate);
      league.getPlayoff().setNbaCalendar(nbaFinalCalendarBuilder.buildCalendar());
   }
}

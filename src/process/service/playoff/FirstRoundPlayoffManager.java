package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;

import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.PlayoffSeries;
import process.builder.calendar.FirstRoundCalendarBuilder;
import process.builder.calendar.SemiCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.leaguetools.TeamPopularityUpdater;

public class FirstRoundPlayoffManager extends PlayoffManager {

   public FirstRoundPlayoffManager(League league,
         FirstRoundCalendarBuilder firstRoundPlayoffCalendarBuilder,
         PlayoffBuilder playoffBuilder,
         FinanceManager financeManager,
         TeamPopularityUpdater teamPopularityUpdater) {
      super(league, firstRoundPlayoffCalendarBuilder, playoffBuilder, financeManager, teamPopularityUpdater);
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
      league.getPlayoff().setCurrentRound(PlayoffRound.CONFERENCE_SEMIFINALS);
      SemiCalendarBuilder semiCalendarBuilder = new SemiCalendarBuilder(league, roundEndDate);
      league.getPlayoff().setNbaCalendar(semiCalendarBuilder.buildCalendar());
   }
}

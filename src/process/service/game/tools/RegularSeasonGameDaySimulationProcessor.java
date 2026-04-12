package process.service.game.tools;

import java.time.LocalDate;

import data.calendar.GameDay;
import data.league.League;
import data.league.Ranking;
import data.league.RegularSeason;
import data.sport.setup.Game;
import process.service.finance.FinanceManager;
import process.service.ranking.RegularSeasonRankingManager;
import process.simulator.GameSimulator;

public class RegularSeasonGameDaySimulationProcessor extends GameDaySimulationProcessor {

   private League league;
   private RegularSeasonRankingManager regularSeasonRankingManager;

   public RegularSeasonGameDaySimulationProcessor(
         League league,
         GameSimulator gameSimulator,
         FinanceManager financeManager,
         RegularSeasonRankingManager regularSeasonRankingManager) {
      super(gameSimulator, financeManager);
      this.league = league;
      this.regularSeasonRankingManager = regularSeasonRankingManager;
   }

   @Override
   protected void applyFinance(Game game, LocalDate date, int month) {
      financeManager.calculateRegularSeasonGame(game, date, month);
   }

   @Override
   protected void afterGame(Game game, LocalDate date) {

   }

   @Override
   protected void afterGameDay(GameDay gameDay, LocalDate date, int month) {
      RegularSeason regularSeason = league.getRegularSeason();
      Ranking ranking = regularSeason.getRanking();

      regularSeasonRankingManager.addSimulatedGameDay(gameDay);
      regularSeason.setRanking(
            regularSeasonRankingManager.updateRanking(
                  league,
                  ranking,
                  regularSeason.getNbaCalendar().getCalendar(),
                  date));
   }

}

package process.service.finance.tools.game.processor;

import data.finance.GameStat;
import data.league.PlayoffRound;
import process.service.finance.tools.game.GameExpenseCalculator;
import process.service.finance.tools.game.GameRevenueCalculator;
import process.service.finance.tools.game.PlayoffGameExpenseCalculator;
import process.service.finance.tools.game.PlayoffGameRevenueCalculator;

public class PlayoffGameFinanceProcessor extends GameFinanceProcessor {
   private PlayoffRound round;

   public PlayoffGameFinanceProcessor(PlayoffRound round) {
      this.round = round;
   }

   @Override
   protected GameRevenueCalculator createRevenueCalculator(GameStat gameStat) {
      return new PlayoffGameRevenueCalculator(gameStat, round);
   }

   @Override
   protected GameExpenseCalculator createExpenseCalculator(GameStat gameStat) {
      return new PlayoffGameExpenseCalculator(gameStat, round);
   }

   public PlayoffRound getRound() {
      return round;
   }

}

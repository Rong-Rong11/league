package process.service.finance.tools.game.processor;

import data.finance.GameStat;
import data.finance.budget.FinanceSeasonMoment;
import process.service.finance.tools.game.GameExpenseCalculator;
import process.service.finance.tools.game.GameRevenueCalculator;
import process.service.finance.tools.game.RegularSeasonGameExpenseCalculator;
import process.service.finance.tools.game.RegularSeasonGameRevenueCalculator;

public class RegularSeasonGameFinanceProcessor extends GameFinanceProcessor {
   @Override
   protected GameRevenueCalculator createRevenueCalculator(GameStat gameStat) {
      return new RegularSeasonGameRevenueCalculator(gameStat);
   }

   @Override
   protected GameExpenseCalculator createExpenseCalculator(GameStat gameStat) {
      return new RegularSeasonGameExpenseCalculator(gameStat);
   }

   @Override
   protected FinanceSeasonMoment getSeasonMoment() {
      return FinanceSeasonMoment.REGULAR_SEASON;
   }
}

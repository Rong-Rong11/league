package data.finance.budget.income;

import data.finance.budget.FinanceMoment;
import data.finance.budget.FinanceScope;

public enum IncomeType {
      // finance league début de saison
      NATIONAL_TV(FinanceScope.NATIONAL, FinanceMoment.SEASON, IncomeCategory.MEDIA),

      NATIONAL_SPONSORING(FinanceScope.NATIONAL, FinanceMoment.SEASON, IncomeCategory.SPONSORING),
      NATIONAL_MERCHANDISING(FinanceScope.NATIONAL, FinanceMoment.SEASON,
                  IncomeCategory.MERCHANDISING),

      // finance locales team
      LOCAL_SPONSORING(FinanceScope.LOCAL, FinanceMoment.MONTHLY, IncomeCategory.SPONSORING),
      LOCAL_MERCHANDISING(FinanceScope.LOCAL, FinanceMoment.MONTHLY,
                  IncomeCategory.MERCHANDISING),

      // finace match
      CONCESSIONS(FinanceScope.LOCAL, FinanceMoment.GAME, IncomeCategory.MATCHDAY),
      PARKING(FinanceScope.LOCAL, FinanceMoment.GAME, IncomeCategory.MATCHDAY),
      GAME_LOCAL_MERCHANDISING(FinanceScope.LOCAL, FinanceMoment.GAME,
                  IncomeCategory.MERCHANDISING),
      TICKET_OFFICE(FinanceScope.LOCAL, FinanceMoment.GAME, IncomeCategory.MATCHDAY),
      LOCAL_TV(FinanceScope.LOCAL, FinanceMoment.GAME, IncomeCategory.MEDIA),

      // finance mois
      REVENUE_SHARING(FinanceScope.SHARED, FinanceMoment.MONTHLY,
                  IncomeCategory.REDISTRIBUTION),
      EQUAL_SHARE(FinanceScope.SHARED, FinanceMoment.MONTHLY,
                  IncomeCategory.REDISTRIBUTION),
      LEAGUE_KEEPS(FinanceScope.LEAGUE, FinanceMoment.MONTHLY,
                  IncomeCategory.REDISTRIBUTION),
      OTHER(FinanceScope.LOCAL, FinanceMoment.MONTHLY, IncomeCategory.OTHER),
      CENTRAL_SHARE(FinanceScope.SHARED, FinanceMoment.MONTHLY,
                  IncomeCategory.REDISTRIBUTION),
      POOL_SHARE(FinanceScope.SHARED, FinanceMoment.MONTHLY,
                  IncomeCategory.REDISTRIBUTION);

      private final FinanceScope scope;
      private final FinanceMoment moment;
      private final IncomeCategory category;

      IncomeType(FinanceScope scope, FinanceMoment moment, IncomeCategory category) {
            this.scope = scope;
            this.moment = moment;
            this.category = category;
      }

      public FinanceScope getScope() {
            return this.scope;
      }

      public FinanceMoment getMoment() {
            return this.moment;
      }

      public IncomeCategory getCategory() {
            return this.category;
      }

      public boolean isLocal() {
            return this.getScope() == FinanceScope.LOCAL;
      }

      public boolean isNational() {
            return this.getScope() == FinanceScope.NATIONAL;
      }

      public boolean isGameIncome() {
            return this.getMoment() == FinanceMoment.GAME;
      }
}

package data.finance.budget.income;

import data.finance.budget.FinanceScope;

public enum IncomeType {
   NATIONAL_TV(new IncomeDefinition("national TV", FinanceScope.NATIONAL, IncomeMoment.SEASON,
         IncomeCategory.MEDIA)),
   LOCAL_TV(new IncomeDefinition("local TV", FinanceScope.LOCAL, IncomeMoment.GAME,
         IncomeCategory.MEDIA)),
   NATIONAL_SPONSORING(new IncomeDefinition("national sponsoring", FinanceScope.NATIONAL,
         IncomeMoment.SEASON, IncomeCategory.SPONSORING)),
   LOCAL_SPONSORING(new IncomeDefinition("local sponsoring", FinanceScope.LOCAL,
         IncomeMoment.MONTHLY, IncomeCategory.SPONSORING)),
   NATIONAL_MERCHANDISING(new IncomeDefinition("national merchandising",
         FinanceScope.NATIONAL, IncomeMoment.SEASON, IncomeCategory.MERCHANDISING)),
   LOCAL_MERCHANDISING(new IncomeDefinition("local merchandising", FinanceScope.LOCAL,
         IncomeMoment.MONTHLY, IncomeCategory.MERCHANDISING)),
   GAME_LOCAL_MERCHANDISING(new IncomeDefinition("local merchandising",
         FinanceScope.LOCAL, IncomeMoment.GAME, IncomeCategory.MERCHANDISING)),
   TICKET_OFFICE(new IncomeDefinition("ticket office", FinanceScope.LOCAL, IncomeMoment.GAME,
         IncomeCategory.MATCHDAY)),
   REVENUE_SHARING(new IncomeDefinition("income revenue sharing", FinanceScope.SHARED,
         IncomeMoment.REDISTRIBUTION, IncomeCategory.REDISTRIBUTION)),
   EQUAL_SHARE(new IncomeDefinition("equal share", FinanceScope.SHARED, IncomeMoment.REDISTRIBUTION,
         IncomeCategory.REDISTRIBUTION)),
   CONCESSIONS(new IncomeDefinition("concessions", FinanceScope.LOCAL, IncomeMoment.GAME,
         IncomeCategory.MATCHDAY)),
   PARKING(new IncomeDefinition("parking", FinanceScope.LOCAL, IncomeMoment.GAME,
         IncomeCategory.MATCHDAY)),
   LEAGUE_KEEPS(new IncomeDefinition("league keeps", FinanceScope.LEAGUE,
         IncomeMoment.REDISTRIBUTION, IncomeCategory.REDISTRIBUTION)),
   REVENUE_SHARING_WEIGHTED_SHARE(new IncomeDefinition("revenue sharing weighted share",
         FinanceScope.SHARED, IncomeMoment.REDISTRIBUTION,
         IncomeCategory.REDISTRIBUTION)),
   OTHER(new IncomeDefinition("others", FinanceScope.LOCAL, IncomeMoment.MONTHLY, IncomeCategory.OTHER)),
   CENTRAL_SHARE(new IncomeDefinition("central share", FinanceScope.SHARED,
         IncomeMoment.REDISTRIBUTION, IncomeCategory.REDISTRIBUTION)),
   POOL_SHARE(new IncomeDefinition("pool share", FinanceScope.SHARED, IncomeMoment.REDISTRIBUTION,
         IncomeCategory.REDISTRIBUTION));

   private final IncomeDefinition definition;

   IncomeType(IncomeDefinition definition) {
      this.definition = definition;
   }

   public IncomeDefinition getDefinition() {
      return this.definition;
   }

   public String getLabel() {
      return this.definition.getLabel();
   }

   public FinanceScope getScope() {
      return this.definition.getScope();
   }

   public IncomeMoment getMoment() {
      return this.definition.getMoment();
   }

   public IncomeCategory getCategory() {
      return this.definition.getCategory();
   }

   public boolean isLocal() {
      return this.getScope() == FinanceScope.LOCAL;
   }

   public boolean isNational() {
      return this.getScope() == FinanceScope.NATIONAL;
   }

   public boolean isGameIncome() {
      return this.getMoment() == IncomeMoment.GAME;
   }
}

package data.finance.budget.income;

import data.finance.budget.FinanceMoment;
import data.finance.budget.FinanceScope;
import data.finance.budget.FinanceSeasonMoment;

public enum IncomeType {
	  // finance league debut de saison
	  NATIONAL_TV(FinanceScope.NATIONAL, FinanceMoment.SEASON, FinanceSeasonMoment.BOTH, IncomeCategory.MEDIA),

	  NATIONAL_SPONSORING(FinanceScope.NATIONAL, FinanceMoment.SEASON, FinanceSeasonMoment.BOTH,
				  IncomeCategory.SPONSORING),
	  NATIONAL_MERCHANDISING(FinanceScope.NATIONAL, FinanceMoment.SEASON, FinanceSeasonMoment.BOTH,
				  IncomeCategory.MERCHANDISING),

	  // finance locales team
	  LOCAL_SPONSORING(FinanceScope.LOCAL, FinanceMoment.MONTHLY, FinanceSeasonMoment.BOTH,
				  IncomeCategory.SPONSORING),
	  LOCAL_MERCHANDISING(FinanceScope.LOCAL, FinanceMoment.MONTHLY, FinanceSeasonMoment.BOTH,
				  IncomeCategory.MERCHANDISING),

	  // finance match
	  CONCESSIONS(FinanceScope.LOCAL, FinanceMoment.GAME, FinanceSeasonMoment.BOTH, IncomeCategory.MATCHDAY),
	  PARKING(FinanceScope.LOCAL, FinanceMoment.GAME, FinanceSeasonMoment.BOTH, IncomeCategory.MATCHDAY),
	  GAME_LOCAL_MERCHANDISING(FinanceScope.LOCAL, FinanceMoment.GAME, FinanceSeasonMoment.BOTH,
				  IncomeCategory.MERCHANDISING),
	  TICKET_OFFICE(FinanceScope.LOCAL, FinanceMoment.GAME, FinanceSeasonMoment.BOTH, IncomeCategory.MATCHDAY),
	  LOCAL_TV(FinanceScope.LOCAL, FinanceMoment.GAME, FinanceSeasonMoment.BOTH, IncomeCategory.MEDIA),
	  PLAYOFF_CONCESSIONS(FinanceScope.LOCAL, FinanceMoment.GAME, FinanceSeasonMoment.PLAYOFF,
				  IncomeCategory.MATCHDAY),
	  PLAYOFF_PARKING(FinanceScope.LOCAL, FinanceMoment.GAME, FinanceSeasonMoment.PLAYOFF, IncomeCategory.MATCHDAY),
	  PLAYOFF_LOCAL_MERCHANDISING(FinanceScope.LOCAL, FinanceMoment.GAME, FinanceSeasonMoment.PLAYOFF,
				  IncomeCategory.MERCHANDISING),
	  PLAYOFF_TICKET_OFFICE(FinanceScope.LOCAL, FinanceMoment.GAME, FinanceSeasonMoment.PLAYOFF,
				  IncomeCategory.MATCHDAY),
	  PLAYOFF_LOCAL_TV(FinanceScope.LOCAL, FinanceMoment.GAME, FinanceSeasonMoment.PLAYOFF, IncomeCategory.MEDIA),
	  PLAYOFF_QUALIFICATION_BONUS(FinanceScope.LOCAL, FinanceMoment.SEASON, FinanceSeasonMoment.PLAYOFF,
				  IncomeCategory.OTHER),
	  PLAYOFF_ROUND_BONUS(FinanceScope.LOCAL, FinanceMoment.SEASON, FinanceSeasonMoment.PLAYOFF,
				  IncomeCategory.OTHER),

	  // finance mois
	  REVENUE_SHARING(FinanceScope.SHARED, FinanceMoment.MONTHLY, FinanceSeasonMoment.REGULAR_SEASON,
				  IncomeCategory.REDISTRIBUTION),
	  EQUAL_SHARE(FinanceScope.SHARED, FinanceMoment.MONTHLY, FinanceSeasonMoment.BOTH,
				  IncomeCategory.REDISTRIBUTION),
	  LEAGUE_KEEPS(FinanceScope.LEAGUE, FinanceMoment.MONTHLY, FinanceSeasonMoment.BOTH,
				  IncomeCategory.REDISTRIBUTION),
	  OTHER(FinanceScope.LOCAL, FinanceMoment.MONTHLY, FinanceSeasonMoment.BOTH, IncomeCategory.OTHER),
	  CENTRAL_SHARE(FinanceScope.SHARED, FinanceMoment.MONTHLY, FinanceSeasonMoment.BOTH,
				  IncomeCategory.REDISTRIBUTION),
	  POOL_SHARE(FinanceScope.SHARED, FinanceMoment.MONTHLY, FinanceSeasonMoment.BOTH,
				  IncomeCategory.REDISTRIBUTION);

	  private final FinanceScope scope;
	  private final FinanceMoment moment;
	  private final FinanceSeasonMoment seasonMoment;
	  private final IncomeCategory category;

	  IncomeType(FinanceScope scope, FinanceMoment moment, FinanceSeasonMoment seasonMoment, IncomeCategory category) {
			this.scope = scope;
			this.moment = moment;
			this.seasonMoment = seasonMoment;
			this.category = category;
	  }

	  public FinanceScope getScope() {
			return this.scope;
	  }

	  public FinanceMoment getMoment() {
			return this.moment;
	  }

	  public FinanceSeasonMoment getSeasonMoment() {
			return this.seasonMoment;
	  }

	  public IncomeCategory getCategory() {
			return this.category;
	  }
}

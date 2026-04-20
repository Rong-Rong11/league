package data.finance.budget.expense;

import data.finance.budget.FinanceScope;
import data.finance.budget.FinanceSeasonMoment;

public enum ExpenseType {
		REVENUE_SHARING_CONTRIBUTION(
						FinanceScope.SHARED, FinanceSeasonMoment.REGULAR_SEASON, ExpenseCategory.REDISTRIBUTION),
		PLAYER_SALARY(FinanceScope.LOCAL, FinanceSeasonMoment.BOTH, ExpenseCategory.PAYROLL),
		STADIUM_COST(FinanceScope.LOCAL, FinanceSeasonMoment.BOTH, ExpenseCategory.FACILITY),
		MAINTENANCE_STADIUM_COST(FinanceScope.LOCAL, FinanceSeasonMoment.BOTH,
						ExpenseCategory.FACILITY),
		STAFF_COST(FinanceScope.LOCAL, FinanceSeasonMoment.BOTH, ExpenseCategory.OPERATIONS),
		ADMINISTRATIVE_COST(FinanceScope.LEAGUE, FinanceSeasonMoment.BOTH,
						ExpenseCategory.LEAGUE_OPERATIONS),
		MEDIA_COST(FinanceScope.LEAGUE, FinanceSeasonMoment.BOTH, ExpenseCategory.LEAGUE_OPERATIONS),
		MARKETING_COST(FinanceScope.LEAGUE, FinanceSeasonMoment.BOTH,
						ExpenseCategory.LEAGUE_OPERATIONS),
		OFFICIATING_COST(FinanceScope.LEAGUE, FinanceSeasonMoment.BOTH,
						ExpenseCategory.LEAGUE_OPERATIONS),
		SECURITY_COST(FinanceScope.LOCAL, FinanceSeasonMoment.BOTH, ExpenseCategory.OPERATIONS),
		LOGISTIC_COST(FinanceScope.LOCAL, FinanceSeasonMoment.BOTH, ExpenseCategory.OPERATIONS),
		TRAVEL_COST(FinanceScope.LOCAL, FinanceSeasonMoment.BOTH, ExpenseCategory.OPERATIONS),
		PLAYOFF_STADIUM_COST(FinanceScope.LOCAL, FinanceSeasonMoment.PLAYOFF, ExpenseCategory.FACILITY),
		PLAYOFF_STAFF_COST(FinanceScope.LOCAL, FinanceSeasonMoment.PLAYOFF, ExpenseCategory.OPERATIONS),
		PLAYOFF_SECURITY_COST(FinanceScope.LOCAL, FinanceSeasonMoment.PLAYOFF, ExpenseCategory.OPERATIONS),
		PLAYOFF_LOGISTIC_COST(FinanceScope.LOCAL, FinanceSeasonMoment.PLAYOFF, ExpenseCategory.OPERATIONS),
		PLAYOFF_TRAVEL_COST(FinanceScope.LOCAL, FinanceSeasonMoment.PLAYOFF, ExpenseCategory.OPERATIONS),
		LUXURY_TAX_PAID(FinanceScope.SHARED, FinanceSeasonMoment.REGULAR_SEASON, ExpenseCategory.TAX);

		private final FinanceScope scope;
		private final FinanceSeasonMoment seasonMoment;
		private final ExpenseCategory category;

		ExpenseType(FinanceScope scope, FinanceSeasonMoment seasonMoment, ExpenseCategory category) {
				this.scope = scope;
				this.seasonMoment = seasonMoment;
				this.category = category;
		}

		public FinanceScope getScope() {
				return this.scope;
		}

		public FinanceSeasonMoment getSeasonMoment() {
				return this.seasonMoment;
		}

		public ExpenseCategory getCategory() {
				return this.category;
		}
}

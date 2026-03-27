package data.finance.budget.expense;

import data.finance.budget.FinanceScope;

public enum ExpenseType {
        REVENUE_SHARING_CONTRIBUTION(
                        FinanceScope.SHARED, ExpenseCategory.REDISTRIBUTION),
        PLAYER_SALARY(FinanceScope.LOCAL, ExpenseCategory.PAYROLL),
        STADIUM_COST(FinanceScope.LOCAL, ExpenseCategory.FACILITY),
        MAINTENANCE_STADIUM_COST(FinanceScope.LOCAL,
                        ExpenseCategory.FACILITY),
        STAFF_COST(FinanceScope.LOCAL, ExpenseCategory.OPERATIONS),
        ADMINISTRATIVE_COST(FinanceScope.LEAGUE,
                        ExpenseCategory.LEAGUE_OPERATIONS),
        MEDIA_COST(FinanceScope.LEAGUE, ExpenseCategory.LEAGUE_OPERATIONS),
        MARKETING_COST(FinanceScope.LEAGUE,
                        ExpenseCategory.LEAGUE_OPERATIONS),
        OFFICIATING_COST(FinanceScope.LEAGUE,
                        ExpenseCategory.LEAGUE_OPERATIONS),
        SECURITY_COST(FinanceScope.LOCAL, ExpenseCategory.OPERATIONS),
        LOGISTIC_COST(FinanceScope.LOCAL, ExpenseCategory.OPERATIONS),
        TRAVEL_COST(FinanceScope.LOCAL, ExpenseCategory.OPERATIONS),
        LUXURY_TAX_PAID(FinanceScope.SHARED, ExpenseCategory.TAX);

        private final FinanceScope scope;
        private final ExpenseCategory category;

        ExpenseType(FinanceScope scope, ExpenseCategory category) {
                this.scope = scope;
                this.category = category;
        }

        public FinanceScope getScope() {
                return this.scope;
        }

        public ExpenseCategory getCategory() {
                return this.category;
        }

        public boolean isLocal() {
                return this.getScope() == FinanceScope.LOCAL;
        }

        public boolean isNational() {
                return this.getScope() == FinanceScope.NATIONAL;
        }
}

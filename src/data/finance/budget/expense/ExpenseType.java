package data.finance.budget.expense;

import data.finance.budget.FinanceScope;

public enum ExpenseType {
        REVENUE_SHARING_CONTRIBUTION(new ExpenseDefinition(
                        "expense revenue sharing contribution", FinanceScope.SHARED, ExpenseCategory.REDISTRIBUTION)),
        PLAYER_SALARY(new ExpenseDefinition("expense player salary", FinanceScope.LOCAL,
                        ExpenseCategory.PAYROLL)),
        STADIUM_COST(new ExpenseDefinition("expense stadium cost", FinanceScope.LOCAL,
                        ExpenseCategory.FACILITY)),
        MAINTENANCE_STADIUM_COST(new ExpenseDefinition("expense maintenance cost",
                        FinanceScope.LOCAL, ExpenseCategory.FACILITY)),
        STAFF_COST(new ExpenseDefinition("expense stAff cost", FinanceScope.LOCAL,
                        ExpenseCategory.OPERATIONS)),
        ADMINISTRATIVE_COST(new ExpenseDefinition("expense administrative cost",
                        FinanceScope.LEAGUE, ExpenseCategory.LEAGUE_OPERATIONS)),
        MEDIA_COST(new ExpenseDefinition("media cost", FinanceScope.LEAGUE,
                        ExpenseCategory.LEAGUE_OPERATIONS)),
        MARKETING_COST(new ExpenseDefinition("marketing cost", FinanceScope.LEAGUE,
                        ExpenseCategory.LEAGUE_OPERATIONS)),
        OFFICIATING_COST(new ExpenseDefinition("officiating cost", FinanceScope.LEAGUE,
                        ExpenseCategory.LEAGUE_OPERATIONS)),
        SECURITY_COST(new ExpenseDefinition("expense security cost", FinanceScope.LOCAL,
                        ExpenseCategory.OPERATIONS)),
        LOGISTIC_COST(new ExpenseDefinition("expense logistic cost", FinanceScope.LOCAL,
                        ExpenseCategory.OPERATIONS)),
        TRAVEL_COST(new ExpenseDefinition("expense travel cost", FinanceScope.LOCAL,
                        ExpenseCategory.OPERATIONS)),
        LUXURY_TAX_PAID(new ExpenseDefinition("luxurytaxpaid", FinanceScope.SHARED,
                        ExpenseCategory.TAX));

        private final ExpenseDefinition definition;

        ExpenseType(ExpenseDefinition definition) {
                this.definition = definition;
        }

        public ExpenseDefinition getDefinition() {
                return this.definition;
        }

        public String getLabel() {
                return this.definition.getLabel();
        }

        public FinanceScope getScope() {
                return this.definition.getScope();
        }

        public ExpenseCategory getCategory() {
                return this.definition.getCategory();
        }

        public boolean isLocal() {
                return this.getScope() == FinanceScope.LOCAL;
        }

        public boolean isNational() {
                return this.getScope() == FinanceScope.NATIONAL;
        }
}

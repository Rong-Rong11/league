package data.finance.budget.income;

import data.finance.budget.FinanceScope;

public final class IncomeDefinition {
    private final String label;
    private final FinanceScope scope;
    private final IncomeMoment moment;
    private final IncomeCategory category;

    public IncomeDefinition(
            String label,
            FinanceScope scope,
            IncomeMoment moment,
            IncomeCategory category) {
        this.label = label;
        this.scope = scope;
        this.moment = moment;
        this.category = category;
    }

    public String getLabel() {
        return this.label;
    }

    public FinanceScope getScope() {
        return this.scope;
    }

    public IncomeMoment getMoment() {
        return this.moment;
    }

    public IncomeCategory getCategory() {
        return this.category;
    }
}

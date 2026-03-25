package data.finance.budget.income;

public class Income {

    private IncomeType incomeType;
    private double amount;

    public Income(IncomeType incomeType, double amount) {
        this.incomeType = incomeType;
        this.amount = amount;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public IncomeType getIncomeType() {
        return this.incomeType;
    }

    public void setIncomeType(IncomeType incomeType) {
        this.incomeType = incomeType;
    }

    public String getName() {
        return this.incomeType.name();
    }

    public String getLabel() {
        return this.incomeType.getLabel();
    }
}

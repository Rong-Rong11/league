package data.finance.budget.expense;

public class Expense {
    private ExpenseType expenseType;
    private double amount;

    public Expense(ExpenseType expenseType, double amount) {
        this.expenseType = expenseType;
        this.amount = amount;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ExpenseType getExpenseType() {
        return this.expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public String getName() {
        return this.expenseType.name();
    }
}

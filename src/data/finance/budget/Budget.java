package data.finance.budget;

import data.finance.budget.expense.Expense;
import data.finance.budget.income.Income;
import java.util.HashMap;

public class Budget {
	private double initialAmount;
	private double remainingAmount;
	private HashMap<Integer, HashMap<String, Income>> monthlyIncomes = new HashMap<>();
	private HashMap<Integer, HashMap<String, Expense>> monthlyExpenses = new HashMap<>();
	private HashMap<Integer, Double> monthlyNetHistory = new HashMap<>();

	public Budget(double initialAmount) {
		this.initialAmount = initialAmount;
		this.remainingAmount = initialAmount;
	}

	public double getInitialAmount() {
		return this.initialAmount;
	}

	public void setInitialAmount(double initialAmount) {
		this.initialAmount = initialAmount;
	}

	public double getRemainingAmount() {
		return this.remainingAmount;
	}

	public void setRemainingAmount(double remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public HashMap<String, Income> getIncomesForMonth(int month) {
		return this.monthlyIncomes.get(month);
	}

	public HashMap<String, Expense> getExpensesForMonth(int month) {
		return this.monthlyExpenses.get(month);
	}

	public HashMap<Integer, HashMap<String, Income>> getMonthlyIncomes() {
		return this.monthlyIncomes;
	}

	public HashMap<Integer, HashMap<String, Expense>> getMonthlyExpenses() {
		return this.monthlyExpenses;
	}

	public HashMap<Integer, Double> getMonthlyNetHistory() {
		return this.monthlyNetHistory;
	}

	public double getNetForMonth(int month) {
		return this.monthlyNetHistory.getOrDefault(month, 0.0);
	}
}

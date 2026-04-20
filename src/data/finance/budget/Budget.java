/*
	* Decompiled with CFR 0.152.
	*/
package data.finance.budget;

import data.finance.budget.expense.Expense;
import data.finance.budget.income.Income;
import java.util.HashMap;

public class Budget {
	private double initialAmount;
	private double remainingAmount;
	private HashMap<Integer, HashMap<String, Income>> monthlyIncomes = new HashMap<>();
	private HashMap<Integer, HashMap<String, Expense>> monthlyExpenses = new HashMap<>();

	public Budget(double d) {
		this.initialAmount = d;
		this.remainingAmount = d;
	}

	public double getInitialAmount() {
		return this.initialAmount;
	}

	public void setInitialAmount(double d) {
		this.initialAmount = d;
	}

	public double getRemainingAmount() {
		return this.remainingAmount;
	}

	public void setRemainingAmount(double d) {
		this.remainingAmount = d;
	}

	public HashMap<String, Income> getIncomesForMonth(int n) {
		return this.monthlyIncomes.get(n);
	}

	public HashMap<String, Expense> getExpensesForMonth(int n) {
		return this.monthlyExpenses.get(n);
	}

	public HashMap<Integer, HashMap<String, Income>> getMonthlyIncomes() {
		return this.monthlyIncomes;
	}

	public HashMap<Integer, HashMap<String, Expense>> getMonthlyExpenses() {
		return this.monthlyExpenses;
	}
}

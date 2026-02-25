package data.finance.budget;

import java.util.HashMap;

public class Budget {
	private double initialAmount ; 
	private double remainingAmount ; 
	private HashMap<String, Income> incomes = new HashMap<String, Income>() ; 
	private HashMap<String, Expense> expenses = new HashMap<String, Expense>() ; 
	
	public Budget(double amount) {
		this.initialAmount = amount ; 
		this.remainingAmount = amount ; 
	}
	
	public void addIncome(Income income) {
		incomes.put(income.getName(), income) ; 
	}
	
	public void addExpense(Expense expense) {
		expenses.put(expense.getName(), expense) ; 
	}

	public double getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(double initialAmount) {
		this.initialAmount = initialAmount;
	}
	

	public double getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(double remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public HashMap<String, Income> getIncomes() {
		return incomes;
	}

	public HashMap<String, Expense> getExpenses() {
		return expenses;
	}

	
	
	
}

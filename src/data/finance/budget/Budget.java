package data.finance.budget;

import java.util.HashMap;

public class Budget {
	private double initialAmount ; 
	private double remainingAmount ; 
	private HashMap<Integer, HashMap<String, Income>> monthlyIncomes = new HashMap<Integer, HashMap<String,Income>>(); 
	private HashMap<Integer, HashMap<String, Expense>> monthlyExpenses = new HashMap<Integer, HashMap<String,Expense>>() ; 
	
	public Budget(double amount) {
		this.initialAmount = amount ; 
		this.remainingAmount = amount ; 
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

	public HashMap<String, Income> getIncomesForMonth(int month) {
		return monthlyIncomes.get(month);
	}

	public HashMap<String, Expense> getExpenses(int month) {
		return monthlyExpenses.get(month);
	}

	public HashMap<Integer, HashMap<String, Income>> getMonthlyIncomes() {
		return monthlyIncomes;
	}


	public HashMap<Integer, HashMap<String, Expense>> getMonthlyExpenses() {
		return monthlyExpenses;
	}


	
	
	

	
	
	
}

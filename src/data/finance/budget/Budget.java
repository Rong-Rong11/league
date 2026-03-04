package data.finance.budget;

import java.util.HashMap;

public class Budget {
	private double initialAmount ; 
	private double remainingAmount ; 
<<<<<<< HEAD
	private HashMap<String, Income> incomes = new HashMap<String, Income>() ; 
	private HashMap<String, Expense> expenses = new HashMap<String, Expense>() ; 
=======
	private HashMap<Integer, HashMap<String, Income>> monthlyIncomes = new HashMap<Integer, HashMap<String,Income>>(); 
	private HashMap<Integer, HashMap<String, Expense>> monthlyExpenses = new HashMap<Integer, HashMap<String,Expense>>() ; 
>>>>>>> Fatima2
	
	public Budget(double amount) {
		this.initialAmount = amount ; 
		this.remainingAmount = amount ; 
	}
<<<<<<< HEAD
	
	public void addIncome(Income income) {
		incomes.put(income.getName(), income) ; 
	}
	
	public void addExpense(Expense expense) {
		expenses.put(expense.getName(), expense) ; 
	}
=======
>>>>>>> Fatima2

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

<<<<<<< HEAD
	public HashMap<String, Income> getIncomes() {
		return incomes;
	}

	public HashMap<String, Expense> getExpenses() {
		return expenses;
	}

=======
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


	
	
	

>>>>>>> Fatima2
	
	
	
}

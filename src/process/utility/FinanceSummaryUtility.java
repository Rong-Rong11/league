package process.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.finance.budget.Budget;
import data.finance.budget.FinanceScope;
import data.finance.budget.expense.Expense;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeCategory;

public final class FinanceSummaryUtility {

	private FinanceSummaryUtility() {
	}

	public static List<Integer> availableMonths(Budget budget, int lastVisibleMonth) {
		ArrayList<Integer> months = new ArrayList<Integer>();
		if (budget == null) {
			months.add(1);
			return months;
		}

		for (int month = 1; month <= lastVisibleMonth; month++) {
			Map<String, Income> incomes = budget.getIncomesForMonth(month);
			Map<String, Expense> expenses = budget.getExpensesForMonth(month);
			if ((incomes != null && !incomes.isEmpty()) || (expenses != null && !expenses.isEmpty())) {
				months.add(month);
			}
		}

		if (months.isEmpty()) {
			months.add(1);
		}
		return months;
	}

	public static double totalIncome(Map<String, Income> incomes) {
		double total = 0.0;
		if (incomes != null) {
			for (Income income : incomes.values()) {
				total += income.getAmount();
			}
		}
		return total;
	}

	public static double totalLocalIncome(Map<String, Income> incomes) {
		double total = 0.0;
		if (incomes != null) {
			for (Income income : incomes.values()) {
				if (income.getIncomeType() != null && income.getIncomeType().getScope() == FinanceScope.LOCAL) {
					total += income.getAmount();
				}
			}
		}
		return total;
	}

	public static double totalExpense(Map<String, Expense> expenses) {
		double total = 0.0;
		if (expenses != null) {
			for (Expense expense : expenses.values()) {
				total += expense.getAmount();
			}
		}
		return total;
	}

	public static double getIncomeForMonth(Budget budget, int month) {
		if (budget == null) {
			return 0.0;
		}
		return totalIncome(budget.getIncomesForMonth(month));
	}

	public static double getExpenseForMonth(Budget budget, int month) {
		if (budget == null) {
			return 0.0;
		}
		return totalExpense(budget.getExpensesForMonth(month));
	}

	public static double getTotalIncome(Budget budget, int lastMonth) {
		double total = 0.0;
		for (int month = 1; month <= lastMonth; month++) {
			total += getIncomeForMonth(budget, month);
		}
		return total;
	}

	public static double getTotalExpense(Budget budget, int lastMonth) {
		double total = 0.0;
		for (int month = 1; month <= lastMonth; month++) {
			total += getExpenseForMonth(budget, month);
		}
		return total;
	}

	public static double getTotalNet(Budget budget, int lastMonth) {
		double total = 0.0;
		if (budget == null) {
			return total;
		}
		for (int month = 1; month <= lastMonth; month++) {
			total += budget.getNetForMonth(month);
		}
		return total;
	}

	public static double getRevenueByCategory(Budget budget, IncomeCategory category) {
		double total = 0.0;
		if (budget == null || category == null) {
			return total;
		}
		for (Map<String, Income> incomes : budget.getMonthlyIncomes().values()) {
			if (incomes == null) {
				continue;
			}
			for (Income income : incomes.values()) {
				if (income != null && income.getIncomeType() != null
						&& income.getIncomeType().getCategory() == category) {
					total += income.getAmount();
				}
			}
		}
		return total;
	}
}

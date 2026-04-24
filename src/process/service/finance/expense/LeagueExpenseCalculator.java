package process.service.finance.expense;

import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.finance.budget.expense.ExpenseType;
import data.league.League;
import process.service.finance.FinanceManager;
import process.utility.FinanceUtility;

public class LeagueExpenseCalculator {

	private League league;
	private LeagueExpenseGameAnalyzer gameAnalyzer;
	private LeagueExpenseCostCalculator costCalculator;

	public LeagueExpenseCalculator(League league) {
		this.league = league;
		this.gameAnalyzer = new LeagueExpenseGameAnalyzer(league);
		LeaguePopularityExpenseTracker popularityTracker = new LeaguePopularityExpenseTracker();
		LeagueExpenseRateCalculator rateCalculator = new LeagueExpenseRateCalculator(gameAnalyzer, popularityTracker);
		this.costCalculator = new LeagueExpenseCostCalculator(rateCalculator);
	}

	public void setFinanceManager(FinanceManager financeManager) {
		gameAnalyzer.setFinanceManager(financeManager);
	}

	public void applyMonthlyExpenses(int month) {
		Budget budget = league.getLeagueFinance().getBudget();

		double administrativeCost = costCalculator.calculateAdministrativeCost();
		double mediaCost = costCalculator.calculateMediaCost(month);
		double marketingCost = costCalculator.calculateMarketingCost(month);
		double officiatingCost = costCalculator.calculateOfficiatingCost(month);

		FinanceUtility.addExpense(
				budget,
				new Expense(ExpenseType.ADMINISTRATIVE_COST, administrativeCost),
				month);

		FinanceUtility.addExpense(
				budget,
				new Expense(ExpenseType.MEDIA_COST, mediaCost),
				month);

		FinanceUtility.addExpense(
				budget,
				new Expense(ExpenseType.MARKETING_COST, marketingCost),
				month);

		FinanceUtility.addExpense(
				budget,
				new Expense(ExpenseType.OFFICIATING_COST, officiatingCost),
				month);

		FinanceUtility.updateBudget(budget);
	}

}

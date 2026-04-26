package process.service.finance.expense;

import org.apache.log4j.Logger;

import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.finance.budget.expense.ExpenseType;
import data.league.League;
import data.league.finance.CentralRevenueSeasonDynamics;
import log.LoggerUtility;
import process.service.finance.FinanceManager;
import process.utility.FinanceUtility;

public class LeagueExpenseCalculator {
	private static final Logger logger = LoggerUtility.getLogger(LeagueExpenseCalculator.class, "text");

	private League league;
	private LeagueExpenseGameAnalyzer gameAnalyzer;
	private LeagueExpenseCostCalculator costCalculator;

	public LeagueExpenseCalculator(League league) {
		this.league = league;
		this.gameAnalyzer = new LeagueExpenseGameAnalyzer(league);
		LeaguePopularityExpenseTracker popularityTracker = new LeaguePopularityExpenseTracker();
		LeagueExpenseRateCalculator rateCalculator = new LeagueExpenseRateCalculator(gameAnalyzer,
				popularityTracker,
				getSeasonDynamics());
		this.costCalculator = new LeagueExpenseCostCalculator(rateCalculator, league.getAllTeam());
	}

	public void setFinanceManager(FinanceManager financeManager) {
		logger.debug("Setting finance manager for league expense calculator");
		gameAnalyzer.setFinanceManager(financeManager);
	}

	public void applyMonthlyExpenses(int month) {
		if (league == null || league.getLeagueFinance() == null) {
			logger.warn("Skipping monthly league expenses because league or league finance is null");
			return;
		}
		logger.info("Applying monthly league expenses for month " + month);
		Budget budget = league.getLeagueFinance().getBudget();

		double administrativeCost = costCalculator.calculateAdministrativeCost();
		double mediaCost = costCalculator.calculateMediaCost(month);
		double marketingCost = costCalculator.calculateMarketingCost(month);
		double officiatingCost = costCalculator.calculateOfficiatingCost(month);
		logger.debug("Calculated monthly league expenses: administrative="
				+ administrativeCost
				+ ", media="
				+ mediaCost
				+ ", marketing="
				+ marketingCost
				+ ", officiating="
				+ officiatingCost);

		logger.trace("Adding administrative league expense for month " + month);
		FinanceUtility.addExpense(
				budget,
				new Expense(ExpenseType.ADMINISTRATIVE_COST, administrativeCost),
				month);

		logger.trace("Adding media league expense for month " + month);
		FinanceUtility.addExpense(
				budget,
				new Expense(ExpenseType.MEDIA_COST, mediaCost),
				month);

		logger.trace("Adding marketing league expense for month " + month);
		FinanceUtility.addExpense(
				budget,
				new Expense(ExpenseType.MARKETING_COST, marketingCost),
				month);

		logger.trace("Adding officiating league expense for month " + month);
		FinanceUtility.addExpense(
				budget,
				new Expense(ExpenseType.OFFICIATING_COST, officiatingCost),
				month);

		FinanceUtility.updateBudget(budget);
		logger.info("Monthly league expenses applied for month " + month);
	}

	private CentralRevenueSeasonDynamics getSeasonDynamics() {
		if (league == null || league.getLeagueFinance() == null || league.getLeagueFinance().getCentralRevenueSeasonDynamics() == null) {
			return new CentralRevenueSeasonDynamics(1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.08, 50.0, 1.0, 0.0);
		}
		return league.getLeagueFinance().getCentralRevenueSeasonDynamics();
	}

}

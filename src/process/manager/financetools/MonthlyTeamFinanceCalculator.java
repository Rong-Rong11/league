package process.manager.financetools;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.Expense;
import data.finance.budget.Income;
import data.team.Team;
import data.team.finance.marketsize.MarketSize;
import process.utilitary.FinanceUtilitary;

public class MonthlyTeamFinanceCalculator {
	public void applyMonthlyFinance(Team team, int month) {
		Budget budget = team.getTeamFinance().getBudget();
		double marketMultiplier = getMarketMultiplier(team.getTeamFinance().getMarketSize());
		double popularityFactor = team.getPopularity() / 100.0;
		double starFactor = team.getStarPlayer() != null ? 1.10 : 1.0;
		double performanceFactor = 0.90 + (team.getTeamPerformance().getPerformanceRating() * 0.20);

		double localSponsoring = 1.20 * marketMultiplier * popularityFactor * starFactor;
		double localMerchandising = 0.75 * marketMultiplier * popularityFactor * starFactor;
		double otherRevenue = 0.25 * marketMultiplier * performanceFactor;

		double monthlyPayroll = team.getTeamFinance().getPayroll() / FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS;
		double stadiumMaintenance = calculateStadiumMaintenance(team, marketMultiplier);
		double staffCost = calculateStaffCost(team, marketMultiplier);
		double administrativeCost = 0.18 * marketMultiplier;

		FinanceUtilitary.addIncome(budget, new Income(FinanceConfiguration.INCOME_TYPE_LOCAL_SPONSORING, localSponsoring), month);
		FinanceUtilitary.addIncome(budget, new Income(FinanceConfiguration.INCOME_TYPE_LOCAL_MERCHANDISING, localMerchandising), month);
		FinanceUtilitary.addIncome(budget, new Income(FinanceConfiguration.INCOME_TYPE_OTHER, otherRevenue), month);

		FinanceUtilitary.addExpense(budget, new Expense(FinanceConfiguration.EXPENSE_TYPE_PLAYER_SALARY, monthlyPayroll), month);
		FinanceUtilitary.addExpense(budget, new Expense(FinanceConfiguration.EXPENSE_TYPE_STADIUM_COST, stadiumMaintenance), month);
		FinanceUtilitary.addExpense(budget, new Expense(FinanceConfiguration.EXPENSE_TYPE_STAFF_COST, staffCost), month);
		FinanceUtilitary.addExpense(budget, new Expense(FinanceConfiguration.EXPENSE_TYPE_ADMINISTRATIVE_COST, administrativeCost), month);

		FinanceUtilitary.updateBudget(budget);
	}

	private double calculateStadiumMaintenance(Team team, double marketMultiplier) {
		double capacityFactor = team.getStadium().getCapacity() / 20000.0;
		return 0.22 * marketMultiplier * capacityFactor;
	}

	private double calculateStaffCost(Team team, double marketMultiplier) {
		int numberOfPlayers = team.getPlayers().size();
		double popularityFactor = 0.80 + (team.getPopularity() / 500.0);
		return ((0.015 * numberOfPlayers) + 0.10) * marketMultiplier * popularityFactor;
	}

	private double getMarketMultiplier(MarketSize marketSize) {
		String size = marketSize.getSize();

		if (size.equals(FinanceConfiguration.MARKET_SIZE_SMALL)) {
			return FinanceConfiguration.MARKET_SIZE_SMALL_MULTIPLIER;
		}
		if (size.equals(FinanceConfiguration.MARKET_SIZE_LARGE)) {
			return FinanceConfiguration.MARKET_SIZE_LARGE_MULTIPLIER;
		}
		return FinanceConfiguration.MARKET_SIZE_MEDIUM_MULTIPLIER;
	}
}

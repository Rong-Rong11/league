package process.service.finance.distribution.league;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import process.utility.FinanceUtility;

public class LeagueRevenueRetainer {

	public double retainLeagueCut(Budget leagueBudget, double revenue, IncomeType incomeType, int month) {
		double leagueCut = calculateLeagueCut(revenue);
		FinanceUtility.addIncome(leagueBudget, new Income(incomeType, leagueCut), month);
		return revenue - leagueCut;
	}

	public double calculateLeagueCut(double revenue) {
		return revenue * FinanceConfiguration.LEAGUE_OPERATING_RATE;
	}
}

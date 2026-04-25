package process.service.finance.distribution.league;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import log.LoggerUtility;
import process.utility.FinanceUtility;

public class LeagueRevenueRetainer {
	private static final Logger logger = LoggerUtility.getLogger(LeagueRevenueRetainer.class, "text");

	public double retainLeagueCut(Budget leagueBudget, double revenue, IncomeType incomeType, int month) {
		if (leagueBudget == null) {
			logger.warn("Skipping league revenue cut retention because league budget is null");
			return revenue;
		}
		if (incomeType == null) {
			logger.warn("Skipping league revenue cut retention because income type is null");
			return revenue;
		}
		logger.debug("Retaining league cut for " + incomeType + " revenue " + revenue + " in month " + month);
		double leagueCut = calculateLeagueCut(revenue);
		FinanceUtility.addIncome(leagueBudget, new Income(incomeType, leagueCut), month);
		double distributableRevenue = revenue - leagueCut;
		logger.debug("League retained "
				+ leagueCut
				+ " for "
				+ incomeType
				+ ", distributable revenue is "
				+ distributableRevenue);
		return distributableRevenue;
	}

	public double calculateLeagueCut(double revenue) {
		double leagueCut = revenue * FinanceConfiguration.LEAGUE_OPERATING_RATE;
		logger.trace("Calculated league cut "
				+ leagueCut
				+ " from revenue "
				+ revenue
				+ " with operating rate "
				+ FinanceConfiguration.LEAGUE_OPERATING_RATE);
		return leagueCut;
	}
}

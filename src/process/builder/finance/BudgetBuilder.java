package process.builder.finance;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.team.finance.economicprofile.EconomicProfile;
import data.team.finance.marketsize.MarketSize;
import log.LoggerUtility;
import process.visitor.marketsize.CalculateInitialTeamBudgetVisitor;

public class BudgetBuilder {
	private static final Logger logger = LoggerUtility.getLogger(BudgetBuilder.class, "text");

	public static void calculateInitialBudget(Budget budget, MarketSize marketSize, EconomicProfile economicProfile,
			double popularity) {
		if (budget == null) {
			logger.warn("Skipping initial budget calculation because budget is null");
			return;
		}
		if (marketSize == null) {
			logger.warn("Skipping initial budget calculation because market size is null");
			return;
		}
		if (economicProfile == null) {
			logger.warn("Skipping initial budget calculation because economic profile is null");
			return;
		}

		logger.info("Calculating initial team budget");
		logger.debug("Initial budget calculation with popularity "
				+ popularity
				+ ", market size "
				+ marketSize.getClass().getSimpleName()
				+ " and profile "
				+ economicProfile.getClass().getSimpleName());
		calculateBaseBudget(budget, popularity);
		CalculateInitialTeamBudgetVisitor calculateInitialTeamBudgetVisitor = new CalculateInitialTeamBudgetVisitor(
				budget.getInitialAmount(), popularity, economicProfile);
		double initialAmount = marketSize.accept(calculateInitialTeamBudgetVisitor);
		budget.setInitialAmount(initialAmount);
		budget.setRemainingAmount(initialAmount);
		logger.debug("Initial team budget calculated at " + initialAmount);
		logger.info("Initial team budget calculation completed");
	}

	private static void calculateBaseBudget(Budget budget, double popularity) {
		if (budget == null) {
			logger.warn("Skipping base budget calculation because budget is null");
			return;
		}

		double initialAmount = FinanceConfiguration.BASE_TEAM_BUDGET;
		logger.trace("Base team budget starts at " + initialAmount);
		if (popularity <= 70) {
			initialAmount *= 1.1;
			logger.trace("Applied popularity multiplier 1.1 for popularity " + popularity);
		} else if (popularity <= 80) {
			initialAmount *= 1.3;
			logger.trace("Applied popularity multiplier 1.3 for popularity " + popularity);
		} else if (popularity <= 90) {
			initialAmount *= 1.45;
			logger.trace("Applied popularity multiplier 1.45 for popularity " + popularity);
		} else {
			initialAmount *= 1.6;
			logger.trace("Applied popularity multiplier 1.6 for popularity " + popularity);
		}
		budget.setInitialAmount(initialAmount);
		logger.debug("Base budget calculated at " + initialAmount);
	}
}

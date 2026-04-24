package process.builder.finance;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import process.visitor.marketsize.CalculateInitialTeamBudgetVisitor;

public class BudgetBuilder {

	public static void calculateInitialBudget(Budget budget, MarketSize marketSize, EconomicProfil economicProfil,
			double popularity) {
		calculateBaseBudget(budget, popularity);
		CalculateInitialTeamBudgetVisitor calculateInitialTeamBudgetVisitor = new CalculateInitialTeamBudgetVisitor(
				budget.getInitialAmount(), popularity, economicProfil);
		double initialAmount = marketSize.accept(calculateInitialTeamBudgetVisitor);
		budget.setInitialAmount(initialAmount);
		budget.setRemainingAmount(initialAmount);
	}

	private static void calculateBaseBudget(Budget budget, double popularity) {
		double initialAmount = FinanceConfiguration.BASE_TEAM_BUDGET;
		if (popularity <= 70) {
			initialAmount *= 1.1;
		} else if (popularity <= 80) {
			initialAmount *= 1.3;
		} else if (popularity <= 90) {
			initialAmount *= 1.45;
		} else {
			initialAmount *= 1.6;
		}
		budget.setInitialAmount(initialAmount);
	}
}

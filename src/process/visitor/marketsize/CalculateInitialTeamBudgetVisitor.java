package process.visitor.marketsize;

import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public class CalculateInitialTeamBudgetVisitor implements MarketSizeVisitor<Double> {

	private double baseBudget;

	public CalculateInitialTeamBudgetVisitor(double baseBudget) {
		super();
		this.baseBudget = baseBudget;
	}

	public Double visit(LargeSize largeSize) {
		return baseBudget * 1.2;
	}

	public Double visit(MediumSize mediumSize) {
		return baseBudget;
	}

	public Double visit(SmallSize smallSize) {
		return baseBudget * 0.8;
	}
}

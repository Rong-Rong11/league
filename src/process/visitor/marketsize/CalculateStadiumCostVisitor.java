package process.visitor.marketsize;

import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public class CalculateStadiumCostVisitor
		implements MarketSizeVisitor<Double> {
	private double baseCosts = 0.2;

	@Override
	public Double visit(LargeSize largeSize) {
		return this.baseCosts *= 1.3;
	}

	@Override
	public Double visit(MediumSize mediumSize) {
		return this.baseCosts *= 1.0;
	}

	@Override
	public Double visit(SmallSize smallSize) {
		return this.baseCosts *= 0.7;
	}
}

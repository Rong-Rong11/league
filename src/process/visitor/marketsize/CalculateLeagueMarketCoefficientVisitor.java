package process.visitor.marketsize;

import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public class CalculateLeagueMarketCoefficientVisitor implements MarketSizeVisitor<Double> {

	@Override
	public Double visit(LargeSize largeSize) {
		return 1.30;
	}

	@Override
	public Double visit(MediumSize mediumSize) {
		return 1.00;
	}

	@Override
	public Double visit(SmallSize smallSize) {
		return 0.70;
	}
}

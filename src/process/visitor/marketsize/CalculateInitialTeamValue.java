package process.visitor.marketsize;

import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public class CalculateInitialTeamValue implements MarketSizeVisitor<Double> {

	public CalculateInitialTeamValue() {
	}

	@Override
	public Double visit(LargeSize largeSize) {
	  return 140.0;
	}

	@Override
	public Double visit(MediumSize mediumSize) {
	  return 80.0;
	}

	@Override
	public Double visit(SmallSize smallSize) {
	  return 40.0;
	}
}

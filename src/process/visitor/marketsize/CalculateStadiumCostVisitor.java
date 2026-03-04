package process.visitor.marketsize;

import config.FinanceConfiguration;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public class CalculateStadiumCostVisitor implements MarketSizeVisitor<Double> {

	private double baseCosts = FinanceConfiguration.BASE_STADIUM_COSTS;

	public CalculateStadiumCostVisitor() {

	}

	public Double visit(LargeSize largeSize) {
		return baseCosts *= FinanceConfiguration.MARKET_SIZE_LARGE_MULTIPLIER;
	}

	public Double visit(MediumSize mediumSize) {
		return baseCosts *= FinanceConfiguration.MARKET_SIZE_MEDIUM_MULTIPLIER;
	}

	public Double visit(SmallSize smallSize) {
		return baseCosts *= FinanceConfiguration.MARKET_SIZE_SMALL_MULTIPLIER;
	}
}

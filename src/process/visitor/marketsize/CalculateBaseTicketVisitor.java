package process.visitor.marketsize;

import config.FinanceConfiguration;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public class CalculateBaseTicketVisitor implements MarketSizeVisitor<Double> {

	private double baseTicketPrice = FinanceConfiguration.BASE_TICKET_PRICE;

	public CalculateBaseTicketVisitor() {

	}

	public Double visit(LargeSize largeSize) {
		return baseTicketPrice * FinanceConfiguration.MARKET_SIZE_LARGE_MULTIPLIER;
	}

	public Double visit(MediumSize mediumSize) {
		return baseTicketPrice * FinanceConfiguration.MARKET_SIZE_MEDIUM_MULTIPLIER;
	}

	public Double visit(SmallSize smallSize) {
		return baseTicketPrice * FinanceConfiguration.MARKET_SIZE_SMALL_MULTIPLIER;
	}
}

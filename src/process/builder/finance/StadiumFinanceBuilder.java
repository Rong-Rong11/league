package process.builder.finance;

import org.apache.log4j.Logger;

import data.team.Stadium;
import data.team.finance.marketsize.MarketSize;
import log.LoggerUtility;
import process.visitor.marketsize.CalculateBaseTicketVisitor;
import process.visitor.marketsize.GenerateStadiumCapacityVisitor;

public class StadiumFinanceBuilder {
	private static final Logger logger = LoggerUtility.getLogger(StadiumFinanceBuilder.class, "text");

	public static void configureStadium(Stadium stadium, MarketSize marketSize) {
		if (stadium == null) {
			logger.warn("Skipping stadium configuration because stadium is null");
			return;
		}
		if (marketSize == null) {
			logger.warn("Skipping stadium configuration because market size is null");
			return;
		}

		logger.debug("Configuring stadium finance from market size " + marketSize.getClass().getSimpleName());
		stadium.setCapacity(generateCapacity(marketSize));
		stadium.setTicketPrice(calculateBaseTicketPrice(marketSize));
		logger.debug("Stadium configured with capacity "
				+ stadium.getCapacity()
				+ " and ticket price "
				+ stadium.getTicketPrice());
	}

	private static int generateCapacity(MarketSize marketSize) {
		if (marketSize == null) {
			logger.warn("Skipping stadium capacity generation because market size is null");
			return 0;
		}

		logger.trace("Generating stadium capacity for " + marketSize.getClass().getSimpleName());
		GenerateStadiumCapacityVisitor generateStadiumCapacityVisitor = new GenerateStadiumCapacityVisitor();
		return marketSize.accept(generateStadiumCapacityVisitor);
	}

	private static double calculateBaseTicketPrice(MarketSize marketSize) {
		if (marketSize == null) {
			logger.warn("Skipping base ticket price calculation because market size is null");
			return 0.0;
		}

		logger.trace("Calculating base ticket price for " + marketSize.getClass().getSimpleName());
		CalculateBaseTicketVisitor calculateBaseTicketVisitor = new CalculateBaseTicketVisitor();
		return marketSize.accept(calculateBaseTicketVisitor);
	}
}

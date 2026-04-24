package process.builder.finance;

import data.team.Stadium;
import data.team.finance.marketsize.MarketSize;
import process.visitor.marketsize.CalculateBaseTicketVisitor;
import process.visitor.marketsize.GenerateStadiumCapacityVisitor;

public class StadiumFinanceBuilder {

	public static void configureStadium(Stadium stadium, MarketSize marketSize) {
		stadium.setCapacity(generateCapacity(marketSize));
		stadium.setTicketPrice(calculateBaseTicketPrice(marketSize));
	}

	private static int generateCapacity(MarketSize marketSize) {
		GenerateStadiumCapacityVisitor generateStadiumCapacityVisitor = new GenerateStadiumCapacityVisitor();
		return marketSize.accept(generateStadiumCapacityVisitor);
	}

	private static double calculateBaseTicketPrice(MarketSize marketSize) {
		CalculateBaseTicketVisitor calculateBaseTicketVisitor = new CalculateBaseTicketVisitor();
		return marketSize.accept(calculateBaseTicketVisitor);
	}
}

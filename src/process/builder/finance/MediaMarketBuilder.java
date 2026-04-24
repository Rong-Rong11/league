package process.builder.finance;

import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import process.visitor.marketsize.CreateMediaMarketVisitor;

public class MediaMarketBuilder {

	public static void createMediaMarket(MediaMarket mediaMarket, MarketSize marketSize) {
		marketSize.accept(new CreateMediaMarketVisitor(mediaMarket));
	}
}

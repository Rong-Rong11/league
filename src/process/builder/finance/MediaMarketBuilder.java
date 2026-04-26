package process.builder.finance;

import org.apache.log4j.Logger;

import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;
import process.visitor.marketsize.CreateMediaMarketVisitor;

public class MediaMarketBuilder {
	private static final Logger logger = LoggerUtility.getLogger(MediaMarketBuilder.class, "text");

	public static void createMediaMarket(MediaMarket mediaMarket, MarketSize marketSize) {
		if (mediaMarket == null) {
			logger.warn("Skipping media market creation because media market is null");
			return;
		}
		if (marketSize == null) {
			logger.warn("Skipping media market creation because market size is null");
			return;
		}

		logger.debug("Creating media market from market size " + marketSize.getClass().getSimpleName());
		logger.trace("Applying CreateMediaMarketVisitor to " + marketSize.getClass().getSimpleName());
		marketSize.accept(new CreateMediaMarketVisitor(mediaMarket));
		logger.debug("Media market created successfully");
	}
}

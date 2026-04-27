package data.team.finance;

import data.team.finance.economicprofile.EconomicProfile;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;

public class TeamFinanceStructure {

	private EconomicProfile economicProfile;
	private MarketSize marketSize;
	private MediaMarket mediaMarket;

	public TeamFinanceStructure(MarketSize marketSize) {
		this(new EconomicProfile(), marketSize, new MediaMarket());
	}

	public TeamFinanceStructure(EconomicProfile economicProfile, MarketSize marketSize, MediaMarket mediaMarket) {
		this.economicProfile = economicProfile;
		this.marketSize = marketSize;
		this.mediaMarket = mediaMarket;
	}

	public EconomicProfile getEconomicProfile() {
		return economicProfile;
	}

	public void setEconomicProfile(EconomicProfile economicProfile) {
		this.economicProfile = economicProfile;
	}

	public MarketSize getMarketSize() {
		return marketSize;
	}

	public void setMarketSize(MarketSize marketSize) {
		this.marketSize = marketSize;
	}

	public MediaMarket getMediaMarket() {
		return mediaMarket;
	}

	public void setMediaMarket(MediaMarket mediaMarket) {
		this.mediaMarket = mediaMarket;
	}
}

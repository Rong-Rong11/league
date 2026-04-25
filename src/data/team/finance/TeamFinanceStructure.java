package data.team.finance;

import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;

public class TeamFinanceStructure {

	private EconomicProfil economicProfil;
	private MarketSize marketSize;
	private MediaMarket mediaMarket;

	public TeamFinanceStructure(MarketSize marketSize) {
		this(new EconomicProfil(), marketSize, new MediaMarket());
	}

	public TeamFinanceStructure(EconomicProfil economicProfil, MarketSize marketSize, MediaMarket mediaMarket) {
		this.economicProfil = economicProfil;
		this.marketSize = marketSize;
		this.mediaMarket = mediaMarket;
	}

	public EconomicProfil getEconomicProfil() {
		return economicProfil;
	}

	public void setEconomicProfil(EconomicProfil economicProfil) {
		this.economicProfil = economicProfil;
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

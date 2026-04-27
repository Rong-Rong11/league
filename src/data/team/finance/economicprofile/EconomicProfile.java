package data.team.finance.economicprofile;

public class EconomicProfile {
	private double fanLoyalty = 0.5;
	private double priceElasticity = 0.5;
	private double commercialAggressiveness = 0.5;
	private double historicalPrestige = 0.5;
	private double ownerDeficitTolerance = 0.5;

	public EconomicProfile() {

	}

	public EconomicProfile(double fanLoyalty, double priceElasticity, double commercialAggressiveness,
			double historicalPrestige, double ownerDeficitTolerance) {
		super();
		this.fanLoyalty = fanLoyalty;
		this.priceElasticity = priceElasticity;
		this.commercialAggressiveness = commercialAggressiveness;
		this.historicalPrestige = historicalPrestige;
		this.ownerDeficitTolerance = ownerDeficitTolerance;
	}

	public double getFanLoyalty() {
		return fanLoyalty;
	}

	public void setFanLoyalty(double fanLoyalty) {
		this.fanLoyalty = fanLoyalty;
	}

	public double getPriceElasticity() {
		return priceElasticity;
	}

	public void setPriceElasticity(double priceElasticity) {
		this.priceElasticity = priceElasticity;
	}

	public double getCommercialAggressiveness() {
		return commercialAggressiveness;
	}

	public void setCommercialAggressiveness(double commercialAggressiveness) {
		this.commercialAggressiveness = commercialAggressiveness;
	}

	public double getHistoricalPrestige() {
		return historicalPrestige;
	}

	public void setHistoricalPrestige(double historicalPrestige) {
		this.historicalPrestige = historicalPrestige;
	}

	public double getOwnerDeficitTolerance() {
		return ownerDeficitTolerance;
	}

	public void setOwnerDeficitTolerance(double ownerDeficitTolerance) {
		this.ownerDeficitTolerance = ownerDeficitTolerance;
	}

}

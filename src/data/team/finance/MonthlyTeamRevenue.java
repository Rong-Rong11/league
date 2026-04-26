package data.team.finance;

public class MonthlyTeamRevenue {
	private double localSponsoring;
	private double localMerchandising;
	private double otherRevenue;

	public MonthlyTeamRevenue(double localSponsoring, double localMerchandising, double otherRevenue) {
		this.localSponsoring = localSponsoring;
		this.localMerchandising = localMerchandising;
		this.otherRevenue = otherRevenue;
	}

	public double getLocalSponsoring() {
		return localSponsoring;
	}

	public double getLocalMerchandising() {
		return localMerchandising;
	}

	public double getOtherRevenue() {
		return otherRevenue;
	}
}

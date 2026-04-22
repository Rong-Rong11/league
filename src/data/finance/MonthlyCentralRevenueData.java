package data.finance;

public class MonthlyCentralRevenueData {
	private final int month;
	private final double nationalTvRevenue;
	private final double nationalSponsoringRevenue;
	private final double nationalMerchandisingRevenue;
	private final double totalCentralRevenue;
	private final double leagueRetainedRevenue;
	private final double redistributedRevenue;

	public MonthlyCentralRevenueData(int month, double nationalTvRevenue,
			double nationalSponsoringRevenue, double nationalMerchandisingRevenue,
			double leagueRetainedRevenue, double redistributedRevenue) {
		this.month = month;
		this.nationalTvRevenue = nationalTvRevenue;
		this.nationalSponsoringRevenue = nationalSponsoringRevenue;
		this.nationalMerchandisingRevenue = nationalMerchandisingRevenue;
		this.totalCentralRevenue = nationalTvRevenue + nationalSponsoringRevenue + nationalMerchandisingRevenue;
		this.leagueRetainedRevenue = leagueRetainedRevenue;
		this.redistributedRevenue = redistributedRevenue;
	}

	public int getMonth() {
		return month;
	}

	public double getNationalTvRevenue() {
		return nationalTvRevenue;
	}

	public double getNationalSponsoringRevenue() {
		return nationalSponsoringRevenue;
	}

	public double getNationalMerchandisingRevenue() {
		return nationalMerchandisingRevenue;
	}

	public double getTotalCentralRevenue() {
		return totalCentralRevenue;
	}

	public double getLeagueRetainedRevenue() {
		return leagueRetainedRevenue;
	}

	public double getRedistributedRevenue() {
		return redistributedRevenue;
	}
}

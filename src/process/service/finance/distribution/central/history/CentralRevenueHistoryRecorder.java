package process.service.finance.distribution.central.history;

import data.finance.MonthlyCentralRevenueData;
import data.league.League;

public class CentralRevenueHistoryRecorder {

	public void storeMonthlyCentralRevenueData(League league, int month, double tvRevenue, double globalSponsors,
			double merchandisingRevenue, double leagueRetainedRevenue) {
		double totalCentralRevenue = tvRevenue + globalSponsors + merchandisingRevenue;
		double redistributedRevenue = totalCentralRevenue - leagueRetainedRevenue;

		MonthlyCentralRevenueData revenueData = new MonthlyCentralRevenueData(
				month,
				tvRevenue,
				globalSponsors,
				merchandisingRevenue,
				leagueRetainedRevenue,
				redistributedRevenue);

		league.getLeagueFinance().getMonthlyCentralRevenueHistory().put(month, revenueData);
	}
}

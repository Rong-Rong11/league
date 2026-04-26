package process.service.finance.distribution.central.history;

import org.apache.log4j.Logger;

import data.finance.MonthlyCentralRevenueData;
import data.league.League;
import log.LoggerUtility;

public class CentralRevenueHistoryRecorder {
	private static final Logger logger = LoggerUtility.getLogger(CentralRevenueHistoryRecorder.class, "text");

	public void storeMonthlyCentralRevenueData(League league, int month, double tvRevenue, double globalSponsors,
			double merchandisingRevenue, double leagueRetainedRevenue) {
		if (league == null || league.getLeagueFinance() == null) {
			logger.warn("Skipping monthly central revenue history storage because league or league finance is null");
			return;
		}
		logger.debug("Storing monthly central revenue data for month " + month);
		logger.debug("Monthly central revenue inputs: tvRevenue="
				+ tvRevenue
				+ ", globalSponsors="
				+ globalSponsors
				+ ", merchandisingRevenue="
				+ merchandisingRevenue
				+ ", leagueRetainedRevenue="
				+ leagueRetainedRevenue);
		double totalCentralRevenue = tvRevenue + globalSponsors + merchandisingRevenue;
		double redistributedRevenue = totalCentralRevenue - leagueRetainedRevenue;
		logger.trace("Total central revenue is " + totalCentralRevenue);
		logger.trace("Redistributed central revenue is " + redistributedRevenue);

		MonthlyCentralRevenueData revenueData = new MonthlyCentralRevenueData(
				month,
				tvRevenue,
				globalSponsors,
				merchandisingRevenue,
				leagueRetainedRevenue,
				redistributedRevenue);

		league.getLeagueFinance().getMonthlyCentralRevenueHistory().put(month, revenueData);
		logger.debug("Monthly central revenue history stored for month " + month);
	}
}

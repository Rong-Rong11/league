package process.service.finance.distribution.central.profile;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import data.league.finance.CentralRevenueProfile;
import log.LoggerUtility;

public class CentralRevenueProfileResolver {
	private static final Logger logger = LoggerUtility.getLogger(CentralRevenueProfileResolver.class, "text");

	public CentralRevenueProfile getRevenueProfile(int month) {
		if (isPlayoffMonth(month)) {
			logger.debug("Resolving playoff central revenue profile for month " + month);
			CentralRevenueProfile profile = new CentralRevenueProfile(
					FinanceConfiguration.PLAYOFF_CENTRAL_TV_RATE,
					FinanceConfiguration.PLAYOFF_CENTRAL_SPONSORING_RATE,
					FinanceConfiguration.PLAYOFF_CENTRAL_MERCH_RATE);
			logger.debug("Resolved playoff central revenue profile with tvRate="
					+ profile.getTvRate()
					+ ", sponsoringRate="
					+ profile.getSponsoringRate()
					+ ", merchandisingRate="
					+ profile.getMerchandisingRate());
			return profile;
		}
		logger.debug("Resolving regular season central revenue profile for month " + month);
		CentralRevenueProfile profile = new CentralRevenueProfile(
				FinanceConfiguration.REGULAR_SEASON_CENTRAL_TV_RATE,
				FinanceConfiguration.REGULAR_SEASON_CENTRAL_SPONSORING_RATE,
				FinanceConfiguration.REGULAR_SEASON_CENTRAL_MERCH_RATE);
		logger.debug("Resolved regular season central revenue profile with tvRate="
				+ profile.getTvRate()
				+ ", sponsoringRate="
				+ profile.getSponsoringRate()
				+ ", merchandisingRate="
				+ profile.getMerchandisingRate());
		return profile;
	}

	private boolean isPlayoffMonth(int month) {
		boolean playoffMonth = month >= 8;
		if (playoffMonth) {
			logger.trace("Month " + month + " is a playoff month for central revenue profile");
		}
		return playoffMonth;
	}
}

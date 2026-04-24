package process.service.finance.distribution.central.profile;

import config.FinanceConfiguration;

public class CentralRevenueProfileResolver {

	public CentralRevenueProfile getRevenueProfile(int month) {
		if (isPlayoffMonth(month)) {
			return new CentralRevenueProfile(
					FinanceConfiguration.PLAYOFF_CENTRAL_TV_RATE,
					FinanceConfiguration.PLAYOFF_CENTRAL_SPONSORING_RATE,
					FinanceConfiguration.PLAYOFF_CENTRAL_MERCH_RATE);
		}
		return new CentralRevenueProfile(
				FinanceConfiguration.REGULAR_SEASON_CENTRAL_TV_RATE,
				FinanceConfiguration.REGULAR_SEASON_CENTRAL_SPONSORING_RATE,
				FinanceConfiguration.REGULAR_SEASON_CENTRAL_MERCH_RATE);
	}

	private boolean isPlayoffMonth(int month) {
		return month >= 8;
	}
}

package process.service.finance.team;

import config.FinanceConfiguration;
import data.league.League;

public class RegularSeasonMonthlyTeamFinanceCalculator extends AbstractMonthlyTeamFinanceCalculator {

	public RegularSeasonMonthlyTeamFinanceCalculator(League league) {
		super(league);
	}

	@Override
	public double getLocalSponsoringMultiplier() {
		return FinanceConfiguration.REGULAR_SEASON_LOCAL_SPONSORING_RATE;
	}

	@Override
	public double getLocalMerchandisingMultiplier() {
		return FinanceConfiguration.REGULAR_SEASON_LOCAL_MERCH_RATE;
	}

	@Override
	public double getOtherRevenueMultiplier() {
		return FinanceConfiguration.REGULAR_SEASON_OTHER_LOCAL_RATE;
	}
}

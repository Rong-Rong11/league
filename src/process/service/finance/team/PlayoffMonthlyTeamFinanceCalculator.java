package process.service.finance.team;

import config.FinanceConfiguration;
import data.league.League;

public class PlayoffMonthlyTeamFinanceCalculator extends AbstractMonthlyTeamFinanceCalculator {

	public PlayoffMonthlyTeamFinanceCalculator(League league) {
		super(league);
	}

	@Override
	protected double getLocalSponsoringMultiplier() {
		return FinanceConfiguration.PLAYOFF_LOCAL_SPONSORING_RATE;
	}

	@Override
	protected double getLocalMerchandisingMultiplier() {
		return FinanceConfiguration.PLAYOFF_LOCAL_MERCH_RATE;
	}

	@Override
	protected double getOtherRevenueMultiplier() {
		return FinanceConfiguration.PLAYOFF_OTHER_LOCAL_RATE;
	}
}

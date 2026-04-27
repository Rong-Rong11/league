package process.service.finance.team.provider;

public interface MonthlyTeamFinanceMultiplierProvider {
	double getLocalSponsoringMultiplier();

	double getLocalMerchandisingMultiplier();

	double getOtherRevenueMultiplier();
}

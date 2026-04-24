package process.service.finance.distribution.central;

import data.finance.budget.Budget;
import data.finance.budget.income.IncomeType;
import data.league.League;
import process.service.finance.distribution.central.calculation.MonthlyCentralRevenueCalculator;
import process.service.finance.distribution.central.history.CentralRevenueHistoryRecorder;
import process.service.finance.distribution.central.profile.CentralRevenueProfile;
import process.service.finance.distribution.central.profile.CentralRevenueProfileResolver;
import process.service.finance.distribution.league.LeagueRevenueRetainer;
import process.service.finance.distribution.team.TeamCentralShareDistributor;
import process.service.finance.FinanceManager;
import process.utility.FinanceUtility;

public class CentralRevenueDistributor {
	private League league;
	private MonthlyCentralRevenueCalculator monthlyCentralRevenueCalculator;
	private CentralRevenueProfileResolver profileResolver = new CentralRevenueProfileResolver();
	private LeagueRevenueRetainer leagueRevenueRetainer = new LeagueRevenueRetainer();
	private TeamCentralShareDistributor teamCentralShareDistributor = new TeamCentralShareDistributor();
	private CentralRevenueHistoryRecorder historyRecorder = new CentralRevenueHistoryRecorder();

	public CentralRevenueDistributor(League league) {
		this.league = league;
		monthlyCentralRevenueCalculator = new MonthlyCentralRevenueCalculator(league);
	}

	public void setFinanceManager(FinanceManager financeManager) {
		monthlyCentralRevenueCalculator.setFinanceManager(financeManager);
	}

	public void distributeMonthlyCentralRevenue(int month) {
		CentralRevenueProfile revenueProfile = profileResolver.getRevenueProfile(month);
		double tvRevenue = monthlyCentralRevenueCalculator.calculateNationalTvRevenue(revenueProfile, month);
		double globalSponsors = monthlyCentralRevenueCalculator.calculateNationalSponsoringRevenue(revenueProfile,
				month);
		double merchandisingRevenue = monthlyCentralRevenueCalculator
				.calculateNationalMerchandisingRevenue(revenueProfile, month);
		distribute(tvRevenue, globalSponsors, merchandisingRevenue, month);
	}

	private void distribute(double tvRevenue, double globalSponsors, double merchandisingRevenue, int month) {
		Budget leagueBudget = league.getLeagueFinance().getBudget();
		double leagueTvCut = leagueRevenueRetainer.calculateLeagueCut(tvRevenue);
		double leagueSponsorsCut = leagueRevenueRetainer.calculateLeagueCut(globalSponsors);
		double leagueMerchandisingCut = leagueRevenueRetainer.calculateLeagueCut(merchandisingRevenue);

		double distributableTv = leagueRevenueRetainer.retainLeagueCut(
				leagueBudget,
				tvRevenue,
				IncomeType.NATIONAL_TV,
				month);

		double distributableSponsors = leagueRevenueRetainer.retainLeagueCut(
				leagueBudget,
				globalSponsors,
				IncomeType.NATIONAL_SPONSORING,
				month);

		double distributableMerchandising = leagueRevenueRetainer.retainLeagueCut(
				leagueBudget,
				merchandisingRevenue,
				IncomeType.NATIONAL_MERCHANDISING,
				month);

		teamCentralShareDistributor.distributeTvShare(distributableTv, month);
		teamCentralShareDistributor.distributeNationalSponsoringShare(distributableSponsors, month);
		teamCentralShareDistributor.distributeMerchandisingShare(distributableMerchandising, month);
		historyRecorder.storeMonthlyCentralRevenueData(league, month, tvRevenue, globalSponsors, merchandisingRevenue,
				leagueTvCut + leagueSponsorsCut + leagueMerchandisingCut);

		FinanceUtility.updateBudget(leagueBudget);
	}

}

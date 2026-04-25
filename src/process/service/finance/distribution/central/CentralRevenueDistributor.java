package process.service.finance.distribution.central;

import org.apache.log4j.Logger;

import data.finance.budget.Budget;
import data.finance.budget.income.IncomeType;
import data.league.League;
import data.league.finance.CentralRevenueProfile;
import log.LoggerUtility;
import process.service.finance.FinanceManager;
import process.service.finance.distribution.central.calculation.MonthlyCentralRevenueCalculator;
import process.service.finance.distribution.central.history.CentralRevenueHistoryRecorder;
import process.service.finance.distribution.central.profile.CentralRevenueProfileResolver;
import process.service.finance.distribution.league.LeagueRevenueRetainer;
import process.service.finance.distribution.team.TeamCentralShareDistributor;
import process.utility.FinanceUtility;

public class CentralRevenueDistributor {
	private static final Logger logger = LoggerUtility.getLogger(CentralRevenueDistributor.class, "text");

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
		logger.debug("Setting finance manager for central revenue distributor");
		monthlyCentralRevenueCalculator.setFinanceManager(financeManager);
	}

	public void distributeMonthlyCentralRevenue(int month) {
		if (league == null || league.getLeagueFinance() == null) {
			logger.warn("Skipping central revenue distribution because league or league finance is null");
			return;
		}
		logger.info("Distributing monthly central revenue for month " + month);
		CentralRevenueProfile revenueProfile = profileResolver.getRevenueProfile(month);
		logger.debug("Resolved central revenue profile for month " + month);
		double tvRevenue = monthlyCentralRevenueCalculator.calculateNationalTvRevenue(revenueProfile, month);
		double globalSponsors = monthlyCentralRevenueCalculator.calculateNationalSponsoringRevenue(revenueProfile,
				month);
		double merchandisingRevenue = monthlyCentralRevenueCalculator
				.calculateNationalMerchandisingRevenue(revenueProfile, month);
		logger.debug("Calculated monthly central revenues: tv="
				+ tvRevenue
				+ ", sponsors="
				+ globalSponsors
				+ ", merchandising="
				+ merchandisingRevenue);
		distribute(tvRevenue, globalSponsors, merchandisingRevenue, month);
		logger.info("Monthly central revenue distribution completed for month " + month);
	}

	private void distribute(double tvRevenue, double globalSponsors, double merchandisingRevenue, int month) {
		logger.debug("Applying central revenue distribution for month " + month);
		Budget leagueBudget = league.getLeagueFinance().getBudget();
		double leagueTvCut = leagueRevenueRetainer.calculateLeagueCut(tvRevenue);
		double leagueSponsorsCut = leagueRevenueRetainer.calculateLeagueCut(globalSponsors);
		double leagueMerchandisingCut = leagueRevenueRetainer.calculateLeagueCut(merchandisingRevenue);
		double totalLeagueCut = leagueTvCut + leagueSponsorsCut + leagueMerchandisingCut;
		logger.debug("League retained central revenue cut is " + totalLeagueCut);

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
		logger.debug("Distributable central revenues: tv="
				+ distributableTv
				+ ", sponsors="
				+ distributableSponsors
				+ ", merchandising="
				+ distributableMerchandising);

		logger.trace("Distributing TV central revenue share");
		teamCentralShareDistributor.distributeTvShare(distributableTv, month);
		logger.trace("Distributing national sponsoring central revenue share");
		teamCentralShareDistributor.distributeNationalSponsoringShare(distributableSponsors, month);
		logger.trace("Distributing merchandising central revenue share");
		teamCentralShareDistributor.distributeMerchandisingShare(distributableMerchandising, month);
		historyRecorder.storeMonthlyCentralRevenueData(league, month, tvRevenue, globalSponsors, merchandisingRevenue,
				totalLeagueCut);

		FinanceUtility.updateBudget(leagueBudget);
		logger.debug("League budget updated after central revenue distribution for month " + month);
	}

}

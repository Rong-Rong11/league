package process.manager.financetools;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.Income;
import data.league.League;
import data.team.Team;
import process.repositery.TeamRepositery;
import process.utilitary.FinanceUtilitary;

public class CentralRevenueDistributor {
	private League league;
	private TeamRepositery teamRepositery = TeamRepositery.getInstance();
	private MonthlyCentralRevenueCalculator monthlyCentralRevenueCalculator;

	public CentralRevenueDistributor(League league) {
		this.league = league;
		monthlyCentralRevenueCalculator = new MonthlyCentralRevenueCalculator();
	}

	public void distributeMonthlyCentralRevenue(int month) {
		double tvRevenue = monthlyCentralRevenueCalculator.calculateNationalTvRevenue();
		double globalSponsors = monthlyCentralRevenueCalculator.calculateNationalSponsoringRevenue();
		double merchandisingRevenue = monthlyCentralRevenueCalculator.calculateNationalMerchandisingRevenue();
		distribute(tvRevenue, globalSponsors, merchandisingRevenue, month);
	}

	private void distribute(double tvRevenue, double globalSponsors, double merchandisingRevenue, int month) {
		Budget leagueBudget = league.getLeagueFinance().getBudget();
		double totalCentralRevenue = tvRevenue + globalSponsors + merchandisingRevenue;

		double leagueTVCut = tvRevenue * FinanceConfiguration.LEAGUE_OPERATING_RATE;
		double leagueSponsorsCut = globalSponsors * FinanceConfiguration.LEAGUE_OPERATING_RATE;
		double leagueMerchandisingCut = merchandisingRevenue * FinanceConfiguration.LEAGUE_OPERATING_RATE;
		FinanceUtilitary.addIncome(leagueBudget, new Income(FinanceConfiguration.INCOME_TYPE_NATIONAL_TV, leagueTVCut),
				month);
		FinanceUtilitary.addIncome(leagueBudget,
				new Income(FinanceConfiguration.INCOME_TYPE_NATIONAL_SPONSORING, leagueSponsorsCut), month);
		FinanceUtilitary.addIncome(leagueBudget,
				new Income(FinanceConfiguration.INCOME_TYPE_NATIONAL_MERCHANDISING, leagueMerchandisingCut), month);

		double distributableRevenue = totalCentralRevenue - (leagueTVCut + leagueMerchandisingCut + leagueSponsorsCut);
		double share = distributableRevenue / teamRepositery.getAllTeams().size();

		for (Team team : teamRepositery.getAllTeams()) {
			Budget budget = team.getTeamFinance().getBudget();
			FinanceUtilitary.addIncome(budget, new Income(FinanceConfiguration.INCOME_TYPE_CENTRAL_SHARE, share), month);
			FinanceUtilitary.updateBudget(budget);
		}
		FinanceUtilitary.updateBudget(leagueBudget);
	}
}

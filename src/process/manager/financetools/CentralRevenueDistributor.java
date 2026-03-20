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

        double distributableTv = retainLeagueCut(
                leagueBudget,
                tvRevenue,
                FinanceConfiguration.INCOME_TYPE_NATIONAL_TV,
                month);

        double distributableSponsors = retainLeagueCut(
                leagueBudget,
                globalSponsors,
                FinanceConfiguration.INCOME_TYPE_NATIONAL_SPONSORING,
                month);

        double distributableMerchandising = retainLeagueCut(
                leagueBudget,
                merchandisingRevenue,
                FinanceConfiguration.INCOME_TYPE_NATIONAL_MERCHANDISING,
                month);

        distributeEqualShare(distributableTv, FinanceConfiguration.INCOME_TYPE_CENTRAL_SHARE, month);
        distributeEqualShare(distributableSponsors, FinanceConfiguration.INCOME_TYPE_CENTRAL_SHARE, month);
        distributeMerchandisingShare(distributableMerchandising, month);

        FinanceUtilitary.updateBudget(leagueBudget);
    }

    private double retainLeagueCut(Budget leagueBudget, double revenue, String incomeType, int month) {
        double leagueCut = revenue * FinanceConfiguration.LEAGUE_OPERATING_RATE;
        FinanceUtilitary.addIncome(leagueBudget, new Income(incomeType, leagueCut), month);
        return revenue - leagueCut;
    }

    private void distributeEqualShare(double amount, String incomeType, int month) {
        double share = amount / teamRepositery.getAllTeams().size();

        for (Team team : teamRepositery.getAllTeams()) {
            Budget budget = team.getTeamFinance().getBudget();
            FinanceUtilitary.addIncome(budget, new Income(incomeType, share), month);
            FinanceUtilitary.updateBudget(budget);
        }
    }

    private void distributeMerchandisingShare(double merchandisingRevenue, int month) {
        double equalPart = merchandisingRevenue * 0.7;
        double weightedPart = merchandisingRevenue * 0.3;
        distributeEqualShare(equalPart, FinanceConfiguration.INCOME_TYPE_CENTRAL_SHARE, month);

        double totalScore = 0.0;
        for (Team team : teamRepositery.getAllTeams()) {
            totalScore += FinanceUtilitary.calculateMerchandisingScore(team);
        }

        for (Team team : teamRepositery.getAllTeams()) {
            double score = FinanceUtilitary.calculateMerchandisingScore(team);
            double share = weightedPart * (score / totalScore);

            Budget budget = team.getTeamFinance().getBudget();
            FinanceUtilitary.addIncome(budget, new Income(FinanceConfiguration.INCOME_TYPE_CENTRAL_SHARE, share),
                    month);
            FinanceUtilitary.updateBudget(budget);
        }
    }

}

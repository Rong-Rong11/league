package process.manager.financetools;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
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
                IncomeType.NATIONAL_TV,
                month);

        double distributableSponsors = retainLeagueCut(
                leagueBudget,
                globalSponsors,
                IncomeType.NATIONAL_SPONSORING,
                month);

        double distributableMerchandising = retainLeagueCut(
                leagueBudget,
                merchandisingRevenue,
                IncomeType.NATIONAL_MERCHANDISING,
                month);

        distributeEqualShare(distributableTv, IncomeType.CENTRAL_SHARE, month);
        distributeEqualShare(distributableSponsors, IncomeType.CENTRAL_SHARE, month);
        distributeMerchandisingShare(distributableMerchandising, month);

        FinanceUtilitary.updateBudget(leagueBudget);
    }

    private double retainLeagueCut(Budget leagueBudget, double revenue, IncomeType incomeType, int month) {
        double leagueCut = revenue * FinanceConfiguration.LEAGUE_OPERATING_RATE;
        FinanceUtilitary.addIncome(leagueBudget, new Income(incomeType, leagueCut), month);
        return revenue - leagueCut;
    }

    private void distributeEqualShare(double amount, IncomeType incomeType, int month) {
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
        distributeEqualShare(equalPart, IncomeType.CENTRAL_SHARE, month);

        double totalScore = 0.0;
        for (Team team : teamRepositery.getAllTeams()) {
            totalScore += FinanceUtilitary.calculateMerchandisingScore(team);
        }

        for (Team team : teamRepositery.getAllTeams()) {
            double score = FinanceUtilitary.calculateMerchandisingScore(team);
            double share = weightedPart * (score / totalScore);

            Budget budget = team.getTeamFinance().getBudget();
            FinanceUtilitary.addIncome(budget, new Income(IncomeType.CENTRAL_SHARE, share),
                    month);
            FinanceUtilitary.updateBudget(budget);
        }
    }

}

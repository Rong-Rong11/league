/*
 * Decompiled with CFR 0.152.
 */
package process.manager.financetools;

import data.finance.budget.Budget;
import data.finance.budget.Income;
import data.league.League;
import data.team.Team;
import process.manager.financetools.MonthlyCentralRevenueCalculator;
import process.repositery.TeamRepositery;
import process.utilitary.FinanceUtilitary;

public class CentralRevenueDistributor {
    private League league;
    private TeamRepositery teamRepositery = TeamRepositery.getInstance();
    private MonthlyCentralRevenueCalculator monthlyCentralRevenueCalculator;

    public CentralRevenueDistributor(League league) {
        this.league = league;
        this.monthlyCentralRevenueCalculator = new MonthlyCentralRevenueCalculator();
    }

    public void distributeMonthlyCentralRevenue(int n) {
        double d = this.monthlyCentralRevenueCalculator.calculateNationalTvRevenue();
        double d2 = this.monthlyCentralRevenueCalculator.calculateNationalSponsoringRevenue();
        double d3 = this.monthlyCentralRevenueCalculator.calculateNationalMerchandisingRevenue();
        this.distribute(d, d2, d3, n);
    }

    private void distribute(double d, double d2, double d3, int n) {
        Budget budget = this.league.getLeagueFinance().getBudget();
        double d4 = d + d2 + d3;
        double d5 = d * 0.1;
        double d6 = d2 * 0.1;
        double d7 = d3 * 0.1;
        FinanceUtilitary.addIncome(budget, new Income("national TV", d5), n);
        FinanceUtilitary.addIncome(budget, new Income("national sponsoring", d6), n);
        FinanceUtilitary.addIncome(budget, new Income("national merchandising", d7), n);
        double d8 = d4 - (d5 + d7 + d6);
        double d9 = d8 / (double)this.teamRepositery.getAllTeams().size();
        for (Team team : this.teamRepositery.getAllTeams()) {
            Budget budget2 = team.getTeamFinance().getBudget();
            FinanceUtilitary.addIncome(budget2, new Income("central share", d9), n);
            FinanceUtilitary.updateBudget(budget2);
        }
        FinanceUtilitary.updateBudget(budget);
    }
}

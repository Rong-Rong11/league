/*
 * Decompiled with CFR 0.152.
 */
package process.manager.financetools;

import data.finance.budget.Budget;
import data.finance.budget.Expense;
import data.finance.budget.Income;
import data.league.League;
import data.team.Team;
import process.repositery.TeamRepositery;
import process.utilitary.FinanceUtilitary;

public class RevenueSharingManager {
    private TeamRepositery teamRepositery = TeamRepositery.getInstance();
    private League league;

    public RevenueSharingManager(League league) {
        this.league = league;
    }

    public void applyRevenueSharing(int n) {
        double d = this.calculateLeagueLocalAverage(n);
        double d2 = this.collectFromRichTeams(d, n);
        double d3 = d2 * 0.05;
        FinanceUtilitary.addIncome(this.league.getLeagueFinance().getBudget(), new Income("income revenue sharing", d3), n);
        double d4 = d2 - d3;
        this.distributeToSmallTeams(d, d4, n);
    }

    private double calculateLeagueLocalAverage(int n) {
        double d = 0.0;
        for (Team team : this.teamRepositery.getAllTeams()) {
            double d2 = FinanceUtilitary.getTeamLocalRevenueOfMonth(team, n);
            Budget budget = team.getTeamFinance().getBudget();
            d += d2;
        }
        return d / (double)this.teamRepositery.getAllTeams().size();
    }

    private double collectFromRichTeams(double d, int n) {
        double d2 = 0.0;
        for (Team team : this.teamRepositery.getAllTeams()) {
            double d3 = FinanceUtilitary.getTeamLocalRevenueOfMonth(team, n);
            Budget budget = team.getTeamFinance().getBudget();
            if (!(d3 > d)) continue;
            double d4 = d3 - d;
            double d5 = d4 * 0.25;
            FinanceUtilitary.addExpense(budget, new Expense("expense revenue sharing", d5), n);
            FinanceUtilitary.updateBudget(budget);
            d2 += d5;
        }
        return d2;
    }

    private void distributeToSmallTeams(double d, double d2, int n) {
        Budget budget;
        double d3;
        double d4 = 0.0;
        for (Team team : this.teamRepositery.getAllTeams()) {
            d3 = FinanceUtilitary.getTeamLocalRevenueOfMonth(team, n);
            budget = team.getTeamFinance().getBudget();
            if (!(d3 < d)) continue;
            d4 += d - d3;
        }
        if (d4 == 0.0) {
            return;
        }
        for (Team team : this.teamRepositery.getAllTeams()) {
            d3 = FinanceUtilitary.getTeamLocalRevenueOfMonth(team, n);
            budget = team.getTeamFinance().getBudget();
            if (!(d3 < d) || !(d2 >= 0.0)) continue;
            double d5 = d - d3;
            double d6 = d5 / d4 * d2;
            d2 -= d6;
            FinanceUtilitary.addIncome(budget, new Income("income revenue sharing", d6), n);
            FinanceUtilitary.updateBudget(budget);
        }
    }
}

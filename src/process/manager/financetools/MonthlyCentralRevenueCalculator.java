/*
 * Decompiled with CFR 0.152.
 */
package process.manager.financetools;

import data.team.Team;
import process.repositery.TeamRepositery;

public class MonthlyCentralRevenueCalculator {
    private TeamRepositery teamRepositery = TeamRepositery.getInstance();

    public double calculateNationalTvRevenue() {
        double d = 0.0;
        double d2 = 0.0;
        for (Team team : this.teamRepositery.getAllTeams()) {
            d += team.getPopularity();
            d2 += team.getTeamPerformance().getPerformanceRating();
        }
        double d3 = d / (double)this.teamRepositery.getAllTeams().size();
        double d4 = d2 / (double)this.teamRepositery.getAllTeams().size();
        return 0.18 * (double)this.teamRepositery.getAllTeams().size() + d3 * 0.03 + d4 * 2.0;
    }

    public double calculateNationalSponsoringRevenue() {
        double d = 0.0;
        int n = 0;
        for (Team team : this.teamRepositery.getAllTeams()) {
            d += team.getPopularity();
            if (team.getStarPlayer() == null) continue;
            ++n;
        }
        double d2 = d / (double)this.teamRepositery.getAllTeams().size();
        return 0.1 * (double)this.teamRepositery.getAllTeams().size() + d2 * 0.02 + (double)n * 0.08;
    }

    public double calculateNationalMerchandisingRevenue() {
        double d = 0.0;
        double d2 = 0.0;
        for (Team team : this.teamRepositery.getAllTeams()) {
            d += team.getPopularity();
            d2 += team.getTeamFinance().getPayroll();
        }
        double d3 = d / (double)this.teamRepositery.getAllTeams().size();
        double d4 = d2 / (double)this.teamRepositery.getAllTeams().size();
        return 0.06 * (double)this.teamRepositery.getAllTeams().size() + d3 * 0.015 + d4 * 0.02;
    }
}

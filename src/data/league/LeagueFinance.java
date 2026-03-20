/*
 * Decompiled with CFR 0.152.
 */
package data.league;

import data.finance.budget.Budget;

public class LeagueFinance {
    private Budget budget;
    public static double salaryCap;
    public static double luxuryTaxLine;
    public static double minimumTeamSalary;

    public LeagueFinance(Budget budget, double d, double d2, double d3) {
        this.budget = budget;
        salaryCap = d;
        luxuryTaxLine = d2;
        minimumTeamSalary = d3;
    }

    public Budget getBudget() {
        return this.budget;
    }

    public double getSalaryCap() {
        return salaryCap;
    }

    public double getLuxuryTaxLine() {
        return luxuryTaxLine;
    }

    public double getMinimumTeamSalary() {
        return minimumTeamSalary;
    }
}

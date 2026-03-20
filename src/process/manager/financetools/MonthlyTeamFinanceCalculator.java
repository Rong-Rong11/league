/*
 * Decompiled with CFR 0.152.
 */
package process.manager.financetools;

import data.finance.budget.Budget;
import data.finance.budget.Expense;
import data.finance.budget.Income;
import data.team.Team;
import data.team.finance.marketsize.MarketSize;
import process.utilitary.FinanceUtilitary;
import process.visitor.marketsize.CalculateMonthlyTeamFinanceVisitor;

public class MonthlyTeamFinanceCalculator {
    public void applyMonthlyFinance(Team team, int n) {
        Budget budget = team.getTeamFinance().getBudget();
        double d = this.getMarketMultiplier(team.getTeamFinance().getMarketSize());
        double d2 = team.getPopularity() / 100.0;
        double d3 = team.getStarPlayer() != null ? 1.1 : 1.0;
        double d4 = 0.9 + team.getTeamPerformance().getPerformanceRating() * 0.2;
        double d5 = 1.2 * d * d2 * d3;
        double d6 = 0.75 * d * d2 * d3;
        double d7 = 0.25 * d * d4;
        double d8 = team.getTeamFinance().getPayroll() / 12.0;
        double d9 = this.calculateStadiumMaintenance(team, d);
        double d10 = this.calculateStaffCost(team, d);
        double d11 = 0.18 * d;
        FinanceUtilitary.addIncome(budget, new Income("local sponsoring", d5), n);
        FinanceUtilitary.addIncome(budget, new Income("local merchandising", d6), n);
        FinanceUtilitary.addIncome(budget, new Income("others", d7), n);
        FinanceUtilitary.addExpense(budget, new Expense("expense player salary", d8), n);
        FinanceUtilitary.addExpense(budget, new Expense("expense stadium cost", d9), n);
        FinanceUtilitary.addExpense(budget, new Expense("expense stAff cost", d10), n);
        FinanceUtilitary.addExpense(budget, new Expense("expense administrative cost", d11), n);
        FinanceUtilitary.updateBudget(budget);
    }

    private double calculateStadiumMaintenance(Team team, double d) {
        double d2 = (double)team.getStadium().getCapacity() / 20000.0;
        return 0.22 * d * d2;
    }

    private double calculateStaffCost(Team team, double d) {
        int n = team.getPlayers().size();
        double d2 = 0.8 + team.getPopularity() / 500.0;
        return (0.015 * (double)n + 0.1) * d * d2;
    }

    private double getMarketMultiplier(MarketSize marketSize) {
        return marketSize.accept(new CalculateMonthlyTeamFinanceVisitor());
    }
}

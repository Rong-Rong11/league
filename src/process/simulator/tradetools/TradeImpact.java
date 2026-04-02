/*
 * Decompiled with CFR 0.152.
 */
package process.simulator.tradetools;

import data.finance.budget.expense.Expense;
import data.finance.budget.expense.ExpenseType;
import data.team.Team;
import process.utility.FinanceUtilitary;

public class TradeImpact {
    public void applyFinanceImpact(Team team, double d, int n) {
        FinanceUtilitary.updateTeamPayroll(team);
        if (team.getTeamFinance().getCurrentPayroll() > d) {
            double d2 = FinanceUtilitary.luxuryTaxPenalty(team.getTeamFinance().getCurrentPayroll(), d);
            FinanceUtilitary.addExpense(team.getTeamFinance().getBudget(), new Expense(ExpenseType.LUXURY_TAX_PAID, d2),
                    n);
        }
    }
}

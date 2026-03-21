/*
 * Decompiled with CFR 0.152.
 */
package process.simulator.tradetools;

import data.finance.budget.Expense;
import data.team.Team;
import process.utilitary.FinanceUtilitary;

public class TradeImpact {
    public void applyFinanceImpact(Team team, double d, int n) {
        FinanceUtilitary.updateTeamPayroll(team);
        if (team.getTeamFinance().getPayroll() > d) {
            double d2 = FinanceUtilitary.luxuryTaxPenalty(team.getTeamFinance().getPayroll(), d);
            FinanceUtilitary.addExpense(team.getTeamFinance().getBudget(), new Expense("luxurytaxpaid", d2), n);
        }
    }
}

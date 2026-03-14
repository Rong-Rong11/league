package process.simulator.tradetools;

import config.FinanceConfiguration;
import data.finance.budget.Expense;
import data.team.Team;
import process.utilitary.FinanceUtilitary;

public class TradeImpact {
	public TradeImpact() {

	}

	public void applyFinanceImpact(Team team, double luxuryTaxLine, int month) {
		FinanceUtilitary.updateTeamPayroll(team);
		if (team.getTeamFinance().getPayroll() > luxuryTaxLine) {
			double penalty = FinanceUtilitary.luxuryTaxPenalty(team.getTeamFinance().getPayroll(), luxuryTaxLine);
			FinanceUtilitary.addExpense(team.getTeamFinance().getBudget(),
					new Expense(FinanceConfiguration.EXPENSE_TYPE_LUXURY_TAX_PAID, penalty), month);
		}
	}
}

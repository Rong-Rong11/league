/*
* Decompiled with CFR 0.152.
*/
package process.service.trade.execution;

import java.util.ArrayList;

import data.finance.budget.Budget;
import data.player.Player;
import data.team.Team;
import data.team.finance.financialpolicy.FinancialPolicy;
import process.utility.FinanceUtility;
import process.visitor.financialprofil.RiskBudgetVisitor;
import process.visitor.financialprofil.ValidateTradeVisitor;

public class TradeValidator {
	public boolean validateTrade(Team teamA, Team teamB, ArrayList<Player> teamAIncoming,
			ArrayList<Player> teamBIncoming, double salaryCap) {
		double teamBIncomingPayroll;
		double teamAOutgoingPayroll = teamA.getTeamFinance().getCurrentPayroll();
		double teamAIncomingPayroll = FinanceUtility.calculatePayroll(teamAIncoming);
		double teamBOutgoingPayroll = teamB.getTeamFinance().getCurrentPayroll();
		if (!TradeValidator.respectPayroll(teamB, teamBOutgoingPayroll,
				teamBIncomingPayroll = FinanceUtility.calculatePayroll(teamBIncoming), salaryCap)) {
			return false;
		}
		if (!TradeValidator.respectPayroll(teamA, teamAOutgoingPayroll, teamAIncomingPayroll, salaryCap)) {
			return false;
		}
		return !TradeValidator.riskBudget(teamA) && !TradeValidator.riskBudget(teamB);
	}

	public static boolean respectPayroll(Team team, double outgoingPayroll, double incomingPayroll, double salaryCap) {
		FinancialPolicy financialProfil = team.getTeamFinance().getFinancialProfil();
		if (incomingPayroll < salaryCap) {
			return true;
		}
		if (incomingPayroll > outgoingPayroll * 1.25) {
			return false;
		}
		ValidateTradeVisitor validateTradeVisitor = new ValidateTradeVisitor(
				incomingPayroll,
				salaryCap,
				team.getTeamFinance().getMarketSize());
		return financialProfil.accept(validateTradeVisitor);
	}

	private static boolean riskBudget(Team team) {
		FinancialPolicy financialProfil = team.getTeamFinance().getFinancialProfil();
		Budget budget = team.getTeamFinance().getBudget();
		return financialProfil.accept(new RiskBudgetVisitor(budget));
	}
}

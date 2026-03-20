/*
 * Decompiled with CFR 0.152.
 */
package process.simulator.tradetools;

import data.finance.budget.Budget;
import data.player.Player;
import data.team.Team;
import data.team.finance.financialprofil.FinancialProfil;
import java.util.ArrayList;
import process.utilitary.FinanceUtilitary;
import process.visitor.financialprofil.RiskBudgetVisitor;
import process.visitor.financialprofil.ValidateTradeVisitor;

public class TradeValidator {
    public boolean validateTrade(Team teamA, Team teamB, ArrayList<Player> teamAIncoming, ArrayList<Player> teamBIncoming, double salaryCap) {
        double teamBIncomingPayroll;
        double teamAOutgoingPayroll = teamA.getTeamFinance().getPayroll();
        double teamAIncomingPayroll = FinanceUtilitary.calculatePayroll(teamAIncoming);
        double teamBOutgoingPayroll = teamB.getTeamFinance().getPayroll();
        if (!TradeValidator.respectPayroll(teamB, teamBOutgoingPayroll, teamBIncomingPayroll = FinanceUtilitary.calculatePayroll(teamBIncoming), salaryCap)) {
            return false;
        }
        if (!TradeValidator.respectPayroll(teamA, teamAOutgoingPayroll, teamAIncomingPayroll, salaryCap)) {
            return false;
        }
        return !TradeValidator.riskBudget(teamA) && !TradeValidator.riskBudget(teamB);
    }

    public static boolean respectPayroll(Team team, double outgoingPayroll, double incomingPayroll, double salaryCap) {
        FinancialProfil financialProfil = team.getTeamFinance().getFinancialProfil();
        if (incomingPayroll < salaryCap) {
            return true;
        }
        if (incomingPayroll > outgoingPayroll * 1.25) {
            return false;
        }
        ValidateTradeVisitor validateTradeVisitor = new ValidateTradeVisitor(incomingPayroll, salaryCap);
        return financialProfil.accept(validateTradeVisitor);
    }

    private static boolean riskBudget(Team team) {
        FinancialProfil financialProfil = team.getTeamFinance().getFinancialProfil();
        Budget budget = team.getTeamFinance().getBudget();
        return financialProfil.accept(new RiskBudgetVisitor(budget)) ; 
    }
}

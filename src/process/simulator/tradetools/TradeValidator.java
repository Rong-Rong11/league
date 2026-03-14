package process.simulator.tradetools;

import java.util.ArrayList;

import config.FinancialPolicy;
import data.finance.budget.Budget;
import data.player.Player;
import data.team.Team;
import data.team.finance.financialprofil.FinancialProfil;
import process.utilitary.FinanceUtilitary;

public class TradeValidator {
	public TradeValidator() {

	}

	public boolean validateTrade(Team teamA, Team teamB, ArrayList<Player> teamAIncoming,
			ArrayList<Player> teamBIncoming, double salaryCap) {
		double teamAOutgoingPayroll = teamA.getTeamFinance().getPayroll();
		double teamAIncomingPayroll = FinanceUtilitary.calculatePayroll(teamAIncoming);

		double teamBOutgoingPayroll = teamB.getTeamFinance().getPayroll();
		double teamBIncomingPayroll = FinanceUtilitary.calculatePayroll(teamBIncoming);

		if (!respectPayroll(teamB, teamBOutgoingPayroll, teamBIncomingPayroll, salaryCap)) {
			return false;
		}

		if (!respectPayroll(teamA, teamAOutgoingPayroll, teamAIncomingPayroll, salaryCap)) {
			return false;
		}
		if (riskBudget(teamA) || riskBudget(teamB)) {
			return false;
		}
		return true;
	}

	public static boolean respectPayroll(Team team, double outgoingPayroll, double incomingPayroll, double salaryCap) {
		FinancialProfil financialProfil = team.getTeamFinance().getFinancialProfil();
		if (incomingPayroll < salaryCap) {
			return true;
		}

		if (incomingPayroll > outgoingPayroll * 1.25) {
			return false;
		}

		if (financialProfil.getName().equals(FinancialPolicy.FINANCE_PROFIL_AMBITIOUS)) {
			return respectAmbitiousPayroll(incomingPayroll, salaryCap);
		}
		if (financialProfil.getName().equals(FinancialPolicy.FINANCE_PROFIL_ECONOMIC)) {
			return respectEconomicPayroll(incomingPayroll, salaryCap);
		}
		return respectBalancedPayroll(incomingPayroll, salaryCap);
	}

	public static boolean respectEconomicPayroll(double payroll, double salaryCap) {
		return payroll <= salaryCap * FinancialPolicy.SALARY_CAP_RATE_ECONOMIC;
	}

	private static boolean respectAmbitiousPayroll(double payroll, double salaryCap) {
		return payroll <= salaryCap * FinancialPolicy.SALARY_CAP_RATE_AMBITIOUS;
	}

	private static boolean respectBalancedPayroll(double payroll, double salaryCap) {
		return payroll <= salaryCap * FinancialPolicy.SALARY_CAP_RATE_BALANCED;
	}

	private static boolean riskBudget(Team team) {
		FinancialProfil financialProfil = team.getTeamFinance().getFinancialProfil();
		Budget budget = team.getTeamFinance().getBudget();
		if (financialProfil.getName().equals(FinancialPolicy.FINANCE_PROFIL_AMBITIOUS)) {
			return budget.getRemainingAmount() < budget.getInitialAmount() * 0.6;
		}
		if (financialProfil.getName().equals(FinancialPolicy.FINANCE_PROFIL_BALANCED)) {
			return budget.getRemainingAmount() < budget.getInitialAmount() * 0.8;
		}
		return budget.getRemainingAmount() < budget.getInitialAmount() * 0.95;

	}
}

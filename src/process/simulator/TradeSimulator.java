package process.simulator;

import java.util.ArrayList;
import java.util.HashMap;

import config.FinanceConfiguration;
import config.FinancialPolicy;
import data.finance.budget.Budget;
import data.finance.budget.Expense;
import data.league.LeagueFinance;
import data.player.Player;
import data.team.Team;
import data.team.finance.financialprofil.FinancialProfil;
import process.utilitary.FinanceUtilitary;

public class TradeSimulator {

	public static boolean validateTrade(Team teamA, Team teamB, ArrayList<Player> teamAIncoming,
			ArrayList<Player> teamBIncoming, int month) {
		double salaryCap = LeagueFinance.salaryCap;
		double luxuryTaxLine = LeagueFinance.luxuryTaxLine;
		if (!validateTrade(teamA, teamB, teamAIncoming, teamBIncoming, salaryCap)) {
			return false;
		}
		applyTrade(teamA, teamAIncoming);
		applyTrade(teamB, teamBIncoming);
		applyFinanceImpact(teamA, luxuryTaxLine, month);
		applyFinanceImpact(teamB, luxuryTaxLine, month);
		return true;
	}

	private static boolean respectPayroll(Team team, double outgoingPayroll, double incomingPayroll, double salaryCap) {
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

	public static boolean respectAmbitiousPayroll(double payroll, double salaryCap) {
		return payroll <= salaryCap * FinancialPolicy.SALARY_CAP_RATE_AMBITIOUS;
	}

	public static boolean respectBalancedPayroll(double payroll, double salaryCap) {
		return payroll <= salaryCap * FinancialPolicy.SALARY_CAP_RATE_BALANCED;
	}

	private static boolean validateTrade(Team teamA, Team teamB, ArrayList<Player> teamAIncoming,
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

	private static void applyTrade(Team team, ArrayList<Player> teamIncoming) {
		HashMap<String, Player> oldTeam = team.getPlayers();
		HashMap<String, Player> updatedTeam = new HashMap<String, Player>();

		for (Player player : teamIncoming) {
			if (!oldTeam.containsKey(player.getName())) {
				player.setTransfered(true);
			}
			updatedTeam.put(player.getName(), player);
		}
		team.setPlayers(updatedTeam);
		updateStarPlayer(team, teamIncoming);
		team.getTeamFinance().incrementTransferMade();
	}

	private static void updateStarPlayer(Team team, ArrayList<Player> teamIncoming) {
		for (Player player : teamIncoming) {
			if (player.isStar()) {
				team.setStarPlayer(player);
				break;
			}
		}
		team.setStarPlayer(null);
	}

	private static void applyFinanceImpact(Team team, double luxuryTaxLine, int month) {
		FinanceUtilitary.updateTeamPayroll(team);
		if (team.getTeamFinance().getPayroll() > luxuryTaxLine) {
			double penalty = FinanceUtilitary.luxuryTaxPenalty(team.getTeamFinance().getPayroll(), luxuryTaxLine);
			FinanceUtilitary.addExpense(team.getTeamFinance().getBudget(),
					new Expense(FinanceConfiguration.EXPENSE_TYPE_LUXURY_TAX_PAID, penalty), month);
		}
	}

}

package process.service.trade.execution;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.finance.budget.Budget;
import data.player.Player;
import data.team.Team;
import data.team.finance.financialpolicy.FinancialPolicy;
import log.LoggerUtility;
import process.utility.FinanceUtility;
import process.visitor.financialpolicy.RiskBudgetVisitor;
import process.visitor.financialpolicy.ValidateTradeVisitor;

public class TradeValidator {
	private static final Logger logger = LoggerUtility.getLogger(TradeValidator.class, "text");

	public boolean validateTrade(Team teamA, Team teamB, ArrayList<Player> teamAIncoming,
			ArrayList<Player> teamBIncoming, double salaryCap) {
		if (teamA == null || teamB == null || teamAIncoming == null || teamBIncoming == null) {
			logger.warn("Trade validation failed because team or incoming players list is null");
			return false;
		}

		logger.trace("Validating trade between " + teamA.getName() + " and " + teamB.getName());

		double teamAOutgoingPayroll = teamA.getTeamFinance().getCurrentPayroll();
		double teamAIncomingPayroll = FinanceUtility.calculatePayroll(teamAIncoming);
		double teamBOutgoingPayroll = teamB.getTeamFinance().getCurrentPayroll();
		double teamBIncomingPayroll = FinanceUtility.calculatePayroll(teamBIncoming);

		if (!TradeValidator.respectPayroll(teamB, teamBOutgoingPayroll, teamBIncomingPayroll, salaryCap)) {
			logger.debug("Trade validation failed because " + teamB.getName() + " does not respect payroll rules");
			return false;
		}

		if (!TradeValidator.respectPayroll(teamA, teamAOutgoingPayroll, teamAIncomingPayroll, salaryCap)) {
			logger.debug("Trade validation failed because " + teamA.getName() + " does not respect payroll rules");
			return false;
		}

		if (TradeValidator.riskBudget(teamA) || TradeValidator.riskBudget(teamB)) {
			logger.debug("Trade validation failed because one team has budget risk");
			return false;
		}

		logger.trace("Trade validation succeeded between " + teamA.getName() + " and " + teamB.getName());
		return true;
	}

	public static boolean respectPayroll(Team team, double outgoingPayroll, double incomingPayroll, double salaryCap) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Payroll validation failed because team or team finance is null");
			return false;
		}

		FinancialPolicy financialProfil = team.getTeamFinance().getBehavior().getFinancialProfil();

		if (incomingPayroll < salaryCap) {
			logger.trace("Payroll accepted for " + team.getName() + " because incoming payroll is below salary cap");
			return true;
		}

		if (incomingPayroll > outgoingPayroll * 1.25) {
			logger.debug("Payroll rejected for " + team.getName() + " because incoming payroll exceeds allowed ratio");
			return false;
		}

		ValidateTradeVisitor validateTradeVisitor = new ValidateTradeVisitor(
				incomingPayroll,
				salaryCap,
				team.getTeamFinance().getStructure().getMarketSize());

		boolean accepted = financialProfil.accept(validateTradeVisitor);

		logger.trace("Payroll validation for " + team.getName()
				+ " | outgoing: " + outgoingPayroll
				+ ", incoming: " + incomingPayroll
				+ ", salary cap: " + salaryCap
				+ ", accepted: " + accepted);

		return accepted;
	}

	private static boolean riskBudget(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Returning true budget risk because team or team finance is null");
			return true;
		}

		FinancialPolicy financialProfil = team.getTeamFinance().getBehavior().getFinancialProfil();
		Budget budget = team.getTeamFinance().getBudget();

		boolean risk = financialProfil.accept(new RiskBudgetVisitor(budget));

		logger.trace("Budget risk for " + team.getName() + ": " + risk);

		return risk;
	}
}

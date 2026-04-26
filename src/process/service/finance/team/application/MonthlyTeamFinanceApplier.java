package process.service.finance.team.application;

import org.apache.log4j.Logger;

import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.finance.budget.expense.ExpenseType;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.team.Team;
import data.team.finance.MonthlyTeamExpense;
import data.team.finance.MonthlyTeamRevenue;
import data.team.finance.TeamFinance;
import log.LoggerUtility;
import process.utility.FinanceUtility;

public class MonthlyTeamFinanceApplier {
	private static final Logger logger = LoggerUtility.getLogger(MonthlyTeamFinanceApplier.class, "text");

	public void applyLocalRevenue(Budget budget, MonthlyTeamRevenue revenue, int month) {
		if (budget == null || revenue == null) {
			logger.warn("Skipping local revenue application because budget or revenue is null");
			return;
		}
		logger.debug("Applying local team revenue for month " + month);
		logger.debug("Local revenue values: sponsoring="
				+ revenue.getLocalSponsoring()
				+ ", merchandising="
				+ revenue.getLocalMerchandising()
				+ ", other="
				+ revenue.getOtherRevenue());
		logger.trace("Adding local sponsoring income");
		FinanceUtility.addIncome(budget,
				new Income(IncomeType.LOCAL_SPONSORING, revenue.getLocalSponsoring()), month);
		logger.trace("Adding local merchandising income");
		FinanceUtility.addIncome(budget,
				new Income(IncomeType.LOCAL_MERCHANDISING, revenue.getLocalMerchandising()),
				month);
		logger.trace("Adding other local income");
		FinanceUtility.addIncome(budget, new Income(IncomeType.OTHER, revenue.getOtherRevenue()),
				month);
	}

	public void applyFixedCosts(Team team, Budget budget, TeamFinance teamFinance, MonthlyTeamExpense expense,
			int month) {
		if (team == null || budget == null || teamFinance == null || expense == null) {
			logger.warn("Skipping fixed costs application because team, budget, team finance or expense is null");
			return;
		}
		logger.debug("Applying fixed costs for team " + team.getName() + " month " + month);
		logger.debug("Fixed cost values for "
				+ team.getName()
				+ ": payroll="
				+ expense.getMonthlyPayroll()
				+ ", luxuryTax="
				+ expense.getMonthlyLuxuryTax()
				+ ", stadiumMaintenance="
				+ expense.getStadiumMaintenance()
				+ ", staff="
				+ expense.getStaffCost()
				+ ", administrative="
				+ expense.getAdministrativeCost());
		logger.trace("Adding player salary expense for " + team.getName());
		FinanceUtility.addExpense(budget,
				new Expense(ExpenseType.PLAYER_SALARY, expense.getMonthlyPayroll()), month);
		logger.trace("Adding luxury tax expense for " + team.getName());
		FinanceUtility.addExpense(budget,
				new Expense(ExpenseType.LUXURY_TAX_PAID, expense.getMonthlyLuxuryTax()), month);
		logger.trace("Adding stadium maintenance expense for " + team.getName());
		FinanceUtility.addExpense(budget,
				new Expense(ExpenseType.MAINTENANCE_STADIUM_COST,
						expense.getStadiumMaintenance()),
				month);
		logger.trace("Adding staff expense for " + team.getName());
		FinanceUtility.addExpense(budget,
				new Expense(ExpenseType.STAFF_COST, expense.getStaffCost()),
				month);
		logger.trace("Adding administrative expense for " + team.getName());
		FinanceUtility.addExpense(budget,
				new Expense(ExpenseType.ADMINISTRATIVE_COST, expense.getAdministrativeCost()),
				month);

		teamFinance.setLuxuryTaxPaid(teamFinance.getLuxuryTaxPaid() + expense.getMonthlyLuxuryTax());
		logger.debug("Updated luxury tax paid for " + team.getName() + " to " + teamFinance.getLuxuryTaxPaid());
		FinanceUtility.updateBudget(budget);
		FinanceUtility.updateTeamValue(team);
		logger.debug("Updated budget and team value for " + team.getName());
	}
}

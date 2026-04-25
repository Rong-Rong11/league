package process.service.finance.team;

import org.apache.log4j.Logger;

import data.finance.budget.Budget;
import data.league.League;
import data.team.Team;
import data.team.finance.MonthlyTeamExpense;
import data.team.finance.MonthlyTeamRevenue;
import data.team.finance.TeamFinance;
import log.LoggerUtility;
import process.service.finance.team.application.MonthlyTeamFinanceApplier;
import process.service.finance.team.calculation.MonthlyTeamExpenseCalculator;
import process.service.finance.team.calculation.MonthlyTeamRevenueCalculator;
import process.service.finance.team.calculation.TeamFinanceRateCalculator;
import process.service.finance.team.provider.MonthlyTeamFinanceMultiplierProvider;

public abstract class AbstractMonthlyTeamFinanceCalculator implements MonthlyTeamFinanceMultiplierProvider {
	private static final Logger logger = LoggerUtility.getLogger(AbstractMonthlyTeamFinanceCalculator.class, "text");

	private MonthlyTeamRevenueCalculator revenueCalculator;
	private MonthlyTeamExpenseCalculator expenseCalculator;
	private MonthlyTeamFinanceApplier financeApplier = new MonthlyTeamFinanceApplier();

	public AbstractMonthlyTeamFinanceCalculator(League league) {
		TeamFinanceRateCalculator rateCalculator = new TeamFinanceRateCalculator(league);
		this.revenueCalculator = new MonthlyTeamRevenueCalculator(rateCalculator, this);
		this.expenseCalculator = new MonthlyTeamExpenseCalculator(league, rateCalculator);
	}

	public void applyMonthlyFinance(Team team, int month) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Skipping monthly finance application because team or team finance is null");
			return;
		}
		logger.debug("Applying monthly finance for team " + team.getName() + " month " + month);
		Budget budget = team.getTeamFinance().getBudget();
		TeamFinance teamFinance = team.getTeamFinance();

		logger.trace("Calculating monthly revenue for " + team.getName());
		MonthlyTeamRevenue revenue = revenueCalculator.calculateRevenue(team, month);
		logger.trace("Calculating monthly expense for " + team.getName());
		MonthlyTeamExpense expense = expenseCalculator.calculateExpense(team, month, false);

		logger.trace("Applying local revenue for " + team.getName());
		financeApplier.applyLocalRevenue(budget, revenue, month);
		logger.trace("Applying fixed costs for " + team.getName());
		financeApplier.applyFixedCosts(team, budget, teamFinance, expense, month);
		logger.debug("Monthly finance applied for team " + team.getName() + " month " + month);
	}

	public void applyMonthlyFixedCosts(Team team, int month) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Skipping monthly fixed costs application because team or team finance is null");
			return;
		}
		logger.debug("Applying monthly fixed costs for team " + team.getName() + " month " + month);
		Budget budget = team.getTeamFinance().getBudget();
		TeamFinance teamFinance = team.getTeamFinance();
		logger.trace("Calculating monthly fixed costs for " + team.getName());
		MonthlyTeamExpense expense = expenseCalculator.calculateExpense(team, month, true);

		financeApplier.applyFixedCosts(team, budget, teamFinance, expense, month);
		logger.debug("Monthly fixed costs applied for team " + team.getName() + " month " + month);
	}
}

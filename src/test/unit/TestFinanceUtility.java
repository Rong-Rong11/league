package test.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import config.FinanceConfiguration;
import data.finance.GameStat;
import data.finance.budget.Budget;
import data.finance.budget.FinanceSeasonMoment;
import data.finance.budget.expense.Expense;
import data.finance.budget.expense.ExpenseType;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import process.utility.FinanceUtility;
import test.support.TestSupport;

public class TestFinanceUtility {

	private League league;
	private Team homeTeam;
	private Team awayTeam;

	@Before
	public void setUp() {
		league = TestSupport.buildLeagueWithFinance();
		ArrayList<Team> teams = TestSupport.firstTeams(league, 2);
		homeTeam = teams.get(0);
		awayTeam = teams.get(1);
	}

	@Test
	public void shouldInitializeBudgetForAllFinancialMonths() {
		Budget budget = new Budget(100.0);

		FinanceUtility.initiateBudget(budget);

		assertEquals(FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS, budget.getMonthlyIncomes().size());
		assertEquals(FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS, budget.getMonthlyExpenses().size());
		for (int month = 0; month < FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS; month++) {
			assertNotNull(budget.getIncomesForMonth(month));
			assertNotNull(budget.getExpensesForMonth(month));
			assertTrue(budget.getIncomesForMonth(month).isEmpty());
			assertTrue(budget.getExpensesForMonth(month).isEmpty());
		}
	}

	@Test
	public void shouldMergeIncomeOfSameTypeInMonth() {
		Budget budget = new Budget(50.0);
		FinanceUtility.initiateBudget(budget);

		FinanceUtility.addIncome(budget, new Income(IncomeType.LOCAL_SPONSORING, 3.5), 2);
		FinanceUtility.addIncome(budget, new Income(IncomeType.LOCAL_SPONSORING, 1.5), 2);

		assertEquals(1, budget.getIncomesForMonth(2).size());
		assertEquals(5.0, budget.getIncomesForMonth(2).get(IncomeType.LOCAL_SPONSORING.name()).getAmount(), 0.0001);
	}

	@Test
	public void shouldMergeExpenseOfSameTypeWithinMonth() {
		Budget budget = new Budget(50.0);
		FinanceUtility.initiateBudget(budget);

		FinanceUtility.addExpense(budget, new Expense(ExpenseType.STAFF_COST, 2.0), 4);
		FinanceUtility.addExpense(budget, new Expense(ExpenseType.STAFF_COST, 1.25), 4);

		assertEquals(1, budget.getExpensesForMonth(4).size());
		assertEquals(3.25, budget.getExpensesForMonth(4).get(ExpenseType.STAFF_COST.name()).getAmount(), 0.0001);
	}

	@Test
	public void shouldUpdateBudgetAfterIncomeAndExpense() {
		Budget budget = new Budget(100.0);
		FinanceUtility.initiateBudget(budget);
		FinanceUtility.addIncome(budget, new Income(IncomeType.OTHER, 8.0), 0);
		FinanceUtility.addExpense(budget, new Expense(ExpenseType.ADMINISTRATIVE_COST, 3.5), 0);

		FinanceUtility.updateBudget(budget);

		assertEquals(104.5, budget.getRemainingAmount(), 0.0001);
	}

	@Test
	public void shouldAddRegularSeasonGameRevenueToBothTeamsBudgets() {
		TestSupport.resetBudget(homeTeam.getTeamFinance().getBudget());
		TestSupport.resetBudget(awayTeam.getTeamFinance().getBudget());
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		GameStat gameStat = TestSupport.createGameStat(game, 18000, 0.90, 0.72);
		gameStat.getHomeFinance().setTicketRevenue(1.20);
		gameStat.getHomeFinance().setConcessionsRevenue(0.40);
		gameStat.getHomeFinance().setParkingRevenue(0.12);
		gameStat.getHomeFinance().setTvRevenue(0.50);
		gameStat.getHomeFinance().setMerchRevenue(0.22);
		gameStat.getAwayFinance().setTvRevenue(0.18);

		FinanceUtility.addGameRevenue(game, gameStat, 1, FinanceSeasonMoment.REGULAR_SEASON);

		assertEquals(1.20,
				homeTeam.getTeamFinance().getBudget().getIncomesForMonth(1).get(IncomeType.TICKET_OFFICE.name())
						.getAmount(),
				0.0001);
		assertEquals(0.40,
				homeTeam.getTeamFinance().getBudget().getIncomesForMonth(1).get(IncomeType.CONCESSIONS.name())
						.getAmount(),
				0.0001);
		assertEquals(0.18,
				awayTeam.getTeamFinance().getBudget().getIncomesForMonth(1).get(IncomeType.LOCAL_TV.name()).getAmount(),
				0.0001);
	}

	@Test
	public void shouldAddPlayoffGameExpenseUsingPlayoffExpenseTypes() {
		TestSupport.resetBudget(homeTeam.getTeamFinance().getBudget());
		TestSupport.resetBudget(awayTeam.getTeamFinance().getBudget());
		Game game = TestSupport.createInterConferenceGame(homeTeam, awayTeam);
		GameStat gameStat = TestSupport.createGameStat(game, 19000, 0.95, 0.80);
		gameStat.getHomeFinance().setArenaCosts(0.75);
		gameStat.getHomeFinance().setStaffCosts(0.20);
		gameStat.getHomeFinance().setSecurityCosts(0.18);
		gameStat.getHomeFinance().setLogisticsCosts(0.16);
		gameStat.getAwayFinance().setTravelCosts(0.09);

		FinanceUtility.addGameExpense(game, gameStat, 8, FinanceSeasonMoment.PLAYOFF);

		assertEquals(0.75,
				homeTeam.getTeamFinance().getBudget().getExpensesForMonth(8)
						.get(ExpenseType.PLAYOFF_STADIUM_COST.name()).getAmount(),
				0.0001);
		assertEquals(0.16,
				homeTeam.getTeamFinance().getBudget().getExpensesForMonth(8)
						.get(ExpenseType.PLAYOFF_LOGISTIC_COST.name()).getAmount(),
				0.0001);
		assertEquals(0.09,
				awayTeam.getTeamFinance().getBudget().getExpensesForMonth(8)
						.get(ExpenseType.PLAYOFF_TRAVEL_COST.name()).getAmount(),
				0.0001);
	}

	@Test
	public void shouldClampNormalizedTeamValueBetweenZeroAndOne() {
		homeTeam.getTeamFinance().setTeamValue(200.0);
		assertEquals(0.0, FinanceUtility.getNormalizedTeamValue(homeTeam), 0.0001);

		homeTeam.getTeamFinance().setTeamValue(1200.0);
		assertEquals(1.0, FinanceUtility.getNormalizedTeamValue(homeTeam), 0.0001);
	}

	@Test
	public void shouldUpdateTeamValueUsingCurrentFinanceAndPerformance() {
		homeTeam.getTeamFinance().getBudget().setRemainingAmount(300.0);
		homeTeam.setCurrentPopularity(80.0);
		homeTeam.getTeamPerformance().setPerformanceRating(0.75);
		homeTeam.getTeamFinance().setCurrentPayroll(150.0);
		homeTeam.getStadium().setCapacity(22000);

		FinanceUtility.updateTeamValue(homeTeam);

		double expected = 180.0 + 300.0 + (80.0 * 2.0) + (0.75 * 120.0) + (150.0 * 0.35) + ((22000 / 1000.0) * 3.0);
		assertEquals(expected, homeTeam.getTeamFinance().getTeamValue(), 0.0001);
	}
}

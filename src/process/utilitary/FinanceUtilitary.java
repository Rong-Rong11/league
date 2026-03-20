package process.utilitary;

import java.util.ArrayList;
import java.util.HashMap;

import config.FinanceConfiguration;
import data.finance.GameStat;
import data.finance.TeamGameFinance;
import data.finance.budget.Budget;
import data.finance.budget.Expense;
import data.finance.budget.Income;
import data.player.Player;
import data.sport.setup.Game;
import data.team.Team;
import process.repositery.TeamRepositery;

//les sommes en millions
public class FinanceUtilitary {

	public static void initiateBudget(Budget budget) {
		HashMap<Integer, HashMap<String, Income>> monthlyIncomes = budget.getMonthlyIncomes();
		for (int i = 0; i < 10; i++) {
			monthlyIncomes.put(i, new HashMap<String, Income>());
		}
		HashMap<Integer, HashMap<String, Expense>> monthlyExpenses = budget.getMonthlyExpenses();
		for (int i = 0; i < 10; i++) {
			monthlyExpenses.put(i, new HashMap<String, Expense>());
		}
	}

	public static void updateFormerLeaguePayroll() {
		TeamRepositery teamRepositery = TeamRepositery.getInstance();
		for (Team team : teamRepositery.getAllTeams()) {
			updateTeamFormerPayroll(team);
		}
	}

	private static void updateTeamFormerPayroll(Team team) {
		double payroll = 0;
		for (Player player : team.getFormerPlayers().values()) {
			payroll += player.getSalary();
		}
		team.getTeamFinance().setFormerPayroll(payroll);
	}

	public static void updateTeamPayroll(Team team) {
		double payroll = 0;
		for (Player player : team.getCurrentPlayers().values()) {
			payroll += player.getSalary();
		}
		team.getTeamFinance().setCurrentPayroll(payroll);
	}

	public static double getAverageSalary(Team team) {
		int numberOfPlayers = 0;
		double sumOfSalary = 0;
		for (Player player : team.getCurrentPlayers().values()) {
			sumOfSalary += player.getSalary();
			numberOfPlayers++;
		}
		return sumOfSalary / numberOfPlayers;
	}

	public static void updateBudget(Budget budget) {
		double amount = budget.getInitialAmount();
		for (HashMap<String, Income> incomes : budget.getMonthlyIncomes().values()) {
			for (Income income : incomes.values())
				amount += income.getAmount();
		}
		for (HashMap<String, Expense> expenses : budget.getMonthlyExpenses().values()) {
			for (Expense expense : expenses.values()) {
				amount -= expense.getAmount();
			}
		}
		budget.setRemainingAmount(amount);
	}

	public static double calculatePayroll(ArrayList<Player> players) {
		double payroll = 0;
		for (Player player : players) {
			payroll += player.getSalary();
		}
		return payroll;
	}

	public static double luxuryTaxPenalty(double payroll, double luxuryTaxLine) {
		double luxuryTax = FinanceConfiguration.LUXURY_TAX_RATE_BASE;
		if (payroll <= luxuryTaxLine) {
			return 0;
		}
		double excess = payroll - luxuryTaxLine;
		double penaltyRate;
		if (excess <= 5) {
			penaltyRate = luxuryTax;
		} else if (excess <= 10) {
			penaltyRate = luxuryTax * 1.25;
		} else {
			penaltyRate = luxuryTax * 1.5;
		}
		return excess * penaltyRate;
	}

	public static double getTeamLocalRevenueOfMonth(Team team, int month) {
		Budget budget = team.getTeamFinance().getBudget();
		double localRevenue = 0;
		for (Income income : budget.getIncomesForMonth(month).values()) {
			if (income.getName().equals(FinanceConfiguration.INCOME_TYPE_LOCAL_TV) ||
					income.getName().equals(FinanceConfiguration.INCOME_TYPE_LOCAL_MERCHANDISING) ||
					income.getName().equals(FinanceConfiguration.INCOME_TYPE_LOCAL_SPONSORING) ||
					income.getName().equals(FinanceConfiguration.INCOME_TYPE_TICKET_OFFICE)) {
				localRevenue += income.getAmount();
			}
		}
		return localRevenue;
	}

	public static void addGameRevenue(Game game, GameStat gameStat, int month) {
		Budget homeTeamBudget = game.getGameContext().getHomeTeam().getTeamFinance().getBudget();
		Budget awayTeamBudget = game.getGameContext().getAwayTeam().getTeamFinance().getBudget();
		TeamGameFinance homeFinance = gameStat.getHomeFinance();
		TeamGameFinance awayFinance = gameStat.getAwayFinance();

		addIncome(homeTeamBudget,
				new Income(FinanceConfiguration.INCOME_TYPE_TICKET_OFFICE, homeFinance.getTicketRevenue()), month);
		addIncome(homeTeamBudget,
				new Income(FinanceConfiguration.INCOME_TYPE_CONCESSIONS, homeFinance.getConcessionsRevenue()), month);
		addIncome(homeTeamBudget, new Income(FinanceConfiguration.INCOME_TYPE_PARKING, homeFinance.getParkingRevenue()),
				month);
		addIncome(homeTeamBudget, new Income(FinanceConfiguration.INCOME_TYPE_LOCAL_TV, homeFinance.getTvRevenue()),
				month);
		addIncome(homeTeamBudget,
				new Income(FinanceConfiguration.INCOME_TYPE_GAME_LOCAL_MERCHANDISING, homeFinance.getMerchRevenue()),
				month);

		addIncome(awayTeamBudget, new Income(FinanceConfiguration.INCOME_TYPE_LOCAL_TV, awayFinance.getTvRevenue()),
				month);

		updateBudget(homeTeamBudget);
		updateBudget(awayTeamBudget);

	}

	public static void addGameExpense(Game game, GameStat gameStat, int month) {
		Budget homeTeamBudget = game.getGameContext().getHomeTeam().getTeamFinance().getBudget();
		Budget awayTeamBudget = game.getGameContext().getAwayTeam().getTeamFinance().getBudget();
		TeamGameFinance homeFinance = gameStat.getHomeFinance();
		TeamGameFinance awayFinance = gameStat.getAwayFinance();

		addExpense(homeTeamBudget,
				new Expense(FinanceConfiguration.EXPENSE_TYPE_STADIUM_COST, homeFinance.getArenaCosts()), month);
		addExpense(homeTeamBudget, new Expense(FinanceConfiguration.EXPENSE_TYPE_STAFF_COST, homeFinance.getStaffCosts()),
				month);
		addExpense(homeTeamBudget,
				new Expense(FinanceConfiguration.EXPENSE_TYPE_SECURITY_COST, homeFinance.getSecurityCosts()), month);
		addExpense(homeTeamBudget,
				new Expense(FinanceConfiguration.EXPENSE_TYPE_LOGISTIC_COST, homeFinance.getLogisticsCosts()), month);
		addExpense(awayTeamBudget,
				new Expense(FinanceConfiguration.EXPENSE_TYPE_TRAVEL_COST, awayFinance.getTravelCosts()), month);

		updateBudget(homeTeamBudget);
		updateBudget(awayTeamBudget);
	}

	public static void addIncome(Budget budget, Income income, int month) {
		HashMap<String, Income> incomesOfMonth = budget.getIncomesForMonth(month);
		if (incomesOfMonth == null) {
			incomesOfMonth = new HashMap<>();
			budget.getMonthlyIncomes().put(month, incomesOfMonth);
		}

		if (incomesOfMonth.containsKey(income.getName())) {
			Income existingIncome = incomesOfMonth.get(income.getName());
			existingIncome.setAmount(existingIncome.getAmount() + income.getAmount());
		} else {
			incomesOfMonth.put(income.getName(), income);
		}
	}

	public static void addExpense(Budget budget, Expense expense, int month) {
		HashMap<String, Expense> expensesOfMonth = budget.getExpensesForMonth(month);
		if (expensesOfMonth == null) {
			expensesOfMonth = new HashMap<>();
			budget.getMonthlyExpenses().put(month, expensesOfMonth);
		}

		if (expensesOfMonth.containsKey(expense.getName())) {
			Expense existingExpense = expensesOfMonth.get(expense.getName());
			existingExpense.setAmount(existingExpense.getAmount() + expense.getAmount());
		} else {
			expensesOfMonth.put(expense.getName(), expense);
		}
	}

	public static double calculateMerchandisingScore(Team team) {
		double popularity = team.getCurrentPopularity() / 100.0;

		double historicalPrestige = team.getTeamFinance()
				.getEconomicProfil()
				.getHistoricalPrestige();

		double fanLoyalty = team.getTeamFinance()
				.getEconomicProfil()
				.getFanLoyalty();

		double mediaPrestige = team.getTeamFinance()
				.getMediaMarket()
				.getPrestigeModifier();

		return (0.4 * popularity)
				+ (0.3 * historicalPrestige)
				+ (0.2 * fanLoyalty)
				+ (0.1 * mediaPrestige);
	}

}

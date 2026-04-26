package process.utility;

import java.util.ArrayList;
import java.util.HashMap;

import config.FinanceConfiguration;
import data.finance.GameStat;
import data.finance.TeamGameFinance;
import data.finance.budget.Budget;
import data.finance.budget.FinanceScope;
import data.finance.budget.FinanceSeasonMoment;
import data.finance.budget.expense.Expense;
import data.finance.budget.expense.ExpenseType;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.player.Player;
import data.sport.setup.Game;
import data.team.Team;
import process.repository.TeamRepository;
import process.utility.tools.FinanceTypeResolver;

//les sommes en millions
public class FinanceUtility {

	public static void initiateBudget(Budget budget) {
		HashMap<Integer, HashMap<String, Income>> monthlyIncomes = budget.getMonthlyIncomes();
		for (int i = 0; i < FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS; i++) {
			monthlyIncomes.put(i, new HashMap<String, Income>());
		}
		HashMap<Integer, HashMap<String, Expense>> monthlyExpenses = budget.getMonthlyExpenses();
		for (int i = 0; i < FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS; i++) {
			monthlyExpenses.put(i, new HashMap<String, Expense>());
		}
		for (int i = 0; i < FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS; i++) {
			budget.getMonthlyNetHistory().put(i, 0.0);
		}
	}

	public static void updateFormerLeaguePayroll() {
		TeamRepository teamRepositery = TeamRepository.getInstance();
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
		for (int month = 0; month < FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS; month++) {
			HashMap<String, Income> incomes = budget.getMonthlyIncomes().get(month);
			HashMap<String, Expense> expenses = budget.getMonthlyExpenses().get(month);
			double monthNet = totalIncome(incomes) - totalExpense(expenses);
			budget.getMonthlyNetHistory().put(month, monthNet);
			amount += monthNet;
		}
		budget.setRemainingAmount(amount);
	}

	private static double totalIncome(HashMap<String, Income> incomes) {
		if (incomes == null) {
			return 0.0;
		}
		double total = 0.0;
		for (Income income : incomes.values()) {
			total += income.getAmount();
		}
		return total;
	}

	private static double totalExpense(HashMap<String, Expense> expenses) {
		if (expenses == null) {
			return 0.0;
		}
		double total = 0.0;
		for (Expense expense : expenses.values()) {
			total += expense.getAmount();
		}
		return total;
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
			penaltyRate = luxuryTax * 1.5;
		} else if (excess <= 10) {
			penaltyRate = luxuryTax * 2.5;
		} else if (excess <= 20) {
			penaltyRate = luxuryTax * 3.45;
		} else {
			penaltyRate = luxuryTax * 5.25;
		}
		return excess * penaltyRate;
	}

	public static double getTeamTotalLocalRevenueOfMonth(Team team, int month) {
		Budget budget = team.getTeamFinance().getBudget();
		double localRevenue = 0.0;

		for (Income income : budget.getIncomesForMonth(month).values()) {
			if (income.getIncomeType().getScope() == FinanceScope.LOCAL) {
				localRevenue += income.getAmount();
			}
		}

		return localRevenue;
	}

	public static double getTeamIncomeOfMonthForRegularSeason(Team team, int month) {
		Budget budget = team.getTeamFinance().getBudget();
		double total = 0.0;

		for (Income income : budget.getIncomesForMonth(month).values()) {
			if (income.getIncomeType().getSeasonMoment() == FinanceSeasonMoment.REGULAR_SEASON) {
				total += income.getAmount();
			}
		}

		return total;
	}

	public static double getTeamIncomeOfMonthForPlayoff(Team team, int month) {
		Budget budget = team.getTeamFinance().getBudget();
		double total = 0.0;

		for (Income income : budget.getIncomesForMonth(month).values()) {
			if (income.getIncomeType().getSeasonMoment() == FinanceSeasonMoment.PLAYOFF) {
				total += income.getAmount();
			}
		}

		return total;
	}

	public static double getTeamIncomeOfMonthForBoth(Team team, int month) {
		Budget budget = team.getTeamFinance().getBudget();
		double total = 0.0;

		for (Income income : budget.getIncomesForMonth(month).values()) {
			if (income.getIncomeType().getSeasonMoment() == FinanceSeasonMoment.BOTH) {
				total += income.getAmount();
			}
		}

		return total;
	}

	public static double getTeamExpenseOfMonthForRegularSeason(Team team, int month) {
		Budget budget = team.getTeamFinance().getBudget();
		double total = 0.0;

		for (Expense expense : budget.getExpensesForMonth(month).values()) {
			if (expense.getExpenseType().getSeasonMoment() == FinanceSeasonMoment.REGULAR_SEASON) {
				total += expense.getAmount();
			}
		}

		return total;
	}

	public static double getTeamExpenseOfMonthForPlayoff(Team team, int month) {
		Budget budget = team.getTeamFinance().getBudget();
		double total = 0.0;

		for (Expense expense : budget.getExpensesForMonth(month).values()) {
			if (expense.getExpenseType().getSeasonMoment() == FinanceSeasonMoment.PLAYOFF) {
				total += expense.getAmount();
			}
		}

		return total;
	}

	public static double getTeamExpenseOfMonthForBoth(Team team, int month) {
		Budget budget = team.getTeamFinance().getBudget();
		double total = 0.0;

		for (Expense expense : budget.getExpensesForMonth(month).values()) {
			if (expense.getExpenseType().getSeasonMoment() == FinanceSeasonMoment.BOTH) {
				total += expense.getAmount();
			}
		}

		return total;
	}

	public static void addGameRevenue(Game game, GameStat gameStat, int month, FinanceSeasonMoment seasonMoment) {
		FinanceTypeResolver financeTypeResolver = new FinanceTypeResolver(seasonMoment);
		Budget homeTeamBudget = game.getGameContext().getHomeTeam().getTeamFinance().getBudget();
		Budget awayTeamBudget = game.getGameContext().getAwayTeam().getTeamFinance().getBudget();
		TeamGameFinance homeFinance = gameStat.getHomeFinance();
		TeamGameFinance awayFinance = gameStat.getAwayFinance();
		IncomeType homeTicketType = financeTypeResolver.resolveIncomeType(IncomeType.TICKET_OFFICE);
		IncomeType homeConcessionsType = financeTypeResolver.resolveIncomeType(IncomeType.CONCESSIONS);
		IncomeType homeParkingType = financeTypeResolver.resolveIncomeType(IncomeType.PARKING);
		IncomeType homeTvType = financeTypeResolver.resolveIncomeType(IncomeType.LOCAL_TV);
		IncomeType homeMerchandisingType = financeTypeResolver.resolveIncomeType(IncomeType.GAME_LOCAL_MERCHANDISING);
		IncomeType awayTvType = financeTypeResolver.resolveIncomeType(IncomeType.LOCAL_TV);

		addIncome(homeTeamBudget,
				new Income(homeTicketType, homeFinance.getTicketRevenue()), month);
		addIncome(homeTeamBudget,
				new Income(homeConcessionsType, homeFinance.getConcessionsRevenue()), month);
		addIncome(homeTeamBudget, new Income(homeParkingType, homeFinance.getParkingRevenue()),
				month);
		addIncome(homeTeamBudget, new Income(homeTvType, homeFinance.getTvRevenue()),
				month);
		addIncome(homeTeamBudget,
				new Income(homeMerchandisingType, homeFinance.getMerchRevenue()),
				month);

		addIncome(awayTeamBudget, new Income(awayTvType, awayFinance.getTvRevenue()),
				month);

		updateBudget(homeTeamBudget);
		updateBudget(awayTeamBudget);

	}

	public static void addGameExpense(Game game, GameStat gameStat, int month, FinanceSeasonMoment seasonMoment) {
		FinanceTypeResolver financeTypeResolver = new FinanceTypeResolver(seasonMoment);
		Budget homeTeamBudget = game.getGameContext().getHomeTeam().getTeamFinance().getBudget();
		Budget awayTeamBudget = game.getGameContext().getAwayTeam().getTeamFinance().getBudget();
		TeamGameFinance homeFinance = gameStat.getHomeFinance();
		TeamGameFinance awayFinance = gameStat.getAwayFinance();
		ExpenseType homeStadiumType = financeTypeResolver.resolveExpenseType(ExpenseType.STADIUM_COST);
		ExpenseType homeStaffType = financeTypeResolver.resolveExpenseType(ExpenseType.STAFF_COST);
		ExpenseType homeSecurityType = financeTypeResolver.resolveExpenseType(ExpenseType.SECURITY_COST);
		ExpenseType homeLogisticType = financeTypeResolver.resolveExpenseType(ExpenseType.LOGISTIC_COST);
		ExpenseType awayTravelType = financeTypeResolver.resolveExpenseType(ExpenseType.TRAVEL_COST);

		addExpense(homeTeamBudget,
				new Expense(homeStadiumType, homeFinance.getArenaCosts()), month);
		addExpense(homeTeamBudget, new Expense(homeStaffType, homeFinance.getStaffCosts()),
				month);
		addExpense(homeTeamBudget,
				new Expense(homeSecurityType, homeFinance.getSecurityCosts()), month);
		addExpense(homeTeamBudget,
				new Expense(homeLogisticType, homeFinance.getLogisticsCosts()), month);
		addExpense(awayTeamBudget,
				new Expense(awayTravelType, awayFinance.getTravelCosts()), month);

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
				.getStructure()
				.getEconomicProfil()
				.getHistoricalPrestige();

		double fanLoyalty = team.getTeamFinance()
				.getStructure()
				.getEconomicProfil()
				.getFanLoyalty();

		double mediaPrestige = team.getTeamFinance()
				.getStructure()
				.getMediaMarket()
				.getPrestigeModifier();

		double valueFactor = getNormalizedTeamValue(team);

		return (0.4 * popularity)
				+ (0.3 * historicalPrestige)
				+ (0.2 * fanLoyalty)
				+ (0.1 * mediaPrestige)
				+ (0.15 * valueFactor);
	}

	public static double getNormalizedTeamValue(Team team) {
		double teamValue = team.getTeamFinance().getTeamValue();
		double minValue = 250.0;
		double maxValue = 900.0;

		if (teamValue <= minValue) {
			return 0.0;
		}

		return Math.max(0.0, Math.min(1.0, (teamValue - minValue) / (maxValue - minValue)));
	}

	public static void updateTeamValue(Team team) {
		double currentBudget = team.getTeamFinance().getBudget().getRemainingAmount();
		double popularityBonus = team.getCurrentPopularity() * 2.0;
		double performanceBonus = team.getTeamPerformance().getPerformanceRating() * 120.0;
		double payrollBonus = Math.min(team.getTeamFinance().getCurrentPayroll(), 220.0) * 0.35;
		double stadiumBonus = (team.getStadium().getCapacity() / 1000.0) * 3.0;

		double updatedValue = 180.0
				+ currentBudget
				+ popularityBonus
				+ performanceBonus
				+ payrollBonus
				+ stadiumBonus;

		team.getTeamFinance().setTeamValue(updatedValue);
	}

}

package gui.panel.financePanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.finance.budget.income.Income;
import data.team.Team;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.MonthNavigatorPanel;
import gui.panel.common.PlayerDisplayUtil;
import process.orchestrator.interfaces.GUIInterface;
import process.utility.FinanceLabelUtility;
import process.utility.FinanceSummaryUtility;

public final class FinanceDataUtil {

	private FinanceDataUtil() {
	}

	public static Team selectedTeam(GUIInterface guiInterface, JComboBox<String> comboBox, int fallbackIndex) {
		if (comboBox.getItemCount() == 0) {
			return null;
		}
		if (comboBox.getSelectedIndex() < 0) {
			comboBox.setSelectedIndex(Math.min(fallbackIndex, comboBox.getItemCount() - 1));
		}
		String teamName = (String) comboBox.getSelectedItem();
		if (teamName == null) {
			return null;
		}
		return guiInterface.getTeamByName(teamName);
	}

	public static List<Integer> availableMonths(GUIInterface guiInterface, Budget budget) {
		return FinanceSummaryUtility.availableMonths(budget, lastVisibleMonth(guiInterface));
	}

	public static int selectedMonth(MonthNavigatorPanel navigator) {
		if (navigator == null) {
			return 1;
		}
		return navigator.getSelectedMonth();
	}

	public static void setAvailableMonths(MonthNavigatorPanel navigator, List<Integer> months) {
		if (navigator != null) {
			navigator.setAvailableMonths(months);
		}
	}

	public static int lastVisibleMonth(GUIInterface guiInterface) {
		return Math.min(Math.max(1, guiInterface.getCurrentFinanceMonth()),
				FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS - 1);
	}

	public static Budget teamBudget(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getBudget();
	}

	public static Map<String, Income> teamIncomes(Team team, int month) {
		Budget budget = teamBudget(team);
		if (budget == null) {
			return null;
		}
		return budget.getIncomesForMonth(month);
	}

	public static Map<String, Expense> teamExpenses(Team team, int month) {
		Budget budget = teamBudget(team);
		if (budget == null) {
			return null;
		}
		return budget.getExpensesForMonth(month);
	}

	public static double totalIncome(Map<String, Income> incomes) {
		return FinanceSummaryUtility.totalIncome(incomes);
	}

	public static double totalLocalIncome(Map<String, Income> incomes) {
		return FinanceSummaryUtility.totalLocalIncome(incomes);
	}

	public static double totalExpense(Map<String, Expense> expenses) {
		return FinanceSummaryUtility.totalExpense(expenses);
	}

	public static double monthNet(Budget budget, int month) {
		if (budget == null) {
			return 0.0;
		}
		return budget.getNetForMonth(month);
	}

	public static double leagueMonthNet(GUIInterface guiInterface, int month) {
		if (guiInterface == null) {
			return 0.0;
		}
		return guiInterface.getLeagueNetForMonth(month);
	}

	public static double teamMonthNet(GUIInterface guiInterface, Team team, int month) {
		if (guiInterface == null || team == null) {
			return 0.0;
		}
		return guiInterface.getTeamNetForMonth(team, month);
	}

	public static String monthLabel(int month) {
		return "M" + month;
	}

	public static String formatMoney(double value) {
		return PlayerDisplayUtil.formatSalary(value);
	}

	public static String formatPercent(double value) {
		return PlayerDisplayUtil.formatOneDecimal(value * 100.0) + "%";
	}

	public static String formatTypeName(String value) {
		if (value == null || value.isEmpty()) {
			return "-";
		}
		if ("MARKETING_COST".equals(value)) {
			return "Marketing";
		}
		if ("ADMINISTRATIVE_COST".equals(value)) {
			return "Administratif";
		}
		if ("MEDIA_COST".equals(value)) {
			return "Media";
		}
		if ("OFFICIATING_COST".equals(value)) {
			return "Arbitrage";
		}
		if ("STAFF_COST".equals(value)) {
			return "Staff";
		}
		if ("PLAYER_SALARY".equals(value)) {
			return "Salaires";
		}
		if ("STADIUM_COST".equals(value)) {
			return "Salle";
		}
		if ("MAINTENANCE_STADIUM_COST".equals(value)) {
			return "Maintenance";
		}
		if ("TRAVEL_COST".equals(value)) {
			return "Deplacements";
		}
		if ("SECURITY_COST".equals(value)) {
			return "Securite";
		}
		if ("LOGISTIC_COST".equals(value)) {
			return "Logistique";
		}
		if ("LUXURY_TAX_PAID".equals(value)) {
			return "Taxe de luxe";
		}
		String[] parts = value.toLowerCase().split("_");
		String text = "";
		for (String part : parts) {
			if (!part.isEmpty()) {
				if (!text.isEmpty()) {
					text += " ";
				}
				text += Character.toUpperCase(part.charAt(0)) + part.substring(1);
			}
		}
		return text;
	}

	public static String formatPolicy(Object object) {
		return FinanceLabelUtility.formatPolicy(object);
	}

	public static String formatMarket(Object object) {
		return FinanceLabelUtility.formatMarket(object);
	}

	public static String formatStrategy(Object object) {
		return FinanceLabelUtility.formatStrategy(object);
	}

	public static void setAmountColor(JLabel label, double value) {
		if (label != null) {
			label.setForeground(DashboardPanelUtil.getValueColorForAmount(value));
		}
	}

	public static void setRevenueColor(JLabel label) {
		if (label != null) {
			label.setForeground(DashboardPanelUtil.REVENUE_COLOR);
		}
	}

	public static void setExpenseColor(JLabel label) {
		if (label != null) {
			label.setForeground(DashboardPanelUtil.EXPENSE_COLOR);
		}
	}

	public static void setPolicyColor(JLabel label, String policyName) {
		if (label != null) {
			label.setForeground(DashboardPanelUtil.getFinancialPolicyColor(policyName));
		}
	}

	public static void setStrategyColor(JLabel label, String strategyName) {
		if (label != null) {
			label.setForeground(DashboardPanelUtil.getTransferStrategyColor(strategyName));
		}
	}

	public static void setMarketColor(JLabel label, String marketName) {
		if (label != null) {
			label.setForeground(DashboardPanelUtil.getMarketColor(marketName));
		}
	}
}

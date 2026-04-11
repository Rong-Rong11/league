package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.data.category.DefaultCategoryDataset;

import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.league.League;
import data.league.finance.LeagueFinancialRules;
import data.league.finance.LeagueRedistributionPolicy;
import data.team.Team;
import gui.panel.common.BuildBox;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.MonthNavigatorPanel;
import gui.panel.common.ThemeAware;
import process.orchestrator.GUIInterface;

public class LeagueFinanceViewPanel extends JPanel implements ThemeAware {

	private static final int DASHBOARD_SPACING = 10;
	private static final int RIGHT_COLUMN_WIDTH = 280;

	private boolean updatingMonthSelector;
	private final GUIInterface guiInterface;
	private final MonthNavigatorPanel monthSelector;
	private final JLabel remainingBudgetValueLabel;
	private final JLabel leagueValueValueLabel;
	private final JLabel summaryRevenueValueLabel;
	private final JLabel summaryNetValueLabel;
	private final JLabel snapshotRevenueValueLabel;
	private final JLabel snapshotExpenseValueLabel;
	private final JLabel snapshotNetValueLabel;
	private final JLabel salaryCapValueLabel;
	private final JLabel luxuryTaxValueLabel;
	private final JLabel minimumSalaryValueLabel;
	private final JLabel retentionValueLabel;
	private final JLabel redistributionValueLabel;
	private final JLabel equalShareValueLabel;
	private final JLabel weightedShareValueLabel;
	private final JPanel topTeamsPanel;
	private final DefaultCategoryDataset historyDataset;
	private final DefaultCategoryDataset expenseDataset;

	public LeagueFinanceViewPanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		monthSelector = new MonthNavigatorPanel();
		remainingBudgetValueLabel = FinancePanelUtil.createMetricValueLabel();
		leagueValueValueLabel = FinancePanelUtil.createMetricValueLabel();
		summaryRevenueValueLabel = FinancePanelUtil.createMetricValueLabel();
		summaryNetValueLabel = FinancePanelUtil.createMetricValueLabel();
		snapshotRevenueValueLabel = FinancePanelUtil.createMetricValueLabel();
		snapshotExpenseValueLabel = FinancePanelUtil.createMetricValueLabel();
		snapshotNetValueLabel = FinancePanelUtil.createMetricValueLabel();
		salaryCapValueLabel = FinancePanelUtil.createMetricValueLabel();
		luxuryTaxValueLabel = FinancePanelUtil.createMetricValueLabel();
		minimumSalaryValueLabel = FinancePanelUtil.createMetricValueLabel();
		retentionValueLabel = FinancePanelUtil.createBodyValueLabel();
		redistributionValueLabel = FinancePanelUtil.createBodyValueLabel();
		equalShareValueLabel = FinancePanelUtil.createBodyValueLabel();
		weightedShareValueLabel = FinancePanelUtil.createBodyValueLabel();
		topTeamsPanel = FinancePanelUtil.createMetricListPanel();
		historyDataset = new DefaultCategoryDataset();
		expenseDataset = new DefaultCategoryDataset();

		organize();
		actions();
	}

	private void organize() {
		setLayout(new BorderLayout(0, DASHBOARD_SPACING));
		add(new BuildBox("RESUME LIGUE", "Vue consolidee de la saison", buildSummaryPanel()), BorderLayout.NORTH);
		add(buildBody(), BorderLayout.CENTER);
	}

	private JPanel buildBody() {
		JPanel body = DashboardPanelUtil.createBodyPanel(DASHBOARD_SPACING, DASHBOARD_SPACING);
		body.add(buildCenterColumn(), BorderLayout.CENTER);
		body.add(buildRightColumn(), BorderLayout.EAST);
		return body;
	}

	private JPanel buildCenterColumn() {
		JPanel centerColumn = new JPanel(new BorderLayout(0, DASHBOARD_SPACING));
		centerColumn.setOpaque(false);
		centerColumn.add(new BuildBox("HISTORIQUE LIGUE", "Revenus et depenses par mois",
				FinancePanelUtil.buildLineChartPanel(historyDataset, "Montant (M$)", DashboardPanelUtil.REVENUE_COLOR)),
				BorderLayout.CENTER);
		centerColumn.add(FinancePanelUtil.withPreferredHeight(
				new BuildBox("TOP EQUIPES DU MOIS", "Net du mois selectionne", topTeamsPanel), 250),
				BorderLayout.SOUTH);
		return centerColumn;
	}

	private JPanel buildRightColumn() {
		JPanel column = DashboardPanelUtil.createGridColumn(3, 1, 0, 12, RIGHT_COLUMN_WIDTH);
		column.add(new BuildBox("VUE MENSUELLE", "Resume du mois selectionne", buildMonthlySnapshotPanel()));
		column.add(new BuildBox("REGLES", "Parametres globaux", buildRulesPanel()));
		column.add(new BuildBox("DEPENSES LIGUE", "Repartition du mois selectionne",
				FinancePanelUtil.buildBarChartPanel(expenseDataset, "Type", "Montant (M$)", DashboardPanelUtil.EXPENSE_COLOR)));
		return column;
	}

	private JPanel buildSummaryPanel() {
		JPanel summaryPanel = new JPanel(new GridLayout(1, 5, DASHBOARD_SPACING, 0));
		summaryPanel.setOpaque(false);
		summaryPanel.add(FinancePanelUtil.buildMetricCard("Budget restant", remainingBudgetValueLabel));
		summaryPanel.add(FinancePanelUtil.buildMetricCard("Valeur ligue", leagueValueValueLabel));
		summaryPanel.add(FinancePanelUtil.buildMetricCard("Revenus mois", summaryRevenueValueLabel));
		summaryPanel.add(FinancePanelUtil.buildMetricCard("Net mois", summaryNetValueLabel));
		summaryPanel.add(FinancePanelUtil.buildMetricCard("Mois", monthSelector));
		return summaryPanel;
	}

	private JPanel buildRulesPanel() {
		JPanel panel = FinancePanelUtil.createSectionContentPanel();
		panel.add(FinancePanelUtil.buildTextMetricRow("Salary cap", salaryCapValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinancePanelUtil.buildTextMetricRow("Luxury tax", luxuryTaxValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinancePanelUtil.buildTextMetricRow("Minimum payroll", minimumSalaryValueLabel));
		return panel;
	}

	private JPanel buildMonthlySnapshotPanel() {
		JPanel panel = FinancePanelUtil.createSectionContentPanel();
		panel.add(FinancePanelUtil.buildTextMetricRow("Revenus", snapshotRevenueValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinancePanelUtil.buildTextMetricRow("Depenses", snapshotExpenseValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinancePanelUtil.buildTextMetricRow("Net", snapshotNetValueLabel));
		panel.add(Box.createVerticalStrut(18));
		panel.add(FinancePanelUtil.buildTextMetricRow("Retention ligue", retentionValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinancePanelUtil.buildTextMetricRow("Redistribution", redistributionValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinancePanelUtil.buildTextMetricRow("Part egale", equalShareValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinancePanelUtil.buildTextMetricRow("Part ponderee", weightedShareValueLabel));
		return panel;
	}

	private void actions() {
		monthSelector.setChangeListener(new RefreshAction());
	}

	public void refreshData() {
		League league = guiInterface.getLeague();
		if (league == null || league.getLeagueFinance() == null) {
			return;
		}

		ArrayList<Team> teams = guiInterface.getTeams();
		Budget leagueBudget = league.getLeagueFinance().getBudget();
		LeagueFinancialRules rules = league.getLeagueFinance().getLeagueFinancialRules();
		LeagueRedistributionPolicy redistributionPolicy = league.getLeagueFinance().getLeagueRedistributionPolicy();
		updatingMonthSelector = true;
		FinancePanelUtil.setAvailableMonths(monthSelector, FinancePanelUtil.getAvailableMonths(guiInterface, leagueBudget));
		updatingMonthSelector = false;
		int selectedMonth = FinancePanelUtil.getSelectedMonth(monthSelector);
		double selectedRevenue = FinancePanelUtil.sumIncomeMap(leagueBudget.getIncomesForMonth(selectedMonth));
		double selectedExpense = FinancePanelUtil.sumExpenseMap(leagueBudget.getExpensesForMonth(selectedMonth));

		remainingBudgetValueLabel.setText(FinancePanelUtil.formatMoney(leagueBudget.getRemainingAmount()));
		leagueValueValueLabel.setText(FinancePanelUtil.formatMoney(league.getLeagueFinance().getLeagueValue()));
		summaryRevenueValueLabel.setText(FinancePanelUtil.formatMoney(selectedRevenue));
		summaryNetValueLabel.setText(FinancePanelUtil.formatMoney(selectedRevenue - selectedExpense));
		snapshotRevenueValueLabel.setText(FinancePanelUtil.formatMoney(selectedRevenue));
		snapshotExpenseValueLabel.setText(FinancePanelUtil.formatMoney(selectedExpense));
		snapshotNetValueLabel.setText(FinancePanelUtil.formatMoney(selectedRevenue - selectedExpense));
		salaryCapValueLabel.setText(FinancePanelUtil.formatMoney(rules.getSalaryCap()));
		luxuryTaxValueLabel.setText(FinancePanelUtil.formatMoney(rules.getLuxuryTaxLine()));
		minimumSalaryValueLabel.setText(FinancePanelUtil.formatMoney(rules.getMinimumTeamSalary()));

		retentionValueLabel.setText(FinancePanelUtil.formatPercent(redistributionPolicy.getBaseLeagueRetentionRate()));
		redistributionValueLabel.setText(FinancePanelUtil.formatPercent(redistributionPolicy.getBaseRedistributionRate()));
		equalShareValueLabel.setText(FinancePanelUtil.formatPercent(redistributionPolicy.getBaseEqualShareRate()));
		weightedShareValueLabel.setText(FinancePanelUtil.formatPercent(redistributionPolicy.getBaseWeightedShareRate()));

		remainingBudgetValueLabel.setForeground(DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
		leagueValueValueLabel.setForeground(DashboardPanelUtil.POLICY_BALANCED_COLOR);
		FinancePanelUtil.applyRevenueColor(summaryRevenueValueLabel);
		FinancePanelUtil.applyAmountColor(summaryNetValueLabel, selectedRevenue - selectedExpense);
		FinancePanelUtil.applyRevenueColor(snapshotRevenueValueLabel);
		FinancePanelUtil.applyExpenseColor(snapshotExpenseValueLabel);
		FinancePanelUtil.applyAmountColor(snapshotNetValueLabel, selectedRevenue - selectedExpense);
		salaryCapValueLabel.setForeground(DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
		luxuryTaxValueLabel.setForeground(DashboardPanelUtil.EXPENSE_COLOR);
		minimumSalaryValueLabel.setForeground(DashboardPanelUtil.POLICY_BALANCED_COLOR);
		retentionValueLabel.setForeground(DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
		redistributionValueLabel.setForeground(DashboardPanelUtil.REVENUE_COLOR);
		equalShareValueLabel.setForeground(DashboardPanelUtil.POLICY_BALANCED_COLOR);
		weightedShareValueLabel.setForeground(DashboardPanelUtil.STRATEGY_REBUILD_COLOR);

		rebuildLeagueHistoryDataset(leagueBudget);
		rebuildLeagueExpenseDataset(leagueBudget, selectedMonth);
		rebuildTopTeamsPanel(teams, selectedMonth);
	}

	private void rebuildLeagueHistoryDataset(Budget leagueBudget) {
		historyDataset.clear();
		for (int month = 1; month <= FinancePanelUtil.getLastVisibleFinanceMonth(guiInterface); month++) {
			double revenue = FinancePanelUtil.sumIncomeMap(leagueBudget.getIncomesForMonth(month));
			double expense = FinancePanelUtil.sumExpenseMap(leagueBudget.getExpensesForMonth(month));
			historyDataset.addValue(revenue, "Revenus", FinancePanelUtil.monthLabel(month));
			historyDataset.addValue(expense, "Depenses", FinancePanelUtil.monthLabel(month));
		}
	}

	private void rebuildLeagueExpenseDataset(Budget leagueBudget, int month) {
		expenseDataset.clear();
		Map<String, Expense> expenses = leagueBudget.getExpensesForMonth(month);
		if (expenses == null || expenses.isEmpty()) {
			expenseDataset.addValue(0.0, "Depenses", "Aucune");
			return;
		}

		for (Expense expense : expenses.values()) {
			expenseDataset.addValue(expense.getAmount(), "Depenses", FinancePanelUtil.prettifyEnum(expense.getName()));
		}
	}

	private void rebuildTopTeamsPanel(List<Team> teams, int month) {
		topTeamsPanel.removeAll();
		List<Team> sortedTeams = new ArrayList<Team>(teams);
		Collections.sort(sortedTeams, new TeamNetComparator(month));

		for (int index = 0; index < Math.min(5, sortedTeams.size()); index++) {
			Team team = sortedTeams.get(index);
			double net = FinancePanelUtil.getNetForMonth(FinancePanelUtil.getBudget(team), month);
			topTeamsPanel.add(FinancePanelUtil.buildListMetricRow((index + 1) + ". " + team.getName(),
					FinancePanelUtil.formatMoney(net), DashboardPanelUtil.getValueColorForAmount(net)));
			if (index < 4) {
				topTeamsPanel.add(Box.createVerticalStrut(8));
			}
		}
		topTeamsPanel.revalidate();
		topTeamsPanel.repaint();
	}

	private class RefreshAction implements Runnable {
		@Override
		public void run() {
			if (!updatingMonthSelector) {
				refreshData();
			}
		}
	}

	private class TeamNetComparator implements Comparator<Team> {
		private final int month;

		private TeamNetComparator(int month) {
			this.month = month;
		}

		@Override
		public int compare(Team teamA, Team teamB) {
			double valueA = FinancePanelUtil.getNetForMonth(FinancePanelUtil.getBudget(teamA), month);
			double valueB = FinancePanelUtil.getNetForMonth(FinancePanelUtil.getBudget(teamB), month);
			return Double.compare(valueB, valueA);
		}
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		monthSelector.applyTheme();
		FinancePanelUtil.applyThemeToCharts(this);
	}
}

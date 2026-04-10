package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.league.League;
import data.league.finance.LeagueFinancialRules;
import data.league.finance.LeagueRedistributionPolicy;
import data.team.Team;
import gui.panel.common.BuildBox;
import gui.panel.common.DashboardPanelUtil;
import process.orchestrator.GUIInterface;

public class LeagueFinanceViewPanel extends AbstractFinanceViewPanel {

	private boolean updatingMonthSelector;
	private final MonthNavigator monthSelector;
	private final JLabel remainingBudgetValueLabel;
	private final JLabel leagueValueValueLabel;
	private final JLabel teamsValueLabel;
	private final JLabel salaryCapValueLabel;
	private final JLabel luxuryTaxValueLabel;
	private final JLabel minimumSalaryValueLabel;
	private final JLabel retentionValueLabel;
	private final JLabel redistributionValueLabel;
	private final JLabel equalShareValueLabel;
	private final JLabel weightedShareValueLabel;
	private final JPanel topTeamsPanel;
	private final DefaultCategoryDataset revenueDataset;
	private final DefaultCategoryDataset expenseDataset;
	private final DefaultPieDataset redistributionDataset;

	public LeagueFinanceViewPanel(GUIInterface guiInterface) {
		super(guiInterface);
		monthSelector = buildMonthNavigator();
		remainingBudgetValueLabel = createMetricValueLabel();
		leagueValueValueLabel = createMetricValueLabel();
		teamsValueLabel = createMetricValueLabel();
		salaryCapValueLabel = createMetricValueLabel();
		luxuryTaxValueLabel = createMetricValueLabel();
		minimumSalaryValueLabel = createMetricValueLabel();
		retentionValueLabel = createBodyValueLabel();
		redistributionValueLabel = createBodyValueLabel();
		equalShareValueLabel = createBodyValueLabel();
		weightedShareValueLabel = createBodyValueLabel();
		topTeamsPanel = createMetricListPanel();
		revenueDataset = new DefaultCategoryDataset();
		expenseDataset = new DefaultCategoryDataset();
		redistributionDataset = new DefaultPieDataset();

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
		centerColumn.add(new BuildBox("REVENUS LIGUE", "Evolution par mois",
				buildLineChartPanel(revenueDataset, "Revenus de la ligue", "Montant (M$)", new Color(0x24, 0x6B, 0xCE))),
				BorderLayout.CENTER);
		centerColumn.add(withPreferredHeight(
				new BuildBox("REDISTRIBUTION", "Politique et repartition", buildRedistributionPanel()), 250),
				BorderLayout.SOUTH);
		return centerColumn;
	}

	private JPanel buildRightColumn() {
		JPanel column = DashboardPanelUtil.createGridColumn(3, 1, 0, 12, RIGHT_COLUMN_WIDTH);
		column.add(new BuildBox("REGLES", "Parametres globaux", buildRulesPanel()));
		column.add(new BuildBox("DEPENSES LIGUE", "Mois selectionne",
				buildBarChartPanel(expenseDataset, "Depenses ligue", "Type", "Montant (M$)", new Color(0xC0, 0x5A, 0x3D))));
		column.add(new BuildBox("TOP EQUIPES", "Classement budget restant", topTeamsPanel));
		return column;
	}

	private JPanel buildSummaryPanel() {
		JPanel summaryPanel = new JPanel(new GridLayout(1, 4, DASHBOARD_SPACING, 0));
		summaryPanel.setOpaque(false);
		summaryPanel.add(buildMetricCard("Budget restant", remainingBudgetValueLabel));
		summaryPanel.add(buildMetricCard("Valeur ligue", leagueValueValueLabel));
		summaryPanel.add(buildMetricCard("Equipes", teamsValueLabel));
		summaryPanel.add(buildMetricCard("Mois", monthSelector));
		return summaryPanel;
	}

	private JPanel buildRulesPanel() {
		JPanel panel = createSectionContentPanel();
		panel.add(buildTextMetricRow("Salary cap", salaryCapValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildTextMetricRow("Luxury tax", luxuryTaxValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildTextMetricRow("Minimum payroll", minimumSalaryValueLabel));
		return panel;
	}

	private JPanel buildRedistributionPanel() {
		JPanel panel = new JPanel(new BorderLayout(DASHBOARD_SPACING, 0));
		panel.setOpaque(false);

		JPanel metricsPanel = createSectionContentPanel();
		metricsPanel.setPreferredSize(new java.awt.Dimension(280, 10));
		metricsPanel.add(buildTextMetricRow("Retention ligue", retentionValueLabel));
		metricsPanel.add(Box.createVerticalStrut(10));
		metricsPanel.add(buildTextMetricRow("Redistribution", redistributionValueLabel));
		metricsPanel.add(Box.createVerticalStrut(10));
		metricsPanel.add(buildTextMetricRow("Part egale", equalShareValueLabel));
		metricsPanel.add(Box.createVerticalStrut(10));
		metricsPanel.add(buildTextMetricRow("Part ponderee", weightedShareValueLabel));

		panel.add(metricsPanel, BorderLayout.WEST);
		panel.add(buildPieChartPanel(redistributionDataset, "Redistribution"), BorderLayout.CENTER);
		return panel;
	}

	private void actions() {
		monthSelector.setChangeListener(() -> {
			if (!updatingMonthSelector) {
				refreshData();
			}
		});
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
		setMonthSelectorOptions(monthSelector, getAvailableMonths(leagueBudget));
		updatingMonthSelector = false;

		remainingBudgetValueLabel.setText(formatMoney(leagueBudget.getRemainingAmount()));
		leagueValueValueLabel.setText(formatMoney(league.getLeagueFinance().getLeagueValue()));
		teamsValueLabel.setText(String.valueOf(teams.size()));
		salaryCapValueLabel.setText(formatMoney(rules.getSalaryCap()));
		luxuryTaxValueLabel.setText(formatMoney(rules.getLuxuryTaxLine()));
		minimumSalaryValueLabel.setText(formatMoney(rules.getMinimumTeamSalary()));

		retentionValueLabel.setText(formatPercent(redistributionPolicy.getBaseLeagueRetentionRate()));
		redistributionValueLabel.setText(formatPercent(redistributionPolicy.getBaseRedistributionRate()));
		equalShareValueLabel.setText(formatPercent(redistributionPolicy.getBaseEqualShareRate()));
		weightedShareValueLabel.setText(formatPercent(redistributionPolicy.getBaseWeightedShareRate()));

		rebuildLeagueRevenueDataset(leagueBudget);
		rebuildLeagueExpenseDataset(leagueBudget, getSelectedMonth(monthSelector));
		rebuildRedistributionDataset(redistributionPolicy);
		rebuildTopTeamsPanel(teams);
	}

	private void rebuildLeagueRevenueDataset(Budget leagueBudget) {
		revenueDataset.clear();
		for (int month = 1; month <= getLastVisibleFinanceMonth(); month++) {
			double revenue = sumIncomeMap(leagueBudget.getIncomesForMonth(month));
			revenueDataset.addValue(revenue, "Revenus", monthLabel(month));
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
			expenseDataset.addValue(expense.getAmount(), "Depenses", prettifyEnum(expense.getName()));
		}
	}

	private void rebuildRedistributionDataset(LeagueRedistributionPolicy redistributionPolicy) {
		redistributionDataset.clear();
		redistributionDataset.setValue("Retention ligue", redistributionPolicy.getBaseLeagueRetentionRate());
		redistributionDataset.setValue("Part egale", redistributionPolicy.getBaseEqualShareRate());
		redistributionDataset.setValue("Part ponderee", redistributionPolicy.getBaseWeightedShareRate());
	}

	private void rebuildTopTeamsPanel(List<Team> teams) {
		topTeamsPanel.removeAll();
		List<Team> sortedTeams = new ArrayList<Team>(teams);
		Collections.sort(sortedTeams, Comparator.comparingDouble(team -> -getRemainingBudget(team)));

		for (int index = 0; index < Math.min(5, sortedTeams.size()); index++) {
			Team team = sortedTeams.get(index);
			topTeamsPanel.add(buildListMetricRow((index + 1) + ". " + team.getName(), formatMoney(getRemainingBudget(team))));
			if (index < 4) {
				topTeamsPanel.add(Box.createVerticalStrut(8));
			}
		}
		topTeamsPanel.revalidate();
		topTeamsPanel.repaint();
	}
}

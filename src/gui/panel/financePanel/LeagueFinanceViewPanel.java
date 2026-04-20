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
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.MonthNavigatorPanel;
import gui.panel.common.ThemeAware;
import process.orchestrator.interf.GUIInterface;

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
		remainingBudgetValueLabel = FinanceViewFactory.metricLabel();
		leagueValueValueLabel = FinanceViewFactory.metricLabel();
		summaryRevenueValueLabel = FinanceViewFactory.metricLabel();
		summaryNetValueLabel = FinanceViewFactory.metricLabel();
		snapshotRevenueValueLabel = FinanceViewFactory.metricLabel();
		snapshotExpenseValueLabel = FinanceViewFactory.metricLabel();
		snapshotNetValueLabel = FinanceViewFactory.metricLabel();
		salaryCapValueLabel = FinanceViewFactory.metricLabel();
		luxuryTaxValueLabel = FinanceViewFactory.metricLabel();
		minimumSalaryValueLabel = FinanceViewFactory.metricLabel();
		retentionValueLabel = FinanceViewFactory.infoLabel();
		redistributionValueLabel = FinanceViewFactory.infoLabel();
		equalShareValueLabel = FinanceViewFactory.infoLabel();
		weightedShareValueLabel = FinanceViewFactory.infoLabel();
		topTeamsPanel = FinanceViewFactory.metricListPanel();
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
				FinanceViewFactory.financeLineChart(historyDataset, DashboardPanelUtil.REVENUE_COLOR)),
				BorderLayout.NORTH);
		centerColumn.add(FinanceViewFactory.panelWithHeight(
				new BuildBox("DEPENSES DE LA LIGUE", "Repartition du mois selectionne",
						FinanceViewFactory.financeBarChart(expenseDataset, DashboardPanelUtil.EXPENSE_COLOR)),
				250),
				BorderLayout.CENTER);
		return centerColumn;
	}

	private JPanel buildRightColumn() {
		JPanel column = DashboardPanelUtil.createGridColumn(3, 1, 0, 12, RIGHT_COLUMN_WIDTH);
		column.add(new BuildBox("VUE MENSUELLE", "Resume du mois selectionne", buildMonthlySnapshotPanel()));
		column.add(new BuildBox("REGLES", "Parametres globaux", buildRulesPanel()));
		column.add(new BuildBox("TOP EQUIPES DU MOIS", "Net du mois selectionne", topTeamsPanel));
		return column;
	}

	private JPanel buildSummaryPanel() {
		JPanel summaryPanel = new JPanel(new GridLayout(1, 5, DASHBOARD_SPACING, 0));
		summaryPanel.setOpaque(false);
		summaryPanel.add(FinanceViewFactory.metricCard("Budget restant", remainingBudgetValueLabel));
		summaryPanel.add(FinanceViewFactory.metricCard("Valeur ligue", leagueValueValueLabel));
		summaryPanel.add(FinanceViewFactory.metricCard("Revenus mois", summaryRevenueValueLabel));
		summaryPanel.add(FinanceViewFactory.metricCard("Net mois", summaryNetValueLabel));
		summaryPanel.add(FinanceViewFactory.metricCard("Mois", monthSelector));
		return summaryPanel;
	}

	private JPanel buildRulesPanel() {
		JPanel panel = FinanceViewFactory.infoPanel();
		panel.add(FinanceViewFactory.infoRow("Plafond salarial", salaryCapValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Taxe de luxe", luxuryTaxValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Masse salariale min.", minimumSalaryValueLabel));
		return panel;
	}

	private JPanel buildMonthlySnapshotPanel() {
		JPanel panel = FinanceViewFactory.infoPanel();
		panel.add(FinanceViewFactory.infoRow("Revenus", snapshotRevenueValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Depenses", snapshotExpenseValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Net", snapshotNetValueLabel));
		panel.add(Box.createVerticalStrut(18));
		panel.add(FinanceViewFactory.infoRow("Retention ligue", retentionValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Redistribution", redistributionValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Part egale", equalShareValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Part ponderee", weightedShareValueLabel));
		return panel;
	}

	private void actions() {
		monthSelector.setChangeListener(new RefreshAction());
	}

	public void refreshData() {
		if (!guiInterface.isSeasonInitialized()) {
			showSeasonNotStartedState();
			return;
		}
		League league = guiInterface.getLeague();
		if (league == null || league.getLeagueFinance() == null) {
			showNoDataState();
			return;
		}

		ArrayList<Team> teams = guiInterface.getTeams();
		Budget leagueBudget = league.getLeagueFinance().getBudget();
		LeagueFinancialRules rules = league.getLeagueFinance().getLeagueFinancialRules();
		LeagueRedistributionPolicy redistributionPolicy = league.getLeagueFinance().getLeagueRedistributionPolicy();
		updatingMonthSelector = true;
		FinanceDataUtil.setAvailableMonths(monthSelector, FinanceDataUtil.availableMonths(guiInterface, leagueBudget));
		updatingMonthSelector = false;
		int selectedMonth = FinanceDataUtil.selectedMonth(monthSelector);
		double selectedRevenue = FinanceDataUtil.totalIncome(leagueBudget.getIncomesForMonth(selectedMonth));
		double selectedExpense = FinanceDataUtil.totalExpense(leagueBudget.getExpensesForMonth(selectedMonth));

		remainingBudgetValueLabel.setText(FinanceDataUtil.formatMoney(leagueBudget.getRemainingAmount()));
		leagueValueValueLabel.setText(FinanceDataUtil.formatMoney(league.getLeagueFinance().getLeagueValue()));
		summaryRevenueValueLabel.setText(FinanceDataUtil.formatMoney(selectedRevenue));
		summaryNetValueLabel.setText(FinanceDataUtil.formatMoney(selectedRevenue - selectedExpense));
		snapshotRevenueValueLabel.setText(FinanceDataUtil.formatMoney(selectedRevenue));
		snapshotExpenseValueLabel.setText(FinanceDataUtil.formatMoney(selectedExpense));
		snapshotNetValueLabel.setText(FinanceDataUtil.formatMoney(selectedRevenue - selectedExpense));
		salaryCapValueLabel.setText(FinanceDataUtil.formatMoney(rules.getSalaryCap()));
		luxuryTaxValueLabel.setText(FinanceDataUtil.formatMoney(rules.getLuxuryTaxLine()));
		minimumSalaryValueLabel.setText(FinanceDataUtil.formatMoney(rules.getMinimumTeamSalary()));

		retentionValueLabel.setText(FinanceDataUtil.formatPercent(redistributionPolicy.getBaseLeagueRetentionRate()));
		redistributionValueLabel.setText(FinanceDataUtil.formatPercent(redistributionPolicy.getBaseRedistributionRate()));
		equalShareValueLabel.setText(FinanceDataUtil.formatPercent(redistributionPolicy.getBaseEqualShareRate()));
		weightedShareValueLabel.setText(FinanceDataUtil.formatPercent(redistributionPolicy.getBaseWeightedShareRate()));

		remainingBudgetValueLabel.setForeground(DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
		leagueValueValueLabel.setForeground(DashboardPanelUtil.POLICY_BALANCED_COLOR);
		FinanceDataUtil.setRevenueColor(summaryRevenueValueLabel);
		FinanceDataUtil.setAmountColor(summaryNetValueLabel, selectedRevenue - selectedExpense);
		FinanceDataUtil.setRevenueColor(snapshotRevenueValueLabel);
		FinanceDataUtil.setExpenseColor(snapshotExpenseValueLabel);
		FinanceDataUtil.setAmountColor(snapshotNetValueLabel, selectedRevenue - selectedExpense);
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

	public void showSeasonNotStartedState() {
		remainingBudgetValueLabel.setText("La saison n'a pas encore commence.");
		leagueValueValueLabel.setText("La valeur de la ligue sera disponible apres le lancement.");
		summaryRevenueValueLabel.setText("Les revenus mensuels apparaitront apres le lancement.");
		summaryNetValueLabel.setText("Le resultat net sera calcule apres le debut de saison.");
		snapshotRevenueValueLabel.setText("Aucun revenu mensuel n'est disponible pour le moment.");
		snapshotExpenseValueLabel.setText("Aucune depense mensuelle n'est disponible pour le moment.");
		snapshotNetValueLabel.setText("Aucun resultat mensuel n'est disponible pour le moment.");
		salaryCapValueLabel.setText("Les regles financieres seront affichees au lancement.");
		luxuryTaxValueLabel.setText("Les regles financieres seront affichees au lancement.");
		minimumSalaryValueLabel.setText("Les regles financieres seront affichees au lancement.");
		retentionValueLabel.setText("Aucune redistribution n'est calculee pour le moment.");
		redistributionValueLabel.setText("Aucune redistribution n'est calculee pour le moment.");
		equalShareValueLabel.setText("Aucune redistribution n'est calculee pour le moment.");
		weightedShareValueLabel.setText("Aucune redistribution n'est calculee pour le moment.");
		resetVisualDatasets("Lancez la saison pour afficher les finances de la ligue.");
	}

	private void showNoDataState() {
		remainingBudgetValueLabel.setText("Aucune donnee de ligue n'est disponible.");
		leagueValueValueLabel.setText("Aucune donnee de ligue n'est disponible.");
		summaryRevenueValueLabel.setText("Aucune donnee de ligue n'est disponible.");
		summaryNetValueLabel.setText("Aucune donnee de ligue n'est disponible.");
		snapshotRevenueValueLabel.setText("Aucune donnee de ligue n'est disponible.");
		snapshotExpenseValueLabel.setText("Aucune donnee de ligue n'est disponible.");
		snapshotNetValueLabel.setText("Aucune donnee de ligue n'est disponible.");
		salaryCapValueLabel.setText("Aucune regle financiere n'est disponible.");
		luxuryTaxValueLabel.setText("Aucune regle financiere n'est disponible.");
		minimumSalaryValueLabel.setText("Aucune regle financiere n'est disponible.");
		retentionValueLabel.setText("Aucune redistribution n'est disponible.");
		redistributionValueLabel.setText("Aucune redistribution n'est disponible.");
		equalShareValueLabel.setText("Aucune redistribution n'est disponible.");
		weightedShareValueLabel.setText("Aucune redistribution n'est disponible.");
		resetVisualDatasets("Aucune donnee de ligue n'est disponible.");
	}

	private void resetVisualDatasets(String message) {
		applyEmptyStateLabel(remainingBudgetValueLabel, 12);
		applyEmptyStateLabel(leagueValueValueLabel, 12);
		applyEmptyStateLabel(summaryRevenueValueLabel, 12);
		applyEmptyStateLabel(summaryNetValueLabel, 12);
		applyEmptyStateLabel(snapshotRevenueValueLabel, 12);
		applyEmptyStateLabel(snapshotExpenseValueLabel, 12);
		applyEmptyStateLabel(snapshotNetValueLabel, 12);
		applyEmptyStateLabel(salaryCapValueLabel, 12);
		applyEmptyStateLabel(luxuryTaxValueLabel, 12);
		applyEmptyStateLabel(minimumSalaryValueLabel, 12);
		applyEmptyStateLabel(retentionValueLabel, 12);
		applyEmptyStateLabel(redistributionValueLabel, 12);
		applyEmptyStateLabel(equalShareValueLabel, 12);
		applyEmptyStateLabel(weightedShareValueLabel, 12);
		historyDataset.clear();
		expenseDataset.clear();
		topTeamsPanel.removeAll();
		JLabel messageLabel = new JLabel(message);
		LabelStyleUtil.styleSubtitleLabel(messageLabel, 12);
		topTeamsPanel.add(messageLabel);
		topTeamsPanel.revalidate();
		topTeamsPanel.repaint();
	}

	private void applyEmptyStateLabel(JLabel label, int fontSize) {
		LabelStyleUtil.styleSubtitleLabel(label, fontSize);
	}

	private void rebuildLeagueHistoryDataset(Budget leagueBudget) {
		historyDataset.clear();
		for (int month = 1; month <= FinanceDataUtil.lastVisibleMonth(guiInterface); month++) {
			double revenue = FinanceDataUtil.totalIncome(leagueBudget.getIncomesForMonth(month));
			double expense = FinanceDataUtil.totalExpense(leagueBudget.getExpensesForMonth(month));
			historyDataset.addValue(revenue, "Revenus", FinanceDataUtil.monthLabel(month));
			historyDataset.addValue(expense, "Depenses", FinanceDataUtil.monthLabel(month));
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
			expenseDataset.addValue(expense.getAmount(), "Depenses", FinanceDataUtil.formatTypeName(expense.getName()));
		}
	}

	private void rebuildTopTeamsPanel(List<Team> teams, int month) {
		topTeamsPanel.removeAll();
		List<Team> sortedTeams = new ArrayList<Team>(teams);
		Collections.sort(sortedTeams, new TeamNetComparator(month));

		for (int index = 0; index < Math.min(5, sortedTeams.size()); index++) {
			Team team = sortedTeams.get(index);
			double net = FinanceDataUtil.monthNet(FinanceDataUtil.teamBudget(team), month);
			topTeamsPanel.add(FinanceViewFactory.valueRow((index + 1) + ". " + team.getName(),
					FinanceDataUtil.formatMoney(net), DashboardPanelUtil.getValueColorForAmount(net)));
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
			double valueA = FinanceDataUtil.monthNet(FinanceDataUtil.teamBudget(teamA), month);
			double valueB = FinanceDataUtil.monthNet(FinanceDataUtil.teamBudget(teamB), month);
			return Double.compare(valueB, valueA);
		}
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		monthSelector.applyTheme();
		FinanceViewFactory.refreshCharts(this);
	}
}

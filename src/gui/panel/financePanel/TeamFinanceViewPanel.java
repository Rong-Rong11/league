package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.data.category.DefaultCategoryDataset;

import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.finance.budget.income.Income;
import data.team.Team;
import gui.panel.common.BuildBox;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.MonthNavigatorPanel;
import gui.panel.common.ThemeAware;
import process.orchestrator.GUIInterface;

public class TeamFinanceViewPanel extends JPanel implements ThemeAware {

	private static final int DASHBOARD_SPACING = 10;
	private static final int RIGHT_COLUMN_WIDTH = 280;

	private boolean updatingSelectors;
	private final GUIInterface guiInterface;
	private final JComboBox<String> teamSelector;
	private final MonthNavigatorPanel monthSelector;
	private final JLabel budgetValueLabel;
	private final JLabel selectedRevenueValueLabel;
	private final JLabel selectedExpenseValueLabel;
	private final JLabel selectedNetValueLabel;
	private final JLabel luxuryTaxValueLabel;
	private final JLabel profileValueLabel;
	private final JLabel marketValueLabel;
	private final JLabel strategyValueLabel;
	private final JLabel ticketPriceValueLabel;
	private final JLabel capacityValueLabel;
	private final JPanel revenueMetricsPanel;
	private final JPanel expenseMetricsPanel;
	private final DefaultCategoryDataset revenueDataset;
	private final DefaultCategoryDataset expenseDataset;
	private final DefaultCategoryDataset historyDataset;

	public TeamFinanceViewPanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		teamSelector = new JComboBox<String>();
		monthSelector = new MonthNavigatorPanel();
		FinancePanelUtil.styleComboBox(teamSelector);
		budgetValueLabel = FinancePanelUtil.createMetricValueLabel();
		selectedRevenueValueLabel = FinancePanelUtil.createMetricValueLabel();
		selectedExpenseValueLabel = FinancePanelUtil.createMetricValueLabel();
		selectedNetValueLabel = FinancePanelUtil.createMetricValueLabel();
		luxuryTaxValueLabel = FinancePanelUtil.createMetricValueLabel();
		profileValueLabel = FinancePanelUtil.createBodyValueLabel();
		marketValueLabel = FinancePanelUtil.createBodyValueLabel();
		strategyValueLabel = FinancePanelUtil.createBodyValueLabel();
		ticketPriceValueLabel = FinancePanelUtil.createBodyValueLabel();
		capacityValueLabel = FinancePanelUtil.createBodyValueLabel();
		revenueMetricsPanel = FinancePanelUtil.createMetricListPanel();
		expenseMetricsPanel = FinancePanelUtil.createMetricListPanel();
		revenueDataset = new DefaultCategoryDataset();
		expenseDataset = new DefaultCategoryDataset();
		historyDataset = new DefaultCategoryDataset();

		organize();
		actions();
	}

	private void organize() {
		setLayout(new BorderLayout(0, DASHBOARD_SPACING));
		add(new BuildBox("RESUME EQUIPE", "Vue detaillee du club", buildSummaryPanel()), BorderLayout.NORTH);
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
		centerColumn.add(FinancePanelUtil.withPreferredHeight(
				new BuildBox("HISTORIQUE MENSUEL", "Revenus vs depenses",
						FinancePanelUtil.buildLineChartPanel(historyDataset, "Montant (M$)", DashboardPanelUtil.REVENUE_COLOR)),
				250), BorderLayout.NORTH);

		JPanel bottomRow = new JPanel(new GridLayout(1, 2, DASHBOARD_SPACING, 0));
		bottomRow.setOpaque(false);
		bottomRow.add(new BuildBox("REVENUS", "Mois selectionne", buildRevenuePanel()));
		bottomRow.add(new BuildBox("DEPENSES", "Mois selectionne", buildExpensePanel()));
		centerColumn.add(FinancePanelUtil.withPreferredHeight(bottomRow, 280), BorderLayout.CENTER);
		return centerColumn;
	}

	private JPanel buildRightColumn() {
		JPanel column = DashboardPanelUtil.createRightColumn(RIGHT_COLUMN_WIDTH, 12);
		column.add(new BuildBox("INFOS STABLES", "Profil et infrastructure", buildStaticInfoPanel()), BorderLayout.NORTH);
		return column;
	}

	private JPanel buildSummaryPanel() {
		JPanel summaryPanel = new JPanel(new GridLayout(1, 5, DASHBOARD_SPACING, 0));
		summaryPanel.setOpaque(false);
		summaryPanel.add(FinancePanelUtil.buildMetricCard("Equipe", teamSelector));
		summaryPanel.add(FinancePanelUtil.buildMetricCard("Budget restant", budgetValueLabel));
		summaryPanel.add(FinancePanelUtil.buildMetricCard("Revenus mois", selectedRevenueValueLabel));
		summaryPanel.add(FinancePanelUtil.buildMetricCard("Net mois", selectedNetValueLabel));
		summaryPanel.add(FinancePanelUtil.buildMetricCard("Mois", monthSelector));
		return summaryPanel;
	}

	private JPanel buildProfilePanel() {
		JPanel panel = FinancePanelUtil.createSectionContentPanel();
		panel.add(FinancePanelUtil.buildTextMetricRow("Financial policy", profileValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinancePanelUtil.buildTextMetricRow("Market size", marketValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinancePanelUtil.buildTextMetricRow("Strategy", strategyValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinancePanelUtil.buildTextMetricRow("Luxury tax paid", luxuryTaxValueLabel));
		return panel;
	}

	private JPanel buildRevenuePanel() {
		JPanel panel = new JPanel(new BorderLayout(0, DASHBOARD_SPACING));
		panel.setOpaque(false);
		panel.add(revenueMetricsPanel, BorderLayout.NORTH);
		panel.add(FinancePanelUtil.buildBarChartPanel(revenueDataset, "Type", "Montant (M$)", DashboardPanelUtil.REVENUE_COLOR),
				BorderLayout.CENTER);
		return panel;
	}

	private JPanel buildExpensePanel() {
		JPanel panel = new JPanel(new BorderLayout(0, DASHBOARD_SPACING));
		panel.setOpaque(false);
		panel.add(expenseMetricsPanel, BorderLayout.NORTH);
		panel.add(FinancePanelUtil.buildBarChartPanel(expenseDataset, "Type", "Montant (M$)", DashboardPanelUtil.EXPENSE_COLOR),
				BorderLayout.CENTER);
		return panel;
	}

	private JPanel buildInfrastructurePanel() {
		JPanel panel = FinancePanelUtil.createSectionContentPanel();
		panel.add(FinancePanelUtil.buildTextMetricRow("Capacite", capacityValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinancePanelUtil.buildTextMetricRow("Prix billet", ticketPriceValueLabel));
		return panel;
	}

	private JPanel buildStaticInfoPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1, 0, DASHBOARD_SPACING));
		panel.setOpaque(false);
		panel.add(buildProfilePanel());
		panel.add(buildInfrastructurePanel());
		return panel;
	}

	private void actions() {
		teamSelector.addActionListener(new TeamSelectionAction());
		monthSelector.setChangeListener(new MonthSelectionAction());
	}

	public void refreshData() {
		populateTeamsIfNeeded();
		Team team = FinancePanelUtil.getSelectedTeam(guiInterface, teamSelector, 0);
		if (team == null) {
			resetView();
			return;
		}

		int month = FinancePanelUtil.getSelectedMonth(monthSelector);
		Budget budget = FinancePanelUtil.getBudget(team);
		updatingSelectors = true;
		FinancePanelUtil.setAvailableMonths(monthSelector, FinancePanelUtil.getAvailableMonths(guiInterface, budget));
		updatingSelectors = false;
		month = FinancePanelUtil.getSelectedMonth(monthSelector);

		budgetValueLabel.setText(budget == null ? "-" : FinancePanelUtil.formatMoney(budget.getRemainingAmount()));
		double selectedRevenue = FinancePanelUtil.sumIncomeMap(FinancePanelUtil.getIncomeMap(team, month));
		double selectedExpense = FinancePanelUtil.sumExpenseMap(FinancePanelUtil.getExpenseMap(team, month));
		selectedRevenueValueLabel.setText(FinancePanelUtil.formatMoney(selectedRevenue));
		selectedExpenseValueLabel.setText(FinancePanelUtil.formatMoney(selectedExpense));
		selectedNetValueLabel.setText(FinancePanelUtil.formatMoney(selectedRevenue - selectedExpense));
		luxuryTaxValueLabel.setText(FinancePanelUtil.formatMoney(getLuxuryTaxPaid(team)));
		profileValueLabel.setText(FinancePanelUtil.prettifyObjectName(getFinancialPolicy(team)));
		marketValueLabel.setText(FinancePanelUtil.prettifyObjectName(getMarketSize(team)));
		strategyValueLabel.setText(FinancePanelUtil.prettifyObjectName(getTransferStrategy(team)));
		ticketPriceValueLabel.setText(team.getStadium() == null ? "-" : FinancePanelUtil.formatMoney(team.getStadium().getTicketPrice()));
		capacityValueLabel.setText(team.getStadium() == null ? "-" : String.valueOf(team.getStadium().getCapacity()));

		budgetValueLabel.setForeground(DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
		FinancePanelUtil.applyRevenueColor(selectedRevenueValueLabel);
		FinancePanelUtil.applyExpenseColor(selectedExpenseValueLabel);
		FinancePanelUtil.applyAmountColor(selectedNetValueLabel, selectedRevenue - selectedExpense);
		luxuryTaxValueLabel.setForeground(DashboardPanelUtil.EXPENSE_COLOR);
		FinancePanelUtil.applyPolicyColor(profileValueLabel, profileValueLabel.getText());
		FinancePanelUtil.applyMarketColor(marketValueLabel, marketValueLabel.getText());
		FinancePanelUtil.applyStrategyColor(strategyValueLabel, strategyValueLabel.getText());
		ticketPriceValueLabel.setForeground(DashboardPanelUtil.REVENUE_COLOR);
		capacityValueLabel.setForeground(DashboardPanelUtil.POLICY_BALANCED_COLOR);

		rebuildIncomeBreakdown(team, month);
		rebuildExpenseBreakdown(team, month);
		rebuildHistoryDataset(team);
	}

	private void rebuildIncomeBreakdown(Team team, int month) {
		revenueDataset.clear();
		revenueMetricsPanel.removeAll();
		Map<String, Income> incomes = FinancePanelUtil.getIncomeMap(team, month);
		double total = FinancePanelUtil.sumIncomeMap(incomes);
		revenueMetricsPanel.add(FinancePanelUtil.buildListMetricRow("Total revenus", FinancePanelUtil.formatMoney(total),
				DashboardPanelUtil.REVENUE_COLOR));
		revenueMetricsPanel.add(Box.createVerticalStrut(8));
		revenueMetricsPanel.add(FinancePanelUtil.buildListMetricRow("Local",
				FinancePanelUtil.formatMoney(FinancePanelUtil.sumLocalIncomeMap(incomes)), DashboardPanelUtil.REVENUE_COLOR));

		if (incomes != null && !incomes.isEmpty()) {
			for (Income income : incomes.values()) {
				revenueDataset.addValue(income.getAmount(), "Revenus", FinancePanelUtil.prettifyEnum(income.getName()));
			}
		} else {
			revenueDataset.addValue(0.0, "Revenus", "Aucun");
		}

		revenueMetricsPanel.revalidate();
		revenueMetricsPanel.repaint();
	}

	private void rebuildExpenseBreakdown(Team team, int month) {
		expenseDataset.clear();
		expenseMetricsPanel.removeAll();
		Map<String, Expense> expenses = FinancePanelUtil.getExpenseMap(team, month);
		double total = FinancePanelUtil.sumExpenseMap(expenses);
		expenseMetricsPanel.add(FinancePanelUtil.buildListMetricRow("Total depenses", FinancePanelUtil.formatMoney(total),
				DashboardPanelUtil.EXPENSE_COLOR));
		expenseMetricsPanel.add(Box.createVerticalStrut(8));
		expenseMetricsPanel.add(FinancePanelUtil.buildListMetricRow("Net du mois",
				FinancePanelUtil.formatMoney(FinancePanelUtil.sumIncomeMap(FinancePanelUtil.getIncomeMap(team, month)) - total),
				DashboardPanelUtil.getValueColorForAmount(FinancePanelUtil.sumIncomeMap(FinancePanelUtil.getIncomeMap(team, month)) - total)));

		if (expenses != null && !expenses.isEmpty()) {
			for (Expense expense : expenses.values()) {
				expenseDataset.addValue(expense.getAmount(), "Depenses", FinancePanelUtil.prettifyEnum(expense.getName()));
			}
		} else {
			expenseDataset.addValue(0.0, "Depenses", "Aucune");
		}

		expenseMetricsPanel.revalidate();
		expenseMetricsPanel.repaint();
	}

	private void rebuildHistoryDataset(Team team) {
		historyDataset.clear();
		for (int month = 1; month <= FinancePanelUtil.getLastVisibleFinanceMonth(guiInterface); month++) {
			double totalIncome = FinancePanelUtil.sumIncomeMap(FinancePanelUtil.getIncomeMap(team, month));
			double totalExpense = FinancePanelUtil.sumExpenseMap(FinancePanelUtil.getExpenseMap(team, month));
			historyDataset.addValue(totalIncome, "Revenus", FinancePanelUtil.monthLabel(month));
			historyDataset.addValue(totalExpense, "Depenses", FinancePanelUtil.monthLabel(month));
		}
	}

	private void populateTeamsIfNeeded() {
		if (teamSelector.getItemCount() == guiInterface.getTeams().size()) {
			return;
		}
		updatingSelectors = true;
		String selected = teamSelector.getItemCount() > 0 ? (String) teamSelector.getSelectedItem() : null;
		teamSelector.removeAllItems();
		for (Team team : guiInterface.getTeams()) {
			teamSelector.addItem(team.getName());
		}
		if (selected != null) {
			teamSelector.setSelectedItem(selected);
		}
		if (teamSelector.getSelectedIndex() < 0 && teamSelector.getItemCount() > 0) {
			teamSelector.setSelectedIndex(0);
		}
		updatingSelectors = false;
	}

	private void resetView() {
		budgetValueLabel.setText("-");
		selectedRevenueValueLabel.setText("-");
		selectedExpenseValueLabel.setText("-");
		selectedNetValueLabel.setText("-");
		luxuryTaxValueLabel.setText("-");
		profileValueLabel.setText("-");
		marketValueLabel.setText("-");
		strategyValueLabel.setText("-");
		ticketPriceValueLabel.setText("-");
		capacityValueLabel.setText("-");
		budgetValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		selectedRevenueValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		selectedExpenseValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		selectedNetValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		luxuryTaxValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		profileValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		marketValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		strategyValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		ticketPriceValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		capacityValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		revenueDataset.clear();
		expenseDataset.clear();
		historyDataset.clear();
	}

	private double getLuxuryTaxPaid(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return 0.0;
		}
		return team.getTeamFinance().getLuxuryTaxPaid();
	}

	private Object getFinancialPolicy(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getFinancialProfil();
	}

	private Object getMarketSize(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getMarketSize();
	}

	private Object getTransferStrategy(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getTeamTransferStrategy();
	}

	private class TeamSelectionAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!updatingSelectors) {
				refreshData();
			}
		}
	}

	private class MonthSelectionAction implements Runnable {
		@Override
		public void run() {
			if (!updatingSelectors) {
				refreshData();
			}
		}
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		FinancePanelUtil.styleComboBox(teamSelector);
		monthSelector.applyTheme();
		FinancePanelUtil.applyThemeToCharts(this);
	}
}

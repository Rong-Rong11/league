package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.Box;
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
		FinanceViewFactory.styleComboBox(teamSelector);
		budgetValueLabel = FinanceViewFactory.metricLabel();
		selectedRevenueValueLabel = FinanceViewFactory.metricLabel();
		selectedExpenseValueLabel = FinanceViewFactory.metricLabel();
		selectedNetValueLabel = FinanceViewFactory.metricLabel();
		luxuryTaxValueLabel = FinanceViewFactory.metricLabel();
		profileValueLabel = FinanceViewFactory.infoLabel();
		marketValueLabel = FinanceViewFactory.infoLabel();
		strategyValueLabel = FinanceViewFactory.infoLabel();
		ticketPriceValueLabel = FinanceViewFactory.infoLabel();
		capacityValueLabel = FinanceViewFactory.infoLabel();
		revenueMetricsPanel = FinanceViewFactory.metricListPanel();
		expenseMetricsPanel = FinanceViewFactory.metricListPanel();
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
		centerColumn.add(FinanceViewFactory.panelWithHeight(
				new BuildBox("HISTORIQUE MENSUEL", "Revenus et depenses",
						FinanceViewFactory.financeLineChart(historyDataset, DashboardPanelUtil.REVENUE_COLOR)),
				250), BorderLayout.NORTH);

		JPanel bottomRow = new JPanel(new GridLayout(1, 2, DASHBOARD_SPACING, 0));
		bottomRow.setOpaque(false);
		bottomRow.add(new BuildBox("REVENUS", "Mois selectionne", buildRevenuePanel()));
		bottomRow.add(new BuildBox("DEPENSES", "Mois selectionne", buildExpensePanel()));
		centerColumn.add(FinanceViewFactory.panelWithHeight(bottomRow, 280), BorderLayout.CENTER);
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
		summaryPanel.add(FinanceViewFactory.metricCard("Equipe", teamSelector));
		summaryPanel.add(FinanceViewFactory.metricCard("Budget restant", budgetValueLabel));
		summaryPanel.add(FinanceViewFactory.metricCard("Revenus mois", selectedRevenueValueLabel));
		summaryPanel.add(FinanceViewFactory.metricCard("Net mois", selectedNetValueLabel));
		summaryPanel.add(FinanceViewFactory.metricCard("Mois", monthSelector));
		return summaryPanel;
	}

	private JPanel buildProfilePanel() {
		JPanel panel = FinanceViewFactory.infoPanel();
		panel.add(FinanceViewFactory.infoRow("Politique financiere", profileValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Taille du marche", marketValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Strategie", strategyValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Taxe de luxe payee", luxuryTaxValueLabel));
		return panel;
	}

	private JPanel buildRevenuePanel() {
		JPanel panel = new JPanel(new BorderLayout(0, DASHBOARD_SPACING));
		panel.setOpaque(false);
		panel.add(revenueMetricsPanel, BorderLayout.NORTH);
		panel.add(FinanceViewFactory.financeBarChart(revenueDataset, DashboardPanelUtil.REVENUE_COLOR),
				BorderLayout.CENTER);
		return panel;
	}

	private JPanel buildExpensePanel() {
		JPanel panel = new JPanel(new BorderLayout(0, DASHBOARD_SPACING));
		panel.setOpaque(false);
		panel.add(expenseMetricsPanel, BorderLayout.NORTH);
		panel.add(FinanceViewFactory.financeBarChart(expenseDataset, DashboardPanelUtil.EXPENSE_COLOR),
				BorderLayout.CENTER);
		return panel;
	}

	private JPanel buildInfrastructurePanel() {
		JPanel panel = FinanceViewFactory.infoPanel();
		panel.add(FinanceViewFactory.infoRow("Capacite", capacityValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Prix billet", ticketPriceValueLabel));
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
		Team team = FinanceDataUtil.selectedTeam(guiInterface, teamSelector, 0);
		if (team == null) {
			resetView();
			return;
		}

		int month = FinanceDataUtil.selectedMonth(monthSelector);
		Budget budget = FinanceDataUtil.teamBudget(team);
		updatingSelectors = true;
		FinanceDataUtil.setAvailableMonths(monthSelector, FinanceDataUtil.availableMonths(guiInterface, budget));
		updatingSelectors = false;
		month = FinanceDataUtil.selectedMonth(monthSelector);

		budgetValueLabel.setText(budget == null ? "-" : FinanceDataUtil.formatMoney(budget.getRemainingAmount()));
		double selectedRevenue = FinanceDataUtil.totalIncome(FinanceDataUtil.teamIncomes(team, month));
		double selectedExpense = FinanceDataUtil.totalExpense(FinanceDataUtil.teamExpenses(team, month));
		selectedRevenueValueLabel.setText(FinanceDataUtil.formatMoney(selectedRevenue));
		selectedExpenseValueLabel.setText(FinanceDataUtil.formatMoney(selectedExpense));
		selectedNetValueLabel.setText(FinanceDataUtil.formatMoney(selectedRevenue - selectedExpense));
		luxuryTaxValueLabel.setText(FinanceDataUtil.formatMoney(getLuxuryTaxPaid(team)));
		profileValueLabel.setText(FinanceDataUtil.formatPolicy(getFinancialPolicy(team)));
		marketValueLabel.setText(FinanceDataUtil.formatMarket(getMarketSize(team)));
		strategyValueLabel.setText(FinanceDataUtil.formatStrategy(getTransferStrategy(team)));
		ticketPriceValueLabel.setText(team.getStadium() == null ? "-" : FinanceDataUtil.formatMoney(team.getStadium().getTicketPrice()));
		capacityValueLabel.setText(team.getStadium() == null ? "-" : String.valueOf(team.getStadium().getCapacity()));

		budgetValueLabel.setForeground(DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
		FinanceDataUtil.setRevenueColor(selectedRevenueValueLabel);
		FinanceDataUtil.setExpenseColor(selectedExpenseValueLabel);
		FinanceDataUtil.setAmountColor(selectedNetValueLabel, selectedRevenue - selectedExpense);
		luxuryTaxValueLabel.setForeground(DashboardPanelUtil.EXPENSE_COLOR);
		FinanceDataUtil.setPolicyColor(profileValueLabel, profileValueLabel.getText());
		FinanceDataUtil.setMarketColor(marketValueLabel, marketValueLabel.getText());
		FinanceDataUtil.setStrategyColor(strategyValueLabel, strategyValueLabel.getText());
		ticketPriceValueLabel.setForeground(DashboardPanelUtil.REVENUE_COLOR);
		capacityValueLabel.setForeground(DashboardPanelUtil.POLICY_BALANCED_COLOR);

		rebuildIncomeBreakdown(team, month);
		rebuildExpenseBreakdown(team, month);
		rebuildHistoryDataset(team);
	}

	private void rebuildIncomeBreakdown(Team team, int month) {
		revenueDataset.clear();
		revenueMetricsPanel.removeAll();
		Map<String, Income> incomes = FinanceDataUtil.teamIncomes(team, month);
		double total = FinanceDataUtil.totalIncome(incomes);
		revenueMetricsPanel.add(FinanceViewFactory.valueRow("Total revenus", FinanceDataUtil.formatMoney(total),
				DashboardPanelUtil.REVENUE_COLOR));
		revenueMetricsPanel.add(Box.createVerticalStrut(8));
		revenueMetricsPanel.add(FinanceViewFactory.valueRow("Revenus locaux",
				FinanceDataUtil.formatMoney(FinanceDataUtil.totalLocalIncome(incomes)), DashboardPanelUtil.REVENUE_COLOR));

		if (incomes != null && !incomes.isEmpty()) {
			for (Income income : incomes.values()) {
				revenueDataset.addValue(income.getAmount(), "Revenus", FinanceDataUtil.formatTypeName(income.getName()));
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
		Map<String, Expense> expenses = FinanceDataUtil.teamExpenses(team, month);
		double total = FinanceDataUtil.totalExpense(expenses);
		expenseMetricsPanel.add(FinanceViewFactory.valueRow("Total depenses", FinanceDataUtil.formatMoney(total),
				DashboardPanelUtil.EXPENSE_COLOR));
		expenseMetricsPanel.add(Box.createVerticalStrut(8));
		expenseMetricsPanel.add(FinanceViewFactory.valueRow("Net du mois",
				FinanceDataUtil.formatMoney(FinanceDataUtil.totalIncome(FinanceDataUtil.teamIncomes(team, month)) - total),
				DashboardPanelUtil.getValueColorForAmount(FinanceDataUtil.totalIncome(FinanceDataUtil.teamIncomes(team, month)) - total)));

		if (expenses != null && !expenses.isEmpty()) {
			for (Expense expense : expenses.values()) {
				expenseDataset.addValue(expense.getAmount(), "Depenses", FinanceDataUtil.formatTypeName(expense.getName()));
			}
		} else {
			expenseDataset.addValue(0.0, "Depenses", "Aucune");
		}

		expenseMetricsPanel.revalidate();
		expenseMetricsPanel.repaint();
	}

	private void rebuildHistoryDataset(Team team) {
		historyDataset.clear();
		for (int month = 1; month <= FinanceDataUtil.lastVisibleMonth(guiInterface); month++) {
			double totalIncome = FinanceDataUtil.totalIncome(FinanceDataUtil.teamIncomes(team, month));
			double totalExpense = FinanceDataUtil.totalExpense(FinanceDataUtil.teamExpenses(team, month));
			historyDataset.addValue(totalIncome, "Revenus", FinanceDataUtil.monthLabel(month));
			historyDataset.addValue(totalExpense, "Depenses", FinanceDataUtil.monthLabel(month));
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
		FinanceViewFactory.styleComboBox(teamSelector);
		monthSelector.applyTheme();
		FinanceViewFactory.refreshCharts(this);
	}
}

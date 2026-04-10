package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
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
import process.orchestrator.GUIInterface;

public class TeamFinanceViewPanel extends AbstractFinanceViewPanel {

	private boolean updatingSelectors;
	private final JComboBox<String> teamSelector;
	private final MonthNavigator monthSelector;
	private final JLabel budgetValueLabel;
	private final JLabel payrollValueLabel;
	private final JLabel teamValueLabel;
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
		super(guiInterface);
		teamSelector = new JComboBox<String>();
		monthSelector = buildMonthNavigator();
		styleComboBox(teamSelector);
		budgetValueLabel = createMetricValueLabel();
		payrollValueLabel = createMetricValueLabel();
		teamValueLabel = createMetricValueLabel();
		luxuryTaxValueLabel = createMetricValueLabel();
		profileValueLabel = createBodyValueLabel();
		marketValueLabel = createBodyValueLabel();
		strategyValueLabel = createBodyValueLabel();
		ticketPriceValueLabel = createBodyValueLabel();
		capacityValueLabel = createBodyValueLabel();
		revenueMetricsPanel = createMetricListPanel();
		expenseMetricsPanel = createMetricListPanel();
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
		centerColumn.add(withPreferredHeight(
				new BuildBox("HISTORIQUE MENSUEL", "Revenus vs depenses",
						buildLineChartPanel(historyDataset, "Historique equipe", "Montant (M$)", new Color(0x17, 0x31, 0x74))),
				250), BorderLayout.NORTH);

		JPanel bottomRow = new JPanel(new GridLayout(1, 2, DASHBOARD_SPACING, 0));
		bottomRow.setOpaque(false);
		bottomRow.add(new BuildBox("REVENUS", "Mois selectionne", buildRevenuePanel()));
		bottomRow.add(new BuildBox("DEPENSES", "Mois selectionne", buildExpensePanel()));
		centerColumn.add(withPreferredHeight(bottomRow, 280), BorderLayout.CENTER);
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
		summaryPanel.add(buildMetricCard("Equipe", teamSelector));
		summaryPanel.add(buildMetricCard("Budget restant", budgetValueLabel));
		summaryPanel.add(buildMetricCard("Payroll", payrollValueLabel));
		summaryPanel.add(buildMetricCard("Team value", teamValueLabel));
		summaryPanel.add(buildMetricCard("Mois", monthSelector));
		return summaryPanel;
	}

	private JPanel buildProfilePanel() {
		JPanel panel = createSectionContentPanel();
		panel.add(buildTextMetricRow("Financial policy", profileValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildTextMetricRow("Market size", marketValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildTextMetricRow("Strategy", strategyValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildTextMetricRow("Luxury tax paid", luxuryTaxValueLabel));
		return panel;
	}

	private JPanel buildRevenuePanel() {
		JPanel panel = new JPanel(new BorderLayout(0, DASHBOARD_SPACING));
		panel.setOpaque(false);
		panel.add(revenueMetricsPanel, BorderLayout.NORTH);
		panel.add(buildBarChartPanel(revenueDataset, "Revenus equipe", "Type", "Montant (M$)", new Color(0x1E, 0x88, 0x6E)),
				BorderLayout.CENTER);
		return panel;
	}

	private JPanel buildExpensePanel() {
		JPanel panel = new JPanel(new BorderLayout(0, DASHBOARD_SPACING));
		panel.setOpaque(false);
		panel.add(expenseMetricsPanel, BorderLayout.NORTH);
		panel.add(buildBarChartPanel(expenseDataset, "Depenses equipe", "Type", "Montant (M$)", new Color(0xC8, 0x54, 0x54)),
				BorderLayout.CENTER);
		return panel;
	}

	private JPanel buildInfrastructurePanel() {
		JPanel panel = createSectionContentPanel();
		panel.add(buildTextMetricRow("Capacite", capacityValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildTextMetricRow("Prix billet", ticketPriceValueLabel));
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
		teamSelector.addActionListener(e -> {
			if (!updatingSelectors) {
				refreshData();
			}
		});
		monthSelector.setChangeListener(() -> {
			if (!updatingSelectors) {
				refreshData();
			}
		});
	}

	public void refreshData() {
		populateTeamsIfNeeded();
		Team team = getSelectedTeam(teamSelector, 0);
		if (team == null) {
			resetView();
			return;
		}

		int month = getSelectedMonth(monthSelector);
		Budget budget = getBudget(team);
		updatingSelectors = true;
		setMonthSelectorOptions(monthSelector, getAvailableMonths(budget));
		updatingSelectors = false;
		month = getSelectedMonth(monthSelector);

		budgetValueLabel.setText(budget == null ? "-" : formatMoney(budget.getRemainingAmount()));
		payrollValueLabel.setText(formatMoney(getCurrentPayroll(team)));
		teamValueLabel.setText(formatMoney(getTeamValue(team)));
		luxuryTaxValueLabel.setText(formatMoney(getLuxuryTaxPaid(team)));
		profileValueLabel.setText(prettifyObjectName(getFinancialPolicy(team)));
		marketValueLabel.setText(prettifyObjectName(getMarketSize(team)));
		strategyValueLabel.setText(prettifyObjectName(getTransferStrategy(team)));
		ticketPriceValueLabel.setText(team.getStadium() == null ? "-" : formatMoney(team.getStadium().getTicketPrice()));
		capacityValueLabel.setText(team.getStadium() == null ? "-" : String.valueOf(team.getStadium().getCapacity()));

		rebuildIncomeBreakdown(team, month);
		rebuildExpenseBreakdown(team, month);
		rebuildHistoryDataset(team);
	}

	private void rebuildIncomeBreakdown(Team team, int month) {
		revenueDataset.clear();
		revenueMetricsPanel.removeAll();
		Map<String, Income> incomes = getIncomeMap(team, month);
		double total = sumIncomeMap(incomes);
		revenueMetricsPanel.add(buildListMetricRow("Total revenus", formatMoney(total)));
		revenueMetricsPanel.add(Box.createVerticalStrut(8));
		revenueMetricsPanel.add(buildListMetricRow("Local", formatMoney(sumLocalIncomeMap(incomes))));

		if (incomes != null && !incomes.isEmpty()) {
			for (Income income : incomes.values()) {
				revenueDataset.addValue(income.getAmount(), "Revenus", prettifyEnum(income.getName()));
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
		Map<String, Expense> expenses = getExpenseMap(team, month);
		double total = sumExpenseMap(expenses);
		expenseMetricsPanel.add(buildListMetricRow("Total depenses", formatMoney(total)));
		expenseMetricsPanel.add(Box.createVerticalStrut(8));
		expenseMetricsPanel.add(buildListMetricRow("Payroll", formatMoney(getCurrentPayroll(team))));

		if (expenses != null && !expenses.isEmpty()) {
			for (Expense expense : expenses.values()) {
				expenseDataset.addValue(expense.getAmount(), "Depenses", prettifyEnum(expense.getName()));
			}
		} else {
			expenseDataset.addValue(0.0, "Depenses", "Aucune");
		}

		expenseMetricsPanel.revalidate();
		expenseMetricsPanel.repaint();
	}

	private void rebuildHistoryDataset(Team team) {
		historyDataset.clear();
		for (int month = 1; month <= getLastVisibleFinanceMonth(); month++) {
			double totalIncome = sumIncomeMap(getIncomeMap(team, month));
			double totalExpense = sumExpenseMap(getExpenseMap(team, month));
			historyDataset.addValue(totalIncome, "Revenus", monthLabel(month));
			historyDataset.addValue(totalExpense, "Depenses", monthLabel(month));
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
		payrollValueLabel.setText("-");
		teamValueLabel.setText("-");
		luxuryTaxValueLabel.setText("-");
		profileValueLabel.setText("-");
		marketValueLabel.setText("-");
		strategyValueLabel.setText("-");
		ticketPriceValueLabel.setText("-");
		capacityValueLabel.setText("-");
		revenueDataset.clear();
		expenseDataset.clear();
		historyDataset.clear();
	}
}

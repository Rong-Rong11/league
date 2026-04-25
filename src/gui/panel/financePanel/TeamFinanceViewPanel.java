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
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.MonthNavigatorPanel;
import gui.panel.common.ThemeAware;
import process.orchestrator.interf.GUIInterface;

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
		if (!guiInterface.isSeasonInitialized()) {
			showSeasonNotStartedState();
			return;
		}
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
		double selectedNet = FinanceDataUtil.teamMonthNet(guiInterface, team, month);
		selectedRevenueValueLabel.setText(FinanceDataUtil.formatMoney(selectedRevenue));
		selectedExpenseValueLabel.setText(FinanceDataUtil.formatMoney(selectedExpense));
		selectedNetValueLabel.setText(FinanceDataUtil.formatMoney(selectedNet));
		luxuryTaxValueLabel.setText(FinanceDataUtil.formatMoney(getLuxuryTaxPaid(team)));
		profileValueLabel.setText(FinanceDataUtil.formatPolicy(getFinancialPolicy(team)));
		marketValueLabel.setText(FinanceDataUtil.formatMarket(getMarketSize(team)));
		strategyValueLabel.setText(FinanceDataUtil.formatStrategy(getTransferStrategy(team)));
		ticketPriceValueLabel
				.setText(team.getStadium() == null ? "-" : FinanceDataUtil.formatMoney(team.getStadium().getTicketPrice()));
		capacityValueLabel.setText(team.getStadium() == null ? "-" : String.valueOf(team.getStadium().getCapacity()));

		budgetValueLabel.setForeground(DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
		FinanceDataUtil.setRevenueColor(selectedRevenueValueLabel);
		FinanceDataUtil.setExpenseColor(selectedExpenseValueLabel);
		FinanceDataUtil.setAmountColor(selectedNetValueLabel, selectedNet);
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
		double monthNet = FinanceDataUtil.teamMonthNet(guiInterface, team, month);
		expenseMetricsPanel.add(FinanceViewFactory.valueRow("Net du mois",
				FinanceDataUtil.formatMoney(monthNet),
				DashboardPanelUtil.getValueColorForAmount(monthNet)));

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
		budgetValueLabel.setText("Aucune equipe n'est disponible.");
		selectedRevenueValueLabel.setText("Aucun revenu mensuel n'est disponible.");
		selectedExpenseValueLabel.setText("Aucune depense mensuelle n'est disponible.");
		selectedNetValueLabel.setText("Aucun resultat mensuel n'est disponible.");
		luxuryTaxValueLabel.setText("Aucune taxe de luxe n'est disponible.");
		profileValueLabel.setText("Aucun profil financier n'est disponible.");
		marketValueLabel.setText("Aucune taille de marche n'est disponible.");
		strategyValueLabel.setText("Aucune strategie n'est disponible.");
		ticketPriceValueLabel.setText("Aucun prix de billet n'est disponible.");
		capacityValueLabel.setText("Aucune capacite n'est disponible.");
		applyEmptyStateLabels();
		revenueDataset.clear();
		expenseDataset.clear();
		historyDataset.clear();
		revenueMetricsPanel.removeAll();
		expenseMetricsPanel.removeAll();
		revenueMetricsPanel.add(buildEmptyStateMessage("Aucune repartition de revenus n'est disponible."));
		expenseMetricsPanel.add(buildEmptyStateMessage("Aucune repartition de depenses n'est disponible."));
		revenueMetricsPanel.revalidate();
		expenseMetricsPanel.revalidate();
	}

	public void showSeasonNotStartedState() {
		budgetValueLabel.setText("La saison n'a pas encore commence.");
		selectedRevenueValueLabel.setText("Les revenus mensuels apparaitront apres le lancement.");
		selectedExpenseValueLabel.setText("Les depenses mensuelles apparaitront apres le lancement.");
		selectedNetValueLabel.setText("Le resultat mensuel sera calcule apres le lancement.");
		luxuryTaxValueLabel.setText("La taxe de luxe sera calculee apres le lancement.");
		profileValueLabel.setText("Le profil financier sera affiche apres le lancement.");
		marketValueLabel.setText("La taille du marche sera affichee apres le lancement.");
		strategyValueLabel.setText("La strategie sera affichee apres le lancement.");
		ticketPriceValueLabel.setText("Le prix du billet sera affiche apres le lancement.");
		capacityValueLabel.setText("La capacite sera affichee apres le lancement.");
		applyEmptyStateLabels();
		revenueDataset.clear();
		expenseDataset.clear();
		historyDataset.clear();
		revenueMetricsPanel.removeAll();
		expenseMetricsPanel.removeAll();
		revenueMetricsPanel.add(buildEmptyStateMessage("Lancez la saison pour afficher les revenus de l'equipe."));
		expenseMetricsPanel.add(buildEmptyStateMessage("Lancez la saison pour afficher les depenses de l'equipe."));
		revenueMetricsPanel.revalidate();
		expenseMetricsPanel.revalidate();
	}

	private void applyEmptyStateLabels() {
		applyEmptyStateLabel(budgetValueLabel, 12);
		applyEmptyStateLabel(selectedRevenueValueLabel, 12);
		applyEmptyStateLabel(selectedExpenseValueLabel, 12);
		applyEmptyStateLabel(selectedNetValueLabel, 12);
		applyEmptyStateLabel(luxuryTaxValueLabel, 12);
		applyEmptyStateLabel(profileValueLabel, 12);
		applyEmptyStateLabel(marketValueLabel, 12);
		applyEmptyStateLabel(strategyValueLabel, 12);
		applyEmptyStateLabel(ticketPriceValueLabel, 12);
		applyEmptyStateLabel(capacityValueLabel, 12);
	}

	private void applyEmptyStateLabel(JLabel label, int fontSize) {
		LabelStyleUtil.styleSubtitleLabel(label, fontSize);
	}

	private JLabel buildEmptyStateMessage(String text) {
		JLabel label = new JLabel(text);
		LabelStyleUtil.styleSubtitleLabel(label, 12);
		return label;
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
		return team.getTeamFinance().getBehavior().getFinancialProfil();
	}

	private Object getMarketSize(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getStructure().getMarketSize();
	}

	private Object getTransferStrategy(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getBehavior().getTeamTransferStrategy();
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

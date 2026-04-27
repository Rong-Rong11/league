package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.finance.budget.income.Income;
import data.team.Team;
import gui.panel.common.BuildBox;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.MonthNavigatorPanel;
import gui.panel.common.ThemeAware;
import process.orchestrator.interfaces.GUIInterface;

public class TeamFinanceViewPanel extends JPanel implements ThemeAware {

	static final int DASHBOARD_SPACING = 10;
	static final int RIGHT_COLUMN_WIDTH = 280;
	static final int RIGHT_COLUMN_SECTION_SPACING = 10;

	private boolean updatingSelectors;
	private final GUIInterface guiInterface;
	private final JComboBox<String> teamSelector;
	private final MonthNavigatorPanel monthSelector;
	private final JLabel budgetValueLabel;
	private final JLabel selectedRevenueValueLabel;
	private final JLabel selectedExpenseValueLabel;
	private final JLabel selectedNetValueLabel;
	private final TeamFinanceProfilePanel profilePanel;
	private final TeamFinanceBreakdownPanel revenuePanel;
	private final TeamFinanceBreakdownPanel expensePanel;
	private final org.jfree.data.category.DefaultCategoryDataset historyDataset;

	public TeamFinanceViewPanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		teamSelector = new JComboBox<String>();
		monthSelector = new MonthNavigatorPanel();
		FinanceViewFactory.styleComboBox(teamSelector);
		budgetValueLabel = FinanceViewFactory.metricLabel();
		selectedRevenueValueLabel = FinanceViewFactory.metricLabel();
		selectedExpenseValueLabel = FinanceViewFactory.metricLabel();
		selectedNetValueLabel = FinanceViewFactory.metricLabel();
		profilePanel = new TeamFinanceProfilePanel();
		revenuePanel = new TeamFinanceBreakdownPanel("Revenus", "Aucun", DashboardPanelUtil.REVENUE_COLOR);
		expensePanel = new TeamFinanceBreakdownPanel("Depenses", "Aucune", DashboardPanelUtil.EXPENSE_COLOR);
		historyDataset = new org.jfree.data.category.DefaultCategoryDataset();

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
		bottomRow.add(new BuildBox("REVENUS", "Mois selectionne", revenuePanel));
		bottomRow.add(new BuildBox("DEPENSES", "Mois selectionne", expensePanel));
		centerColumn.add(FinanceViewFactory.panelWithHeight(bottomRow, 280), BorderLayout.CENTER);
		return centerColumn;
	}

	private JPanel buildRightColumn() {
		JPanel column = DashboardPanelUtil.createRightColumn(RIGHT_COLUMN_WIDTH, 12);
		column.add(new BuildBox("INFOS STABLES", "Profil et infrastructure", profilePanel), BorderLayout.NORTH);
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
		profilePanel.updateForTeam(team, this);

		budgetValueLabel.setForeground(DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
		FinanceDataUtil.setRevenueColor(selectedRevenueValueLabel);
		FinanceDataUtil.setExpenseColor(selectedExpenseValueLabel);
		FinanceDataUtil.setAmountColor(selectedNetValueLabel, selectedNet);

		rebuildIncomeBreakdown(team, month);
		rebuildExpenseBreakdown(team, month);
		rebuildHistoryDataset(team);
	}

	private void rebuildIncomeBreakdown(Team team, int month) {
		Map<String, Income> incomes = FinanceDataUtil.teamIncomes(team, month);
		revenuePanel.showIncomeBreakdown(incomes);
	}

	private void rebuildExpenseBreakdown(Team team, int month) {
		Map<String, Expense> expenses = FinanceDataUtil.teamExpenses(team, month);
		double monthNet = FinanceDataUtil.teamMonthNet(guiInterface, team, month);
		expensePanel.showExpenseBreakdown(expenses, monthNet);
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
		profilePanel.showEmptyState(new String[] {
				"Aucun profil financier n'est disponible.",
				"Aucune taille de marche n'est disponible.",
				"Aucune strategie n'est disponible.",
				"Aucun prix de billet n'est disponible.",
				"Aucune capacite n'est disponible.",
				"Aucune fidelite des fans n'est disponible.",
				"Aucune agressivite commerciale n'est disponible.",
				"Aucun prestige historique n'est disponible.",
				"Aucune base de fans n'est disponible.",
				"Aucune opportunite commerciale n'est disponible.",
				"Aucun pouvoir de prix n'est disponible.",
				"Aucune taxe de luxe n'est disponible."
		});
		applyEmptyStateLabels();
		historyDataset.clear();
		revenuePanel.showEmptyState("Aucune repartition de revenus n'est disponible.");
		expensePanel.showEmptyState("Aucune repartition de depenses n'est disponible.");
	}

	public void showSeasonNotStartedState() {
		budgetValueLabel.setText("La saison n'a pas encore commence.");
		selectedRevenueValueLabel.setText("Les revenus mensuels apparaitront apres le lancement.");
		selectedExpenseValueLabel.setText("Les depenses mensuelles apparaitront apres le lancement.");
		selectedNetValueLabel.setText("Le resultat mensuel sera calcule apres le lancement.");
		profilePanel.showEmptyState(new String[] {
				"Le profil financier sera affiche apres le lancement.",
				"La taille du marche sera affichee apres le lancement.",
				"La strategie sera affichee apres le lancement.",
				"Le prix du billet sera affiche apres le lancement.",
				"La capacite sera affichee apres le lancement.",
				"Aucune fidelite des fans n'est disponible.",
				"Aucune agressivite commerciale n'est disponible.",
				"Aucun prestige historique n'est disponible.",
				"Aucune base de fans n'est disponible.",
				"Aucune opportunite commerciale n'est disponible.",
				"Aucun pouvoir de prix n'est disponible.",
				"La taxe de luxe sera calculee apres le lancement."
		});
		applyEmptyStateLabels();
		historyDataset.clear();
		revenuePanel.showEmptyState("Lancez la saison pour afficher les revenus de l'equipe.");
		expensePanel.showEmptyState("Lancez la saison pour afficher les depenses de l'equipe.");
	}

	private void applyEmptyStateLabels() {
		applyEmptyStateLabel(budgetValueLabel, 12);
		applyEmptyStateLabel(selectedRevenueValueLabel, 12);
		applyEmptyStateLabel(selectedExpenseValueLabel, 12);
		applyEmptyStateLabel(selectedNetValueLabel, 12);
	}

	private void applyEmptyStateLabel(JLabel label, int fontSize) {
		LabelStyleUtil.styleSubtitleLabel(label, fontSize);
	}

	double getLuxuryTaxPaid(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return 0.0;
		}
		return team.getTeamFinance().getLuxuryTaxPaid();
	}

	Object getFinancialPolicy(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getBehavior().getFinancialPolicy();
	}

	Object getMarketSize(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getStructure().getMarketSize();
	}

	Object getTransferStrategy(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getBehavior().getTeamTransferStrategy();
	}

	double getFanLoyalty(Team team) {
		if (team == null || team.getTeamFinance() == null)
			return 0.0;
		return team.getTeamFinance().getStructure().getEconomicProfile().getFanLoyalty();
	}

	double getCommercialAggressiveness(Team team) {
		if (team == null || team.getTeamFinance() == null)
			return 0.0;
		return team.getTeamFinance().getStructure().getEconomicProfile().getCommercialAggressiveness();
	}

	double getHistoricalPrestige(Team team) {
		if (team == null || team.getTeamFinance() == null)
			return 0.0;
		return team.getTeamFinance().getStructure().getEconomicProfile().getHistoricalPrestige();
	}

	String formatCoefficient(double value) {
		String level;

		if (value < 0.30) {
			level = "Faible";
		} else if (value < 0.60) {
			level = "Moyen";
		} else if (value < 0.80) {
			level = "Eleve";
		} else {
			level = "Tres eleve";
		}

		return level + " (" + Math.round(value * 100) + "%)";
	}

	double getFanBase(Team team) {
		if (team == null || team.getTeamFinance() == null)
			return 0.0;
		return team.getTeamFinance().getStructure().getMediaMarket().getFanBaseModifier();
	}

	double getBusinessOpportunity(Team team) {
		if (team == null || team.getTeamFinance() == null)
			return 0.0;
		return team.getTeamFinance().getStructure().getMediaMarket().getBusinessOpportunityModifier();
	}

	double getPricingPower(Team team) {
		if (team == null || team.getTeamFinance() == null)
			return 0.0;
		return team.getTeamFinance().getStructure().getMediaMarket().getPricingPowerModifier();
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

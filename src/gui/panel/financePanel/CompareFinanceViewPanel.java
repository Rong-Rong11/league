package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.data.category.DefaultCategoryDataset;

import data.team.Team;
import gui.panel.common.BuildBox;
import gui.panel.common.DashboardPanelUtil;
import process.orchestrator.GUIInterface;

public class CompareFinanceViewPanel extends AbstractFinanceViewPanel {

	private boolean updatingSelectors;
	private final JComboBox<String> teamASelector;
	private final JComboBox<String> teamBSelector;
	private final MonthNavigator monthSelector;
	private final JLabel budgetAValueLabel;
	private final JLabel payrollAValueLabel;
	private final JLabel valueAValueLabel;
	private final JLabel budgetBValueLabel;
	private final JLabel payrollBValueLabel;
	private final JLabel valueBValueLabel;
	private final JLabel ticketPriceAValueLabel;
	private final JLabel ticketPriceBValueLabel;
	private final JPanel comparisonMetricsPanel;
	private final DefaultCategoryDataset comparisonDataset;

	public CompareFinanceViewPanel(GUIInterface guiInterface) {
		super(guiInterface);
		teamASelector = new JComboBox<String>();
		teamBSelector = new JComboBox<String>();
		monthSelector = buildMonthNavigator();
		styleComboBox(teamASelector);
		styleComboBox(teamBSelector);
		budgetAValueLabel = createMetricValueLabel();
		payrollAValueLabel = createMetricValueLabel();
		valueAValueLabel = createMetricValueLabel();
		budgetBValueLabel = createMetricValueLabel();
		payrollBValueLabel = createMetricValueLabel();
		valueBValueLabel = createMetricValueLabel();
		ticketPriceAValueLabel = createBodyValueLabel();
		ticketPriceBValueLabel = createBodyValueLabel();
		comparisonMetricsPanel = createMetricListPanel();
		comparisonDataset = new DefaultCategoryDataset();

		organize();
		actions();
	}

	private void organize() {
		setLayout(new BorderLayout(0, DASHBOARD_SPACING));
		add(new BuildBox("SELECTION", "Comparer deux equipes", buildSelectionPanel()), BorderLayout.NORTH);
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
				new BuildBox("COMPARATIF GRAPHIQUE", "Budget, payroll, revenus et depenses",
						buildBarChartPanel(comparisonDataset, "Comparatif equipes", "Metrique", "Montant (M$)",
								new Color(0x5D, 0x83, 0xC2))),
				250), BorderLayout.NORTH);
		centerColumn.add(withPreferredHeight(
				new BuildBox("COMPARATIF FINANCIER", "Lecture rapide", comparisonMetricsPanel), 190),
				BorderLayout.CENTER);
		return centerColumn;
	}

	private JPanel buildRightColumn() {
		JPanel column = DashboardPanelUtil.createRightColumn(RIGHT_COLUMN_WIDTH, 12);
		JPanel dynamicCards = new JPanel(new GridLayout(2, 1, 0, 12));
		dynamicCards.setOpaque(false);
		dynamicCards.add(new BuildBox("EQUIPE A", "Resume financier",
				buildTeamCardPanel(budgetAValueLabel, payrollAValueLabel, valueAValueLabel)));
		dynamicCards.add(new BuildBox("EQUIPE B", "Resume financier",
				buildTeamCardPanel(budgetBValueLabel, payrollBValueLabel, valueBValueLabel)));
		column.add(dynamicCards, BorderLayout.CENTER);
		column.add(new BuildBox("INFOS STABLES", "Comparaison infrastructure", buildStaticInfoPanel()), BorderLayout.SOUTH);
		return column;
	}

	private JPanel buildSelectionPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 3, DASHBOARD_SPACING, 0));
		panel.setOpaque(false);
		panel.add(buildMetricCard("Equipe A", teamASelector));
		panel.add(buildMetricCard("Equipe B", teamBSelector));
		panel.add(buildMetricCard("Mois", monthSelector));
		return panel;
	}

	private JPanel buildTeamCardPanel(JLabel budgetLabel, JLabel payrollLabel, JLabel valueLabel) {
		JPanel panel = createSectionContentPanel();
		panel.add(buildTextMetricRow("Budget restant", budgetLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildTextMetricRow("Payroll", payrollLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildTextMetricRow("Team value", valueLabel));
		return panel;
	}

	private JPanel buildStaticInfoPanel() {
		JPanel panel = createSectionContentPanel();
		panel.add(buildTextMetricRow("Ticket price A", ticketPriceAValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(buildTextMetricRow("Ticket price B", ticketPriceBValueLabel));
		return panel;
	}

	private void actions() {
		teamASelector.addActionListener(e -> {
			if (!updatingSelectors) {
				refreshData();
			}
		});
		teamBSelector.addActionListener(e -> {
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
		Team teamA = getSelectedTeam(teamASelector, 0);
		Team teamB = getSelectedTeam(teamBSelector, 1);
		if (teamA == null || teamB == null) {
			return;
		}

		budgetAValueLabel.setText(formatMoney(getRemainingBudget(teamA)));
		payrollAValueLabel.setText(formatMoney(getCurrentPayroll(teamA)));
		valueAValueLabel.setText(formatMoney(getTeamValue(teamA)));

		budgetBValueLabel.setText(formatMoney(getRemainingBudget(teamB)));
		payrollBValueLabel.setText(formatMoney(getCurrentPayroll(teamB)));
		valueBValueLabel.setText(formatMoney(getTeamValue(teamB)));
		ticketPriceAValueLabel.setText(teamA.getStadium() == null ? "-" : formatMoney(teamA.getStadium().getTicketPrice()));
		ticketPriceBValueLabel.setText(teamB.getStadium() == null ? "-" : formatMoney(teamB.getStadium().getTicketPrice()));

		updatingSelectors = true;
		setMonthSelectorOptions(monthSelector, getSharedAvailableMonths(teamA, teamB));
		updatingSelectors = false;
		int month = getSelectedMonth(monthSelector);
		rebuildComparisonMetrics(teamA, teamB, month);
		rebuildComparisonDataset(teamA, teamB, month);
	}

	private void rebuildComparisonMetrics(Team teamA, Team teamB, int month) {
		comparisonMetricsPanel.removeAll();
		addComparisonRow("Budget restant", getRemainingBudget(teamA), getRemainingBudget(teamB));
		addComparisonRow("Payroll", getCurrentPayroll(teamA), getCurrentPayroll(teamB));
		addComparisonRow("Revenus du mois", sumIncomeMap(getIncomeMap(teamA, month)), sumIncomeMap(getIncomeMap(teamB, month)));
		addComparisonRow("Depenses du mois", sumExpenseMap(getExpenseMap(teamA, month)),
				sumExpenseMap(getExpenseMap(teamB, month)));
		comparisonMetricsPanel.revalidate();
		comparisonMetricsPanel.repaint();
	}

	private void addComparisonRow(String label, double valueA, double valueB) {
		comparisonMetricsPanel.add(buildListMetricRow(label, formatMoney(valueA) + "   |   " + formatMoney(valueB)));
		comparisonMetricsPanel.add(Box.createVerticalStrut(8));
	}

	private void rebuildComparisonDataset(Team teamA, Team teamB, int month) {
		comparisonDataset.clear();
		addComparisonValue("Budget", teamA.getName(), getRemainingBudget(teamA));
		addComparisonValue("Budget", teamB.getName(), getRemainingBudget(teamB));
		addComparisonValue("Payroll", teamA.getName(), getCurrentPayroll(teamA));
		addComparisonValue("Payroll", teamB.getName(), getCurrentPayroll(teamB));
		addComparisonValue("Revenus mois", teamA.getName(), sumIncomeMap(getIncomeMap(teamA, month)));
		addComparisonValue("Revenus mois", teamB.getName(), sumIncomeMap(getIncomeMap(teamB, month)));
		addComparisonValue("Depenses mois", teamA.getName(), sumExpenseMap(getExpenseMap(teamA, month)));
		addComparisonValue("Depenses mois", teamB.getName(), sumExpenseMap(getExpenseMap(teamB, month)));
		addComparisonValue("Team value", teamA.getName(), getTeamValue(teamA));
		addComparisonValue("Team value", teamB.getName(), getTeamValue(teamB));
	}

	private void addComparisonValue(String metric, String teamName, double value) {
		comparisonDataset.addValue(value, teamName, metric);
	}

	private void populateTeamsIfNeeded() {
		int teamCount = guiInterface.getTeams().size();
		if (teamASelector.getItemCount() == teamCount && teamBSelector.getItemCount() == teamCount) {
			return;
		}

		updatingSelectors = true;
		String selectedA = teamASelector.getItemCount() > 0 ? (String) teamASelector.getSelectedItem() : null;
		String selectedB = teamBSelector.getItemCount() > 0 ? (String) teamBSelector.getSelectedItem() : null;

		teamASelector.removeAllItems();
		teamBSelector.removeAllItems();
		for (Team team : guiInterface.getTeams()) {
			teamASelector.addItem(team.getName());
			teamBSelector.addItem(team.getName());
		}

		if (selectedA != null) {
			teamASelector.setSelectedItem(selectedA);
		}
		if (selectedB != null) {
			teamBSelector.setSelectedItem(selectedB);
		}
		if (teamASelector.getSelectedIndex() < 0 && teamASelector.getItemCount() > 0) {
			teamASelector.setSelectedIndex(0);
		}
		if (teamBSelector.getSelectedIndex() < 0 && teamBSelector.getItemCount() > 1) {
			teamBSelector.setSelectedIndex(1);
		} else if (teamBSelector.getSelectedIndex() < 0 && teamBSelector.getItemCount() > 0) {
			teamBSelector.setSelectedIndex(0);
		}
		updatingSelectors = false;
	}

	private List<Integer> getSharedAvailableMonths(Team teamA, Team teamB) {
		List<Integer> months = new ArrayList<Integer>(getAvailableMonths(getBudget(teamA)));
		for (int month : getAvailableMonths(getBudget(teamB))) {
			if (!months.contains(month)) {
				months.add(month);
			}
		}
		months.sort(Integer::compareTo);
		return months;
	}
}

package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import data.finance.MonthlyCentralRevenueData;
import data.league.League;
import gui.panel.common.BuildBox;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.MonthNavigatorPanel;
import gui.panel.common.ThemeAware;
import process.orchestrator.interf.GUIInterface;

public class CentralFinanceViewPanel extends JPanel implements ThemeAware {

	private static final int DASHBOARD_SPACING = 10;
	private static final int RIGHT_COLUMN_WIDTH = 280;

	private final GUIInterface guiInterface;
	private final MonthNavigatorPanel monthSelector;
	private final JLabel totalCentralRevenueValueLabel;
	private final JLabel retainedRevenueValueLabel;
	private final JLabel redistributedRevenueValueLabel;
	private final JLabel nationalTvValueLabel;
	private final JLabel nationalSponsoringValueLabel;
	private final JLabel nationalMerchandisingValueLabel;
	private final JLabel averageMonthlyValueLabel;
	private final JLabel bestMonthValueLabel;
	private final JLabel topSourceValueLabel;
	private final DefaultCategoryDataset historyDataset;
	private final DefaultPieDataset<String> breakdownDataset;

	public CentralFinanceViewPanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		monthSelector = new MonthNavigatorPanel();
		totalCentralRevenueValueLabel = FinanceViewFactory.metricLabel();
		retainedRevenueValueLabel = FinanceViewFactory.metricLabel();
		redistributedRevenueValueLabel = FinanceViewFactory.metricLabel();
		nationalTvValueLabel = FinanceViewFactory.metricLabel();
		nationalSponsoringValueLabel = FinanceViewFactory.metricLabel();
		nationalMerchandisingValueLabel = FinanceViewFactory.metricLabel();
		averageMonthlyValueLabel = FinanceViewFactory.infoLabel();
		bestMonthValueLabel = FinanceViewFactory.infoLabel();
		topSourceValueLabel = FinanceViewFactory.infoLabel();
		historyDataset = new DefaultCategoryDataset();
		breakdownDataset = new DefaultPieDataset<String>();

		organize();
		actions();
	}

	private void organize() {
		setLayout(new BorderLayout(0, DASHBOARD_SPACING));
		add(new BuildBox("RESUME CENTRAL", "Revenus centraux de la saison", buildSummaryPanel()), BorderLayout.NORTH);
		add(buildBody(), BorderLayout.CENTER);
	}

	private JPanel buildSummaryPanel() {
		JPanel summaryPanel = new JPanel(new GridLayout(1, 4, DASHBOARD_SPACING, 0));
		summaryPanel.setOpaque(false);
		summaryPanel.add(FinanceViewFactory.metricCard("Revenu central total", totalCentralRevenueValueLabel));
		summaryPanel.add(FinanceViewFactory.metricCard("Part gardee ligue", retainedRevenueValueLabel));
		summaryPanel.add(FinanceViewFactory.metricCard("Part redistribuee", redistributedRevenueValueLabel));
		summaryPanel.add(FinanceViewFactory.metricCard("Mois", monthSelector));
		return summaryPanel;
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
		centerColumn.add(new BuildBox("HISTORIQUE CENTRAL", "Total, retention et redistribution",
				FinanceViewFactory.financeLineChart(historyDataset, DashboardPanelUtil.REVENUE_COLOR)),
				BorderLayout.NORTH);
		centerColumn.add(FinanceViewFactory.panelWithHeight(
				new BuildBox("DETAIL DU MOIS", "Repartition du mois selectionne",
						FinanceViewFactory.financePieChart(breakdownDataset)),
				280),
				BorderLayout.CENTER);
		return centerColumn;
	}

	private JPanel buildRightColumn() {
		JPanel column = DashboardPanelUtil.createGridColumn(2, 1, 0, 12, RIGHT_COLUMN_WIDTH);
		column.add(new BuildBox("COMPOSITION", "Sources du mois selectionne", buildBreakdownPanel()), BorderLayout.NORTH);
		column.add(new BuildBox("INDICATEURS", "Lecture rapide de la saison", buildIndicatorsPanel()), BorderLayout.CENTER);
		return column;
	}

	private JPanel buildBreakdownPanel() {
		JPanel panel = FinanceViewFactory.infoPanel();
		panel.add(FinanceViewFactory.infoRow("TV nationale", nationalTvValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Sponsoring national", nationalSponsoringValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Merch national", nationalMerchandisingValueLabel));
		return panel;
	}

	private JPanel buildIndicatorsPanel() {
		JPanel panel = FinanceViewFactory.infoPanel();
		panel.add(FinanceViewFactory.infoRow("Moyenne mensuelle", averageMonthlyValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Meilleur mois", bestMonthValueLabel));
		panel.add(Box.createVerticalStrut(10));
		panel.add(FinanceViewFactory.infoRow("Source dominante", topSourceValueLabel));
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

		Map<Integer, MonthlyCentralRevenueData> history = league.getLeagueFinance().getMonthlyCentralRevenueHistory();
		List<Integer> months = availableMonths(history);
		FinanceDataUtil.setAvailableMonths(monthSelector, months);
		int selectedMonth = FinanceDataUtil.selectedMonth(monthSelector);
		MonthlyCentralRevenueData selectedData = history.get(selectedMonth);

		if (selectedData == null) {
			showNoDataState();
			return;
		}

		totalCentralRevenueValueLabel.setText(FinanceDataUtil.formatMoney(selectedData.getTotalCentralRevenue()));
		retainedRevenueValueLabel.setText(FinanceDataUtil.formatMoney(selectedData.getLeagueRetainedRevenue()));
		redistributedRevenueValueLabel.setText(FinanceDataUtil.formatMoney(selectedData.getRedistributedRevenue()));
		nationalTvValueLabel.setText(FinanceDataUtil.formatMoney(selectedData.getNationalTvRevenue()));
		nationalSponsoringValueLabel.setText(FinanceDataUtil.formatMoney(selectedData.getNationalSponsoringRevenue()));
		nationalMerchandisingValueLabel.setText(FinanceDataUtil.formatMoney(selectedData.getNationalMerchandisingRevenue()));
		averageMonthlyValueLabel.setText(FinanceDataUtil.formatMoney(calculateAverageMonthlyRevenue(history)));
		bestMonthValueLabel.setText(FinanceDataUtil.monthLabel(findBestMonth(history)));
		topSourceValueLabel.setText(getTopSourceLabel(selectedData));

		FinanceDataUtil.setRevenueColor(totalCentralRevenueValueLabel);
		retainedRevenueValueLabel.setForeground(DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
		redistributedRevenueValueLabel.setForeground(DashboardPanelUtil.REVENUE_COLOR);
		FinanceDataUtil.setRevenueColor(nationalTvValueLabel);
		FinanceDataUtil.setRevenueColor(nationalSponsoringValueLabel);
		FinanceDataUtil.setRevenueColor(nationalMerchandisingValueLabel);
		averageMonthlyValueLabel.setForeground(DashboardPanelUtil.REVENUE_COLOR);
		bestMonthValueLabel.setForeground(DashboardPanelUtil.POLICY_BALANCED_COLOR);
		topSourceValueLabel.setForeground(DashboardPanelUtil.STRATEGY_REBUILD_COLOR);

		rebuildHistoryDataset(history);
		rebuildBreakdownDataset(selectedData);
	}

	private List<Integer> availableMonths(Map<Integer, MonthlyCentralRevenueData> history) {
		List<Integer> months = new ArrayList<Integer>();
		for (int month = 1; month <= FinanceDataUtil.lastVisibleMonth(guiInterface); month++) {
			if (history != null && history.containsKey(month)) {
				months.add(month);
			}
		}
		if (months.isEmpty()) {
			months.add(1);
		}
		return months;
	}

	private void rebuildHistoryDataset(Map<Integer, MonthlyCentralRevenueData> history) {
		historyDataset.clear();
		for (int month = 1; month <= FinanceDataUtil.lastVisibleMonth(guiInterface); month++) {
			MonthlyCentralRevenueData data = history.get(month);
			if (data == null) {
				continue;
			}
			historyDataset.addValue(data.getTotalCentralRevenue(), "Total central", FinanceDataUtil.monthLabel(month));
			historyDataset.addValue(data.getLeagueRetainedRevenue(), "Retention ligue", FinanceDataUtil.monthLabel(month));
			historyDataset.addValue(data.getRedistributedRevenue(), "Redistribution", FinanceDataUtil.monthLabel(month));
		}
	}

	private void rebuildBreakdownDataset(MonthlyCentralRevenueData data) {
		breakdownDataset.clear();
		breakdownDataset.setValue("TV", data.getNationalTvRevenue());
		breakdownDataset.setValue("Sponsoring", data.getNationalSponsoringRevenue());
		breakdownDataset.setValue("Merch", data.getNationalMerchandisingRevenue());
	}

	private void showSeasonNotStartedState() {
		totalCentralRevenueValueLabel.setText("La saison n'a pas encore commence.");
		retainedRevenueValueLabel.setText("Les flux centraux apparaitront apres le lancement.");
		redistributedRevenueValueLabel.setText("Les flux centraux apparaitront apres le lancement.");
		nationalTvValueLabel.setText("Les details centraux apparaitront apres le lancement.");
		nationalSponsoringValueLabel.setText("Les details centraux apparaitront apres le lancement.");
		nationalMerchandisingValueLabel.setText("Les details centraux apparaitront apres le lancement.");
		averageMonthlyValueLabel.setText("Les indicateurs apparaitront apres le lancement.");
		bestMonthValueLabel.setText("Les indicateurs apparaitront apres le lancement.");
		topSourceValueLabel.setText("Les indicateurs apparaitront apres le lancement.");
		resetDatasets("Lancez la saison pour afficher les revenus centraux.");
	}

	private void showNoDataState() {
		totalCentralRevenueValueLabel.setText("Aucune donnee centrale n'est disponible.");
		retainedRevenueValueLabel.setText("Aucune donnee centrale n'est disponible.");
		redistributedRevenueValueLabel.setText("Aucune donnee centrale n'est disponible.");
		nationalTvValueLabel.setText("Aucune donnee centrale n'est disponible.");
		nationalSponsoringValueLabel.setText("Aucune donnee centrale n'est disponible.");
		nationalMerchandisingValueLabel.setText("Aucune donnee centrale n'est disponible.");
		averageMonthlyValueLabel.setText("Aucune donnee centrale n'est disponible.");
		bestMonthValueLabel.setText("Aucune donnee centrale n'est disponible.");
		topSourceValueLabel.setText("Aucune donnee centrale n'est disponible.");
		resetDatasets("Aucune donnee centrale n'est disponible.");
	}

	private void resetDatasets(String message) {
		applyEmptyStateLabel(totalCentralRevenueValueLabel, 12);
		applyEmptyStateLabel(retainedRevenueValueLabel, 12);
		applyEmptyStateLabel(redistributedRevenueValueLabel, 12);
		applyEmptyStateLabel(nationalTvValueLabel, 12);
		applyEmptyStateLabel(nationalSponsoringValueLabel, 12);
		applyEmptyStateLabel(nationalMerchandisingValueLabel, 12);
		applyEmptyStateLabel(averageMonthlyValueLabel, 12);
		applyEmptyStateLabel(bestMonthValueLabel, 12);
		applyEmptyStateLabel(topSourceValueLabel, 12);
		historyDataset.clear();
		breakdownDataset.clear();
		historyDataset.addValue(0.0, "Central", message);
		breakdownDataset.setValue("Aucun", 1.0);
	}

	private double calculateAverageMonthlyRevenue(Map<Integer, MonthlyCentralRevenueData> history) {
		if (history == null || history.isEmpty()) {
			return 0.0;
		}
		double total = 0.0;
		int count = 0;
		for (MonthlyCentralRevenueData data : history.values()) {
			total += data.getTotalCentralRevenue();
			count++;
		}
		return count == 0 ? 0.0 : total / count;
	}

	private int findBestMonth(Map<Integer, MonthlyCentralRevenueData> history) {
		int bestMonth = 1;
		double bestValue = Double.NEGATIVE_INFINITY;
		for (Map.Entry<Integer, MonthlyCentralRevenueData> entry : history.entrySet()) {
			double value = entry.getValue().getTotalCentralRevenue();
			if (value > bestValue) {
				bestValue = value;
				bestMonth = entry.getKey();
			}
		}
		return bestMonth;
	}

	private String getTopSourceLabel(MonthlyCentralRevenueData data) {
		double tv = data.getNationalTvRevenue();
		double sponsoring = data.getNationalSponsoringRevenue();
		double merch = data.getNationalMerchandisingRevenue();

		if (tv >= sponsoring && tv >= merch) {
			return "TV";
		}
		if (sponsoring >= merch) {
			return "Sponsoring";
		}
		return "Merch";
	}

	private void applyEmptyStateLabel(JLabel label, int fontSize) {
		LabelStyleUtil.styleSubtitleLabel(label, fontSize);
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		monthSelector.applyTheme();
		FinanceViewFactory.refreshCharts(this);
	}

	private class RefreshAction implements Runnable {
		@Override
		public void run() {
			refreshData();
		}
	}
}

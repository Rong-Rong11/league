package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.data.category.DefaultCategoryDataset;

import data.finance.budget.expense.Expense;
import data.finance.budget.income.Income;
import gui.panel.common.DashboardPanelUtil;

public class TeamFinanceBreakdownPanel extends JPanel {
	private final String seriesName;
	private final String emptyCategoryLabel;
	private final JPanel metricsPanel;
	private final DefaultCategoryDataset dataset;

	public TeamFinanceBreakdownPanel(String seriesName, String emptyCategoryLabel, java.awt.Color chartColor) {
		super(new BorderLayout(0, TeamFinanceViewPanel.DASHBOARD_SPACING));
		this.seriesName = seriesName;
		this.emptyCategoryLabel = emptyCategoryLabel;
		this.metricsPanel = FinanceViewFactory.metricListPanel();
		this.dataset = new DefaultCategoryDataset();
		setOpaque(false);
		add(metricsPanel, BorderLayout.NORTH);
		add(FinanceViewFactory.financeBarChart(dataset, chartColor), BorderLayout.CENTER);
	}

	public void showIncomeBreakdown(Map<String, Income> incomes) {
		dataset.clear();
		metricsPanel.removeAll();
		double total = FinanceDataUtil.totalIncome(incomes);
		metricsPanel.add(FinanceViewFactory.valueRow("Total revenus", FinanceDataUtil.formatMoney(total),
				DashboardPanelUtil.REVENUE_COLOR));
		metricsPanel.add(Box.createVerticalStrut(8));
		metricsPanel.add(FinanceViewFactory.valueRow("Revenus locaux",
				FinanceDataUtil.formatMoney(FinanceDataUtil.totalLocalIncome(incomes)), DashboardPanelUtil.REVENUE_COLOR));

		if (incomes != null && !incomes.isEmpty()) {
			for (Income income : incomes.values()) {
				dataset.addValue(income.getAmount(), seriesName, FinanceDataUtil.formatTypeName(income.getName()));
			}
		} else {
			dataset.addValue(0.0, seriesName, emptyCategoryLabel);
		}

		refreshView();
	}

	public void showExpenseBreakdown(Map<String, Expense> expenses, double monthNet) {
		dataset.clear();
		metricsPanel.removeAll();
		double total = FinanceDataUtil.totalExpense(expenses);
		metricsPanel.add(FinanceViewFactory.valueRow("Total depenses", FinanceDataUtil.formatMoney(total),
				DashboardPanelUtil.EXPENSE_COLOR));
		metricsPanel.add(Box.createVerticalStrut(8));
		metricsPanel.add(FinanceViewFactory.valueRow("Net du mois", FinanceDataUtil.formatMoney(monthNet),
				DashboardPanelUtil.getValueColorForAmount(monthNet)));

		if (expenses != null && !expenses.isEmpty()) {
			for (Expense expense : expenses.values()) {
				dataset.addValue(expense.getAmount(), seriesName, FinanceDataUtil.formatTypeName(expense.getName()));
			}
		} else {
			dataset.addValue(0.0, seriesName, emptyCategoryLabel);
		}

		refreshView();
	}

	public void showEmptyState(String text) {
		dataset.clear();
		metricsPanel.removeAll();
		metricsPanel.add(buildEmptyStateMessage(text));
		refreshView();
	}

	private JLabel buildEmptyStateMessage(String text) {
		JLabel label = new JLabel(text);
		gui.panel.common.LabelStyleUtil.styleSubtitleLabel(label, 12);
		return label;
	}

	private void refreshView() {
		metricsPanel.revalidate();
		metricsPanel.repaint();
	}
}

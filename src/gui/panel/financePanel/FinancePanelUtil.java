package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.FinanceScope;
import data.finance.budget.expense.Expense;
import data.finance.budget.income.Income;
import data.team.Team;
import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.MonthNavigatorPanel;
import gui.panel.common.PlayerDisplayUtil;
import process.orchestrator.GUIInterface;

public final class FinancePanelUtil {

	private FinancePanelUtil() {
	}

	public static void styleComboBox(JComboBox<String> comboBox) {
		comboBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		comboBox.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		comboBox.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
		comboBox.setBorder(BorderFactory.createLineBorder(DashboardPanelUtil.BORDER_COLOR));
		comboBox.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
				if (isSelected) {
					label.setBackground(DashboardPanelUtil.TITLE_TEXT_COLOR);
					label.setForeground(Color.WHITE);
				} else {
					label.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
					label.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
				}
				return label;
			}
		});
	}

	public static JPanel buildMetricCard(String title, Component component) {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		titleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		if (component instanceof JComponent) {
			((JComponent) component).setAlignmentX(Component.LEFT_ALIGNMENT);
		}

		card.add(titleLabel);
		card.add(Box.createVerticalStrut(4));
		card.add(component);
		return card;
	}

	public static JLabel createMetricValueLabel() {
		JLabel label = new JLabel("-");
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		label.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		return label;
	}

	public static JLabel createBodyValueLabel() {
		JLabel label = new JLabel("-");
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		label.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		return label;
	}

	public static JPanel buildTextMetricRow(String title, JLabel valueLabel) {
		JPanel panel = new JPanel(new BorderLayout(8, 0));
		panel.setOpaque(false);
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		titleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		panel.add(titleLabel, BorderLayout.WEST);
		panel.add(valueLabel, BorderLayout.EAST);
		return panel;
	}

	public static JPanel buildTextMetricRow(String title, JLabel valueLabel, Color valueColor) {
		valueLabel.setForeground(valueColor);
		return buildTextMetricRow(title, valueLabel);
	}

	public static JPanel buildListMetricRow(String title, String value) {
		JPanel row = new JPanel(new BorderLayout(8, 0));
		row.setOpaque(false);

		JLabel leftLabel = new JLabel(title);
		leftLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		leftLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);

		JLabel rightLabel = new JLabel(value);
		rightLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		rightLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);

		row.add(leftLabel, BorderLayout.WEST);
		row.add(rightLabel, BorderLayout.EAST);
		return row;
	}

	public static JPanel buildListMetricRow(String title, String value, Color valueColor) {
		JPanel row = buildListMetricRow(title, value);
		Component rightComponent = row.getComponent(1);
		if (rightComponent instanceof JLabel) {
			((JLabel) rightComponent).setForeground(valueColor);
		}
		return row;
	}

	public static JPanel createMetricListPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
		return panel;
	}

	public static JPanel createSectionContentPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
		return panel;
	}

	public static JPanel withPreferredHeight(JPanel content, int preferredHeight) {
		content.setPreferredSize(new Dimension(content.getPreferredSize().width, preferredHeight));
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.add(content, BorderLayout.NORTH);
		return wrapper;
	}

	public static JPanel buildLineChartPanel(DefaultCategoryDataset dataset, String valueAxisLabel, Color mainColor) {
		JFreeChart chart = ChartFactory.createLineChart(null, "", valueAxisLabel, dataset, PlotOrientation.VERTICAL,
				false, false, false);

		CategoryPlot plot = chart.getCategoryPlot();
		stylePlot(plot);

		LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, true);
		renderer.setSeriesPaint(0, mainColor);
		if (dataset.getRowCount() > 1) {
			renderer.setSeriesPaint(1, DashboardPanelUtil.EXPENSE_COLOR);
		}
		plot.setRenderer(renderer);

		styleAxes(plot);
		return wrapChart(chart, 190);
	}

	public static JPanel buildBarChartPanel(DefaultCategoryDataset dataset, String categoryAxisLabel,
			String valueAxisLabel, Color color) {
		JFreeChart chart = ChartFactory.createBarChart(null, categoryAxisLabel, valueAxisLabel, dataset,
				PlotOrientation.VERTICAL, false, false, false);

		CategoryPlot plot = chart.getCategoryPlot();
		stylePlot(plot);

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, color);
		renderer.setSeriesPaint(1, DashboardPanelUtil.REVENUE_COLOR);
		renderer.setBarPainter(new StandardBarPainter());
		renderer.setShadowVisible(false);
		renderer.setMaximumBarWidth(0.12);

		styleAxes(plot);
		plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		return wrapChart(chart, 170);
	}

	private static void stylePlot(CategoryPlot plot) {
		plot.setBackgroundPaint(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		plot.setOutlineVisible(false);
		plot.setRangeGridlinePaint(
				DashboardPanelUtil.isDarkMode() ? new Color(70, 76, 86) : new Color(226, 230, 236));
		plot.setNoDataMessage("Aucune donnee");
		plot.setNoDataMessagePaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
	}

	private static void styleAxes(CategoryPlot plot) {
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setTickLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		domainAxis.setLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		domainAxis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		domainAxis.setAxisLineVisible(false);
		domainAxis.setTickMarksVisible(false);

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setTickLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		rangeAxis.setLabelPaint(DashboardPanelUtil.TITLE_TEXT_COLOR);
		rangeAxis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		rangeAxis.setAxisLineVisible(false);
		rangeAxis.setAutoRangeIncludesZero(true);
	}

	private static JPanel wrapChart(JFreeChart chart, int preferredHeight) {
		applyChartTheme(chart);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		chartPanel.setMouseWheelEnabled(false);
		chartPanel.setPopupMenu(null);
		chartPanel.setPreferredSize(new Dimension(360, preferredHeight));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.add(chartPanel, BorderLayout.CENTER);
		panel.setPreferredSize(new Dimension(360, preferredHeight));
		return panel;
	}

	public static void applyChartTheme(JFreeChart chart) {
		if (chart == null) {
			return;
		}

		chart.setBackgroundPaint(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		Plot plot = chart.getPlot();

		if (plot instanceof CategoryPlot) {
			applyCategoryPlotTheme((CategoryPlot) plot);
		}

		if (plot instanceof PiePlot) {
			applyPiePlotTheme((PiePlot) plot);
		}
	}

	private static void applyCategoryPlotTheme(CategoryPlot plot) {
		plot.setBackgroundPaint(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		plot.setOutlineVisible(false);
		plot.setRangeGridlinePaint(
				DashboardPanelUtil.isDarkMode() ? new Color(70, 76, 86) : new Color(226, 230, 236));
		plot.setNoDataMessagePaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);

		CategoryAxis domainAxis = plot.getDomainAxis();
		if (domainAxis != null) {
			domainAxis.setTickLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
			domainAxis.setLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
			domainAxis.setAxisLineVisible(false);
			domainAxis.setTickMarksVisible(false);
		}

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		if (rangeAxis != null) {
			rangeAxis.setTickLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
			rangeAxis.setLabelPaint(DashboardPanelUtil.TITLE_TEXT_COLOR);
			rangeAxis.setAxisLineVisible(false);
		}
	}

	private static void applyPiePlotTheme(PiePlot plot) {
		plot.setBackgroundPaint(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		plot.setOutlineVisible(false);
		plot.setShadowPaint(null);
		plot.setLabelBackgroundPaint(null);
		plot.setLabelOutlinePaint(null);
		plot.setLabelShadowPaint(null);
		plot.setLabelPaint(DashboardPanelUtil.TITLE_TEXT_COLOR);
		plot.setLabelLinkPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		plot.setNoDataMessagePaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
	}

	public static void applyThemeToCharts(Component component) {
		if (component instanceof ChartPanel) {
			ChartPanel chartPanel = (ChartPanel) component;
			chartPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
			applyChartTheme(chartPanel.getChart());
			chartPanel.repaint();
		}

		if (component instanceof Container) {
			Container container = (Container) component;
			for (Component child : container.getComponents()) {
				applyThemeToCharts(child);
			}
		}
	}

	public static Team getSelectedTeam(GUIInterface guiInterface, JComboBox<String> comboBox, int fallbackIndex) {
		if (comboBox.getItemCount() == 0) {
			return null;
		}
		if (comboBox.getSelectedIndex() < 0) {
			comboBox.setSelectedIndex(Math.min(fallbackIndex, comboBox.getItemCount() - 1));
		}
		String teamName = (String) comboBox.getSelectedItem();
		return teamName == null ? null : guiInterface.getTeamByName(teamName);
	}

	public static List<Integer> getAvailableMonths(GUIInterface guiInterface, Budget budget) {
		List<Integer> months = new ArrayList<Integer>();
		if (budget == null) {
			months.add(1);
			return months;
		}

		int lastVisibleMonth = Math.min(Math.max(1, guiInterface.getCurrentFinanceMonth()),
				FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS - 1);
		for (int month = 1; month <= lastVisibleMonth; month++) {
			Map<String, Income> incomes = budget.getIncomesForMonth(month);
			Map<String, Expense> expenses = budget.getExpensesForMonth(month);
			if ((incomes != null && !incomes.isEmpty()) || (expenses != null && !expenses.isEmpty())) {
				months.add(month);
			}
		}

		if (months.isEmpty()) {
			months.add(1);
		}
		return months;
	}

	public static int getSelectedMonth(MonthNavigatorPanel navigator) {
		if (navigator == null) {
			return 1;
		}
		return navigator.getSelectedMonth();
	}

	public static void setAvailableMonths(MonthNavigatorPanel navigator, List<Integer> months) {
		if (navigator != null) {
			navigator.setAvailableMonths(months);
		}
	}

	public static int getLastVisibleFinanceMonth(GUIInterface guiInterface) {
		return Math.min(Math.max(1, guiInterface.getCurrentFinanceMonth()),
				FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS - 1);
	}

	public static Budget getBudget(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getBudget();
	}

	public static Map<String, Income> getIncomeMap(Team team, int month) {
		Budget budget = getBudget(team);
		if (budget == null) {
			return null;
		}
		return budget.getIncomesForMonth(month);
	}

	public static Map<String, Expense> getExpenseMap(Team team, int month) {
		Budget budget = getBudget(team);
		if (budget == null) {
			return null;
		}
		return budget.getExpensesForMonth(month);
	}

	public static double sumIncomeMap(Map<String, Income> incomes) {
		double total = 0.0;
		if (incomes != null) {
			for (Income income : incomes.values()) {
				total += income.getAmount();
			}
		}
		return total;
	}

	public static double sumLocalIncomeMap(Map<String, Income> incomes) {
		double total = 0.0;
		if (incomes != null) {
			for (Income income : incomes.values()) {
				if (income.getIncomeType() != null && income.getIncomeType().getScope() == FinanceScope.LOCAL) {
					total += income.getAmount();
				}
			}
		}
		return total;
	}

	public static double sumExpenseMap(Map<String, Expense> expenses) {
		double total = 0.0;
		if (expenses != null) {
			for (Expense expense : expenses.values()) {
				total += expense.getAmount();
			}
		}
		return total;
	}

	public static double getNetForMonth(Budget budget, int month) {
		if (budget == null) {
			return 0.0;
		}
		return sumIncomeMap(budget.getIncomesForMonth(month)) - sumExpenseMap(budget.getExpensesForMonth(month));
	}

	public static String monthLabel(int month) {
		return "M" + month;
	}

	public static String formatMoney(double value) {
		return PlayerDisplayUtil.formatSalary(value);
	}

	public static String formatPercent(double value) {
		return PlayerDisplayUtil.formatOneDecimal(value * 100.0) + "%";
	}

	public static String prettifyEnum(String value) {
		if (value == null || value.isEmpty()) {
			return "-";
		}
		String[] parts = value.toLowerCase().split("_");
		String text = "";
		for (String part : parts) {
			if (!part.isEmpty()) {
				if (!text.isEmpty()) {
					text += " ";
				}
				text += Character.toUpperCase(part.charAt(0)) + part.substring(1);
			}
		}
		return text;
	}

	public static String prettifyObjectName(Object object) {
		if (object == null) {
			return "-";
		}
		return object.getClass().getSimpleName().replaceAll("([a-z])([A-Z])", "$1 $2");
	}

	public static void applyAmountColor(JLabel label, double value) {
		if (label != null) {
			label.setForeground(DashboardPanelUtil.getValueColorForAmount(value));
		}
	}

	public static void applyRevenueColor(JLabel label) {
		if (label != null) {
			label.setForeground(DashboardPanelUtil.REVENUE_COLOR);
		}
	}

	public static void applyExpenseColor(JLabel label) {
		if (label != null) {
			label.setForeground(DashboardPanelUtil.EXPENSE_COLOR);
		}
	}

	public static void applyPolicyColor(JLabel label, String policyName) {
		if (label != null) {
			label.setForeground(DashboardPanelUtil.getFinancialPolicyColor(policyName));
		}
	}

	public static void applyStrategyColor(JLabel label, String strategyName) {
		if (label != null) {
			label.setForeground(DashboardPanelUtil.getTransferStrategyColor(strategyName));
		}
	}

	public static void applyMarketColor(JLabel label, String marketName) {
		if (label != null) {
			label.setForeground(DashboardPanelUtil.getMarketColor(marketName));
		}
	}
}

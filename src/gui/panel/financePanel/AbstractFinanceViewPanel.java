package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.DefaultListCellRenderer;

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
import org.jfree.data.general.DefaultPieDataset;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.FinanceScope;
import data.finance.budget.expense.Expense;
import data.finance.budget.income.Income;
import data.team.Team;
import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.PlayerDisplayUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.ThemeAware;
import process.orchestrator.GUIInterface;

public abstract class AbstractFinanceViewPanel extends JPanel implements ThemeAware {

	protected static final int DASHBOARD_SPACING = 10;
	protected static final int RIGHT_COLUMN_WIDTH = 280;

	protected final GUIInterface guiInterface;

	protected AbstractFinanceViewPanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		setOpaque(false);
	}

	protected MonthNavigator buildMonthNavigator() {
		return new MonthNavigator();
	}

	protected void styleComboBox(JComboBox<String> comboBox) {
		comboBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		comboBox.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		comboBox.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
		comboBox.setBorder(BorderFactory.createLineBorder(DashboardPanelUtil.BORDER_COLOR));
		comboBox.setRenderer(buildComboBoxRenderer());
	}

	protected ListCellRenderer<? super String> buildComboBoxRenderer() {
		return new DefaultListCellRenderer() {
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
		};
	}

	protected JPanel buildMetricCard(String title, Component component) {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		titleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);

		component.setPreferredSize(
				new Dimension(component.getPreferredSize().width, Math.max(28, component.getPreferredSize().height)));
		if (component instanceof JComponent) {
			((JComponent) component).setAlignmentX(LEFT_ALIGNMENT);
		}

		card.add(titleLabel);
		card.add(Box.createVerticalStrut(4));
		card.add(component);
		return card;
	}

	protected JLabel createMetricValueLabel() {
		JLabel label = new JLabel("-");
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		label.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		label.setAlignmentX(LEFT_ALIGNMENT);
		return label;
	}

	protected JLabel createBodyValueLabel() {
		JLabel label = new JLabel("-");
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		label.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		return label;
	}

	protected JPanel buildTextMetricRow(String title, JLabel valueLabel) {
		JPanel panel = new JPanel(new BorderLayout(8, 0));
		panel.setOpaque(false);
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		titleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		panel.add(titleLabel, BorderLayout.WEST);
		panel.add(valueLabel, BorderLayout.EAST);
		return panel;
	}

	protected JPanel createMetricListPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
		return panel;
	}

	protected JPanel createSectionContentPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
		return panel;
	}

	protected JPanel pinToTop(JPanel content) {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.add(content, BorderLayout.NORTH);
		return wrapper;
	}

	protected JPanel withPreferredHeight(JPanel content, int preferredHeight) {
		content.setPreferredSize(new Dimension(content.getPreferredSize().width, preferredHeight));
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.add(content, BorderLayout.NORTH);
		return wrapper;
	}

	protected JPanel buildListMetricRow(String title, String value) {
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

	protected JPanel buildLineChartPanel(DefaultCategoryDataset dataset, String title, String valueAxisLabel,
			Color seriesColor) {
		JFreeChart chart = ChartFactory.createLineChart(
				null,
				"",
				valueAxisLabel,
				dataset,
				PlotOrientation.VERTICAL,
				false,
				false,
				false);

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setOutlineVisible(false);
		plot.setDomainGridlinesVisible(false);
		plot.setNoDataMessage("Aucune donnee a afficher");
		plot.setNoDataMessageFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

		LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, true);
		renderer.setSeriesPaint(0, seriesColor);
		renderer.setSeriesStroke(0, new java.awt.BasicStroke(2.2f));
		if (dataset.getRowCount() > 1) {
			renderer.setSeriesPaint(1, new Color(0xD0, 0x6B, 0x3D));
			renderer.setSeriesStroke(1, new java.awt.BasicStroke(2.0f));
		}
		plot.setRenderer(renderer);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		domainAxis.setAxisLineVisible(false);
		domainAxis.setTickMarksVisible(false);

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		rangeAxis.setAxisLineVisible(false);
		rangeAxis.setNumberFormatOverride(new DecimalFormat("0.#"));
		rangeAxis.setAutoRangeIncludesZero(true);
		applyChartTheme(chart);
		return wrapChart(chart, 190);
	}

	protected JPanel buildBarChartPanel(DefaultCategoryDataset dataset, String title, String categoryAxisLabel,
			String valueAxisLabel, Color color) {
		JFreeChart chart = ChartFactory.createBarChart(
				null,
				categoryAxisLabel,
				valueAxisLabel,
				dataset,
				PlotOrientation.VERTICAL,
				false,
				false,
				false);

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setOutlineVisible(false);
		plot.setNoDataMessage("Aucune donnee a afficher");
		plot.setNoDataMessageFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, color);
		renderer.setSeriesPaint(1, new Color(0xC0, 0x5A, 0x3D));
		renderer.setBarPainter(new StandardBarPainter());
		renderer.setShadowVisible(false);
		renderer.setMaximumBarWidth(0.12);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		domainAxis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		domainAxis.setAxisLineVisible(false);
		domainAxis.setTickMarksVisible(false);

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		rangeAxis.setAxisLineVisible(false);
		rangeAxis.setNumberFormatOverride(new DecimalFormat("0.#"));
		rangeAxis.setAutoRangeIncludesZero(true);
		applyChartTheme(chart);
		return wrapChart(chart, 170);
	}

	protected JPanel buildPieChartPanel(DefaultPieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart(null, dataset, false, false, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setOutlineVisible(false);
		plot.setShadowPaint(null);
		plot.setLabelBackgroundPaint(null);
		plot.setLabelOutlinePaint(null);
		plot.setLabelShadowPaint(null);
		plot.setLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		plot.setNoDataMessage("Aucune donnee a afficher");
		plot.setNoDataMessageFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		plot.setSectionPaint("Retention ligue", new Color(0x17, 0x31, 0x74));
		plot.setSectionPaint("Part egale", new Color(0x5D, 0x83, 0xC2));
		plot.setSectionPaint("Part ponderee", new Color(0xD7, 0x87, 0x3B));

		applyChartTheme(chart);
		return wrapChart(chart, 165);
	}

	protected JPanel wrapChart(JFreeChart chart, int preferredHeight) {
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setOpaque(true);
		chartPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		chartPanel.setMouseWheelEnabled(false);
		chartPanel.setPopupMenu(null);
		chartPanel.setPreferredSize(new Dimension(360, preferredHeight));
		chartPanel.setMinimumDrawHeight(180);
		chartPanel.setMaximumDrawHeight(1200);
		chartPanel.setMinimumDrawWidth(240);
		chartPanel.setMaximumDrawWidth(2400);

		ThemedChartContainer container = new ThemedChartContainer(chart, chartPanel, preferredHeight);
		return pinToTop(container);
	}

	protected void applyChartTheme(JFreeChart chart) {
		if (chart == null) {
			return;
		}

		chart.setBackgroundPaint(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		Plot rawPlot = chart.getPlot();

		if (rawPlot instanceof CategoryPlot) {
			CategoryPlot plot = (CategoryPlot) rawPlot;
			plot.setBackgroundPaint(DashboardPanelUtil.PANEL_SURFACE_COLOR);
			plot.setRangeGridlinePaint(DashboardPanelUtil.isDarkMode()
					? new Color(70, 76, 86)
					: new Color(226, 230, 236));
			plot.setNoDataMessagePaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);

			CategoryAxis domainAxis = plot.getDomainAxis();
			domainAxis.setTickLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
			domainAxis.setLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);

			NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setTickLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
			rangeAxis.setLabelPaint(DashboardPanelUtil.TITLE_TEXT_COLOR);
			return;
		}

		if (rawPlot instanceof PiePlot) {
			PiePlot plot = (PiePlot) rawPlot;
			plot.setBackgroundPaint(DashboardPanelUtil.PANEL_SURFACE_COLOR);
			plot.setNoDataMessagePaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
			plot.setLabelPaint(DashboardPanelUtil.TITLE_TEXT_COLOR);
			plot.setLabelLinkPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		}
	}


	protected Team getSelectedTeam(JComboBox<String> comboBox, int fallbackIndex) {
		if (comboBox.getItemCount() == 0) {
			return null;
		}
		if (comboBox.getSelectedIndex() < 0) {
			comboBox.setSelectedIndex(Math.min(fallbackIndex, comboBox.getItemCount() - 1));
		}
		String teamName = (String) comboBox.getSelectedItem();
		return teamName == null ? null : guiInterface.getTeamByName(teamName);
	}

	protected int getSelectedMonth(MonthNavigator navigator) {
		return navigator == null ? 1 : navigator.getSelectedMonth();
	}

	protected int getLastVisibleFinanceMonth() {
		int currentMonth = guiInterface == null ? 1 : guiInterface.getCurrentFinanceMonth();
		if (currentMonth < 1) {
			return 1;
		}
		return Math.min(currentMonth, FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS - 1);
	}

	protected List<Integer> getAvailableMonths(Budget budget) {
		List<Integer> months = new ArrayList<Integer>();
		if (budget == null) {
			months.add(1);
			return months;
		}

		int upperBound = Math.max(1, getLastVisibleFinanceMonth());
		for (int month = 1; month <= upperBound; month++) {
			Map<String, Income> incomes = budget.getIncomesForMonth(month);
			Map<String, Expense> expenses = budget.getExpensesForMonth(month);
			boolean hasIncome = incomes != null && !incomes.isEmpty();
			boolean hasExpense = expenses != null && !expenses.isEmpty();

			if (hasIncome || hasExpense) {
				months.add(month);
			}
		}

		if (months.isEmpty()) {
			months.add(1);
		}
		return months;
	}

	protected void setMonthSelectorOptions(MonthNavigator navigator, List<Integer> months) {
		if (navigator != null) {
			navigator.setAvailableMonths(months);
		}
	}

	protected double sumIncomeMap(Map<String, Income> incomes) {
		double total = 0.0;
		if (incomes == null) {
			return total;
		}
		for (Income income : incomes.values()) {
			total += income.getAmount();
		}
		return total;
	}

	protected double sumLocalIncomeMap(Map<String, Income> incomes) {
		double total = 0.0;
		if (incomes == null) {
			return total;
		}
		for (Income income : incomes.values()) {
			if (income.getIncomeType() != null && income.getIncomeType().getScope() == FinanceScope.LOCAL) {
				total += income.getAmount();
			}
		}
		return total;
	}

	protected double sumExpenseMap(Map<String, Expense> expenses) {
		double total = 0.0;
		if (expenses == null) {
			return total;
		}
		for (Expense expense : expenses.values()) {
			total += expense.getAmount();
		}
		return total;
	}

	protected Budget getBudget(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getBudget();
	}

	protected Map<String, Income> getIncomeMap(Team team, int month) {
		Budget budget = getBudget(team);
		return budget == null ? null : budget.getIncomesForMonth(month);
	}

	protected Map<String, Expense> getExpenseMap(Team team, int month) {
		Budget budget = getBudget(team);
		return budget == null ? null : budget.getExpensesForMonth(month);
	}

	protected double getRemainingBudget(Team team) {
		Budget budget = getBudget(team);
		return budget == null ? 0.0 : budget.getRemainingAmount();
	}

	protected double getCurrentPayroll(Team team) {
		return team == null || team.getTeamFinance() == null ? 0.0 : team.getTeamFinance().getCurrentPayroll();
	}

	protected double getTeamValue(Team team) {
		return team == null || team.getTeamFinance() == null ? 0.0 : team.getTeamFinance().getTeamValue();
	}

	protected double getLuxuryTaxPaid(Team team) {
		return team == null || team.getTeamFinance() == null ? 0.0 : team.getTeamFinance().getLuxuryTaxPaid();
	}

	protected Object getFinancialPolicy(Team team) {
		return team == null || team.getTeamFinance() == null ? null : team.getTeamFinance().getFinancialProfil();
	}

	protected Object getMarketSize(Team team) {
		return team == null || team.getTeamFinance() == null ? null : team.getTeamFinance().getMarketSize();
	}

	protected Object getTransferStrategy(Team team) {
		return team == null || team.getTeamFinance() == null ? null : team.getTeamFinance().getTeamTransferStrategy();
	}

	protected String monthLabel(int month) {
		return "M" + month;
	}

	protected String formatMoney(double value) {
		return PlayerDisplayUtil.formatSalary(value);
	}

	protected String formatPercent(double value) {
		return PlayerDisplayUtil.formatOneDecimal(value * 100.0) + "%";
	}

	protected String prettifyEnum(String value) {
		if (value == null || value.isEmpty()) {
			return "-";
		}
		String[] parts = value.toLowerCase().split("_");
		StringBuilder builder = new StringBuilder();
		for (String part : parts) {
			if (part.isEmpty()) {
				continue;
			}
			if (builder.length() > 0) {
				builder.append(' ');
			}
			builder.append(Character.toUpperCase(part.charAt(0)));
			builder.append(part.substring(1));
		}
		return builder.toString();
	}

	protected String prettifyObjectName(Object object) {
		if (object == null) {
			return "-";
		}
		return object.getClass().getSimpleName().replaceAll("([a-z])([A-Z])", "$1 $2");
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		DashboardPanelUtil.refreshChildrenTheme(this);
		applyThemeToControls(this);
	}

	private void applyThemeToControls(Component component) {
		if (component instanceof JComboBox<?>) {
			@SuppressWarnings("unchecked")
			JComboBox<String> comboBox = (JComboBox<String>) component;
			styleComboBox(comboBox);
		}
		if (component instanceof Container) {
			for (Component child : ((Container) component).getComponents()) {
				applyThemeToControls(child);
			}
		}
	}

	protected class MonthNavigator extends JPanel implements ThemeAware {
		private final JButton previousButton;
		private final JButton nextButton;
		private final JLabel monthLabel;
		private List<Integer> months;
		private int selectedMonth;
		private Runnable changeListener;
		private boolean updating;

		protected MonthNavigator() {
			super(new BorderLayout(6, 0));
			setOpaque(false);
			months = new ArrayList<Integer>();
			months.add(1);
			selectedMonth = 1;

			previousButton = buildArrowButton("<");
			nextButton = buildArrowButton(">");
			monthLabel = new JLabel(monthLabel(selectedMonth), JLabel.CENTER);
			monthLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
			monthLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);

			previousButton.addActionListener(e -> moveSelection(-1));
			nextButton.addActionListener(e -> moveSelection(1));

			add(previousButton, BorderLayout.WEST);
			add(monthLabel, BorderLayout.CENTER);
			add(nextButton, BorderLayout.EAST);
			applyTheme();
			refreshState();
		}

		protected void setChangeListener(Runnable changeListener) {
			this.changeListener = changeListener;
		}

		protected int getSelectedMonth() {
			return selectedMonth;
		}

		protected void setAvailableMonths(List<Integer> availableMonths) {
			updating = true;
			months = new ArrayList<Integer>(availableMonths == null || availableMonths.isEmpty()
					? java.util.List.of(1)
					: availableMonths);
			if (!months.contains(selectedMonth)) {
				selectedMonth = months.get(months.size() - 1);
			}
			refreshState();
			updating = false;
		}

		private JButton buildArrowButton(String text) {
			JButton button = new RoundedButton(text);
			button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
			button.setFocusPainted(false);
			button.setPreferredSize(new Dimension(42, 32));
			button.setMinimumSize(new Dimension(42, 32));
			button.setMargin(new Insets(2, 8, 2, 8));
			button.setBackground(new Color(0x17, 0x31, 0x74));
			button.setForeground(Color.WHITE);
			return button;
		}

		private void moveSelection(int direction) {
			int index = months.indexOf(selectedMonth);
			if (index < 0) {
				index = months.size() - 1;
			}
			int nextIndex = index + direction;
			if (nextIndex < 0 || nextIndex >= months.size()) {
				return;
			}
			selectedMonth = months.get(nextIndex);
			refreshState();
			if (!updating && changeListener != null) {
				changeListener.run();
			}
		}

		private void refreshState() {
			monthLabel.setText(monthLabel(selectedMonth));
			int index = months.indexOf(selectedMonth);
			previousButton.setEnabled(index > 0);
			nextButton.setEnabled(index >= 0 && index < months.size() - 1);
		}

		@Override
		public void applyTheme() {
			monthLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		}
	}

	protected class ThemedChartContainer extends JPanel implements ThemeAware {
		private final JFreeChart chart;
		private final ChartPanel chartPanel;

		protected ThemedChartContainer(JFreeChart chart, ChartPanel chartPanel, int preferredHeight) {
			super(new BorderLayout());
			this.chart = chart;
			this.chartPanel = chartPanel;
			setOpaque(false);
			add(chartPanel, BorderLayout.CENTER);
			setPreferredSize(new Dimension(360, preferredHeight));
		}

		@Override
		public void applyTheme() {
			applyChartTheme(chart);
			chartPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
			chartPanel.repaint();
		}
	}
}

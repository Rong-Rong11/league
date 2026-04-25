package gui.panel.financePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

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
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;

public final class FinanceViewFactory {

	private FinanceViewFactory() {
	}

	public static void styleComboBox(JComboBox<String> comboBox) {
		LabelStyleUtil.styleSubtitleLabel((JLabel) comboBox.getRenderer(), 13);
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

	public static JLabel metricLabel() {
		JLabel label = new JLabel("-");
		LabelStyleUtil.styleValueLabel(label, 18);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		return label;
	}

	public static JLabel infoLabel() {
		JLabel label = new JLabel("-");
		LabelStyleUtil.styleValueLabel(label, 12);
		return label;
	}

	public static JPanel metricCard(String title, Component component) {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

		JLabel titleLabel = new JLabel(title);
		LabelStyleUtil.styleSubtitleLabel(titleLabel, 11);
		titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		if (component instanceof JComponent) {
			((JComponent) component).setAlignmentX(Component.LEFT_ALIGNMENT);
		}

		card.add(titleLabel);
		card.add(Box.createVerticalStrut(4));
		card.add(component);
		return card;
	}

	public static JPanel infoRow(String title, JLabel valueLabel) {
		JPanel panel = new JPanel(new BorderLayout(8, 0));
		panel.setOpaque(false);

		JLabel titleLabel = new JLabel(title);
		LabelStyleUtil.styleSubtitleLabel(titleLabel, 12);

		panel.add(titleLabel, BorderLayout.WEST);
		panel.add(valueLabel, BorderLayout.EAST);
		return panel;
	}

	public static JPanel valueRow(String title, String value, Color valueColor) {
		JPanel row = new JPanel(new BorderLayout(8, 0));
		row.setOpaque(false);

		JLabel leftLabel = new JLabel(title);
		LabelStyleUtil.styleSubtitleLabel(leftLabel, 12);

		JLabel rightLabel = new JLabel(value);
		LabelStyleUtil.styleValueLabel(rightLabel, 12);
		rightLabel.setForeground(valueColor);

		row.add(leftLabel, BorderLayout.WEST);
		row.add(rightLabel, BorderLayout.EAST);
		return row;
	}

	public static JPanel metricListPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
		return panel;
	}

	public static JPanel infoPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
		return panel;
	}

	public static JPanel panelWithHeight(JPanel content, int preferredHeight) {
		content.setPreferredSize(new Dimension(content.getPreferredSize().width, preferredHeight));
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.add(content, BorderLayout.NORTH);
		return wrapper;
	}

	public static JPanel financeLineChart(DefaultCategoryDataset dataset, Color mainColor) {
		JFreeChart chart = ChartFactory.createLineChart(null, "", "Montant (M$)", dataset, PlotOrientation.VERTICAL,
				false, false, false);

		LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, true);
		CategoryPlot chartArea = chart.getCategoryPlot();
		chartArea.setRenderer(renderer);
		applyChartTheme(chart, mainColor);

		return wrapChart(chart, 190);
	}

	public static JPanel financeBarChart(DefaultCategoryDataset dataset, Color color) {
		JFreeChart chart = ChartFactory.createBarChart(null, "Categorie", "Montant (M$)", dataset,
				PlotOrientation.VERTICAL, false, false, false);

		CategoryPlot chartArea = chart.getCategoryPlot();
		BarRenderer renderer = (BarRenderer) chartArea.getRenderer();
		renderer.setBarPainter(new StandardBarPainter());
		renderer.setShadowVisible(false);
		renderer.setMaximumBarWidth(0.12);
		applyChartTheme(chart, color);

		return wrapChart(chart, 190);
	}

	public static JPanel financePieChart(DefaultPieDataset<String> dataset) {
		JFreeChart chart = ChartFactory.createPieChart(null, dataset, false, false, false);
		chart.setBackgroundPaint(DashboardPanelUtil.PANEL_SURFACE_COLOR);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		plot.setOutlineVisible(false);
		plot.setShadowPaint(null);
		plot.setLabelBackgroundPaint(null);
		plot.setLabelOutlinePaint(null);
		plot.setLabelShadowPaint(null);
		plot.setLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		plot.setLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		plot.setNoDataMessage("Aucune donnee financiere n'est disponible pour le moment.");
		plot.setNoDataMessagePaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		plot.setSectionPaint("TV", DashboardPanelUtil.REVENUE_COLOR);
		plot.setSectionPaint("Sponsoring", DashboardPanelUtil.POLICY_BALANCED_COLOR);
		plot.setSectionPaint("Merch", DashboardPanelUtil.STRATEGY_REBUILD_COLOR);

		return wrapChart(chart, 220);
	}

	private static void applyChartTheme(JFreeChart chart, Color mainColor) {
		chart.setBackgroundPaint(DashboardPanelUtil.PANEL_SURFACE_COLOR);

		CategoryPlot chartArea = chart.getCategoryPlot();
		styleChartArea(chartArea);
		styleAxes(chartArea);

		CategoryAxis domainAxis = chartArea.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
		domainAxis.setMaximumCategoryLabelLines(2);

		if (chartArea.getRenderer() instanceof LineAndShapeRenderer) {
			LineAndShapeRenderer renderer = (LineAndShapeRenderer) chartArea.getRenderer();
			renderer.setSeriesPaint(0, mainColor);
			if (chartArea.getDataset() != null && chartArea.getDataset().getRowCount() > 1) {
				renderer.setSeriesPaint(1, DashboardPanelUtil.EXPENSE_COLOR);
			}
		}

		if (chartArea.getRenderer() instanceof BarRenderer) {
			BarRenderer renderer = (BarRenderer) chartArea.getRenderer();
			Color barColor = mainColor;
			if (chartArea.getDataset() != null && chartArea.getDataset().getRowCount() > 0) {
				Comparable<?> rowKey = chartArea.getDataset().getRowKey(0);
				if (rowKey != null) {
					String text = rowKey.toString().toLowerCase();
					if (text.contains("depense")) {
						barColor = DashboardPanelUtil.EXPENSE_COLOR;
					}
					if (text.contains("revenu")) {
						barColor = DashboardPanelUtil.REVENUE_COLOR;
					}
				}
			}
			renderer.setSeriesPaint(0, barColor);
		}
	}

	private static void styleChartArea(CategoryPlot chartArea) {
		chartArea.setBackgroundPaint(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		chartArea.setOutlineVisible(false);
		chartArea.setRangeGridlinePaint(DashboardPanelUtil.getCalendarGridBorderColor());
		chartArea.setNoDataMessage("Aucune donnee financiere n'est disponible pour le moment.");
		chartArea.setNoDataMessagePaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
	}

	private static void styleAxes(CategoryPlot chartArea) {
		CategoryAxis domainAxis = chartArea.getDomainAxis();
		domainAxis.setTickLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		domainAxis.setLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		domainAxis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		domainAxis.setAxisLineVisible(false);
		domainAxis.setTickMarksVisible(false);

		NumberAxis rangeAxis = (NumberAxis) chartArea.getRangeAxis();
		rangeAxis.setTickLabelPaint(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		rangeAxis.setLabelPaint(DashboardPanelUtil.TITLE_TEXT_COLOR);
		rangeAxis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		rangeAxis.setAxisLineVisible(false);
		rangeAxis.setAutoRangeIncludesZero(true);
	}

	private static JPanel wrapChart(JFreeChart chart, int preferredHeight) {
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

	public static void refreshCharts(Component component) {
		if (component instanceof ChartPanel) {
			ChartPanel chartPanel = (ChartPanel) component;
			chartPanel.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
			if (chartPanel.getChart() != null) {
				applyChartTheme(chartPanel.getChart(), DashboardPanelUtil.REVENUE_COLOR);
			}
			chartPanel.repaint();
		}

		if (component instanceof Container) {
			Container container = (Container) component;
			for (Component child : container.getComponents()) {
				refreshCharts(child);
			}
		}
	}
}

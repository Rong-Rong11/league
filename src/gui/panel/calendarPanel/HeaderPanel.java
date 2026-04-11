package gui.panel.calendarPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.CustomProgressBar;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.RoundedPanel;
import gui.panel.common.ThemeAware;

public class HeaderPanel extends RoundedPanel implements ThemeAware {

	private static final Color NAVIGATION_BUTTON_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color CALENDAR_PROGRESS_COLOR = DashboardPanelUtil.ACCENT_RED_COLOR;

	private JLabel progressTitleLabel;
	private JLabel progressSubtitleLabel;
	private JLabel percentageLabel;
	private CustomProgressBar progressBar;

	private JButton simulateDayButton;
	private JButton simulateWeekButton;
	private JButton simulateSeasonButton;

	private JButton previousMonthButton;
	private JButton nextMonthButton;
	private JLabel monthLabel;
	private JButton previousWeekButton;
	private JButton nextWeekButton;
	private JLabel weekLabel;
	private JButton monthButton;
	private JButton weekButton;
	private JPanel navigationLeftPanel;
	private boolean monthViewSelectedState;

	public HeaderPanel() {
		create();
		organize();
	}

	private void create() {
		progressTitleLabel = new JLabel("Progression de la saison");
		progressSubtitleLabel = new JLabel("0 jours completes sur 0");
		percentageLabel = new JLabel("0%");
		progressBar = new CustomProgressBar(0, 100);

		simulateDayButton = new RoundedButton("Simuler Jour");
		simulateWeekButton = new RoundedButton("Simuler Semaine");
		simulateSeasonButton = new RoundedButton("Simuler Saison");

		previousMonthButton = new RoundedButton("<");
		nextMonthButton = new RoundedButton(">");
		monthLabel = new JLabel("-");
		previousWeekButton = new RoundedButton("<");
		nextWeekButton = new RoundedButton(">");
		weekLabel = new JLabel("-");
		monthButton = new RoundedButton("Mois");
		weekButton = new RoundedButton("Semaine");

		progressTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		progressSubtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		percentageLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		monthLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		weekLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
		weekLabel.setHorizontalAlignment(SwingConstants.CENTER);
		monthLabel.setPreferredSize(new Dimension(210, 36));
		monthLabel.setMinimumSize(new Dimension(210, 36));
		weekLabel.setPreferredSize(new Dimension(250, 36));
		weekLabel.setMinimumSize(new Dimension(250, 36));

		progressBar.setValue(0);
		progressBar.setPreferredSize(new Dimension(260, 14));
		progressBar.setFillColor(CALENDAR_PROGRESS_COLOR);
		progressBar.setCornerRadius(14);

		styleNavigationButton(previousMonthButton);
		styleNavigationButton(nextMonthButton);
		styleNavigationButton(previousWeekButton);
		styleNavigationButton(nextWeekButton);

		ButtonStyleUtil.styleToggleButton(monthButton);
		ButtonStyleUtil.styleToggleButton(weekButton);
	}

	private void styleNavigationButton(JButton button) {
		button.setBackground(NAVIGATION_BUTTON_COLOR);
		button.setForeground(Color.WHITE);
		button.setPreferredSize(new Dimension(42, 32));
	}

	private void stylePrimaryActionButton(JButton button) {
		button.setBackground(new Color(0x17, 0x31, 0x74));
		button.setForeground(Color.WHITE);
	}

	private void organize() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		setBorder(BorderFactory.createEmptyBorder(14, 16, 10, 16));

		add(buildTopRow());
		add(Box.createVerticalStrut(8));
		add(buildNavigationRow());
		applyTheme();
	}

	private JPanel buildTopRow() {
		JPanel row = new JPanel(new BorderLayout(12, 0));
		row.setOpaque(false);

		JPanel leftPanel = new JPanel(new BorderLayout(0, 8));
		leftPanel.setOpaque(false);

		JPanel leftTexts = new JPanel(new GridLayout(2, 1, 0, 2));
		leftTexts.setOpaque(false);
		leftTexts.add(progressTitleLabel);
		leftTexts.add(progressSubtitleLabel);

		JPanel progressWrapper = new JPanel(new BorderLayout());
		progressWrapper.setOpaque(false);
		progressWrapper.add(progressBar, BorderLayout.CENTER);

		leftPanel.add(leftTexts, BorderLayout.NORTH);
		leftPanel.add(progressWrapper, BorderLayout.CENTER);

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		rightPanel.setOpaque(false);
		rightPanel.add(percentageLabel);
		rightPanel.add(simulateDayButton);
		rightPanel.add(simulateWeekButton);
		rightPanel.add(simulateSeasonButton);

		row.add(leftPanel, BorderLayout.CENTER);
		row.add(rightPanel, BorderLayout.EAST);
		return row;
	}

	private JPanel buildNavigationRow() {
		JPanel row = new JPanel(new BorderLayout());
		row.setOpaque(false);

		navigationLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
		navigationLeftPanel.setOpaque(false);

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		rightPanel.setOpaque(false);
		rightPanel.add(monthButton);
		rightPanel.add(weekButton);

		row.add(navigationLeftPanel, BorderLayout.WEST);
		row.add(rightPanel, BorderLayout.EAST);
		return row;
	}

	public void setProgress(int completedDays, int totalDays) {
		int percentage = 0;
		if (totalDays > 0) {
			percentage = (completedDays * 100) / totalDays;
		}
		progressSubtitleLabel.setText(completedDays + " jours completes sur " + totalDays);
		percentageLabel.setText(percentage + "%");
		progressBar.setValue(percentage);
	}

	public void setMonthText(String text) {
		monthLabel.setText(text);
	}

	public void setWeekText(String text) {
		weekLabel.setText(text);
	}

	public void setMonthViewSelected(boolean selected) {
		monthViewSelectedState = selected;
		ButtonStyleUtil.setToggleButtonSelected(monthButton, selected);
		ButtonStyleUtil.setToggleButtonSelected(weekButton, !selected);
		refreshNavigation(selected);
	}

	private void refreshNavigation(boolean monthSelected) {
		navigationLeftPanel.removeAll();
		if (monthSelected) {
			navigationLeftPanel.add(previousMonthButton);
			navigationLeftPanel.add(monthLabel);
			navigationLeftPanel.add(nextMonthButton);
		} else {
			navigationLeftPanel.add(previousWeekButton);
			navigationLeftPanel.add(weekLabel);
			navigationLeftPanel.add(nextWeekButton);
		}
		navigationLeftPanel.revalidate();
		navigationLeftPanel.repaint();
	}

	public void setSimulateDayAction(ActionListener actionListener) {
		simulateDayButton.addActionListener(actionListener);
	}

	public void setSimulateWeekAction(ActionListener actionListener) {
		simulateWeekButton.addActionListener(actionListener);
	}

	public void setSimulateSeasonAction(ActionListener actionListener) {
		simulateSeasonButton.addActionListener(actionListener);
	}

	public void setPreviousMonthAction(ActionListener actionListener) {
		previousMonthButton.addActionListener(actionListener);
	}

	public void setNextMonthAction(ActionListener actionListener) {
		nextMonthButton.addActionListener(actionListener);
	}

	public void setPreviousWeekAction(ActionListener actionListener) {
		previousWeekButton.addActionListener(actionListener);
	}

	public void setNextWeekAction(ActionListener actionListener) {
		nextWeekButton.addActionListener(actionListener);
	}

	public void setMonthToggleAction(ActionListener actionListener) {
		monthButton.addActionListener(actionListener);
	}

	public void setWeekToggleAction(ActionListener actionListener) {
		weekButton.addActionListener(actionListener);
	}

	@Override
	public void applyTheme() {
		if (navigationLeftPanel == null) {
			return;
		}
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		progressTitleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		progressSubtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		percentageLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		monthLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		weekLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		progressBar.setFillColor(CALENDAR_PROGRESS_COLOR);
		progressBar.applyTheme();
		stylePrimaryActionButton(simulateDayButton);
		stylePrimaryActionButton(simulateWeekButton);
		stylePrimaryActionButton(simulateSeasonButton);
		setMonthViewSelected(monthViewSelectedState);
	}
}

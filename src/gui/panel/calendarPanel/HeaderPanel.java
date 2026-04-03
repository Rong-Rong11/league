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
import javax.swing.JProgressBar;

import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.RoundedPanel;

public class HeaderPanel extends RoundedPanel {

	private static final Color BACKGROUND_COLOR = DashboardPanelUtil.PANEL_SURFACE_COLOR;
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);
	private static final Color PROGRESS_COLOR = new Color(0x2F, 0x80, 0xA9);

	private JLabel progressTitleLabel;
	private JLabel progressSubtitleLabel;
	private JLabel percentageLabel;
	private JProgressBar progressBar;

	private JButton simulateDayButton;
	private JButton simulateWeekButton;
	private JButton simulateSeasonButton;

	private JButton previousMonthButton;
	private JButton nextMonthButton;
	private JLabel monthLabel;
	private JButton monthButton;
	private JButton weekButton;

	public HeaderPanel() {
		create();
		organize();
	}

	private void create() {
		progressTitleLabel = new JLabel("Progression de la saison");
		progressSubtitleLabel = new JLabel("0 jours complétés sur 0");
		percentageLabel = new JLabel("0%");
		progressBar = new JProgressBar(0, 100);

		simulateDayButton = new RoundedButton("Simuler Jour");
		simulateWeekButton = new RoundedButton("Simuler Semaine");
		simulateSeasonButton = new RoundedButton("Simuler Saison");

		previousMonthButton = new RoundedButton("<");
		nextMonthButton = new RoundedButton(">");
		monthLabel = new JLabel("-");
		monthButton = new RoundedButton("Mois");
		weekButton = new RoundedButton("Semaine");

		progressTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		progressSubtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		percentageLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		monthLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
		monthLabel.setPreferredSize(new Dimension(210, 36));
		monthLabel.setMinimumSize(new Dimension(210, 36));

		progressTitleLabel.setForeground(TITLE_COLOR);
		progressSubtitleLabel.setForeground(SUBTITLE_COLOR);
		percentageLabel.setForeground(TITLE_COLOR);
		monthLabel.setForeground(TITLE_COLOR);

		progressBar.setValue(0);
		progressBar.setStringPainted(false);
		progressBar.setForeground(PROGRESS_COLOR);
		progressBar.setBackground(new Color(0xE3, 0xE8, 0xEE));
		progressBar.setPreferredSize(new Dimension(260, 14));
		progressBar.setBorder(BorderFactory.createEmptyBorder());
		ButtonStyleUtil.styleToggleButton(monthButton);
		ButtonStyleUtil.styleToggleButton(weekButton);
	}

	private void organize() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(BACKGROUND_COLOR);
		setBorder(BorderFactory.createEmptyBorder(14, 16, 10, 16));

		add(buildTopRow());
		add(Box.createVerticalStrut(8));
		add(buildNavigationRow());
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

		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
		leftPanel.setOpaque(false);
		leftPanel.add(previousMonthButton);
		leftPanel.add(monthLabel);
		leftPanel.add(nextMonthButton);

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		rightPanel.setOpaque(false);
		rightPanel.add(monthButton);
		rightPanel.add(weekButton);

		row.add(leftPanel, BorderLayout.WEST);
		row.add(rightPanel, BorderLayout.EAST);
		return row;
	}

	public void setProgress(int completedDays, int totalDays) {
		int percentage = 0;
		if (totalDays > 0) {
			percentage = (completedDays * 100) / totalDays;
		}
		progressSubtitleLabel.setText(completedDays + " jours complétés sur " + totalDays);
		percentageLabel.setText(percentage + "%");
		progressBar.setValue(percentage);
	}

	public void setMonthText(String text) {
		monthLabel.setText(text);
	}

	public void setMonthViewSelected(boolean selected) {
		ButtonStyleUtil.setToggleButtonSelected(monthButton, selected);
		ButtonStyleUtil.setToggleButtonSelected(weekButton, !selected);
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

	public void setMonthToggleAction(ActionListener actionListener) {
		monthButton.addActionListener(actionListener);
	}

	public void setWeekToggleAction(ActionListener actionListener) {
		weekButton.addActionListener(actionListener);
	}
}

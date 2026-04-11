package gui.panel.matchPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.sport.setup.GameResult;
import gui.panel.common.CustomProgressBar;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;

public class MatchStatPanel extends JPanel implements ThemeAware {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);
	private static final Color PRIMARY_BAR_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SECONDARY_BAR_COLOR = DashboardPanelUtil.ACCENT_RED_COLOR;

	private ComparisonBarPanel madeShotsBar;
	private ComparisonBarPanel threePointsBar;
	private ComparisonBarPanel freeThrowsBar;
	private ComparisonBarPanel reboundsBar;
	private JLabel attendanceSummaryLabel;
	private JLabel attendanceRateLabel;
	private CustomProgressBar attendanceBar;
	private JLabel statsTitleLabel;
	private JLabel attendanceTitleLabel;

	public MatchStatPanel() {
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(buildStatsPanel());
		add(buildAttendancePanel());
		applyTheme();
	}

	public void showStats(GameResult[] quarterResults) {
		int twoHome = 0;
		int twoAway = 0;
		int threeHome = 0;
		int threeAway = 0;
		int freeHome = 0;
		int freeAway = 0;
		int rebHome = 0;
		int rebAway = 0;

		if (quarterResults != null) {
			for (int i = 0; i < quarterResults.length; i++) {
				GameResult quarter = quarterResults[i];
				if (quarter != null) {
					twoHome += quarter.getTwoPointsHomeTeam();
					twoAway += quarter.getTwoPointsAwayTeam();
					threeHome += quarter.getThreePointsHomeTeam();
					threeAway += quarter.getThreePointsAwayTeam();
					freeHome += quarter.getFreeThrowHomeTeam();
					freeAway += quarter.getFreeThrowAwayTeam();
					rebHome += quarter.getReboundHomeTeam();
					rebAway += quarter.getReboundAwayTeam();
				}
			}
		}

		madeShotsBar.updateValues(twoHome + threeHome, twoAway + threeAway);
		threePointsBar.updateValues(threeHome, threeAway);
		freeThrowsBar.updateValues(freeHome, freeAway);
		reboundsBar.updateValues(rebHome, rebAway);
	}

	public void showAttendance(Game game, GameStat gameStat) {
		if (game == null || gameStat == null) {
			showEmptyState();
			return;
		}
		int capacity = game.getGameContext().getHomeTeam().getStadium().getCapacity();
		int rate = capacity == 0 ? 0 : (int) Math.round((gameStat.getAttendees() * 100.0) / capacity);
		attendanceSummaryLabel.setText(gameStat.getAttendees() + " spectateurs");
		attendanceRateLabel.setText(rate + "%");
		attendanceBar.setValue(rate);
	}

	public void showEmptyState() {
		madeShotsBar.updateValues(0, 0);
		threePointsBar.updateValues(0, 0);
		freeThrowsBar.updateValues(0, 0);
		reboundsBar.updateValues(0, 0);
		attendanceSummaryLabel.setText("-");
		attendanceRateLabel.setText("-");
		attendanceBar.setValue(0);
	}

	private JPanel buildStatsPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

		statsTitleLabel = new JLabel("STATISTIQUES DU MATCH");
		statsTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		statsTitleLabel.setForeground(TITLE_COLOR);
		statsTitleLabel.setAlignmentX(CENTER_ALIGNMENT);

		madeShotsBar = new ComparisonBarPanel("Tirs reussis");
		threePointsBar = new ComparisonBarPanel("Tirs a 3 points");
		freeThrowsBar = new ComparisonBarPanel("Lancers francs");
		reboundsBar = new ComparisonBarPanel("Rebonds");

		panel.add(statsTitleLabel);
		panel.add(Box.createVerticalStrut(8));
		panel.add(madeShotsBar);
		panel.add(Box.createVerticalStrut(8));
		panel.add(threePointsBar);
		panel.add(Box.createVerticalStrut(8));
		panel.add(freeThrowsBar);
		panel.add(Box.createVerticalStrut(8));
		panel.add(reboundsBar);
		return panel;
	}

	private JPanel buildAttendancePanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

		attendanceTitleLabel = new JLabel("AFFLUENCE");
		attendanceTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		attendanceTitleLabel.setForeground(TITLE_COLOR);
		attendanceTitleLabel.setAlignmentX(CENTER_ALIGNMENT);

		JPanel summary = new JPanel(new BorderLayout());
		summary.setOpaque(false);
		attendanceSummaryLabel = new JLabel("-");
		attendanceRateLabel = new JLabel("-");
		attendanceRateLabel.setForeground(TITLE_COLOR);
		attendanceRateLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		summary.add(attendanceSummaryLabel, BorderLayout.WEST);
		summary.add(attendanceRateLabel, BorderLayout.EAST);

		attendanceBar = new CustomProgressBar(0, 100);
		attendanceBar.setValue(0);
		attendanceBar.setFillColor(PRIMARY_BAR_COLOR);
		attendanceBar.setCornerRadius(10);

		panel.add(attendanceTitleLabel);
		panel.add(Box.createVerticalStrut(8));
		panel.add(summary);
		panel.add(Box.createVerticalStrut(8));
		panel.add(attendanceBar);
		return panel;
	}

	private class ComparisonBarPanel extends JPanel {
		private JLabel leftValueLabel;
		private JLabel rightValueLabel;
		private JLabel titleLabel;
		private ComparisonProgressBar progressBar;

		private ComparisonBarPanel(String title) {
			super(new BorderLayout(0, 6));
			setOpaque(false);

			JPanel header = new JPanel(new BorderLayout());
			header.setOpaque(false);

			leftValueLabel = new JLabel("0");
			leftValueLabel.setForeground(TITLE_COLOR);
			leftValueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

			titleLabel = new JLabel(title, JLabel.CENTER);
			titleLabel.setForeground(SUBTITLE_COLOR);
			titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

			rightValueLabel = new JLabel("0", JLabel.RIGHT);
			rightValueLabel.setForeground(TITLE_COLOR);
			rightValueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

			header.add(leftValueLabel, BorderLayout.WEST);
			header.add(titleLabel, BorderLayout.CENTER);
			header.add(rightValueLabel, BorderLayout.EAST);

			progressBar = new ComparisonProgressBar();

			add(header, BorderLayout.NORTH);
			add(progressBar, BorderLayout.CENTER);
		}

		private void updateValues(int homeValue, int awayValue) {
			leftValueLabel.setText(String.valueOf(homeValue));
			rightValueLabel.setText(String.valueOf(awayValue));
			progressBar.setValues(homeValue, awayValue);
		}

		private void applyTheme() {
			leftValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
			rightValueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
			titleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
			progressBar.applyTheme();
		}
	}

	private class ComparisonProgressBar extends JPanel implements ThemeAware {
		private int homeValue;
		private int awayValue;

		private ComparisonProgressBar() {
			setOpaque(false);
			setPreferredSize(new Dimension(260, 12));
		}

		private void setValues(int homeValue, int awayValue) {
			this.homeValue = homeValue;
			this.awayValue = awayValue;
			repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int width = getWidth();
			int height = getHeight();
			if (width <= 0 || height <= 0) {
				return;
			}

			g2.setColor(getTrackColor());
			g2.fillRoundRect(0, 0, width, height, 10, 10);

			int total = homeValue + awayValue;
			if (total <= 0) {
				return;
			}

			int homeWidth = (int) Math.round((homeValue * width) / (double) total);
			int awayWidth = width - homeWidth;

			if (homeWidth > 0) {
				g2.setColor(PRIMARY_BAR_COLOR);
				g2.fillRoundRect(0, 0, homeWidth, height, 10, 10);
			}
			if (awayWidth > 0) {
				g2.setColor(SECONDARY_BAR_COLOR);
				g2.fillRoundRect(width - awayWidth, 0, awayWidth, height, 10, 10);
			}
		}

		private Color getTrackColor() {
			if (DashboardPanelUtil.isDarkMode()) {
				return new Color(53, 58, 68);
			}
			return new Color(227, 232, 238);
		}

		@Override
		public void applyTheme() {
			repaint();
		}
	}

	@Override
	public void applyTheme() {
		statsTitleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		attendanceTitleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		attendanceSummaryLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		attendanceRateLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		attendanceBar.applyTheme();
		madeShotsBar.applyTheme();
		threePointsBar.applyTheme();
		freeThrowsBar.applyTheme();
		reboundsBar.applyTheme();
	}
}

package gui.panel.calendarPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.calendar.GameDay;
import data.sport.setup.Game;
import gui.dashboard.MatchDashboard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.RoundedPanel;
import gui.panel.common.ThemeAware;
import process.utility.CalendarUtility;
import gui.utility.TeamDisplayUtility;

public class MonthViewPanel extends JPanel implements ThemeAware {
	private static final String[] DAY_NAMES = { "LUN", "MAR", "MER", "JEU", "VEN", "SAM", "DIM" };
	private MatchDashboard matchDashboard;
	private Runnable showMatchDashboardAction;

	public MonthViewPanel() {
		setLayout(new GridLayout(0, 7));
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
	}

	public void showMonth(YearMonth displayedMonth, LocalDate currentDate, HashMap<LocalDate, GameDay> calendar) {
		removeAll();

		for (int i = 0; i < DAY_NAMES.length; i++) {
			add(buildDayNameLabel(DAY_NAMES[i]));
		}

		LocalDate firstDayOfMonth = displayedMonth.atDay(1);
		int firstDayColumn = firstDayOfMonth.getDayOfWeek().getValue();
		int dayOffset = firstDayColumn - 1;
		LocalDate firstDateShown = firstDayOfMonth.minusDays(dayOffset);

		for (int i = 0; i < 42; i++) {
			LocalDate date = firstDateShown.plusDays(i);
			GameDay gameDay = null;
			if (calendar != null) {
				gameDay = calendar.get(date);
			}
			add(buildDayPanel(date, displayedMonth, gameDay, currentDate));
		}

		revalidate();
		repaint();
	}

	private JLabel buildDayNameLabel(String text) {
		JLabel label = new JLabel(text, JLabel.CENTER);
		label.setOpaque(true);
		label.setBackground(DashboardPanelUtil.getCalendarHeaderBackgroundColor());
		label.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
		LabelStyleUtil.styleValueLabel(label, 15);
		label.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
		label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, getGridColor()));
		return label;
	}

	private JPanel buildDayPanel(LocalDate date, YearMonth displayedMonth, GameDay gameDay, LocalDate currentDate) {
		JPanel dayPanel = new JPanel(new BorderLayout(0, 8));
		dayPanel.setOpaque(true);
		dayPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, getGridColor()));
		dayPanel.setBackground(getDefaultDayBackground());
		boolean sameMonth = isSameMonth(date, displayedMonth);

		if (sameMonth && gameDay != null && !gameDay.isEmpty()) {
			dayPanel.addMouseListener(new DayClickListener(gameDay, date));
		}

		if (gameDay != null && gameDay.isDisplayed()) {
			dayPanel.setBackground(getDisplayedDayBackground());
		}
		if (!sameMonth) {
			dayPanel.setBackground(getOtherMonthBackground());
		}

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setOpaque(false);
		topPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));

		JLabel dayNumberLabel = new JLabel(String.valueOf(date.getDayOfMonth()), JLabel.CENTER);
		LabelStyleUtil.styleValueLabel(dayNumberLabel, 16);
		if (!sameMonth) {
			dayNumberLabel.setForeground(getOutsideMonthTextColor());
		} else {
			dayNumberLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		}
		if (date.equals(currentDate)) {
			RoundedPanel currentDayBadge = new RoundedPanel(18);
			currentDayBadge.setLayout(new BorderLayout());
			currentDayBadge.setBackground(DashboardPanelUtil.ACCENT_RED_COLOR);
			currentDayBadge.setPreferredSize(new Dimension(34, 28));
			currentDayBadge.setMinimumSize(new Dimension(34, 28));
			currentDayBadge.setMaximumSize(new Dimension(34, 28));
			dayNumberLabel.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
			currentDayBadge.add(dayNumberLabel, BorderLayout.CENTER);
			topPanel.add(currentDayBadge, BorderLayout.WEST);
		} else {
			topPanel.add(dayNumberLabel, BorderLayout.WEST);
		}
		dayPanel.add(topPanel, BorderLayout.NORTH);

		if (gameDay != null && !gameDay.isEmpty() && sameMonth) {
			JPanel matchesPanel = new JPanel();
			matchesPanel.setOpaque(false);
			matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));
			matchesPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));

			ArrayList<Game> displayedGames = getBestGames(gameDay.getGames(), date);
			int matchCount = Math.min(2, displayedGames.size());
			for (int i = 0; i < matchCount; i++) {
				String homeTeam = TeamDisplayUtility.getAbbreviation(displayedGames.get(i).getGameContext().getHomeTeam());
				String awayTeam = TeamDisplayUtility.getAbbreviation(displayedGames.get(i).getGameContext().getAwayTeam());
				boolean hasBottomSpacing = i < matchCount - 1;
				matchesPanel.add(buildMatchLabel(homeTeam + " vs " + awayTeam, hasBottomSpacing ? 4 : 0));
			}
			dayPanel.add(matchesPanel, BorderLayout.CENTER);
		}

		return dayPanel;
	}

	private JLabel buildMatchLabel(String text, int bottomSpacing) {
		JLabel label = new JLabel(text);
		label.setOpaque(true);
		label.setBackground(getMatchChipColor());
		LabelStyleUtil.styleValueLabel(label, 11);
		label.setBorder(BorderFactory.createEmptyBorder(3, 6, 3 + bottomSpacing, 6));
		label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		return label;
	}

	private boolean isSameMonth(LocalDate date, YearMonth displayedMonth) {
		return date.getYear() == displayedMonth.getYear()
				&& date.getMonthValue() == displayedMonth.getMonthValue();
	}

	public static String buildMonthText(YearMonth yearMonth) {
		String monthText = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH) + " " + yearMonth.getYear();
		return monthText;
	}

	public void setMatchDashboard(MatchDashboard matchDashboard, Runnable showMatchDashboardAction) {
		this.matchDashboard = matchDashboard;
		this.showMatchDashboardAction = showMatchDashboardAction;
	}

	private Color getGridColor() {
		return DashboardPanelUtil.getCalendarGridBorderColor();
	}

	private Color getDefaultDayBackground() {
		return DashboardPanelUtil.getCalendarCellBackgroundColor();
	}

	private Color getDisplayedDayBackground() {
		return DashboardPanelUtil.getCalendarDisplayedDayBackgroundColor();
	}

	private Color getOtherMonthBackground() {
		return DashboardPanelUtil.getCalendarOtherMonthBackgroundColor();
	}

	private Color getOutsideMonthTextColor() {
		return DashboardPanelUtil.getCalendarOutsideMonthTextColor();
	}

	private Color getMatchChipColor() {
		return DashboardPanelUtil.getCalendarMatchChipColor();
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
	}

	private ArrayList<Game> getBestGames(ArrayList<Game> games, LocalDate date) {
		ArrayList<Game> remainingGames = new ArrayList<Game>(games);
		ArrayList<Game> bestGames = new ArrayList<Game>();

		while (!remainingGames.isEmpty() && bestGames.size() < 2) {
			Game bestGame = remainingGames.get(0);

			for (int i = 1; i < remainingGames.size(); i++) {
				double currentScore = CalendarUtility.popularityScoreGame(remainingGames.get(i), date);
				double bestScore = CalendarUtility.popularityScoreGame(bestGame, date);

				if (currentScore > bestScore) {
					bestGame = remainingGames.get(i);
				}
			}

			bestGames.add(bestGame);
			remainingGames.remove(bestGame);
		}

		return bestGames;
	}

	private class DayClickListener implements MouseListener {
		private GameDay gameDay;
		private LocalDate date;

		private DayClickListener(GameDay gameDay, LocalDate date) {
			this.gameDay = gameDay;
			this.date = date;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (matchDashboard != null) {
				matchDashboard.showGameDay(gameDay, date);
			}
			if (showMatchDashboardAction != null) {
				showMatchDashboardAction.run();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}

}

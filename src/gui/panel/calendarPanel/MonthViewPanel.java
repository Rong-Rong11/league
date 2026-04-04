package gui.panel.calendarPanel;

import data.calendar.GameDay;
import data.sport.setup.Game;
import gui.dashboard.MatchDashboard;
import process.utility.CalendarUtilitary;
import process.utility.TeamDisplayUtil;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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

public class MonthViewPanel extends JPanel {
	private static final String[] DAY_NAMES = { "LUN", "MAR", "MER", "JEU", "VEN", "SAM", "DIM" };
	private static final Color GRID_COLOR = new Color(220, 224, 230);
	private static final Color HEADER_BACKGROUND = new Color(234, 240, 248);
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(0x6D, 0x75, 0x83);
	private static final Color CURRENT_DAY_COLOR = new Color(0x2F, 0x80, 0xA9);
	private static final Color DISPLAYED_DAY_COLOR = new Color(245, 247, 250);
	private static final Color OTHER_MONTH_COLOR = new Color(245, 246, 248);
	private static final Color MATCH_CHIP_COLOR = new Color(236, 242, 250);
	private MatchDashboard matchDashboard;
	private Runnable showMatchDashboardAction;

	public MonthViewPanel() {
		setLayout(new GridLayout(0, 7));
		setBackground(Color.WHITE);
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
		label.setBackground(HEADER_BACKGROUND);
		label.setForeground(TITLE_COLOR);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, GRID_COLOR));
		return label;
	}

	private JPanel buildDayPanel(LocalDate date, YearMonth displayedMonth, GameDay gameDay, LocalDate currentDate) {
		JPanel dayPanel = new JPanel(new BorderLayout(0, 8));
		dayPanel.setOpaque(true);
		dayPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, GRID_COLOR));
		dayPanel.setBackground(Color.WHITE);
		boolean sameMonth = isSameMonth(date, displayedMonth);

		if (sameMonth && gameDay != null && !gameDay.isEmpty()) {
			dayPanel.addMouseListener(new DayClickListener(gameDay, date));
		}

		if (gameDay != null && gameDay.isDisplayed()) {
			dayPanel.setBackground(DISPLAYED_DAY_COLOR);
		}
		if (!sameMonth) {
			dayPanel.setBackground(OTHER_MONTH_COLOR);
		}

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setOpaque(false);
		topPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));

		JLabel dayNumberLabel = new JLabel(String.valueOf(date.getDayOfMonth()));
		dayNumberLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		if (!sameMonth) {
			dayNumberLabel.setForeground(new Color(180, 185, 193));
		} else {
			dayNumberLabel.setForeground(TITLE_COLOR);
		}
		if (date.equals(currentDate)) {
			dayNumberLabel.setOpaque(true);
			dayNumberLabel.setBackground(CURRENT_DAY_COLOR);
			dayNumberLabel.setForeground(Color.WHITE);
			dayNumberLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
		}
		topPanel.add(dayNumberLabel, BorderLayout.WEST);
		dayPanel.add(topPanel, BorderLayout.NORTH);

		if (gameDay != null && !gameDay.isEmpty() && sameMonth) {
			JPanel matchesPanel = new JPanel();
			matchesPanel.setOpaque(false);
			matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));
			matchesPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));

			ArrayList<Game> displayedGames = getBestGames(gameDay.getGames(), date);
			int matchCount = Math.min(2, displayedGames.size());
			for (int i = 0; i < matchCount; i++) {
				String homeTeam = TeamDisplayUtil.getAbbreviation(displayedGames.get(i).getGameContext().getHomeTeam());
				String awayTeam = TeamDisplayUtil.getAbbreviation(displayedGames.get(i).getGameContext().getAwayTeam());
				boolean hasBottomSpacing = i < matchCount - 1 || gameDay.getGames().size() - matchCount > 0;
				matchesPanel.add(buildMatchLabel(homeTeam + " vs " + awayTeam, hasBottomSpacing ? 4 : 0));
			}

			int remainingMatches = gameDay.getGames().size() - matchCount;
			if (remainingMatches > 0) {
				JLabel otherLabel = new JLabel("+" + remainingMatches + " autres");
				otherLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
				otherLabel.setForeground(SUBTITLE_COLOR);
				int topPadding = 0;
				if (matchCount > 0) {
					topPadding = 2;
				}
				otherLabel.setBorder(BorderFactory.createEmptyBorder(topPadding, 0, 0, 0));
				matchesPanel.add(otherLabel);
			}
			dayPanel.add(matchesPanel, BorderLayout.CENTER);
		}

		return dayPanel;
	}

	private JLabel buildMatchLabel(String text, int bottomSpacing) {
		JLabel label = new JLabel(text);
		label.setOpaque(true);
		label.setBackground(MATCH_CHIP_COLOR);
		label.setForeground(TITLE_COLOR);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
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

	private ArrayList<Game> getBestGames(ArrayList<Game> games, LocalDate date) {
		ArrayList<Game> remainingGames = new ArrayList<Game>(games);
		ArrayList<Game> bestGames = new ArrayList<Game>();

		while (!remainingGames.isEmpty() && bestGames.size() < 2) {
			Game bestGame = remainingGames.get(0);

			for (int i = 1; i < remainingGames.size(); i++) {
				double currentScore = CalendarUtilitary.popularityScoreGame(remainingGames.get(i), date);
				double bestScore = CalendarUtilitary.popularityScoreGame(bestGame, date);

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

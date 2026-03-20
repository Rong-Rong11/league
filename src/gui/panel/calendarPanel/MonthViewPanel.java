package gui.panel.calendarPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.calendar.GameDay;
import gui.panel.common.TeamDisplayUtil;

public class MonthViewPanel extends JPanel {

	private static final String[] DAY_NAMES = {"LUN", "MAR", "MER", "JEU", "VEN", "SAM", "DIM"};

	public MonthViewPanel() {
		setLayout(new GridLayout(0, 7));
		setBackground(Color.WHITE);
	}

	public void showMonth(YearMonth displayedMonth, LocalDate currentDate, TreeMap<LocalDate, GameDay> calendar) {
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
			add(buildDayPanel(date, displayedMonth, gameDay));
		}

		repaint();
	}

	private JLabel buildDayNameLabel(String text) {
		JLabel label = new JLabel(text, JLabel.CENTER);
		label.setOpaque(true);
		label.setBackground(Color.WHITE);
		label.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
		return label;
	}

	private JPanel buildDayPanel(LocalDate date, YearMonth displayedMonth, GameDay gameDay) {
		JPanel dayPanel = new JPanel();
		dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
		dayPanel.setOpaque(true);
		dayPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
		dayPanel.setBackground(Color.WHITE);

		if (gameDay != null && gameDay.isDisplayed()) {
			dayPanel.setBackground(new Color(230, 230, 230));
		}

		JLabel dayNumberLabel = new JLabel(String.valueOf(date.getDayOfMonth()));
		if (!YearMonth.from(date).equals(displayedMonth)) {
			dayNumberLabel.setForeground(Color.LIGHT_GRAY);
		}
		dayPanel.add(dayNumberLabel);

		if (gameDay != null && !gameDay.isEmpty() && YearMonth.from(date).equals(displayedMonth)) {
			int matchCount = Math.min(3, gameDay.getGames().size());
			for (int i = 0; i < matchCount; i++) {
				String homeTeam = TeamDisplayUtil.getAbbreviation(gameDay.getGames().get(i).getGameContext().getHomeTeam().getName());
				String awayTeam = TeamDisplayUtil.getAbbreviation(gameDay.getGames().get(i).getGameContext().getAwayTeam().getName());
				dayPanel.add(new JLabel(homeTeam + " vs " + awayTeam));
			}

			int remainingMatches = gameDay.getGames().size() - matchCount;
			if (remainingMatches > 0) {
				dayPanel.add(new JLabel("+" + remainingMatches + " autres"));
			}
		}

		return dayPanel;
	}

	public static String buildMonthText(YearMonth yearMonth) {
		String monthText = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH) + " " + yearMonth.getYear();
		return monthText;
	}

}

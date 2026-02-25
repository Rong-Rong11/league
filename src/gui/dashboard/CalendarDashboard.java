package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.calendar.GameDay;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.sport.setup.GameResult;
import process.LeagueManager;

public class CalendarDashboard extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Font DISPLAY_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);

	private JButton previousDayButton = new JButton("Jour -");
	private JButton simulateDayButton = new JButton("Simuler jour");
	private JButton nextDayButton = new JButton("Jour +");

	private JLabel currentDateLabel = new JLabel();

	private LeagueManager leagueManager;
	private RegularSeason regularSeason;
	private LocalDate currentDate;
	private GameDay currentGameDay;

	public CalendarDashboard() {

		setLayout(new BorderLayout());

		leagueManager = new LeagueManager();
		leagueManager.buildLeague();
		leagueManager.buildRegularSeasonCalendar();

		regularSeason = leagueManager.getLeague().getReagularSeason();
		currentDate = regularSeason.getDebutDate();

		JPanel topBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		currentDateLabel.setFont(DISPLAY_FONT);

		previousDayButton.addActionListener(new PreviousDayListener());
		simulateDayButton.addActionListener(new simulateCurrentDayListener());
		nextDayButton.addActionListener(new NextDayListener());

		topBarPanel.add(previousDayButton);
		topBarPanel.add(simulateDayButton);
		topBarPanel.add(nextDayButton);
		topBarPanel.add(currentDateLabel);

		add(topBarPanel, BorderLayout.NORTH);

		updateDisplay();
	}

	private void updateDisplay() {
		currentGameDay = regularSeason.getCalendar().getCalendar().get(currentDate);
		currentDateLabel.setText("Date : " + currentDate);
		repaint();
	}

	private class PreviousDayListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentDate.isAfter(regularSeason.getDebutDate())) {
				currentDate = currentDate.minusDays(1);
				updateDisplay();
			}
		}
	}

	private class NextDayListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentDate.isBefore(regularSeason.getEndDate())) {
				currentDate = currentDate.plusDays(1);
				updateDisplay();
			}
		}
	}

	private class simulateCurrentDayListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			leagueManager.simulateDay(currentDate, regularSeason);
			updateDisplay();

		}
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		graphics.setFont(DISPLAY_FONT);

		int yPosition = 60;

		if (currentGameDay == null || currentGameDay.isEmpty()) {
			graphics.drawString("Aucun match ce jour.", 20, yPosition);
			return;
		}

		for (Game game : currentGameDay.getGames()) {

			String matchLine = game.getGameContext().getAwayTeam().getName()
					+ " vs "
					+ game.getGameContext().getHomeTeam().getName();

			if (currentGameDay.isSimulated()) {
				matchLine += " | " + game.getHomeFinalScore() + " - " + game.getAwayFinalScore();
			} else {
				matchLine += " | non simulé";
			}

			graphics.drawString(matchLine, 20, yPosition += 30);

			if (currentGameDay.isSimulated()) {
				int threePointsHome = 0;
				int twoPointsHome = 0;
				int freeThrowsHome = 0;
				int threePointsAway = 0;
				int twoPointsAway = 0;
				int freeThrowsAway = 0;

				for (GameResult quarter : game.getQuarterResults()) {
					threePointsHome += quarter.getThreePointsHomeTeam();
					twoPointsHome += quarter.getTwoPointsHomeTeam();
					freeThrowsHome += quarter.getFreeThrowHomeTeam();

					threePointsAway += quarter.getThreePointsAwayTeam();
					twoPointsAway += quarter.getTwoPointsAwayTeam();
					freeThrowsAway += quarter.getFreeThrowAwayTeam();

				}

				String pointsDetail = "Détail: " + game.getGameContext().getHomeTeam().getName() + " [3P: "
						+ threePointsHome + ", 2P: " + twoPointsHome + ", LF: " + freeThrowsHome + "]";
				graphics.drawString(pointsDetail, 40, yPosition += 15);

				pointsDetail = game.getGameContext().getAwayTeam().getName() + " [3P: " + threePointsAway + ", 2P: "
						+ twoPointsAway + ", LF: " + freeThrowsAway + "]";
				graphics.drawString(pointsDetail, 40, yPosition += 15);

			}
		}
	}
}

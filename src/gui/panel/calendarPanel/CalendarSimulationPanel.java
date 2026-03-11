package gui.panel.calendarPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.calendar.GameDay;
import data.league.RegularSeason;
import data.sport.setup.Game;
import process.manager.LeagueManager;
import process.simulator.GameSimulator;

public class CalendarSimulationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Font DISPLAY_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
	private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	private static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);

	private final JButton previousDayButton = new JButton("Jour -");
	private final JButton simulateDayButton = new JButton("Simuler");
	private final JButton nextDayButton = new JButton("Jour +");
	private final JLabel currentDateLabel = new JLabel();
	private final JPanel matchDisplayPanel = new JPanel();

	private LeagueManager leagueManager;
	private GameSimulator gameSimulator;
	private RegularSeason regularSeason;
	private LocalDate currentDate;
	private GameDay currentGameDay;
	private SeasonProgressBarPanel seasonProgressBarPanel;

	public CalendarSimulationPanel() {
		setLayout(new BorderLayout());

		leagueManager = new LeagueManager();
		gameSimulator = new GameSimulator();

		JPanel topBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		currentDateLabel.setFont(DISPLAY_FONT);
		currentDateLabel.setText("Date : -");

		previousDayButton.addActionListener(new PreviousDayListener());
		simulateDayButton.addActionListener(new SimulateCurrentDayListener());
		nextDayButton.addActionListener(new NextDayListener());

		topBarPanel.add(previousDayButton);
		topBarPanel.add(currentDateLabel);
		topBarPanel.add(simulateDayButton);
		topBarPanel.add(nextDayButton);

		matchDisplayPanel.setLayout(new BoxLayout(matchDisplayPanel, BoxLayout.Y_AXIS));
		matchDisplayPanel.setOpaque(false);

		add(topBarPanel, BorderLayout.NORTH);
		add(matchDisplayPanel, BorderLayout.CENTER);

		showWaitingState();
	}

	public LeagueManager getLeagueManager() {
		return leagueManager;
	}

	public void loadSeasonState() {
		regularSeason = leagueManager.getLeague().getReagularSeason();
		currentDate = regularSeason.getDebutDate();
		updateDisplay();
	}

	private void updateDisplay() {
		currentGameDay = regularSeason.getCalendar().getCalendar().get(currentDate);
		updateGameDaySimulationState();
		currentDateLabel.setText("Date : " + currentDate);
		if (currentGameDay != null && currentGameDay.isSimulated()) {
			simulateDayButton.setText("Détail");
		} else {
			simulateDayButton.setText("Simuler");
		}
		updateMatchButtons();
		updateProgressBar();
		repaint();
	}

	private void showWaitingState() {
		matchDisplayPanel.removeAll();
		JLabel waitingLabel = new JLabel("Saison non initialisée.");
		waitingLabel.setFont(TEXT_FONT);
		waitingLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		matchDisplayPanel.add(waitingLabel);
		matchDisplayPanel.revalidate();
		matchDisplayPanel.repaint();
	}

	public void setSeasonProgressBarPanel(SeasonProgressBarPanel seasonProgressBarPanel) {
		this.seasonProgressBarPanel = seasonProgressBarPanel;
		updateProgressBar();
	}

	private void updateProgressBar() {
		if (seasonProgressBarPanel == null) {
			return;
		}
		int totalGameDays = regularSeason.getCalendar().getCalendar().size();
		int simulatedGameDays = 0;
		for (GameDay gameDay : regularSeason.getCalendar().getCalendar().values()) {
			if (gameDay.isSimulated()) {
				simulatedGameDays++;
			}
		}
		seasonProgressBarPanel.setProgress(simulatedGameDays, totalGameDays);
	}

	private void updateGameDaySimulationState() {
		if (currentGameDay == null || currentGameDay.isEmpty()) {
			return;
		}

		boolean allSimulated = true;
		for (Game game : currentGameDay.getGames()) {
			if (!isGameSimulated(game)) {
				allSimulated = false;
				break;
			}
		}

		currentGameDay.setSimulated(allSimulated);
	}

	private void updateMatchButtons() {
		matchDisplayPanel.removeAll();

		if (currentGameDay == null || currentGameDay.isEmpty()) {
			JLabel emptyLabel = new JLabel("Aucun match ce jour.");
			emptyLabel.setFont(TEXT_FONT);
			emptyLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
			matchDisplayPanel.add(emptyLabel);
			matchDisplayPanel.revalidate();
			matchDisplayPanel.repaint();
			return;
		}

		for (int index = 0; index < currentGameDay.getGames().size(); index++) {
			Game game = currentGameDay.getGames().get(index);
			matchDisplayPanel.add(buildMatchRow(game));
		}

		matchDisplayPanel.revalidate();
		matchDisplayPanel.repaint();
	}

	private void simulateCurrentDayIfNeeded() {
		if (currentGameDay == null || currentGameDay.isEmpty() || currentGameDay.isSimulated()) {
			return;
		}

		for (Game game : currentGameDay.getGames()) {
			if (!isGameSimulated(game)) {
				gameSimulator.simulateGame(game);
			}
		}

		currentGameDay.setSimulated(true);
	}

	private JPanel buildMatchRow(Game game) {
		JPanel rowPanel = new JPanel(new BorderLayout(12, 0));
		rowPanel.setOpaque(false);
		rowPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

		JLabel matchTitle = new JLabel(game.getGameContext().getAwayTeam().getName()
				+ " vs "
				+ game.getGameContext().getHomeTeam().getName());
		matchTitle.setFont(TITLE_FONT);

		String stateText = isGameSimulated(game) ? "Simulé" : "Match non simulé";
		JLabel matchState = new JLabel(stateText);
		matchState.setFont(TEXT_FONT);

		JPanel infoPanel = new JPanel();
		infoPanel.setOpaque(false);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(matchTitle);
		infoPanel.add(matchState);

		JButton simulateButton = new JButton("Simuler");
		simulateButton.setFont(TEXT_FONT);
		simulateButton.addActionListener(new SimulateMatchListener(game));

		JButton detailButton = new JButton("Détail");
		detailButton.setFont(TEXT_FONT);
		detailButton.addActionListener(new MatchDetailListener(game));

		JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actionsPanel.setOpaque(false);
		actionsPanel.add(simulateButton);
		actionsPanel.add(detailButton);

		rowPanel.add(infoPanel, BorderLayout.CENTER);
		rowPanel.add(actionsPanel, BorderLayout.EAST);
		return rowPanel;
	}

	private boolean isGameSimulated(Game game) {
		if (game.getQuarterResults() == null) {
			return false;
		}

		for (int index = 0; index < game.getQuarterResults().length; index++) {
			if (game.getQuarterResults()[index] != null) {
				return true;
			}
		}

		return false;
	}

	private void openMatchDashboard(Game game) {
		//! Point d'extension: brancher ici la navigation vers MatchDashboard.
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
			simulateCurrentDayIfNeeded();
			if (currentDate.isBefore(regularSeason.getEndDate())) {
				currentDate = currentDate.plusDays(1);
			}
			updateDisplay();
		}
	}

	private class SimulateCurrentDayListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentGameDay == null || currentGameDay.isEmpty()) {
				return;
			}

			if (currentGameDay.isSimulated()) {
				openMatchDashboard(null);
				return;
			}

			simulateCurrentDayIfNeeded();
			updateDisplay();
		}
	}

	private class SimulateMatchListener implements ActionListener {
		private final Game game;

		private SimulateMatchListener(Game game) {
			this.game = game;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (isGameSimulated(game)) {
				return;
			}

			gameSimulator.simulateGame(game);
			updateGameDaySimulationState();
			updateDisplay();
		}
	}

	private class MatchDetailListener implements ActionListener {
		private final Game game;

		private MatchDetailListener(Game game) {
			this.game = game;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isGameSimulated(game)) {
				return;
			}
			openMatchDashboard(game);
		}
	}
}

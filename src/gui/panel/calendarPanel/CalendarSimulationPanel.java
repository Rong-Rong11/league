package gui.panel.calendarPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.calendar.GameDay;
import data.league.RegularSeason;
import data.sport.setup.Game;
import process.manager.SimulationManager;

public class CalendarSimulationPanel extends JPanel {
	public interface MatchDaySelectionListener {
		void openMatchDay(GameDay gameDay, LocalDate date);
	}

	private static final long serialVersionUID = 1L;
	private static final Font DISPLAY_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
	private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	private static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
	private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("EEEE dd/MM");
	private static final DateTimeFormatter WEEK_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");

	private final JButton previousDayButton = new JButton("Semaine -");
	private final JButton nextDayButton = new JButton("Semaine +");
	private final JLabel currentDateLabel = new JLabel();
	private final JPanel matchDisplayPanel = new JPanel();

	private final SimulationManager simulationManager;
	private RegularSeason regularSeason;
	private LocalDate currentDate;
	private SeasonProgressBarPanel seasonProgressBarPanel;
	private MatchDaySelectionListener matchDaySelectionListener;

	public CalendarSimulationPanel(SimulationManager simulationManager) {
		setLayout(new BorderLayout());

		this.simulationManager = simulationManager;

		JPanel topBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		currentDateLabel.setFont(DISPLAY_FONT);
		currentDateLabel.setText("Date : -");

		previousDayButton.addActionListener(new PreviousDayListener());
		nextDayButton.addActionListener(new NextDayListener());

		topBarPanel.add(previousDayButton);
		topBarPanel.add(currentDateLabel);
		topBarPanel.add(nextDayButton);

		matchDisplayPanel.setLayout(new BoxLayout(matchDisplayPanel, BoxLayout.Y_AXIS));
		matchDisplayPanel.setOpaque(false);

		add(topBarPanel, BorderLayout.NORTH);
		add(matchDisplayPanel, BorderLayout.CENTER);

		showWaitingState();
	}

	public void loadSeasonState() {
		regularSeason = simulationManager.getLeague().getReagularSeason();
		currentDate = normalizeDisplayedDate(simulationManager.getCurrentDate());
		updateDisplay();
	}

	public void advanceDay() {
		if (regularSeason == null) {
			return;
		}
		if (simulationManager.getCurrentDate().isBefore(regularSeason.getEndDate())) {
			simulationManager.nextDay();
			currentDate = normalizeDisplayedDate(simulationManager.getCurrentDate());
			updateDisplay();
		}
	}

	public void advanceWeek() {
		for (int index = 0; index < 7; index++) {
			if (regularSeason == null || currentDate == null || currentDate.isEqual(regularSeason.getEndDate())) {
				break;
			}
			advanceDay();
		}
	}

	private void updateDisplay() {
		if (regularSeason == null || currentDate == null) {
			showWaitingState();
			return;
		}
		currentDate = normalizeDisplayedDate(currentDate);
		currentDateLabel.setText(buildWeekLabel());
		if (seasonProgressBarPanel != null) {
			seasonProgressBarPanel.setCurrentDate(simulationManager.getCurrentDate());
		}
		updateWeekRows();
		updateProgressBar();
		repaint();
	}

	private String buildWeekLabel() {
		LocalDate weekStart = getWeekStart(currentDate);
		LocalDate weekEnd = weekStart.plusDays(6);
		return "Semaine du " + WEEK_FORMATTER.format(weekStart) + " au " + WEEK_FORMATTER.format(weekEnd);
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
	}

	public void setMatchDaySelectionListener(MatchDaySelectionListener matchDaySelectionListener) {
		this.matchDaySelectionListener = matchDaySelectionListener;
	}

	private void updateProgressBar() {
		if (seasonProgressBarPanel == null || regularSeason == null || regularSeason.getCalendar() == null) {
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

	private void updateWeekRows() {
		matchDisplayPanel.removeAll();
		LocalDate weekStart = getWeekStart(currentDate);
		for (int offset = 0; offset < 7; offset++) {
			LocalDate day = weekStart.plusDays(offset);
			if (day.isBefore(regularSeason.getDebutDate()) || day.isAfter(regularSeason.getEndDate())) {
				continue;
			}
			GameDay gameDay = regularSeason.getCalendar().getCalendar().get(day);
			if (gameDay == null || gameDay.isEmpty()) {
				continue;
			}
			matchDisplayPanel.add(buildDayRow(day));
		}
		if (matchDisplayPanel.getComponentCount() == 0) {
			JLabel emptyLabel = new JLabel("Aucun match sur cette semaine.");
			emptyLabel.setFont(TEXT_FONT);
			emptyLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
			matchDisplayPanel.add(emptyLabel);
		}
		matchDisplayPanel.revalidate();
		matchDisplayPanel.repaint();
	}

	private JPanel buildDayRow(LocalDate day) {
		GameDay gameDay = regularSeason.getCalendar().getCalendar().get(day);
		boolean simulated = isGameDaySimulated(gameDay);

		JPanel rowPanel = new JPanel(new BorderLayout(12, 0));
		rowPanel.setOpaque(false);
		rowPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

		JLabel dayTitle = new JLabel(DAY_FORMATTER.format(day));
		dayTitle.setFont(TITLE_FONT);

		String detailText = gameDay.getGames().size() == 1 ? "1 match" : gameDay.getGames().size() + " matchs";
		JLabel dayDetail = new JLabel(detailText);
		dayDetail.setFont(TEXT_FONT);

		JPanel infoPanel = new JPanel();
		infoPanel.setOpaque(false);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(dayTitle);
		infoPanel.add(dayDetail);

		JButton simulateButton = new JButton("Simuler");
		simulateButton.setFont(TEXT_FONT);
		simulateButton.addActionListener(new SimulateDayListener(day, simulated));

		JButton detailButton = new JButton("Détail");
		detailButton.setFont(TEXT_FONT);
		detailButton.addActionListener(new DetailDayListener(gameDay, day));

		JLabel stateLabel = new JLabel(simulated ? "Simulé" : "À simuler");
		stateLabel.setFont(TEXT_FONT);

		JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actionsPanel.setOpaque(false);
		actionsPanel.add(stateLabel);
		actionsPanel.add(simulateButton);
		actionsPanel.add(detailButton);

		rowPanel.add(infoPanel, BorderLayout.CENTER);
		rowPanel.add(actionsPanel, BorderLayout.EAST);
		return rowPanel;
	}

	private void simulateUntil(LocalDate targetDate) {
		if (regularSeason == null || targetDate == null) {
			return;
		}
		LocalDate simulatedDate = simulationManager.getCurrentDate();
		while (simulatedDate.isBefore(targetDate) && simulatedDate.isBefore(regularSeason.getEndDate())) {
			simulationManager.nextDay();
			simulatedDate = simulationManager.getCurrentDate();
		}
	}

	private void simulateDisplayedWeek() {
		if (regularSeason == null || currentDate == null) {
			return;
		}
		LocalDate weekEnd = getWeekStart(currentDate).plusDays(6);
		if (weekEnd.isAfter(regularSeason.getEndDate())) {
			weekEnd = regularSeason.getEndDate();
		}
		simulateUntil(weekEnd);
	}

	private boolean isGameSimulated(Game game) {
		if (game.getQuarterResults() == null) {
			return false;
		}
		for (int index = 0; index < game.getQuarterResults().length; index++) {
			if (game.getQuarterResults()[index] == null) {
				return false;
			}
		}
		return true;
	}

	private boolean isGameDaySimulated(GameDay gameDay) {
		if (gameDay == null || gameDay.isEmpty()) {
			return false;
		}
		if (gameDay.isSimulated()) {
			return true;
		}
		for (Game game : gameDay.getGames()) {
			if (!isGameSimulated(game)) {
				return false;
			}
		}
		gameDay.setSimulated(true);
		return true;
	}

	private LocalDate getWeekStart(LocalDate date) {
		return date.minusDays(date.getDayOfWeek().getValue() - 1L);
	}

	private LocalDate normalizeDisplayedDate(LocalDate referenceDate) {
		if (regularSeason == null || referenceDate == null || regularSeason.getCalendar() == null) {
			return referenceDate;
		}

		LocalDate bestDate = findClosestPreviousDateWithMatch(referenceDate);
		if (bestDate != null) {
			return bestDate;
		}

		bestDate = findClosestNextDateWithMatch(referenceDate);
		if (bestDate != null) {
			return bestDate;
		}

		return referenceDate;
	}

	private LocalDate findClosestPreviousDateWithMatch(LocalDate referenceDate) {
		for (LocalDate date = referenceDate; !date.isBefore(regularSeason.getDebutDate()); date = date.minusDays(1)) {
			GameDay gameDay = regularSeason.getCalendar().getCalendar().get(date);
			if (gameDay != null && !gameDay.isEmpty()) {
				return date;
			}
		}
		return null;
	}

	private LocalDate findClosestNextDateWithMatch(LocalDate referenceDate) {
		for (LocalDate date = referenceDate; !date.isAfter(regularSeason.getEndDate()); date = date.plusDays(1)) {
			GameDay gameDay = regularSeason.getCalendar().getCalendar().get(date);
			if (gameDay != null && !gameDay.isEmpty()) {
				return date;
			}
		}
		return null;
	}

	private void openMatchDashboard(GameDay gameDay, LocalDate date) {
		if (matchDaySelectionListener != null) {
			matchDaySelectionListener.openMatchDay(gameDay, date);
		}
	}

	private class PreviousDayListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (regularSeason == null || currentDate == null) {
				return;
			}
			LocalDate previousWeek = currentDate.minusDays(7);
			if (!previousWeek.isBefore(regularSeason.getDebutDate())) {
				currentDate = previousWeek;
				updateDisplay();
			}
		}
	}

	private class NextDayListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (regularSeason == null || currentDate == null) {
				return;
			}
			LocalDate nextWeek = currentDate.plusDays(7);
			if (!nextWeek.isAfter(regularSeason.getEndDate())) {
				simulateDisplayedWeek();
				currentDate = nextWeek;
				updateDisplay();
			}
		}
	}

	private class SimulateDayListener implements ActionListener {
		private final LocalDate day;
		private final boolean simulated;

		private SimulateDayListener(LocalDate day, boolean simulated) {
			this.day = day;
			this.simulated = simulated;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (simulated) {
				return;
			}
			simulateUntil(day);
			currentDate = day;
			updateDisplay();
		}
	}

	private class DetailDayListener implements ActionListener {
		private final GameDay gameDay;
		private final LocalDate day;

		private DetailDayListener(GameDay gameDay, LocalDate day) {
			this.gameDay = gameDay;
			this.day = day;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			openMatchDashboard(gameDay, day);
		}
	}
}

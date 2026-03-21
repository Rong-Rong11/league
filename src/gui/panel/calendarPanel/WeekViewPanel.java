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
import process.manager.SimulationManager;

public class WeekViewPanel extends JPanel {
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
	private LocalDate simulationDate;
	private OpenMatchDayAction openMatchDayAction;

	public WeekViewPanel(SimulationManager simulationManager) {
		this.simulationManager = simulationManager;
		create();
		organize();
		actions();
		showWaitingState();
	}

	private void create() {
		currentDateLabel.setFont(DISPLAY_FONT);
		currentDateLabel.setText("Date : -");
		matchDisplayPanel.setLayout(new BoxLayout(matchDisplayPanel, BoxLayout.Y_AXIS));
		matchDisplayPanel.setOpaque(false);
	}

	private void organize() {
		setLayout(new BorderLayout());
		JPanel topBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topBarPanel.add(previousDayButton);
		topBarPanel.add(currentDateLabel);
		topBarPanel.add(nextDayButton);

		add(topBarPanel, BorderLayout.NORTH);
		add(matchDisplayPanel, BorderLayout.CENTER);
	}

	private void actions() {
		previousDayButton.addActionListener(new PreviousWeekListener());
		nextDayButton.addActionListener(new NextWeekListener());
	}

	public void loadSeasonState() {
		regularSeason = simulationManager.getLeague().getReagularSeason();
		simulationDate = simulationManager.getCurrentDate();
		currentDate = normalizeDisplayedDate(simulationDate);
		updateDisplay();
	}

	public void advanceDay() {
		if (regularSeason == null) {
			return;
		}
		simulationManager.displayGameDay(currentDate);
		if (simulationManager.getCurrentDate().isBefore(regularSeason.getEndDate())) {
			simulationManager.nextDay();
		}
		simulationDate = simulationManager.getCurrentDate();
		currentDate = normalizeDisplayedDate(simulationDate);
		updateDisplay();
	}

	public void advanceWeek() {
		if (regularSeason == null || currentDate == null) {
			return;
		}
		LocalDate weekStart = getWeekStart(currentDate);
		simulationManager.displayWeek(weekStart);
		LocalDate nextWeek = weekStart.plusDays(7);
		if (!nextWeek.isAfter(regularSeason.getEndDate())) {
			simulationDate = nextWeek;
		} else {
			simulationDate = regularSeason.getEndDate();
		}
		currentDate = normalizeDisplayedDate(simulationDate);
		updateDisplay();
	}

	private void updateDisplay() {
		if (regularSeason == null || currentDate == null) {
			showWaitingState();
			return;
		}
		currentDate = normalizeDisplayedDate(currentDate);
		currentDateLabel.setText(buildWeekLabel());
		updateWeekRows();
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

	public void setOpenMatchDayAction(OpenMatchDayAction openMatchDayAction) {
		this.openMatchDayAction = openMatchDayAction;
	}

	public LocalDate getCurrentDate() {
		return currentDate;
	}

	public LocalDate getSimulationDate() {
		return simulationDate;
	}

	public void setSimulationDate(LocalDate simulationDate) {
		this.simulationDate = simulationDate;
		currentDate = normalizeDisplayedDate(simulationDate);
		updateDisplay();
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
		boolean displayed = isGameDayDisplayed(gameDay);

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
		simulateButton.addActionListener(new SimulateDayListener(day, displayed));

		JButton detailButton = new JButton("Détail");
		detailButton.setFont(TEXT_FONT);
		detailButton.addActionListener(new DetailDayListener(gameDay, day));

		JLabel stateLabel = new JLabel(displayed ? "Simulé" : "À simuler");
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

	private boolean isGameDayDisplayed(GameDay gameDay) {
		return gameDay != null && gameDay.isDisplayed();
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
		if (openMatchDayAction != null) {
			openMatchDayAction.open(gameDay, date);
		}
	}

	private void showPreviousWeek() {
		if (regularSeason == null || currentDate == null) {
			return;
		}
		LocalDate previousWeek = currentDate.minusDays(7);
		if (!previousWeek.isBefore(regularSeason.getDebutDate())) {
			currentDate = previousWeek;
			updateDisplay();
		}
	}

	public static class OpenMatchDayAction {
		public void open(GameDay gameDay, LocalDate date) {
		}
	}

	private class PreviousWeekListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			showPreviousWeek();
		}
	}

	private class NextWeekListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			advanceWeek();
		}
	}

	private class SimulateDayListener implements ActionListener {
		private final LocalDate day;
		private final boolean displayed;

		private SimulateDayListener(LocalDate day, boolean displayed) {
			this.day = day;
			this.displayed = displayed;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (displayed) {
				return;
			}
			simulationManager.displayGameDay(day);
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

package gui.panel.calendarPanel;

import java.awt.BorderLayout;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Font;

import data.calendar.GameDay;
import process.orchestrator.GUIInterface;

public class WeekViewPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);

	private final GUIInterface guiInterface;
	private final JPanel matchDisplayPanel = new JPanel();

	private LocalDate displayedDate;
	private LocalDate lastSimulatedDate;
	private OpenMatchDayAction openMatchDayAction;
	private DisplayedDateChangeListener displayedDateChangeListener;

	public WeekViewPanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		create();
		organize();
		showWaitingState();
	}

	private void create() {
		matchDisplayPanel.setLayout(new BorderLayout());
		matchDisplayPanel.setOpaque(false);
	}

	private void organize() {
		setLayout(new BorderLayout());
		add(matchDisplayPanel, BorderLayout.CENTER);
	}

	public void loadSeasonState() {
		if (!guiInterface.isSeasonInitialized()) {
			clearDisplayedState();
			return;
		}
		syncToSimulationDate(guiInterface.getCurrentDate());
	}

	public void syncToSimulationDate(LocalDate simulationDate) {
		if (!guiInterface.isSeasonInitialized() || simulationDate == null) {
			clearDisplayedState();
			return;
		}
		lastSimulatedDate = simulationDate;
		displayedDate = guiInterface.getDisplayedDateAfterWeekSimulation(simulationDate);
		updateDisplay();
	}

	public void advanceDay() {
		if (!canDisplayWeek()) {
			return;
		}
		LocalDate day = displayedDate;
		simulateDisplayedDay(day);
		lastSimulatedDate = day;
		displayedDate = guiInterface.getDisplayedDateAfterDaySimulation(day);
		updateDisplay();
	}

	public void advanceWeek() {
		if (!canDisplayWeek()) {
			return;
		}
		LocalDate simulationBaseDate = guiInterface.getCurrentWeekIndicatorDate();
		if (simulationBaseDate == null) {
			simulationBaseDate = displayedDate;
		}
		guiInterface.simulateWeek(simulationBaseDate);
		lastSimulatedDate = guiInterface.getCurrentDate();
		displayedDate = guiInterface.getDisplayedDateAfterWeekSimulation(simulationBaseDate);
		updateDisplay();
	}

	public void advanceSeason() {
		if (!canDisplayWeek()) {
			return;
		}
		guiInterface.simulateSeasonFrom(displayedDate);
		lastSimulatedDate = guiInterface.getCurrentDate();
		displayedDate = guiInterface.getDisplayedDateAfterSeasonSimulation(displayedDate);
		updateDisplay();
	}

	public String getWeekText() {
		return guiInterface.getWeekText(displayedDate);
	}

	public void setOpenMatchDayAction(OpenMatchDayAction openMatchDayAction) {
		this.openMatchDayAction = openMatchDayAction;
	}

	public void setDisplayedDateChangeListener(DisplayedDateChangeListener displayedDateChangeListener) {
		this.displayedDateChangeListener = displayedDateChangeListener;
	}

	public LocalDate getCurrentDate() {
		return displayedDate;
	}

	public LocalDate getSimulationDate() {
		return lastSimulatedDate;
	}

	public void setSimulationDate(LocalDate date) {
		lastSimulatedDate = date;
		displayedDate = date;
		updateDisplay();
	}

	public void showPreviousWeek() {
		if (!canDisplayWeek()) {
			return;
		}
		displayedDate = guiInterface.getPreviousWeekDisplayDate(displayedDate);
		updateDisplay();
	}

	public void showNextWeek() {
		if (!canDisplayWeek()) {
			return;
		}
		displayedDate = guiInterface.getNextWeekDisplayDate(displayedDate);
		updateDisplay();
	}

	private boolean canDisplayWeek() {
		return guiInterface.isSeasonInitialized() && displayedDate != null;
	}

	private void clearDisplayedState() {
		displayedDate = null;
		lastSimulatedDate = null;
		updateDisplay();
	}

	private void updateDisplay() {
		matchDisplayPanel.removeAll();
		if (!canDisplayWeek()) {
			showWaitingState();
			return;
		}

		LocalDate weekStart = guiInterface.getWeekStartDate(displayedDate);
		LocalDate indicatorDay = guiInterface.getCurrentWeekIndicatorDate();
		WeekScheduleGridPanel gridPanel = new WeekScheduleGridPanel(guiInterface, weekStart, indicatorDay,
				new OpenDayAction(), new DaySimulationAction());
		matchDisplayPanel.add(gridPanel, BorderLayout.CENTER);
		matchDisplayPanel.revalidate();
		matchDisplayPanel.repaint();
	}

	private void showWaitingState() {
		JLabel waitingLabel = new JLabel("Saison non initialisée.");
		waitingLabel.setFont(TEXT_FONT);
		waitingLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		matchDisplayPanel.add(waitingLabel, BorderLayout.CENTER);
		matchDisplayPanel.revalidate();
		matchDisplayPanel.repaint();
	}

	private void simulateDisplayedDay(LocalDate day) {
		guiInterface.simulateAndDisplayDay(day);
	}

	private void openMatchDashboard(GameDay gameDay, LocalDate date) {
		if (openMatchDayAction != null) {
			openMatchDayAction.open(gameDay, date);
		}
	}

	private void notifyDisplayedDateChanged() {
		if (displayedDateChangeListener != null && displayedDate != null) {
			displayedDateChangeListener.onDisplayedDateChanged(displayedDate);
		}
	}

	public static class OpenMatchDayAction {
		public void open(GameDay gameDay, LocalDate date) {
		}
	}

	public interface DisplayedDateChangeListener {
		void onDisplayedDateChanged(LocalDate date);
	}

	private class DaySimulationAction implements WeekScheduleGridPanel.DaySimulationAction {
		@Override
		public void simulate(LocalDate day) {
			simulateDisplayedDay(day);
			lastSimulatedDate = day;
			displayedDate = day;
			updateDisplay();
			notifyDisplayedDateChanged();
		}
	}

	private class OpenDayAction implements WeekScheduleGridPanel.OpenDayAction {
		@Override
		public void open(GameDay gameDay, LocalDate day) {
			openMatchDashboard(gameDay, day);
		}
	}
}

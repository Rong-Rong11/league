package gui.dashboard;
import config.CalendarConfiguration;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import data.calendar.GameDay;
import gui.panel.calendarPanel.HeaderPanel;
import gui.panel.calendarPanel.MonthViewPanel;
import gui.panel.calendarPanel.WeekViewPanel;
import process.manager.SimulationManager;

public class CalendarDashboard extends JPanel {

	private static final int DASHBOARD_SPACING = 16;
	private static final String MONTH_VIEW = "MONTH_VIEW";
	private static final String WEEK_VIEW = "WEEK_VIEW";
	private static final Color BACKGROUND_COLOR = new Color(247, 248, 250);
	private final SimulationManager simulationManager;
	private HeaderPanel headerPanel;
	private WeekViewPanel calendarSimulationPanel;
	private MonthViewPanel monthViewPanel;
	private JPanel contentPanel;
	private CardLayout contentLayout;
	private YearMonth displayedMonth;
	private boolean monthViewSelected;

	public CalendarDashboard(SimulationManager simulationManager, MatchDashboard matchDashboard, Runnable showMatchDashboardAction) {
		this.simulationManager = simulationManager;
		create(matchDashboard, showMatchDashboardAction);
		organize();
		actions();
		updateDashboardState();
	}

	private void create(MatchDashboard matchDashboard, Runnable showMatchDashboardAction) {
		headerPanel = new HeaderPanel();
		calendarSimulationPanel = new WeekViewPanel(simulationManager);
		calendarSimulationPanel.setOpenMatchDayAction(new OpenMatchDayAction(matchDashboard, showMatchDashboardAction));
		monthViewPanel = new MonthViewPanel();
		contentLayout = new CardLayout();
		contentPanel = new JPanel(contentLayout);
		contentPanel.setOpaque(false);
		contentPanel.add(monthViewPanel, MONTH_VIEW);
		contentPanel.add(calendarSimulationPanel, WEEK_VIEW);
		displayedMonth = YearMonth.from(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE);
		monthViewSelected = true;
	}

	private void organize() {
		setLayout(new BorderLayout());
		setBackground(BACKGROUND_COLOR);

		JPanel content = buildContentPanel();
		content.add(headerPanel, BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);
		add(content, BorderLayout.CENTER);
	}

	private JPanel buildContentPanel() {
		JPanel content = new JPanel(new BorderLayout(DASHBOARD_SPACING, DASHBOARD_SPACING));
		content.setOpaque(false);
		content.setBorder(BorderFactory.createEmptyBorder(0, DASHBOARD_SPACING, DASHBOARD_SPACING, DASHBOARD_SPACING));
		return content;
	}

	public void startSeason() {
		simulationManager.randomFinance();
		simulationManager.startSeason();
		calendarSimulationPanel.loadSeasonState();
		updateDashboardState();
	}

	public void refreshSeasonState() {
		calendarSimulationPanel.loadSeasonState();
		updateDashboardState();
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(0, 0));
		body.setOpaque(false);
		body.add(contentPanel, BorderLayout.CENTER);
		return body;
	}

	private void updateDashboardState() {
		LocalDate currentDate = simulationManager.getCurrentDate();
		headerPanel.setMonthText(MonthViewPanel.buildMonthText(displayedMonth));
		headerPanel.setMonthViewSelected(monthViewSelected);
		updateProgress();
		updateMonthView(currentDate);
		updateCurrentCard();
	}

	private void updateProgress() {
		if (simulationManager.getLeague() == null || simulationManager.getLeague().getReagularSeason() == null
				|| simulationManager.getLeague().getReagularSeason().getCalendar() == null) {
			headerPanel.setProgress(0, 0);
			return;
		}

		int totalGameDays = simulationManager.getLeague().getReagularSeason().getCalendar().getCalendar().size();
		int displayedGameDays = 0;
		for (data.calendar.GameDay gameDay : simulationManager.getLeague().getReagularSeason().getCalendar().getCalendar().values()) {
			if (gameDay.isDisplayed()) {
				displayedGameDays++;
			}
		}
		headerPanel.setProgress(displayedGameDays, totalGameDays);
	}

	private void updateMonthView(LocalDate currentDate) {
		if (simulationManager.getLeague() == null || simulationManager.getLeague().getReagularSeason() == null
				|| simulationManager.getLeague().getReagularSeason().getCalendar() == null) {
			monthViewPanel.showMonth(displayedMonth, currentDate, null);
			return;
		}

		monthViewPanel.showMonth(
				displayedMonth,
				currentDate,
				simulationManager.getLeague().getReagularSeason().getCalendar().getCalendar());
	}

	private void updateCurrentCard() {
		if (monthViewSelected) {
			contentLayout.show(contentPanel, MONTH_VIEW);
		} else {
			contentLayout.show(contentPanel, WEEK_VIEW);
		}
	}

	private void actions() {
		headerPanel.setSimulateDayAction(new SimulateDayAction());
		headerPanel.setSimulateWeekAction(new SimulateWeekAction());
		headerPanel.setSimulateSeasonAction(new SimulateSeasonAction());
		headerPanel.setPreviousMonthAction(new PreviousMonthAction());
		headerPanel.setNextMonthAction(new NextMonthAction());
		headerPanel.setMonthToggleAction(new ShowMonthViewAction());
		headerPanel.setWeekToggleAction(new ShowWeekViewAction());
	}

	private class OpenMatchDayAction extends WeekViewPanel.OpenMatchDayAction {
		private MatchDashboard matchDashboard;
		private Runnable showMatchDashboardAction;

		private OpenMatchDayAction(MatchDashboard matchDashboard, Runnable showMatchDashboardAction) {
			this.matchDashboard = matchDashboard;
			this.showMatchDashboardAction = showMatchDashboardAction;
		}

		@Override
		public void open(GameDay gameDay, LocalDate date) {
			matchDashboard.showGameDay(gameDay, date);
			showMatchDashboardAction.run();
		}
	}

	private class SimulateDayAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			calendarSimulationPanel.advanceDay();
			updateDashboardState();
		}
	}

	private class SimulateWeekAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			calendarSimulationPanel.advanceWeek();
			updateDashboardState();
		}
	}

	private class SimulateSeasonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			simulationManager.displayCurrentSeason();
			calendarSimulationPanel.loadSeasonState();
			updateDashboardState();
		}
	}

	private class PreviousMonthAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			displayedMonth = displayedMonth.minusMonths(1);
			updateDashboardState();
		}
	}

	private class NextMonthAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			displayedMonth = displayedMonth.plusMonths(1);
			updateDashboardState();
		}
	}

	private class ShowMonthViewAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			monthViewSelected = true;
			updateCurrentCard();
		}
	}

	private class ShowWeekViewAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			monthViewSelected = false;
			updateCurrentCard();
		}
	}
}

package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import gui.panel.calendarPanel.HeaderPanel;
import gui.panel.calendarPanel.MonthViewPanel;
import gui.panel.calendarPanel.WeekViewPanel;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;
import process.orchestrator.interf.GUIInterface;

public class CalendarDashboard extends JPanel implements ThemeAware, RefreshableDashboard {

	private static final int DASHBOARD_SPACING = 16;
	private static final String MONTH_VIEW = "MONTH_VIEW";
	private static final String WEEK_VIEW = "WEEK_VIEW";
	private static final Color BACKGROUND_COLOR = DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR;
	private GUIInterface guiInterface;
	private HeaderPanel headerPanel;
	private WeekViewPanel weekViewPanel;
	private MonthViewPanel monthViewPanel;
	private JPanel contentPanel;
	private CardLayout contentLayout;
	private YearMonth displayedMonth;
	private boolean monthViewSelected;
	private LocalDate currentCalendarDate;
	private MatchDashboard matchDashboard;
	private Runnable showMatchDashboardAction;
	private Runnable regularSeasonEndAction;
	private RosterDashboard rosterDashboard;
	private MapDashboard mapDashboard;

	public CalendarDashboard(GUIInterface guiInterface, MatchDashboard matchDashboard, Runnable showMatchDashboardAction,
			RosterDashboard rosterDashboard, MapDashboard mapDashboard) {
		this.guiInterface = guiInterface;
		this.matchDashboard = matchDashboard;
		this.showMatchDashboardAction = showMatchDashboardAction;
		this.rosterDashboard = rosterDashboard;
		this.mapDashboard = mapDashboard;
		create();
		organize();
		actions();
		updateDashboardState();
	}

	private void create() {
		headerPanel = new HeaderPanel();
		weekViewPanel = new WeekViewPanel(guiInterface);
		weekViewPanel.setDisplayedDateChangeListener(new WeekDisplayedDateChangeListener());
		OpenMatchDayAction openMatchDayAction = new OpenMatchDayAction(matchDashboard, showMatchDashboardAction);
		weekViewPanel.setOpenMatchDayAction(openMatchDayAction);
		monthViewPanel = new MonthViewPanel();
		contentLayout = new CardLayout();
		contentPanel = new JPanel(contentLayout);
		contentPanel.setOpaque(false);
		contentPanel.add(monthViewPanel, MONTH_VIEW);
		contentPanel.add(weekViewPanel, WEEK_VIEW);
		currentCalendarDate = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE;
		displayedMonth = buildDisplayedMonth(currentCalendarDate);
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
		guiInterface.startSeason();
		currentCalendarDate = guiInterface.getCalendarDisplayDate(guiInterface.getCurrentDate());
		weekViewPanel.syncToSimulationDate(guiInterface.getCurrentDate());
		updateDisplayedMonth(currentCalendarDate);
		updateDashboardState();
	}

	public void refreshSeasonState() {
		if (!guiInterface.isSeasonInitialized()) {
			weekViewPanel.loadSeasonState();
			updateDashboardState();
			return;
		}

		LocalDate simulationDate = guiInterface.getCurrentDate();
		currentCalendarDate = guiInterface.getCalendarDisplayDate(simulationDate);
		weekViewPanel.syncToSimulationDate(simulationDate);
		updateDisplayedMonth(currentCalendarDate);
		updateDashboardState();
	}

	@Override
	public void refresh() {
		refreshSeasonState();
	}

	public void showUninitializedSeasonState() {
		weekViewPanel.loadSeasonState();
		updateDashboardState();
	}

	public void applySeasonSynchronization(LocalDate simulationDate) {
		currentCalendarDate = guiInterface.getCalendarDisplayDate(simulationDate);
		weekViewPanel.syncToSimulationDate(simulationDate);
		updateDisplayedMonth(currentCalendarDate);
		updateDashboardState();
	}

	public void setRegularSeasonEndAction(Runnable regularSeasonEndAction) {
		this.regularSeasonEndAction = regularSeasonEndAction;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(0, 0));
		body.setOpaque(false);
		body.add(contentPanel, BorderLayout.CENTER);
		return body;
	}

	private void updateDashboardState() {
		LocalDate currentDate = currentCalendarDate;
		checkDisplayedMonth();
		updateHeaderState();
		updateCalendarPanels(currentDate);
		updateCurrentCard();
	}

	private void updateHeaderState() {
		headerPanel.setMonthText(MonthViewPanel.buildMonthText(displayedMonth));
		headerPanel.setWeekText(weekViewPanel.getWeekText());
		headerPanel.setMonthViewSelected(monthViewSelected);
		updateProgress();
	}

	private void updateCalendarPanels(LocalDate currentDate) {
		updateMonthView(currentDate);
	}

	private void updateProgress() {
		if (!guiInterface.isSeasonInitialized()) {
			headerPanel.setProgress(0, 0);
			return;
		}

		TreeMap<LocalDate, GameDay> seasonCalendar = guiInterface.getSeasonCalendar();
		int totalGameDays = seasonCalendar.size();
		int displayedGameDays = 0;
		for (GameDay gameDay : seasonCalendar.values()) {
			if (gameDay.isDisplayed()) {
				displayedGameDays++;
			}
		}
		headerPanel.setProgress(displayedGameDays, totalGameDays);
	}

	private void updateMonthView(LocalDate currentDate) {
		if (!guiInterface.isSeasonInitialized()) {
			monthViewPanel.showMonth(displayedMonth, currentDate, null);
			return;
		}

		HashMap<LocalDate, GameDay> seasonCalendar = new HashMap<LocalDate, GameDay>(guiInterface.getSeasonCalendar());
		monthViewPanel.showMonth(displayedMonth, currentDate, seasonCalendar);
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
		headerPanel.setPreviousWeekAction(new PreviousWeekAction());
		headerPanel.setNextWeekAction(new NextWeekAction());
		headerPanel.setMonthToggleAction(new ShowMonthViewAction());
		headerPanel.setWeekToggleAction(new ShowWeekViewAction());
		monthViewPanel.setMatchDashboard(matchDashboard, showMatchDashboardAction);
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

	private class WeekDisplayedDateChangeListener implements WeekViewPanel.DisplayedDateChangeListener {
		@Override
		public void onDisplayedDateChanged(LocalDate date) {
			currentCalendarDate = date;
			updateDisplayedMonth(currentCalendarDate);
			updateDashboardState();
		}
	}

	private class SimulateDayAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			weekViewPanel.advanceDay();
			rosterDashboard.refreshSelectedTeam();
			currentCalendarDate = weekViewPanel.getCurrentDate();
			updateDisplayedMonth(currentCalendarDate);
			updateDashboardState();
			mapDashboard.refreshSelectedTeam();
			notifyRegularSeasonEndIfNecessary();
		}
	}

	private class SimulateWeekAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			weekViewPanel.advanceWeek();
			rosterDashboard.refreshSelectedTeam();
			if (weekViewPanel.getCurrentDate() != null) {
				currentCalendarDate = weekViewPanel.getCurrentDate();
			}
			updateDisplayedMonth(currentCalendarDate);
			updateDashboardState();
			mapDashboard.refreshSelectedTeam();
			notifyRegularSeasonEndIfNecessary();
		}
	}

	private class SimulateSeasonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			weekViewPanel.advanceSeason();
			rosterDashboard.refreshSelectedTeam();
			if (weekViewPanel.getCurrentDate() != null) {
				currentCalendarDate = weekViewPanel.getCurrentDate();
			} else {
				currentCalendarDate = weekViewPanel.getSimulationDate();
			}
			updateDisplayedMonth(currentCalendarDate);
			updateDashboardState();
			mapDashboard.refreshSelectedTeam();
			notifyRegularSeasonEndIfNecessary();
		}
	}

	private void notifyRegularSeasonEndIfNecessary() {
		if (regularSeasonEndAction != null
				&& guiInterface.isRegularSeasonFinished()
				&& !guiInterface.hasUserConfirmedPlayoffs()) {
			regularSeasonEndAction.run();
		}
	}

	private class PreviousMonthAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isSeasonStartMonth(displayedMonth)) {
				displayedMonth = displayedMonth.minusMonths(1);
			}
			updateDashboardState();
		}
	}

	private class NextMonthAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isSeasonEndMonth(displayedMonth)) {
				displayedMonth = displayedMonth.plusMonths(1);
			}
			updateDashboardState();
		}
	}

	private class PreviousWeekAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			weekViewPanel.showPreviousWeek();
			if (weekViewPanel.getCurrentDate() != null) {
				currentCalendarDate = weekViewPanel.getCurrentDate();
			}
			updateDisplayedMonth(currentCalendarDate);
			updateDashboardState();
		}
	}

	private class NextWeekAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			weekViewPanel.showNextWeek();
			if (weekViewPanel.getCurrentDate() != null) {
				currentCalendarDate = weekViewPanel.getCurrentDate();
			}
			updateDisplayedMonth(currentCalendarDate);
			updateDashboardState();
		}
	}

	private class ShowMonthViewAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			monthViewSelected = true;
			updateDashboardState();
		}
	}

	private class ShowWeekViewAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			monthViewSelected = false;
			updateDashboardState();
		}
	}

	private void checkDisplayedMonth() {
		if (isBeforeSeasonStartMonth(displayedMonth)) {
			displayedMonth = buildDisplayedMonth(getSeasonStartDate());
		}
		if (isAfterSeasonEndMonth(displayedMonth)) {
			displayedMonth = buildDisplayedMonth(getSeasonEndDate());
		}
	}

	private void updateDisplayedMonth(LocalDate date) {
		displayedMonth = buildDisplayedMonth(date);
	}

	private YearMonth buildDisplayedMonth(LocalDate date) {
		YearMonth month = YearMonth.now();
		month = month.withYear(date.getYear());
		month = month.withMonth(date.getMonthValue());
		return month;
	}

	private boolean isSeasonStartMonth(YearMonth month) {
		LocalDate seasonStartDate = getSeasonStartDate();
		return month.getYear() == seasonStartDate.getYear()
				&& month.getMonthValue() == seasonStartDate.getMonthValue();
	}

	private boolean isSeasonEndMonth(YearMonth month) {
		LocalDate seasonEndDate = getSeasonEndDate();
		return month.getYear() == seasonEndDate.getYear()
				&& month.getMonthValue() == seasonEndDate.getMonthValue();
	}

	private boolean isBeforeSeasonStartMonth(YearMonth month) {
		LocalDate seasonStartDate = getSeasonStartDate();
		if (month.getYear() < seasonStartDate.getYear()) {
			return true;
		}
		if (month.getYear() > seasonStartDate.getYear()) {
			return false;
		}
		return month.getMonthValue() < seasonStartDate.getMonthValue();
	}

	private boolean isAfterSeasonEndMonth(YearMonth month) {
		LocalDate seasonEndDate = getSeasonEndDate();
		if (month.getYear() > seasonEndDate.getYear()) {
			return true;
		}
		if (month.getYear() < seasonEndDate.getYear()) {
			return false;
		}
		return month.getMonthValue() > seasonEndDate.getMonthValue();
	}

	private LocalDate getSeasonStartDate() {
		return CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE;
	}

	private LocalDate getSeasonEndDate() {
		if (!guiInterface.isSeasonInitialized()) {
			return CalendarConfiguration.REGULAR_SEASON_END_DATE;
		}
		TreeMap<LocalDate, GameDay> seasonCalendar = guiInterface.getSeasonCalendar();
		if (seasonCalendar.isEmpty()) {
			return CalendarConfiguration.REGULAR_SEASON_END_DATE;
		}
		return seasonCalendar.lastKey();
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		updateDashboardState();
		DashboardPanelUtil.refreshChildrenTheme(this);
	}
}

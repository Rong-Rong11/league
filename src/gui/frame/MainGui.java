package gui.frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.dashboard.CalendarDashboard;
import gui.dashboard.FinanceDashboard;
import gui.dashboard.WelcomeDashboard;
import gui.dashboard.LiveMatchDashboard;
import gui.dashboard.LoadingDashboard;
import gui.dashboard.MapDashboard;
import gui.dashboard.MatchDashboard;
import gui.dashboard.OpeningDashboard;
import gui.dashboard.RankingDashboard;
import gui.dashboard.RefreshableDashboard;
import gui.dashboard.RegularSeasonEndDashboard;
import gui.dashboard.RosterDashboard;
import gui.dashboard.SeasonEndDashboard;
import gui.layout.SidebarPanel;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;
import process.orchestrator.interfaces.GUIInterface;

public class MainGui extends JFrame {
	private static final String ROOT_WELCOME = "welcome";
	private static final String ROOT_LOADING = "loading";
	private static final String ROOT_OPENING = "opening";
	private static final String ROOT_MAIN = "main";
	private static final String ROOT_REGULAR_SEASON_END = "regularSeasonEnd";
	private static final String ROOT_SEASON_END = "seasonEnd";
	private static final String DASHBOARD_MATCH = "match";
	private static final String DASHBOARD_LIVE_MATCH = "liveMatch";
	private static final String DASHBOARD_CALENDAR = "calendar";
	private static final String DASHBOARD_RANKING = "ranking";
	private static final String DASHBOARD_FINANCE = "finance";
	private static final String DASHBOARD_MAP = "map";
	private static final String DASHBOARD_ROSTER = "roster";

	private CardLayout rootLayout;
	private JPanel rootPanel;
	private CardLayout dashboardLayout;
	private JPanel dashboardPanel;
	private WelcomeDashboard welcomeDashboard;
	private LoadingDashboard loadingDashboard;
	private OpeningDashboard openingPanel;
	private RegularSeasonEndDashboard regularSeasonEndDashboard;
	private SeasonEndDashboard seasonEndDashboard;
	private JPanel mainPanel;
	private CalendarDashboard calendarDashboard;
	private MatchDashboard matchDashboard;
	private LiveMatchDashboard liveMatchDashboard;
	private RankingDashboard rankingDashboard;
	private MapDashboard mapDashboard;
	private RosterDashboard rosterDashboard;
	private FinanceDashboard financeDashboard;
	private GUIInterface guiInterface;
	private SidebarPanel sidebar;
	private Map<String, RefreshableDashboard> refreshableDashboards;
	private String currentRootCard;
	private String currentDashboardCard;

	public MainGui(GUIInterface guiInterface) {
		this(guiInterface, true);
	}

	public MainGui(GUIInterface guiInterface, boolean visible) {
		this.guiInterface = guiInterface;
		create();
		organize(visible);
		actions();
	}

	private void create() {
		setTitle("NBA League");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		rootLayout = new CardLayout();
		rootPanel = new JPanel(rootLayout);

		dashboardLayout = new CardLayout();
		dashboardPanel = new JPanel(dashboardLayout);
		refreshableDashboards = new HashMap<String, RefreshableDashboard>();
		welcomeDashboard = new WelcomeDashboard();
		loadingDashboard = new LoadingDashboard();
		openingPanel = new OpeningDashboard(guiInterface);
		regularSeasonEndDashboard = new RegularSeasonEndDashboard(guiInterface);
		seasonEndDashboard = new SeasonEndDashboard(guiInterface);
		mainPanel = buildApplicationPanel();
	}

	private void organize(boolean visible) {
		rootPanel.add(welcomeDashboard, ROOT_WELCOME);
		rootPanel.add(loadingDashboard, ROOT_LOADING);
		rootPanel.add(openingPanel, ROOT_OPENING);
		rootPanel.add(mainPanel, ROOT_MAIN);
		rootPanel.add(regularSeasonEndDashboard, ROOT_REGULAR_SEASON_END);
		rootPanel.add(seasonEndDashboard, ROOT_SEASON_END);

		setLayout(new BorderLayout());
		add(rootPanel, BorderLayout.CENTER);

		showDashboardCard(DASHBOARD_MATCH);
		showRootCard(ROOT_WELCOME);

		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setVisible(visible);
	}

	private void actions() {
		registerOpeningActions();
	}

	private JPanel buildApplicationPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		sidebar = new SidebarPanel();

		buildDashboards();
		registerDashboardCards();
		registerRefreshableDashboards();
		registerDashboardLinks();
		registerSidebarActions();

		mainPanel.add(sidebar, BorderLayout.WEST);
		mainPanel.add(dashboardPanel, BorderLayout.CENTER);

		return mainPanel;
	}

	private void buildDashboards() {
		matchDashboard = new MatchDashboard(guiInterface);
		liveMatchDashboard = new LiveMatchDashboard(guiInterface);
		mapDashboard = new MapDashboard(guiInterface);
		rosterDashboard = new RosterDashboard(guiInterface);
		calendarDashboard = new CalendarDashboard(guiInterface, matchDashboard, new ShowMatchDashboardAction(),
				rosterDashboard, mapDashboard);
		rankingDashboard = new RankingDashboard(guiInterface);
		financeDashboard = new FinanceDashboard(guiInterface);
	}

	private void registerDashboardCards() {
		dashboardPanel.add(matchDashboard, DASHBOARD_MATCH);
		dashboardPanel.add(liveMatchDashboard, DASHBOARD_LIVE_MATCH);
		dashboardPanel.add(calendarDashboard, DASHBOARD_CALENDAR);
		dashboardPanel.add(rankingDashboard, DASHBOARD_RANKING);
		dashboardPanel.add(financeDashboard, DASHBOARD_FINANCE);
		dashboardPanel.add(mapDashboard, DASHBOARD_MAP);
		dashboardPanel.add(rosterDashboard, DASHBOARD_ROSTER);
	}

	private void registerDashboardLinks() {
		matchDashboard.setOpenLiveMatchAction(new ShowLiveMatchDashboardAction());
		liveMatchDashboard.setBackToMatchAction(new ShowMatchDashboardAction());
		mapDashboard.setOpenRosterAction(new ShowRosterDashboardAction());
		rosterDashboard.setBackToMapAction(new ShowMapDashboardAction());
		calendarDashboard.setRegularSeasonEndAction(new ShowRegularSeasonEndAction());
		rankingDashboard.setSeasonEndAction(new ShowSeasonEndAction());
	}

	private void registerOpeningActions() {
		welcomeDashboard.getContinueButton().addActionListener(new ShowOpeningDashboardAction());
		welcomeDashboard.getThemeButton().addActionListener(new ToggleThemeAction());
		openingPanel.getContinueButton().addActionListener(new OpenApplicationAction(openingPanel));
		openingPanel.getThemeButton().addActionListener(new ToggleThemeAction());
		regularSeasonEndDashboard.getReviewRankingButton().addActionListener(new ReviewRankingAction());
		regularSeasonEndDashboard.getStartPlayoffsButton().addActionListener(new StartPlayoffsAction());
		seasonEndDashboard.getReviewRankingButton().addActionListener(new ReviewRankingAction());
		seasonEndDashboard.getOpenFinanceButton().addActionListener(new OpenFinanceAction());
	}

	private void registerSidebarActions() {
		sidebar.getMatchButton().addActionListener(new SwitchDashboardAction(DASHBOARD_MATCH));
		sidebar.getCalendarButton().addActionListener(new SwitchDashboardAction(DASHBOARD_CALENDAR));
		sidebar.getRankingButton().addActionListener(new SwitchDashboardAction(DASHBOARD_RANKING));
		sidebar.getFinanceButton().addActionListener(new SwitchDashboardAction(DASHBOARD_FINANCE));
		sidebar.getMapButton().addActionListener(new SwitchDashboardAction(DASHBOARD_MAP));
		sidebar.getFinalResultsButton().addActionListener(new ShowSeasonEndSidebarAction());
		sidebar.getThemeButton().addActionListener(new ToggleThemeAction());
		sidebar.getExitButton().addActionListener(new QuitAction());
	}

	private void registerRefreshableDashboards() {
		refreshableDashboards.put(DASHBOARD_MATCH, matchDashboard);
		refreshableDashboards.put(DASHBOARD_CALENDAR, calendarDashboard);
		refreshableDashboards.put(DASHBOARD_RANKING, rankingDashboard);
		refreshableDashboards.put(DASHBOARD_FINANCE, financeDashboard);
		refreshableDashboards.put(DASHBOARD_MAP, mapDashboard);
		refreshableDashboards.put(DASHBOARD_ROSTER, rosterDashboard);
	}

	private void refreshDashboard(String cardName) {
		RefreshableDashboard dashboard = refreshableDashboards.get(cardName);
		if (dashboard != null) {
			dashboard.refresh();
		}
	}

	private void showRootCard(String cardName) {
		currentRootCard = cardName;
		rootLayout.show(rootPanel, cardName);
	}

	private void showDashboardCard(String cardName) {
		currentDashboardCard = cardName;
		dashboardLayout.show(dashboardPanel, cardName);
	}

	private class ShowOpeningDashboardAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			showRootCard(ROOT_OPENING);
		}
	}

	private class SwitchDashboardAction implements ActionListener {
		private String cardName;

		public SwitchDashboardAction(String cardName) {
			this.cardName = cardName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (DASHBOARD_CALENDAR.equals(cardName) && guiInterface.isRegularSeasonFinished()) {
				if (!guiInterface.hasUserConfirmedPlayoffs()) {
					showRegularSeasonEndDashboard();
					return;
				}
			}
			openMainDashboard(cardName, cardName, true);
		}
	}

	private void showRegularSeasonEndDashboard() {
		regularSeasonEndDashboard.refresh();
		showRootCard(ROOT_REGULAR_SEASON_END);
	}

	private void showSeasonEndDashboard() {
		seasonEndDashboard.refresh();
		syncSidebarSeasonEndVisibility();
		sidebar.setActiveSection(ROOT_SEASON_END);
		showRootCard(ROOT_SEASON_END);
	}

	private void syncSidebarSeasonEndVisibility() {
		if (sidebar != null) {
			sidebar.setFinalResultsVisible(guiInterface.arePlayoffsFinished());
		}
	}

	private class ShowRegularSeasonEndAction implements Runnable {
		@Override
		public void run() {
			showRegularSeasonEndDashboard();
		}
	}

	private class ShowSeasonEndAction implements Runnable {
		@Override
		public void run() {
			showSeasonEndDashboard();
		}
	}

	private class ShowSeasonEndSidebarAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			showSeasonEndDashboard();
		}
	}

	private class ReviewRankingAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			openMainDashboard(DASHBOARD_RANKING, DASHBOARD_RANKING, true);
		}
	}

	private class OpenFinanceAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			openMainDashboard(DASHBOARD_FINANCE, DASHBOARD_FINANCE, true);
		}
	}

	private class StartPlayoffsAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			guiInterface.initializePlayoffs();
			guiInterface.setUserConfirmedPlayoffs(true);
			syncSidebarSeasonEndVisibility();
			rankingDashboard.showPlayoffs();
			openMainDashboard(DASHBOARD_RANKING, DASHBOARD_RANKING, false);
		}
	}

	private class OpenApplicationAction implements ActionListener {
		private OpeningDashboard openingPanel;

		public OpenApplicationAction(OpeningDashboard openingPanel) {
			this.openingPanel = openingPanel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!openingPanel.hasSelectedProfil()) {
				openingPanel.showSelectionWarning();
				return;
			}
			startApplicationWithLoading();
		}
	}

	private void startApplicationWithLoading() {
		showRootCard(ROOT_LOADING);
		loadingDashboard.startLoadingSequence(new ApplicationLoadingHandler());
	}

	private class ApplicationLoadingHandler implements LoadingDashboard.LoadingSequenceHandler {
		@Override
		public void initializeSeason() {
			calendarDashboard.startSeason();
			syncSidebarSeasonEndVisibility();
		}

		@Override
		public void loadMatches() {
			matchDashboard.loadGamesOfDay(guiInterface.getMatchDisplayDate());
		}

		@Override
		public void finishLoading() {
			openMainApplication();
		}
	}

	private void openMainApplication() {
		openMainDashboard(DASHBOARD_MATCH, DASHBOARD_MATCH, false);
	}

	private class QuitAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainGui frame = MainGui.this;
			String question = "Voulez-vous vraiment quitter la simulation ?";
			int choice = JOptionPane.showConfirmDialog(frame, question, "Confirmation", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			if (choice == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}

	private class ShowMatchDashboardAction implements Runnable {
		@Override
		public void run() {
			calendarDashboard.refreshSeasonState();
			openMainDashboard(DASHBOARD_MATCH, DASHBOARD_MATCH, false);
		}
	}

	private class ShowLiveMatchDashboardAction implements Runnable {
		@Override
		public void run() {
			liveMatchDashboard.setGame(matchDashboard.getSelectedGame());
			showDashboardCard(DASHBOARD_LIVE_MATCH);
		}
	}

	private class ShowRosterDashboardAction implements Runnable {
		@Override
		public void run() {
			rosterDashboard.setSelectedTeam(mapDashboard.getSelectedTeam());
			openMainDashboard(DASHBOARD_MAP, DASHBOARD_ROSTER, false);
		}
	}

	private class ShowMapDashboardAction implements Runnable {
		@Override
		public void run() {
			openMainDashboard(DASHBOARD_MAP, DASHBOARD_MAP, false);
		}
	}

	private void openMainDashboard(String sidebarSection, String dashboardCard, boolean refresh) {
		syncSidebarSeasonEndVisibility();
		if (refresh) {
			refreshDashboard(dashboardCard);
		}
		sidebar.setActiveSection(sidebarSection);
		showDashboardCard(dashboardCard);
		showRootCard(ROOT_MAIN);
	}

	private class ToggleThemeAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			DashboardPanelUtil.toggleDarkMode();
			applyCurrentTheme();
			rootPanel.revalidate();
			rootPanel.repaint();
		}
	}

	private void applyCurrentTheme() {
		rootPanel.setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		mainPanel.setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		dashboardPanel.setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		applyTheme(openingPanel);
		applyTheme(sidebar);
		syncSidebarSeasonEndVisibility();
		applyTheme(matchDashboard);
		applyTheme(liveMatchDashboard);
		applyTheme(calendarDashboard);
		refreshThemeSensitiveDashboards();
		applyTheme(mapDashboard);
		applyTheme(rosterDashboard);
		applyTheme(rankingDashboard);
		applyTheme(financeDashboard);
		applyTheme(welcomeDashboard);
		applyTheme(loadingDashboard);
	}

	private void applyTheme(ThemeAware component) {
		if (component != null) {
			component.applyTheme();
		}
	}

	private void refreshThemeSensitiveDashboards() {
		if (regularSeasonEndDashboard != null) {
			regularSeasonEndDashboard.refresh();
		}
		if (seasonEndDashboard != null) {
			seasonEndDashboard.refresh();
		}
	}

	public OpeningDashboard getOpeningPanel() {
		return openingPanel;
	}

	public SidebarPanel getSidebar() {
		return sidebar;
	}

	public String getCurrentRootCard() {
		return currentRootCard;
	}

	public String getCurrentDashboardCard() {
		return currentDashboardCard;
	}
}

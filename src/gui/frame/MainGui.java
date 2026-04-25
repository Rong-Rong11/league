package gui.frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.dashboard.CalendarDashboard;
import gui.dashboard.FinanceDashboard;
import gui.dashboard.LiveMatchDashboard;
import gui.dashboard.MapDashboard;
import gui.dashboard.MatchDashboard;
import gui.dashboard.OpeningDashboard;
import gui.dashboard.PlayoffsDashboard;
import gui.dashboard.RefreshableDashboard;
import gui.dashboard.RankingDashboard;
import gui.dashboard.RegularSeasonEndDashboard;
import gui.dashboard.RosterDashboard;
import gui.layout.SidebarPanel;
import gui.panel.common.DashboardPanelUtil;
import process.orchestrator.interf.GUIInterface;

import java.util.HashMap;
import java.util.Map;

public class MainGui extends JFrame {

	private CardLayout rootLayout;
	private JPanel rootPanel;
	private CardLayout dashboardLayout;
	private JPanel dashboardPanel;
	private OpeningDashboard openingPanel;
	private RegularSeasonEndDashboard regularSeasonEndDashboard;
	private JPanel mainPanel;
	private CalendarDashboard calendarDashboard;
	private PlayoffsDashboard playoffsDashboard;
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
		openingPanel = new OpeningDashboard(guiInterface);
		regularSeasonEndDashboard = new RegularSeasonEndDashboard(guiInterface);
		mainPanel = buildApplicationPanel();
	}

	private void organize(boolean visible) {
		rootPanel.add(openingPanel, "opening");
		rootPanel.add(mainPanel, "main");
		rootPanel.add(regularSeasonEndDashboard, "regularSeasonEnd");

		setLayout(new BorderLayout());
		add(rootPanel, BorderLayout.CENTER);

		showDashboardCard("match");
		showRootCard("opening");

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
		playoffsDashboard = new PlayoffsDashboard(guiInterface);
		rankingDashboard = new RankingDashboard(guiInterface);
		financeDashboard = new FinanceDashboard(guiInterface);
	}

	private void registerDashboardCards() {
		dashboardPanel.add(matchDashboard, "match");
		dashboardPanel.add(liveMatchDashboard, "liveMatch");
		dashboardPanel.add(calendarDashboard, "calendar");
		dashboardPanel.add(playoffsDashboard, "playoffs");
		dashboardPanel.add(rankingDashboard, "ranking");
		dashboardPanel.add(financeDashboard, "finance");
		dashboardPanel.add(mapDashboard, "map");
		dashboardPanel.add(rosterDashboard, "roster");
	}

	private void registerDashboardLinks() {
		matchDashboard.setOpenLiveMatchAction(new ShowLiveMatchDashboardAction());
		liveMatchDashboard.setBackToMatchAction(new ShowMatchDashboardAction());
		mapDashboard.setOpenRosterAction(new ShowRosterDashboardAction());
		rosterDashboard.setBackToMapAction(new ShowMapDashboardAction());
		calendarDashboard.setRegularSeasonEndAction(new ShowRegularSeasonEndAction());
	}

	private void registerOpeningActions() {
		openingPanel.getContinueButton().addActionListener(new OpenApplicationAction(openingPanel));
		openingPanel.getThemeButton().addActionListener(new ToggleThemeAction());
		regularSeasonEndDashboard.getReviewRankingButton().addActionListener(new ReviewRankingAction());
		regularSeasonEndDashboard.getStartPlayoffsButton().addActionListener(new StartPlayoffsAction());
	}

	private void registerSidebarActions() {
		sidebar.getMatchButton().addActionListener(new SwitchDashboardAction("match"));
		sidebar.getCalendarButton().addActionListener(new SwitchDashboardAction("calendar"));
		sidebar.getRankingButton().addActionListener(new SwitchDashboardAction("ranking"));
		sidebar.getFinanceButton().addActionListener(new SwitchDashboardAction("finance"));
		sidebar.getMapButton().addActionListener(new SwitchDashboardAction("map"));
		sidebar.getThemeButton().addActionListener(new ToggleThemeAction());
		sidebar.getExitButton().addActionListener(new QuitAction());
	}

	private void registerRefreshableDashboards() {
		refreshableDashboards.put("match", matchDashboard);
		refreshableDashboards.put("calendar", calendarDashboard);
		refreshableDashboards.put("playoffs", playoffsDashboard);
		refreshableDashboards.put("ranking", rankingDashboard);
		refreshableDashboards.put("finance", financeDashboard);
		refreshableDashboards.put("map", mapDashboard);
		refreshableDashboards.put("roster", rosterDashboard);
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

	private class SwitchDashboardAction implements ActionListener {
		private String cardName;

		public SwitchDashboardAction(String cardName) {
			this.cardName = cardName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if ("calendar".equals(cardName) && guiInterface.isRegularSeasonFinished()) {
				if (!guiInterface.hasUserConfirmedPlayoffs()) {
					showRegularSeasonEndDashboard();
					return;
				}
				refreshDashboard("playoffs");
				sidebar.setActiveSection("calendar");
				showDashboardCard("playoffs");
				return;
			}
			refreshDashboard(cardName);
			sidebar.setActiveSection(cardName);
			showDashboardCard(cardName);
		}
	}

	private void showRegularSeasonEndDashboard() {
		regularSeasonEndDashboard.refresh();
		showRootCard("regularSeasonEnd");
	}

	private class ShowRegularSeasonEndAction implements Runnable {
		@Override
		public void run() {
			showRegularSeasonEndDashboard();
		}
	}

	private class ReviewRankingAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			rankingDashboard.refresh();
			sidebar.setActiveSection("ranking");
			showDashboardCard("ranking");
			showRootCard("main");
		}
	}

	private class StartPlayoffsAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			guiInterface.initializePlayoffs();
			guiInterface.setUserConfirmedPlayoffs(true);
			playoffsDashboard.refresh();
			sidebar.setActiveSection("calendar");
			showDashboardCard("playoffs");
			showRootCard("main");
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

			calendarDashboard.startSeason();
			matchDashboard.loadGamesOfDay(guiInterface.getMatchDisplayDate());
			sidebar.setActiveSection("match");
			showDashboardCard("match");
			showRootCard("main");
		}
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
			sidebar.setActiveSection("match");
			showDashboardCard("match");
		}
	}

	private class ShowLiveMatchDashboardAction implements Runnable {
		@Override
		public void run() {
			liveMatchDashboard.setGame(matchDashboard.getSelectedGame());
			showDashboardCard("liveMatch");
		}
	}

	private class ShowRosterDashboardAction implements Runnable {
		@Override
		public void run() {
			rosterDashboard.setSelectedTeam(mapDashboard.getSelectedTeam());
			sidebar.setActiveSection("map");
			showDashboardCard("roster");
		}
	}

	private class ShowMapDashboardAction implements Runnable {
		@Override
		public void run() {
			sidebar.setActiveSection("map");
			showDashboardCard("map");
		}
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
		openingPanel.applyTheme();
		if (sidebar != null) {
			sidebar.applyTheme();
		}
		if (matchDashboard != null) {
			matchDashboard.applyTheme();
		}
		if (liveMatchDashboard != null) {
			liveMatchDashboard.applyTheme();
		}
		if (calendarDashboard != null) {
			calendarDashboard.applyTheme();
		}
		if (playoffsDashboard != null) {
			playoffsDashboard.refresh();
		}
		if (regularSeasonEndDashboard != null) {
			regularSeasonEndDashboard.refresh();
		}
		if (mapDashboard != null) {
			mapDashboard.applyTheme();
		}
		if (rosterDashboard != null) {
			rosterDashboard.applyTheme();
		}
		if (rankingDashboard != null) {
			rankingDashboard.applyTheme();
		}
		if (financeDashboard != null) {
			financeDashboard.applyTheme();
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

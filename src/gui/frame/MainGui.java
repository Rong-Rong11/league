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
import gui.dashboard.RankingDashboard;
import gui.dashboard.RosterDashboard;
import gui.layout.SidebarPanel;
import gui.panel.common.DashboardPanelUtil;
import process.orchestrator.interf.GUIInterface;

public class MainGui extends JFrame {

	private CardLayout rootLayout;
	private JPanel rootPanel;
	private CardLayout dashboardLayout;
	private JPanel dashboardPanel;
	private OpeningDashboard openingPanel;
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

	public MainGui(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		create();
		organize();
		actions();
	}

	private void create() {
		setTitle("NBA League");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		rootLayout = new CardLayout();
		rootPanel = new JPanel(rootLayout);

		dashboardLayout = new CardLayout();
		dashboardPanel = new JPanel(dashboardLayout);
		openingPanel = new OpeningDashboard(guiInterface);
		mainPanel = buildApplicationPanel();
	}

	private void organize() {
		rootPanel.add(openingPanel, "opening");
		rootPanel.add(mainPanel, "main");

		setLayout(new BorderLayout());
		add(rootPanel, BorderLayout.CENTER);

		dashboardLayout.show(dashboardPanel, "match");
		rootLayout.show(rootPanel, "opening");

		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void actions() {
		openingPanel.getContinueButton().addActionListener(new OpenApplicationAction(openingPanel));
		openingPanel.getThemeButton().addActionListener(new ToggleThemeAction());
	}

	private JPanel buildApplicationPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		sidebar = new SidebarPanel();

		matchDashboard = new MatchDashboard(guiInterface);
		liveMatchDashboard = new LiveMatchDashboard(guiInterface);
		mapDashboard = new MapDashboard(guiInterface);
		rosterDashboard = new RosterDashboard(guiInterface);
		dashboardPanel.add(matchDashboard, "match");
		dashboardPanel.add(liveMatchDashboard, "liveMatch");
		calendarDashboard = new CalendarDashboard(guiInterface, matchDashboard, new ShowMatchDashboardAction(),
				rosterDashboard, mapDashboard);
		dashboardPanel.add(calendarDashboard, "calendar");
		rankingDashboard = new RankingDashboard(guiInterface);
		financeDashboard = new FinanceDashboard(guiInterface);
		dashboardPanel.add(rankingDashboard, "ranking");
		dashboardPanel.add(financeDashboard, "finance");
		dashboardPanel.add(mapDashboard, "map");
		dashboardPanel.add(rosterDashboard, "roster");

		matchDashboard.setOpenLiveMatchAction(new ShowLiveMatchDashboardAction());
		liveMatchDashboard.setBackToMatchAction(new ShowMatchDashboardAction());
		mapDashboard.setOpenRosterAction(new ShowRosterDashboardAction());
		rosterDashboard.setBackToMapAction(new ShowMapDashboardAction());

		sidebar.getMatchButton().addActionListener(new SwitchDashboardAction("match"));
		sidebar.getCalendarButton().addActionListener(new SwitchDashboardAction("calendar"));
		sidebar.getRankingButton().addActionListener(new SwitchDashboardAction("ranking"));
		sidebar.getFinanceButton().addActionListener(new SwitchDashboardAction("finance"));
		sidebar.getMapButton().addActionListener(new SwitchDashboardAction("map"));
		sidebar.getThemeButton().addActionListener(new ToggleThemeAction());
		sidebar.getExitButton().addActionListener(new QuitAction());

		mainPanel.add(sidebar, BorderLayout.WEST);
		mainPanel.add(dashboardPanel, BorderLayout.CENTER);

		return mainPanel;
	}

	private class SwitchDashboardAction implements ActionListener {
		private String cardName;

		public SwitchDashboardAction(String cardName) {
			this.cardName = cardName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if ("calendar".equals(cardName)) {
				calendarDashboard.refreshSeasonState();
			}
			if ("match".equals(cardName)) {
				matchDashboard.refreshSelectedGame();
			}
			if ("ranking".equals(cardName)) {
				rankingDashboard.refreshRanking();
			}
			if ("finance".equals(cardName)) {
				financeDashboard.refreshData();
			}
			sidebar.setActiveSection(cardName);
			dashboardLayout.show(dashboardPanel, cardName);
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
			dashboardLayout.show(dashboardPanel, "match");
			rootLayout.show(rootPanel, "main");
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
			dashboardLayout.show(dashboardPanel, "match");
		}
	}

	private class ShowLiveMatchDashboardAction implements Runnable {
		@Override
		public void run() {
			liveMatchDashboard.setGame(matchDashboard.getSelectedGame());
			dashboardLayout.show(dashboardPanel, "liveMatch");
		}
	}

	private class ShowRosterDashboardAction implements Runnable {
		@Override
		public void run() {
			rosterDashboard.setSelectedTeam(mapDashboard.getSelectedTeam());
			sidebar.setActiveSection("map");
			dashboardLayout.show(dashboardPanel, "roster");
		}
	}

	private class ShowMapDashboardAction implements Runnable {
		@Override
		public void run() {
			sidebar.setActiveSection("map");
			dashboardLayout.show(dashboardPanel, "map");
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
}

package gui.frame;

//! a relire 

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.dashboard.CalendarDashboard;
import gui.dashboard.FinanceDashboard;
import gui.dashboard.MapDashboard;
import gui.dashboard.MatchDashboard;
import gui.dashboard.OpeningDashboard;
import gui.dashboard.RankingDashboard;
import gui.layout.SidebarPanel;

public class MainGui extends JFrame {

	private CardLayout rootLayout;
	private JPanel rootPanel;
	private CardLayout dashboardLayout;
	private JPanel dashboardPanel;
	private CalendarDashboard calendarDashboard;

	public MainGui() {
		setTitle("NBA League");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		rootLayout = new CardLayout();
		rootPanel = new JPanel(rootLayout);

		dashboardLayout = new CardLayout();
		dashboardPanel = new JPanel(dashboardLayout);

		OpeningDashboard openingPanel = new OpeningDashboard();
		JPanel mainPanel = buildApplicationPanel();

		rootPanel.add(openingPanel, "opening");
		rootPanel.add(mainPanel, "main");

		openingPanel.getContinueButton().addActionListener(new OpenApplicationAction(openingPanel));

		setLayout(new BorderLayout());
		add(rootPanel, BorderLayout.CENTER);

		dashboardLayout.show(dashboardPanel, "match");
		rootLayout.show(rootPanel, "opening");

		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JPanel buildApplicationPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		SidebarPanel sidebar = new SidebarPanel();

		dashboardPanel.add(new MatchDashboard(), "match");
		calendarDashboard = new CalendarDashboard();
		dashboardPanel.add(calendarDashboard, "calendar");
		dashboardPanel.add(new RankingDashboard(), "ranking");
		dashboardPanel.add(new FinanceDashboard(), "finance");
		dashboardPanel.add(new MapDashboard(), "map");

		sidebar.getMatchButton().addActionListener(new SwitchDashboardAction("match"));
		sidebar.getCalendarButton().addActionListener(new SwitchDashboardAction("calendar"));
		sidebar.getRankingButton().addActionListener(new SwitchDashboardAction("ranking"));
		sidebar.getFinanceButton().addActionListener(new SwitchDashboardAction("finance"));
		sidebar.getMapButton().addActionListener(new SwitchDashboardAction("map"));
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
			dashboardLayout.show(dashboardPanel, "match");
			rootLayout.show(rootPanel, "main");
		}
	}

	private class QuitAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}

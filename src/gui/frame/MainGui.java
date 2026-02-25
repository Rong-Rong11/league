package gui.frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.layout.SidebarPanel;
import gui.dashboard.CalendarDashboard;
import gui.dashboard.MatchDashboard;
import gui.dashboard.FinanceDashboard;
import gui.dashboard.RankingDashboard;
import gui.dashboard.MapDashboard;

public class MainGui extends JFrame {

	private CardLayout cardLayout;
	private JPanel centerPanel;

	public MainGui() {

		setTitle("NBA League");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		cardLayout = new CardLayout();
		centerPanel = new JPanel(cardLayout);

		MatchDashboard matchPanel = new MatchDashboard();
		CalendarDashboard calendarPanel = new CalendarDashboard();
		RankingDashboard rankingPanel = new RankingDashboard();
		FinanceDashboard financePanel = new FinanceDashboard();
		MapDashboard mapPanel = new MapDashboard();

		centerPanel.add(matchPanel, "match");
		centerPanel.add(calendarPanel, "calendar");
		centerPanel.add(rankingPanel, "ranking");
		centerPanel.add(financePanel, "finance");
		centerPanel.add(mapPanel, "map");

		SidebarPanel sidebar = new SidebarPanel();

		sidebar.getMatchButton().addActionListener(new SwitchAction("match"));
		sidebar.getCalendarButton().addActionListener(new SwitchAction("calendar"));
		sidebar.getRankingButton().addActionListener(new SwitchAction("ranking"));
		sidebar.getFinanceButton().addActionListener(new SwitchAction("finance"));
		sidebar.getMapButton().addActionListener(new SwitchAction("map"));
		sidebar.getExitButton().addActionListener(new QuitAction());

		setLayout(new BorderLayout());
		add(sidebar, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);

		cardLayout.show(centerPanel, "match");

		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private class SwitchAction implements ActionListener {
		private String cardName;
		public SwitchAction(String cardName) {
			this.cardName = cardName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(centerPanel, cardName);
		}
	}

	private class QuitAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}
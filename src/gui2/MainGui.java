package gui2;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import process.LeagueManager;

public class MainGui extends JFrame {
	private LeagueManager manager;

	private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(900, 500);
	private static final Dimension IDEAL_DASHBOARD_DIMENSION = new Dimension(700, 500);
	private static Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);

	private CardLayout cardLayout = new CardLayout();
	private JPanel centerPanel = new JPanel(cardLayout);

	private MatchDashboard matchDashboard = new MatchDashboard();
	private CalendarDashboard calendarDashboard = new CalendarDashboard();
	private RankingDashboard rankingDashboard = new RankingDashboard();
	private FinanceDashboard financeDashboard = new FinanceDashboard();
	private MapDashboard mapDashboard = new MapDashboard();

	private JButton matchButton = new JButton("Match");
	private JButton calendarButton = new JButton("Calendrier");
	private JButton rankingButton = new JButton("Classement");
	private JButton financeButton = new JButton("Finance");
	private JButton mapButton = new JButton("Carte");
	private JButton exitButton = new JButton("Quitter");

	private JPanel leftMenu = new JPanel();

	public MainGui() {
		super("NBA Simulation");
		init();
	}

	private void init() {

		manager = new LeagueManager();
		manager.buildLeague();
		manager.buildRegularSeasonCalendar();

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		Color menuText = new Color(226, 226, 226); // blanc cassé
		Color menuBg = new Color(15, 40, 90);

		leftMenu.setLayout(new GridLayout(6, 1, 0, 15));
		leftMenu.setPreferredSize(new Dimension(220, 0));
		leftMenu.setBackground(new Color(10, 36, 99)); // bleu foncé

		contentPane.add(BorderLayout.WEST, leftMenu);

		matchDashboard.setPreferredSize(IDEAL_DASHBOARD_DIMENSION);
		calendarDashboard.setPreferredSize(IDEAL_DASHBOARD_DIMENSION);
		rankingDashboard.setPreferredSize(IDEAL_DASHBOARD_DIMENSION);
		financeDashboard.setPreferredSize(IDEAL_DASHBOARD_DIMENSION);
		mapDashboard.setPreferredSize(IDEAL_DASHBOARD_DIMENSION);

		centerPanel.add(matchDashboard, "MATCH");
		centerPanel.add(calendarDashboard, "CALENDRIER");
		centerPanel.add(rankingDashboard, "CLASSEMENT");
		centerPanel.add(financeDashboard, "FINANCE");
		centerPanel.add(mapDashboard, "CARTE");
		contentPane.add(BorderLayout.CENTER, centerPanel);

		matchButton.setFont(font);
		matchButton.setForeground(menuText);
		matchButton.setBackground(menuBg);
		matchButton.setFocusPainted(false);
		matchButton.setBorderPainted(false);
		matchButton.setHorizontalAlignment(SwingConstants.LEFT);
		matchButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		matchButton.addActionListener(new SwitchAction("MATCH"));
		leftMenu.add(matchButton);

		calendarButton.setFont(font);
		calendarButton.setForeground(menuText);
		calendarButton.setBackground(menuBg);
		calendarButton.setFocusPainted(false);
		calendarButton.setBorderPainted(false);
		calendarButton.setHorizontalAlignment(SwingConstants.LEFT);
		calendarButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		calendarButton.addActionListener(new SwitchAction("CALENDRIER"));
		leftMenu.add(calendarButton);

		rankingButton.setFont(font);
		rankingButton.setForeground(menuText);
		rankingButton.setBackground(menuBg);
		rankingButton.setFocusPainted(false);
		rankingButton.setBorderPainted(false);
		rankingButton.setHorizontalAlignment(SwingConstants.LEFT);
		rankingButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		rankingButton.addActionListener(new SwitchAction("CLASSEMENT"));
		leftMenu.add(rankingButton);

		financeButton.setFont(font);
		financeButton.setForeground(menuText);
		financeButton.setBackground(menuBg);
		financeButton.setFocusPainted(false);
		financeButton.setBorderPainted(false);
		financeButton.setHorizontalAlignment(SwingConstants.LEFT);
		financeButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		financeButton.addActionListener(new SwitchAction("FINANCE"));
		leftMenu.add(financeButton);

		mapButton.setFont(font);
		mapButton.setForeground(menuText);
		mapButton.setBackground(menuBg);
		mapButton.setFocusPainted(false);
		mapButton.setBorderPainted(false);
		mapButton.setHorizontalAlignment(SwingConstants.LEFT);
		mapButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		mapButton.addActionListener(new SwitchAction("CARTE"));
		leftMenu.add(mapButton);

		exitButton.setFont(font);
		exitButton.setForeground(menuText);
		exitButton.setBackground(menuBg);
		exitButton.setFocusPainted(false);
		exitButton.setBorderPainted(false);
		exitButton.setHorizontalAlignment(SwingConstants.LEFT);
		exitButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		exitButton.addActionListener(new QuitAction());
		leftMenu.add(exitButton);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setPreferredSize(IDEAL_MAIN_DIMENSION);
		setResizable(false);
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

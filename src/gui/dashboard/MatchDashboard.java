package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import config.SimulationConfiguration;
import data.calendar.GameDay;
import data.finance.GameStat;
import data.finance.TeamGameFinance;
import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.sport.setup.GameResult;
import gui.panel.common.BuildBox;
import gui.panel.common.SectionTitle;
import process.manager.LeagueManager;

public class MatchDashboard extends JPanel {
	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
	private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 270;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 300;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);
	private static final DateTimeFormatter HEADER_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE dd/MM");

	private LeagueManager leagueManager;
	private LocalDate selectedDate;
	private Game selectedGame;
	private GameDay selectedGameDay;

	private JPanel gamesColumn;
	private JLabel headerSubtitleLabel;

	private JLabel teamsLabel;
	private JLabel mainScoreLabel;
	private JLabel quarterLabel;
	private JLabel statsLabel;

	private JLabel attendanceLabel;
	private JLabel homeRevenueLabel;
	private JLabel homeExpenseLabel;
	private JLabel awayRevenueLabel;
	private JLabel awayExpenseLabel;
	private JLabel netLabel;

	private Runnable openLiveMatchAction;

	public MatchDashboard() {
		this(null);
	}

	public MatchDashboard(LeagueManager leagueManager) {
		if (leagueManager == null) {
			this.leagueManager = new LeagueManager();
		} else {
			this.leagueManager = leagueManager;
		}
		selectedDate = SimulationConfiguration.REGULAR_SEASON_DEBUT_DATE;

		setLayout(new BorderLayout());
		setBackground(IDEAL_DASHBOARD_BACKGROUND_COLOR);

		JPanel content = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		content.setOpaque(false);

		JPanel leftSpace = new JPanel();
		leftSpace.setPreferredSize(new Dimension(IDEAL_DASHBOARD_SPACING, 0));
		leftSpace.setOpaque(false);

		JPanel rightSpace = new JPanel();
		rightSpace.setPreferredSize(new Dimension(IDEAL_DASHBOARD_SPACING, 0));
		rightSpace.setOpaque(false);

		JPanel bottomSpace = new JPanel();
		bottomSpace.setPreferredSize(new Dimension(0, IDEAL_DASHBOARD_SPACING));
		bottomSpace.setOpaque(false);

		add(leftSpace, BorderLayout.WEST);
		add(rightSpace, BorderLayout.EAST);
		add(bottomSpace, BorderLayout.SOUTH);
		add(content, BorderLayout.CENTER);

		content.add(buildHeader(), BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);

		loadGamesOfDay(selectedDate);
	}

	public LeagueManager getLeagueManager() {
		return leagueManager;
	}

	public LocalDate getSelectedDate() {
		return selectedDate;
	}

	public Game getSelectedGame() {
		return selectedGame;
	}

	public void setOpenLiveMatchAction(Runnable openLiveMatchAction) {
		this.openLiveMatchAction = openLiveMatchAction;
	}

	private JPanel buildHeader() {
		JPanel header = new JPanel();
		header.setOpaque(false);
		header.setLayout(new javax.swing.BoxLayout(header, javax.swing.BoxLayout.Y_AXIS));
		JLabel headerTitleLabel = new JLabel("SAISON RÉGULIÈRE");
		headerTitleLabel.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.BOLD, 15));
		headerTitleLabel.setForeground(new Color(0x17, 0x31, 0x74));
		headerTitleLabel.setAlignmentX(LEFT_ALIGNMENT);
		headerSubtitleLabel = new JLabel(buildHeaderSubtitle());
		headerSubtitleLabel.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 13));
		headerSubtitleLabel.setForeground(new Color(0x6D, 0x75, 0x83));
		headerSubtitleLabel.setAlignmentX(LEFT_ALIGNMENT);
		header.add(headerTitleLabel);
		header.add(javax.swing.Box.createVerticalStrut(3));
		header.add(headerSubtitleLabel);
		header.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, IDEAL_DASHBOARD_HEADER_HEIGHT));
		return header;
	}

	private String buildHeaderSubtitle() {
		if (selectedDate == null) {
			return "Jour : -";
		}
		return "Jour : " + HEADER_DATE_FORMATTER.format(selectedDate);
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, 0));
		body.setOpaque(false);

		JPanel leftCard = new BuildBox("MATCHS DU JOUR", "Rencontres de la journée", buildGameListPanel());
		leftCard.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, 10));

		JPanel centerCard = new BuildBox("MATCH SÉLECTIONNÉ", "Score et statistiques", buildMatchDetailsPanel());

		JPanel rightCard = new BuildBox("FINANCES DU MATCH", "Revenus et dépenses", buildFinancePanel());
		rightCard.setPreferredSize(new Dimension(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, 10));

		body.add(leftCard, BorderLayout.WEST);
		body.add(centerCard, BorderLayout.CENTER);
		body.add(rightCard, BorderLayout.EAST);

		return body;
	}

	private JPanel buildGameListPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		gamesColumn = new JPanel(new GridLayout(SimulationConfiguration.MAX_GAMES_PER_DAY, 1, 0, 8));
		gamesColumn.setOpaque(false);
		panel.add(gamesColumn, BorderLayout.CENTER);
		return panel;
	}

	private JPanel buildMatchDetailsPanel() {
		JPanel panel = new JPanel(new GridLayout(4, 1, 0, 12));
		teamsLabel = new JLabel("HOME vs AWAY", JLabel.CENTER);
		mainScoreLabel = new JLabel("0 - 0", JLabel.CENTER);
		quarterLabel = new JLabel("Q1 0-0 | Q2 0-0 | Q3 0-0 | Q4 0-0", JLabel.CENTER);
		statsLabel = new JLabel("2PTS 0-0 | 3PTS 0-0 | REB 0-0", JLabel.CENTER);

		panel.add(teamsLabel);
		panel.add(mainScoreLabel);
		panel.add(quarterLabel);
		panel.add(statsLabel);
		return panel;
	}

	private JPanel buildFinancePanel() {
		JPanel panel = new JPanel(new GridLayout(6, 1, 0, 10));
		attendanceLabel = new JLabel("Spectateurs : -");
		homeRevenueLabel = new JLabel("Domicile revenus : -");
		homeExpenseLabel = new JLabel("Domicile dépenses : -");
		awayRevenueLabel = new JLabel("Extérieur revenus : -");
		awayExpenseLabel = new JLabel("Extérieur dépenses : -");
		netLabel = new JLabel("Résultat net : -");

		panel.add(attendanceLabel);
		panel.add(homeRevenueLabel);
		panel.add(homeExpenseLabel);
		panel.add(awayRevenueLabel);
		panel.add(awayExpenseLabel);
		panel.add(netLabel);
		return panel;
	}

	public void loadGamesOfDay(LocalDate date) {
		selectedDate = date;
		GameDay gameDay = leagueManager.getLeague().getReagularSeason().getCalendar().getCalendar().get(date);
		showGameDay(gameDay, date);
	}

	public void showGameDay(GameDay gameDay, LocalDate date) {
		selectedDate = date;
		selectedGameDay = gameDay;
		if (headerSubtitleLabel != null) {
			headerSubtitleLabel.setText(buildHeaderSubtitle());
		}
		gamesColumn.removeAll();

		if (gameDay == null || gameDay.getGames().isEmpty()) {
			fillWithEmptyRows("Aucun match aujourd'hui");
			resetSelectedGame();
			return;
		}

		for (Game game : gameDay.getGames()) {
			gamesColumn.add(buildGameRow(game));
		}
		for (int i = gameDay.getGames().size(); i < SimulationConfiguration.MAX_GAMES_PER_DAY; i++) {
			gamesColumn.add(new JPanel());
		}

		updateSelectedGame(gameDay.getGames().get(0));
		revalidate();
		repaint();
	}

	private JPanel buildGameRow(Game game) {
		JPanel row = new JPanel(new BorderLayout(8, 0));
		row.setOpaque(false);

		JButton selectButton = new JButton(buildGameLabel(game));
		selectButton.setHorizontalAlignment(JButton.LEFT);
		selectButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				updateSelectedGame(game);
			}
		});

		JButton detailButton = new JButton("Détail");
		detailButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				updateSelectedGame(game);
				if (openLiveMatchAction != null) {
					openLiveMatchAction.run();
				}
			}
		});

		row.add(selectButton, BorderLayout.CENTER);
		row.add(detailButton, BorderLayout.EAST);
		return row;
	}

	private void fillWithEmptyRows(String message) {
		gamesColumn.add(new JLabel(message));
		for (int i = 1; i < SimulationConfiguration.MAX_GAMES_PER_DAY; i++) {
			gamesColumn.add(new JPanel());
		}
		revalidate();
		repaint();
	}

	private String buildGameLabel(Game game) {
		GameContext gameContext = game.getGameContext();
		String home = gameContext.getHomeTeam().getName();
		String away = gameContext.getAwayTeam().getName();
		return buildAbbreviation(home) + " vs " + buildAbbreviation(away);
	}

	private String buildAbbreviation(String teamName) {
		String[] words = teamName.split(" ");
		String abbreviation = "";
		for (int i = 0; i < words.length && abbreviation.length() < 3; i++) {
			if (!words[i].isEmpty()) {
				abbreviation += words[i].substring(0, 1).toUpperCase();
			}
		}
		while (abbreviation.length() < 3) {
			abbreviation += "X";
		}
		return abbreviation;
	}

	private void updateSelectedGame(Game game) {
		selectedGame = game;
		GameContext gameContext = game.getGameContext();

		teamsLabel.setText(gameContext.getHomeTeam().getName() + " vs " + gameContext.getAwayTeam().getName());
		if (selectedGameDay == null || !selectedGameDay.isDisplayed()) {
			showHiddenResultState();
			return;
		}
		mainScoreLabel.setText(game.getHomeFinalScore() + " - " + game.getAwayFinalScore());

		GameResult[] quarterResults = game.getQuarterResults();
		quarterLabel.setText(buildQuarterLabel(quarterResults));
		statsLabel.setText(buildStatsLabel(quarterResults));
		updateFinanceValues(game);
	}

	private String buildQuarterLabel(GameResult[] quarterResults) {
		int q1h = 0, q1a = 0, q2h = 0, q2a = 0, q3h = 0, q3a = 0, q4h = 0, q4a = 0;
		if (quarterResults != null) {
			if (quarterResults.length > 0 && quarterResults[0] != null) {
				q1h = quarterResults[0].getScorehomeTeam();
				q1a = quarterResults[0].getScoreAwayTeam();
			}
			if (quarterResults.length > 1 && quarterResults[1] != null) {
				q2h = quarterResults[1].getScorehomeTeam();
				q2a = quarterResults[1].getScoreAwayTeam();
			}
			if (quarterResults.length > 2 && quarterResults[2] != null) {
				q3h = quarterResults[2].getScorehomeTeam();
				q3a = quarterResults[2].getScoreAwayTeam();
			}
			if (quarterResults.length > 3 && quarterResults[3] != null) {
				q4h = quarterResults[3].getScorehomeTeam();
				q4a = quarterResults[3].getScoreAwayTeam();
			}
		}
		return "Q1 " + q1h + "-" + q1a + " | Q2 " + q2h + "-" + q2a + " | Q3 " + q3h + "-" + q3a + " | Q4 " + q4h + "-" + q4a;
	}

	private String buildStatsLabel(GameResult[] quarterResults) {
		int twoHome = 0, twoAway = 0;
		int threeHome = 0, threeAway = 0;
		int rebHome = 0, rebAway = 0;

		if (quarterResults != null) {
			for (GameResult quarterResult : quarterResults) {
				if (quarterResult != null) {
					twoHome += quarterResult.getTwoPointsHomeTeam();
					twoAway += quarterResult.getTwoPointsAwayTeam();
					threeHome += quarterResult.getThreePointsHomeTeam();
					threeAway += quarterResult.getThreePointsAwayTeam();
					rebHome += quarterResult.getReboundHomeTeam();
					rebAway += quarterResult.getReboundAwayTeam();
				}
			}
		}

		return "2PTS " + twoHome + "-" + twoAway + " | 3PTS " + threeHome + "-" + threeAway + " | REB " + rebHome + "-" + rebAway;
	}

	private void updateFinanceValues(Game game) {
		GameStat gameStat = leagueManager.getFinanceManager().getGameStat(game);
		if (gameStat == null) {
			attendanceLabel.setText("Spectateurs : -");
			homeRevenueLabel.setText("Domicile revenus : -");
			homeExpenseLabel.setText("Domicile dépenses : -");
			awayRevenueLabel.setText("Extérieur revenus : -");
			awayExpenseLabel.setText("Extérieur dépenses : -");
			netLabel.setText("Résultat net : -");
			return;
		}

		TeamGameFinance home = gameStat.getHomeFinance();
		TeamGameFinance away = gameStat.getAwayFinance();

		double homeRevenue = computeRevenue(home);
		double homeExpense = computeExpense(home);
		double awayRevenue = computeRevenue(away);
		double awayExpense = computeExpense(away);

		attendanceLabel.setText("Spectateurs : " + gameStat.getAttendees());
		homeRevenueLabel.setText("Domicile revenus : " + formatMoney(homeRevenue));
		homeExpenseLabel.setText("Domicile dépenses : -" + formatMoney(homeExpense));
		awayRevenueLabel.setText("Extérieur revenus : " + formatMoney(awayRevenue));
		awayExpenseLabel.setText("Extérieur dépenses : -" + formatMoney(awayExpense));
		netLabel.setText("Résultat net : " + formatMoney((homeRevenue + awayRevenue) - (homeExpense + awayExpense)));
	}

	private double computeRevenue(TeamGameFinance teamGameFinance) {
		return teamGameFinance.getTicketRevenue()
				+ teamGameFinance.getConcessionsRevenue()
				+ teamGameFinance.getMerchRevenue()
				+ teamGameFinance.getTvRevenue()
				+ teamGameFinance.getParkingRevenue();
	}

	private double computeExpense(TeamGameFinance teamGameFinance) {
		return teamGameFinance.getArenaCosts()
				+ teamGameFinance.getStaffCosts()
				+ teamGameFinance.getSecurityCosts()
				+ teamGameFinance.getLogisticsCosts()
				+ teamGameFinance.getTravelCosts();
	}

	private String formatMoney(double amount) {
		return String.format("%.2fM", amount);
	}

	private void resetSelectedGame() {
		selectedGameDay = null;
		selectedGame = null;
		teamsLabel.setText("HOME vs AWAY");
		showHiddenResultState();
	}

	private void showHiddenResultState() {
		mainScoreLabel.setText("Non simulé");
		quarterLabel.setText("Résultats masqués");
		statsLabel.setText("Statistiques indisponibles");
		attendanceLabel.setText("Spectateurs : -");
		homeRevenueLabel.setText("Domicile revenus : -");
		homeExpenseLabel.setText("Domicile dépenses : -");
		awayRevenueLabel.setText("Extérieur revenus : -");
		awayExpenseLabel.setText("Extérieur dépenses : -");
		netLabel.setText("Résultat net : -");
	}

	public void refreshSelectedGame() {
		if (selectedGame != null) {
			updateSelectedGame(selectedGame);
		}
	}
}

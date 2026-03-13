package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.calendar.GameDay;
import data.sport.setup.Game;
import gui.panel.common.BuildBox;

public class MatchDashboard extends JPanel {

	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
	private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 270;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 300;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);
	private static final DateTimeFormatter HEADER_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final JLabel headerTitleLabel = new JLabel("SAISON RÉGULIÈRE");
	private final JLabel headerSubtitleLabel = new JLabel("Aucun jour sélectionné");
	private final JPanel matchesListPanel = new JPanel();
	private final JLabel matchTitleLabel = new JLabel("Aucun match sélectionné");
	private final JLabel matchStateLabel = new JLabel("Sélectionne un jour depuis le calendrier.");
	private final JLabel matchScoreLabel = new JLabel("Score indisponible");
	private final JLabel financeLabel = new JLabel("Aucune donnée financière disponible.");

	public MatchDashboard() {
		setLayout(new BorderLayout());
		setBackground(IDEAL_DASHBOARD_BACKGROUND_COLOR);

		JPanel content = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		content.setOpaque(false);
		content.setBorder(BorderFactory.createEmptyBorder(0, IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		add(content, BorderLayout.CENTER);

		content.add(buildHeader(), BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);
	}

	private JPanel buildHeader() {
		JPanel header = new JPanel();
		header.setOpaque(false);
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
		headerTitleLabel.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.BOLD, 15));
		headerSubtitleLabel.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 13));
		headerSubtitleLabel.setForeground(new Color(0x6D, 0x75, 0x83));
		header.add(headerTitleLabel);
		header.add(Box.createVerticalStrut(3));
		header.add(headerSubtitleLabel);
		header.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, IDEAL_DASHBOARD_HEADER_HEIGHT));
		return header;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, 0));
		body.setOpaque(false);

		JPanel leftCard = new BuildBox("MATCHS DU JOUR", "Liste des rencontres", buildMatchesListPanel());
		leftCard.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, 0));

		JPanel centerCard = new BuildBox("MATCH SÉLECTIONNÉ", "Détails du match", buildMatchDetailPanel());

		JPanel rightCard = new BuildBox("FINANCES DU MATCH", "Revenus et dépenses", buildFinancePanel());
		rightCard.setPreferredSize(new Dimension(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, 0));

		body.add(leftCard, BorderLayout.WEST);
		body.add(centerCard, BorderLayout.CENTER);
		body.add(rightCard, BorderLayout.EAST);

		return body;
	}

	private JPanel buildMatchesListPanel() {
		matchesListPanel.setLayout(new BoxLayout(matchesListPanel, BoxLayout.Y_AXIS));
		matchesListPanel.setOpaque(false);
		matchesListPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		matchesListPanel.add(new JLabel("Aucun match à afficher."));
		return matchesListPanel;
	}

	private JPanel buildMatchDetailPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		matchTitleLabel.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.BOLD, 14));
		panel.add(matchTitleLabel);
		panel.add(Box.createVerticalStrut(8));
		panel.add(matchStateLabel);
		panel.add(Box.createVerticalStrut(6));
		panel.add(matchScoreLabel);

		return panel;
	}

	private JPanel buildFinancePanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		panel.add(financeLabel);
		return panel;
	}

	public void showGameDay(GameDay gameDay, LocalDate date) {
		headerTitleLabel.setText("MATCHS DU " + HEADER_DATE_FORMATTER.format(date));
		headerSubtitleLabel.setText(gameDay == null || gameDay.isEmpty()
				? "Aucun match programmé"
				: gameDay.getGames().size() + " matchs programmés");

		matchesListPanel.removeAll();
		if (gameDay == null || gameDay.isEmpty()) {
			matchesListPanel.add(new JLabel("Aucun match à afficher."));
			showGame(null);
		} else {
			for (Game game : gameDay.getGames()) {
				matchesListPanel.add(buildGameButton(game));
				matchesListPanel.add(Box.createVerticalStrut(8));
			}
			showGame(gameDay.getGames().get(0));
		}
		matchesListPanel.revalidate();
		matchesListPanel.repaint();
	}

	private JButton buildGameButton(Game game) {
		String teams = game.getGameContext().getAwayTeam().getName() + " vs " + game.getGameContext().getHomeTeam().getName();
		String status = isGameSimulated(game) ? "Simulé" : "À simuler";
		JButton button = new JButton(teams + " - " + status);
		button.setAlignmentX(LEFT_ALIGNMENT);
		button.addActionListener(e -> showGame(game));
		return button;
	}

	private void showGame(Game game) {
		if (game == null) {
			matchTitleLabel.setText("Aucun match sélectionné");
			matchStateLabel.setText("Sélectionne un jour depuis le calendrier.");
			matchScoreLabel.setText("Score indisponible");
			financeLabel.setText("Aucune donnée financière disponible.");
			return;
		}

		matchTitleLabel.setText(game.getGameContext().getAwayTeam().getName() + " vs "
				+ game.getGameContext().getHomeTeam().getName());
		if (isGameSimulated(game)) {
			int displayedAwayScore = getDisplayedAwayScore(game);
			int displayedHomeScore = getDisplayedHomeScore(game);
			matchStateLabel.setText("Match déjà simulé");
			matchScoreLabel.setText(
					game.getGameContext().getAwayTeam().getName() + " " + displayedAwayScore
							+ " - "
							+ displayedHomeScore + " " + game.getGameContext().getHomeTeam().getName());
			financeLabel.setText("Simulation terminée. Données financières à brancher.");
		} else {
			matchStateLabel.setText("Match non simulé");
			matchScoreLabel.setText("Le score apparaîtra après simulation.");
			financeLabel.setText("Le match n'est pas encore simulé.");
		}
	}

	private boolean isGameSimulated(Game game) {
		if (game.getQuarterResults() == null) {
			return false;
		}
		for (int index = 0; index < game.getQuarterResults().length; index++) {
			if (game.getQuarterResults()[index] == null) {
				return false;
			}
		}
		return true;
	}

	private int getDisplayedHomeScore(Game game) {
		if (game.getQuarterResults() == null) {
			return game.getHomeFinalScore();
		}
		int total = 0;
		for (int index = 0; index < game.getQuarterResults().length; index++) {
			if (game.getQuarterResults()[index] != null) {
				total += game.getQuarterResults()[index].getScorehomeTeam();
			}
		}
		return total;
	}

	private int getDisplayedAwayScore(Game game) {
		if (game.getQuarterResults() == null) {
			return game.getAwayFinalScore();
		}
		int total = 0;
		for (int index = 0; index < game.getQuarterResults().length; index++) {
			if (game.getQuarterResults()[index] != null) {
				total += game.getQuarterResults()[index].getScoreAwayTeam();
			}
		}
		return total;
	}
}

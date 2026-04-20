package gui.panel.matchPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.sport.setup.Game;
import data.sport.setup.GameResult;
import data.team.Team;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.ThemeAware;
import gui.panel.mapPanel.effectifPanel.teamPanel.TeamLogoPanel;
import gui.utility.TeamDisplayUtility;

public class MatchResultPanel extends JPanel implements ThemeAware {
	private JLabel titleLabel;
	private JLabel matchStatusLabel;
	private TeamLogoPanel homeLogoPanel;
	private TeamLogoPanel awayLogoPanel;
	private JLabel homeNameLabel;
	private JLabel awayNameLabel;
	private JLabel homeCityLabel;
	private JLabel awayCityLabel;
	private JLabel mainScoreLabel;
	private JLabel quarterTitleLabel;
	private JLabel homeQuarterTeamLabel;
	private JLabel awayQuarterTeamLabel;
	private JLabel[] homeQuarterLabels;
	private JLabel[] awayQuarterLabels;
	private JLabel homeQuarterTotalLabel;
	private JLabel awayQuarterTotalLabel;
	private JLabel scoreSectionTitleLabel;
	private JLabel[] quarterHeaderLabels;

	public MatchResultPanel() {
		super(new BorderLayout(0, 12));
		setOpaque(false);
		add(buildScoreHeaderPanel(), BorderLayout.NORTH);
		add(buildQuarterPanel(), BorderLayout.CENTER);
		applyTheme();
	}

	public void showHiddenState(Game game, String dayLabel) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();
		updateTeamLabels(homeTeam, awayTeam, dayLabel);
		matchStatusLabel.setText("A venir");
		mainScoreLabel.setText("--");
		quarterTitleLabel.setText("Resultats masques");
		homeQuarterTeamLabel.setText(TeamDisplayUtility.getShortName(homeTeam));
		awayQuarterTeamLabel.setText(TeamDisplayUtility.getShortName(awayTeam));
		resetQuarterTable();
	}

	public void showGame(Game game, String dayLabel) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();
		updateTeamLabels(homeTeam, awayTeam, dayLabel);
		matchStatusLabel.setText("Termine");
		mainScoreLabel.setText(game.getHomeFinalScore() + " - " + game.getAwayFinalScore());
		quarterTitleLabel.setText("Match termine");
		updateQuarterTable(game.getQuarterResults(), homeTeam, awayTeam);
	}

	public void showEmptyState() {
		titleLabel.setText("SAISON REGULIERE");
		matchStatusLabel.setText("A venir");
		homeLogoPanel.setTeamName("");
		awayLogoPanel.setTeamName("");
		homeNameLabel.setText("Home");
		awayNameLabel.setText("Away");
		homeCityLabel.setText("-");
		awayCityLabel.setText("-");
		mainScoreLabel.setText("--");
		quarterTitleLabel.setText("Resultats masques");
		homeQuarterTeamLabel.setText("Home");
		awayQuarterTeamLabel.setText("Away");
		resetQuarterTable();
	}

	private JPanel buildScoreHeaderPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 0));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		topPanel.setOpaque(false);
		titleLabel = new JLabel("SAISON REGULIERE");
		LabelStyleUtil.styleSubtitleLabel(titleLabel, 15);
		matchStatusLabel = new JLabel("A venir");
		LabelStyleUtil.styleValueLabel(matchStatusLabel, 12);
		topPanel.add(titleLabel);
		topPanel.add(matchStatusLabel);

		JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 0));
		centerPanel.setOpaque(false);
		centerPanel.add(buildTeamPanel(true));
		centerPanel.add(buildScorePanel());
		centerPanel.add(buildTeamPanel(false));

		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(centerPanel, BorderLayout.CENTER);
		return panel;
	}

	private JPanel buildTeamPanel(boolean home) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		TeamLogoPanel logoPanel = new TeamLogoPanel("", 60);
		logoPanel.setAlignmentX(CENTER_ALIGNMENT);

		JLabel nameLabel = new JLabel(home ? "Home" : "Away");
		LabelStyleUtil.styleValueLabel(nameLabel, 13);
		nameLabel.setAlignmentX(CENTER_ALIGNMENT);

		JLabel cityLabel = new JLabel("-");
		LabelStyleUtil.styleSubtitleLabel(cityLabel, 11);
		cityLabel.setAlignmentX(CENTER_ALIGNMENT);

		panel.add(logoPanel);
		panel.add(Box.createVerticalStrut(6));
		panel.add(nameLabel);
		panel.add(Box.createVerticalStrut(3));
		panel.add(cityLabel);

		if (home) {
			homeLogoPanel = logoPanel;
			homeNameLabel = nameLabel;
			homeCityLabel = cityLabel;
		} else {
			awayLogoPanel = logoPanel;
			awayNameLabel = nameLabel;
			awayCityLabel = cityLabel;
		}
		return panel;
	}

	private JPanel buildScorePanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		mainScoreLabel = new JLabel("0 - 0", JLabel.CENTER);
		mainScoreLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		mainScoreLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		mainScoreLabel.setAlignmentX(CENTER_ALIGNMENT);
		mainScoreLabel.setHorizontalAlignment(JLabel.CENTER);

		quarterTitleLabel = new JLabel("Resultat masque");
		LabelStyleUtil.styleSubtitleLabel(quarterTitleLabel, 12);
		quarterTitleLabel.setAlignmentX(CENTER_ALIGNMENT);

		panel.add(Box.createVerticalGlue());
		panel.add(mainScoreLabel);
		panel.add(Box.createVerticalStrut(3));
		panel.add(quarterTitleLabel);
		panel.add(Box.createVerticalGlue());
		return panel;
	}

	private JPanel buildQuarterPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 8));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

		scoreSectionTitleLabel = new JLabel("SCORE PAR QUART-TEMPS");
		LabelStyleUtil.styleTitleLabel(scoreSectionTitleLabel, 13);
		panel.add(scoreSectionTitleLabel, BorderLayout.NORTH);

		JPanel table = new JPanel(new GridLayout(3, 6, 10, 6));
		table.setOpaque(false);
		quarterHeaderLabels = new JLabel[] {
				new JLabel("EQUIPE"),
				new JLabel("Q1", JLabel.CENTER),
				new JLabel("Q2", JLabel.CENTER),
				new JLabel("Q3", JLabel.CENTER),
				new JLabel("Q4", JLabel.CENTER),
				new JLabel("TOTAL", JLabel.CENTER)
		};
		for (int i = 0; i < quarterHeaderLabels.length; i++) {
			LabelStyleUtil.styleValueLabel(quarterHeaderLabels[i], 11);
			table.add(quarterHeaderLabels[i]);
		}

		homeQuarterTeamLabel = new JLabel("Home");
		awayQuarterTeamLabel = new JLabel("Away");
		homeQuarterLabels = createQuarterLabels();
		awayQuarterLabels = createQuarterLabels();
		homeQuarterTotalLabel = new JLabel("-", JLabel.CENTER);
		awayQuarterTotalLabel = new JLabel("-", JLabel.CENTER);
		LabelStyleUtil.styleValueLabel(homeQuarterTeamLabel, 11);
		LabelStyleUtil.styleValueLabel(awayQuarterTeamLabel, 11);
		LabelStyleUtil.styleValueLabel(homeQuarterTotalLabel, 11);
		LabelStyleUtil.styleValueLabel(awayQuarterTotalLabel, 11);

		table.add(homeQuarterTeamLabel);
		addQuarterRow(table, homeQuarterLabels, homeQuarterTotalLabel);
		table.add(awayQuarterTeamLabel);
		addQuarterRow(table, awayQuarterLabels, awayQuarterTotalLabel);

		panel.add(table, BorderLayout.CENTER);
		return panel;
	}

	private JLabel[] createQuarterLabels() {
		JLabel[] labels = new JLabel[] {
				new JLabel("-", JLabel.CENTER),
				new JLabel("-", JLabel.CENTER),
				new JLabel("-", JLabel.CENTER),
				new JLabel("-", JLabel.CENTER)
		};
		for (int i = 0; i < labels.length; i++) {
			LabelStyleUtil.styleValueLabel(labels[i], 11);
		}
		return labels;
	}

	private void addQuarterRow(JPanel table, JLabel[] quarterLabels, JLabel totalLabel) {
		for (int i = 0; i < quarterLabels.length; i++) {
			table.add(quarterLabels[i]);
		}
		table.add(totalLabel);
	}

	private void updateTeamLabels(Team homeTeam, Team awayTeam, String dayLabel) {
		titleLabel.setText("SAISON REGULIERE - " + dayLabel.toUpperCase());
		homeLogoPanel.setTeamName(homeTeam.getName());
		awayLogoPanel.setTeamName(awayTeam.getName());
		homeNameLabel.setText(TeamDisplayUtility.getShortName(homeTeam));
		awayNameLabel.setText(TeamDisplayUtility.getShortName(awayTeam));
		homeCityLabel.setText(TeamDisplayUtility.getCityName(homeTeam));
		awayCityLabel.setText(TeamDisplayUtility.getCityName(awayTeam));
	}

	private void updateQuarterTable(GameResult[] quarterResults, Team homeTeam, Team awayTeam) {
		homeQuarterTeamLabel.setText(TeamDisplayUtility.getShortName(homeTeam));
		awayQuarterTeamLabel.setText(TeamDisplayUtility.getShortName(awayTeam));
		int homeTotal = 0;
		int awayTotal = 0;
		for (int i = 0; i < 4; i++) {
			int homeScore = 0;
			int awayScore = 0;
			if (quarterResults != null && quarterResults.length > i && quarterResults[i] != null) {
				homeScore = quarterResults[i].getScorehomeTeam();
				awayScore = quarterResults[i].getScoreAwayTeam();
			}
			homeQuarterLabels[i].setText(String.valueOf(homeScore));
			awayQuarterLabels[i].setText(String.valueOf(awayScore));
			homeTotal += homeScore;
			awayTotal += awayScore;
		}
		homeQuarterTotalLabel.setText(String.valueOf(homeTotal));
		awayQuarterTotalLabel.setText(String.valueOf(awayTotal));
	}

	private void resetQuarterTable() {
		for (int i = 0; i < 4; i++) {
			homeQuarterLabels[i].setText("-");
			awayQuarterLabels[i].setText("-");
		}
		homeQuarterTotalLabel.setText("-");
		awayQuarterTotalLabel.setText("-");
	}

	@Override
	public void applyTheme() {
		titleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		matchStatusLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		homeNameLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		awayNameLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		homeCityLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		awayCityLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		mainScoreLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		quarterTitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		scoreSectionTitleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		for (int i = 0; i < quarterHeaderLabels.length; i++) {
			quarterHeaderLabels[i].setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		}
		homeQuarterTeamLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		awayQuarterTeamLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		for (int i = 0; i < homeQuarterLabels.length; i++) {
			homeQuarterLabels[i].setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
			awayQuarterLabels[i].setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		}
		homeQuarterTotalLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		awayQuarterTotalLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
	}
}

package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.components.PlayoffsImageBracketPanel;
import data.league.League;
import data.league.Playoff;
import data.league.PlayoffRound;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.PlaceholderPanel;
import gui.panel.common.RoundedPanel;
import gui.panel.common.ThemeAware;
import gui.panel.mapPanel.effectifPanel.teamPanel.TeamLogoPanel;
import gui.utility.TeamDisplayUtility;
import process.orchestrator.interf.GUIInterface;

public class RankingPlayoffsViewPanel extends JPanel implements ThemeAware {
	private static final String GLOBAL_MODE = "global";
	private static final String EAST_MODE = "east";
	private static final String WEST_MODE = "west";

	private final GUIInterface guiInterface;
	private String selectedMode;
	private PlayoffsImageBracketPanel bracketPanel;

	public RankingPlayoffsViewPanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		selectedMode = GLOBAL_MODE;
		bracketPanel = new PlayoffsImageBracketPanel();
		setLayout(new BorderLayout());
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		refreshPlayoffs();
	}

	public void setSelectedMode(String selectedMode) {
		this.selectedMode = selectedMode;
	}

	public void refreshPlayoffs() {
		removeAll();

		Playoff playoff = getPlayoff();
		if (guiInterface == null || !guiInterface.hasPlayoffsStarted()) {
			add(new PlaceholderPanel("Les playoffs ne sont pas encore disponibles. "
					+ "Terminez ou simulez la saison reguliere pour generer le tableau."),
					BorderLayout.CENTER);
			revalidate();
			repaint();
			return;
		}
		if (!hasPlayoffData(playoff)) {
			add(new PlaceholderPanel("Les playoffs ne sont pas encore disponibles. "
					+ "Terminez ou simulez la saison reguliere pour generer le tableau."),
					BorderLayout.CENTER);
			revalidate();
			repaint();
			return;
		}

		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new BorderLayout(0, 16));
		content.add(buildSummaryCard(playoff), BorderLayout.NORTH);
		content.add(buildBracketCard(), BorderLayout.CENTER);

		add(content, BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	private JPanel buildBracketCard() {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BorderLayout());
		card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		bracketPanel.refreshFromPlayoffsData(guiInterface.getPlayoffPositionMap());
		card.add(bracketPanel, BorderLayout.CENTER);
		return card;
	}

	private Playoff getPlayoff() {
		return guiInterface == null ? null : guiInterface.getPlayoff();
	}

	private boolean hasPlayoffData(Playoff playoff) {
		if (playoff == null) {
			return false;
		}
		return !playoff.getQualifiedEastTeams().isEmpty()
				|| !playoff.getQualifiedWestTeams().isEmpty()
				|| !playoff.getEastFirstRound().isEmpty()
				|| !playoff.getWestFirstRound().isEmpty()
				|| !playoff.getEastConferenceSemis().isEmpty()
				|| !playoff.getWestConferenceSemis().isEmpty()
				|| !playoff.getEastConferenceFinals().isEmpty()
				|| !playoff.getWestConferenceFinals().isEmpty()
				|| !playoff.getNbaFinals().isEmpty();
	}

	private JPanel buildSummaryCard(Playoff playoff) {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BorderLayout(12, 0));
		card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel("Tableau des playoffs");
		LabelStyleUtil.styleTitleLabel(titleLabel, 16);

		JLabel subtitleLabel = new JLabel(buildSummarySubtitle(playoff));
		LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 12);

		textPanel.add(titleLabel);
		textPanel.add(Box.createVerticalStrut(4));
		textPanel.add(subtitleLabel);
		if (playoff.getChampion() != null) {
			JLabel championLabel = new JLabel("Champion NBA : "
					+ TeamDisplayUtility.getShortName(playoff.getChampion()));
			LabelStyleUtil.styleValueLabel(championLabel, 13);
			textPanel.add(Box.createVerticalStrut(6));
			textPanel.add(championLabel);
		}

		JPanel badgesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		badgesPanel.setOpaque(false);
		badgesPanel.add(createBadge("Round", getRoundLabel(playoff.getCurrentRound())));
		badgesPanel.add(createBadge("Series", String.valueOf(countBuiltSeries(playoff))));

		card.add(textPanel, BorderLayout.CENTER);
		card.add(badgesPanel, BorderLayout.EAST);
		return card;
	}

	private String buildSummarySubtitle(Playoff playoff) {
		int qualifiedTeams = playoff.getQualifiedEastTeams().size() + playoff.getQualifiedWestTeams().size();
		if (qualifiedTeams == 0) {
			return "Les playoffs ne sont pas encore lances.";
		}
		return qualifiedTeams + " equipes qualifiees pour la phase finale.";
	}

	private JPanel createBadge(String label, String value) {
		RoundedPanel badge = new RoundedPanel(new FlowLayout(FlowLayout.CENTER, 0, 0), 18);
		badge.setBackground(DashboardPanelUtil.getPrimaryActionColor());
		badge.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
		badge.setPreferredSize(new Dimension(120, 34));

		JLabel badgeLabel = new JLabel(label + " : " + value);
		badgeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		badgeLabel.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
		badge.add(badgeLabel);
		return badge;
	}

	private JPanel buildConferenceSection(Playoff playoff) {
		if (GLOBAL_MODE.equals(selectedMode)) {
			JPanel grid = new JPanel(new GridLayout(1, 2, 16, 0));
			grid.setOpaque(false);
			grid.add(buildConferenceCard(
					"Conference Est",
					playoff.getQualifiedEastTeams(),
					playoff.getEastFirstRound(),
					playoff.getEastConferenceSemis(),
					playoff.getEastConferenceFinals()));
			grid.add(buildConferenceCard(
					"Conference Ouest",
					playoff.getQualifiedWestTeams(),
					playoff.getWestFirstRound(),
					playoff.getWestConferenceSemis(),
					playoff.getWestConferenceFinals()));
			return grid;
		}

		if (EAST_MODE.equals(selectedMode)) {
			return buildConferenceCard(
					"Conference Est",
					playoff.getQualifiedEastTeams(),
					playoff.getEastFirstRound(),
					playoff.getEastConferenceSemis(),
					playoff.getEastConferenceFinals());
		}

		return buildConferenceCard(
				"Conference Ouest",
				playoff.getQualifiedWestTeams(),
				playoff.getWestFirstRound(),
				playoff.getWestConferenceSemis(),
				playoff.getWestConferenceFinals());
	}

	private boolean shouldShowFinals() {
		return GLOBAL_MODE.equals(selectedMode);
	}

	private JPanel buildConferenceCard(String title, ArrayList<Team> qualifiedTeams, ArrayList<PlayoffSeries> firstRound,
			ArrayList<PlayoffSeries> conferenceSemis, ArrayList<PlayoffSeries> conferenceFinals) {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BorderLayout());
		card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel(title);
		LabelStyleUtil.styleTitleLabel(titleLabel, 15);
		content.add(titleLabel);
		content.add(Box.createVerticalStrut(10));
		content.add(buildQualifiedTeamsPanel(qualifiedTeams));

		addRoundSection(content, "Premier tour", firstRound);
		addRoundSection(content, "Demi-finales de conference", conferenceSemis);
		addRoundSection(content, "Finale de conference", conferenceFinals);

		card.add(content, BorderLayout.CENTER);
		return card;
	}

	private JPanel buildFinalsCard(ArrayList<PlayoffSeries> nbaFinals) {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BorderLayout());
		card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel("Finales NBA");
		LabelStyleUtil.styleTitleLabel(titleLabel, 15);
		content.add(titleLabel);
		content.add(Box.createVerticalStrut(12));
		content.add(buildSeriesList(nbaFinals));

		card.add(content, BorderLayout.CENTER);
		return card;
	}

	private JPanel buildQualifiedTeamsPanel(ArrayList<Team> qualifiedTeams) {
		JPanel wrapper = new JPanel(new BorderLayout(0, 8));
		wrapper.setOpaque(false);

		JLabel subtitleLabel = new JLabel("Equipes qualifiees");
		LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 12);
		wrapper.add(subtitleLabel, BorderLayout.NORTH);

		if (qualifiedTeams == null || qualifiedTeams.isEmpty()) {
			JLabel emptyLabel = new JLabel("Aucune equipe n'est encore qualifiee.");
			LabelStyleUtil.styleSubtitleLabel(emptyLabel, 12);
			wrapper.add(emptyLabel, BorderLayout.CENTER);
			return wrapper;
		}

		int rows = (int) Math.ceil(qualifiedTeams.size() / 2.0);
		JPanel seedsGrid = new JPanel(new GridLayout(Math.max(1, rows), 2, 12, 6));
		seedsGrid.setOpaque(false);

		for (int index = 0; index < qualifiedTeams.size(); index++) {
			seedsGrid.add(createQualifiedTeamLabel(index + 1, qualifiedTeams.get(index)));
		}
		if (qualifiedTeams.size() % 2 != 0) {
			JPanel filler = new JPanel();
			filler.setOpaque(false);
			seedsGrid.add(filler);
		}

		wrapper.add(seedsGrid, BorderLayout.CENTER);
		return wrapper;
	}

	private JLabel createQualifiedTeamLabel(int seed, Team team) {
		JLabel label = new JLabel(seed + ". " + TeamDisplayUtility.getShortName(team)
				+ " (" + TeamDisplayUtility.getAbbreviation(team) + ")");
		LabelStyleUtil.styleValueLabel(label, 12);
		return label;
	}

	private void addRoundSection(JPanel content, String title, ArrayList<PlayoffSeries> seriesList) {
		if (seriesList == null || seriesList.isEmpty()) {
			return;
		}
		content.add(Box.createVerticalStrut(14));

		JLabel sectionLabel = new JLabel(title);
		LabelStyleUtil.styleSubtitleLabel(sectionLabel, 12);
		content.add(sectionLabel);
		content.add(Box.createVerticalStrut(8));
		content.add(buildSeriesList(seriesList));
	}

	private JPanel buildSeriesList(ArrayList<PlayoffSeries> seriesList) {
		JPanel list = new JPanel();
		list.setOpaque(false);
		list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

		for (PlayoffSeries series : seriesList) {
			list.add(buildSeriesRow(series));
			list.add(Box.createVerticalStrut(8));
		}

		if (list.getComponentCount() > 0) {
			list.remove(list.getComponentCount() - 1);
		}
		return list;
	}

	private JPanel buildSeriesRow(PlayoffSeries series) {
		RoundedPanel row = new RoundedPanel(new BorderLayout(12, 0), 18);
		row.setBackground(getSeriesRowBackground());
		row.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

		row.add(createTeamPanel(series.getHigherTeam(), series.getHigherTeamWins(), true,
				series.getHigherTeamWins() >= series.getLowerTeamWins()), BorderLayout.WEST);
		row.add(createSeriesScorePanel(series), BorderLayout.CENTER);
		row.add(createTeamPanel(series.getLowerTeam(), series.getLowerTeamWins(), false,
				series.getLowerTeamWins() > series.getHigherTeamWins()), BorderLayout.EAST);
		return row;
	}

	private JPanel createTeamPanel(Team team, int wins, boolean leftAligned, boolean leading) {
		JPanel teamPanel = new JPanel(new FlowLayout(leftAligned ? FlowLayout.LEFT : FlowLayout.RIGHT, 8, 0));
		teamPanel.setOpaque(false);
		teamPanel.setPreferredSize(new Dimension(180, 36));

		TeamLogoPanel logoPanel = new TeamLogoPanel(team == null ? "" : team.getName(), 26);
		logoPanel.setTeamQueryInterface(guiInterface);

		JLabel teamLabel = new JLabel(TeamDisplayUtility.getShortName(team));
		teamLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		teamLabel.setForeground(leading ? DashboardPanelUtil.REVENUE_COLOR : DashboardPanelUtil.TITLE_TEXT_COLOR);

		JLabel winsLabel = new JLabel(String.valueOf(wins));
		winsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		winsLabel.setForeground(leading ? DashboardPanelUtil.REVENUE_COLOR : DashboardPanelUtil.SUBTITLE_TEXT_COLOR);

		if (leftAligned) {
			teamPanel.add(logoPanel);
			teamPanel.add(teamLabel);
			teamPanel.add(winsLabel);
			return teamPanel;
		}

		teamPanel.add(winsLabel);
		teamPanel.add(teamLabel);
		teamPanel.add(logoPanel);
		return teamPanel;
	}

	private JPanel createSeriesScorePanel(PlayoffSeries series) {
		JPanel scorePanel = new JPanel();
		scorePanel.setOpaque(false);
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));

		JLabel scoreLabel = new JLabel(series.getHigherTeamWins() + " - " + series.getLowerTeamWins(), JLabel.CENTER);
		LabelStyleUtil.styleValueLabel(scoreLabel, 18);
		scoreLabel.setAlignmentX(CENTER_ALIGNMENT);

		JLabel statusLabel = new JLabel(getSeriesStatusLabel(series), JLabel.CENTER);
		LabelStyleUtil.styleSubtitleLabel(statusLabel, 11);
		statusLabel.setAlignmentX(CENTER_ALIGNMENT);

		scorePanel.add(scoreLabel);
		scorePanel.add(Box.createVerticalStrut(4));
		scorePanel.add(statusLabel);
		return scorePanel;
	}

	private String getSeriesStatusLabel(PlayoffSeries series) {
		if (series.isFinished()) {
			return "Terminee";
		}
		if (series.getNumberPlayedGames() == 0) {
			return "A jouer";
		}
		return "En cours - match " + Math.min(7, series.getNumberPlayedGames() + 1);
	}

	private Color getSeriesRowBackground() {
		if (DashboardPanelUtil.isDarkMode()) {
			return new Color(39, 43, 50);
		}
		return new Color(247, 249, 252);
	}

	private int countBuiltSeries(Playoff playoff) {
		return playoff.getEastFirstRound().size()
				+ playoff.getWestFirstRound().size()
				+ playoff.getEastConferenceSemis().size()
				+ playoff.getWestConferenceSemis().size()
				+ playoff.getEastConferenceFinals().size()
				+ playoff.getWestConferenceFinals().size()
				+ playoff.getNbaFinals().size();
	}

	private String getRoundLabel(PlayoffRound round) {
		if (round == null) {
			return "A venir";
		}
		switch (round) {
		case FIRST_ROUND:
			return "Premier tour";
		case CONFERENCE_SEMIFINALS:
			return "Demies";
		case CONFERENCE_FINALS:
			return "Finales conf.";
		case NBA_FINALS:
			return "Finales NBA";
		default:
			return round.name();
		}
	}

	@Override
	public void applyTheme() {
		refreshPlayoffs();
	}
}

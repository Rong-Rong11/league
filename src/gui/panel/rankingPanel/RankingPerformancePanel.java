package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import data.team.Team;
import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.PlayerDisplayUtil;
import gui.panel.common.RoundedPanel;
import gui.panel.common.ThemeAware;
import gui.panel.mapPanel.effectifPanel.teamPanel.TeamLogoPanel;
import process.orchestrator.interfaces.GUIInterface;
import gui.utility.TeamDisplayUtility;

public class RankingPerformancePanel extends JPanel implements ThemeAware {
	private static final int CARD_HEIGHT = 108;

	private GUIInterface guiInterface;
	private TeamLogoPanel leaderLogoPanel;
	private JLabel leaderTeamValue;
	private JLabel leaderDetailLabel;
	private JTextArea leaderStatsLabel;
	private RoundedPanel leaderDetailBadge;
	private TeamLogoPanel streakLogoPanel;
	private JLabel streakTeamValue;
	private JLabel streakDetailLabel;
	private JTextArea streakStatsLabel;
	private RoundedPanel streakDetailBadge;
	private TeamLogoPanel lastLogoPanel;
	private JLabel lastTeamValue;
	private JLabel lastDetailLabel;
	private JTextArea lastStatsLabel;
	private RoundedPanel lastDetailBadge;

	public RankingPerformancePanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		setLayout(new GridLayout(3, 1, 0, 12));
		setOpaque(false);

		leaderLogoPanel = createLogoPanel();
		leaderTeamValue = new JLabel();
		leaderDetailLabel = createDetailLabel();
		leaderStatsLabel = createStatsLabel();
		leaderDetailBadge = createDetailBadge(leaderDetailLabel);
		streakLogoPanel = createLogoPanel();
		streakTeamValue = new JLabel();
		streakDetailLabel = createDetailLabel();
		streakStatsLabel = createStatsLabel();
		streakDetailBadge = createDetailBadge(streakDetailLabel);
		lastLogoPanel = createLogoPanel();
		lastTeamValue = new JLabel();
		lastDetailLabel = createDetailLabel();
		lastStatsLabel = createStatsLabel();
		lastDetailBadge = createDetailBadge(lastDetailLabel);

		add(createPerformanceCard("Leader", leaderLogoPanel, leaderTeamValue, leaderStatsLabel, leaderDetailBadge));
		add(createPerformanceCard("Meilleure serie", streakLogoPanel, streakTeamValue, streakStatsLabel,
				streakDetailBadge));
		add(createPerformanceCard("Derniere place", lastLogoPanel, lastTeamValue, lastStatsLabel, lastDetailBadge));
		refreshPerformance();
		applyTheme();
	}

	public void refreshPerformance() {
		if (!guiInterface.isSeasonInitialized()) {
			showEmptyState();
			return;
		}

		ArrayList<Team> teams = guiInterface.getGlobalRanking();
		if (teams.isEmpty()) {
			showEmptyState();
			return;
		}

		Team leader = teams.get(0);
		Team last = teams.get(teams.size() - 1);
		Team bestStreakTeam = findBestStreakTeam(teams);

		updateTeamBlock(leaderLogoPanel, leaderTeamValue, leaderStatsLabel, leaderDetailLabel, leader);
		updateTeamBlock(lastLogoPanel, lastTeamValue, lastStatsLabel, lastDetailLabel, last);

		if (bestStreakTeam == null) {
			streakLogoPanel.setTeamName("");
			streakTeamValue.setText("Aucune serie n'est disponible.");
			streakDetailLabel.setText("Lancez la saison pour afficher les performances.");
			streakStatsLabel.setText("Aucune statistique n'est disponible pour le moment.");
		} else {
			streakLogoPanel.setTeamName(bestStreakTeam.getName());
			streakTeamValue.setText(TeamDisplayUtility.getShortName(bestStreakTeam));
			streakDetailLabel.setText(guiInterface.getTeamMaxWinStreak(bestStreakTeam) + " victoires");
			streakStatsLabel.setText(buildStatsText(bestStreakTeam));
		}
	}

	private void showEmptyState() {
		leaderLogoPanel.setTeamName("");
		leaderTeamValue.setText("Aucun leader n'est disponible.");
		leaderDetailLabel.setText("Lancez la saison pour afficher les performances.");
		leaderStatsLabel.setText("Aucune statistique n'est disponible pour le moment.");
		streakLogoPanel.setTeamName("");
		streakTeamValue.setText("Aucune serie n'est disponible.");
		streakDetailLabel.setText("Lancez la saison pour afficher les performances.");
		streakStatsLabel.setText("Aucune statistique n'est disponible pour le moment.");
		lastLogoPanel.setTeamName("");
		lastTeamValue.setText("Aucune equipe n'est classee.");
		lastDetailLabel.setText("Lancez la saison pour afficher les performances.");
		lastStatsLabel.setText("Aucune statistique n'est disponible pour le moment.");
	}

	private Team findBestStreakTeam(ArrayList<Team> teams) {
		Team bestTeam = null;
		int bestStreak = -1;
		for (Team team : teams) {
			int streak = guiInterface.getTeamMaxWinStreak(team);
			if (streak > bestStreak) {
				bestStreak = streak;
				bestTeam = team;
			}
		}
		return bestTeam;
	}

	private void updateTeamBlock(TeamLogoPanel logoPanel, JLabel teamValue, JTextArea statsLabel, JLabel detailLabel,
			Team team) {
		logoPanel.setTeamName(team.getName());
		teamValue.setText(TeamDisplayUtility.getShortName(team));
		statsLabel.setText(buildStatsText(team));
		detailLabel
				.setText("V " + guiInterface.getTeamNumberWin(team) + "  |  D " + guiInterface.getTeamNumberLose(team));
	}

	private String buildStatsText(Team team) {
		double points = guiInterface.getAveragePoints(team, true);
		double rebounds = guiInterface.getAverageRebounds(team, true);
		double assists = guiInterface.getAverageAssists(team, true);
		return "PTS " + PlayerDisplayUtil.formatOneDecimal(points)
				+ "\nREB " + PlayerDisplayUtil.formatOneDecimal(rebounds)
				+ "\nAST " + PlayerDisplayUtil.formatOneDecimal(assists);
	}

	private TeamLogoPanel createLogoPanel() {
		TeamLogoPanel logoPanel = new TeamLogoPanel("", 40);
		logoPanel.setTeamQueryInterface(guiInterface);
		return logoPanel;
	}

	private JLabel createDetailLabel() {
		JLabel label = new JLabel("Lancez la saison pour afficher les performances.", JLabel.CENTER);
		LabelStyleUtil.styleValueLabel(label, 11);
		return label;
	}

	private JTextArea createStatsLabel() {
		JTextArea label = new JTextArea("Aucune statistique n'est disponible pour le moment.");
		label.setOpaque(false);
		label.setEditable(false);
		label.setLineWrap(false);
		label.setWrapStyleWord(false);
		label.setRows(3);
		label.setBorder(null);
		label.setFocusable(false);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		label.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		return label;
	}

	private JPanel createPerformanceCard(String label, TeamLogoPanel logoPanel, JLabel teamValue, JTextArea statsLabel,
			RoundedPanel detailBadge) {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BorderLayout(0, 10));
		card.setPreferredSize(new java.awt.Dimension(10, CARD_HEIGHT));
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, DashboardPanelUtil.BORDER_COLOR),
				BorderFactory.createEmptyBorder(10, 12, 10, 12)));

		JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
		headerPanel.setOpaque(false);

		JPanel headerTextPanel = new JPanel();
		headerTextPanel.setOpaque(false);
		headerTextPanel.setLayout(new BoxLayout(headerTextPanel, BoxLayout.Y_AXIS));

		JLabel labelValue = new JLabel(label);
		LabelStyleUtil.styleSubtitleLabel(labelValue, 12);

		LabelStyleUtil.styleValueLabel(teamValue, 20);

		headerTextPanel.add(labelValue);
		headerTextPanel.add(teamValue);

		JPanel logoWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		logoWrapper.setOpaque(false);
		logoWrapper.setPreferredSize(new Dimension(60, 48));
		logoWrapper.add(logoPanel);

		JPanel statsWrapper = new JPanel(new BorderLayout());
		statsWrapper.setOpaque(false);
		statsWrapper.add(statsLabel, BorderLayout.WEST);

		JPanel fieldWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		fieldWrapper.setOpaque(false);
		fieldWrapper.add(detailBadge);

		headerPanel.add(headerTextPanel, BorderLayout.CENTER);
		headerPanel.add(logoWrapper, BorderLayout.EAST);

		card.add(headerPanel, BorderLayout.NORTH);
		card.add(statsWrapper, BorderLayout.CENTER);
		card.add(fieldWrapper, BorderLayout.SOUTH);
		return card;
	}

	private RoundedPanel createDetailBadge(JLabel detailLabel) {
		RoundedPanel badge = new RoundedPanel(new FlowLayout(FlowLayout.CENTER, 0, 0), 18);
		badge.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		badge.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
		badge.setPreferredSize(new Dimension(140, 30));
		badge.add(detailLabel);
		return badge;
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		applyTeamValueStyle(leaderTeamValue);
		applyTeamValueStyle(streakTeamValue);
		applyTeamValueStyle(lastTeamValue);
		applyStatsStyle(leaderStatsLabel);
		applyStatsStyle(streakStatsLabel);
		applyStatsStyle(lastStatsLabel);
		applyDetailTheme(leaderDetailBadge, leaderDetailLabel);
		applyDetailTheme(streakDetailBadge, streakDetailLabel);
		applyDetailTheme(lastDetailBadge, lastDetailLabel);
		revalidate();
		repaint();
	}

	private void applyDetailTheme(RoundedPanel badge, JLabel label) {
		badge.setBackground(DashboardPanelUtil.getPrimaryActionColor());
		LabelStyleUtil.styleValueLabel(label, 12);
		label.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
	}

	private void applyTeamValueStyle(JLabel label) {
		if (label.getText() != null && label.getText().startsWith("Aucune")) {
			LabelStyleUtil.styleSubtitleLabel(label, 12);
			return;
		}
		LabelStyleUtil.styleValueLabel(label, 20);
		label.setForeground(DashboardPanelUtil.getPrimaryActionColor());
	}

	private void applyStatsStyle(JTextArea label) {
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		label.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
	}
}

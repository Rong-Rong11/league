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

import data.team.Team;
import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.PlayerDisplayUtil;
import gui.panel.common.RoundedPanel;
import gui.panel.common.ThemeAware;
import gui.panel.mapPanel.effectifPanel.teamPanel.TeamLogoPanel;
import process.orchestrator.GUIInterface;
import process.utility.TeamDisplayUtility;

public class RankingPerformancePanel extends JPanel implements ThemeAware {
	private static final int CARD_HEIGHT = 108;

	private GUIInterface guiInterface;
	private TeamLogoPanel leaderLogoPanel;
	private JLabel leaderTeamValue;
	private JLabel leaderDetailLabel;
	private JLabel leaderStatsLabel;
	private RoundedPanel leaderDetailBadge;
	private TeamLogoPanel streakLogoPanel;
	private JLabel streakTeamValue;
	private JLabel streakDetailLabel;
	private JLabel streakStatsLabel;
	private RoundedPanel streakDetailBadge;
	private TeamLogoPanel lastLogoPanel;
	private JLabel lastTeamValue;
	private JLabel lastDetailLabel;
	private JLabel lastStatsLabel;
	private RoundedPanel lastDetailBadge;

	public RankingPerformancePanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		setLayout(new GridLayout(3, 1, 0, 12));
		setOpaque(false);

		leaderLogoPanel = createLogoPanel();
		leaderTeamValue = new JLabel("-");
		leaderDetailLabel = createDetailLabel();
		leaderStatsLabel = createStatsLabel();
		leaderDetailBadge = createDetailBadge(leaderDetailLabel);
		streakLogoPanel = createLogoPanel();
		streakTeamValue = new JLabel("-");
		streakDetailLabel = createDetailLabel();
		streakStatsLabel = createStatsLabel();
		streakDetailBadge = createDetailBadge(streakDetailLabel);
		lastLogoPanel = createLogoPanel();
		lastTeamValue = new JLabel("-");
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
			streakTeamValue.setText("-");
			streakDetailLabel.setText("-");
			streakStatsLabel.setText("-");
		} else {
			streakLogoPanel.setTeamName(bestStreakTeam.getName());
			streakTeamValue.setText(TeamDisplayUtility.getShortName(bestStreakTeam));
			streakDetailLabel.setText(guiInterface.getTeamMaxWinStreak(bestStreakTeam) + " victoires");
			streakStatsLabel.setText(buildStatsText(bestStreakTeam));
		}
	}

	private void showEmptyState() {
		leaderLogoPanel.setTeamName("");
		leaderTeamValue.setText("-");
		leaderDetailLabel.setText("-");
		leaderStatsLabel.setText("-");
		streakLogoPanel.setTeamName("");
		streakTeamValue.setText("-");
		streakDetailLabel.setText("-");
		streakStatsLabel.setText("-");
		lastLogoPanel.setTeamName("");
		lastTeamValue.setText("-");
		lastDetailLabel.setText("-");
		lastStatsLabel.setText("-");
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

	private void updateTeamBlock(TeamLogoPanel logoPanel, JLabel teamValue, JLabel statsLabel, JLabel detailLabel,
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
		return PlayerDisplayUtil.formatOneDecimal(points) + " PTS  |  "
				+ PlayerDisplayUtil.formatOneDecimal(rebounds) + " REB  |  "
				+ PlayerDisplayUtil.formatOneDecimal(assists) + " AST";
	}

	private TeamLogoPanel createLogoPanel() {
		TeamLogoPanel logoPanel = new TeamLogoPanel("", 44);
		logoPanel.setTeamQueryInterface(guiInterface);
		return logoPanel;
	}

	private JLabel createDetailLabel() {
		JLabel label = new JLabel("-", JLabel.CENTER);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		label.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		return label;
	}

	private JLabel createStatsLabel() {
		JLabel label = new JLabel("-", JLabel.LEFT);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		label.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		return label;
	}

	private JPanel createPerformanceCard(String label, TeamLogoPanel logoPanel, JLabel teamValue, JLabel statsLabel,
			RoundedPanel detailBadge) {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BorderLayout(0, 8));
		card.setPreferredSize(new java.awt.Dimension(10, CARD_HEIGHT));
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, DashboardPanelUtil.BORDER_COLOR),
				BorderFactory.createEmptyBorder(10, 12, 10, 12)));

		JPanel topPanel = new JPanel(new BorderLayout(8, 0));
		topPanel.setOpaque(false);

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

		JLabel labelValue = new JLabel(label);
		labelValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		labelValue.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);

		teamValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		teamValue.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);

		textPanel.add(labelValue);
		textPanel.add(teamValue);
		textPanel.add(statsLabel);

		JPanel logoWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		logoWrapper.setOpaque(false);
		logoWrapper.setPreferredSize(new Dimension(88, 52));
		logoWrapper.add(logoPanel);

		JPanel fieldWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		fieldWrapper.setOpaque(false);
		fieldWrapper.add(detailBadge);

		topPanel.add(textPanel, BorderLayout.CENTER);
		topPanel.add(logoWrapper, BorderLayout.EAST);

		card.add(topPanel, BorderLayout.CENTER);
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
		leaderTeamValue.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		streakTeamValue.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		lastTeamValue.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		leaderStatsLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		streakStatsLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		lastStatsLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		applyDetailTheme(leaderDetailBadge, leaderDetailLabel);
		applyDetailTheme(streakDetailBadge, streakDetailLabel);
		applyDetailTheme(lastDetailBadge, lastDetailLabel);
		revalidate();
		repaint();
	}

	private void applyDetailTheme(RoundedPanel badge, JLabel label) {
		badge.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		label.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
	}
}

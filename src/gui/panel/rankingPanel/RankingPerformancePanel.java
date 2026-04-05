package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.Color;
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
import gui.panel.mapPanel.effectifPanel.teamPanel.TeamLogoPanel;
import process.orchestrator.GUIInterface;
import process.utility.TeamDisplayUtil;

public class RankingPerformancePanel extends JPanel {
	private static final Color TITLE_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color SUBTITLE_COLOR = new Color(110, 117, 131);
	private static final Color BORDER_COLOR = new Color(229, 232, 238);
	private static final int CARD_HEIGHT = 120;

	private GUIInterface guiInterface;
	private TeamLogoPanel leaderLogoPanel;
	private JLabel leaderTeamValue;
	private JLabel leaderDetailValue;
	private TeamLogoPanel streakLogoPanel;
	private JLabel streakTeamValue;
	private JLabel streakDetailValue;
	private TeamLogoPanel lastLogoPanel;
	private JLabel lastTeamValue;
	private JLabel lastDetailValue;

	public RankingPerformancePanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		setLayout(new GridLayout(3, 1, 0, 12));
		setOpaque(false);

		leaderLogoPanel = createLogoPanel();
		leaderTeamValue = new JLabel("-");
		leaderDetailValue = new JLabel("-");
		streakLogoPanel = createLogoPanel();
		streakTeamValue = new JLabel("-");
		streakDetailValue = new JLabel("-");
		lastLogoPanel = createLogoPanel();
		lastTeamValue = new JLabel("-");
		lastDetailValue = new JLabel("-");

		add(createPerformanceCard("Leader", leaderLogoPanel, leaderTeamValue, leaderDetailValue));
		add(createPerformanceCard("Meilleure serie", streakLogoPanel, streakTeamValue, streakDetailValue));
		add(createPerformanceCard("Derniere place", lastLogoPanel, lastTeamValue, lastDetailValue));
		refreshPerformance();
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

		updateTeamBlock(leaderLogoPanel, leaderTeamValue, leaderDetailValue, leader);
		updateTeamBlock(lastLogoPanel, lastTeamValue, lastDetailValue, last);

		if (bestStreakTeam == null) {
			streakLogoPanel.setTeamName("");
			streakTeamValue.setText("-");
			streakDetailValue.setText("-");
		} else {
			streakLogoPanel.setTeamName(bestStreakTeam.getName());
			streakTeamValue.setText(TeamDisplayUtil.getShortName(bestStreakTeam));
			streakDetailValue.setText(guiInterface.getTeamMaxWinStreak(bestStreakTeam) + " victoires");
		}
	}

	private void showEmptyState() {
		leaderLogoPanel.setTeamName("");
		leaderTeamValue.setText("-");
		leaderDetailValue.setText("-");
		streakLogoPanel.setTeamName("");
		streakTeamValue.setText("-");
		streakDetailValue.setText("-");
		lastLogoPanel.setTeamName("");
		lastTeamValue.setText("-");
		lastDetailValue.setText("-");
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

	private void updateTeamBlock(TeamLogoPanel logoPanel, JLabel teamValue, JLabel detailValue, Team team) {
		logoPanel.setTeamName(team.getName());
		teamValue.setText(TeamDisplayUtil.getShortName(team));
		detailValue.setText(guiInterface.getTeamNumberWin(team) + "-" + guiInterface.getTeamNumberLose(team));
	}

	private TeamLogoPanel createLogoPanel() {
		TeamLogoPanel logoPanel = new TeamLogoPanel("", 44);
		logoPanel.setTeamQueryInterface(guiInterface);
		return logoPanel;
	}

	private JPanel createPerformanceCard(String label, TeamLogoPanel logoPanel, JLabel teamValue, JLabel detailValue) {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BorderLayout(0, 10));
		card.setPreferredSize(new java.awt.Dimension(10, CARD_HEIGHT));
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
				BorderFactory.createEmptyBorder(10, 12, 10, 12)));

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

		JLabel labelValue = new JLabel(label);
		labelValue.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		labelValue.setForeground(SUBTITLE_COLOR);

		teamValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		teamValue.setForeground(TITLE_COLOR);

		detailValue.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		detailValue.setForeground(SUBTITLE_COLOR);

		textPanel.add(labelValue);
		textPanel.add(teamValue);
		textPanel.add(detailValue);

		JPanel logoWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		logoWrapper.setOpaque(false);
		logoWrapper.add(logoPanel);

		card.add(textPanel, BorderLayout.NORTH);
		card.add(logoWrapper, BorderLayout.CENTER);
		return card;
	}
}

package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.team.Team;
import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.mapPanel.effectifPanel.teamPanel.TeamLogoPanel;
import gui.panel.common.ThemeAware;
import process.orchestrator.GUIInterface;
import process.utility.TeamDisplayUtil;

public class RankingPerformancePanel extends JPanel implements ThemeAware {
	private static final int CARD_HEIGHT = 108;

	private GUIInterface guiInterface;
	private TeamLogoPanel leaderLogoPanel;
	private JLabel leaderTeamValue;
	private JTextField leaderDetailField;
	private TeamLogoPanel streakLogoPanel;
	private JLabel streakTeamValue;
	private JTextField streakDetailField;
	private TeamLogoPanel lastLogoPanel;
	private JLabel lastTeamValue;
	private JTextField lastDetailField;

	public RankingPerformancePanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		setLayout(new GridLayout(3, 1, 0, 12));
		setOpaque(false);

		leaderLogoPanel = createLogoPanel();
		leaderTeamValue = new JLabel("-");
		leaderDetailField = createValueField();
		streakLogoPanel = createLogoPanel();
		streakTeamValue = new JLabel("-");
		streakDetailField = createValueField();
		lastLogoPanel = createLogoPanel();
		lastTeamValue = new JLabel("-");
		lastDetailField = createValueField();

		add(createPerformanceCard("Leader", leaderLogoPanel, leaderTeamValue, leaderDetailField));
		add(createPerformanceCard("Meilleure serie", streakLogoPanel, streakTeamValue, streakDetailField));
		add(createPerformanceCard("Derniere place", lastLogoPanel, lastTeamValue, lastDetailField));
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

		updateTeamBlock(leaderLogoPanel, leaderTeamValue, leaderDetailField, leader);
		updateTeamBlock(lastLogoPanel, lastTeamValue, lastDetailField, last);

		if (bestStreakTeam == null) {
			streakLogoPanel.setTeamName("");
			streakTeamValue.setText("-");
			streakDetailField.setText("-");
		} else {
			streakLogoPanel.setTeamName(bestStreakTeam.getName());
			streakTeamValue.setText(TeamDisplayUtil.getShortName(bestStreakTeam));
			streakDetailField.setText(guiInterface.getTeamMaxWinStreak(bestStreakTeam) + " victoires");
		}
	}

	private void showEmptyState() {
		leaderLogoPanel.setTeamName("");
		leaderTeamValue.setText("-");
		leaderDetailField.setText("-");
		streakLogoPanel.setTeamName("");
		streakTeamValue.setText("-");
		streakDetailField.setText("-");
		lastLogoPanel.setTeamName("");
		lastTeamValue.setText("-");
		lastDetailField.setText("-");
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

	private void updateTeamBlock(TeamLogoPanel logoPanel, JLabel teamValue, JTextField detailField, Team team) {
		logoPanel.setTeamName(team.getName());
		teamValue.setText(TeamDisplayUtil.getShortName(team));
		detailField.setText("V " + guiInterface.getTeamNumberWin(team) + "  |  D " + guiInterface.getTeamNumberLose(team));
	}

	private TeamLogoPanel createLogoPanel() {
		TeamLogoPanel logoPanel = new TeamLogoPanel("", 44);
		logoPanel.setTeamQueryInterface(guiInterface);
		return logoPanel;
	}

	private JTextField createValueField() {
		JTextField field = new JTextField("-");
		field.setEditable(false);
		field.setHorizontalAlignment(JTextField.CENTER);
		field.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		field.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		field.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		field.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
		return field;
	}

	private JPanel createPerformanceCard(String label, TeamLogoPanel logoPanel, JLabel teamValue, JTextField detailField) {
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

		JPanel logoWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		logoWrapper.setOpaque(false);
		logoWrapper.setPreferredSize(new Dimension(88, 52));
		logoWrapper.add(logoPanel);

		JPanel fieldWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		fieldWrapper.setOpaque(false);
		detailField.setPreferredSize(new Dimension(120, 28));
		fieldWrapper.add(detailField);

		topPanel.add(textPanel, BorderLayout.CENTER);
		topPanel.add(logoWrapper, BorderLayout.EAST);

		card.add(topPanel, BorderLayout.CENTER);
		card.add(fieldWrapper, BorderLayout.SOUTH);
		return card;
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		leaderTeamValue.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		streakTeamValue.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		lastTeamValue.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		applyValueFieldTheme(leaderDetailField);
		applyValueFieldTheme(streakDetailField);
		applyValueFieldTheme(lastDetailField);
		revalidate();
		repaint();
	}

	private void applyValueFieldTheme(JTextField field) {
		field.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		field.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		field.setCaretColor(DashboardPanelUtil.TITLE_TEXT_COLOR);
	}
}

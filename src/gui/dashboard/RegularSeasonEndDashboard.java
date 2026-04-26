package gui.dashboard;

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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import data.team.Team;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.RoundedPanel;
import gui.panel.common.ThemeAware;
import gui.utility.TeamDisplayUtility;
import process.orchestrator.interf.GUIInterface;

public class RegularSeasonEndDashboard extends JPanel implements RefreshableDashboard, ThemeAware {
	private static final Color EAST_ACCENT = new Color(0xA6, 0x4D, 0x5A);

	private GUIInterface guiInterface;
	private JButton reviewRankingButton;
	private JButton startPlayoffsButton;

	public RegularSeasonEndDashboard(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		create();
		organize();
	}

	private void create() {
		reviewRankingButton = new RoundedButton("Revoir le classement");
		startPlayoffsButton = new RoundedButton("Passer aux playoffs  >");
		applyButtonStyle();
	}

	private void organize() {
		setLayout(new BorderLayout());
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		setBorder(BorderFactory.createEmptyBorder(40, 32, 40, 32));
		applyButtonStyle();

		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(buildHeader());
		content.add(Box.createVerticalStrut(28));
		content.add(buildStatsPanel());
		content.add(Box.createVerticalStrut(28));
		content.add(buildConferencePanels());
		content.add(Box.createVerticalStrut(28));
		content.add(buildButtonsPanel());
		add(content, BorderLayout.CENTER);
	}

	private JPanel buildHeader() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel iconLabel = new JLabel("T", SwingConstants.CENTER);
		iconLabel.setOpaque(true);
		iconLabel.setBackground(DashboardPanelUtil.getPrimaryActionColor());
		iconLabel.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
		iconLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 34));
		iconLabel.setPreferredSize(new Dimension(80, 80));
		iconLabel.setMaximumSize(new Dimension(80, 80));
		iconLabel.setAlignmentX(CENTER_ALIGNMENT);

		JLabel titleLabel = new JLabel("Saison reguliere terminee", SwingConstants.CENTER);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
		titleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);

		JLabel subtitleLabel = new JLabel(
				"Les classements sont finalises. Vous pouvez maintenant preparer les playoffs.",
				SwingConstants.CENTER);
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
		subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);

		panel.add(iconLabel);
		panel.add(Box.createVerticalStrut(22));
		panel.add(titleLabel);
		panel.add(Box.createVerticalStrut(10));
		panel.add(subtitleLabel);
		return panel;
	}

	private JPanel buildStatsPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 4, 18, 0));
		panel.setOpaque(false);
		ArrayList<Team> globalRanking = guiInterface.getGlobalRanking();
		Team bestTeam = globalRanking.isEmpty() ? null : globalRanking.get(0);
		Team worstTeam = globalRanking.isEmpty() ? null : globalRanking.get(globalRanking.size() - 1);
		Team bestScoreTeam = getBestScoreTeam(globalRanking);

		panel.add(buildStatCard("Matchs joues", String.valueOf(countPlayedGames()), ""));
		panel.add(buildStatCard("Meilleure equipe", TeamDisplayUtility.getShortName(bestTeam), buildRecordText(bestTeam)));
		panel.add(buildStatCard("Pire equipe", TeamDisplayUtility.getShortName(worstTeam), buildRecordText(worstTeam)));
		panel.add(buildStatCard("Meilleur score moyen", TeamDisplayUtility.getShortName(bestScoreTeam),
				formatOneDecimal(guiInterface.getAveragePoints(bestScoreTeam, true)) + " pts/match"));
		return panel;
	}

	private JPanel buildStatCard(String title, String value, String subtitle) {
		RoundedPanel card = new RoundedPanel(new BorderLayout(), 18);
		card.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(DashboardPanelUtil.BORDER_COLOR, 1),
				BorderFactory.createEmptyBorder(22, 22, 22, 22)));
		card.setPreferredSize(new Dimension(260, 120));

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
		titleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		JLabel valueLabel = new JLabel(value);
		valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
		valueLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		JLabel subtitleLabel = new JLabel(subtitle);
		subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		textPanel.add(titleLabel);
		textPanel.add(Box.createVerticalStrut(12));
		textPanel.add(valueLabel);
		textPanel.add(Box.createVerticalStrut(4));
		textPanel.add(subtitleLabel);
		card.add(textPanel, BorderLayout.CENTER);
		return card;
	}

	private JPanel buildConferencePanels() {
		JPanel panel = new JPanel(new GridLayout(1, 2, 24, 0));
		panel.setOpaque(false);
		panel.add(buildConferenceCard("Eastern Conference", guiInterface.getEastRanking(), EAST_ACCENT));
		panel.add(buildConferenceCard("Western Conference", guiInterface.getWestRanking(), DashboardPanelUtil.getPrimaryActionColor()));
		return panel;
	}

	private JPanel buildConferenceCard(String title, ArrayList<Team> ranking, Color accentColor) {
		RoundedPanel card = new RoundedPanel(new BorderLayout(), 20);
		card.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(DashboardPanelUtil.BORDER_COLOR, 1),
				BorderFactory.createEmptyBorder(22, 26, 22, 26)));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		titleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		card.add(titleLabel, BorderLayout.NORTH);

		JPanel teamsPanel = new JPanel(new GridLayout(4, 2, 12, 12));
		teamsPanel.setOpaque(false);
		teamsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		for (int i = 0; i < 8; i++) {
			Team team = i < ranking.size() ? ranking.get(i) : null;
			teamsPanel.add(buildTeamLine(i + 1, team, accentColor));
		}
		card.add(teamsPanel, BorderLayout.CENTER);
		return card;
	}

	private JPanel buildTeamLine(int rank, Team team, Color accentColor) {
		RoundedPanel line = new RoundedPanel(new BorderLayout(12, 0), 10);
		line.setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		line.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(DashboardPanelUtil.BORDER_COLOR, 1),
				BorderFactory.createEmptyBorder(10, 14, 10, 14)));

		JLabel rankLabel = new JLabel(String.valueOf(rank), SwingConstants.CENTER);
		rankLabel.setOpaque(true);
		rankLabel.setBackground(accentColor);
		rankLabel.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
		rankLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		rankLabel.setPreferredSize(new Dimension(34, 34));

		JLabel teamLabel = new JLabel(TeamDisplayUtility.getShortName(team));
		teamLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		teamLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);

		JLabel recordLabel = new JLabel(buildRecordText(team));
		recordLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		recordLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);

		line.add(rankLabel, BorderLayout.WEST);
		line.add(teamLabel, BorderLayout.CENTER);
		line.add(recordLabel, BorderLayout.EAST);
		return line;
	}

	private JPanel buildButtonsPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
		panel.setOpaque(false);
		panel.add(reviewRankingButton);
		panel.add(startPlayoffsButton);
		return panel;
	}

	private void styleButton(JButton button, Color background, Color foreground) {
		button.setPreferredSize(new Dimension(270, 56));
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		button.setBackground(background);
		button.setForeground(foreground);
		button.setFocusPainted(false);
	}

	private void applyButtonStyle() {
		styleButton(reviewRankingButton, DashboardPanelUtil.BUTTON_SURFACE_COLOR, DashboardPanelUtil.BUTTON_TEXT_COLOR);
		styleButton(startPlayoffsButton, DashboardPanelUtil.getPrimaryActionColor(),
				DashboardPanelUtil.getPrimaryActionTextColor());
	}

	private int countPlayedGames() {
		int playedGames = 0;
		ArrayList<Team> teams = guiInterface.getTeams();
		for (int i = 0; i < teams.size(); i++) {
			playedGames += guiInterface.getTeamNumberPlayedGames(teams.get(i));
		}
		return playedGames / 2;
	}

	private Team getBestScoreTeam(ArrayList<Team> teams) {
		Team bestTeam = null;
		double bestScore = -1;
		for (int i = 0; i < teams.size(); i++) {
			Team team = teams.get(i);
			double score = guiInterface.getAveragePoints(team, true);
			if (score > bestScore) {
				bestScore = score;
				bestTeam = team;
			}
		}
		return bestTeam;
	}

	private String buildRecordText(Team team) {
		if (team == null) {
			return "-";
		}
		int wins = guiInterface.getTeamNumberWin(team);
		int losses = guiInterface.getTeamNumberLose(team);
		int games = wins + losses;
		if (games == 0) {
			return wins + "-" + losses;
		}
		double pct = wins * 100.0 / games;
		return wins + "-" + losses + " (" + formatOneDecimal(pct) + "%)";
	}

	private String formatOneDecimal(double value) {
		return String.format(java.util.Locale.US, "%.1f", value);
	}

	public JButton getReviewRankingButton() {
		return reviewRankingButton;
	}

	public JButton getStartPlayoffsButton() {
		return startPlayoffsButton;
	}

	@Override
	public void refresh() {
		removeAll();
		organize();
		revalidate();
		repaint();
	}

	@Override
	public void applyTheme() {
		refresh();
	}
}

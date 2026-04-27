package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.RoundedButton;
import gui.utility.TeamDisplayUtility;
import process.orchestrator.interfaces.GUIInterface;

public final class RankingTableFactory {
	private RankingTableFactory() {
	}

	public static JPanel buildPageBar(int pageIndex, int pageCount, ActionListener previousAction,
			ActionListener nextAction) {
		JPanel pageBar = new JPanel(new BorderLayout());
		pageBar.setOpaque(false);
		pageBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		JButton previousPageButton = createPageButton("<");
		JButton nextPageButton = createPageButton(">");
		JLabel pageLabel = new JLabel("Page " + (pageIndex + 1) + " / " + pageCount, JLabel.CENTER);
		LabelStyleUtil.styleValueLabel(pageLabel, 12);
		pageLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);

		previousPageButton.addActionListener(previousAction);
		nextPageButton.addActionListener(nextAction);
		previousPageButton.setEnabled(pageIndex > 0);
		nextPageButton.setEnabled(pageIndex < pageCount - 1);
		stylePageButton(previousPageButton);
		stylePageButton(nextPageButton);

		pageBar.add(previousPageButton, BorderLayout.WEST);
		pageBar.add(pageLabel, BorderLayout.CENTER);
		pageBar.add(nextPageButton, BorderLayout.EAST);
		return pageBar;
	}

	private static JButton createPageButton(String text) {
		JButton button = new RoundedButton(text);
		ButtonStyleUtil.styleActionButton(button, 44, 32, 14);
		return button;
	}

	private static void stylePageButton(JButton button) {
		if (button.isEnabled()) {
			button.setBackground(DashboardPanelUtil.getNavigationButtonColor());
			button.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
			return;
		}
		button.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		button.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
	}

	public static JPanel buildSingleTable(ArrayList<Team> teams, int startIndex, int endIndex, GUIInterface guiInterface) {
		JPanel table = new JPanel(new BorderLayout(0, 0));
		table.setOpaque(false);
		table.add(buildHeaderRow(), BorderLayout.NORTH);
		table.add(buildRankingColumn(teams, startIndex, endIndex, guiInterface), BorderLayout.CENTER);
		return table;
	}

	public static JPanel buildEmptyStatePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		JLabel messageLabel = new JLabel("Aucun classement n'est disponible pour le moment.", JLabel.CENTER);
		LabelStyleUtil.styleSubtitleLabel(messageLabel, 13);
		panel.add(messageLabel, BorderLayout.CENTER);
		return panel;
	}

	private static JPanel buildHeaderRow() {
		JPanel header = new JPanel(new GridLayout(1, 7, 12, 0));
		header.setOpaque(true);
		header.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		header.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 1, 0, DashboardPanelUtil.BORDER_COLOR),
				BorderFactory.createEmptyBorder(8, 12, 8, 12)));

		header.add(createHeaderLabel("RANG"));
		header.add(createHeaderLabel("EQUIPE"));
		header.add(createHeaderLabel("V"));
		header.add(createHeaderLabel("D"));
		header.add(createHeaderLabel("POINTS"));
		header.add(createHeaderLabel("% VICT"));
		header.add(createHeaderLabel("MEILLEURE SERIE"));
		return header;
	}

	private static JLabel createHeaderLabel(String text) {
		JLabel label = new JLabel(text);
		LabelStyleUtil.styleSubtitleLabel(label, 11);
		return label;
	}

	private static JPanel buildRankingColumn(ArrayList<Team> teams, int startIndex, int endIndex, GUIInterface guiInterface) {
		JPanel column = new JPanel();
		column.setOpaque(false);
		column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));

		for (int index = startIndex; index < endIndex && index < teams.size(); index++) {
			column.add(createTeamRow(index + 1, teams.get(index), guiInterface));
		}

		return column;
	}

	private static JPanel createTeamRow(int rank, Team team, GUIInterface guiInterface) {
		int wins = guiInterface.getTeamNumberWin(team);
		int losses = guiInterface.getTeamNumberLose(team);
		int points = wins * 2;
		String percentage = buildPercentage(team, guiInterface);
		String bestWinStreak = guiInterface.getTeamMaxWinStreak(team) + " W";
		return createRow(rank, TeamDisplayUtility.getShortName(team), wins, losses, points, percentage, bestWinStreak);
	}

	private static String buildPercentage(Team team, GUIInterface guiInterface) {
		int playedGames = guiInterface.getTeamNumberPlayedGames(team);
		if (playedGames == 0) {
			return "0.0%";
		}
		double winRate = (guiInterface.getTeamNumberWin(team) * 100.0) / playedGames;
		return String.format("%.1f%%", winRate);
	}

	private static JPanel createRow(int rank, String teamName, int wins, int losses, int points, String percentage,
			String bestWinStreak) {
		JPanel row = new JPanel(new GridLayout(1, 7, 12, 0));
		row.setOpaque(true);
		row.setBackground(getRowBackground(rank));
		row.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, DashboardPanelUtil.BORDER_COLOR),
				BorderFactory.createEmptyBorder(8, 12, 8, 12)));

		row.add(createColoredValueLabel(String.valueOf(rank), true, getRankColor(rank)));
		row.add(createColoredValueLabel(teamName, true, DashboardPanelUtil.TITLE_TEXT_COLOR));
		row.add(createColoredValueLabel(String.valueOf(wins), true, DashboardPanelUtil.POSITIVE_VALUE_COLOR));
		row.add(createColoredValueLabel(String.valueOf(losses), false, DashboardPanelUtil.EXPENSE_COLOR));
		row.add(createColoredValueLabel(String.valueOf(points), true, DashboardPanelUtil.NEUTRAL_ACCENT_COLOR));
		row.add(createColoredValueLabel(percentage, true, DashboardPanelUtil.REVENUE_COLOR));
		row.add(createColoredValueLabel(bestWinStreak, true, DashboardPanelUtil.POSITIVE_VALUE_COLOR));

		return row;
	}

	private static JLabel createColoredValueLabel(String text, boolean accented, Color color) {
		JLabel label = new JLabel(text);
		label.setForeground(color);
		label.setFont(new Font(Font.SANS_SERIF, accented ? Font.BOLD : Font.PLAIN, 12));
		return label;
	}

	private static Color getRankColor(int rank) {
		if (rank == 1) {
			return DashboardPanelUtil.NEUTRAL_ACCENT_COLOR;
		}
		if (rank <= 3) {
			return DashboardPanelUtil.POLICY_BALANCED_COLOR;
		}
		if (rank <= 8) {
			return DashboardPanelUtil.REVENUE_COLOR;
		}
		if (rank <= 15) {
			return DashboardPanelUtil.TITLE_TEXT_COLOR;
		}
		return DashboardPanelUtil.SUBTITLE_TEXT_COLOR;
	}

	private static Color getRowBackground(int rank) {
		if (DashboardPanelUtil.isDarkMode()) {
			if (rank % 2 == 0) {
				return DashboardPanelUtil.PANEL_SURFACE_COLOR;
			}
			return new Color(39, 43, 50);
		}
		if (rank % 2 == 0) {
			return DashboardPanelUtil.PANEL_SURFACE_COLOR;
		}
		return new Color(250, 251, 253);
	}
}

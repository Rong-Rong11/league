package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.team.Team;
import process.orchestrator.GUIInterface;
import process.utility.TeamDisplayUtil;

public class RankingTablePanel extends JPanel {
	private static final Color HEADER_BACKGROUND = new Color(245, 247, 250);
	private static final Color HEADER_TEXT_COLOR = new Color(110, 117, 131);
	private static final Color PRIMARY_TEXT_COLOR = new Color(0x17, 0x31, 0x74);
	private static final Color PRIMARY_ACCENT = new Color(0x37, 0x84, 0xB3);
	private static final Color MUTED_TEXT_COLOR = new Color(90, 90, 90);
	private static final Color BORDER_COLOR = new Color(229, 232, 238);
	private static final String GLOBAL_MODE = "global";
	private static final String EAST_MODE = "east";
	private static final String WEST_MODE = "west";

	private GUIInterface guiInterface;
	private JPanel tableContentPanel;
	private JButton globalButton;
	private JButton eastButton;
	private JButton westButton;
	private JButton regularSeasonButton;
	private JButton playoffsButton;
	private String selectedMode;

	public RankingTablePanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		selectedMode = GLOBAL_MODE;
		setLayout(new BorderLayout(0, 12));
		setOpaque(false);

		add(buildTopBar(), BorderLayout.NORTH);
		add(buildTableContent(), BorderLayout.CENTER);
		refreshRanking();
	}

	private JPanel buildTopBar() {
		JPanel topBar = new JPanel(new BorderLayout());
		topBar.setOpaque(false);

		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		leftPanel.setOpaque(false);
		globalButton = createFilterButton("Global", true);
		eastButton = createFilterButton("Est", false);
		westButton = createFilterButton("Ouest", false);
		globalButton.addActionListener(e -> setSelectedMode(GLOBAL_MODE));
		eastButton.addActionListener(e -> setSelectedMode(EAST_MODE));
		westButton.addActionListener(e -> setSelectedMode(WEST_MODE));
		leftPanel.add(globalButton);
		leftPanel.add(eastButton);
		leftPanel.add(westButton);

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		rightPanel.setOpaque(false);
		regularSeasonButton = createFilterButton("Saison reguliere", true);
		playoffsButton = createFilterButton("Playoffs", false);
		rightPanel.add(regularSeasonButton);
		rightPanel.add(playoffsButton);

		topBar.add(leftPanel, BorderLayout.WEST);
		topBar.add(rightPanel, BorderLayout.EAST);
		return topBar;
	}

	private JButton createFilterButton(String text, boolean selected) {
		JButton button = new JButton(text);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setOpaque(true);
		button.setFont(new Font(Font.SANS_SERIF, selected ? Font.BOLD : Font.PLAIN, 12));
		button.setBackground(selected ? PRIMARY_ACCENT : HEADER_BACKGROUND);
		button.setForeground(selected ? Color.WHITE : MUTED_TEXT_COLOR);
		return button;
	}

	private JPanel buildTableContent() {
		tableContentPanel = new JPanel(new BorderLayout(0, 0));
		tableContentPanel.setOpaque(false);
		return tableContentPanel;
	}

	private JPanel buildHeaderRow() {
		JPanel header = new JPanel(new GridLayout(1, 7, 12, 0));
		header.setOpaque(true);
		header.setBackground(HEADER_BACKGROUND);
		header.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 1, 0, BORDER_COLOR),
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

	private JLabel createHeaderLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(HEADER_TEXT_COLOR);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		return label;
	}

	private JPanel buildRowsPanel() {
		JPanel rowsPanel = new JPanel();
		rowsPanel.setOpaque(false);
		rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));
		return rowsPanel;
	}

	public void refreshRanking() {
		tableContentPanel.removeAll();

		ArrayList<Team> teams = getSelectedTeams();
		if (GLOBAL_MODE.equals(selectedMode)) {
			tableContentPanel.add(buildGlobalTable(teams), BorderLayout.CENTER);
		} else {
			tableContentPanel.add(buildSingleTable(teams), BorderLayout.CENTER);
		}

		revalidate();
		repaint();
	}

	private JPanel buildSingleTable(ArrayList<Team> teams) {
		JPanel table = new JPanel(new BorderLayout(0, 0));
		table.setOpaque(false);
		table.add(buildHeaderRow(), BorderLayout.NORTH);
		table.add(buildRankingColumn(teams, 0, teams.size()), BorderLayout.CENTER);
		return table;
	}

	private JPanel buildGlobalTable(ArrayList<Team> teams) {
		JPanel globalTable = new JPanel(new GridLayout(1, 2, 16, 0));
		globalTable.setOpaque(false);
		globalTable.add(buildTableColumn(teams, 0, 15));
		globalTable.add(buildTableColumn(teams, 15, teams.size()));
		return globalTable;
	}

	private JPanel buildTableColumn(ArrayList<Team> teams, int startIndex, int endIndex) {
		JPanel column = new JPanel(new BorderLayout(0, 0));
		column.setOpaque(false);
		column.add(buildHeaderRow(), BorderLayout.NORTH);
		column.add(buildRankingColumn(teams, startIndex, endIndex), BorderLayout.CENTER);
		return column;
	}

	private JPanel buildRankingColumn(ArrayList<Team> teams, int startIndex, int endIndex) {
		JPanel column = new JPanel();
		column.setOpaque(false);
		column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));

		for (int index = startIndex; index < endIndex && index < teams.size(); index++) {
			column.add(createTeamRow(index + 1, teams.get(index)));
		}

		return column;
	}

	private ArrayList<Team> getSelectedTeams() {
		if (EAST_MODE.equals(selectedMode)) {
			return guiInterface.getEastRanking();
		}
		if (WEST_MODE.equals(selectedMode)) {
			return guiInterface.getWestRanking();
		}
		return guiInterface.getGlobalRanking();
	}

	private void setSelectedMode(String mode) {
		selectedMode = mode;
		updateModeButtons();
		refreshRanking();
	}

	private void updateModeButtons() {
		styleFilterButton(globalButton, GLOBAL_MODE.equals(selectedMode));
		styleFilterButton(eastButton, EAST_MODE.equals(selectedMode));
		styleFilterButton(westButton, WEST_MODE.equals(selectedMode));
	}

	private void styleFilterButton(JButton button, boolean selected) {
		button.setFont(new Font(Font.SANS_SERIF, selected ? Font.BOLD : Font.PLAIN, 12));
		button.setBackground(selected ? PRIMARY_ACCENT : HEADER_BACKGROUND);
		button.setForeground(selected ? Color.WHITE : MUTED_TEXT_COLOR);
	}

	private JPanel createTeamRow(int rank, Team team) {
		int wins = guiInterface.getTeamNumberWin(team);
		int losses = guiInterface.getTeamNumberLose(team);
		int points = wins * 2;
		String percentage = buildPercentage(team);
		String bestWinStreak = guiInterface.getTeamMaxWinStreak(team) + " W";
		return createRow(rank, TeamDisplayUtil.getShortName(team), wins, losses, points, percentage, bestWinStreak);
	}

	private JPanel createRow(int rank, String teamName, int wins, int losses, int points, String percentage,
			String bestWinStreak) {
		JPanel row = new JPanel(new GridLayout(1, 7, 12, 0));
		row.setOpaque(true);
		row.setBackground(Color.WHITE);
		row.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
				BorderFactory.createEmptyBorder(8, 12, 8, 12)));

		row.add(createValueLabel(String.valueOf(rank), true));
		row.add(createValueLabel(teamName, true));
		row.add(createValueLabel(String.valueOf(wins), false));
		row.add(createValueLabel(String.valueOf(losses), false));
		row.add(createValueLabel(String.valueOf(points), true));
		row.add(createValueLabel(percentage, true));
		row.add(createValueLabel(bestWinStreak, true));

		return row;
	}

	private String buildPercentage(Team team) {
		int playedGames = guiInterface.getTeamNumberPlayedGames(team);
		if (playedGames == 0) {
			return "0.0%";
		}
		double winRate = (guiInterface.getTeamNumberWin(team) * 100.0) / playedGames;
		return String.format("%.1f%%", winRate);
	}

	private JLabel createValueLabel(String text, boolean accented) {
		JLabel label = new JLabel(text);
		label.setForeground(accented ? PRIMARY_TEXT_COLOR : MUTED_TEXT_COLOR);
		label.setFont(new Font(Font.SANS_SERIF, accented ? Font.BOLD : Font.PLAIN, 12));
		return label;
	}
}

package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.ThemeAware;
import process.orchestrator.GUIInterface;
import process.utility.TeamDisplayUtil;

public class RankingTablePanel extends JPanel implements ThemeAware {
	private static final Color PRIMARY_ACCENT = new Color(0x17, 0x31, 0x74);
	private static final int GLOBAL_PAGE_SIZE = 15;
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
	private JButton previousPageButton;
	private JButton nextPageButton;
	private JLabel pageLabel;
	private String selectedMode;
	private int globalPageIndex;

	public RankingTablePanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		selectedMode = GLOBAL_MODE;
		globalPageIndex = 0;
		setLayout(new BorderLayout(0, 12));
		setOpaque(false);

		add(buildTopBar(), BorderLayout.NORTH);
		add(buildTableContent(), BorderLayout.CENTER);
		refreshRanking();
		applyTheme();
	}

	private JPanel buildTopBar() {
		JPanel topBar = new JPanel(new BorderLayout());
		topBar.setOpaque(false);

		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		leftPanel.setOpaque(false);
		globalButton = createFilterButton("Global", true);
		eastButton = createFilterButton("Est", false);
		westButton = createFilterButton("Ouest", false);
		globalButton.addActionListener(new ModeAction(GLOBAL_MODE));
		eastButton.addActionListener(new ModeAction(EAST_MODE));
		westButton.addActionListener(new ModeAction(WEST_MODE));
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
		JButton button = new RoundedButton(text);
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		applyFilterButtonTheme(button, selected);
		return button;
	}

	private JPanel buildTableContent() {
		tableContentPanel = new JPanel(new BorderLayout(0, 0));
		tableContentPanel.setOpaque(false);
		return tableContentPanel;
	}

	private JPanel buildPageBar(int pageCount) {
		JPanel pageBar = new JPanel(new BorderLayout());
		pageBar.setOpaque(false);
		pageBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		previousPageButton = createPageButton("<");
		nextPageButton = createPageButton(">");
		pageLabel = new JLabel(buildPageText(pageCount), JLabel.CENTER);
		pageLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		pageLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);

		previousPageButton.addActionListener(new PreviousPageAction(pageCount));
		nextPageButton.addActionListener(new NextPageAction(pageCount));

		pageBar.add(previousPageButton, BorderLayout.WEST);
		pageBar.add(pageLabel, BorderLayout.CENTER);
		pageBar.add(nextPageButton, BorderLayout.EAST);
		updatePageButtons(pageCount);
		return pageBar;
	}

	private JButton createPageButton(String text) {
		JButton button = new RoundedButton(text);
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		button.setPreferredSize(new Dimension(44, 32));
		button.setBackground(PRIMARY_ACCENT);
		button.setForeground(Color.WHITE);
		return button;
	}

	private String buildPageText(int pageCount) {
		return "Page " + (globalPageIndex + 1) + " / " + pageCount;
	}

	private void updatePageButtons(int pageCount) {
		if (pageLabel != null) {
			pageLabel.setText(buildPageText(pageCount));
			pageLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
		}
		if (previousPageButton != null) {
			previousPageButton.setEnabled(globalPageIndex > 0);
			stylePageButton(previousPageButton);
		}
		if (nextPageButton != null) {
			nextPageButton.setEnabled(globalPageIndex < pageCount - 1);
			stylePageButton(nextPageButton);
		}
	}

	private void stylePageButton(JButton button) {
		button.setBackground(PRIMARY_ACCENT);
		button.setForeground(Color.WHITE);
	}

	private JPanel buildHeaderRow() {
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

	private JLabel createHeaderLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
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
			int pageCount = Math.max(1, (int) Math.ceil((double) teams.size() / GLOBAL_PAGE_SIZE));
			if (globalPageIndex >= pageCount) {
				globalPageIndex = pageCount - 1;
			}
			tableContentPanel.add(buildPageBar(pageCount), BorderLayout.NORTH);
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
		int startIndex = globalPageIndex * GLOBAL_PAGE_SIZE;
		int endIndex = Math.min(startIndex + GLOBAL_PAGE_SIZE, teams.size());
		return buildSinglePageTable(teams, startIndex, endIndex);
	}

	private JPanel buildSinglePageTable(ArrayList<Team> teams, int startIndex, int endIndex) {
		JPanel table = new JPanel(new BorderLayout(0, 0));
		table.setOpaque(false);
		table.add(buildHeaderRow(), BorderLayout.NORTH);
		table.add(buildRankingColumn(teams, startIndex, endIndex), BorderLayout.CENTER);
		return table;
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
		if (GLOBAL_MODE.equals(mode)) {
			globalPageIndex = 0;
		}
		updateModeButtons();
		refreshRanking();
	}

	private void updateModeButtons() {
		styleFilterButton(globalButton, GLOBAL_MODE.equals(selectedMode));
		styleFilterButton(eastButton, EAST_MODE.equals(selectedMode));
		styleFilterButton(westButton, WEST_MODE.equals(selectedMode));
	}

	private void styleFilterButton(JButton button, boolean selected) {
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		applyFilterButtonTheme(button, selected);
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
		row.setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		row.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, DashboardPanelUtil.BORDER_COLOR),
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
		label.setForeground(accented ? DashboardPanelUtil.TITLE_TEXT_COLOR : DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
		label.setFont(new Font(Font.SANS_SERIF, accented ? Font.BOLD : Font.PLAIN, 12));
		return label;
	}

	private void applyFilterButtonTheme(JButton button, boolean selected) {
		button.setBackground(selected ? PRIMARY_ACCENT : DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		button.setForeground(selected ? Color.WHITE : DashboardPanelUtil.BUTTON_TEXT_COLOR);
	}

	private class ModeAction implements ActionListener {
		private final String mode;

		private ModeAction(String mode) {
			this.mode = mode;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setSelectedMode(mode);
		}
	}

	private class PreviousPageAction implements ActionListener {
		private final int pageCount;

		private PreviousPageAction(int pageCount) {
			this.pageCount = pageCount;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (globalPageIndex > 0) {
				globalPageIndex--;
				updatePageButtons(pageCount);
				refreshRanking();
			}
		}
	}

	private class NextPageAction implements ActionListener {
		private final int pageCount;

		private NextPageAction(int pageCount) {
			this.pageCount = pageCount;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (globalPageIndex < pageCount - 1) {
				globalPageIndex++;
				updatePageButtons(pageCount);
				refreshRanking();
			}
		}
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		updateModeButtons();
		if (previousPageButton != null) {
			stylePageButton(previousPageButton);
		}
		if (nextPageButton != null) {
			stylePageButton(nextPageButton);
		}
		refreshRanking();
	}
}

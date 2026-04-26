package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.Color;
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
import gui.panel.common.ButtonStyleUtil;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.RoundedButton;
import gui.panel.common.ThemeAware;
import gui.utility.TeamDisplayUtility;
import process.orchestrator.interf.GUIInterface;

public class RankingTablePanel extends JPanel implements ThemeAware {
	private static final int GLOBAL_PAGE_SIZE = 15;
	private static final String GLOBAL_MODE = "global";
	private static final String EAST_MODE = "east";
	private static final String WEST_MODE = "west";
	private static final String REGULAR_SEASON = "regular";
	private static final String PLAYOFFS = "playoffs";

	private GUIInterface guiInterface;
	private JPanel tableContentPanel;
	private JPanel modeFilterPanel;
	private JButton globalButton;
	private JButton eastButton;
	private JButton westButton;
	private JButton simulatePlayoffRoundButton;
	private JButton regularSeasonButton;
	private JButton playoffsButton;
	private JButton previousPageButton;
	private JButton nextPageButton;
	private JLabel pageLabel;
	private final RankingPlayoffsViewPanel playoffsViewPanel;
	private Runnable seasonEndAction;
	private String selectedMode;
	private String selectedSeason;
	private int globalPageIndex;

	public RankingTablePanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		selectedMode = GLOBAL_MODE;
		selectedSeason = REGULAR_SEASON;
		globalPageIndex = 0;
		playoffsViewPanel = new RankingPlayoffsViewPanel(guiInterface);
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

		modeFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		modeFilterPanel.setOpaque(false);
		globalButton = createFilterButton("Global", true);
		eastButton = createFilterButton("Est", false);
		westButton = createFilterButton("Ouest", false);
		simulatePlayoffRoundButton = new RoundedButton("Simuler le tour");
		globalButton.addActionListener(new ModeAction(GLOBAL_MODE));
		eastButton.addActionListener(new ModeAction(EAST_MODE));
		westButton.addActionListener(new ModeAction(WEST_MODE));
		simulatePlayoffRoundButton.addActionListener(new SimulatePlayoffRoundAction());
		modeFilterPanel.add(globalButton);
		modeFilterPanel.add(eastButton);
		modeFilterPanel.add(westButton);
		modeFilterPanel.add(simulatePlayoffRoundButton);

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		rightPanel.setOpaque(false);
		regularSeasonButton = createFilterButton("Saison reguliere", true);
		playoffsButton = createFilterButton("Playoffs", false);
		regularSeasonButton.addActionListener(new SeasonAction(REGULAR_SEASON));
		playoffsButton.addActionListener(new SeasonAction(PLAYOFFS));
		rightPanel.add(regularSeasonButton);
		rightPanel.add(playoffsButton);

		topBar.add(modeFilterPanel, BorderLayout.WEST);
		topBar.add(rightPanel, BorderLayout.EAST);
		return topBar;
	}

	private JButton createFilterButton(String text, boolean selected) {
		JButton button = new RoundedButton(text);
		ButtonStyleUtil.styleToggleButton(button);
		ButtonStyleUtil.setToggleButtonSelected(button, selected);
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
		LabelStyleUtil.styleValueLabel(pageLabel, 12);

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
		ButtonStyleUtil.styleActionButton(button, 44, 32, 14);
		stylePageButton(button);
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
		if (button.isEnabled()) {
			button.setBackground(DashboardPanelUtil.getNavigationButtonColor());
			button.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
			return;
		}
		button.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
		button.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
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
		LabelStyleUtil.styleSubtitleLabel(label, 11);
		return label;
	}

	public void refreshRanking() {
		tableContentPanel.removeAll();

		if (PLAYOFFS.equals(selectedSeason)) {
			playoffsViewPanel.refreshPlayoffs();
			tableContentPanel.add(playoffsViewPanel, BorderLayout.CENTER);
			revalidate();
			repaint();
			return;
		}

		ArrayList<Team> teams = getSelectedTeams();
		if (teams.isEmpty()) {
			tableContentPanel.add(buildEmptyStatePanel(), BorderLayout.CENTER);
			revalidate();
			repaint();
			return;
		}
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

	private JPanel buildEmptyStatePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		JLabel messageLabel = new JLabel("Aucun classement n'est disponible pour le moment.", JLabel.CENTER);
		LabelStyleUtil.styleSubtitleLabel(messageLabel, 13);
		panel.add(messageLabel, BorderLayout.CENTER);
		return panel;
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

	private void setSelectedSeason(String season) {
		selectedSeason = season;
		updateSeasonButtons();
		refreshRanking();
	}

	public void showPlayoffs() {
		setSelectedSeason(PLAYOFFS);
	}

	public void setSeasonEndAction(Runnable seasonEndAction) {
		this.seasonEndAction = seasonEndAction;
	}

	private void updateModeButtons() {
		styleFilterButton(globalButton, GLOBAL_MODE.equals(selectedMode));
		styleFilterButton(eastButton, EAST_MODE.equals(selectedMode));
		styleFilterButton(westButton, WEST_MODE.equals(selectedMode));
	}

	private void updateSeasonButtons() {
		styleFilterButton(regularSeasonButton, REGULAR_SEASON.equals(selectedSeason));
		styleFilterButton(playoffsButton, PLAYOFFS.equals(selectedSeason));
		if (modeFilterPanel != null) {
			boolean playoffsSelected = PLAYOFFS.equals(selectedSeason);
			globalButton.setVisible(!playoffsSelected);
			eastButton.setVisible(!playoffsSelected);
			westButton.setVisible(!playoffsSelected);
			simulatePlayoffRoundButton.setVisible(playoffsSelected);
			updateModeButtons();
			updatePlayoffRoundButton();
		}
	}

	private void updatePlayoffRoundButton() {
		ButtonStyleUtil.styleActionButton(simulatePlayoffRoundButton, 190, 44, 15);
		boolean enabled = guiInterface.hasPlayoffsStarted() && !guiInterface.arePlayoffsFinished();
		simulatePlayoffRoundButton.setEnabled(enabled);
		if (guiInterface.arePlayoffsFinished()) {
			simulatePlayoffRoundButton.setText("Playoffs termines");
		} else {
			simulatePlayoffRoundButton.setText("Simuler le tour");
		}
		if (enabled) {
			simulatePlayoffRoundButton.setBackground(DashboardPanelUtil.getPrimaryActionColor());
			simulatePlayoffRoundButton.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
		} else {
			simulatePlayoffRoundButton.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
			simulatePlayoffRoundButton.setForeground(DashboardPanelUtil.BUTTON_TEXT_COLOR);
		}
	}

	private void styleFilterButton(JButton button, boolean selected) {
		ButtonStyleUtil.styleToggleButton(button);
		ButtonStyleUtil.setToggleButtonSelected(button, selected);
	}

	private JPanel createTeamRow(int rank, Team team) {
		int wins = guiInterface.getTeamNumberWin(team);
		int losses = guiInterface.getTeamNumberLose(team);
		int points = wins * 2;
		String percentage = buildPercentage(team);
		String bestWinStreak = guiInterface.getTeamMaxWinStreak(team) + " W";
		return createRow(rank, TeamDisplayUtility.getShortName(team), wins, losses, points, percentage, bestWinStreak);
	}

	private JPanel createRow(int rank, String teamName, int wins, int losses, int points, String percentage,
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

	private String buildPercentage(Team team) {
		int playedGames = guiInterface.getTeamNumberPlayedGames(team);
		if (playedGames == 0) {
			return "0.0%";
		}
		double winRate = (guiInterface.getTeamNumberWin(team) * 100.0) / playedGames;
		return String.format("%.1f%%", winRate);
	}

	private JLabel createColoredValueLabel(String text, boolean accented, Color color) {
		JLabel label = new JLabel(text);
		label.setForeground(color);
		label.setFont(new Font(Font.SANS_SERIF, accented ? Font.BOLD : Font.PLAIN, 12));
		return label;
	}

	private Color getRankColor(int rank) {
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

	private Color getRowBackground(int rank) {
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

	private class SeasonAction implements ActionListener {
		private final String season;

		private SeasonAction(String season) {
			this.season = season;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setSelectedSeason(season);
		}
	}

	private class SimulatePlayoffRoundAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			guiInterface.simulateNextPlayoffRound();
			refreshRanking();
			updatePlayoffRoundButton();
			if (guiInterface.arePlayoffsFinished() && seasonEndAction != null) {
				seasonEndAction.run();
			}
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
		updateSeasonButtons();
		if (previousPageButton != null) {
			stylePageButton(previousPageButton);
		}
		if (nextPageButton != null) {
			stylePageButton(nextPageButton);
		}
		refreshRanking();
	}
}

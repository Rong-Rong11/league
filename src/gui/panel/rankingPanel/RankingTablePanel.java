package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;
import process.orchestrator.interfaces.GUIInterface;

public class RankingTablePanel extends JPanel implements ThemeAware {
	private static final int GLOBAL_PAGE_SIZE = 15;
	static final String GLOBAL_MODE = "global";
	static final String EAST_MODE = "east";
	static final String WEST_MODE = "west";
	static final String REGULAR_SEASON = "regular";
	static final String PLAYOFFS = "playoffs";

	private final GUIInterface guiInterface;
	private final JPanel tableContentPanel;
	private final RankingFilterBar filterBar;
	private final RankingPlayoffsViewPanel playoffsViewPanel;
	private Runnable seasonEndAction;
	private String selectedMode;
	private String selectedSeason;
	private int globalPageIndex;

	public RankingTablePanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		this.selectedMode = GLOBAL_MODE;
		this.selectedSeason = REGULAR_SEASON;
		this.globalPageIndex = 0;
		this.filterBar = new RankingFilterBar();
		this.playoffsViewPanel = new RankingPlayoffsViewPanel(guiInterface);
		this.tableContentPanel = buildTableContent();

		setLayout(new BorderLayout(0, 12));
		setOpaque(false);
		add(filterBar, BorderLayout.NORTH);
		add(tableContentPanel, BorderLayout.CENTER);

		registerFilterBarActions();
		refreshRanking();
		applyTheme();
	}

	private JPanel buildTableContent() {
		JPanel panel = new JPanel(new BorderLayout(0, 0));
		panel.setOpaque(false);
		return panel;
	}

	private void registerFilterBarActions() {
		filterBar.registerModeActions(new ModeAction(GLOBAL_MODE), new ModeAction(EAST_MODE), new ModeAction(WEST_MODE));
		filterBar.registerSeasonActions(new SeasonAction(REGULAR_SEASON), new SeasonAction(PLAYOFFS),
				new SimulatePlayoffRoundAction());
	}

	public void refreshRanking() {
		tableContentPanel.removeAll();

		if (PLAYOFFS.equals(selectedSeason)) {
			playoffsViewPanel.refreshPlayoffs();
			tableContentPanel.add(playoffsViewPanel, BorderLayout.CENTER);
			refreshLayout();
			return;
		}

		ArrayList<Team> teams = getSelectedTeams();
		if (teams.isEmpty()) {
			tableContentPanel.add(RankingTableFactory.buildEmptyStatePanel(), BorderLayout.CENTER);
			refreshLayout();
			return;
		}

		if (GLOBAL_MODE.equals(selectedMode)) {
			int pageCount = Math.max(1, (int) Math.ceil((double) teams.size() / GLOBAL_PAGE_SIZE));
			if (globalPageIndex >= pageCount) {
				globalPageIndex = pageCount - 1;
			}
			tableContentPanel.add(
					RankingTableFactory.buildPageBar(globalPageIndex, pageCount, new PreviousPageAction(),
							new NextPageAction(pageCount)),
					BorderLayout.NORTH);
			tableContentPanel.add(buildGlobalTable(teams), BorderLayout.CENTER);
		} else {
			tableContentPanel.add(RankingTableFactory.buildSingleTable(teams, 0, teams.size(), guiInterface),
					BorderLayout.CENTER);
		}

		refreshLayout();
	}

	private JPanel buildGlobalTable(ArrayList<Team> teams) {
		int startIndex = globalPageIndex * GLOBAL_PAGE_SIZE;
		int endIndex = Math.min(startIndex + GLOBAL_PAGE_SIZE, teams.size());
		return RankingTableFactory.buildSingleTable(teams, startIndex, endIndex, guiInterface);
	}

	private void refreshLayout() {
		revalidate();
		repaint();
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
		updateFilterBar();
		refreshRanking();
	}

	private void setSelectedSeason(String season) {
		selectedSeason = season;
		updateFilterBar();
		refreshRanking();
	}

	private void updateFilterBar() {
		filterBar.updateModeSelection(selectedMode);
		filterBar.updateSeasonSelection(selectedSeason, guiInterface.hasPlayoffsStarted(), guiInterface.arePlayoffsFinished());
	}

	public void showPlayoffs() {
		setSelectedSeason(PLAYOFFS);
	}

	public void setSeasonEndAction(Runnable seasonEndAction) {
		this.seasonEndAction = seasonEndAction;
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
			updateFilterBar();
			refreshRanking();
			if (guiInterface.arePlayoffsFinished() && seasonEndAction != null) {
				seasonEndAction.run();
			}
		}
	}

	private class PreviousPageAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (globalPageIndex > 0) {
				globalPageIndex--;
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
				refreshRanking();
			}
		}
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.PANEL_SURFACE_COLOR);
		updateFilterBar();
		refreshRanking();
	}
}

package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.BuildBox;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.DashboardTitleBanner;
import gui.panel.common.TeamMapPanel;
import gui.panel.common.ThemeAware;
import gui.panel.mapPanel.effectifPanel.MapTeamSummaryPanel;
import process.orchestrator.interf.GUIInterface;

/**
	* Dashboard dedie a la page Carte.
	*/
public class MapDashboard extends JPanel implements ThemeAware, RefreshableDashboard {
	private static final String DEFAULT_TEAM_NAME = "Los Angeles Lakers";
	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 64;
	private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 270;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 340;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR;

	private GUIInterface guiInterface;
	private ArrayList<Team> teams;
	private Team selectedTeam;
	private TeamMapPanel mapPanel;
	private MapTeamSummaryPanel teamSummaryPanel;
	private Runnable openRosterAction;
	private boolean currentSeasonSelected = true;

	public MapDashboard(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		create();
		organize();
		actions();
		selectDefaultTeam();
	}

	private void create() {
		teams = new ArrayList<Team>(guiInterface.getTeams());
		mapPanel = new TeamMapPanel();
		teamSummaryPanel = new MapTeamSummaryPanel(guiInterface);
	}

	private void organize() {
		setLayout(new BorderLayout());
		setBackground(IDEAL_DASHBOARD_BACKGROUND_COLOR);

		JPanel content = buildContentPanel();
		content.add(buildHeader(), BorderLayout.NORTH);
		content.add(buildBody(), BorderLayout.CENTER);
		add(content, BorderLayout.CENTER);
	}

	private JPanel buildContentPanel() {
		return DashboardPanelUtil.createContentPanel(IDEAL_DASHBOARD_SPACING);
	}

	private JPanel buildHeader() {
		JPanel header = new DashboardTitleBanner("Carte des equipes", "Distribution geographique");
		header.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, IDEAL_DASHBOARD_HEADER_HEIGHT));
		return header;
	}

	private JPanel buildBody() {
		JPanel body = DashboardPanelUtil.createBodyPanel(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING);
		body.add(buildCenterColumn(), BorderLayout.CENTER);
		body.add(buildRightColumn(), BorderLayout.EAST);
		return body;
	}

	private JPanel buildCenterColumn() {
		return new BuildBox("LOCALISATION DES FRANCHISES", "", mapPanel);
	}

	private JPanel buildRightColumn() {
		JPanel column = DashboardPanelUtil.createGridColumn(1, 1, 0, 12, IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH);
		column.add(
				new BuildBox("Details de l'equipe", "Informations detaillees sur l'equipe selectionnee", teamSummaryPanel));
		return column;
	}

	private void actions() {
		teamSummaryPanel.getOpenRosterButton().addActionListener(new OpenRosterListener());
		mapPanel.setTeamSelectionAction(new MapSelectionAction());
	}

	private void selectDefaultTeam() {
		Team defaultTeam = guiInterface.getTeamByName(DEFAULT_TEAM_NAME);
		if (defaultTeam != null) {
			setSelectedTeam(defaultTeam);
			return;
		}
		if (teams.isEmpty()) {
			setSelectedTeam(null);
			return;
		}
		setSelectedTeam(teams.get(0));
	}

	public void setSelectedTeam(Team selectedTeam) {
		this.selectedTeam = selectedTeam;
		teamSummaryPanel.updateTeam(selectedTeam, currentSeasonSelected);
		if (selectedTeam == null) {
			mapPanel.setSelectedTeamName(null);
		} else {
			mapPanel.setSelectedTeamName(selectedTeam.getName());
		}
	}

	public Team getSelectedTeam() {
		return selectedTeam;
	}

	public void setOpenRosterAction(Runnable openRosterAction) {
		this.openRosterAction = openRosterAction;
	}

	public void refreshSelectedTeam() {
		setSelectedTeam(selectedTeam);
	}

	@Override
	public void refresh() {
		refreshSelectedTeam();
	}

	private class OpenRosterListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (openRosterAction != null) {
				openRosterAction.run();
			}
		}
	}

	private class MapSelectionAction implements Runnable {
		@Override
		public void run() {
			setSelectedTeam(guiInterface.getTeamByName(mapPanel.getSelectedTeamName()));
		}
	}

	@Override
	public void applyTheme() {
		setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
		DashboardPanelUtil.refreshChildrenTheme(this);
	}
}

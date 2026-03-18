package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.BuildBox;
import gui.panel.common.SectionTitle;
import gui.panel.teamPanel.MapTeamPlayersPanel;
import gui.panel.teamPanel.MapTeamSummaryPanel;
import process.manager.LeagueManager;
/**
 * Dashboard dédié à la page Carte.
 */
public class MapDashboard extends JPanel {
	private static final int IDEAL_DASHBOARD_SPACING = 16;
	private static final int IDEAL_DASHBOARD_HEADER_HEIGHT = 50;
	private static final int IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH = 270;
	private static final int IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH = 340;
	private static final Color IDEAL_DASHBOARD_BACKGROUND_COLOR = new Color(247, 248, 250);

	private LeagueManager leagueManager;
	private ArrayList<Team> teams;
	private Team selectedTeam;
	private MapTeamSummaryPanel teamSummaryPanel;
	private MapTeamPlayersPanel teamPlayersPanel;
	private Runnable openRosterAction;

	public MapDashboard() {
		this(new LeagueManager());
	}

	public MapDashboard(LeagueManager leagueManager) {
		this.leagueManager = leagueManager;
		create();
		organize();
		actions();
		selectDefaultTeam();
	}

	private void create() {
		teams = new ArrayList<Team>(leagueManager.getLeague().getAllTeam());
		Collections.sort(teams, new Comparator<Team>() {
			@Override
			public int compare(Team a, Team b) {
				return a.getName().compareTo(b.getName());
			}
		});
		teamSummaryPanel = new MapTeamSummaryPanel();
		teamPlayersPanel = new MapTeamPlayersPanel();
		teamSummaryPanel.setTeamNames(buildTeamNames());
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
		JPanel content = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		content.setOpaque(false);
		content.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		return content;
	}

	private JPanel buildHeader(){
		JPanel header = new SectionTitle("Carte des equipes", "Distribution geographique");
		header.setPreferredSize(new Dimension(IDEAL_DASHBOARD_LEFT_COLUMN_WIDTH, IDEAL_DASHBOARD_HEADER_HEIGHT));
		return header;
	}

	private JPanel buildBody() {
		JPanel body = new JPanel(new BorderLayout(IDEAL_DASHBOARD_SPACING, IDEAL_DASHBOARD_SPACING));
		body.setOpaque(false);
		body.add(buildCenterColumn(), BorderLayout.CENTER);
		body.add(buildRightColumn(), BorderLayout.EAST);
		return body;
	}

	private JPanel buildCenterColumn() {
		return new BuildBox("LOCALISATION DES FRANCHISES", "", "CARTE");//! À changer le string par un jpanel quand on aura la fonctionnalité
	}

	private JPanel buildRightColumn(){
		JPanel column = new JPanel(new GridLayout(2, 1, 0, 12));
		column.setOpaque(false);
		column.setPreferredSize(new Dimension(IDEAL_DASHBOARD_RIGHT_COLUMN_WIDTH, 10));
		
		column.add(new BuildBox("Détails de l'équipe", "Informations détaillées sur l'équipe sélectionnée", teamSummaryPanel));
		column.add(new BuildBox("Joueurs de l'équipe", "", teamPlayersPanel));

		return column;
	}

	private void actions() {
		teamSummaryPanel.getOpenRosterButton().addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (openRosterAction != null) {
					openRosterAction.run();
				}
			}
		});
		teamSummaryPanel.setTeamSelectionListener(new MapTeamSummaryPanel.TeamSelectionListener() {
			@Override
			public void onTeamSelected(String teamName) {
				setSelectedTeam(findTeamByName(teamName));
			}
		});
	}

	private void selectDefaultTeam() {
		if (teams.isEmpty()) {
			setSelectedTeam(null);
			return;
		}
		setSelectedTeam(teams.get(0));
	}

	public void setSelectedTeam(Team selectedTeam) {
		this.selectedTeam = selectedTeam;
		teamSummaryPanel.updateTeam(selectedTeam);
		teamPlayersPanel.updateTeam(selectedTeam);
	}

	public Team getSelectedTeam() {
		return selectedTeam;
	}

	public void setOpenRosterAction(Runnable openRosterAction) {
		this.openRosterAction = openRosterAction;
	}

	private String[] buildTeamNames() {
		String[] teamNames = new String[teams.size()];
		for (int i = 0; i < teams.size(); i++) {
			teamNames[i] = teams.get(i).getName();
		}
		return teamNames;
	}

	private Team findTeamByName(String teamName) {
		for (int i = 0; i < teams.size(); i++) {
			if (teams.get(i).getName().equals(teamName)) {
				return teams.get(i);
			}
		}
		return null;
	}
}

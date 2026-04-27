package gui.panel.seasonEndPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.financePanel.FinanceViewFactory;

public class SeasonEndProfilePanel extends JPanel {
	private final SeasonEndDataProvider dataProvider;

	public SeasonEndProfilePanel(SeasonEndDataProvider dataProvider) {
		this.dataProvider = dataProvider;
		organize();
	}

	private void organize() {
		setLayout(new BorderLayout(SeasonEndPanelFactory.GAP, 0));
		setOpaque(false);
		add(buildLeftColumn(), BorderLayout.WEST);
		add(buildChartsPanel(), BorderLayout.CENTER);
	}

	private JPanel buildLeftColumn() {
		JPanel leftColumn = new JPanel(new BorderLayout());
		leftColumn.setOpaque(false);
		leftColumn.setPreferredSize(new Dimension(420, 0));

		leftColumn.add(SeasonEndPanelFactory.buildInfoBox("PROFILS DES CLUBS", "Selection compacte",
				buildTeamProfilePanel(), "Profils des clubs",
				"Pour les meilleurs et pires nets, cette zone affiche le marche, la politique, la strategie, le budget restant et le payroll."),
				BorderLayout.CENTER);
		return leftColumn;
	}

	private JPanel buildChartsPanel() {
		JPanel charts = new JPanel(new GridLayout(1, 2, SeasonEndPanelFactory.GAP, 0));
		charts.setOpaque(false);
		charts.add(SeasonEndPanelFactory.buildInfoBox("MARCHES", "Nombre d'equipes",
				FinanceViewFactory.countBarChart(dataProvider.buildCountDataset(dataProvider.countByMarket(), "Marche"),
						DashboardPanelUtil.POLICY_BALANCED_COLOR),
				"Graphique des marches",
				"Chaque barre compte les equipes dans une taille de marche. Ce n'est pas de l'argent: l'axe vertical indique un nombre d'equipes."));
		charts.add(SeasonEndPanelFactory.buildInfoBox("POLITIQUES", "Nombre d'equipes",
				FinanceViewFactory.countBarChart(dataProvider.buildCountDataset(dataProvider.countByPolicy(), "Politique"),
						DashboardPanelUtil.NEUTRAL_ACCENT_COLOR),
				"Graphique des politiques",
				"Chaque barre compte les equipes par politique financiere. Cela montre si la ligue est plutot prudente, equilibree ou agressive."));
		return charts;
	}

	private JPanel buildTeamProfilePanel() {
		JPanel panel = SeasonEndPanelFactory.buildListPanel();
		List<Team> teamsByNet = dataProvider.getTeamsSortedByNet();
		panel.add(SeasonEndPanelFactory.buildSectionLabel("Meilleurs nets"));
		panel.add(Box.createVerticalStrut(6));
		for (int i = 0; i < SeasonEndPanelFactory.PROFILE_LIST_LIMIT && i < teamsByNet.size(); i++) {
			addProfileRow(panel, teamsByNet.get(i), DashboardPanelUtil.POSITIVE_VALUE_COLOR);
		}
		if (!teamsByNet.isEmpty()) {
			panel.add(Box.createVerticalStrut(8));
			panel.add(SeasonEndPanelFactory.buildSectionLabel("Pires nets"));
			panel.add(Box.createVerticalStrut(6));
			int start = Math.max(0, teamsByNet.size() - SeasonEndPanelFactory.PROFILE_LIST_LIMIT);
			for (int i = start; i < teamsByNet.size(); i++) {
				addProfileRow(panel, teamsByNet.get(i), DashboardPanelUtil.EXPENSE_COLOR);
			}
		}
		return panel;
	}

	private void addProfileRow(JPanel panel, Team team, Color accentColor) {
		panel.add(SeasonEndPanelFactory.buildProfileRow(team, dataProvider, accentColor));
		panel.add(Box.createVerticalStrut(5));
	}
}

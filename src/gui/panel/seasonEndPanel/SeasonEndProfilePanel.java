package gui.panel.seasonEndPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.BuildBox;
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
		JPanel leftColumn = new JPanel(new BorderLayout(0, 10));
		leftColumn.setOpaque(false);
		leftColumn.setPreferredSize(new Dimension(360, 0));

		JPanel distributionPanel = new JPanel(new GridLayout(2, 1, 0, 10));
		distributionPanel.setOpaque(false);
		distributionPanel.add(new BuildBox("TAILLE DU MARCHE", "Repartition", buildMarketSummaryPanel()));
		distributionPanel.add(new BuildBox("POLITIQUE FINANCIERE", "Repartition", buildPolicySummaryPanel()));

		leftColumn.add(new BuildBox("PROFILS DES CLUBS", "Selection compacte", buildTeamProfilePanel()),
				BorderLayout.CENTER);
		leftColumn.add(distributionPanel, BorderLayout.SOUTH);
		return leftColumn;
	}

	private JPanel buildChartsPanel() {
		JPanel charts = new JPanel(new GridLayout(2, 2, SeasonEndPanelFactory.GAP, SeasonEndPanelFactory.GAP));
		charts.setOpaque(false);
		charts.add(new BuildBox("HISTORIQUE LIGUE", "Revenus, depenses et net",
				FinanceViewFactory.financeLineChart(dataProvider.buildLeagueHistoryDataset(),
						DashboardPanelUtil.REVENUE_COLOR)));
		charts.add(new BuildBox("LIGUE", "Revenus contre depenses",
				FinanceViewFactory.financeBarChart(dataProvider.buildLeagueTotalDataset(),
						DashboardPanelUtil.REVENUE_COLOR)));
		charts.add(new BuildBox("MARCHES", "Nombre d'equipes",
				FinanceViewFactory.financeBarChart(dataProvider.buildCountDataset(dataProvider.countByMarket(), "Marche"),
						DashboardPanelUtil.POLICY_BALANCED_COLOR)));
		charts.add(new BuildBox("POLITIQUES", "Nombre d'equipes",
				FinanceViewFactory.financeBarChart(dataProvider.buildCountDataset(dataProvider.countByPolicy(), "Politique"),
						DashboardPanelUtil.NEUTRAL_ACCENT_COLOR)));
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

	private JPanel buildMarketSummaryPanel() {
		return buildCountPanel(dataProvider.countByMarket(), DashboardPanelUtil.POLICY_BALANCED_COLOR);
	}

	private JPanel buildPolicySummaryPanel() {
		return buildCountPanel(dataProvider.countByPolicy(), DashboardPanelUtil.NEUTRAL_ACCENT_COLOR);
	}

	private JPanel buildCountPanel(Map<String, Integer> counts, Color color) {
		JPanel panel = SeasonEndPanelFactory.buildCompactListPanel();
		for (String key : counts.keySet()) {
			panel.add(SeasonEndPanelFactory.buildInfoRow(key, String.valueOf(counts.get(key)), color));
			panel.add(Box.createVerticalStrut(6));
		}
		return panel;
	}
}

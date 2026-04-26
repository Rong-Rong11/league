package gui.panel.seasonEndPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.JPanel;

import data.finance.budget.Budget;
import data.team.Team;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.financePanel.FinanceViewFactory;
import gui.utility.TeamDisplayUtility;

public class SeasonEndFinancePanel extends JPanel {
	private final SeasonEndDataProvider dataProvider;

	public SeasonEndFinancePanel(SeasonEndDataProvider dataProvider) {
		this.dataProvider = dataProvider;
		organize();
	}

	private void organize() {
		setLayout(new BorderLayout(0, SeasonEndPanelFactory.GAP));
		setOpaque(false);

		JPanel top = new JPanel(new GridLayout(1, 2, SeasonEndPanelFactory.GAP, 0));
		top.setOpaque(false);
		top.add(SeasonEndPanelFactory.buildInfoBox("LIGUE", "Budget et resultat global", buildLeagueFinancePanel(),
				"Finance ligue",
				"Budget restant, valeur ligue, revenus, depenses et net cumules sur toute la saison."));
		top.add(SeasonEndPanelFactory.buildInfoBox("CLUBS", "Top et bottom nets", buildTeamFinancePanel(),
				"Nets des clubs",
				"Le net correspond aux revenus moins les depenses. Le top montre les clubs les plus rentables, le bottom ceux qui perdent le plus."));
		add(top, BorderLayout.NORTH);

		JPanel charts = new JPanel(new GridLayout(1, 3, SeasonEndPanelFactory.GAP, 0));
		charts.setOpaque(false);
		charts.add(SeasonEndPanelFactory.buildInfoBox("HISTORIQUE LIGUE", "Revenus, depenses et net",
				FinanceViewFactory.financeLineChart(dataProvider.buildLeagueHistoryDataset(),
						DashboardPanelUtil.REVENUE_COLOR),
				"Historique financier",
				"Chaque point est un mois. Bleu = revenus, rouge = depenses, violet = net. Le net au-dessus de zero indique un mois rentable."));
		charts.add(SeasonEndPanelFactory.buildInfoBox("TOP 5 NETS", "Equipes les plus rentables",
				FinanceViewFactory.financeBarChart(dataProvider.buildTeamNetDataset(true),
						DashboardPanelUtil.POSITIVE_VALUE_COLOR),
				"Top 5 nets",
				"Ces barres classent les cinq clubs avec le meilleur resultat financier total sur la saison."));
		charts.add(SeasonEndPanelFactory.buildInfoBox("BOTTOM 5 NETS", "Equipes les moins rentables",
				FinanceViewFactory.financeBarChart(dataProvider.buildTeamNetDataset(false),
						DashboardPanelUtil.EXPENSE_COLOR),
				"Bottom 5 nets",
				"Ces barres montrent les cinq clubs avec le net total le plus faible. Une valeur negative indique plus de depenses que de revenus."));
		add(charts, BorderLayout.CENTER);
	}

	private JPanel buildLeagueFinancePanel() {
		JPanel panel = SeasonEndPanelFactory.buildListPanel();
		Budget leagueBudget = dataProvider.getLeagueBudget();
		double totalNet = dataProvider.getTotalLeagueNet();
		panel.add(SeasonEndPanelFactory.buildInfoRow("Budget restant",
				dataProvider.formatMoney(dataProvider.getRemainingBudget(leagueBudget)),
				DashboardPanelUtil.POLICY_BALANCED_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Valeur ligue",
				dataProvider.formatMoney(dataProvider.getLeagueValue()), DashboardPanelUtil.NEUTRAL_ACCENT_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Revenus totaux",
				dataProvider.formatMoney(dataProvider.getTotalIncome(leagueBudget)), DashboardPanelUtil.REVENUE_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Depenses totales",
				dataProvider.formatMoney(dataProvider.getTotalExpense(leagueBudget)), DashboardPanelUtil.EXPENSE_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Net total", dataProvider.formatMoney(totalNet),
				DashboardPanelUtil.getValueColorForAmount(totalNet)));
		return panel;
	}

	private JPanel buildTeamFinancePanel() {
		JPanel panel = new JPanel(new GridLayout(1, 2, SeasonEndPanelFactory.GAP, 0));
		panel.setOpaque(false);

		List<Team> teamsByNet = dataProvider.getTeamsSortedByNet();
		JPanel topPanel = SeasonEndPanelFactory.buildListPanel();
		topPanel.add(SeasonEndPanelFactory.buildSectionLabel("Top nets"));
		addTeamNetRows(topPanel, teamsByNet, 0, Math.min(SeasonEndPanelFactory.LIST_LIMIT, teamsByNet.size()));

		JPanel bottomPanel = SeasonEndPanelFactory.buildListPanel();
		bottomPanel.add(SeasonEndPanelFactory.buildSectionLabel("Bottom nets"));
		int bottomStart = Math.max(0, teamsByNet.size() - SeasonEndPanelFactory.LIST_LIMIT);
		addTeamNetRows(bottomPanel, teamsByNet, bottomStart, teamsByNet.size());

		panel.add(topPanel);
		panel.add(bottomPanel);
		return panel;
	}

	private void addTeamNetRows(JPanel panel, List<Team> teams, int start, int end) {
		for (int i = start; i < end && i < teams.size(); i++) {
			Team team = teams.get(i);
			double net = dataProvider.getTotalTeamNet(team);
			panel.add(SeasonEndPanelFactory.buildInfoRow(TeamDisplayUtility.getShortName(team),
					dataProvider.formatMoney(net), DashboardPanelUtil.getValueColorForAmount(net)));
			if (i < end - 1 && i < teams.size() - 1) {
				panel.add(Box.createVerticalStrut(8));
			}
		}
	}
}

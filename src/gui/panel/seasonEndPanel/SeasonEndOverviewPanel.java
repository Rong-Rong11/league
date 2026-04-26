package gui.panel.seasonEndPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JPanel;

import data.team.Team;
import gui.panel.common.BuildBox;
import gui.panel.common.DashboardPanelUtil;
import gui.utility.TeamDisplayUtility;

public class SeasonEndOverviewPanel extends JPanel {
	private final SeasonEndDataProvider dataProvider;

	public SeasonEndOverviewPanel(SeasonEndDataProvider dataProvider) {
		this.dataProvider = dataProvider;
		organize();
	}

	private void organize() {
		setLayout(new BorderLayout(0, SeasonEndPanelFactory.GAP));
		setOpaque(false);
		add(buildKeyStatsPanel(), BorderLayout.NORTH);

		JPanel center = new JPanel(new GridLayout(1, 2, SeasonEndPanelFactory.GAP, 0));
		center.setOpaque(false);
		center.add(new BuildBox("BILAN SPORTIF", "Classement et playoffs", buildSportPanel()));
		center.add(new BuildBox("SYNTHESE SAISON", "Resultats finaux", buildSeasonSummaryPanel()));
		add(center, BorderLayout.CENTER);
	}

	private JPanel buildKeyStatsPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 6, SeasonEndPanelFactory.GAP, 0));
		panel.setOpaque(false);
		Team bestTeam = dataProvider.getBestRegularSeasonTeam();
		Team bestAttack = dataProvider.getBestAttackTeam();
		Team richestTeam = dataProvider.getRichestTeam();
		Team bestNetTeam = dataProvider.getBestNetTeam();
		Team worstNetTeam = dataProvider.getWorstNetTeam();

		panel.add(SeasonEndPanelFactory.buildStatCard("Champion NBA", dataProvider.getChampionName(),
				"Playoffs termines", DashboardPanelUtil.NEUTRAL_ACCENT_COLOR));
		panel.add(SeasonEndPanelFactory.buildStatCard("Meilleure equipe", TeamDisplayUtility.getShortName(bestTeam),
				dataProvider.buildRecordText(bestTeam), DashboardPanelUtil.REVENUE_COLOR));
		panel.add(SeasonEndPanelFactory.buildStatCard("Meilleure attaque", TeamDisplayUtility.getShortName(bestAttack),
				dataProvider.formatOneDecimal(dataProvider.getAveragePoints(bestAttack)) + " pts/match",
				DashboardPanelUtil.POSITIVE_VALUE_COLOR));
		panel.add(SeasonEndPanelFactory.buildStatCard("Plus gros budget", TeamDisplayUtility.getShortName(richestTeam),
				dataProvider.formatMoney(dataProvider.getRemainingBudget(richestTeam)),
				DashboardPanelUtil.POLICY_BALANCED_COLOR));
		panel.add(SeasonEndPanelFactory.buildStatCard("Meilleur net", TeamDisplayUtility.getShortName(bestNetTeam),
				dataProvider.formatMoney(dataProvider.getTotalTeamNet(bestNetTeam)),
				DashboardPanelUtil.POSITIVE_VALUE_COLOR));
		panel.add(SeasonEndPanelFactory.buildStatCard("Pire net", TeamDisplayUtility.getShortName(worstNetTeam),
				dataProvider.formatMoney(dataProvider.getTotalTeamNet(worstNetTeam)), DashboardPanelUtil.EXPENSE_COLOR));
		return panel;
	}

	private JPanel buildSportPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 2, SeasonEndPanelFactory.GAP, 0));
		panel.setOpaque(false);
		panel.add(buildTopRankingPanel());
		panel.add(buildPlayoffSummaryPanel());
		return panel;
	}

	private JPanel buildTopRankingPanel() {
		JPanel panel = SeasonEndPanelFactory.buildListPanel();
		ArrayList<Team> ranking = dataProvider.getGlobalRanking();
		for (int index = 0; index < 8 && index < ranking.size(); index++) {
			Team team = ranking.get(index);
			panel.add(SeasonEndPanelFactory.buildInfoRow((index + 1) + ". " + TeamDisplayUtility.getShortName(team),
					dataProvider.buildRecordText(team), dataProvider.getRankColor(index + 1)));
			if (index < 7) {
				panel.add(Box.createVerticalStrut(8));
			}
		}
		return panel;
	}

	private JPanel buildPlayoffSummaryPanel() {
		JPanel panel = SeasonEndPanelFactory.buildListPanel();
		panel.add(SeasonEndPanelFactory.buildInfoRow("Champion", dataProvider.getChampionName(),
				DashboardPanelUtil.NEUTRAL_ACCENT_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Finaliste", TeamDisplayUtility.getShortName(dataProvider.getFinalist()),
				DashboardPanelUtil.POLICY_BALANCED_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Finale", dataProvider.buildFinalsScoreText(),
				DashboardPanelUtil.TITLE_TEXT_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Series jouees", String.valueOf(dataProvider.getPlayoffSeriesCount()),
				DashboardPanelUtil.REVENUE_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Matchs playoffs", String.valueOf(dataProvider.countPlayoffGames()),
				DashboardPanelUtil.POSITIVE_VALUE_COLOR));
		return panel;
	}

	private JPanel buildSeasonSummaryPanel() {
		JPanel panel = SeasonEndPanelFactory.buildListPanel();
		panel.add(SeasonEndPanelFactory.buildInfoRow("Champion", dataProvider.getChampionName(),
				DashboardPanelUtil.NEUTRAL_ACCENT_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Finaliste", TeamDisplayUtility.getShortName(dataProvider.getFinalist()),
				DashboardPanelUtil.POLICY_BALANCED_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Series jouees", String.valueOf(dataProvider.getPlayoffSeriesCount()),
				DashboardPanelUtil.REVENUE_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Matchs playoffs", String.valueOf(dataProvider.countPlayoffGames()),
				DashboardPanelUtil.POSITIVE_VALUE_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Finale NBA", dataProvider.buildFinalsScoreText(),
				DashboardPanelUtil.TITLE_TEXT_COLOR));
		panel.add(Box.createVerticalStrut(10));
		panel.add(SeasonEndPanelFactory.buildInfoRow("Matchs totaux", String.valueOf(dataProvider.countTotalGames()),
				DashboardPanelUtil.STRATEGY_REBUILD_COLOR));
		return panel;
	}
}

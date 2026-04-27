package gui.panel.seasonEndPanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.league.League;
import data.league.Playoff;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import data.team.finance.TeamFinance;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.financePanel.FinanceDataUtil;
import gui.utility.TeamDisplayUtility;
import process.orchestrator.interfaces.GUIInterface;
import process.utility.FinanceSummaryUtility;
import process.utility.SeasonEndSummaryUtility;
import process.utility.TeamMetricsUtility;

public class SeasonEndDataProvider {
	private final GUIInterface guiInterface;

	public SeasonEndDataProvider(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
	}

	public ArrayList<Team> getGlobalRanking() {
		return guiInterface.getGlobalRanking();
	}

	public Team getBestRegularSeasonTeam() {
		return SeasonEndSummaryUtility.getBestRegularSeasonTeam(guiInterface.getGlobalRanking());
	}

	public Team getBestAttackTeam() {
		return SeasonEndSummaryUtility.getBestAttackTeam(guiInterface.getGlobalRanking());
	}

	public Team getRichestTeam() {
		return SeasonEndSummaryUtility.getRichestTeam(guiInterface.getTeams());
	}

	public Team getBestNetTeam() {
		List<Team> teams = getTeamsSortedByNet();
		return teams.isEmpty() ? null : teams.get(0);
	}

	public Team getWorstNetTeam() {
		List<Team> teams = getTeamsSortedByNet();
		return teams.isEmpty() ? null : teams.get(teams.size() - 1);
	}

	public List<Team> getTeamsSortedByNet() {
		return SeasonEndSummaryUtility.getTeamsSortedByNet(guiInterface.getTeams(), lastFinanceMonth());
	}

	public String getChampionName() {
		return safeText(guiInterface.getPlayoffChampionName());
	}

	public Team getFinalist() {
		PlayoffSeries finals = getFinalsSeries();
		if (finals == null || !finals.isFinished()) {
			return null;
		}
		if (finals.getHigherTeamWins() > finals.getLowerTeamWins()) {
			return finals.getLowerTeam();
		}
		return finals.getHigherTeam();
	}

	public String buildFinalsScoreText() {
		PlayoffSeries finals = getFinalsSeries();
		if (finals == null) {
			return "-";
		}
		return TeamDisplayUtility.getShortName(finals.getHigherTeam()) + " "
				+ finals.getHigherTeamWins() + "-"
				+ finals.getLowerTeamWins() + " "
				+ TeamDisplayUtility.getShortName(finals.getLowerTeam());
	}

	public String buildHeaderSubtitle() {
		String champion = guiInterface.getPlayoffChampionName();
		String finalist = TeamDisplayUtility.getShortName(getFinalist());
		if (champion == null || champion.equals("")) {
			return "Bilan final de la saison sur plusieurs vues.";
		}
		if (finalist == null || finalist.equals("-")) {
			return "Champion NBA : " + champion + ". Navigation sans scroll.";
		}
		return "Champion NBA : " + champion + " contre " + finalist + ". Navigation sans scroll.";
	}

	public int getPlayoffSeriesCount() {
		return guiInterface.getPlayoffSeriesCount();
	}

	public String buildRecordText(Team team) {
		if (team == null) {
			return "-";
		}
		int wins = guiInterface.getTeamNumberWin(team);
		int losses = guiInterface.getTeamNumberLose(team);
		int games = wins + losses;
		if (games == 0) {
			return wins + "-" + losses;
		}
		double pct = wins * 100.0 / games;
		return wins + "-" + losses + " (" + formatOneDecimal(pct) + "%)";
	}

	public double getAveragePoints(Team team) {
		return TeamMetricsUtility.getAveragePoints(team, true);
	}

	public Budget getLeagueBudget() {
		League league = guiInterface.getLeague();
		if (league == null || league.getLeagueFinance() == null) {
			return null;
		}
		return league.getLeagueFinance().getBudget();
	}

	public double getLeagueValue() {
		League league = guiInterface.getLeague();
		if (league == null || league.getLeagueFinance() == null) {
			return 0.0;
		}
		return league.getLeagueFinance().getLeagueValue();
	}

	public double getTotalTeamNet(Team team) {
		return SeasonEndSummaryUtility.getTotalTeamNet(team, lastFinanceMonth());
	}

	public double getTotalLeagueNet() {
		return FinanceSummaryUtility.getTotalNet(getLeagueBudget(), lastFinanceMonth());
	}

	public double getTotalTvRevenue() {
		return guiInterface.getTotalTvRevenue();
	}

	public double getTotalMerchandisingRevenue() {
		return guiInterface.getTotalMerchandisingRevenue();
	}

	public double getTotalIncome(Budget budget) {
		return FinanceSummaryUtility.getTotalIncome(budget, lastFinanceMonth());
	}

	public double getTotalExpense(Budget budget) {
		return FinanceSummaryUtility.getTotalExpense(budget, lastFinanceMonth());
	}

	public double getRemainingBudget(Team team) {
		return SeasonEndSummaryUtility.getRemainingBudget(team);
	}

	public double getRemainingBudget(Budget budget) {
		return budget == null ? 0.0 : budget.getRemainingAmount();
	}

	public double getCurrentPayroll(Team team) {
		return team == null || team.getTeamFinance() == null ? 0.0 : team.getTeamFinance().getCurrentPayroll();
	}

	public Map<String, Integer> countByMarket() {
		return new LinkedHashMap<String, Integer>(SeasonEndSummaryUtility.countByMarket(guiInterface.getTeams()));
	}

	public Map<String, Integer> countByPolicy() {
		return new LinkedHashMap<String, Integer>(SeasonEndSummaryUtility.countByPolicy(guiInterface.getTeams()));
	}

	public String getMarketLabel(Team team) {
		return SeasonEndSummaryUtility.getMarketLabel(team);
	}

	public String getPolicyLabel(Team team) {
		return SeasonEndSummaryUtility.getPolicyLabel(team);
	}

	public String getStrategyLabel(Team team) {
		TeamFinance finance = team == null ? null : team.getTeamFinance();
		if (finance == null || finance.getBehavior() == null) {
			return "-";
		}
		return FinanceDataUtil.formatStrategy(finance.getBehavior().getTeamTransferStrategy());
	}

	public DefaultCategoryDataset buildLeagueHistoryDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Budget budget = getLeagueBudget();
		for (int month = 1; month <= lastFinanceMonth(); month++) {
			String label = "M" + month;
			dataset.addValue(toChartAmount(FinanceSummaryUtility.getIncomeForMonth(budget, month)), "Revenus", label);
			dataset.addValue(toChartAmount(FinanceSummaryUtility.getExpenseForMonth(budget, month)), "Depenses", label);
			dataset.addValue(toChartAmount(budget == null ? 0.0 : budget.getNetForMonth(month)), "Net", label);
		}
		return dataset;
	}

	public DefaultCategoryDataset buildTeamNetDataset(boolean top) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<Team> teams = new ArrayList<Team>(getTeamsSortedByNet());
		if (!top) {
			Collections.reverse(teams);
		}
		int limit = Math.min(SeasonEndPanelFactory.LIST_LIMIT, teams.size());
		for (int i = 0; i < limit; i++) {
			Team team = teams.get(i);
			dataset.addValue(toChartAmount(getTotalTeamNet(team)), top ? "Top nets" : "Bottom nets",
					TeamDisplayUtility.getShortName(team));
		}
		return dataset;
	}

	public DefaultCategoryDataset buildCountDataset(Map<String, Integer> counts, String rowName) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (String key : counts.keySet()) {
			dataset.addValue(counts.get(key), rowName, key);
		}
		return dataset;
	}

	public String formatMoney(double value) {
		return FinanceDataUtil.formatMoney(value);
	}

	public String formatOneDecimal(double value) {
		return String.format(java.util.Locale.US, "%.1f", value);
	}

	public String safeText(String text) {
		return text == null || text.equals("") ? "-" : text;
	}

	public Color getRankColor(int rank) {
		if (rank == 1) {
			return DashboardPanelUtil.NEUTRAL_ACCENT_COLOR;
		}
		if (rank <= 3) {
			return DashboardPanelUtil.POLICY_BALANCED_COLOR;
		}
		return DashboardPanelUtil.REVENUE_COLOR;
	}

	private PlayoffSeries getFinalsSeries() {
		Playoff playoff = getPlayoff();
		if (playoff == null || playoff.getNbaFinals().isEmpty()) {
			return null;
		}
		return playoff.getNbaFinals().get(0);
	}

	private Playoff getPlayoff() {
		League league = guiInterface.getLeague();
		return league == null ? null : league.getPlayoff();
	}

	private int lastFinanceMonth() {
		return Math.max(1, FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS - 1);
	}

	private double toChartAmount(double value) {
		return value;
	}

}

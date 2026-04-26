package gui.panel.seasonEndPanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.finance.budget.income.Income;
import data.league.League;
import data.league.Playoff;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import data.team.finance.TeamFinance;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.financePanel.FinanceDataUtil;
import gui.utility.TeamDisplayUtility;
import process.orchestrator.interf.GUIInterface;

public class SeasonEndDataProvider {
	private final GUIInterface guiInterface;

	public SeasonEndDataProvider(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
	}

	public ArrayList<Team> getGlobalRanking() {
		return guiInterface.getGlobalRanking();
	}

	public Team getBestRegularSeasonTeam() {
		ArrayList<Team> ranking = guiInterface.getGlobalRanking();
		return ranking.isEmpty() ? null : ranking.get(0);
	}

	public Team getBestAttackTeam() {
		Team bestTeam = null;
		double bestScore = -1.0;
		for (Team team : guiInterface.getGlobalRanking()) {
			double score = guiInterface.getAveragePoints(team, true);
			if (score > bestScore) {
				bestScore = score;
				bestTeam = team;
			}
		}
		return bestTeam;
	}

	public Team getRichestTeam() {
		Team bestTeam = null;
		double bestBudget = Double.NEGATIVE_INFINITY;
		for (Team team : guiInterface.getTeams()) {
			double budget = getRemainingBudget(team);
			if (budget > bestBudget) {
				bestBudget = budget;
				bestTeam = team;
			}
		}
		return bestTeam;
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
		ArrayList<Team> teams = new ArrayList<Team>(guiInterface.getTeams());
		Collections.sort(teams, new Comparator<Team>() {
			@Override
			public int compare(Team first, Team second) {
				return Double.compare(getTotalTeamNet(second), getTotalTeamNet(first));
			}
		});
		return teams;
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
		return guiInterface.getAveragePoints(team, true);
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
		return team == null ? 0.0 : guiInterface.getTeamTotalNet(team);
	}

	public double getTotalLeagueNet() {
		return guiInterface.getLeagueTotalNet();
	}

	public double getTotalTvRevenue() {
		return guiInterface.getTotalTvRevenue();
	}

	public double getTotalMerchandisingRevenue() {
		return guiInterface.getTotalMerchandisingRevenue();
	}

	public double getTotalIncome(Budget budget) {
		double total = 0.0;
		if (budget != null) {
			for (int month = 1; month <= lastFinanceMonth(); month++) {
				total += getIncomeForMonth(budget, month);
			}
		}
		return total;
	}

	public double getTotalExpense(Budget budget) {
		double total = 0.0;
		if (budget != null) {
			for (int month = 1; month <= lastFinanceMonth(); month++) {
				total += getExpenseForMonth(budget, month);
			}
		}
		return total;
	}

	public double getRemainingBudget(Team team) {
		return getRemainingBudget(getTeamBudget(team));
	}

	public double getRemainingBudget(Budget budget) {
		return budget == null ? 0.0 : budget.getRemainingAmount();
	}

	public double getCurrentPayroll(Team team) {
		return team == null || team.getTeamFinance() == null ? 0.0 : team.getTeamFinance().getCurrentPayroll();
	}

	public Map<String, Integer> countByMarket() {
		Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
		counts.put("Petit marche", 0);
		counts.put("Marche moyen", 0);
		counts.put("Grand marche", 0);
		for (Team team : guiInterface.getTeams()) {
			increment(counts, getMarketLabel(team));
		}
		return counts;
	}

	public Map<String, Integer> countByPolicy() {
		Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
		counts.put("Politique econome", 0);
		counts.put("Politique equilibree", 0);
		counts.put("Politique ambitieuse", 0);
		for (Team team : guiInterface.getTeams()) {
			increment(counts, getPolicyLabel(team));
		}
		return counts;
	}

	public String getMarketLabel(Team team) {
		TeamFinance finance = team == null ? null : team.getTeamFinance();
		if (finance == null || finance.getStructure() == null) {
			return "-";
		}
		return FinanceDataUtil.formatMarket(finance.getStructure().getMarketSize());
	}

	public String getPolicyLabel(Team team) {
		TeamFinance finance = team == null ? null : team.getTeamFinance();
		if (finance == null || finance.getBehavior() == null) {
			return "-";
		}
		return FinanceDataUtil.formatPolicy(finance.getBehavior().getFinancialProfil());
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
			dataset.addValue(toChartAmount(getIncomeForMonth(budget, month)), "Revenus", label);
			dataset.addValue(toChartAmount(getExpenseForMonth(budget, month)), "Depenses", label);
			dataset.addValue(toChartAmount(guiInterface.getLeagueNetForMonth(month)), "Net", label);
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

	private Budget getTeamBudget(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getBudget();
	}

	private double getIncomeForMonth(Budget budget, int month) {
		double total = 0.0;
		if (budget != null && budget.getIncomesForMonth(month) != null) {
			for (Income income : budget.getIncomesForMonth(month).values()) {
				total += income.getAmount();
			}
		}
		return total;
	}

	private double getExpenseForMonth(Budget budget, int month) {
		double total = 0.0;
		if (budget != null && budget.getExpensesForMonth(month) != null) {
			for (Expense expense : budget.getExpensesForMonth(month).values()) {
				total += expense.getAmount();
			}
		}
		return total;
	}

	private int lastFinanceMonth() {
		return Math.max(1, FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS - 1);
	}

	private double toChartAmount(double value) {
		return value;
	}

	private void increment(Map<String, Integer> counts, String key) {
		String safeKey = key == null || key.equals("-") ? "Inconnu" : key;
		if (!counts.containsKey(safeKey)) {
			counts.put(safeKey, 0);
		}
		counts.put(safeKey, counts.get(safeKey) + 1);
	}
}

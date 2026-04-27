package process.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import data.finance.budget.Budget;
import data.team.Team;
import data.team.finance.TeamFinance;

public final class SeasonEndSummaryUtility {

	private SeasonEndSummaryUtility() {
	}

	public static Team getBestRegularSeasonTeam(List<Team> globalRanking) {
		return globalRanking.isEmpty() ? null : globalRanking.get(0);
	}

	public static Team getBestAttackTeam(List<Team> globalRanking) {
		Team bestTeam = null;
		double bestScore = -1.0;
		for (Team team : globalRanking) {
			double score = TeamMetricsUtility.getAveragePoints(team, true);
			if (score > bestScore) {
				bestScore = score;
				bestTeam = team;
			}
		}
		return bestTeam;
	}

	public static Team getRichestTeam(List<Team> teams) {
		Team bestTeam = null;
		double bestBudget = Double.NEGATIVE_INFINITY;
		for (Team team : teams) {
			double budget = getRemainingBudget(team);
			if (budget > bestBudget) {
				bestBudget = budget;
				bestTeam = team;
			}
		}
		return bestTeam;
	}

	public static List<Team> getTeamsSortedByNet(List<Team> teams, int lastFinanceMonth) {
		ArrayList<Team> sortedTeams = new ArrayList<Team>(teams);
		Collections.sort(sortedTeams, new Comparator<Team>() {
			@Override
			public int compare(Team first, Team second) {
				return Double.compare(getTotalTeamNet(second, lastFinanceMonth), getTotalTeamNet(first, lastFinanceMonth));
			}
		});
		return sortedTeams;
	}

	public static double getTotalTeamNet(Team team, int lastFinanceMonth) {
		return FinanceSummaryUtility.getTotalNet(getBudget(team), lastFinanceMonth);
	}

	public static double getRemainingBudget(Team team) {
		Budget budget = getBudget(team);
		return budget == null ? 0.0 : budget.getRemainingAmount();
	}

	public static Map<String, Integer> countByMarket(List<Team> teams) {
		Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
		counts.put("Petit marche", 0);
		counts.put("Marche moyen", 0);
		counts.put("Grand marche", 0);
		for (Team team : teams) {
			increment(counts, getMarketLabel(team));
		}
		return counts;
	}

	public static Map<String, Integer> countByPolicy(List<Team> teams) {
		Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
		counts.put("Politique econome", 0);
		counts.put("Politique equilibree", 0);
		counts.put("Politique ambitieuse", 0);
		for (Team team : teams) {
			increment(counts, getPolicyLabel(team));
		}
		return counts;
	}

	public static String getMarketLabel(Team team) {
		TeamFinance finance = team == null ? null : team.getTeamFinance();
		if (finance == null || finance.getStructure() == null) {
			return "-";
		}
		return FinanceLabelUtility.formatMarket(finance.getStructure().getMarketSize());
	}

	public static String getPolicyLabel(Team team) {
		TeamFinance finance = team == null ? null : team.getTeamFinance();
		if (finance == null || finance.getBehavior() == null) {
			return "-";
		}
		return FinanceLabelUtility.formatPolicy(finance.getBehavior().getFinancialPolicy());
	}

	private static Budget getBudget(Team team) {
		if (team == null || team.getTeamFinance() == null) {
			return null;
		}
		return team.getTeamFinance().getBudget();
	}

	private static void increment(Map<String, Integer> counts, String key) {
		String safeKey = key == null || key.equals("-") ? "Inconnu" : key;
		if (!counts.containsKey(safeKey)) {
			counts.put(safeKey, 0);
		}
		counts.put(safeKey, counts.get(safeKey) + 1);
	}
}

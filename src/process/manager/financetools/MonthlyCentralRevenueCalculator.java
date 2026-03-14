package process.manager.financetools;

import data.team.Team;
import process.repositery.TeamRepositery;

public class MonthlyCentralRevenueCalculator {
	private TeamRepositery teamRepositery = TeamRepositery.getInstance();

	public double calculateNationalTvRevenue() {
		double totalPopularity = 0;
		double totalPerformance = 0;

		for (Team team : teamRepositery.getAllTeams()) {
			totalPopularity += team.getPopularity();
			totalPerformance += team.getTeamPerformance().getPerformanceRating();
		}

		double averagePopularity = totalPopularity / teamRepositery.getAllTeams().size();
		double averagePerformance = totalPerformance / teamRepositery.getAllTeams().size();

		return (0.18 * teamRepositery.getAllTeams().size()) + (averagePopularity * 0.03) + (averagePerformance * 2.0);
	}

	public double calculateNationalSponsoringRevenue() {
		double totalPopularity = 0;
		int numberOfStarTeams = 0;

		for (Team team : teamRepositery.getAllTeams()) {
			totalPopularity += team.getPopularity();
			if (team.getStarPlayer() != null) {
				numberOfStarTeams++;
			}
		}

		double averagePopularity = totalPopularity / teamRepositery.getAllTeams().size();
		return (0.10 * teamRepositery.getAllTeams().size()) + (averagePopularity * 0.02) + (numberOfStarTeams * 0.08);
	}

	public double calculateNationalMerchandisingRevenue() {
		double totalPopularity = 0;
		double totalPayroll = 0;

		for (Team team : teamRepositery.getAllTeams()) {
			totalPopularity += team.getPopularity();
			totalPayroll += team.getTeamFinance().getPayroll();
		}

		double averagePopularity = totalPopularity / teamRepositery.getAllTeams().size();
		double averagePayroll = totalPayroll / teamRepositery.getAllTeams().size();

		return (0.06 * teamRepositery.getAllTeams().size()) + (averagePopularity * 0.015) + (averagePayroll * 0.02);
	}
}

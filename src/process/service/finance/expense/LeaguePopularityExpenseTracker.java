package process.service.finance.expense;

import data.team.Team;
import process.repository.TeamRepository;

public class LeaguePopularityExpenseTracker {

	private final TeamRepository teamRepository = TeamRepository.getInstance();
	private final double initialAveragePopularity;

	public LeaguePopularityExpenseTracker() {
		this.initialAveragePopularity = calculateAverageTeamPopularity();
	}

	public double getPopularitySeasonExpenseRate() {
		double currentAveragePopularity = calculateAverageTeamPopularity();
		double growth = currentAveragePopularity - initialAveragePopularity;

		if (growth <= 0) {
			return 1.0;
		}
		if (growth < 3) {
			return 1.04;
		}
		if (growth < 6) {
			return 1.08;
		}
		return 1.14;
	}

	private double calculateAverageTeamPopularity() {
		double total = 0.0;
		int teamCount = 0;

		for (Team team : teamRepository.getAllTeams()) {
			total += team.getCurrentPopularity();
			teamCount++;
		}

		return teamCount == 0 ? 0.0 : total / teamCount;
	}
}

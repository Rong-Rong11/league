package process.service.league;

import data.league.PlayoffRound;
import data.team.Team;
import process.repository.TeamRepository;

public class TeamPopularityUpdater {
	private TeamRepository teamRepositery = TeamRepository.getInstance();
	private TeamPopularityVariationCalculator variationCalculator = new TeamPopularityVariationCalculator();
	private PlayoffPopularityImpactCalculator playoffImpactCalculator = new PlayoffPopularityImpactCalculator();

	public TeamPopularityUpdater() {
	}

	public void updateBeforeSeason() {
		for (Team team : teamRepositery.getAllTeams()) {
			updateTeamBeforeSeason(team);
		}
	}

	public void updateMonthlyPopularity() {
		for (Team team : teamRepositery.getAllTeams()) {
			updateTeamMonthlyPopularity(team);
		}
	}

	public void applyPlayoffQualificationBonus(Team team) {
		double bonus = 3.0;
		double newPopularity = clampPopularity(team.getCurrentPopularity() + bonus);
		team.setCurrentPopularity(newPopularity);
	}

	public void applyPlayoffRoundBonus(Team team, PlayoffRound round) {
		double bonus = playoffImpactCalculator.getPlayoffRoundPopularityBonus(round);
		double newPopularity = clampPopularity(team.getCurrentPopularity() + bonus);
		team.setCurrentPopularity(newPopularity);
	}

	public void applyMissedPlayoffPenalty(Team team) {
		double penalty = playoffImpactCalculator.calculateMissedPlayoffPenalty(team);
		double newPopularity = clampPopularity(team.getCurrentPopularity() - penalty);
		team.setCurrentPopularity(newPopularity);
	}

	private void updateTeamBeforeSeason(Team team) {
		double currentPopularity = team.getFormerPopularity();
		double variation = variationCalculator.calculatePreSeasonVariation(team);

		double newPopularity = clampPopularity(currentPopularity + variation);
		team.setFormerPopularity(newPopularity);
		team.setCurrentPopularity(newPopularity);
	}

	private void updateTeamMonthlyPopularity(Team team) {
		double currentPopularity = team.getCurrentPopularity();
		double variation = variationCalculator.calculateMonthlyVariation(team);

		double newPopularity = currentPopularity + (variation * 0.4);
		newPopularity = clampPopularity(newPopularity);
		team.setCurrentPopularity(newPopularity);
	}

	private double clampPopularity(double popularity) {
		return Math.max(20.0, Math.min(100.0, popularity));
	}
}

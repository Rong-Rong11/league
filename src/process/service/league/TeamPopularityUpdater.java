package process.service.league;

import org.apache.log4j.Logger;

import data.league.PlayoffRound;
import data.team.Team;
import log.LoggerUtility;
import process.repository.TeamRepository;

public class TeamPopularityUpdater {
	private static final Logger logger = LoggerUtility.getLogger(TeamPopularityUpdater.class, "text");

	private TeamRepository teamRepositery = TeamRepository.getInstance();
	private TeamPopularityVariationCalculator variationCalculator = new TeamPopularityVariationCalculator();
	private PlayoffPopularityImpactCalculator playoffImpactCalculator = new PlayoffPopularityImpactCalculator();

	public TeamPopularityUpdater() {
	}

	public void updateBeforeSeason() {
		logger.debug("Updating team popularity before season");
		for (Team team : teamRepositery.getAllTeams()) {
			updateTeamBeforeSeason(team);
		}
		logger.debug("Team popularity updated before season");
	}

	public void updateMonthlyPopularity() {
		logger.debug("Updating monthly team popularity");
		for (Team team : teamRepositery.getAllTeams()) {
			updateTeamMonthlyPopularity(team);
		}
		logger.debug("Monthly team popularity updated");
	}

	public void applyPlayoffQualificationBonus(Team team) {
		if (team == null) {
			logger.warn("Skipping playoff qualification popularity bonus because team is null");
			return;
		}

		double bonus = 3.0;
		double newPopularity = clampPopularity(team.getCurrentPopularity() + bonus);

		logger.debug("Applying playoff qualification popularity bonus to " + team.getName());
		logger.trace("Bonus: " + bonus + ", new popularity: " + newPopularity);

		team.setCurrentPopularity(newPopularity);
	}

	public void applyPlayoffRoundBonus(Team team, PlayoffRound round) {
		if (team == null || round == null) {
			logger.warn("Skipping playoff round popularity bonus because team or round is null");
			return;
		}

		double bonus = playoffImpactCalculator.getPlayoffRoundPopularityBonus(round);
		double newPopularity = clampPopularity(team.getCurrentPopularity() + bonus);

		logger.debug("Applying playoff round popularity bonus to " + team.getName() + " for round " + round);
		logger.trace("Bonus: " + bonus + ", new popularity: " + newPopularity);

		team.setCurrentPopularity(newPopularity);
	}

	public void applyMissedPlayoffPenalty(Team team) {
		if (team == null) {
			logger.warn("Skipping missed playoff penalty because team is null");
			return;
		}

		double penalty = playoffImpactCalculator.calculateMissedPlayoffPenalty(team);
		double newPopularity = clampPopularity(team.getCurrentPopularity() - penalty);

		logger.debug("Applying missed playoff penalty to " + team.getName());
		logger.trace("Penalty: " + penalty + ", new popularity: " + newPopularity);

		team.setCurrentPopularity(newPopularity);
	}

	private void updateTeamBeforeSeason(Team team) {
		if (team == null) {
			logger.warn("Skipping preseason popularity update because team is null");
			return;
		}

		double currentPopularity = team.getFormerPopularity();
		double variation = variationCalculator.calculatePreSeasonVariation(team);

		double newPopularity = clampPopularity(currentPopularity + variation);

		logger.trace("Preseason update for " + team.getName()
				+ " | current: " + currentPopularity
				+ ", variation: " + variation
				+ ", new: " + newPopularity);

		team.setFormerPopularity(newPopularity);
		team.setCurrentPopularity(newPopularity);
	}

	private void updateTeamMonthlyPopularity(Team team) {
		if (team == null) {
			logger.warn("Skipping monthly popularity update because team is null");
			return;
		}

		double currentPopularity = team.getCurrentPopularity();
		double variation = variationCalculator.calculateMonthlyVariation(team);

		double newPopularity = currentPopularity + (variation * 0.4);
		newPopularity = clampPopularity(newPopularity);

		logger.trace("Monthly update for " + team.getName()
				+ " | current: " + currentPopularity
				+ ", variation: " + variation
				+ ", new: " + newPopularity);

		team.setCurrentPopularity(newPopularity);
	}

	private double clampPopularity(double popularity) {
		double clamped = Math.max(20.0, Math.min(100.0, popularity));
		logger.trace("Clamping popularity value: " + popularity + " -> " + clamped);
		return clamped;
	}
}

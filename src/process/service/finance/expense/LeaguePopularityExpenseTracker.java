package process.service.finance.expense;

import org.apache.log4j.Logger;

import data.team.Team;
import log.LoggerUtility;
import process.repository.TeamRepository;

public class LeaguePopularityExpenseTracker {
	private static final Logger logger = LoggerUtility.getLogger(LeaguePopularityExpenseTracker.class, "text");

	private final TeamRepository teamRepository = TeamRepository.getInstance();
	private final double initialAveragePopularity;

	public LeaguePopularityExpenseTracker() {
		this.initialAveragePopularity = calculateAverageTeamPopularity();
		logger.debug("League popularity expense tracker initialized with average popularity "
				+ initialAveragePopularity);
	}

	public double getPopularitySeasonExpenseRate() {
		double currentAveragePopularity = calculateAverageTeamPopularity();
		double growth = currentAveragePopularity - initialAveragePopularity;
		logger.debug("Calculating popularity season expense rate with initialAverage="
				+ initialAveragePopularity
				+ ", currentAverage="
				+ currentAveragePopularity
				+ ", growth="
				+ growth);

		if (growth <= 0) {
			logger.debug("Popularity season expense rate is 1.0");
			return 1.0;
		}
		if (growth < 3) {
			logger.debug("Popularity season expense rate is 1.04");
			return 1.04;
		}
		if (growth < 6) {
			logger.debug("Popularity season expense rate is 1.08");
			return 1.08;
		}
		logger.debug("Popularity season expense rate is 1.14");
		return 1.14;
	}

	private double calculateAverageTeamPopularity() {
		double total = 0.0;
		int teamCount = 0;

		for (Team team : teamRepository.getAllTeams()) {
			logger.trace("Adding team popularity " + team.getCurrentPopularity() + " for " + team.getName());
			total += team.getCurrentPopularity();
			teamCount++;
		}

		if (teamCount == 0) {
			logger.warn("Average team popularity is 0 because no teams are registered");
			return 0.0;
		}
		double averagePopularity = total / teamCount;
		logger.trace("Calculated average team popularity " + averagePopularity + " for " + teamCount + " teams");
		return averagePopularity;
	}
}

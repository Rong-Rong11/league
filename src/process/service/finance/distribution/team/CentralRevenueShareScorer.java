package process.service.finance.distribution.team;

import org.apache.log4j.Logger;

import data.team.Team;
import data.team.finance.economicprofile.EconomicProfile;
import log.LoggerUtility;
import process.utility.FinanceUtility;

public class CentralRevenueShareScorer {
	private static final Logger logger = LoggerUtility.getLogger(CentralRevenueShareScorer.class, "text");

	public static final String TV_SHARE_TYPE = "tv";
	public static final String SPONSORING_SHARE_TYPE = "sponsoring";
	public static final String MERCHANDISING_SHARE_TYPE = "merchandising";

	public double calculateShareScore(Team team, String shareType) {
		if (team == null) {
			logger.warn("Unable to calculate central revenue share score because team is null");
			return 0.0;
		}
		logger.trace("Calculating " + shareType + " central revenue share score for " + team.getName());
		if (TV_SHARE_TYPE.equals(shareType)) {
			return calculateTvShareScore(team);
		}

		if (SPONSORING_SHARE_TYPE.equals(shareType)) {
			return calculateSponsoringShareScore(team);
		}

		if (!MERCHANDISING_SHARE_TYPE.equals(shareType)) {
			logger.warn("Unknown central revenue share type " + shareType + ", using merchandising score");
		}
		double score = FinanceUtility.calculateMerchandisingScore(team);
		logger.debug("Merchandising central revenue share score for " + team.getName() + " is " + score);
		return score;
	}

	private double calculateTvShareScore(Team team) {
		EconomicProfile profil = team.getTeamFinance().getStructure().getEconomicProfile();
		double score = 1.0;
		logger.trace("TV share base score starts at " + score + " for " + team.getName());
		score += team.getCurrentPopularity() / 250.0;
		score += profil.getHistoricalPrestige() * 0.8;
		score += FinanceUtility.getNormalizedTeamValue(team) * 0.7;

		if (team.hasStarPlayer()) {
			logger.trace("Applying TV share star player bonus for " + team.getName());
			score += 0.7;
		}

		logger.debug("TV central revenue share score for " + team.getName() + " is " + score);
		return score;
	}

	private double calculateSponsoringShareScore(Team team) {
		EconomicProfile profil = team.getTeamFinance().getStructure().getEconomicProfile();
		double score = 1.0;
		logger.trace("Sponsoring share base score starts at " + score + " for " + team.getName());
		score += team.getCurrentPopularity() / 200.0;
		score += profil.getCommercialAggressiveness() * 0.8;
		score += profil.getHistoricalPrestige() * 0.5;
		score += FinanceUtility.getNormalizedTeamValue(team) * 0.5;

		if (team.hasStarPlayer()) {
			logger.trace("Applying sponsoring share star player bonus for " + team.getName());
			score += 0.55;
		}

		logger.debug("Sponsoring central revenue share score for " + team.getName() + " is " + score);
		return score;
	}
}

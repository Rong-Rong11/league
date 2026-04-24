package process.service.finance.distribution.team;

import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import process.utility.FinanceUtility;

public class CentralRevenueShareScorer {
	public static final String TV_SHARE_TYPE = "tv";
	public static final String SPONSORING_SHARE_TYPE = "sponsoring";
	public static final String MERCHANDISING_SHARE_TYPE = "merchandising";

	public double calculateShareScore(Team team, String shareType) {
		if (TV_SHARE_TYPE.equals(shareType)) {
			return calculateTvShareScore(team);
		}

		if (SPONSORING_SHARE_TYPE.equals(shareType)) {
			return calculateSponsoringShareScore(team);
		}

		return FinanceUtility.calculateMerchandisingScore(team);
	}

	private double calculateTvShareScore(Team team) {
		EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
		double score = 1.0;
		score += team.getCurrentPopularity() / 250.0;
		score += profil.getHistoricalPrestige() * 0.8;
		score += FinanceUtility.getNormalizedTeamValue(team) * 0.7;

		if (team.hasStarPlayer()) {
			score += 0.7;
		}

		return score;
	}

	private double calculateSponsoringShareScore(Team team) {
		EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
		double score = 1.0;
		score += team.getCurrentPopularity() / 200.0;
		score += profil.getCommercialAggressiveness() * 0.8;
		score += profil.getHistoricalPrestige() * 0.5;
		score += FinanceUtility.getNormalizedTeamValue(team) * 0.5;

		if (team.hasStarPlayer()) {
			score += 0.55;
		}

		return score;
	}
}

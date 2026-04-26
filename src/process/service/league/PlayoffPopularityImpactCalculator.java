package process.service.league;

import data.league.PlayoffRound;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;

public class PlayoffPopularityImpactCalculator {

	public double getPlayoffRoundPopularityBonus(PlayoffRound round) {
		if (round == null) {
			return 0.0;
		}

		switch (round) {
			case FIRST_ROUND:
				return 3.0;
			case CONFERENCE_SEMIFINALS:
				return 4.5;
			case CONFERENCE_FINALS:
				return 6.5;
			case NBA_FINALS:
				return 9.0;
			default:
				return 0.0;
		}
	}

	public double calculateMissedPlayoffPenalty(Team team) {
		EconomicProfil economicProfil = team.getTeamFinance().getStructure().getEconomicProfil();
		MediaMarket mediaMarket = team.getTeamFinance().getStructure().getMediaMarket();
		double currentPopularity = team.getCurrentPopularity();

		if (currentPopularity < 65) {
			return 0.0;
		}

		double penalty = 0.0;

		penalty += (currentPopularity - 65) * 0.08;
		penalty += economicProfil.getHistoricalPrestige() * 1.5;
		penalty += economicProfil.getCommercialAggressiveness() * 1.0;
		penalty += mediaMarket.getPrestigeModifier() * 1.2;
		penalty += mediaMarket.getFanBaseModifier() * 1.0;
		penalty += mediaMarket.getBusinessOpportunityModifier() * 0.8;
		penalty -= economicProfil.getFanLoyalty() * 1.2;

		return Math.max(0.0, penalty);
	}
}

package process.service.finance.distribution.central.calculation;

import java.util.List;

import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import process.utility.FinanceUtility;

public class LeagueFinanceMetricsCalculator {

	public double calculateAveragePopularity(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			total += team.getCurrentPopularity();
		}
		return total / teams.size();
	}

	public double calculateAveragePerformance(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			total += team.getTeamPerformance().getPerformanceRating();
		}
		return total / teams.size();
	}

	public double calculateAverageHistoricalPrestige(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
			total += profil.getHistoricalPrestige();
		}
		return total / teams.size();
	}

	public double calculateAverageFanLoyalty(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
			total += profil.getFanLoyalty();
		}
		return total / teams.size();
	}

	public double calculateAverageCommercialAggressiveness(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
			total += profil.getCommercialAggressiveness();
		}
		return total / teams.size();
	}

	public double calculateAverageBusinessOpportunity(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			MediaMarket mediaMarket = team.getTeamFinance().getMediaMarket();
			total += mediaMarket.getBusinessOpportunityModifier();
		}
		return total / teams.size();
	}

	public double calculateAverageTeamValue(List<Team> teams) {
		double total = 0.0;
		for (Team team : teams) {
			total += FinanceUtility.getNormalizedTeamValue(team);
		}
		return total / teams.size();
	}

	public int countTeamsWithStarPlayer(List<Team> teams) {
		int count = 0;
		for (Team team : teams) {
			if (team.getStarPlayer() != null) {
				count++;
			}
		}
		return count;
	}
}

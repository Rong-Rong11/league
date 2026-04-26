package process.service.finance.distribution.central.calculation;

import java.util.List;

import org.apache.log4j.Logger;

import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import log.LoggerUtility;
import process.utility.FinanceUtility;

public class LeagueFinanceMetricsCalculator {
	private static final Logger logger = LoggerUtility.getLogger(LeagueFinanceMetricsCalculator.class, "text");

	public double calculateAveragePopularity(List<Team> teams) {
		logMetricCalculation("average popularity", teams);
		double total = 0.0;
		for (Team team : teams) {
			logger.trace("Adding popularity " + team.getCurrentPopularity() + " for " + team.getName());
			total += team.getCurrentPopularity();
		}
		double average = total / teams.size();
		logger.debug("Calculated average popularity " + average + " for " + teams.size() + " teams");
		return average;
	}

	public double calculateAveragePerformance(List<Team> teams) {
		logMetricCalculation("average performance", teams);
		double total = 0.0;
		for (Team team : teams) {
			logger.trace("Adding performance rating "
					+ team.getTeamPerformance().getPerformanceRating()
					+ " for "
					+ team.getName());
			total += team.getTeamPerformance().getPerformanceRating();
		}
		double average = total / teams.size();
		logger.debug("Calculated average performance " + average + " for " + teams.size() + " teams");
		return average;
	}

	public double calculateAverageHistoricalPrestige(List<Team> teams) {
		logMetricCalculation("average historical prestige", teams);
		double total = 0.0;
		for (Team team : teams) {
			EconomicProfil profil = team.getTeamFinance().getStructure().getEconomicProfil();
			logger.trace("Adding historical prestige " + profil.getHistoricalPrestige() + " for " + team.getName());
			total += profil.getHistoricalPrestige();
		}
		double average = total / teams.size();
		logger.debug("Calculated average historical prestige " + average + " for " + teams.size() + " teams");
		return average;
	}

	public double calculateAverageFanLoyalty(List<Team> teams) {
		logMetricCalculation("average fan loyalty", teams);
		double total = 0.0;
		for (Team team : teams) {
			EconomicProfil profil = team.getTeamFinance().getStructure().getEconomicProfil();
			logger.trace("Adding fan loyalty " + profil.getFanLoyalty() + " for " + team.getName());
			total += profil.getFanLoyalty();
		}
		double average = total / teams.size();
		logger.debug("Calculated average fan loyalty " + average + " for " + teams.size() + " teams");
		return average;
	}

	public double calculateAverageCommercialAggressiveness(List<Team> teams) {
		logMetricCalculation("average commercial aggressiveness", teams);
		double total = 0.0;
		for (Team team : teams) {
			EconomicProfil profil = team.getTeamFinance().getStructure().getEconomicProfil();
			logger.trace("Adding commercial aggressiveness "
					+ profil.getCommercialAggressiveness()
					+ " for "
					+ team.getName());
			total += profil.getCommercialAggressiveness();
		}
		double average = total / teams.size();
		logger.debug("Calculated average commercial aggressiveness " + average + " for " + teams.size() + " teams");
		return average;
	}

	public double calculateAverageBusinessOpportunity(List<Team> teams) {
		logMetricCalculation("average business opportunity", teams);
		double total = 0.0;
		for (Team team : teams) {
			MediaMarket mediaMarket = team.getTeamFinance().getStructure().getMediaMarket();
			logger.trace("Adding business opportunity modifier "
					+ mediaMarket.getBusinessOpportunityModifier()
					+ " for "
					+ team.getName());
			total += mediaMarket.getBusinessOpportunityModifier();
		}
		double average = total / teams.size();
		logger.debug("Calculated average business opportunity " + average + " for " + teams.size() + " teams");
		return average;
	}

	public double calculateAverageTeamValue(List<Team> teams) {
		logMetricCalculation("average team value", teams);
		double total = 0.0;
		for (Team team : teams) {
			double normalizedTeamValue = FinanceUtility.getNormalizedTeamValue(team);
			logger.trace("Adding normalized team value " + normalizedTeamValue + " for " + team.getName());
			total += normalizedTeamValue;
		}
		double average = total / teams.size();
		logger.debug("Calculated average team value " + average + " for " + teams.size() + " teams");
		return average;
	}

	public int countTeamsWithStarPlayer(List<Team> teams) {
		logMetricCalculation("teams with star player", teams);
		int count = 0;
		for (Team team : teams) {
			if (team.getStarPlayer() != null) {
				logger.trace("Counting star player for " + team.getName());
				count++;
			}
		}
		logger.debug("Counted " + count + " teams with a star player out of " + teams.size());
		return count;
	}

	private void logMetricCalculation(String metricName, List<Team> teams) {
		if (teams == null) {
			logger.warn("Calculating " + metricName + " with null teams list");
			return;
		}
		if (teams.isEmpty()) {
			logger.warn("Calculating " + metricName + " with empty teams list");
			return;
		}
		logger.trace("Calculating " + metricName + " for " + teams.size() + " teams");
	}
}

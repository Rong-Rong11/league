package process.service.finance.distribution.central.calculation;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.league.League;
import data.league.finance.CentralRevenueProfile;
import data.league.finance.CentralRevenueSeasonDynamics;
import data.team.Team;
import log.LoggerUtility;
import process.repository.TeamRepository;
import process.service.finance.FinanceManager;

public class MonthlyCentralRevenueCalculator {
	private static final Logger logger = LoggerUtility.getLogger(MonthlyCentralRevenueCalculator.class, "text");

	private final League league;
	private final TeamRepository teamRepository = TeamRepository.getInstance();
	private final LeagueFinanceMetricsCalculator metricsCalculator = new LeagueFinanceMetricsCalculator();
	private final MonthlyGameRevenueAnalyzer gameRevenueAnalyzer;
	private final MonthlyRevenueRateCalculator rateCalculator;
	private final MonthlyRevenueBonusCalculator bonusCalculator;

	public MonthlyCentralRevenueCalculator(League league) {
		this.league = league;
		this.gameRevenueAnalyzer = new MonthlyGameRevenueAnalyzer(league);
		this.rateCalculator = new MonthlyRevenueRateCalculator(gameRevenueAnalyzer, getSeasonDynamics());
		this.bonusCalculator = new MonthlyRevenueBonusCalculator(gameRevenueAnalyzer);
	}

	public void setFinanceManager(FinanceManager financeManager) {
		logger.debug("Setting finance manager for monthly central revenue calculator");
		gameRevenueAnalyzer.setFinanceManager(financeManager);
	}

	public double calculateNationalTvRevenue(CentralRevenueProfile profile, int month) {
		logger.debug("Calculating national TV revenue for month " + month);
		ArrayList<Team> teams = teamRepository.getAllTeams();
		int teamCount = teams.size();
		if (profile == null) {
			logger.warn("Calculating national TV revenue with null revenue profile");
		}
		logger.debug("National TV revenue calculation uses " + teamCount + " teams");

		double averagePopularity = metricsCalculator.calculateAveragePopularity(teams);
		double averagePerformance = metricsCalculator.calculateAveragePerformance(teams);
		double averagePrestige = metricsCalculator.calculateAverageHistoricalPrestige(teams);
		double averageTeamValue = metricsCalculator.calculateAverageTeamValue(teams);
		int starTeams = metricsCalculator.countTeamsWithStarPlayer(teams);
		logger.debug("TV revenue metrics: popularity="
				+ averagePopularity
				+ ", performance="
				+ averagePerformance
				+ ", prestige="
				+ averagePrestige
				+ ", teamValue="
				+ averageTeamValue
				+ ", starTeams="
				+ starTeams);

		double revenue = (0.37 * teamCount)
				+ (averagePopularity * 0.050)
				+ (averagePerformance * 0.82)
				+ (averagePrestige * 1.18)
				+ (averageTeamValue * 1.42)
				+ (starTeams * 0.10);
		logger.trace("Base national TV revenue before rates is " + revenue);

		CentralRevenueSeasonDynamics seasonDynamics = getSeasonDynamics();
		revenue *= profile.getTvRate();
		revenue *= amplifySeasonRate(seasonDynamics.getTvSeasonRate(), 2.35);
		revenue *= amplifySeasonRate(seasonDynamics.getLeagueSeasonMediaMomentum(), 1.45);
		revenue *= getPopularityGainRate(teams, seasonDynamics, 0.55);
		revenue *= rateCalculator.getLeagueMonthlyAttractivenessRate(month);
		revenue *= rateCalculator.getImportantGamesRevenueRate(month, 0.0024);
		revenue *= rateCalculator.getPlayoffGamesRevenueRate(month, 0.0030);
		revenue *= rateCalculator.getPremiumGamesRevenueRate(month, 0.0052);
		revenue *= rateCalculator.getHighAttendanceRevenueRate(month, 0.0032);
		revenue *= rateCalculator.getStarDrivenRevenueRate(month, 0.0018, 0.0015, 0.0036);
		revenue *= rateCalculator.getActivePlayoffTeamsRate(month, 0.0054);
		revenue *= rateCalculator.getSeasonMomentumRate(month, 0.10);
		revenue *= rateCalculator.getControlledEconomicNoise(month, 0.165);
		revenue *= rateCalculator.getRevenueTypeMonthlyRate(month, 0.018, 0.010, 0.0);
		revenue += bonusCalculator.getLeagueMonthlyAdditiveBonus(month) * 0.15;
		logger.debug("Calculated national TV revenue " + revenue + " for month " + month);

		return revenue;
	}

	public double calculateNationalSponsoringRevenue(CentralRevenueProfile profile, int month) {
		logger.debug("Calculating national sponsoring revenue for month " + month);
		ArrayList<Team> teams = teamRepository.getAllTeams();
		int teamCount = teams.size();
		if (profile == null) {
			logger.warn("Calculating national sponsoring revenue with null revenue profile");
		}
		logger.debug("National sponsoring revenue calculation uses " + teamCount + " teams");

		double averagePopularity = metricsCalculator.calculateAveragePopularity(teams);
		double averageCommercialAggressiveness = metricsCalculator.calculateAverageCommercialAggressiveness(teams);
		double averageBusinessOpportunity = metricsCalculator.calculateAverageBusinessOpportunity(teams);
		double averageTeamValue = metricsCalculator.calculateAverageTeamValue(teams);
		int starTeams = metricsCalculator.countTeamsWithStarPlayer(teams);
		logger.debug("Sponsoring revenue metrics: popularity="
				+ averagePopularity
				+ ", commercialAggressiveness="
				+ averageCommercialAggressiveness
				+ ", businessOpportunity="
				+ averageBusinessOpportunity
				+ ", teamValue="
				+ averageTeamValue
				+ ", starTeams="
				+ starTeams);

		double revenue = (0.16 * teamCount)
				+ (averagePopularity * 0.040)
				+ (averageCommercialAggressiveness * 0.96)
				+ (averageBusinessOpportunity * 0.84)
				+ (averageTeamValue * 0.80)
				+ (starTeams * 0.07);
		logger.trace("Base national sponsoring revenue before rates is " + revenue);

		CentralRevenueSeasonDynamics seasonDynamics = getSeasonDynamics();
		revenue *= profile.getSponsoringRate();
		revenue *= amplifySeasonRate(seasonDynamics.getSponsoringSeasonRate(), 2.35);
		revenue *= amplifySeasonRate(seasonDynamics.getCentralMarketCycle(), 1.45);
		revenue *= getPopularityGainRate(teams, seasonDynamics, 0.70);
		revenue *= rateCalculator.getLeagueMonthlyAttractivenessRate(month);
		revenue *= rateCalculator.getImportantGamesRevenueRate(month, 0.0030);
		revenue *= rateCalculator.getPlayoffGamesRevenueRate(month, 0.0028);
		revenue *= rateCalculator.getPremiumGamesRevenueRate(month, 0.0060);
		revenue *= rateCalculator.getHighAttendanceRevenueRate(month, 0.0024);
		revenue *= rateCalculator.getStarDrivenRevenueRate(month, 0.0018, 0.0016, 0.0035);
		revenue *= rateCalculator.getActivePlayoffTeamsRate(month, 0.0050);
		revenue *= rateCalculator.getSeasonMomentumRate(month, 0.12);
		revenue *= rateCalculator.getControlledEconomicNoise(month, 0.220);
		revenue *= rateCalculator.getRevenueTypeMonthlyRate(month, 0.040, 0.022, 0.7);
		revenue += bonusCalculator.getLeagueMonthlyAdditiveBonus(month) * 0.13;
		logger.debug("Calculated national sponsoring revenue " + revenue + " for month " + month);

		return revenue;
	}

	public double calculateNationalMerchandisingRevenue(CentralRevenueProfile profile, int month) {
		logger.debug("Calculating national merchandising revenue for month " + month);
		ArrayList<Team> teams = teamRepository.getAllTeams();
		int teamCount = teams.size();
		if (profile == null) {
			logger.warn("Calculating national merchandising revenue with null revenue profile");
		}
		logger.debug("National merchandising revenue calculation uses " + teamCount + " teams");

		double averagePopularity = metricsCalculator.calculateAveragePopularity(teams);
		double averageFanLoyalty = metricsCalculator.calculateAverageFanLoyalty(teams);
		double averagePrestige = metricsCalculator.calculateAverageHistoricalPrestige(teams);
		double averageTeamValue = metricsCalculator.calculateAverageTeamValue(teams);
		int starTeams = metricsCalculator.countTeamsWithStarPlayer(teams);
		logger.debug("Merchandising revenue metrics: popularity="
				+ averagePopularity
				+ ", fanLoyalty="
				+ averageFanLoyalty
				+ ", prestige="
				+ averagePrestige
				+ ", teamValue="
				+ averageTeamValue
				+ ", starTeams="
				+ starTeams);

		double revenue = (0.08 * teamCount)
				+ (averagePopularity * 0.031)
				+ (averageFanLoyalty * 0.86)
				+ (averagePrestige * 0.68)
				+ (averageTeamValue * 0.48)
				+ (starTeams * 0.06);
		logger.trace("Base national merchandising revenue before rates is " + revenue);

		CentralRevenueSeasonDynamics seasonDynamics = getSeasonDynamics();
		revenue *= profile.getMerchandisingRate();
		revenue *= amplifySeasonRate(seasonDynamics.getMerchandisingSeasonRate(), 2.45);
		revenue *= amplifySeasonRate((seasonDynamics.getLeagueSeasonMediaMomentum()
				+ seasonDynamics.getCentralMarketCycle()) / 2.0, 1.35);
		revenue *= getPopularityGainRate(teams, seasonDynamics, 0.82);
		revenue *= rateCalculator.getLeagueMonthlyAttractivenessRate(month);
		revenue *= rateCalculator.getImportantGamesRevenueRate(month, 0.0040);
		revenue *= rateCalculator.getPlayoffGamesRevenueRate(month, 0.0036);
		revenue *= rateCalculator.getPremiumGamesRevenueRate(month, 0.0072);
		revenue *= rateCalculator.getHighAttendanceRevenueRate(month, 0.0048);
		revenue *= rateCalculator.getStarDrivenRevenueRate(month, 0.0021, 0.0023, 0.0045);
		revenue *= rateCalculator.getActivePlayoffTeamsRate(month, 0.0062);
		revenue *= rateCalculator.getSeasonMomentumRate(month, 0.16);
		revenue *= rateCalculator.getControlledEconomicNoise(month, 0.285);
		revenue *= rateCalculator.getRevenueTypeMonthlyRate(month, 0.065, 0.032, 1.4);
		revenue += bonusCalculator.getLeagueMonthlyAdditiveBonus(month) * 0.10;
		logger.debug("Calculated national merchandising revenue " + revenue + " for month " + month);

		return revenue;
	}

	private CentralRevenueSeasonDynamics getSeasonDynamics() {
		if (league == null || league.getLeagueFinance() == null || league.getLeagueFinance().getCentralRevenueSeasonDynamics() == null) {
			return new CentralRevenueSeasonDynamics(1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.08, 50.0, 1.0, 0.0);
		}
		return league.getLeagueFinance().getCentralRevenueSeasonDynamics();
	}

	private double amplifySeasonRate(double rate, double factor) {
		return 1.0 + ((rate - 1.0) * factor);
	}

	private double getPopularityGainRate(ArrayList<Team> teams, CentralRevenueSeasonDynamics seasonDynamics,
			double gainWeight) {
		if (teams == null || teams.isEmpty() || seasonDynamics == null) {
			return 1.0;
		}
		double currentAveragePopularity = metricsCalculator.calculateAveragePopularity(teams);
		double popularityGain = currentAveragePopularity - seasonDynamics.getBaselineAveragePopularity();
		double normalizedGain = popularityGain / 20.0;
		double rate = 1.0 + (normalizedGain * gainWeight);
		if (rate < 0.80) {
			return 0.80;
		}
		if (rate > 1.28) {
			return 1.28;
		}
		return rate;
	}
}

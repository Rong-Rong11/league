package process.service.finance.expense;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import data.team.Team;
import log.LoggerUtility;
import process.service.finance.distribution.central.calculation.LeagueFinanceMetricsCalculator;
import process.utility.CalendarUtility;

public class LeagueExpenseCostCalculator {
	private static final Logger logger = LoggerUtility.getLogger(LeagueExpenseCostCalculator.class, "text");

	private LeagueExpenseRateCalculator rateCalculator;
	private LeagueFinanceMetricsCalculator metricsCalculator = new LeagueFinanceMetricsCalculator();
	private List<Team> teams;
	private static final double LEAGUE_EXPENSE_PRESSURE = 1.10;

	public LeagueExpenseCostCalculator(LeagueExpenseRateCalculator rateCalculator) {
		this(rateCalculator, new ArrayList<Team>());
	}

	public LeagueExpenseCostCalculator(LeagueExpenseRateCalculator rateCalculator, List<Team> teams) {
		this.rateCalculator = rateCalculator;
		this.teams = teams;
	}

	public double calculateAdministrativeCost() {
		double cost = FinanceConfiguration.LEAGUE_ADMINISTRATIVE_COST * 1.28 * LEAGUE_EXPENSE_PRESSURE;
		cost *= getMarketPowerExpenseRate(0.24, 0.96, 1.12);
		cost *= getAverageBasedExpenseRate(getAverageCommercialAggressiveness(), 0.5, 0.18, 0.96, 1.12);
		cost *= getAverageBasedExpenseRate(getAverageBusinessOpportunity(), 0.20, 0.22, 0.96, 1.10);
		logger.debug("Calculated administrative league cost " + cost);
		return cost;
	}

	public double calculateMediaCost(int month) {
		double cost = FinanceConfiguration.LEAGUE_MEDIA_COST * 0.84 * LEAGUE_EXPENSE_PRESSURE;
		logger.trace("Base media league cost for month " + month + " is " + cost);
		if (CalendarUtility.isImportantMonth(month)) {
			logger.trace("Applying important month media cost multiplier for month " + month);
			cost *= 1.04;
		}
		cost *= getMarketPowerExpenseRate(0.36, 0.92, 1.18);
		cost *= getAverageBasedExpenseRate(getAverageMediaFanBase(), 0.20, 0.48, 0.94, 1.20);
		cost *= getAverageBasedExpenseRate(getAverageMediaPrestige(), 0.12, 0.38, 0.96, 1.16);
		cost *= rateCalculator.getImportantGamesExpenseRate(month, 0.010);
		cost *= rateCalculator.getPlayoffGamesExpenseRate(month, 0.015);
		cost *= rateCalculator.getPremiumGamesExpenseRate(month, 0.009);
		cost *= rateCalculator.getHighAttendanceExpenseRate(month, 0.006);
		cost *= rateCalculator.getStarRivalryExpenseRate(month, 0.010);
		cost *= rateCalculator.getActivePlayoffTeamsExpenseRate(month, 0.007);
		cost *= rateCalculator.getSeasonExpenseRate(month, 0.08);
		cost *= rateCalculator.getPopularitySeasonExpenseRate();
		cost *= rateCalculator.getControlledEconomicNoise(month, 0.055);
		logger.debug("Calculated media league cost " + cost + " for month " + month);
		return cost;
	}

	public double calculateMarketingCost(int month) {
		double cost = FinanceConfiguration.LEAGUE_MARKETING_COST * 1.10 * LEAGUE_EXPENSE_PRESSURE;
		logger.trace("Base marketing league cost for month " + month + " is " + cost);
		if (CalendarUtility.isImportantMonth(month)) {
			logger.trace("Applying important month marketing cost multiplier for month " + month);
			cost *= 1.05;
		}
		cost *= getMarketPowerExpenseRate(0.26, 0.96, 1.18);
		cost *= getSmallMarketDevelopmentRate(0.25);
		cost *= getWeakEconomicSupportRate(getAverageCommercialAggressiveness(), 0.5, 0.30, 1.00, 1.15);
		cost *= getWeakEconomicSupportRate(getAverageFanLoyalty(), 0.5, 0.24, 1.00, 1.12);
		cost *= rateCalculator.getImportantGamesExpenseRate(month, 0.012);
		cost *= rateCalculator.getPlayoffGamesExpenseRate(month, 0.017);
		cost *= rateCalculator.getPremiumGamesExpenseRate(month, 0.011);
		cost *= rateCalculator.getHighAttendanceExpenseRate(month, 0.007);
		cost *= rateCalculator.getStarRivalryExpenseRate(month, 0.012);
		cost *= rateCalculator.getActivePlayoffTeamsExpenseRate(month, 0.008);
		cost *= rateCalculator.getSeasonExpenseRate(month, 0.10);
		cost *= rateCalculator.getPopularitySeasonExpenseRate();
		cost *= rateCalculator.getControlledEconomicNoise(month, 0.060);
		logger.debug("Calculated marketing league cost " + cost + " for month " + month);
		return cost;
	}

	public double calculateOfficiatingCost(int month) {
		double cost = FinanceConfiguration.LEAGUE_OFFICIATING_COST * 1.02 * LEAGUE_EXPENSE_PRESSURE;
		logger.trace("Base officiating league cost for month " + month + " is " + cost);
		if (CalendarUtility.isImportantMonth(month)) {
			logger.trace("Applying important month officiating cost multiplier for month " + month);
			cost *= 1.03;
		}
		cost *= getMarketPowerExpenseRate(0.10, 0.98, 1.06);
		cost *= rateCalculator.getImportantGamesExpenseRate(month, 0.007);
		cost *= rateCalculator.getPlayoffGamesExpenseRate(month, 0.010);
		cost *= rateCalculator.getPremiumGamesExpenseRate(month, 0.005);
		cost *= rateCalculator.getHighAttendanceExpenseRate(month, 0.004);
		cost *= rateCalculator.getStarRivalryExpenseRate(month, 0.006);
		cost *= rateCalculator.getActivePlayoffTeamsExpenseRate(month, 0.0045);
		cost *= rateCalculator.getSeasonExpenseRate(month, 0.08);
		cost *= rateCalculator.getControlledEconomicNoise(month, 0.045);
		logger.debug("Calculated officiating league cost " + cost + " for month " + month);
		return cost;
	}

	private double getMarketPowerExpenseRate(double weight, double min, double max) {
		double marketPowerIndex = getMarketPowerIndex();
		double rate = 1.0 + ((marketPowerIndex - 1.0) * weight);
		return clamp(rate, min, max);
	}

	private double getAverageBasedExpenseRate(double average, double baseline, double weight, double min, double max) {
		double rate = 1.0 + ((average - baseline) * weight);
		return clamp(rate, min, max);
	}

	private double getSmallMarketDevelopmentRate(double maxPressure) {
		if (teams == null || teams.isEmpty()) {
			return 1.0;
		}
		double smallMarketRatio = (double) metricsCalculator.countSmallMarketTeams(teams) / teams.size();
		return 1.0 + (smallMarketRatio * maxPressure);
	}

	private double getWeakEconomicSupportRate(double average, double baseline, double weight, double min, double max) {
		double weakness = baseline - average;
		if (weakness <= 0.0) {
			return 1.0;
		}
		return clamp(1.0 + (weakness * weight), min, max);
	}

	private double getMarketPowerIndex() {
		if (teams == null || teams.isEmpty()) {
			return 1.0;
		}
		return metricsCalculator.calculateMarketPowerIndex(teams);
	}

	private double getAverageCommercialAggressiveness() {
		if (teams == null || teams.isEmpty()) {
			return 0.5;
		}
		return metricsCalculator.calculateAverageCommercialAggressiveness(teams);
	}

	private double getAverageFanLoyalty() {
		if (teams == null || teams.isEmpty()) {
			return 0.5;
		}
		return metricsCalculator.calculateAverageFanLoyalty(teams);
	}

	private double getAverageBusinessOpportunity() {
		if (teams == null || teams.isEmpty()) {
			return 0.20;
		}
		return metricsCalculator.calculateAverageBusinessOpportunity(teams);
	}

	private double getAverageMediaFanBase() {
		if (teams == null || teams.isEmpty()) {
			return 0.20;
		}
		return metricsCalculator.calculateAverageMediaFanBase(teams);
	}

	private double getAverageMediaPrestige() {
		if (teams == null || teams.isEmpty()) {
			return 0.12;
		}
		return metricsCalculator.calculateAverageMediaPrestige(teams);
	}

	private double clamp(double value, double min, double max) {
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}
}

package process.service.finance.distribution.central.calculation;

import java.util.ArrayList;

import data.league.League;
import data.team.Team;
import process.repository.TeamRepository;
import process.service.finance.distribution.central.profile.CentralRevenueProfile;
import process.service.finance.FinanceManager;

public class MonthlyCentralRevenueCalculator {

	private final League league;
	private final TeamRepository teamRepository = TeamRepository.getInstance();
	private final LeagueFinanceMetricsCalculator metricsCalculator = new LeagueFinanceMetricsCalculator();
	private final MonthlyGameRevenueAnalyzer gameRevenueAnalyzer;
	private final MonthlyRevenueRateCalculator rateCalculator;
	private final MonthlyRevenueBonusCalculator bonusCalculator;

	public MonthlyCentralRevenueCalculator(League league) {
		this.league = league;
		this.gameRevenueAnalyzer = new MonthlyGameRevenueAnalyzer(league);
		this.rateCalculator = new MonthlyRevenueRateCalculator(gameRevenueAnalyzer);
		this.bonusCalculator = new MonthlyRevenueBonusCalculator(gameRevenueAnalyzer);
	}

	public void setFinanceManager(FinanceManager financeManager) {
		gameRevenueAnalyzer.setFinanceManager(financeManager);
	}

	public double calculateNationalTvRevenue(CentralRevenueProfile profile, int month) {
		ArrayList<Team> teams = teamRepository.getAllTeams();
		int teamCount = teams.size();

		double averagePopularity = metricsCalculator.calculateAveragePopularity(teams);
		double averagePerformance = metricsCalculator.calculateAveragePerformance(teams);
		double averagePrestige = metricsCalculator.calculateAverageHistoricalPrestige(teams);
		double averageTeamValue = metricsCalculator.calculateAverageTeamValue(teams);
		int starTeams = metricsCalculator.countTeamsWithStarPlayer(teams);

		double revenue = (0.58 * teamCount)
				+ (averagePopularity * 0.080)
				+ (averagePerformance * 1.32)
				+ (averagePrestige * 1.90)
				+ (averageTeamValue * 2.30)
				+ (starTeams * 0.17);

		revenue *= profile.getTvRate();
		revenue *= rateCalculator.getLeagueMonthlyAttractivenessRate(month);
		revenue *= rateCalculator.getImportantGamesRevenueRate(month, 0.0040);
		revenue *= rateCalculator.getPlayoffGamesRevenueRate(month, 0.0045);
		revenue *= rateCalculator.getActivePlayoffTeamsRate(month, 0.0038);
		revenue *= rateCalculator.getSeasonMomentumRate(month, 0.10);
		revenue *= rateCalculator.getControlledEconomicNoise(month, 0.165);
		revenue *= rateCalculator.getRevenueTypeMonthlyRate(month, 0.018, 0.010, 0.0);
		revenue += bonusCalculator.getLeagueMonthlyAdditiveBonus(month) * 0.33;

		return revenue;
	}

	public double calculateNationalSponsoringRevenue(CentralRevenueProfile profile, int month) {
		ArrayList<Team> teams = teamRepository.getAllTeams();
		int teamCount = teams.size();

		double averagePopularity = metricsCalculator.calculateAveragePopularity(teams);
		double averageCommercialAggressiveness = metricsCalculator.calculateAverageCommercialAggressiveness(teams);
		double averageBusinessOpportunity = metricsCalculator.calculateAverageBusinessOpportunity(teams);
		double averageTeamValue = metricsCalculator.calculateAverageTeamValue(teams);
		int starTeams = metricsCalculator.countTeamsWithStarPlayer(teams);

		double revenue = (0.27 * teamCount)
				+ (averagePopularity * 0.062)
				+ (averageCommercialAggressiveness * 1.54)
				+ (averageBusinessOpportunity * 1.34)
				+ (averageTeamValue * 1.32)
				+ (starTeams * 0.12);

		revenue *= profile.getSponsoringRate();
		revenue *= rateCalculator.getLeagueMonthlyAttractivenessRate(month);
		revenue *= rateCalculator.getImportantGamesRevenueRate(month, 0.0055);
		revenue *= rateCalculator.getPlayoffGamesRevenueRate(month, 0.0043);
		revenue *= rateCalculator.getActivePlayoffTeamsRate(month, 0.0036);
		revenue *= rateCalculator.getSeasonMomentumRate(month, 0.12);
		revenue *= rateCalculator.getControlledEconomicNoise(month, 0.220);
		revenue *= rateCalculator.getRevenueTypeMonthlyRate(month, 0.040, 0.022, 0.7);
		revenue += bonusCalculator.getLeagueMonthlyAdditiveBonus(month) * 0.29;

		return revenue;
	}

	public double calculateNationalMerchandisingRevenue(CentralRevenueProfile profile, int month) {
		ArrayList<Team> teams = teamRepository.getAllTeams();
		int teamCount = teams.size();

		double averagePopularity = metricsCalculator.calculateAveragePopularity(teams);
		double averageFanLoyalty = metricsCalculator.calculateAverageFanLoyalty(teams);
		double averagePrestige = metricsCalculator.calculateAverageHistoricalPrestige(teams);
		double averageTeamValue = metricsCalculator.calculateAverageTeamValue(teams);
		int starTeams = metricsCalculator.countTeamsWithStarPlayer(teams);

		double revenue = (0.15 * teamCount)
				+ (averagePopularity * 0.049)
				+ (averageFanLoyalty * 1.42)
				+ (averagePrestige * 1.10)
				+ (averageTeamValue * 0.80)
				+ (starTeams * 0.11);

		revenue *= profile.getMerchandisingRate();
		revenue *= rateCalculator.getLeagueMonthlyAttractivenessRate(month);
		revenue *= rateCalculator.getImportantGamesRevenueRate(month, 0.0075);
		revenue *= rateCalculator.getPlayoffGamesRevenueRate(month, 0.0060);
		revenue *= rateCalculator.getActivePlayoffTeamsRate(month, 0.0048);
		revenue *= rateCalculator.getSeasonMomentumRate(month, 0.16);
		revenue *= rateCalculator.getControlledEconomicNoise(month, 0.285);
		revenue *= rateCalculator.getRevenueTypeMonthlyRate(month, 0.065, 0.032, 1.4);
		revenue += bonusCalculator.getLeagueMonthlyAdditiveBonus(month) * 0.21;

		return revenue;
	}
}

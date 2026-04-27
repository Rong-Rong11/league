package process.service.finance.expense;

import org.apache.log4j.Logger;

import data.league.finance.CentralRevenueSeasonDynamics;
import log.LoggerUtility;
import process.utility.CalendarUtility;

public class LeagueExpenseRateCalculator {
	private static final Logger logger = LoggerUtility.getLogger(LeagueExpenseRateCalculator.class, "text");

	private LeagueExpenseGameAnalyzer gameAnalyzer;
	private LeaguePopularityExpenseTracker popularityTracker;
	private CentralRevenueSeasonDynamics seasonDynamics;

	public LeagueExpenseRateCalculator(LeagueExpenseGameAnalyzer gameAnalyzer,
			LeaguePopularityExpenseTracker popularityTracker,
			CentralRevenueSeasonDynamics seasonDynamics) {
		this.gameAnalyzer = gameAnalyzer;
		this.popularityTracker = popularityTracker;
		this.seasonDynamics = seasonDynamics;
	}

	public double getImportantGamesExpenseRate(int month, double ratePerGame) {
		int importantGames = gameAnalyzer.countImportantGamesInMonth(month);
		double rate = 1 + (importantGames * ratePerGame);
		logger.trace("Important games expense rate is "
				+ rate
				+ " for month "
				+ month
				+ " with "
				+ importantGames
				+ " important games");
		return rate;
	}

	public double getPlayoffGamesExpenseRate(int month, double ratePerGame) {
		int playoffGames = gameAnalyzer.countPlayoffGamesInMonth(month);
		double rate = 1 + (playoffGames * ratePerGame);
		logger.trace("Playoff games expense rate is "
				+ rate
				+ " for month "
				+ month
				+ " with "
				+ playoffGames
				+ " playoff games");
		return rate;
	}

	public double getActivePlayoffTeamsExpenseRate(int month, double ratePerTeam) {
		if (!isPlayoffMonth(month)) {
			logger.trace("Active playoff teams expense rate is 1.0 because month " + month + " is not a playoff month");
			return 1.0;
		}
		int activePlayoffTeams = gameAnalyzer.countActivePlayoffTeams();
		double rate = 1 + (activePlayoffTeams * ratePerTeam);
		logger.trace("Active playoff teams expense rate is "
				+ rate
				+ " for month "
				+ month
				+ " with "
				+ activePlayoffTeams
				+ " active teams");
		return rate;
	}

	public double getSeasonExpenseRate(int month, double playoffBonusRate) {
		if (isPlayoffMonth(month)) {
			int activePlayoffTeams = gameAnalyzer.countActivePlayoffTeams();
			double rate = 1 + (playoffBonusRate * 0.70) + (activePlayoffTeams * 0.0060);
			logger.trace("Season expense rate is " + rate + " for playoff month " + month);
			return rate;
		}
		if (CalendarUtility.isImportantMonth(month)) {
			double rate = seasonDynamics == null ? 1.08 : seasonDynamics.getImportantMonthRate();
			logger.trace("Season expense rate is " + rate + " for important month " + month);
			return rate;
		}
		logger.trace("Season expense rate is 1.0 for month " + month);
		return 1.0;
	}

	public double getControlledEconomicNoise(int month, double maxAmplitude) {
		int importantGames = gameAnalyzer.countImportantGamesInMonth(month);
		int premiumGames = gameAnalyzer.countPremiumGamesInMonth(month);
		int highAttendanceGames = gameAnalyzer.countHighAttendanceGamesInMonth(month);
		int playoffGames = gameAnalyzer.countPlayoffGamesInMonth(month);
		int activeTeams = gameAnalyzer.countActivePlayoffTeams();
		double phaseShift = seasonDynamics == null ? 0.0 : seasonDynamics.getLeagueExpenseNoisePhaseShift();
		double wave = Math.cos((month * 1.09)
				+ phaseShift
				+ (importantGames * 0.10)
				+ (premiumGames * 0.15)
				+ (highAttendanceGames * 0.12)
				+ (playoffGames * 0.18)
				+ (activeTeams * 0.15));
		double rate = 1 + (wave * maxAmplitude);
		logger.trace("Controlled league expense noise wave is "
				+ wave
				+ " for month="
				+ month
				+ ", importantGames="
				+ importantGames
				+ ", premiumGames="
				+ premiumGames
				+ ", highAttendanceGames="
				+ highAttendanceGames
				+ ", playoffGames="
				+ playoffGames
				+ ", activeTeams="
				+ activeTeams);
		logger.trace("Controlled league expense noise rate is " + rate + " for month " + month);
		return rate;
	}

	public double getPopularitySeasonExpenseRate() {
		double rate = popularityTracker.getPopularitySeasonExpenseRate();
		logger.trace("Popularity season expense rate is " + rate);
		return rate;
	}

	public double getPremiumGamesExpenseRate(int month, double ratePerGame) {
		int premiumGames = gameAnalyzer.countPremiumGamesInMonth(month);
		double rate = 1 + (premiumGames * ratePerGame);
		logger.trace("Premium games expense rate is " + rate + " for month " + month + " with " + premiumGames
				+ " premium games");
		return rate;
	}

	public double getHighAttendanceExpenseRate(int month, double ratePerGame) {
		int highAttendanceGames = gameAnalyzer.countHighAttendanceGamesInMonth(month);
		double rate = 1 + (highAttendanceGames * ratePerGame);
		logger.trace("High attendance expense rate is "
				+ rate
				+ " for month "
				+ month
				+ " with "
				+ highAttendanceGames
				+ " high attendance games");
		return rate;
	}

	public double getStarRivalryExpenseRate(int month, double ratePerGame) {
		int starRivalryGames = gameAnalyzer.countStarRivalryGamesInMonth(month);
		double rate = 1 + (starRivalryGames * ratePerGame);
		logger.trace("Star rivalry expense rate is "
				+ rate
				+ " for month "
				+ month
				+ " with "
				+ starRivalryGames
				+ " star rivalry games");
		return rate;
	}

	private boolean isPlayoffMonth(int month) {
		boolean playoffMonth = month >= 8;
		if (playoffMonth) {
			logger.trace("Month " + month + " is a playoff month for league expenses");
		}
		return playoffMonth;
	}
}

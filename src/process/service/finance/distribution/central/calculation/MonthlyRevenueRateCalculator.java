package process.service.finance.distribution.central.calculation;

import org.apache.log4j.Logger;

import log.LoggerUtility;
import process.utility.CalendarUtility;

public class MonthlyRevenueRateCalculator {
	private static final Logger logger = LoggerUtility.getLogger(MonthlyRevenueRateCalculator.class, "text");

	private MonthlyGameRevenueAnalyzer gameRevenueAnalyzer;

	public MonthlyRevenueRateCalculator(MonthlyGameRevenueAnalyzer gameRevenueAnalyzer) {
		this.gameRevenueAnalyzer = gameRevenueAnalyzer;
	}

	public double getLeagueMonthlyAttractivenessRate(int month) {
		double attractiveness = gameRevenueAnalyzer.calculateMonthlyLeagueAttractiveness(month);

		if (attractiveness < 60) {
			logger.trace("League monthly attractiveness rate is 0.60 for month " + month);
			return 0.60;
		}
		if (attractiveness < 74) {
			logger.trace("League monthly attractiveness rate is 0.78 for month " + month);
			return 0.78;
		}
		if (attractiveness < 90) {
			logger.trace("League monthly attractiveness rate is 1.00 for month " + month);
			return 1.00;
		}
		if (attractiveness < 108) {
			logger.trace("League monthly attractiveness rate is 1.24 for month " + month);
			return 1.24;
		}
		logger.trace("League monthly attractiveness rate is 1.52 for month " + month);
		return 1.52;
	}

	public double getImportantGamesRevenueRate(int month, double ratePerGame) {
		int importantGames = gameRevenueAnalyzer.countImportantGamesInMonth(month);
		double rate = 1 + (importantGames * ratePerGame);
		logger.trace("Important games revenue rate is "
				+ rate
				+ " for month "
				+ month
				+ " with "
				+ importantGames
				+ " important games");
		return rate;
	}

	public double getPlayoffGamesRevenueRate(int month, double ratePerGame) {
		int playoffGames = gameRevenueAnalyzer.countPlayoffGamesInMonth(month);
		double rate = 1 + (playoffGames * ratePerGame);
		logger.trace("Playoff games revenue rate is "
				+ rate
				+ " for month "
				+ month
				+ " with "
				+ playoffGames
				+ " playoff games");
		return rate;
	}

	public double getActivePlayoffTeamsRate(int month, double ratePerTeam) {
		if (!isPlayoffMonth(month)) {
			logger.trace("Active playoff teams rate is 1.0 because month " + month + " is not a playoff month");
			return 1.0;
		}
		int activePlayoffTeams = gameRevenueAnalyzer.countActivePlayoffTeams();
		double rate = 1 + (activePlayoffTeams * ratePerTeam);
		logger.trace("Active playoff teams revenue rate is "
				+ rate
				+ " for month "
				+ month
				+ " with "
				+ activePlayoffTeams
				+ " active teams");
		return rate;
	}

	public double getSeasonMomentumRate(int month, double playoffBonusRate) {
		if (isPlayoffMonth(month)) {
			double rate = 1 + playoffBonusRate;
			logger.trace("Season momentum rate is " + rate + " for playoff month " + month);
			return rate;
		}
		if (CalendarUtility.isImportantMonth(month)) {
			logger.trace("Season momentum rate is 1.28 for important month " + month);
			return 1.28;
		}
		logger.trace("Season momentum rate is 1.0 for month " + month);
		return 1.0;
	}

	public double getControlledEconomicNoise(int month, double maxAmplitude) {
		int importantGames = gameRevenueAnalyzer.countImportantGamesInMonth(month);
		int playoffGames = gameRevenueAnalyzer.countPlayoffGamesInMonth(month);
		int activeTeams = gameRevenueAnalyzer.countActivePlayoffTeams();
		double wave = Math.sin((month * 1.73) + (importantGames * 0.11) + (playoffGames * 0.23) + (activeTeams * 0.19));
		double rate = 1 + (wave * maxAmplitude);
		logger.trace("Controlled economic noise wave is "
				+ wave
				+ " for month="
				+ month
				+ ", importantGames="
				+ importantGames
				+ ", playoffGames="
				+ playoffGames
				+ ", activeTeams="
				+ activeTeams);
		logger.trace("Controlled economic noise rate is " + rate + " for month " + month);
		return rate;
	}

	public double getRevenueTypeMonthlyRate(int month, double primaryAmplitude, double secondaryAmplitude,
			double phaseShift) {
		double primaryWave = Math.sin((month * 1.11) + phaseShift);
		double secondaryWave = Math.cos((month * 0.67) + (phaseShift * 0.6));

		double rate = 1 + (primaryWave * primaryAmplitude) + (secondaryWave * secondaryAmplitude);
		logger.trace("Revenue type monthly waves are primary="
				+ primaryWave
				+ " and secondary="
				+ secondaryWave
				+ " for month "
				+ month);
		logger.trace("Revenue type monthly rate is " + rate + " for month " + month);
		return rate;
	}

	private boolean isPlayoffMonth(int month) {
		boolean playoffMonth = month >= 8;
		if (playoffMonth) {
			logger.trace("Month " + month + " is a playoff month");
		}
		return playoffMonth;
	}
}

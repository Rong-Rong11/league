package process.service.finance.expense;

import org.apache.log4j.Logger;

import log.LoggerUtility;
import process.utility.CalendarUtility;

public class LeagueExpenseRateCalculator {
	private static final Logger logger = LoggerUtility.getLogger(LeagueExpenseRateCalculator.class, "text");

	private LeagueExpenseGameAnalyzer gameAnalyzer;
	private LeaguePopularityExpenseTracker popularityTracker;

	public LeagueExpenseRateCalculator(LeagueExpenseGameAnalyzer gameAnalyzer,
			LeaguePopularityExpenseTracker popularityTracker) {
		this.gameAnalyzer = gameAnalyzer;
		this.popularityTracker = popularityTracker;
		logger.debug("League expense rate calculator initialized");
	}

	public double getImportantGamesExpenseRate(int month, double ratePerGame) {
		int importantGames = gameAnalyzer.countImportantGamesInMonth(month);
		double rate = 1 + (importantGames * ratePerGame);
		logger.debug("Important games expense rate is "
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
		logger.debug("Playoff games expense rate is "
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
			logger.debug("Active playoff teams expense rate is 1.0 because month " + month + " is not a playoff month");
			return 1.0;
		}
		int activePlayoffTeams = gameAnalyzer.countActivePlayoffTeams();
		double rate = 1 + (activePlayoffTeams * ratePerTeam);
		logger.debug("Active playoff teams expense rate is "
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
			double rate = 1 + playoffBonusRate;
			logger.debug("Season expense rate is " + rate + " for playoff month " + month);
			return rate;
		}
		if (CalendarUtility.isImportantMonth(month)) {
			logger.debug("Season expense rate is 1.06 for important month " + month);
			return 1.06;
		}
		logger.debug("Season expense rate is 1.0 for month " + month);
		return 1.0;
	}

	public double getControlledEconomicNoise(int month, double maxAmplitude) {
		int importantGames = gameAnalyzer.countImportantGamesInMonth(month);
		int playoffGames = gameAnalyzer.countPlayoffGamesInMonth(month);
		int activeTeams = gameAnalyzer.countActivePlayoffTeams();
		double wave = Math.cos((month * 1.41) + (importantGames * 0.13) + (playoffGames * 0.21) + (activeTeams * 0.17));
		double rate = 1 + (wave * maxAmplitude);
		logger.trace("Controlled league expense noise wave is "
				+ wave
				+ " for month="
				+ month
				+ ", importantGames="
				+ importantGames
				+ ", playoffGames="
				+ playoffGames
				+ ", activeTeams="
				+ activeTeams);
		logger.debug("Controlled league expense noise rate is " + rate + " for month " + month);
		return rate;
	}

	public double getPopularitySeasonExpenseRate() {
		double rate = popularityTracker.getPopularitySeasonExpenseRate();
		logger.debug("Popularity season expense rate is " + rate);
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

package process.service.finance.distribution.central.calculation;

import org.apache.log4j.Logger;

import data.league.League;
import data.league.finance.CentralRevenueSeasonDynamics;
import log.LoggerUtility;
import process.utility.CalendarUtility;

public class MonthlyRevenueRateCalculator {
	private static final Logger logger = LoggerUtility.getLogger(MonthlyRevenueRateCalculator.class, "text");

	private MonthlyGameRevenueAnalyzer gameRevenueAnalyzer;
	private CentralRevenueSeasonDynamics seasonDynamics;
	private League league;

	public MonthlyRevenueRateCalculator(MonthlyGameRevenueAnalyzer gameRevenueAnalyzer,
			CentralRevenueSeasonDynamics seasonDynamics, League league) {
		this.gameRevenueAnalyzer = gameRevenueAnalyzer;
		this.seasonDynamics = seasonDynamics;
		this.league = league;
	}

	public double getPlayoffCentralRevenueRate() {
		if (league == null || league.getPlayoff() == null || league.getPlayoff().getCurrentRound() == null) {
			return 1.0;
		}

		switch (league.getPlayoff().getCurrentRound()) {
			case FIRST_ROUND:
				return 2;
			case CONFERENCE_SEMIFINALS:
				return 3.1;
			case CONFERENCE_FINALS:
				return 4.2;
			case NBA_FINALS:
				return 6;
			default:
				return 1.0;
		}
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

	public double getPremiumGamesRevenueRate(int month, double ratePerGame) {
		int premiumGames = gameRevenueAnalyzer.countPremiumGamesInMonth(month);
		double rate = 1 + (premiumGames * ratePerGame);
		logger.trace("Premium games revenue rate is "
				+ rate
				+ " for month "
				+ month
				+ " with "
				+ premiumGames
				+ " premium games");
		return rate;
	}

	public double getHighAttendanceRevenueRate(int month, double ratePerGame) {
		int highAttendanceGames = gameRevenueAnalyzer.countHighAttendanceGamesInMonth(month);
		double rate = 1 + (highAttendanceGames * ratePerGame);
		logger.trace("High attendance revenue rate is "
				+ rate
				+ " for month "
				+ month
				+ " with "
				+ highAttendanceGames
				+ " high attendance games");
		return rate;
	}

	public double getStarDrivenRevenueRate(int month, double rivalryRatePerGame, double starRatePerGame,
			double starRivalryRatePerGame) {
		int rivalryGames = gameRevenueAnalyzer.countRivalryGamesInMonth(month);
		int starGames = gameRevenueAnalyzer.countStarGamesInMonth(month);
		int starRivalryGames = gameRevenueAnalyzer.countStarRivalryGamesInMonth(month);
		double rate = 1
				+ (rivalryGames * rivalryRatePerGame)
				+ (starGames * starRatePerGame)
				+ (starRivalryGames * starRivalryRatePerGame);
		logger.trace("Star driven revenue rate is "
				+ rate
				+ " for month "
				+ month
				+ " with rivalryGames="
				+ rivalryGames
				+ ", starGames="
				+ starGames
				+ ", starRivalryGames="
				+ starRivalryGames);
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
			int activePlayoffTeams = gameRevenueAnalyzer.countActivePlayoffTeams();
			double rate = 1 + (playoffBonusRate * 0.68) + (activePlayoffTeams * 0.0045);
			logger.trace("Season momentum rate is " + rate + " for playoff month " + month);
			return rate;
		}
		if (CalendarUtility.isImportantMonth(month)) {
			double rate = seasonDynamics == null ? 1.08 : seasonDynamics.getImportantMonthRate();
			logger.trace("Season momentum rate is " + rate + " for important month " + month);
			return rate;
		}
		logger.trace("Season momentum rate is 1.0 for month " + month);
		return 1.0;
	}

	public double getControlledEconomicNoise(int month, double maxAmplitude) {
		int importantGames = gameRevenueAnalyzer.countImportantGamesInMonth(month);
		int premiumGames = gameRevenueAnalyzer.countPremiumGamesInMonth(month);
		int highAttendanceGames = gameRevenueAnalyzer.countHighAttendanceGamesInMonth(month);
		int playoffGames = gameRevenueAnalyzer.countPlayoffGamesInMonth(month);
		int activeTeams = gameRevenueAnalyzer.countActivePlayoffTeams();
		double phaseShift = seasonDynamics == null ? 0.0 : seasonDynamics.getEconomicNoisePhaseShift();
		double wave = Math.sin((month * 1.37)
				+ phaseShift
				+ (importantGames * 0.07)
				+ (premiumGames * 0.11)
				+ (highAttendanceGames * 0.09)
				+ (playoffGames * 0.17)
				+ (activeTeams * 0.14));
		double rate = 1 + (wave * maxAmplitude);
		logger.trace("Controlled economic noise wave is "
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
		logger.trace("Controlled economic noise rate is " + rate + " for month " + month);
		return rate;
	}

	public double getRevenueTypeMonthlyRate(int month, double primaryAmplitude, double secondaryAmplitude,
			double phaseShift) {
		int premiumGames = gameRevenueAnalyzer.countPremiumGamesInMonth(month);
		int starRivalryGames = gameRevenueAnalyzer.countStarRivalryGamesInMonth(month);
		double dynamicPhaseShift = seasonDynamics == null ? 0.0 : seasonDynamics.getRevenueTypePhaseShift();
		double primaryWave = Math.sin((month * 0.93) + phaseShift + dynamicPhaseShift + (premiumGames * 0.08));
		double secondaryWave = Math.cos((month * 0.58) + (phaseShift * 0.6) + (dynamicPhaseShift * 0.7)
				+ (starRivalryGames * 0.10));

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

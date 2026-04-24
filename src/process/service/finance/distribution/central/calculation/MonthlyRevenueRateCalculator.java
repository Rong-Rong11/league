package process.service.finance.distribution.central.calculation;

import process.utility.CalendarUtility;

public class MonthlyRevenueRateCalculator {

	private MonthlyGameRevenueAnalyzer gameRevenueAnalyzer;

	public MonthlyRevenueRateCalculator(MonthlyGameRevenueAnalyzer gameRevenueAnalyzer) {
		this.gameRevenueAnalyzer = gameRevenueAnalyzer;
	}

	public double getLeagueMonthlyAttractivenessRate(int month) {
		double attractiveness = gameRevenueAnalyzer.calculateMonthlyLeagueAttractiveness(month);

		if (attractiveness < 60) {
			return 0.60;
		}
		if (attractiveness < 74) {
			return 0.78;
		}
		if (attractiveness < 90) {
			return 1.00;
		}
		if (attractiveness < 108) {
			return 1.24;
		}
		return 1.52;
	}

	public double getImportantGamesRevenueRate(int month, double ratePerGame) {
		int importantGames = gameRevenueAnalyzer.countImportantGamesInMonth(month);
		return 1 + (importantGames * ratePerGame);
	}

	public double getPlayoffGamesRevenueRate(int month, double ratePerGame) {
		int playoffGames = gameRevenueAnalyzer.countPlayoffGamesInMonth(month);
		return 1 + (playoffGames * ratePerGame);
	}

	public double getActivePlayoffTeamsRate(int month, double ratePerTeam) {
		if (!isPlayoffMonth(month)) {
			return 1.0;
		}
		return 1 + (gameRevenueAnalyzer.countActivePlayoffTeams() * ratePerTeam);
	}

	public double getSeasonMomentumRate(int month, double playoffBonusRate) {
		if (isPlayoffMonth(month)) {
			return 1 + playoffBonusRate;
		}
		if (CalendarUtility.isImportantMonth(month)) {
			return 1.28;
		}
		return 1.0;
	}

	public double getControlledEconomicNoise(int month, double maxAmplitude) {
		int importantGames = gameRevenueAnalyzer.countImportantGamesInMonth(month);
		int playoffGames = gameRevenueAnalyzer.countPlayoffGamesInMonth(month);
		int activeTeams = gameRevenueAnalyzer.countActivePlayoffTeams();
		double wave = Math.sin((month * 1.73) + (importantGames * 0.11) + (playoffGames * 0.23) + (activeTeams * 0.19));
		return 1 + (wave * maxAmplitude);
	}

	public double getRevenueTypeMonthlyRate(int month, double primaryAmplitude, double secondaryAmplitude,
			double phaseShift) {
		double primaryWave = Math.sin((month * 1.11) + phaseShift);
		double secondaryWave = Math.cos((month * 0.67) + (phaseShift * 0.6));

		return 1 + (primaryWave * primaryAmplitude) + (secondaryWave * secondaryAmplitude);
	}

	private boolean isPlayoffMonth(int month) {
		return month >= 8;
	}
}

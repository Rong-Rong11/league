package process.service.finance.expense;

import process.utility.CalendarUtility;

public class LeagueExpenseRateCalculator {

	private LeagueExpenseGameAnalyzer gameAnalyzer;
	private LeaguePopularityExpenseTracker popularityTracker;

	public LeagueExpenseRateCalculator(LeagueExpenseGameAnalyzer gameAnalyzer,
			LeaguePopularityExpenseTracker popularityTracker) {
		this.gameAnalyzer = gameAnalyzer;
		this.popularityTracker = popularityTracker;
	}

	public double getImportantGamesExpenseRate(int month, double ratePerGame) {
		return 1 + (gameAnalyzer.countImportantGamesInMonth(month) * ratePerGame);
	}

	public double getPlayoffGamesExpenseRate(int month, double ratePerGame) {
		return 1 + (gameAnalyzer.countPlayoffGamesInMonth(month) * ratePerGame);
	}

	public double getActivePlayoffTeamsExpenseRate(int month, double ratePerTeam) {
		if (!isPlayoffMonth(month)) {
			return 1.0;
		}
		return 1 + (gameAnalyzer.countActivePlayoffTeams() * ratePerTeam);
	}

	public double getSeasonExpenseRate(int month, double playoffBonusRate) {
		if (isPlayoffMonth(month)) {
			return 1 + playoffBonusRate;
		}
		if (CalendarUtility.isImportantMonth(month)) {
			return 1.06;
		}
		return 1.0;
	}

	public double getControlledEconomicNoise(int month, double maxAmplitude) {
		int importantGames = gameAnalyzer.countImportantGamesInMonth(month);
		int playoffGames = gameAnalyzer.countPlayoffGamesInMonth(month);
		int activeTeams = gameAnalyzer.countActivePlayoffTeams();
		double wave = Math.cos((month * 1.41) + (importantGames * 0.13) + (playoffGames * 0.21) + (activeTeams * 0.17));
		return 1 + (wave * maxAmplitude);
	}

	public double getPopularitySeasonExpenseRate() {
		return popularityTracker.getPopularitySeasonExpenseRate();
	}

	private boolean isPlayoffMonth(int month) {
		return month >= 8;
	}
}

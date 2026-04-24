package process.service.finance.distribution.central.calculation;

import java.time.LocalDate;

import data.calendar.GameDay;
import data.sport.setup.Game;
import process.utility.CalendarUtility;

public class MonthlyRevenueBonusCalculator {

	private MonthlyGameRevenueAnalyzer gameRevenueAnalyzer;

	public MonthlyRevenueBonusCalculator(MonthlyGameRevenueAnalyzer gameRevenueAnalyzer) {
		this.gameRevenueAnalyzer = gameRevenueAnalyzer;
	}

	public double getLeagueMonthlyAdditiveBonus(int month) {
		double totalAttractiveness = 0.0;
		double totalAttendance = 0.0;
		int gameCount = 0;
		int importantGames = 0;
		int premiumGames = 0;
		int highAttendanceGames = 0;
		int rivalryGames = 0;
		int starGames = 0;
		int starRivalryGames = 0;

		for (GameDay gameDay : gameRevenueAnalyzer.getAllGameDaysForMonth(month)) {
			LocalDate date = gameDay.getDate();
			for (Game game : gameDay.getGames()) {
				double score = CalendarUtility.popularityScoreGame(game, date);
				totalAttractiveness += score;
				gameCount++;

				if (gameRevenueAnalyzer.isImportantGame(game, date)) {
					importantGames++;
				}
				if (score >= 110) {
					premiumGames++;
				}

				boolean rivalry = game.getGameContext().isRivalry();
				boolean starGame = game.getGameContext().getHomeTeam().hasStarPlayer()
						|| game.getGameContext().getAwayTeam().hasStarPlayer();

				if (rivalry) {
					rivalryGames++;
				}
				if (starGame) {
					starGames++;
				}
				if (rivalry && starGame) {
					starRivalryGames++;
				}

				double attendanceRate = gameRevenueAnalyzer.getAttendanceRate(game);
				totalAttendance += attendanceRate;
				if (attendanceRate >= 0.92) {
					highAttendanceGames++;
				}
			}
		}

		double averageAttractiveness = gameCount == 0 ? 0.0 : totalAttractiveness / gameCount;
		double averageAttendance = gameCount == 0 ? 0.0 : totalAttendance / gameCount;

		double bonus = 0.0;
		bonus += getAttractivenessBonus(averageAttractiveness);
		bonus += getAttendanceBonus(averageAttendance);
		bonus += getVolumeBonus(importantGames, premiumGames, highAttendanceGames);
		bonus += getStarRivalryBonus(rivalryGames, starGames, starRivalryGames);
		bonus += getPlayoffMonthlyBonus(month);

		return bonus;
	}

	private double getPlayoffMonthlyBonus(int month) {
		int playoffGames = gameRevenueAnalyzer.countPlayoffGamesInMonth(month);
		return playoffGames * 0.16;
	}

	private double getAttractivenessBonus(double averageAttractiveness) {
		if (averageAttractiveness < 60) {
			return -2.4;
		}
		if (averageAttractiveness < 74) {
			return -1.1;
		}
		if (averageAttractiveness >= 108) {
			return 3.0;
		}
		if (averageAttractiveness >= 90) {
			return 1.5;
		}
		return 0.0;
	}

	private double getAttendanceBonus(double averageAttendance) {
		if (averageAttendance < 0.72) {
			return -1.2;
		}
		if (averageAttendance >= 0.90) {
			return 1.9;
		}
		if (averageAttendance >= 0.82) {
			return 1.0;
		}
		return 0.0;
	}

	private double getVolumeBonus(int importantGames, int premiumGames, int highAttendanceGames) {
		return (importantGames * 0.10)
				+ (premiumGames * 0.16)
				+ (highAttendanceGames * 0.12);
	}

	private double getStarRivalryBonus(int rivalryGames, int starGames, int starRivalryGames) {
		return (rivalryGames * 0.04)
				+ (starGames * 0.025)
				+ (starRivalryGames * 0.09);
	}
}

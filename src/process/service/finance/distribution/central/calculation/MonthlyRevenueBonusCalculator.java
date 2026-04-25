package process.service.finance.distribution.central.calculation;

import java.time.LocalDate;

import org.apache.log4j.Logger;

import data.calendar.GameDay;
import data.sport.setup.Game;
import log.LoggerUtility;
import process.utility.CalendarUtility;

public class MonthlyRevenueBonusCalculator {
	private static final Logger logger = LoggerUtility.getLogger(MonthlyRevenueBonusCalculator.class, "text");

	private MonthlyGameRevenueAnalyzer gameRevenueAnalyzer;

	public MonthlyRevenueBonusCalculator(MonthlyGameRevenueAnalyzer gameRevenueAnalyzer) {
		this.gameRevenueAnalyzer = gameRevenueAnalyzer;
	}

	public double getLeagueMonthlyAdditiveBonus(int month) {
		logger.debug("Calculating league monthly additive bonus for month " + month);
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
				logger.trace("Adding bonus input game score " + score + " for " + date);
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
		logger.debug("Monthly bonus inputs for month "
				+ month
				+ ": games="
				+ gameCount
				+ ", averageAttractiveness="
				+ averageAttractiveness
				+ ", averageAttendance="
				+ averageAttendance
				+ ", importantGames="
				+ importantGames
				+ ", premiumGames="
				+ premiumGames
				+ ", highAttendanceGames="
				+ highAttendanceGames
				+ ", rivalryGames="
				+ rivalryGames
				+ ", starGames="
				+ starGames
				+ ", starRivalryGames="
				+ starRivalryGames);

		double bonus = 0.0;
		bonus += getAttractivenessBonus(averageAttractiveness);
		bonus += getAttendanceBonus(averageAttendance);
		bonus += getVolumeBonus(importantGames, premiumGames, highAttendanceGames);
		bonus += getStarRivalryBonus(rivalryGames, starGames, starRivalryGames);
		bonus += getPlayoffMonthlyBonus(month);
		logger.debug("Calculated league monthly additive bonus " + bonus + " for month " + month);

		return bonus;
	}

	private double getPlayoffMonthlyBonus(int month) {
		int playoffGames = gameRevenueAnalyzer.countPlayoffGamesInMonth(month);
		double bonus = playoffGames * 0.16;
		logger.trace("Playoff monthly bonus is " + bonus + " for " + playoffGames + " playoff games");
		return bonus;
	}

	private double getAttractivenessBonus(double averageAttractiveness) {
		if (averageAttractiveness < 60) {
			logger.trace("Attractiveness bonus is -2.4 for average attractiveness " + averageAttractiveness);
			return -2.4;
		}
		if (averageAttractiveness < 74) {
			logger.trace("Attractiveness bonus is -1.1 for average attractiveness " + averageAttractiveness);
			return -1.1;
		}
		if (averageAttractiveness >= 108) {
			logger.trace("Attractiveness bonus is 3.0 for average attractiveness " + averageAttractiveness);
			return 3.0;
		}
		if (averageAttractiveness >= 90) {
			logger.trace("Attractiveness bonus is 1.5 for average attractiveness " + averageAttractiveness);
			return 1.5;
		}
		logger.trace("Attractiveness bonus is 0.0 for average attractiveness " + averageAttractiveness);
		return 0.0;
	}

	private double getAttendanceBonus(double averageAttendance) {
		if (averageAttendance < 0.72) {
			logger.trace("Attendance bonus is -1.2 for average attendance " + averageAttendance);
			return -1.2;
		}
		if (averageAttendance >= 0.90) {
			logger.trace("Attendance bonus is 1.9 for average attendance " + averageAttendance);
			return 1.9;
		}
		if (averageAttendance >= 0.82) {
			logger.trace("Attendance bonus is 1.0 for average attendance " + averageAttendance);
			return 1.0;
		}
		logger.trace("Attendance bonus is 0.0 for average attendance " + averageAttendance);
		return 0.0;
	}

	private double getVolumeBonus(int importantGames, int premiumGames, int highAttendanceGames) {
		double bonus = (importantGames * 0.10)
				+ (premiumGames * 0.16)
				+ (highAttendanceGames * 0.12);
		logger.trace("Volume bonus is "
				+ bonus
				+ " for importantGames="
				+ importantGames
				+ ", premiumGames="
				+ premiumGames
				+ ", highAttendanceGames="
				+ highAttendanceGames);
		return bonus;
	}

	private double getStarRivalryBonus(int rivalryGames, int starGames, int starRivalryGames) {
		double bonus = (rivalryGames * 0.04)
				+ (starGames * 0.025)
				+ (starRivalryGames * 0.09);
		logger.trace("Star rivalry bonus is "
				+ bonus
				+ " for rivalryGames="
				+ rivalryGames
				+ ", starGames="
				+ starGames
				+ ", starRivalryGames="
				+ starRivalryGames);
		return bonus;
	}
}

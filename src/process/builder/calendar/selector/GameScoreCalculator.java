package process.builder.calendar.selector;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.apache.log4j.Logger;

import data.league.RegularSeason;
import data.sport.setup.Game;
import data.team.Team;
import log.LoggerUtility;
import process.utility.CalendarUtility;

public class GameScoreCalculator {
	private static final Logger logger = LoggerUtility.getLogger(GameScoreCalculator.class, "text");

	private RegularSeason regularSeason;
	private GameScheduleConstraintChecker constraintChecker;

	public GameScoreCalculator(RegularSeason regularSeason, GameScheduleConstraintChecker constraintChecker) {
		this.regularSeason = regularSeason;
		this.constraintChecker = constraintChecker;
	}

	public double calculateScore(Game game, LocalDate date) {
		double homeLoad = loadGameRatio(game.getGameContext().getHomeTeam(), date, regularSeason.getEndDate());
		double awayLoad = loadGameRatio(game.getGameContext().getAwayTeam(), date, regularSeason.getEndDate());
		double loadScore = homeLoad + awayLoad;
		double gameScheduleScore = scheduleScore(game, date);

		double popularityScore = 0;
		if (CalendarUtility.isSpecialEvent(regularSeason, date) || CalendarUtility.isImportantDay(date)) {
			popularityScore = CalendarUtility.popularityScoreGame(game, date);
		}
		double randomScore = (Math.random() - 0.5) * 10.0;
		double totalScore = loadScore + popularityScore + gameScheduleScore + randomScore;
		logger.trace("Scored game "
				+ game.getGameContext().getHomeTeam().getName()
				+ " vs "
				+ game.getGameContext().getAwayTeam().getName()
				+ " with total score "
				+ totalScore);
		return totalScore;
	}

	private double loadGameRatio(Team team, LocalDate date, LocalDate endOfSeasonDate) {
		int remainingGames = GameCandidateFinder.getNumberOfRemainingUnscheduledGames(team.getSchedule());
		long remainingDays = ChronoUnit.DAYS.between(date, endOfSeasonDate);
		if (remainingDays == 0) {
			logger.trace("Load ratio for " + team.getName()
					+ " uses remaining games only because no days remain in season");
			return remainingGames;
		}
		double ratio = (double) remainingGames / remainingDays;
		logger.trace("Load ratio for "
				+ team.getName()
				+ " is "
				+ ratio
				+ " with "
				+ remainingGames
				+ " remaining games over "
				+ remainingDays
				+ " remaining days");
		return ratio;
	}

	private double scheduleScore(Game game, LocalDate localDate) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();
		int homeDays = homeTeam.getSchedule().daysSinceLastGame(localDate);
		int awayDays = awayTeam.getSchedule().daysSinceLastGame(localDate);
		double score = 0;

		score += restPenalty(homeDays);
		score += restPenalty(awayDays);

		if (isScheduleTight(homeTeam, localDate) || isScheduleTight(awayTeam, localDate)) {
			score += 120.0;
		}

		if (constraintChecker.playedRecentlyAgainst(homeTeam, awayTeam, localDate, 5)) {
			score -= 80.0;
		}

		logger.trace("Schedule score for "
				+ homeTeam.getName()
				+ " vs "
				+ awayTeam.getName()
				+ " is "
				+ score);
		return score;
	}

	private double restPenalty(int daysSinceLastGame) {
		if (daysSinceLastGame <= 0) {
			return -10000.0;
		}
		if (daysSinceLastGame == 1) {
			return -1000.0;
		}
		if (daysSinceLastGame == 2) {
			return -200.0;
		}
		return 0.0;
	}

	private boolean isScheduleTight(Team team, LocalDate date) {
		int remainingGames = GameCandidateFinder.getNumberOfRemainingUnscheduledGames(team.getSchedule());
		long remainingDays = ChronoUnit.DAYS.between(date, regularSeason.getEndDate());
		boolean tight = remainingDays > 0 && remainingGames >= remainingDays;
		if (tight) {
			logger.trace("Schedule is tight for "
					+ team.getName()
					+ " with "
					+ remainingGames
					+ " remaining games over "
					+ remainingDays
					+ " days");
		}
		return tight;
	}
}

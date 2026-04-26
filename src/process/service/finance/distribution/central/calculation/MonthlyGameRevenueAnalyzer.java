package process.service.finance.distribution.central.calculation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import data.calendar.GameDay;
import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import log.LoggerUtility;
import process.service.finance.FinanceManager;
import process.utility.CalendarUtility;

public class MonthlyGameRevenueAnalyzer {
	private static final Logger logger = LoggerUtility.getLogger(MonthlyGameRevenueAnalyzer.class, "text");

	private final League league;
	private FinanceManager financeManager;

	public MonthlyGameRevenueAnalyzer(League league) {
		this.league = league;
	}

	public void setFinanceManager(FinanceManager financeManager) {
		logger.debug("Setting finance manager for monthly game revenue analyzer");
		this.financeManager = financeManager;
	}

	public double calculateMonthlyLeagueAttractiveness(int month) {
		logger.debug("Calculating monthly league attractiveness for month " + month);
		double totalScore = 0.0;
		int gameCount = 0;

		for (GameDay gameDay : getAllGameDaysForMonth(month)) {
			LocalDate date = gameDay.getDate();
			for (Game game : gameDay.getGames()) {
				double popularityScore = CalendarUtility.popularityScoreGame(game, date);
				logger.trace("Adding game popularity score " + popularityScore + " for " + date);
				totalScore += popularityScore;
				gameCount++;
			}
		}

		if (gameCount == 0) {
			logger.warn("Monthly league attractiveness is 0 because no games were found for month " + month);
			return 0.0;
		}

		double attractiveness = totalScore / gameCount;
		logger.debug("Calculated monthly league attractiveness "
				+ attractiveness
				+ " from "
				+ gameCount
				+ " games for month "
				+ month);
		return attractiveness;
	}

	public int countImportantGamesInMonth(int month) {
		logger.debug("Counting important games for month " + month);
		int count = 0;
		count += countImportantGamesForSeasonMonth(month, false);
		count += countImportantGamesForSeasonMonth(month, true);
		logger.debug("Counted " + count + " important games for month " + month);
		return count;
	}

	public int countPremiumGamesInMonth(int month) {
		int count = 0;
		for (GameDay gameDay : getAllGameDaysForMonth(month)) {
			LocalDate date = gameDay.getDate();
			for (Game game : gameDay.getGames()) {
				if (CalendarUtility.popularityScoreGame(game, date) >= 110) {
					count++;
				}
			}
		}
		return count;
	}

	public int countHighAttendanceGamesInMonth(int month) {
		int count = 0;
		for (GameDay gameDay : getAllGameDaysForMonth(month)) {
			for (Game game : gameDay.getGames()) {
				if (getAttendanceRate(game) >= 0.92) {
					count++;
				}
			}
		}
		return count;
	}

	public int countRivalryGamesInMonth(int month) {
		int count = 0;
		for (GameDay gameDay : getAllGameDaysForMonth(month)) {
			for (Game game : gameDay.getGames()) {
				if (game.getGameContext().isRivalry()) {
					count++;
				}
			}
		}
		return count;
	}

	public int countStarGamesInMonth(int month) {
		int count = 0;
		for (GameDay gameDay : getAllGameDaysForMonth(month)) {
			for (Game game : gameDay.getGames()) {
				boolean starGame = game.getGameContext().getHomeTeam().hasStarPlayer()
						|| game.getGameContext().getAwayTeam().hasStarPlayer();
				if (starGame) {
					count++;
				}
			}
		}
		return count;
	}

	public int countStarRivalryGamesInMonth(int month) {
		int count = 0;
		for (GameDay gameDay : getAllGameDaysForMonth(month)) {
			for (Game game : gameDay.getGames()) {
				boolean rivalry = game.getGameContext().isRivalry();
				boolean starGame = game.getGameContext().getHomeTeam().hasStarPlayer()
						|| game.getGameContext().getAwayTeam().hasStarPlayer();
				if (rivalry && starGame) {
					count++;
				}
			}
		}
		return count;
	}

	public int countPlayoffGamesInMonth(int month) {
		logger.debug("Counting playoff games for month " + month);
		int count = 0;

		for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
			LocalDate date = gameDay.getDate();
			if (date == null || !matchesFinanceMonth(date, month)) {
				continue;
			}
			count += gameDay.getGames().size();
		}
		logger.debug("Counted " + count + " playoff games for month " + month);
		return count;
	}

	public List<GameDay> getAllGameDaysForMonth(int month) {
		logger.trace("Collecting all game days for finance month " + month);
		List<GameDay> gameDays = new ArrayList<GameDay>();

		if (league.getRegularSeason() != null && league.getRegularSeason().getNbaCalendar() != null) {
			for (GameDay gameDay : league.getRegularSeason().getNbaCalendar().getCalendar().values()) {
				if (gameDay.getDate() != null && matchesFinanceMonth(gameDay.getDate(), month)) {
					gameDays.add(gameDay);
				}
			}
		}

		if (league.getPlayoff() != null && league.getPlayoff().getNbaCalendar() != null) {
			for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
				if (gameDay.getDate() != null && matchesFinanceMonth(gameDay.getDate(), month)) {
					gameDays.add(gameDay);
				}
			}
		}

		logger.debug("Collected " + gameDays.size() + " game days for finance month " + month);
		return gameDays;
	}

	public int countActivePlayoffTeams() {
		if (league.getPlayoff() == null || league.getPlayoff().getCurrentRound() == null) {
			logger.warn("No active playoff teams because playoff or current round is null");
			return 0;
		}

		ArrayList<Team> activeTeams = new ArrayList<Team>();
		for (data.sport.setup.PlayoffSeries series : CalendarUtility.getCurrentRoundSeries(league.getPlayoff())) {
			if (series == null || series.isFinished()) {
				continue;
			}
			activeTeams.add(series.getHigherTeam());
			activeTeams.add(series.getLowerTeam());
		}
		logger.debug("Counted " + activeTeams.size() + " active playoff teams");
		return activeTeams.size();
	}

	public boolean isImportantGame(Game game, LocalDate date) {
		boolean importantGame = CalendarUtility.popularityScoreGame(game, date) >= 72 || game.getGameContext().isRivalry();
		if (importantGame) {
			logger.trace("Game on " + date + " is important");
		}
		return importantGame;
	}

	public boolean hasHighAttendance(Game game) {
		if (financeManager == null) {
			logger.warn("Unable to check high attendance because finance manager is null");
			return false;
		}
		GameStat gameStat = financeManager.getGameStat(game);
		boolean highAttendance = gameStat != null && gameStat.getAttendanceRate() > 0.85;
		if (highAttendance) {
			logger.trace("Game has high attendance with rate " + gameStat.getAttendanceRate());
		}
		return highAttendance;
	}

	public double getAttendanceRate(Game game) {
		if (financeManager == null) {
			logger.warn("Unable to get attendance rate because finance manager is null");
			return 0.0;
		}
		GameStat gameStat = financeManager.getGameStat(game);
		if (gameStat == null) {
			logger.trace("Attendance rate is 0 because game stat is null");
			return 0.0;
		}
		logger.trace("Attendance rate is " + gameStat.getAttendanceRate());
		return gameStat.getAttendanceRate();
	}

	private int countImportantGamesForSeasonMonth(int month, boolean playoff) {
		logger.trace("Counting important "
				+ (playoff ? "playoff" : "regular season")
				+ " games for month "
				+ month);
		int count = 0;
		if (playoff) {
			for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
				LocalDate date = gameDay.getDate();
				if (date == null || !matchesFinanceMonth(date, month)) {
					continue;
				}
				for (Game game : gameDay.getGames()) {
					if (isImportantGame(game, date)) {
						count++;
						if (hasHighAttendance(game)) {
							count++;
						}
					}
				}
			}
			logger.trace("Counted " + count + " important playoff games for month " + month);
			return count;
		}
		for (GameDay gameDay : league.getRegularSeason().getNbaCalendar().getCalendar().values()) {
			LocalDate date = gameDay.getDate();
			if (date == null || !matchesFinanceMonth(date, month)) {
				continue;
			}
			for (Game game : gameDay.getGames()) {
				if (isImportantGame(game, date)) {
					count++;
					if (hasHighAttendance(game)) {
						count++;
					}
				}
			}
		}
		logger.trace("Counted " + count + " important regular season games for month " + month);
		return count;
	}

	private boolean matchesFinanceMonth(LocalDate date, int month) {
		int startMonth = league.getRegularSeason().getDebutDate().getMonthValue();
		int monthDelta = date.getMonthValue() - startMonth;
		if (monthDelta < 0) {
			monthDelta += 12;
		}
		boolean matches = (monthDelta + 1) == month;
		if (matches) {
			logger.trace("Date " + date + " matches finance month " + month);
		}
		return matches;
	}
}

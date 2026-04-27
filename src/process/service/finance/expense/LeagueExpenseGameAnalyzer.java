package process.service.finance.expense;

import org.apache.log4j.Logger;

import data.calendar.GameDay;
import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import log.LoggerUtility;
import process.service.finance.FinanceManager;
import process.utility.CalendarUtility;

public class LeagueExpenseGameAnalyzer {
	private static final Logger logger = LoggerUtility.getLogger(LeagueExpenseGameAnalyzer.class, "text");

	private League league;
	private FinanceManager financeManager;

	public LeagueExpenseGameAnalyzer(League league) {
		this.league = league;
	}

	public void setFinanceManager(FinanceManager financeManager) {
		logger.debug("Setting finance manager for league expense game analyzer");
		this.financeManager = financeManager;
	}

	public int countImportantGamesInMonth(int month) {
		logger.debug("Counting important expense games for month " + month);
		int count = 0;
		count += countImportantGamesForSeasonMonth(month, false);
		count += countImportantGamesForSeasonMonth(month, true);
		logger.debug("Counted " + count + " important expense games for month " + month);
		return count;
	}

	public int countPlayoffGamesInMonth(int month) {
		if (league.getPlayoff() == null || league.getPlayoff().getNbaCalendar() == null) {
			logger.warn("No playoff games counted because playoff or playoff calendar is null");
			return 0;
		}

		logger.debug("Counting playoff expense games for month " + month);
		int count = 0;
		for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
			if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
				continue;
			}
			count += gameDay.getGames().size();
		}
		logger.debug("Counted " + count + " playoff expense games for month " + month);
		return count;
	}

	public int countActivePlayoffTeams() {
		if (league.getPlayoff() == null || league.getPlayoff().getCurrentRound() == null) {
			logger.warn("No active playoff teams counted because playoff or current round is null");
			return 0;
		}

		int count = 0;
		for (PlayoffSeries series : CalendarUtility.getCurrentRoundSeries(league.getPlayoff())) {
			if (series == null || series.isFinished()) {
				continue;
			}
			count += 2;
		}
		logger.debug("Counted " + count + " active playoff teams for league expenses");
		return count;
	}

	public int countPremiumGamesInMonth(int month) {
		int count = 0;
		if (league.getRegularSeason() != null && league.getRegularSeason().getNbaCalendar() != null) {
			for (GameDay gameDay : league.getRegularSeason().getNbaCalendar().getCalendar().values()) {
				if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
					continue;
				}
				for (Game game : gameDay.getGames()) {
					if (CalendarUtility.popularityScoreGame(game, gameDay.getDate()) >= 110) {
						count++;
					}
				}
			}
		}
		if (league.getPlayoff() != null && league.getPlayoff().getNbaCalendar() != null) {
			for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
				if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
					continue;
				}
				for (Game game : gameDay.getGames()) {
					if (CalendarUtility.popularityScoreGame(game, gameDay.getDate()) >= 110) {
						count++;
					}
				}
			}
		}
		return count;
	}

	public int countHighAttendanceGamesInMonth(int month) {
		int count = 0;
		if (league.getRegularSeason() != null && league.getRegularSeason().getNbaCalendar() != null) {
			for (GameDay gameDay : league.getRegularSeason().getNbaCalendar().getCalendar().values()) {
				if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
					continue;
				}
				for (Game game : gameDay.getGames()) {
					if (hasHighAttendance(game)) {
						count++;
					}
				}
			}
		}
		if (league.getPlayoff() != null && league.getPlayoff().getNbaCalendar() != null) {
			for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
				if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
					continue;
				}
				for (Game game : gameDay.getGames()) {
					if (hasHighAttendance(game)) {
						count++;
					}
				}
			}
		}
		return count;
	}

	public int countStarRivalryGamesInMonth(int month) {
		int count = 0;
		if (league.getRegularSeason() != null && league.getRegularSeason().getNbaCalendar() != null) {
			for (GameDay gameDay : league.getRegularSeason().getNbaCalendar().getCalendar().values()) {
				if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
					continue;
				}
				count += countStarRivalryGames(gameDay);
			}
		}
		if (league.getPlayoff() != null && league.getPlayoff().getNbaCalendar() != null) {
			for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
				if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
					continue;
				}
				count += countStarRivalryGames(gameDay);
			}
		}
		return count;
	}

	private int countImportantGamesForSeasonMonth(int month, boolean playoff) {
		logger.trace("Counting important "
				+ (playoff ? "playoff" : "regular season")
				+ " expense games for month "
				+ month);
		int count = 0;
		if (playoff) {
			if (league.getPlayoff() == null || league.getPlayoff().getNbaCalendar() == null) {
				logger.warn("No important playoff expense games counted because playoff or playoff calendar is null");
				return 0;
			}
			for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
				if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
					continue;
				}
				for (Game game : gameDay.getGames()) {
					if (isImportantGame(game, gameDay.getDate())) {
						count++;
						if (hasHighAttendance(game)) {
							count++;
						}
					}
				}
			}
			logger.trace("Counted " + count + " important playoff expense games for month " + month);
			return count;
		}

		if (league.getRegularSeason() == null || league.getRegularSeason().getNbaCalendar() == null) {
			logger.warn("No important regular season expense games counted because regular season or calendar is null");
			return 0;
		}
		for (GameDay gameDay : league.getRegularSeason().getNbaCalendar().getCalendar().values()) {
			if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
				continue;
			}
			for (Game game : gameDay.getGames()) {
				if (isImportantGame(game, gameDay.getDate())) {
					count++;
					if (hasHighAttendance(game)) {
						count++;
					}
				}
			}
		}
		logger.trace("Counted " + count + " important regular season expense games for month " + month);
		return count;
	}

	private boolean matchesFinanceMonth(java.time.LocalDate date, int month) {
		int startMonth = league.getRegularSeason().getDebutDate().getMonthValue();
		int monthDelta = date.getMonthValue() - startMonth;
		if (monthDelta < 0) {
			monthDelta += 12;
		}
		boolean matches = (monthDelta + 1) == month;
		if (matches) {
			logger.trace("Date " + date + " matches expense finance month " + month);
		}
		return matches;
	}

	private boolean isImportantGame(Game game, java.time.LocalDate date) {
		boolean importantGame = CalendarUtility.popularityScoreGame(game, date) >= 95
				|| game.getGameContext().isRivalry();
		if (importantGame) {
			logger.trace("Game on " + date + " is important for league expenses");
		}
		return importantGame;
	}

	private boolean hasHighAttendance(Game game) {
		if (financeManager == null) {
			logger.warn("Unable to check high attendance for league expenses because finance manager is null");
			return false;
		}
		GameStat gameStat = financeManager.getGameStat(game);
		boolean highAttendance = gameStat != null && gameStat.getAttendanceRate() > 0.85;
		if (highAttendance) {
			logger.trace("Game has high attendance for league expenses with rate " + gameStat.getAttendanceRate());
		}
		return highAttendance;
	}

	private int countStarRivalryGames(GameDay gameDay) {
		int count = 0;
		for (Game game : gameDay.getGames()) {
			boolean rivalry = game.getGameContext().isRivalry();
			boolean starGame = game.getGameContext().getHomeTeam().hasStarPlayer()
					|| game.getGameContext().getAwayTeam().hasStarPlayer();
			if (rivalry && starGame) {
				count++;
			}
		}
		return count;
	}
}

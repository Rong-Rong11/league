package process.service.finance.expense;

import data.calendar.GameDay;
import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import process.service.finance.FinanceManager;
import process.utility.CalendarUtility;

public class LeagueExpenseGameAnalyzer {

	private League league;
	private FinanceManager financeManager;

	public LeagueExpenseGameAnalyzer(League league) {
		this.league = league;
	}

	public void setFinanceManager(FinanceManager financeManager) {
		this.financeManager = financeManager;
	}

	public int countImportantGamesInMonth(int month) {
		int count = 0;
		count += countImportantGamesForSeasonMonth(month, false);
		count += countImportantGamesForSeasonMonth(month, true);
		return count;
	}

	public int countPlayoffGamesInMonth(int month) {
		if (league.getPlayoff() == null || league.getPlayoff().getNbaCalendar() == null) {
			return 0;
		}

		int count = 0;
		for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
			if (gameDay.getDate() == null || !matchesFinanceMonth(gameDay.getDate(), month)) {
				continue;
			}
			count += gameDay.getGames().size();
		}
		return count;
	}

	public int countActivePlayoffTeams() {
		if (league.getPlayoff() == null || league.getPlayoff().getCurrentRound() == null) {
			return 0;
		}

		int count = 0;
		for (PlayoffSeries series : CalendarUtility.getCurrentRoundSeries(league.getPlayoff())) {
			if (series == null || series.isFinished()) {
				continue;
			}
			count += 2;
		}
		return count;
	}

	private int countImportantGamesForSeasonMonth(int month, boolean playoff) {
		int count = 0;
		if (playoff) {
			if (league.getPlayoff() == null || league.getPlayoff().getNbaCalendar() == null) {
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
			return count;
		}

		if (league.getRegularSeason() == null || league.getRegularSeason().getNbaCalendar() == null) {
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
		return count;
	}

	private boolean matchesFinanceMonth(java.time.LocalDate date, int month) {
		int startMonth = league.getRegularSeason().getDebutDate().getMonthValue();
		int monthDelta = date.getMonthValue() - startMonth;
		if (monthDelta < 0) {
			monthDelta += 12;
		}
		return (monthDelta + 1) == month;
	}

	private boolean isImportantGame(Game game, java.time.LocalDate date) {
		return CalendarUtility.popularityScoreGame(game, date) >= 95 || game.getGameContext().isRivalry();
	}

	private boolean hasHighAttendance(Game game) {
		if (financeManager == null) {
			return false;
		}
		GameStat gameStat = financeManager.getGameStat(game);
		return gameStat != null && gameStat.getAttendanceRate() > 0.85;
	}
}

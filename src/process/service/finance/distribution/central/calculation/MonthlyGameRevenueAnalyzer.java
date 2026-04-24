package process.service.finance.distribution.central.calculation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import data.calendar.GameDay;
import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import process.service.finance.FinanceManager;
import process.utility.CalendarUtility;

public class MonthlyGameRevenueAnalyzer {

	private final League league;
	private FinanceManager financeManager;

	public MonthlyGameRevenueAnalyzer(League league) {
		this.league = league;
	}

	public void setFinanceManager(FinanceManager financeManager) {
		this.financeManager = financeManager;
	}

	public double calculateMonthlyLeagueAttractiveness(int month) {
		double totalScore = 0.0;
		int gameCount = 0;

		for (GameDay gameDay : getAllGameDaysForMonth(month)) {
			LocalDate date = gameDay.getDate();
			for (Game game : gameDay.getGames()) {
				totalScore += CalendarUtility.popularityScoreGame(game, date);
				gameCount++;
			}
		}

		if (gameCount == 0) {
			return 0.0;
		}

		return totalScore / gameCount;
	}

	public int countImportantGamesInMonth(int month) {
		int count = 0;
		count += countImportantGamesForSeasonMonth(month, false);
		count += countImportantGamesForSeasonMonth(month, true);
		return count;
	}

	public int countPlayoffGamesInMonth(int month) {
		int count = 0;

		for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
			LocalDate date = gameDay.getDate();
			if (date == null || !matchesFinanceMonth(date, month)) {
				continue;
			}
			count += gameDay.getGames().size();
		}
		return count;
	}

	public List<GameDay> getAllGameDaysForMonth(int month) {
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

		return gameDays;
	}

	public int countActivePlayoffTeams() {
		if (league.getPlayoff() == null || league.getPlayoff().getCurrentRound() == null) {
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
		return activeTeams.size();
	}

	public boolean isImportantGame(Game game, LocalDate date) {
		return CalendarUtility.popularityScoreGame(game, date) >= 72 || game.getGameContext().isRivalry();
	}

	public boolean hasHighAttendance(Game game) {
		if (financeManager == null) {
			return false;
		}
		GameStat gameStat = financeManager.getGameStat(game);
		return gameStat != null && gameStat.getAttendanceRate() > 0.85;
	}

	public double getAttendanceRate(Game game) {
		if (financeManager == null) {
			return 0.0;
		}
		GameStat gameStat = financeManager.getGameStat(game);
		if (gameStat == null) {
			return 0.0;
		}
		return gameStat.getAttendanceRate();
	}

	private int countImportantGamesForSeasonMonth(int month, boolean playoff) {
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
		return count;
	}

	private boolean matchesFinanceMonth(LocalDate date, int month) {
		int startMonth = league.getRegularSeason().getDebutDate().getMonthValue();
		int monthDelta = date.getMonthValue() - startMonth;
		if (monthDelta < 0) {
			monthDelta += 12;
		}
		return (monthDelta + 1) == month;
	}
}

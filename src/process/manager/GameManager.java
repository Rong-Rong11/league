package process.manager;

import java.time.LocalDate;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.league.League;
import data.league.Playoff;
import data.league.RegularSeason;
import data.sport.setup.Game;
import process.simulator.GameSimulator;
import process.utilitary.CalendarUtilitary;

public class GameManager {

	private League league;
	private GameSimulator gameSimulator = new GameSimulator();
	private FinanceManager financeManager;

	public GameManager(League league, FinanceManager financeManager) {
		this.league = league;
		this.financeManager = financeManager;
	}

	public boolean simulateDay(League league, LocalDate date, int month) {
		RegularSeason regularSeason = league.getReagularSeason();
		Playoff playoff = league.getPlayoff();
		TreeMap<LocalDate, GameDay> regularSeasonCalendar = regularSeason.getCalendar().getCalendar();
		TreeMap<LocalDate, GameDay> playoffCalendar = playoff.getCalendar().getCalendar();
		if (CalendarUtilitary.checkDate(date, regularSeason.getDebutDate(), regularSeason.getEndDate())) {
			return simulateGameDay(regularSeasonCalendar, date, month);
		}
		if (CalendarUtilitary.checkDate(date, playoff.getDebutDate(), playoff.getEndDate())) {
			return simulateGameDay(playoffCalendar, date, month);
		}
		return false;
	}

	private boolean simulateGameDay(TreeMap<LocalDate, GameDay> calendar, LocalDate date, int month) {
		GameDay gameDay = calendar.get(date);
		if (gameDay != null && !gameDay.isSimulated()) {
			for (Game game : gameDay.getGames()) {
				gameSimulator.simulateGame(game);
				financeManager.calculateGame(game, date, month);
			}

			gameDay.setSimulated(true);
			return true;
		}
		return false;
	}

	public void setLeague(League league) {
		this.league = league;
	}

}

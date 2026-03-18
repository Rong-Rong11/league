package process.manager.submanager;

import java.time.LocalDate;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.league.League;
import data.league.Playoff;
import data.league.Ranking;
import data.league.RegularSeason;
import data.sport.setup.Game;
import process.simulator.GameSimulator;
import process.utilitary.CalendarUtilitary;

public class GameManager {

	private League league;
	private GameSimulator gameSimulator = new GameSimulator();
	private FinanceManager financeManager;
	private RegularSeasonRankingManager regularSeasonRankingManager = new RegularSeasonRankingManager() ; 

	public GameManager(League league, FinanceManager financeManager) {
		this.league = league;
		this.financeManager = financeManager;
	}

	public boolean simulateRegularSeasonDay(LocalDate date, int month) {
		RegularSeason regularSeason = league.getReagularSeason();
		Playoff playoff = league.getPlayoff();
		TreeMap<LocalDate, GameDay> regularSeasonCalendar = regularSeason.getCalendar().getCalendar();
		Ranking ranking = regularSeason.getRanking() ; 
		if(simulateGameDay(regularSeasonCalendar, date, month)) {
			regularSeasonRankingManager.updateRanking(ranking);
			return true ; 
		}
		return false ; 
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

	public boolean simulateGameDay(LocalDate date, int month) {
		RegularSeason regularSeason = league.getReagularSeason();
		TreeMap<LocalDate, GameDay> regularSeasonCalendar = regularSeason.getCalendar().getCalendar();
		return simulateGameDay(regularSeasonCalendar, date, month);
	}

	public boolean simulateGame(Game game, LocalDate date, int month) {
		if (game == null) {
			return false;
		}
		gameSimulator.simulateGame(game);
		financeManager.calculateGame(game, date, month);
		return true;
	}

	public void setLeague(League league) {
		this.league = league;
	}

}

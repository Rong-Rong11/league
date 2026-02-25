package data.team.calendar;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.TreeMap;

import data.sport.setup.Game;


public class Schedule {
	private int numberOfPlayedGames ; 
	private int numberOfAwayGames ; 
	private int numberOfHomeGames ; 
	private ArrayList<Game> games ;
	private TreeMap<LocalDate, Game> scheduledGames ; 
	private TreeMap<LocalDate, Game> playedGames ; 
	
	public Schedule() {
		setNumberOfAwayGames(0);
		setNumberOfHomeGames(0);
		setNumberOfPlayedGames(0);
		games = new ArrayList<Game>() ; 
		scheduledGames = new TreeMap<LocalDate, Game>(); 
		playedGames = new TreeMap<LocalDate, Game>() ; 
	}

	public int getNumberOfPlayedGames() {
		return numberOfPlayedGames;
	}
	public void setNumberOfPlayedGames(int numberOfPlayedGames) {
		this.numberOfPlayedGames = numberOfPlayedGames;
	}
	public int getNumberOfAwayGames() {
		return numberOfAwayGames;
	}
	public void setNumberOfAwayGames(int numberOfAwayGames) {
		this.numberOfAwayGames = numberOfAwayGames;
	}
	public int getNumberOfHomeGames() {
		return numberOfHomeGames;
	}
	public void setNumberOfHomeGames(int numberOfHomeGames) {
		this.numberOfHomeGames = numberOfHomeGames;
	}
	public ArrayList<Game> getGames() {
		return games;
	}
	public void setGames(ArrayList<Game> games) {
		this.games = games;
	} 
	
	public void addGame(Game game) {
		games.add(game); 
		
	}
	public void incrementNumberOfAwayGames() {
		numberOfAwayGames ++ ; 
	}
	
	public void incrementNumberOfHomeGames() {
		numberOfHomeGames ++ ; 
	}
	
	public boolean isPlayingOn(LocalDate date) {
		return scheduledGames.containsKey(date) ; 
	}
	
	public int daysSinceLastGame(LocalDate date) {
		if (playedGames.isEmpty()) {
			return Integer.MAX_VALUE ; 
		}
		LocalDate lastGameDate = playedGames.lowerKey(date);
		return (int) ChronoUnit.DAYS.between(lastGameDate, date);
	}
	
	public void scheduleGame(LocalDate date , Game game) {
		scheduledGames.put(date, game);
	}
	
	
	public void clearGames() {
		games.clear(); 
	}
	
	public void clearScheduledGames() {
		scheduledGames.clear();
	}

	public TreeMap<LocalDate, Game> getScheduledGames() {
		return scheduledGames;
	}

}

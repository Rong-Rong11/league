package process;

import java.time.LocalDate;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.league.League;
import data.league.Season;
import data.sport.setup.Game;
import process.builder.CalendarBuilder;
import process.builder.LeagueBuilder;

public class LeagueManager {
	private LeagueBuilder leagueBuilder = new LeagueBuilder() ; 
	private League league ; 
	private GameSimulator gameSimulator = new GameSimulator() ; 
	
	
	public LeagueManager() {
		league = new League() ; 
	}
	
	public void buildLeague() {
		league = leagueBuilder.build() ; 
	}
	
	public void buildRegularSeasonCalendar() {
		CalendarBuilder.initialization(league);
		CalendarBuilder.specialEventsPlacement(league.getReagularSeason());
		CalendarBuilder.generateAllGames(league);
		CalendarBuilder.generateRegulaSeasonCalendar(league) ; 
	}
	
	public boolean simulateDay(LocalDate date, Season season) {
		TreeMap<LocalDate, GameDay> calendar = season.getCalendar().getCalendar() ; 
		if(checkDate(date, season.getDebutDate(), season.getEndDate()))  {
			GameDay gameDay = calendar.get(date) ; 
			if(gameDay != null && !gameDay.isSimulated()) {
				for(Game game : gameDay.getGames()) {
					gameSimulator.simulateGame(game);
				}
				gameDay.setSimulated(true);
				return true ; 
				
			}
		}
		return false ; 
	}
	
	private static boolean checkDate(LocalDate checkedDate, LocalDate debutDate , LocalDate endDate) {
		return ((checkedDate.isEqual(debutDate)) || checkedDate.isAfter(debutDate)) &&
				(checkedDate.isBefore(endDate) || checkedDate.isEqual(endDate)) ; 
				
	}
	
	public League getLeague() {
		return league ; 
	}
}

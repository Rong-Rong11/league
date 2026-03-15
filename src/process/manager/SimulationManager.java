package process.manager;
import config.CalendarConfiguration;

import java.time.LocalDate;
import java.time.Month;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.league.League;
import data.sport.setup.Game;
import process.utilitary.CalendarUtilitary;

//cerveau de la simulation 
public class SimulationManager {
	private LeagueManager leagueManager = new LeagueManager();
	private int month = 1;
	private Month debutMonthDate = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE.getMonth();
	private Month currentMonthDate = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE.getMonth();
	private LocalDate date = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE;

	public SimulationManager() {
		
	}

	public void randomFinance() {
		leagueManager.randomFinancialProfil();
	}
	
	//méthode à utiliser pour lancer la saison 
	public void startSeason() {
		leagueManager.startSeason();
		simulateCurrentSeason();
		resetCalendarCursor();
	}
	
	//passe le prochain jour, méthode à utiliser pour la simulation et tout se fais tous seul 
	public void nextDay() {
		date = date.plusDays(1);
		currentMonthDate = date.getMonth();
	}
	
	// si nouveau mois les évènements des nouveaux mois sont appliqués comme le partage des revenus etc ...
	private void verifyMonth() {
		int monthsBetween = currentMonthDate.getValue() - debutMonthDate.getValue();
		if (monthsBetween < 0) {
			monthsBetween += 12;
		}
		int newMonth = monthsBetween + 1;
		if (newMonth != month) {
			month = newMonth ; 
			leagueManager.newMonth(month);
		}
	}
	
	//simuler jour par jour
	private void simulateDay() {
		if(CalendarUtilitary.checkDate(date, CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE, CalendarConfiguration.REGULAR_SEASON_END_DATE)) {
			leagueManager.simulateRegularSeasonDay(date, month) ; 
		}
		if(CalendarUtilitary.checkDate(date, CalendarConfiguration.PLAYOFF_DEBUT_DATE, CalendarConfiguration.PLAYOFF_END_DATE)) {
			
		}
		
	}
	
	//simuler la fin de saison régulière ou fin playoff
	public void simulateCurrentSeason() {
		if(CalendarUtilitary.checkDate(date, CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE, CalendarConfiguration.REGULAR_SEASON_END_DATE)) {
			while(!date.equals(CalendarConfiguration.REGULAR_SEASON_END_DATE)) {
	        simulateDay();
	        nextDay();
			}
		}
		else if(CalendarUtilitary.checkDate(date, CalendarConfiguration.PLAYOFF_DEBUT_DATE, CalendarConfiguration.PLAYOFF_END_DATE)) {
				while(!date.equals(CalendarConfiguration.PLAYOFF_END_DATE)) {
			        simulateDay();
			        nextDay();
			}
		}    
	}

	private void resetCalendarCursor() {
		date = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE;
		debutMonthDate = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE.getMonth();
		currentMonthDate = debutMonthDate;
		month = 1;
	}

	public void displayGameDay(LocalDate date) {
		if (date == null) {
			return;
		}
		GameDay gameDay = leagueManager.getLeague().getReagularSeason().getCalendar().getCalendar().get(date);
		if (gameDay != null) {
			gameDay.setDisplayed(true);
			for (data.sport.setup.Game game : gameDay.getGames()) {
				game.setDisplayed(true);
			}
		}
	}

	public void displayWeek(LocalDate startDate) {
		if (startDate == null) {
			return;
		}
		for (int offset = 0; offset < 7; offset++) {
			displayGameDay(startDate.plusDays(offset));
		}
	}

	public void displayCurrentSeason() {
		TreeMap<LocalDate, GameDay> calendar = leagueManager.getLeague().getReagularSeason().getCalendar().getCalendar();
		for (GameDay gameDay : calendar.values()) {
			gameDay.setDisplayed(true);
			for (Game game : gameDay.getGames()) {
				game.setDisplayed(true);
			}
		}
	}
	
	public League getLeague() {
		return leagueManager.getLeague(); 
	}

	public LeagueManager getLeagueManager() {
		return leagueManager;
	}

	public LocalDate getCurrentDate() {
		return date;
	}
}

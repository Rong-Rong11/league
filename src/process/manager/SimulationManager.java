package process.manager;

import java.time.LocalDate;
import java.time.Month;

import config.CalendarConfiguration;
import data.league.League;
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
		simulateDay(); //simule le premier jour 
	}
	
	//passe le prochain jour, méthode à utiliser pour la simulation et tout se fais tous seul 
	public void nextDay() {
		date = date.plusDays(1);
		currentMonthDate = date.getMonth();
		verifyMonth();
		simulateDay();
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
	
	public League getLeague() {
		return leagueManager.getLeague(); 
	}
}

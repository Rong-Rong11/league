package process.manager;

import java.time.LocalDate;
import java.time.Month;

import config.SimulationConfiguration;

//cerveau de la simulation
public class SimulationManager {
	private LeagueManager leagueManager = new LeagueManager();
	private int month = 1;
	private Month debutMonthDate = SimulationConfiguration.REGULAR_SEASON_DEBUT_DATE.getMonth();
	private Month currentMonthDate = SimulationConfiguration.REGULAR_SEASON_DEBUT_DATE.getMonth();
	private LocalDate date = SimulationConfiguration.REGULAR_SEASON_DEBUT_DATE;

	public SimulationManager() {
		leagueManager.buildLeague();
	}

	public void randomFinance() {
		leagueManager.randomFinancialProfil();
	}

	public void startSeason() {
		leagueManager.startSeason();
	}

	public void nextDay() {
		date = date.plusDays(1);
		currentMonthDate = date.getMonth();
		verifyMonth();
	}

	private void verifyMonth() {
		int monthsBetween = currentMonthDate.getValue() - debutMonthDate.getValue();
		if (monthsBetween < 0) {
			monthsBetween += 12;
		}
		month = monthsBetween + 1;
	}

	public void simulateDay() {
		leagueManager.simulateDay(date, month);
	}
}

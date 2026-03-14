package config;

import java.time.LocalDate;

public class CalendarConfiguration {
	// League calendar
	public static final int NUMBER_OF_TEAM = 30 ; 
	public static final int SEASON_YEAR = 2026 ; 
	public static final LocalDate REGULAR_SEASON_DEBUT_DATE = LocalDate.of(SEASON_YEAR - 1, 10, 21) ; 
	public static final LocalDate REGULAR_SEASON_END_DATE = LocalDate.of(SEASON_YEAR, 4, 12) ; 
	public static final LocalDate PLAYOFF_DEBUT_DATE = LocalDate.of(SEASON_YEAR, 4, 18) ; 
	public static final LocalDate PLAYOFF_END_DATE = LocalDate.of(SEASON_YEAR, 6, 15) ; 
	public static final LocalDate CHRISTMAS_DAY = LocalDate.of(SEASON_YEAR - 1, 12, 25) ; 

	// Scheduling rules
	public static final int MAX_GAMES_PER_DAY = 10 ;

	private CalendarConfiguration() {
	}
}

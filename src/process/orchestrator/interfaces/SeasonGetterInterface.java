package process.orchestrator.interfaces;

import java.time.LocalDate;
import java.util.TreeMap;

import data.calendar.GameDay;

public interface SeasonGetterInterface {

	LocalDate getCurrentDate();

	LocalDate getRegularSeasonStartDate();

	LocalDate getRegularSeasonEndDate();

	LocalDate getCalendarDisplayDate(LocalDate simulationDate);

	LocalDate getCurrentWeekIndicatorDate();

	LocalDate getNextGameDay(LocalDate startDate);

	LocalDate getPreviousGameDay(LocalDate startDate);

	LocalDate getMatchDisplayDate();

	LocalDate getWeekStartDate(LocalDate date);

	LocalDate getWeekDisplayDate(LocalDate weekStart);

	LocalDate getDisplayedDateAfterDaySimulation(LocalDate displayedDate);

	LocalDate getDisplayedDateAfterWeekSimulation(LocalDate displayedDate);

	LocalDate getDisplayedDateAfterSeasonSimulation(LocalDate displayedDate);

	LocalDate getPreviousWeekDisplayDate(LocalDate displayedDate);

	LocalDate getNextWeekDisplayDate(LocalDate displayedDate);

	String getWeekText(LocalDate displayedDate);

	GameDay getGameDay(LocalDate date);

	TreeMap<LocalDate, GameDay> getRegularSeasonCalendar();

	TreeMap<LocalDate, GameDay> getSeasonCalendar();

	boolean isSeasonInitialized();
}

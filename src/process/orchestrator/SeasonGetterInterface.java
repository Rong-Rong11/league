package process.orchestrator;

import java.time.LocalDate;
import java.util.TreeMap;

import data.calendar.GameDay;

public interface SeasonGetterInterface {

   LocalDate getCurrentDate();

   LocalDate getRegularSeasonStartDate();

   LocalDate getRegularSeasonEndDate();

   GameDay getGameDay(LocalDate date);

   TreeMap<LocalDate, GameDay> getSeasonCalendar();

   boolean isSeasonInitialized();
}

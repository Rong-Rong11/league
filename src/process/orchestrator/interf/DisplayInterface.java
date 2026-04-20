package process.orchestrator.interf;

import java.time.LocalDate;

public interface DisplayInterface {
	void displayGameDay(LocalDate date);

	void displayWeek(LocalDate startDate);

	void displayCurrentSeason();
}

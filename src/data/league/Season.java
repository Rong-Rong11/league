package data.league;

import java.time.LocalDate;
import java.util.HashMap;

import data.calendar.NBACalendar;
import data.calendar.SpecialEvent;

public abstract class Season {
	private NBACalendar calendar;
	private LocalDate debutDate;
	private LocalDate endDate;
	private HashMap<LocalDate, SpecialEvent> specialEvents;
	private Ranking ranking;

	public Season(LocalDate debutDate, LocalDate endDate) {
		calendar = new NBACalendar();
		this.debutDate = debutDate;
		this.endDate = endDate;
		specialEvents = new HashMap<LocalDate, SpecialEvent>();
		ranking = new Ranking();
	}

	public LocalDate getDebutDate() {
		return debutDate;
	}

	public void setDebutDate(LocalDate debutDate) {
		this.debutDate = debutDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public NBACalendar getCalendar() {
		return calendar;
	}

	public void setCalendar(NBACalendar calendar) {
		this.calendar = calendar;
	}

	public void addSpecialEvents(SpecialEvent specialEvent) {
		if (!specialEvents.containsKey(specialEvent.getDate())) {
			specialEvents.put(specialEvent.getDate(), specialEvent);
		}
	}

}

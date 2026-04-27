package data.calendar;

import java.time.LocalDate;

public class SpecialEvent
extends GameDay {
	String event;
	LocalDate date;

	public SpecialEvent(LocalDate date, String event) {
		super(date);
		this.event = event;
	}
}

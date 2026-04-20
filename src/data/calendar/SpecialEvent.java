/*
	* Decompiled with CFR 0.152.
	*/
package data.calendar;

import java.time.LocalDate;

public class SpecialEvent
extends GameDay {
	String event;
	LocalDate date;

	public SpecialEvent(LocalDate localDate, String string) {
		super(localDate);
		this.event = string;
	}
}

package data.calendar;

import java.time.LocalDate;
import java.util.TreeMap;

public class NBACalendar {
	private TreeMap<LocalDate, GameDay> calendar;

	public NBACalendar() {
		calendar = new TreeMap<LocalDate, GameDay>();
	}

	public TreeMap<LocalDate, GameDay> getCalendar() {
		return calendar;
	}

	public void setCalendar(TreeMap<LocalDate, GameDay> calendar) {
		this.calendar = calendar;
	}

}

package process.builder.calendartools;

import config.CalendarConfiguration;
import data.calendar.SpecialEvent;
import data.league.RegularSeason;
import process.utility.CalendarUtilitary;

public class SpecialEventPlanner {
    public static void specialEventsPlacement(RegularSeason regularSeason) {
        regularSeason.addSpecialEvents(new SpecialEvent(CalendarConfiguration.CHRISTMAS_DAY, "christmas"));
        regularSeason.addSpecialEvents(new SpecialEvent(regularSeason.getDebutDate(), "opening night"));
        regularSeason.addSpecialEvents(new SpecialEvent(regularSeason.getEndDate(), "ending night"));
        regularSeason.addSpecialEvents(new SpecialEvent(CalendarUtilitary.getMLKDay(), "mlk day"));
    }
}

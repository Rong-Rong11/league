package process.builder.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.league.League;
import data.league.RegularSeason;
import data.sport.setup.Game;
import process.builder.calendar.tools.GameGenerator;
import process.builder.calendar.tools.GameSelector;
import process.builder.calendar.tools.ScheduleNotifier;
import process.builder.calendar.tools.SpecialEventPlanner;

public class RegularSeasonCalendarBuilder extends CalendarBuilder {
    private GameSelector gameSelector;

    public RegularSeasonCalendarBuilder(League league) {
        super(league);
        this.gameSelector = new GameSelector(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE, league);
    }

    private void specialEventsPlacement() {
        SpecialEventPlanner.specialEventsPlacement(getLeague().getReagularSeason());
    }

    protected void generateGames() {
        GameGenerator.generateAllGamesRegularSeason(getLeague());
    }

    public NBACalendar build() {
        specialEventsPlacement();
        RegularSeason regularSeason = getLeague().getReagularSeason();
        TreeMap<LocalDate, GameDay> calendar = new TreeMap<LocalDate, GameDay>();
        LocalDate debutDate = regularSeason.getDebutDate();
        LocalDate endDate = regularSeason.getEndDate();
        LocalDate currentDate = debutDate;
        while (!currentDate.isAfter(endDate)) {
            GameDay gameDay = new GameDay(currentDate);
            gameSelector.setDate(currentDate);
            ArrayList<Game> arrayList = gameSelector.selectGamesForDay();
            gameDay.setGames(arrayList);
            for (Game game : arrayList) {
                ScheduleNotifier.notifySchedule(currentDate, game);
            }
            if (!gameDay.isEmpty()) {
                calendar.put(currentDate, gameDay);
            }
            currentDate = currentDate.plusDays(1L);
        }
        NBACalendar newCalendar = new NBACalendar(calendar);
        return newCalendar;

    }
}

package process.builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.league.League;
import data.league.RegularSeason;
import data.sport.setup.Game;
import process.builder.calendartools.GameGenerator;
import process.builder.calendartools.GameSelector;
import process.builder.calendartools.ScheduleReset;
import process.builder.calendartools.SpecialEventPlanner;

public class CalendarBuilder {
    private ScheduleReset scheduleReset = new ScheduleReset();
    private GameSelector gameSelector;
    private League league;

    public CalendarBuilder(League league) {
        this.league = league;
        this.gameSelector = new GameSelector(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE, league);
    }

    private void resetSchedule() {
        this.scheduleReset.initialization();
    }

    private void specialEventsPlacement() {
        SpecialEventPlanner.specialEventsPlacement(this.league.getReagularSeason());
    }

    private void generateAllGames() {
        GameGenerator.generateAllGamesRegularSeason(this.league);
    }

    public void buildRegulaSeasonCalendar() {
        this.resetSchedule();
        this.specialEventsPlacement();
        this.generateAllGames();
        RegularSeason regularSeason = this.league.getReagularSeason();
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
                game.getGameContext().setScheduled(true);
                game.getGameContext().getHomeTeam().getSchedule().scheduleGame(currentDate, game);
                game.getGameContext().getAwayTeam().getSchedule().scheduleGame(currentDate, game);
            }
            if (!gameDay.isEmpty()) {
                calendar.put(currentDate, gameDay);
            }
            currentDate = currentDate.plusDays(1L);
        }
        this.league.getReagularSeason().getCalendar().setCalendar(calendar);
    }
}

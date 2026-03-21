package process.builder;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.league.League;
import data.league.RegularSeason;
import data.sport.setup.Game;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;
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
        TreeMap<LocalDate, GameDay> treeMap = new TreeMap<LocalDate, GameDay>();
        LocalDate localDate = regularSeason.getDebutDate();
        LocalDate localDate2 = regularSeason.getEndDate();
        LocalDate localDate3 = localDate;
        while (!localDate3.isAfter(localDate2)) {
            GameDay gameDay = new GameDay(localDate3);
            gameSelector.setDate(localDate3);
            ArrayList<Game> arrayList = gameSelector.selectGamesForDay();
            gameDay.setGames(arrayList);
            for (Game game : arrayList) {
                game.getGameContext().setScheduled(true);
                game.getGameContext().getHomeTeam().getSchedule().scheduleGame(localDate3, game);
                game.getGameContext().getAwayTeam().getSchedule().scheduleGame(localDate3, game);
            }
            if (!gameDay.isEmpty()) {
                treeMap.put(localDate3, gameDay);
            }
            localDate3 = localDate3.plusDays(1L);
        }
        this.league.getReagularSeason().getCalendar().setCalendar(treeMap);
    }
}

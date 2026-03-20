package process.builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.league.League;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
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

    private void generateFirstRoundPlayoffGames() {
        GameGenerator.generateFirstRoundPlayoffGames(league.getPlayoff());
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
                notifySchedule(currentDate, game);
            }
            if (!gameDay.isEmpty()) {
                calendar.put(currentDate, gameDay);
            }
            currentDate = currentDate.plusDays(1L);
        }
        this.league.getReagularSeason().getCalendar().setCalendar(calendar);
    }

    public void buildFirstRoundCalendar() {
        resetSchedule();
        TreeMap<LocalDate, GameDay> playoffCalendar = new TreeMap<>();

        LocalDate startDate = config.CalendarConfiguration.PLAYOFF_DEBUT_DATE;

        scheduleConferenceFirstFourGames(playoffCalendar, league.getPlayoff().getEastFirstRound(), startDate);
        scheduleConferenceFirstFourGames(playoffCalendar, league.getPlayoff().getWestFirstRound(), startDate);

        league.getPlayoff().getCalendar().setCalendar(playoffCalendar);
    }

    private void scheduleConferenceFirstFourGames(TreeMap<LocalDate, GameDay> playoffCalendar,
            ArrayList<PlayoffSeries> roundSeries,
            LocalDate startDate) {

        int[] gameOffsets = { 0, 2, 4, 7 };
        int[] seriesStartOffsets = { 0, 0, 1, 1 };

        for (int seriesIndex = 0; seriesIndex < roundSeries.size(); seriesIndex++) {
            PlayoffSeries series = roundSeries.get(seriesIndex);
            Game[] expectedGames = series.getExpectedGames();

            int seriesStartOffset = (seriesIndex < seriesStartOffsets.length)
                    ? seriesStartOffsets[seriesIndex]
                    : seriesIndex % 2;

            for (int i = 0; i < 4; i++) {
                Game game = expectedGames[i];
                LocalDate gameDate = startDate.plusDays(seriesStartOffset + gameOffsets[i]);
                addGameToCalendar(playoffCalendar, game, gameDate);
                notifySchedule(gameDate, game);
            }
        }
    }

    public void scheduleNextGameIfNecessary(TreeMap<LocalDate, GameDay> playoffCalendar, PlayoffSeries series,
            LocalDate lastGameDate) {
        if (series.isFinished()) {
            return;
        }
        int nextGameIndex = series.getNumberPlayedGames();
        Game[] expectedGames = series.getExpectedGames();
        if (nextGameIndex >= expectedGames.length) {
            return;
        }
        Game nextGame = expectedGames[nextGameIndex];
        LocalDate nextDate = lastGameDate.plusDays(2);
        addGameToCalendar(playoffCalendar, nextGame, nextDate);
        notifySchedule(nextDate, nextGame);
    }

    private void addGameToCalendar(TreeMap<LocalDate, GameDay> playoffCalendar, Game game, LocalDate gameDate) {
        if (!playoffCalendar.containsKey(gameDate)) {
            GameDay gameDay = new GameDay(gameDate);
            gameDay.addGame(game);
            playoffCalendar.put(gameDate, gameDay);
        } else {
            playoffCalendar.get(gameDate).addGame(game);
        }
    }

    private void notifySchedule(LocalDate date, Game game) {
        game.getGameContext().setScheduled(true);
        game.getGameContext().getHomeTeam().getSchedule().scheduleGame(date, game);
        game.getGameContext().getAwayTeam().getSchedule().scheduleGame(date, game);
    }
}

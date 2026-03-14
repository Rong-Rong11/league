package process.builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import config.SimulationConfiguration;
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
		gameSelector = new GameSelector(SimulationConfiguration.REGULAR_SEASON_DEBUT_DATE, league);
	}

	private void resetSchedule() {
		scheduleReset.initialization();
	}

	private void specialEventsPlacement() {
		SpecialEventPlanner.specialEventsPlacement(league.getReagularSeason());
	}

	private void generateAllGames() {
		GameGenerator.generateAllGamesRegularSeason(league);
	}

	public void buildRegulaSeasonCalendar() {
		resetSchedule();
		specialEventsPlacement();
		generateAllGames();

		RegularSeason regularSeason = league.getReagularSeason();
		TreeMap<LocalDate, GameDay> calendar = new TreeMap<LocalDate, GameDay>();
		LocalDate debutDate = regularSeason.getDebutDate();
		LocalDate endDate = regularSeason.getEndDate();

		for (LocalDate date = debutDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			GameDay gameDay = new GameDay(date);
			gameSelector.setDate(date);

			ArrayList<Game> games = gameSelector.selectGamesForDay();

			gameDay.setGames(games);
			for (Game game : games) {
				game.getGameContext().setScheduled(true);
				game.getGameContext().getHomeTeam().getSchedule().scheduleGame(date, game);
				game.getGameContext().getAwayTeam().getSchedule().scheduleGame(date, game);
			}
			if (!gameDay.isEmpty()) {
				calendar.put(date, gameDay);
			}
		}
		league.getReagularSeason().getCalendar().setCalendar(calendar);
	}

} 

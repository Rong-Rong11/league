package process.builder.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.league.League;
import data.league.RegularSeason;
import data.sport.setup.Afternoon;
import data.sport.setup.Evening;
import data.sport.setup.Game;
import data.sport.setup.Night;
import process.builder.calendar.tools.GameGenerator;
import process.builder.calendar.tools.GameSelector;
import process.builder.calendar.tools.ScheduleNotifier;
import process.builder.calendar.tools.SpecialEventPlanner;
import process.utility.CalendarUtilitary;

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
			assignGameMoments(arrayList, currentDate);
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

	private void assignGameMoments(ArrayList<Game> games, LocalDate date) {
		ArrayList<Game> sortedGames = getGamesSortedByImportance(games, date);
		int gameCount = sortedGames.size();

		if (gameCount == 0) {
			return;
		}

		int veryImportantLimit = Math.max(1, gameCount / 3);
		int importantLimit = Math.max(1, (gameCount * 2) / 3);

		for (int index = 0; index < sortedGames.size(); index++) {
			Game game = sortedGames.get(index);

			if (index < veryImportantLimit) {
				game.getGameContext().setGameMoment(new Afternoon());
			} else if (index < importantLimit) {
				game.getGameContext().setGameMoment(new Night());
			} else {
				game.getGameContext().setGameMoment(new Evening());
			}
		}
	}

	private ArrayList<Game> getGamesSortedByImportance(ArrayList<Game> games, LocalDate date) {
		ArrayList<Game> sortedGames = new ArrayList<Game>(games);
		Collections.sort(sortedGames, new GameImportanceComparator(date));
		return sortedGames;
	}

	private class GameImportanceComparator implements java.util.Comparator<Game> {
		private final LocalDate date;

		private GameImportanceComparator(LocalDate date) {
			this.date = date;
		}

		@Override
		public int compare(Game gameA, Game gameB) {
			return Double.compare(
					CalendarUtilitary.popularityScoreGame(gameB, date),
					CalendarUtilitary.popularityScoreGame(gameA, date));
		}
	}

}

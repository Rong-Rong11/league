package process.builder.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.league.League;
import data.league.RegularSeason;
import data.sport.setup.Afternoon;
import data.sport.setup.Evening;
import data.sport.setup.Game;
import data.sport.setup.Night;
import process.builder.calendar.generator.RegularSeasonGameGenerator;
import process.builder.calendar.schedule.ScheduleNotifier;
import process.builder.calendar.schedule.SpecialEventPlanner;
import process.builder.calendar.selector.GameSelector;
import process.utility.CalendarUtility;
import log.LoggerUtility;

public class RegularSeasonCalendarBuilder extends CalendarBuilder {
	private static final Logger logger = LoggerUtility.getLogger(RegularSeasonCalendarBuilder.class, "text");
	private GameSelector gameSelector;

	public RegularSeasonCalendarBuilder(League league) {
		super(league);
		this.gameSelector = new GameSelector(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE, league);
	}

	private void specialEventsPlacement() {
		SpecialEventPlanner.specialEventsPlacement(getLeague().getRegularSeason());
	}

	protected void generateGames() {
		RegularSeasonGameGenerator.generateAllGamesRegularSeason(getLeague());
	}

	public NBACalendar build() {
		logger.info("Building regular season calendar");
		specialEventsPlacement();
		RegularSeason regularSeason = getLeague().getRegularSeason();
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
		logger.info("Regular season calendar built with " + calendar.size() + " game days");
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
				game.getGameContext().setGameMoment(new Evening());
			} else if (index < importantLimit) {
				game.getGameContext().setGameMoment(new Night());
			} else {
				game.getGameContext().setGameMoment(new Afternoon());
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
					CalendarUtility.popularityScoreGame(gameB, date),
					CalendarUtility.popularityScoreGame(gameA, date));
		}
	}

}

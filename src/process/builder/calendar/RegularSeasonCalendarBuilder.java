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
		logger.debug("Placing regular season special events");
		SpecialEventPlanner.specialEventsPlacement(getLeague().getRegularSeason());
	}

	protected void generateGames() {
		if (getLeague() == null) {
			logger.warn("Skipping regular season game generation because league is null");
			return;
		}
		logger.debug("Generating all regular season games before calendar assembly");
		RegularSeasonGameGenerator.generateAllGamesRegularSeason(getLeague());
	}

	public NBACalendar build() {
		if (getLeague() == null || getLeague().getRegularSeason() == null) {
			logger.warn("Skipping regular season calendar build because league or regular season is null");
			return null;
		}
		if (gameSelector == null) {
			logger.warn("Skipping regular season calendar build because game selector is null");
			return null;
		}

		logger.info("Building regular season calendar");
		specialEventsPlacement();
		RegularSeason regularSeason = getLeague().getRegularSeason();
		TreeMap<LocalDate, GameDay> calendar = new TreeMap<LocalDate, GameDay>();
		LocalDate debutDate = regularSeason.getDebutDate();
		LocalDate endDate = regularSeason.getEndDate();
		logger.debug("Regular season calendar range is " + debutDate + " to " + endDate);
		LocalDate currentDate = debutDate;
		while (!currentDate.isAfter(endDate)) {
			logger.trace("Building regular season game day for " + currentDate);
			GameDay gameDay = new GameDay(currentDate);
			gameSelector.setDate(currentDate);
			ArrayList<Game> arrayList = gameSelector.selectGamesForDay();
			logger.debug("Selected " + arrayList.size() + " regular season games for " + currentDate);
			gameDay.setGames(arrayList);
			assignGameMoments(arrayList, currentDate);
			for (Game game : arrayList) {
				ScheduleNotifier.notifySchedule(currentDate, game);
			}
			if (!gameDay.isEmpty()) {
				calendar.put(currentDate, gameDay);
				logger.trace("Added non-empty regular season game day for " + currentDate);
			}
			currentDate = currentDate.plusDays(1L);
		}
		NBACalendar newCalendar = new NBACalendar(calendar);
		logger.info("Regular season calendar built with " + calendar.size() + " game days");
		return newCalendar;

	}

	private void assignGameMoments(ArrayList<Game> games, LocalDate date) {
		if (games == null) {
			logger.warn("Skipping game moment assignment because games list is null for " + date);
			return;
		}
		if (date == null) {
			logger.warn("Skipping game moment assignment because date is null");
			return;
		}

		ArrayList<Game> sortedGames = getGamesSortedByImportance(games, date);
		int gameCount = sortedGames.size();
		logger.debug("Assigning game moments for " + gameCount + " games on " + date);

		if (gameCount == 0) {
			logger.trace("No games to assign moments for on " + date);
			return;
		}

		int veryImportantLimit = Math.max(1, gameCount / 3);
		int importantLimit = Math.max(1, (gameCount * 2) / 3);

		for (int index = 0; index < sortedGames.size(); index++) {
			Game game = sortedGames.get(index);

			if (index < veryImportantLimit) {
				game.getGameContext().setGameMoment(new Evening());
				logger.trace("Assigned Evening to "
						+ game.getGameContext().getHomeTeam().getName()
						+ " vs "
						+ game.getGameContext().getAwayTeam().getName()
						+ " on "
						+ date);
			} else if (index < importantLimit) {
				game.getGameContext().setGameMoment(new Night());
				logger.trace("Assigned Night to "
						+ game.getGameContext().getHomeTeam().getName()
						+ " vs "
						+ game.getGameContext().getAwayTeam().getName()
						+ " on "
						+ date);
			} else {
				game.getGameContext().setGameMoment(new Afternoon());
				logger.trace("Assigned Afternoon to "
						+ game.getGameContext().getHomeTeam().getName()
						+ " vs "
						+ game.getGameContext().getAwayTeam().getName()
						+ " on "
						+ date);
			}
		}
	}

	private ArrayList<Game> getGamesSortedByImportance(ArrayList<Game> games, LocalDate date) {
		logger.trace("Sorting " + games.size() + " games by importance for " + date);
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

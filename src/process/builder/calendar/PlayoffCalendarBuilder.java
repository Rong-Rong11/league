package process.builder.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import data.calendar.GameDay;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.Night;
import data.sport.setup.PlayoffSeries;
import log.LoggerUtility;
import process.builder.calendar.schedule.ScheduleNotifier;

public abstract class PlayoffCalendarBuilder extends CalendarBuilder {
	private static final Logger logger = LoggerUtility.getLogger(PlayoffCalendarBuilder.class, "text");

	public PlayoffCalendarBuilder(League league) {
		super(league);
	}

	protected void scheduleRoundFirstFourGames(TreeMap<LocalDate, GameDay> playoffCalendar,
			ArrayList<PlayoffSeries> roundSeries,
			LocalDate startDate) {
		if (playoffCalendar == null) {
			logger.warn("Skipping playoff round scheduling because playoff calendar is null");
			return;
		}
		if (roundSeries == null) {
			logger.warn("Skipping playoff round scheduling because round series are null");
			return;
		}
		if (startDate == null) {
			logger.warn("Skipping playoff round scheduling because start date is null");
			return;
		}

		int[] gameOffsets = { 0, 2, 4, 7 };
		int[] seriesStartOffsets = { 0, 0, 1, 1 };
		logger.debug("Scheduling first four games for "
				+ roundSeries.size()
				+ " playoff series starting on "
				+ startDate);

		for (int seriesIndex = 0; seriesIndex < roundSeries.size(); seriesIndex++) {
			PlayoffSeries series = roundSeries.get(seriesIndex);
			if (series == null) {
				logger.warn("Skipping playoff series scheduling because series is null");
				continue;
			}
			Game[] expectedGames = series.getExpectedGames();
			logger.debug("Scheduling first four games for series "
					+ series.getHigherTeam().getName()
					+ " vs "
					+ series.getLowerTeam().getName());

			int seriesStartOffset = (seriesIndex < seriesStartOffsets.length)
					? seriesStartOffsets[seriesIndex]
					: seriesIndex % 2;

			for (int i = 0; i < 4; i++) {
				Game game = expectedGames[i];
				LocalDate gameDate = startDate.plusDays(seriesStartOffset + gameOffsets[i]);
				logger.trace("Scheduling playoff game "
						+ (i + 1)
						+ " for "
						+ series.getHigherTeam().getName()
						+ " vs "
						+ series.getLowerTeam().getName()
						+ " on "
						+ gameDate);
				addGameToCalendar(playoffCalendar, game, gameDate);
				game.getGameContext().setGameMoment(new Night());
				ScheduleNotifier.notifySchedule(gameDate, game);
			}
		}
		logger.debug("Finished scheduling first four playoff games");
	}

	public void scheduleNextGameIfNecessary(TreeMap<LocalDate, GameDay> playoffCalendar, PlayoffSeries series,
			LocalDate lastGameDate) {
		if (playoffCalendar == null) {
			logger.warn("Skipping next playoff game scheduling because playoff calendar is null");
			return;
		}
		if (series == null) {
			logger.warn("Skipping next playoff game scheduling because series is null");
			return;
		}
		if (lastGameDate == null) {
			logger.warn("Skipping next playoff game scheduling because last game date is null");
			return;
		}
		if (series.isFinished()) {
			logger.debug("Skipping next playoff game scheduling because series is finished");
			return;
		}
		int nextGameIndex = series.getNumberPlayedGames();
		Game[] expectedGames = series.getExpectedGames();
		if (nextGameIndex >= expectedGames.length) {
			logger.debug("Skipping next playoff game scheduling because no expected games remain");
			return;
		}
		Game nextGame = expectedGames[nextGameIndex];
		LocalDate nextDate = lastGameDate.plusDays(2);
		logger.debug("Scheduling next playoff game " + (nextGameIndex + 1) + " on " + nextDate);
		logger.trace("Scheduling next playoff game for "
				+ series.getHigherTeam().getName()
				+ " vs "
				+ series.getLowerTeam().getName()
				+ " on "
				+ nextDate);
		addGameToCalendar(playoffCalendar, nextGame, nextDate);
		nextGame.getGameContext().setGameMoment(new Night());
		ScheduleNotifier.notifySchedule(nextDate, nextGame);
	}

	protected void addGameToCalendar(TreeMap<LocalDate, GameDay> playoffCalendar, Game game, LocalDate gameDate) {
		if (playoffCalendar == null) {
			logger.warn("Skipping add game to playoff calendar because playoff calendar is null");
			return;
		}
		if (game == null) {
			logger.warn("Skipping add game to playoff calendar because game is null");
			return;
		}
		if (gameDate == null) {
			logger.warn("Skipping add game to playoff calendar because game date is null");
			return;
		}
		if (!playoffCalendar.containsKey(gameDate)) {
			logger.trace("Creating playoff game day for " + gameDate);
			GameDay gameDay = new GameDay(gameDate);
			gameDay.addGame(game);
			playoffCalendar.put(gameDate, gameDay);
		} else {
			logger.trace("Adding playoff game to existing game day " + gameDate);
			playoffCalendar.get(gameDate).addGame(game);
		}
	}

}

package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import data.calendar.GameDay;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import log.LoggerUtility;
import process.builder.calendar.PlayoffCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.league.TeamPopularityUpdater;

public abstract class PlayoffManager {
	private static final Logger logger = LoggerUtility.getLogger(PlayoffManager.class, "text");

	private League league;
	private PlayoffCalendarBuilder currentRoundCalendarBuilder;
	private PlayoffBuilder playoffBuilder;
	private FinanceManager financeManager;
	private TeamPopularityUpdater teamPopularityUpdater;

	public PlayoffManager(League league, PlayoffCalendarBuilder currentRoundCalendarBuilder,
			PlayoffBuilder playoffBuilder, FinanceManager financeManager, TeamPopularityUpdater teamPopularityUpdater) {
		this.league = league;
		this.currentRoundCalendarBuilder = currentRoundCalendarBuilder;
		this.playoffBuilder = playoffBuilder;
		this.financeManager = financeManager;
		this.teamPopularityUpdater = teamPopularityUpdater;
		logger.debug("Playoff manager initialized");
	}

	public void handlePlayedGame(Game game, LocalDate gameDate) {
		if (game == null || gameDate == null) {
			logger.warn("Skipping playoff played game handling because game or date is null");
			return;
		}
		logger.debug("Handling played playoff game on " + gameDate);
		PlayoffSeries series = findSeriesByGame(game);
		if (series == null || series.isFinished()) {
			logger.debug("Skipping playoff game handling because series is null or already finished");
			return;
		}

		updateSeries(series, game);

		if (series.isFinished()) {
			Team winner = getSeriesWinner(series);
			if (winner != null) {
				logger.debug("Playoff series finished with winner " + winner.getName());
				financeManager.applyPlayoffRoundBonus(
						winner,
						gameDate.getMonthValue(),
						league.getPlayoff().getCurrentRound());
				teamPopularityUpdater.applyPlayoffRoundBonus(
						winner,
						league.getPlayoff().getCurrentRound());
			}

			if (isManagedRoundFinished()) {
				logger.debug("Managed playoff round finished at " + gameDate);
				advanceToNextRound(gameDate);
			}
			return;
		}

		TreeMap<LocalDate, GameDay> playoffCalendar = league.getPlayoff().getNbaCalendar().getCalendar();
		logger.trace("Scheduling next playoff game if necessary after " + gameDate);
		currentRoundCalendarBuilder.scheduleNextGameIfNecessary(playoffCalendar, series, gameDate);
	}

	private PlayoffSeries findSeriesByGame(Game game) {
		logger.trace("Searching playoff series for game");
		for (PlayoffSeries series : getManagedSeries()) {
			if (containsGame(series, game)) {
				logger.trace("Found playoff series for game");
				return series;
			}
		}
		logger.trace("No playoff series found for game");
		return null;
	}

	private void updateSeries(PlayoffSeries series, Game game) {
		Team winner = getWinner(game);

		if (winner == null) {
			logger.warn("Skipping playoff series update because game winner is null");
			return;
		}

		if (winner.equals(series.getHigherTeam())) {
			series.setHigherTeamWins(series.getHigherTeamWins() + 1);
			logger.debug("Higher seed playoff win registered for " + winner.getName());
		} else if (winner.equals(series.getLowerTeam())) {
			series.setLowerTeamWins(series.getLowerTeamWins() + 1);
			logger.debug("Lower seed playoff win registered for " + winner.getName());
		} else {
			logger.warn("Skipping playoff series update because winner is not in the series");
			return;
		}

		series.setNumberPlayedGames(series.getNumberPlayedGames() + 1);
		logger.trace("Playoff series score is "
				+ series.getHigherTeamWins()
				+ "-"
				+ series.getLowerTeamWins()
				+ " after "
				+ series.getNumberPlayedGames()
				+ " games");
		if (series.getHigherTeamWins() >= 4 || series.getLowerTeamWins() >= 4) {
			series.setFinished(true);
			logger.debug("Playoff series marked as finished");
		}
	}

	private boolean isManagedRoundFinished() {
		ArrayList<PlayoffSeries> managedSeries = getManagedSeries();
		if (managedSeries.isEmpty()) {
			logger.trace("Managed playoff round is not finished because there are no series");
			return false;
		}

		for (PlayoffSeries series : managedSeries) {
			if (!series.isFinished()) {
				logger.trace("Managed playoff round is not finished yet");
				return false;
			}
		}
		logger.debug("All managed playoff series are finished");
		return true;
	}

	public Team getSeriesWinner(PlayoffSeries series) {
		if (series == null) {
			logger.warn("Unable to get playoff series winner because series is null");
			return null;
		}
		if (!series.isFinished()) {
			logger.trace("Playoff series winner unavailable because series is not finished");
			return null;
		}
		if (series.getHigherTeamWins() > series.getLowerTeamWins()) {
			logger.trace("Playoff series winner is higher seed " + series.getHigherTeam().getName());
			return series.getHigherTeam();
		}
		logger.trace("Playoff series winner is lower seed " + series.getLowerTeam().getName());
		return series.getLowerTeam();
	}

	private Team getWinner(Game game) {
		if (game.getHomeFinalScore() == game.getAwayFinalScore()) {
			logger.warn("Unable to determine playoff game winner because scores are tied");
			return null;
		}
		if (game.getHomeFinalScore() > game.getAwayFinalScore()) {
			logger.trace("Playoff game winner is home team " + game.getGameContext().getHomeTeam().getName());
			return game.getGameContext().getHomeTeam();
		}
		logger.trace("Playoff game winner is away team " + game.getGameContext().getAwayTeam().getName());
		return game.getGameContext().getAwayTeam();
	}

	private boolean containsGame(PlayoffSeries series, Game game) {
		for (Game expectedGame : series.getExpectedGames()) {
			if (expectedGame == game) {
				return true;
			}
		}
		return false;
	}

	public League getLeague() {
		return league;
	}

	public PlayoffBuilder getPlayoffBuilder() {
		return playoffBuilder;
	}

	public abstract ArrayList<PlayoffSeries> getManagedSeries();

	public abstract void advanceToNextRound(LocalDate roundEndDate);

	public PlayoffCalendarBuilder getCurrentRoundCalendarBuilder() {
		return currentRoundCalendarBuilder;
	}
}

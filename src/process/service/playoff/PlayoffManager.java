package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.calendar.NBACalendar;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import process.builder.calendar.PlayoffCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.league.TeamPopularityUpdater;

public abstract class PlayoffManager {
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
	}

	public void handlePlayedGame(Game game, LocalDate gameDate) {
	  PlayoffSeries series = findSeriesByGame(game);
	  if (series == null || series.isFinished()) {
		 return;
	  }

	  updateSeries(series, game);

	  if (series.isFinished()) {
		 Team winner = getSeriesWinner(series);
		 if (winner != null) {
			financeManager.applyPlayoffRoundBonus(
				  winner,
				  gameDate.getMonthValue(),
				  league.getPlayoff().getCurrentRound());
			teamPopularityUpdater.applyPlayoffRoundBonus(
				  winner,
				  league.getPlayoff().getCurrentRound());
		 }

		 if (isManagedRoundFinished()) {
			advanceToNextRound(gameDate);
		 }
		 return;
	  }

	  TreeMap<LocalDate, GameDay> playoffCalendar = league.getPlayoff().getNbaCalendar().getCalendar();
	  currentRoundCalendarBuilder.scheduleNextGameIfNecessary(playoffCalendar, series, gameDate);
	}

	private PlayoffSeries findSeriesByGame(Game game) {
	  for (PlayoffSeries series : getManagedSeries()) {
		 if (containsGame(series, game)) {
			return series;
		 }
	  }
	  return null;
	}

	private void updateSeries(PlayoffSeries series, Game game) {
	  Team winner = getWinner(game);

	  if (winner == null) {
		 return;
	  }

	  if (winner.equals(series.getHigherTeam())) {
		 series.setHigherTeamWins(series.getHigherTeamWins() + 1);
	  } else if (winner.equals(series.getLowerTeam())) {
		 series.setLowerTeamWins(series.getLowerTeamWins() + 1);
	  } else {
		 return;
	  }

	  series.setNumberPlayedGames(series.getNumberPlayedGames() + 1);
	  if (series.getHigherTeamWins() >= 4 || series.getLowerTeamWins() >= 4) {
		 series.setFinished(true);
	  }
	}

	private boolean isManagedRoundFinished() {
	  ArrayList<PlayoffSeries> managedSeries = getManagedSeries();
	  if (managedSeries.isEmpty()) {
		 return false;
	  }

	  for (PlayoffSeries series : managedSeries) {
		 if (!series.isFinished()) {
			return false;
		 }
	  }
	  return true;
	}

	public Team getSeriesWinner(PlayoffSeries series) {
	  if (!series.isFinished()) {
		 return null;
	  }
	  if (series.getHigherTeamWins() > series.getLowerTeamWins()) {
		 return series.getHigherTeam();
	  }
	  return series.getLowerTeam();
	}

	private Team getWinner(Game game) {
	  if (game.getHomeFinalScore() == game.getAwayFinalScore()) {
		 game.setHomeFinalScore(game.getHomeFinalScore() + 1);
		 game.setWinner(game.getGameContext().getHomeTeam());
		 game.setLoser(game.getGameContext().getAwayTeam());
		 return game.getGameContext().getHomeTeam();
	  }
	  if (game.getHomeFinalScore() > game.getAwayFinalScore()) {
		 Team winner = game.getGameContext().getHomeTeam();
		 game.setWinner(winner);
		 game.setLoser(game.getGameContext().getAwayTeam());
		 return winner;
	  }
	  Team winner = game.getGameContext().getAwayTeam();
	  game.setWinner(winner);
	  game.setLoser(game.getGameContext().getHomeTeam());
	  return winner;
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

	protected void mergePlayoffCalendar(NBACalendar newRoundCalendar) {
	  if (newRoundCalendar == null || newRoundCalendar.getCalendar() == null) {
		 return;
	  }
	  TreeMap<LocalDate, GameDay> playoffCalendar = league.getPlayoff().getNbaCalendar().getCalendar();
	  playoffCalendar.putAll(newRoundCalendar.getCalendar());
	}
}

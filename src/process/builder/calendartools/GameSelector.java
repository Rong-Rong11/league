package process.builder.calendartools;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.TreeMap;

import config.SimulationConfiguration;
import data.league.League;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.team.Team;
import data.team.calendar.Schedule;
import process.repositery.TeamRepositery;
import process.utilitary.CalendarUtilitary;

public class GameSelector {

	private LocalDate date;
	private TeamRepositery teamRepositery = TeamRepositery.getInstance();
	private League league;
	private RegularSeason regularSeason;

	public GameSelector(LocalDate date, League league) {
		super();
		this.date = date;
		this.league = league;
		regularSeason = league.getReagularSeason();
	}

	public ArrayList<Game> selectGamesForDay() {
		ArrayList<Game> selectedGames = new ArrayList<Game>();
		ArrayList<Game> candidates = getCandidates(league, date);

		TreeMap<Double, ArrayList<Game>> scoreMap = new TreeMap<Double, ArrayList<Game>>();
		for (Game game : candidates) {
			double homeLoad = loadGameRatio(game.getGameContext().getHomeTeam(), regularSeason.getEndDate());
			double awayLoad = loadGameRatio(game.getGameContext().getAwayTeam(), regularSeason.getEndDate());
			double loadScore = homeLoad + awayLoad;

			double popularityScore = 0;
			if (CalendarUtilitary.isSpecialEvent(regularSeason, date) || CalendarUtilitary.isImportantDay(date)) {
				popularityScore = CalendarUtilitary.popularityScoreGame(game, date);
			}
			double totalScore = loadScore + popularityScore;
			if (scoreMap.containsKey(totalScore)) {
				scoreMap.get(totalScore).add(game);
			} else {
				ArrayList<Game> list = new ArrayList<Game>();
				list.add(game);
				scoreMap.put(totalScore, list);
			}
		}

		for (Double score : scoreMap.descendingKeySet()) {
			for (Game game : scoreMap.get(score)) {
				if (selectedGames.size() >= SimulationConfiguration.MAX_GAMES_PER_DAY) {
					break;
				}
				if (conflictWithSelected(game, selectedGames)) {
					continue;
				}
				selectedGames.add(game);
			}

		}
		return selectedGames;
	}

	private double loadGameRatio(Team team, LocalDate endOfSeasonDate) {
		int remainingGames = getNumberOfRemainingUnscheduledGames(team.getSchedule());
		long remainingDays = ChronoUnit.DAYS.between(date, endOfSeasonDate);
		if (remainingDays == 0) {
			return remainingGames;
		}
		return (double) remainingGames / remainingDays;
	}

	private boolean canBeScheduled(Game game, LocalDate date) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();
		if (homeTeam.getSchedule().isPlayingOn(date))
			return false;
		if (awayTeam.getSchedule().isPlayingOn(date))
			return false;

		return true;
	}

	private boolean conflictWithSelected(Game game, ArrayList<Game> selectedGames) {
		Team home = game.getGameContext().getHomeTeam();
		Team away = game.getGameContext().getAwayTeam();
		for (Game selected : selectedGames) {
			if (selected.getGameContext().getHomeTeam() == home ||
					selected.getGameContext().getAwayTeam() == home ||
					selected.getGameContext().getHomeTeam() == away ||
					selected.getGameContext().getAwayTeam() == away) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<Game> getCandidates(League league, LocalDate date) {
		ArrayList<Game> candidates = new ArrayList<Game>();

		for (Team team : teamRepositery.getAllTeams()) {
			for (Game game : getUnscheduledGames(team.getSchedule())) {
				if (!candidates.contains(game) && canBeScheduled(game, date)) {
					candidates.add(game);
				}
			}
		}
		return candidates;
	}

	private static ArrayList<Game> getUnscheduledGames(Schedule schedule) {
		ArrayList<Game> unscheduledGames = new ArrayList<Game>();
		for (Game game : schedule.getGames()) {
			if (!schedule.getScheduledGames().containsValue(game)) {
				unscheduledGames.add(game);
			}
		}
		return unscheduledGames;
	}

	private static int getNumberOfRemainingUnscheduledGames(Schedule schedule) {
		int remaining = 0;
		for (Game game : schedule.getGames()) {
			if (!schedule.getScheduledGames().containsValue(game)) {
				remaining++;
			}
		}
		return remaining;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}

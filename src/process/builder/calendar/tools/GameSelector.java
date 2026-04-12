package process.builder.calendar.tools;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import config.CalendarConfiguration;
import data.league.League;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.team.Team;
import data.team.calendar.Schedule;
import process.repositery.TeamRepositery;
import process.utility.CalendarUtility;

public class GameSelector {

	private LocalDate date;
	private TeamRepositery teamRepositery = TeamRepositery.getInstance();
	private League league;
	private RegularSeason regularSeason;

	public GameSelector(LocalDate date, League league) {
		super();
		this.date = date;
		this.league = league;
		regularSeason = league.getRegularSeason();
	}

	public ArrayList<Game> selectGamesForDay() {
		ArrayList<Game> selectedGames = new ArrayList<Game>();
		ArrayList<Game> candidates = getCandidates(league, date);
		Collections.shuffle(candidates);

		TreeMap<Double, ArrayList<Game>> scoreMap = new TreeMap<Double, ArrayList<Game>>();
		for (Game game : candidates) {
			double homeLoad = loadGameRatio(game.getGameContext().getHomeTeam(), regularSeason.getEndDate());
			double awayLoad = loadGameRatio(game.getGameContext().getAwayTeam(), regularSeason.getEndDate());
			double loadScore = homeLoad + awayLoad;
			double scheduleScore = scheduleScore(game, date);

			double popularityScore = 0;
			if (CalendarUtility.isSpecialEvent(regularSeason, date) || CalendarUtility.isImportantDay(date)) {
				popularityScore = CalendarUtility.popularityScoreGame(game, date);
			}
			double randomScore = (Math.random() - 0.5) * 10.0;
			double totalScore = loadScore + popularityScore + scheduleScore + randomScore;

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
				if (selectedGames.size() >= CalendarConfiguration.MAX_GAMES_PER_DAY) {
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

	private double scheduleScore(Game game, LocalDate localDate) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();
		int homeDays = homeTeam.getSchedule().daysSinceLastGame(localDate);
		int awayDays = awayTeam.getSchedule().daysSinceLastGame(localDate);
		double score = 0;

		score += restPenalty(homeDays);
		score += restPenalty(awayDays);

		if (isScheduleTight(homeTeam) || isScheduleTight(awayTeam)) {
			score += 120.0;
		}

		if (playedRecentlyAgainst(homeTeam, awayTeam, localDate, 5)) {
			score -= 80.0;
		}

		return score;
	}

	private boolean playedRecentlyAgainst(Team teamA, Team teamB, LocalDate localDate, int numberOfDays) {
		LocalDate startDate = localDate.minusDays(numberOfDays);

		for (LocalDate gameDate : teamA.getSchedule().getScheduledGames().keySet()) {
			if ((gameDate.isEqual(startDate) || gameDate.isAfter(startDate)) && gameDate.isBefore(localDate)) {
				Game scheduledGame = teamA.getSchedule().getScheduledGames().get(gameDate);

				Team scheduledHome = scheduledGame.getGameContext().getHomeTeam();
				Team scheduledAway = scheduledGame.getGameContext().getAwayTeam();

				boolean sameMatchup = (scheduledHome == teamA && scheduledAway == teamB) ||
						(scheduledHome == teamB && scheduledAway == teamA);

				if (sameMatchup) {
					return true;
				}
			}
		}

		return false;
	}

	private double restPenalty(int daysSinceLastGame) {
		if (daysSinceLastGame <= 0) {
			return -10000.0;
		}
		if (daysSinceLastGame == 1) {
			return -1000.0;
		}
		if (daysSinceLastGame == 2) {
			return -200.0;
		}
		return 0.0;
	}

	private boolean isScheduleTight(Team team) {
		int remainingGames = getNumberOfRemainingUnscheduledGames(team.getSchedule());
		long remainingDays = ChronoUnit.DAYS.between(date, regularSeason.getEndDate());
		return remainingDays > 0 && remainingGames >= remainingDays;
	}

	private boolean canBeScheduled(Game game, LocalDate date) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();

		if (homeTeam.getSchedule().isPlayingOn(date) || awayTeam.getSchedule().isPlayingOn(date)) {
			return false;
		}

		int homeDays = homeTeam.getSchedule().daysSinceLastGame(date);
		int awayDays = awayTeam.getSchedule().daysSinceLastGame(date);
		if (homeDays <= 0 || awayDays <= 0) {
			return false;
		}

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

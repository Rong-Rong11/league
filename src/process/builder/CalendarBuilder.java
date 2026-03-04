package process.builder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.TreeMap;

import config.SimulationConfiguration;
import data.calendar.GameDay;
import data.calendar.SpecialEvent;
import data.league.Conference;
import data.league.Division;
import data.league.League;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.team.Team;
import data.team.calendar.Schedule;
import process.GameGenerator;
<<<<<<< HEAD
import process.GameManager;

public class CalendarBuilder {

	public static void initialization(League league) {
		Conference westernConference = league.getWesternConference();
		for (Division division : westernConference.getDivisions().values()) {
			for (Team team : division.getTeams().values()) {
				team.getSchedule().setNumberOfAwayGames(0);
				team.getSchedule().setNumberOfHomeGames(0);
				team.getSchedule().setNumberOfPlayedGames(0);
				team.getSchedule().clearGames();
				team.getSchedule().clearScheduledGames();
			}
		}
	}

	public static void specialEventsPlacement(RegularSeason regularSeason) {
		regularSeason.addSpecialEvents(new SpecialEvent(SimulationConfiguration.CHRISTMAS_DAY, "christmas"));
		regularSeason.addSpecialEvents(new SpecialEvent(regularSeason.getDebutDate(), "opening night"));
		regularSeason.addSpecialEvents(new SpecialEvent(regularSeason.getEndDate(), "ending night"));
		regularSeason.addSpecialEvents(new SpecialEvent(GameManager.getMLKDay(), "mlk day"));
	}

	public static void generateAllGames(League league) {
=======
import process.repositery.DivisionRepositery;
import process.repositery.PlayerRepositery;
import process.repositery.TeamRepositery;
import process.utilitary.CalendarUtilitary;

public class CalendarBuilder {

	private TeamRepositery teamRepositery = TeamRepositery.getInstance();
	private DivisionRepositery divisionRepositery = DivisionRepositery.getInstance();
	private PlayerRepositery playerRepositery = PlayerRepositery.getInstance();

	public CalendarBuilder() {

	}

	public void initialization() {
		for (Team team : teamRepositery.getAllTeams()) {
			team.getSchedule().setNumberOfAwayGames(0);
			team.getSchedule().setNumberOfHomeGames(0);
			team.getSchedule().setNumberOfPlayedGames(0);
			team.getSchedule().clearGames();
			team.getSchedule().clearScheduledGames();

		}
	}

	public void specialEventsPlacement(RegularSeason regularSeason) {
		regularSeason.addSpecialEvents(new SpecialEvent(SimulationConfiguration.CHRISTMAS_DAY, "christmas"));
		regularSeason.addSpecialEvents(new SpecialEvent(regularSeason.getDebutDate(), "opening night"));
		regularSeason.addSpecialEvents(new SpecialEvent(regularSeason.getEndDate(), "ending night"));
		regularSeason.addSpecialEvents(new SpecialEvent(CalendarUtilitary.getMLKDay(), "mlk day"));
	}

	public void generateAllGames(League league) {
>>>>>>> Fatima2
		Conference westernConference = league.getWesternConference();
		Conference easternConference = league.getEasternConference();
		for (Division division : westernConference.getDivisions().values()) {
			GameGenerator.generateIntraDivision(division);
		}
		for (Division division : easternConference.getDivisions().values()) {
			GameGenerator.generateIntraDivision(division);
		}
		GameGenerator.generateIntraConference(easternConference);
		GameGenerator.generateIntraConference(westernConference);

		GameGenerator.generateInterConference(league);
	}

<<<<<<< HEAD
	public static void generateRegulaSeasonCalendar(League league) {
=======
	public void generateRegulaSeasonCalendar(League league) {
>>>>>>> Fatima2
		RegularSeason regularSeason = league.getReagularSeason();
		TreeMap<LocalDate, GameDay> calendar = new TreeMap<LocalDate, GameDay>();
		LocalDate debutDate = regularSeason.getDebutDate();
		LocalDate endDate = regularSeason.getEndDate();

		for (LocalDate date = debutDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			GameDay gameDay = new GameDay(date);
			ArrayList<Game> games = selectGamesForDay(date, league, regularSeason);
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

<<<<<<< HEAD
	private static ArrayList<Game> selectGamesForDay(LocalDate date, League league, RegularSeason regularSeason) {
=======
	private ArrayList<Game> selectGamesForDay(LocalDate date, League league, RegularSeason regularSeason) {
>>>>>>> Fatima2
		ArrayList<Game> selectedGames = new ArrayList<Game>();
		ArrayList<Game> candidates = getCandidates(league, date);

		TreeMap<Double, ArrayList<Game>> scoreMap = new TreeMap<Double, ArrayList<Game>>();
		for (Game game : candidates) {
			double homeLoad = loadGameRatio(game.getGameContext().getHomeTeam(), date, regularSeason.getEndDate());
			double awayLoad = loadGameRatio(game.getGameContext().getAwayTeam(), date, regularSeason.getEndDate());
			double loadScore = homeLoad + awayLoad;

			double popularityScore = 0;
<<<<<<< HEAD
			if (GameManager.isSpecialEvent(regularSeason, date) || GameManager.isImportantDay(date)) {
				popularityScore = GameManager.popularityScoreGame(game, date);
=======
			if (CalendarUtilitary.isSpecialEvent(regularSeason, date) || CalendarUtilitary.isImportantDay(date)) {
				popularityScore = CalendarUtilitary.popularityScoreGame(game, date);
>>>>>>> Fatima2
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

	private static double loadGameRatio(Team team, LocalDate date, LocalDate endOfSeasonDate) {
		int remainingGames = getNumberOfRemainingUnscheduledGames(team.getSchedule());
		long remainingDays = ChronoUnit.DAYS.between(date, endOfSeasonDate);
		if (remainingDays == 0) {
			return remainingGames;
		}
		return (double) remainingGames / remainingDays;
	}

	private static boolean canBeScheduled(Game game, LocalDate date) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();
		if (homeTeam.getSchedule().isPlayingOn(date))
			return false;
		if (awayTeam.getSchedule().isPlayingOn(date))
			return false;

		return true;
	}

	private static boolean conflictWithSelected(Game game, ArrayList<Game> selectedGames) {
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

<<<<<<< HEAD
	private static ArrayList<Game> getCandidates(League league, LocalDate date) {
		ArrayList<Game> candidates = new ArrayList<Game>();

		for (Team team : GameManager.getAllTeamsOfLeague(league)) {
=======
	private ArrayList<Game> getCandidates(League league, LocalDate date) {
		ArrayList<Game> candidates = new ArrayList<Game>();

		for (Team team : teamRepositery.getAllTeams()) {
>>>>>>> Fatima2
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
}

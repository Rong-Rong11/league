package process;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import config.SimulationConfiguration;
import data.league.Conference;
import data.league.Division;
import data.league.League;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.team.Team;

public class GameManager {

	public static boolean isWeekend(LocalDate date) {
		return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
	}

	public static boolean isImportantDay(LocalDate date) {
		return isWeekend(date) || date.getDayOfWeek() == DayOfWeek.WEDNESDAY;
	}

	public static boolean isSpecialEvent(RegularSeason regularSeason, LocalDate date) {
		return date.isEqual(SimulationConfiguration.CHRISTMAS_DAY) ||
				date.isEqual(regularSeason.getDebutDate()) ||
				date.isEqual(regularSeason.getEndDate()) ||
				date.isEqual(getMLKDay());
	}

	public static boolean playedYesterday(Team team, LocalDate date) {
		return team.getSchedule().isPlayingOn(date.minusDays(1));
	}

	public static LocalDate getMLKDay() {
		LocalDate date = LocalDate.of(SimulationConfiguration.SEASON_YEAR, Month.JANUARY, 1);

		int mondays = 0;
		while (date.getMonth() == Month.JANUARY) {
			if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
				mondays++;
				if (mondays == 3) {
					return date;
				}
			}
			date = date.plusDays(1);
		}
		return null;

	}

	public static double popularityScoreGame(Game game, LocalDate date) {
		double score = 0;
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();

		score += (homeTeam.getPopularity() + awayTeam.getPopularity()) * 5;
		if (game.getGameContext().isRivalry()) {
			score += 40;
		}
		if (homeTeam.hasStarPlayer()) {
			score += 30;
		}
		if (awayTeam.hasStarPlayer()) {
			score += 30;
		}
		switch (game.getGameContext().getTypeGame()) {
			case SimulationConfiguration.GAME_INTRA_DIVISION -> score += 15;
			case SimulationConfiguration.GAME_INTRA_CONFERENCE -> score += 10;
			case SimulationConfiguration.GAME_INTER_CONFERENCE -> score += 5;
		}

		int restHome = homeTeam.getSchedule().daysSinceLastGame(date);
		int restAway = awayTeam.getSchedule().daysSinceLastGame(date);
		if (restHome < 3) {
			score -= 5;
		}
		if (restAway < 3) {
			score -= 5;
		}

		if (playedYesterday(game.getGameContext().getHomeTeam(), date)
				|| playedYesterday(game.getGameContext().getAwayTeam(), date)) {
			score -= 1000;
		}
		return score;

	}

	public static ArrayList<Team> getAllTeamsOfConference(Conference conference) {
		ArrayList<Team> teams = new ArrayList<Team>();
		for (Division division : conference.getDivisions().values()) {
			teams.addAll(division.getTeams().values());
		}
		return teams;
	}

	public static ArrayList<Team> getAllTeamsOfLeague(League league) {
		ArrayList<Team> allTeams = new ArrayList<Team>();
		allTeams.addAll(getAllTeamsOfConference(league.getEasternConference()));
		allTeams.addAll(getAllTeamsOfConference(league.getWesternConference()));
		return allTeams;
	}

	public static boolean isRivalry(GameContext gameContext) {
		if (gameContext.getHomeTeam().getRival() == null || gameContext.getAwayTeam().getRival() == null) {
			return false;
		}
		String homeRivalName = gameContext.getHomeTeam().getRival();
		String awayRivalName = gameContext.getAwayTeam().getRival();
		return homeRivalName.equals(awayRivalName);

	}

	public static ArrayList<Division> getAllDivisionsOfLeague(League league) {
		ArrayList<Division> allDivisions = new ArrayList<Division>();
		for (Division division : league.getEasternConference().getDivisions().values()) {
			allDivisions.add(division);
		}
		for (Division division : league.getWesternConference().getDivisions().values()) {
			allDivisions.add(division);
		}
		return allDivisions;
	}

}

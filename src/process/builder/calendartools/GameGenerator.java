package process.builder.calendartools;

import data.league.Conference;
import data.league.Division;
import data.league.League;
import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.team.Team;
import java.util.ArrayList;

import config.GameConfiguration;

public class GameGenerator {
	public static void generateAllGamesRegularSeason(League league) {
		Conference westernConference = league.getWesternConference();
		Conference easternConference = league.getEasternConference();
		for (Division division : westernConference.getDivisions().values()) {
			generateIntraDivision(division);
		}
		for (Division division : easternConference.getDivisions().values()) {
			generateIntraDivision(division);
		}
		generateIntraConference(easternConference);
		generateIntraConference(westernConference);
		generateInterConference(league);
	}
	
	private static void generateIntraDivision(Division division) {
		ArrayList<Team> teams = new ArrayList<Team>(division.getTeams().values());
		for (int i = 0; i < teams.size(); i++) {
			for (int j = i + 1; j < teams.size(); j++) {
				Team team = teams.get(i);
				Team other = teams.get(j);
				for (int k = 0; k < 4; k++) {
					boolean home = (k % 2 == 0);
					Game game;
					GameContext gameContext;
					if (home) {
						gameContext = new GameContext(team, other, GameConfiguration.GAME_INTRA_DIVISION);
						game = new Game(gameContext);
					} else {
						gameContext = new GameContext(other, team, GameConfiguration.GAME_INTRA_DIVISION);
						game = new Game(gameContext);
					}
					addGameToTeam(game, team);
					addGameToTeam(game, other);
				}
			}
		}
	}
	
	private static void generateIntraConference(Conference conference) {
		ArrayList<Division> divisions = new ArrayList<Division>(conference.getDivisions().values());
		for (int division1 = 0; division1 < divisions.size(); division1++) {
			for (int division2 = division1 + 1; division2 < divisions.size(); division2++) {
				Division divisionA = divisions.get(division1);
				Division divisionB = divisions.get(division2);

				for (Team team : divisionA.getTeams().values()) {
					for (Team other : divisionB.getTeams().values()) {
						int games = 3;
						for (int k = 0; k < games; k++) {
							boolean home = (k % 2 == 0);
							Game game;
							GameContext gameContext;
							if (home) {
								gameContext = new GameContext(team, other, GameConfiguration.GAME_INTRA_CONFERENCE);
								game = new Game(gameContext);
							} else {
								gameContext = new GameContext(other, team, GameConfiguration.GAME_INTRA_CONFERENCE);
								game = new Game(gameContext);
							}
							addGameToTeam(game, team);
							addGameToTeam(game, other);
						}
					}
				}
			}
		}
	}

	public static void generateInterConference(League league) {
		Conference westernConference = league.getWesternConference();
		Conference easternConference = league.getEasternConference();

		for (Team team : getAllTeamsOfConference(westernConference)) {
			for (Team other : getAllTeamsOfConference(easternConference)) {
				for (int i = 0; i < 2; i++) {
					boolean home = (i % 2 == 0);
					Game game;
					GameContext gameContext;
					if (home) {
						gameContext = new GameContext(team, other, GameConfiguration.GAME_INTER_CONFERENCE);
						game = new Game(gameContext);
					} else {
						gameContext = new GameContext(other, team, GameConfiguration.GAME_INTER_CONFERENCE);
						game = new Game(gameContext);
					}
					addGameToTeam(game, team);
					addGameToTeam(game, other);
				}
			}
		}
	}

	private static void addGameToTeam(Game game, Team team) {
		if (game.getGameContext().getHomeTeam().getName().equals(team.getName())) {
			team.getSchedule().incrementNumberOfHomeGames();
		} else {
			team.getSchedule().incrementNumberOfAwayGames();
		}
		team.addGame(game);
	}

	private static ArrayList<Team> getAllTeamsOfConference(Conference conference) {
		ArrayList<Team> teams = new ArrayList<Team>();
		for (Division division : conference.getDivisions().values()) {
			teams.addAll(division.getTeams().values());
		}
		return teams;
	}
}

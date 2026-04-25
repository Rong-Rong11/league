package process.builder.calendar.generator;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import config.GameConfiguration;
import data.league.Conference;
import data.league.Division;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import log.LoggerUtility;

public class RegularSeasonGameGenerator {
	private static final Logger logger = LoggerUtility.getLogger(RegularSeasonGameGenerator.class, "text");

	public static void generateAllGamesRegularSeason(League league) {
		logger.info("Generating all regular season games");
		Conference westernConference = league.getWesternConference();
		Conference easternConference = league.getEasternConference();
		logger.debug("Loaded western and eastern conferences for regular season generation");
		for (Division division : westernConference.getDivisions().values()) {
			generateIntraDivision(division);
		}
		for (Division division : easternConference.getDivisions().values()) {
			generateIntraDivision(division);
		}
		logger.info("Generating intra-conference games");
		generateIntraConference(easternConference);
		generateIntraConference(westernConference);
		generateInterConference(league);
		logger.info("Regular season games generated successfully");
	}

	private static void generateIntraDivision(Division division) {
		ArrayList<Team> teams = new ArrayList<Team>(division.getTeams().values());
		logger.debug("Generating intra-division games for division " + division.getName());
		logger.trace("Division " + division.getName() + " contains " + teams.size() + " teams");
		for (int i = 0; i < teams.size(); i++) {
			for (int j = i + 1; j < teams.size(); j++) {
				Team team = teams.get(i);
				Team other = teams.get(j);
				for (int k = 0; k < 4; k++) {
					Game game = createBalancedHomeAwayGame(
							team,
							other,
							GameConfiguration.GAME_INTRA_DIVISION,
							k);
					logger.trace("Creating intra-division game "
							+ game.getGameContext().getHomeTeam().getName()
							+ " vs "
							+ game.getGameContext().getAwayTeam().getName());
					GameScheduleHelper.addGameToTeam(game, team);
					GameScheduleHelper.addGameToTeam(game, other);
				}
			}
		}
		logger.debug("Completed intra-division generation for division " + division.getName());
	}

	private static void generateIntraConference(Conference conference) {
		ArrayList<Division> divisions = new ArrayList<Division>(conference.getDivisions().values());
		logger.debug("Generating intra-conference games for " + conference.getName());
		logger.trace("Conference " + conference.getName() + " contains " + divisions.size() + " divisions");
		for (int division1 = 0; division1 < divisions.size(); division1++) {
			for (int division2 = division1 + 1; division2 < divisions.size(); division2++) {
				Division divisionA = divisions.get(division1);
				Division divisionB = divisions.get(division2);
				logger.debug("Generating games between divisions "
						+ divisionA.getName()
						+ " and "
						+ divisionB.getName());

				for (Team team : divisionA.getTeams().values()) {
					for (Team other : divisionB.getTeams().values()) {
						int games = 4;
						for (int k = 0; k < games; k++) {
							Game game = createBalancedHomeAwayGame(
									team,
									other,
									GameConfiguration.GAME_INTRA_CONFERENCE,
									k);
							logger.trace("Creating intra-conference game "
									+ game.getGameContext().getHomeTeam().getName()
									+ " vs "
									+ game.getGameContext().getAwayTeam().getName());
							GameScheduleHelper.addGameToTeam(game, team);
							GameScheduleHelper.addGameToTeam(game, other);
						}
					}
				}
			}
		}
		logger.debug("Completed intra-conference generation for " + conference.getName());
	}

	private static void generateInterConference(League league) {
		Conference westernConference = league.getWesternConference();
		Conference easternConference = league.getEasternConference();
		ArrayList<Team> westernTeams = getAllTeamsOfConference(westernConference);
		ArrayList<Team> easternTeams = getAllTeamsOfConference(easternConference);
		logger.info("Generating inter-conference games");
		logger.debug("Generating inter-conference games for "
				+ westernTeams.size()
				+ " western teams and "
				+ easternTeams.size()
				+ " eastern teams");

		for (Team team : westernTeams) {
			for (Team other : easternTeams) {
				for (int i = 0; i < 2; i++) {
					Game game = createBalancedHomeAwayGame(
							team,
							other,
							GameConfiguration.GAME_INTER_CONFERENCE,
							i);
					logger.trace("Creating inter-conference game "
							+ game.getGameContext().getHomeTeam().getName()
							+ " vs "
							+ game.getGameContext().getAwayTeam().getName());
					GameScheduleHelper.addGameToTeam(game, team);
					GameScheduleHelper.addGameToTeam(game, other);
				}
			}
		}
		logger.debug("Completed inter-conference generation");
	}

	private static Game createBalancedHomeAwayGame(Team team, Team other, int gameType, int index) {
		if (index % 2 == 0) {
			return GameScheduleHelper.createGame(team, other, gameType);
		}
		return GameScheduleHelper.createGame(other, team, gameType);
	}

	private static ArrayList<Team> getAllTeamsOfConference(Conference conference) {
		ArrayList<Team> teams = new ArrayList<Team>();
		for (Division division : conference.getDivisions().values()) {
			teams.addAll(division.getTeams().values());
		}
		logger.trace("Collected "
				+ teams.size()
				+ " teams for conference "
				+ conference.getName());
		return teams;
	}
}

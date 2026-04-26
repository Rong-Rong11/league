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
		if (league == null) {
			logger.warn("Skipping regular season games generation because league is null");
			return;
		}

		Conference westernConference = league.getWesternConference();
		Conference easternConference = league.getEasternConference();

		if (westernConference == null || easternConference == null) {
			logger.warn("Skipping regular season games generation because western or eastern conference is null");
			return;
		}

		logger.debug("Generating regular season games");

		for (Division division : westernConference.getDivisions().values()) {
			generateIntraDivision(division);
		}

		for (Division division : easternConference.getDivisions().values()) {
			generateIntraDivision(division);
		}

		generateIntraConference(easternConference);
		generateIntraConference(westernConference);
		generateInterConference(league);

		logger.debug("Regular season games generated");
	}

	private static void generateIntraDivision(Division division) {
		if (division == null) {
			logger.warn("Skipping intra-division games generation because division is null");
			return;
		}

		ArrayList<Team> teams = new ArrayList<Team>(division.getTeams().values());

		for (int i = 0; i < teams.size(); i++) {
			for (int j = i + 1; j < teams.size(); j++) {
				Team team = teams.get(i);
				Team other = teams.get(j);

				if (team == null || other == null) {
					logger.warn("Skipping intra-division matchup because one team is null");
					continue;
				}

				for (int k = 0; k < 4; k++) {
					Game game = createBalancedHomeAwayGame(
							team,
							other,
							GameConfiguration.GAME_INTRA_DIVISION,
							k);

					GameScheduleHelper.addGameToTeam(game, team);
					GameScheduleHelper.addGameToTeam(game, other);
				}
			}
		}
	}

	private static void generateIntraConference(Conference conference) {
		if (conference == null) {
			logger.warn("Skipping intra-conference games generation because conference is null");
			return;
		}

		ArrayList<Division> divisions = new ArrayList<Division>(conference.getDivisions().values());

		for (int division1 = 0; division1 < divisions.size(); division1++) {
			for (int division2 = division1 + 1; division2 < divisions.size(); division2++) {
				Division divisionA = divisions.get(division1);
				Division divisionB = divisions.get(division2);

				if (divisionA == null || divisionB == null) {
					logger.warn("Skipping intra-conference division matchup because one division is null");
					continue;
				}

				for (Team team : divisionA.getTeams().values()) {
					for (Team other : divisionB.getTeams().values()) {
						if (team == null || other == null) {
							logger.warn("Skipping intra-conference matchup because one team is null");
							continue;
						}

						for (int k = 0; k < 4; k++) {
							Game game = createBalancedHomeAwayGame(
									team,
									other,
									GameConfiguration.GAME_INTRA_CONFERENCE,
									k);

							GameScheduleHelper.addGameToTeam(game, team);
							GameScheduleHelper.addGameToTeam(game, other);
						}
					}
				}
			}
		}
	}

	private static void generateInterConference(League league) {
		if (league == null) {
			logger.warn("Skipping inter-conference games generation because league is null");
			return;
		}

		Conference westernConference = league.getWesternConference();
		Conference easternConference = league.getEasternConference();

		if (westernConference == null || easternConference == null) {
			logger.warn("Skipping inter-conference games generation because western or eastern conference is null");
			return;
		}

		ArrayList<Team> westernTeams = getAllTeamsOfConference(westernConference);
		ArrayList<Team> easternTeams = getAllTeamsOfConference(easternConference);

		for (Team team : westernTeams) {
			for (Team other : easternTeams) {
				if (team == null || other == null) {
					logger.warn("Skipping inter-conference matchup because one team is null");
					continue;
				}

				for (int i = 0; i < 2; i++) {
					Game game = createBalancedHomeAwayGame(
							team,
							other,
							GameConfiguration.GAME_INTER_CONFERENCE,
							i);

					GameScheduleHelper.addGameToTeam(game, team);
					GameScheduleHelper.addGameToTeam(game, other);
				}
			}
		}
	}

	private static Game createBalancedHomeAwayGame(Team team, Team other, int gameType, int index) {
		if (index % 2 == 0) {
			return GameScheduleHelper.createGame(team, other, gameType);
		}
		return GameScheduleHelper.createGame(other, team, gameType);
	}

	private static ArrayList<Team> getAllTeamsOfConference(Conference conference) {
		ArrayList<Team> teams = new ArrayList<Team>();

		if (conference == null) {
			logger.warn("Returning empty teams list because conference is null");
			return teams;
		}

		for (Division division : conference.getDivisions().values()) {
			if (division == null) {
				continue;
			}
			teams.addAll(division.getTeams().values());
		}

		return teams;
	}
}

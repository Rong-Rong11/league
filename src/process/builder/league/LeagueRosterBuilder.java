package process.builder.league;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import data.league.Division;
import data.league.League;
import data.player.Player;
import data.team.Team;
import log.LoggerUtility;
import process.factory.PlayerFactory;
import process.factory.TeamFactory;
import process.repository.CurrentSeasonAssetRepository;
import process.repository.DivisionRepository;
import process.repository.PlayerRepository;
import process.repository.PreSeasonAssetRepository;
import process.repository.TeamRepository;
import process.utility.TeamUtility;

public class LeagueRosterBuilder {
	private static final Logger logger = LoggerUtility.getLogger(LeagueRosterBuilder.class, "text");

	private PlayerRepository playerRepositery = PlayerRepository.getInstance();
	private TeamRepository teamRepositery = TeamRepository.getInstance();
	private DivisionRepository divisionRepositery = DivisionRepository.getInstance();
	private PreSeasonAssetRepository preSeasonAssetRepositery = PreSeasonAssetRepository.getInstance();
	private CurrentSeasonAssetRepository currentSeasonAssetRepositery = CurrentSeasonAssetRepository.getInstance();

	public void buildRoster(League league, BufferedReader bufferedReader) throws IOException {
		if (league == null) {
			logger.warn("Skipping league roster build because league is null");
			return;
		}
		if (bufferedReader == null) {
			logger.warn("Skipping league roster build because buffered reader is null");
			return;
		}

		logger.info("Building league roster from CSV");
		String line;
		bufferedReader.readLine();
		int playerCount = 0;

		while ((line = bufferedReader.readLine()) != null) {
			if (line.startsWith("player_id")) {
				logger.trace("Skipping repeated CSV header line");
				continue;
			}
			String[] data = line.split(",", -1);
			if (data.length <= 5) {
				logger.warn("Skipping malformed roster line: " + line);
				continue;
			}
			String teamName = data[2];
			String conferenceName = data[4];
			String divisionName = data[5];

			Player player = PlayerFactory.createPlayer(line);

			if (divisionRepositery.getDivision(divisionName) == null) {
				logger.debug("Creating division " + divisionName + " for conference " + conferenceName);
				Division division = new Division(divisionName);
				if (conferenceName.equals("Ouest")) {
					league.addDivisionWesternConference(division);
				} else {
					league.addDivisionEasternConference(division);
				}
				divisionRepositery.register(divisionName, division);
			}

			if (teamRepositery.getTeam(teamName) == null) {
				logger.debug("Creating team " + teamName + " in division " + divisionName);
				Team team = TeamFactory.createTeam(line);
				if (conferenceName.equals("Ouest")) {
					league.addTeamWesternConference(team, divisionName);
				} else {
					league.addTeamEasternConference(team, divisionName);
				}
				teamRepositery.register(teamName, team);

			}

			logger.trace("Registering player " + player.getName() + " for team " + teamName);
			teamRepositery.getTeam(teamName).addFirstPlayer(player);
			playerRepositery.register(player.getName(), player);
			preSeasonAssetRepositery.register(player, player.getPreSeasonAssets());
			currentSeasonAssetRepositery.register(player, player.getCurrentSeasonAssets());
			playerCount++;
		}
		logger.debug("Roster import completed with " + playerCount + " registered players");
		logger.debug("Assigning star players to all teams");
		setStarPlayerTeams();
		logger.info("League roster built successfully");
	}

	private void setStarPlayerTeams() {
		for (Team team : teamRepositery.getAllTeams()) {
			if (team == null) {
				logger.warn("Skipping star player assignment because team is null");
				continue;
			}
			logger.trace("Assigning star player for team " + team.getName());
			TeamUtility.setStarPlayer(team);
		}
	}
}

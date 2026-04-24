package process.builder.league;

import java.io.BufferedReader;
import java.io.IOException;

import data.league.Division;
import data.league.League;
import data.player.Player;
import data.team.Team;
import process.factory.PlayerFactory;
import process.factory.TeamFactory;
import process.repository.CurrentSeasonAssetRepository;
import process.repository.DivisionRepository;
import process.repository.PlayerRepository;
import process.repository.PreSeasonAssetRepository;
import process.repository.TeamRepository;
import process.utility.TeamUtility;

public class LeagueRosterBuilder {

	private PlayerRepository playerRepositery = PlayerRepository.getInstance();
	private TeamRepository teamRepositery = TeamRepository.getInstance();
	private DivisionRepository divisionRepositery = DivisionRepository.getInstance();
	private PreSeasonAssetRepository preSeasonAssetRepositery = PreSeasonAssetRepository.getInstance();
	private CurrentSeasonAssetRepository currentSeasonAssetRepositery = CurrentSeasonAssetRepository.getInstance();

	public void buildRoster(League league, BufferedReader bufferedReader) throws IOException {
		String line;
		bufferedReader.readLine();

		while ((line = bufferedReader.readLine()) != null) {
			if (line.startsWith("player_id")) {
				continue;
			}
			String[] data = line.split(",", -1);
			String teamName = data[2];
			String conferenceName = data[4];
			String divisionName = data[5];

			Player player = PlayerFactory.createPlayer(line);

			if (divisionRepositery.getDivision(divisionName) == null) {
				Division division = new Division(divisionName);
				if (conferenceName.equals("Ouest")) {
					league.addDivisionWesternConference(division);
				} else {
					league.addDivisionEasternConference(division);
				}
				divisionRepositery.register(divisionName, division);
			}

			if (teamRepositery.getTeam(teamName) == null) {
				Team team = TeamFactory.createTeam(line);
				if (conferenceName.equals("Ouest")) {
					league.addTeamWesternConference(team, divisionName);
				} else {
					league.addTeamEasternConference(team, divisionName);
				}
				teamRepositery.register(teamName, team);

			}

			teamRepositery.getTeam(teamName).addFirstPlayer(player);
			playerRepositery.register(player.getName(), player);
			preSeasonAssetRepositery.register(player, player.getPreSeasonAssets());
			currentSeasonAssetRepositery.register(player, player.getCurrentSeasonAssets());
		}
		setStarPlayerTeams();
	}

	private void setStarPlayerTeams() {
		for (Team team : teamRepositery.getAllTeams()) {
			TeamUtility.setStarPlayer(team);
		}
	}
}

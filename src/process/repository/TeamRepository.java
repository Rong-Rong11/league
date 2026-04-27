package process.repository;

import java.util.ArrayList;
import java.util.HashMap;

import data.team.Team;

public class TeamRepository {
	private HashMap<String, Team> teams = new HashMap<>();
	private static TeamRepository instance = new TeamRepository();

	private TeamRepository() {
	}

	public static TeamRepository getInstance() {
		return instance;
	}

	public void register(String teamName, Team team) {
		this.teams.put(teamName, team);
	}

	public Team getTeam(String teamName) {
		if (this.teams.containsKey(teamName)) {
			return this.teams.get(teamName);
		}
		return null;
	}

	public ArrayList<Team> getAllTeams() {
		ArrayList<Team> arrayList = new ArrayList<Team>(this.teams.values());
		return arrayList;
	}

	public void clear() {
		this.teams.clear();
	}
}

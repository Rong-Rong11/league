package process.repositery;

import java.util.ArrayList;
import java.util.HashMap;

import data.team.Team;

public class TeamRepositery {

	private HashMap<String, Team> teams = new HashMap<String, Team>();
	private static TeamRepositery instance = new TeamRepositery();

	private TeamRepositery() {

	}

	public static TeamRepositery getInstance() {
		return instance;
	}

	public void register(String name, Team team) {
		teams.put(name, team);
	}

	public Team getTeam(String name) {
		if (teams.containsKey(name)) {
			return teams.get(name);
		}
		return null;
	}

	public ArrayList<Team> getAllTeams() {
		ArrayList<Team> allTeams = new ArrayList<Team>(teams.values());
		return allTeams;
	}

	public void clear() {
		teams.clear();
	}

}

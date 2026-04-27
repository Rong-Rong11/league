package data.league;

import data.team.Team;
import java.util.HashMap;


public class Division {
	private String name;
	private HashMap<String, Team> teams;

	public Division(String name) {
		this.name = name;
		this.teams = new HashMap<String, Team>();
	}

	public HashMap<String, Team> getTeams() {
		return this.teams;
	}

	public void setTeams(HashMap<String, Team> hashMap) {
		this.teams = hashMap;
	}

	public void addTeam(Team team) {
		this.teams.put(team.getName(), team);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		String text = this.name;
		for (Team team : this.teams.values()) {
			text = text + "\n" + team.toString();
		}
		return text;
	}
}

package data.league;

import java.util.HashMap;

import data.team.Team;

public class Division {
	private String name ; 
	private HashMap<String, Team> teams ;
	
	public Division(String name) {
		this.name = name ; 
		teams = new HashMap<String, Team>() ; 
	}
	
	public HashMap<String, Team> getTeams() {
		return teams;
	}

	public void setTeams(HashMap<String, Team> teams) {
		this.teams = teams;
	} 
	
	public void addTeam(Team team) {
		teams.put(team.getName(), team) ; 
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString () {
		String s = name ; 
		for(Team team : teams.values()) {
			s+= "\n" + team.toString() ; 
		}
		return s ; 
	}
	
	
	
}

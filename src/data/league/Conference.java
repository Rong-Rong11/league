package data.league;

import java.util.HashMap;

import data.team.Team;

public class Conference {
	private String name;
	private HashMap<String, Division> divisions;

	public Conference(String name) {
		this.name = name;
		divisions = new HashMap<String, Division>();
	}

	public HashMap<String, Division> getDivisions() {
		return divisions;
	}

	public void setDivisions(HashMap<String, Division> divisions) {
		this.divisions = divisions;
	}

	public void addTeam(Team team, String divisionName) {
		Division division = divisions.get(divisionName);
		division.addTeam(team);
	}

	public void addDivision(Division division) {
		divisions.put(division.getName(), division);
	}

	public String getName() {
		return name;
	}

	public String toString() {
		String s = name;
		for (Division division : divisions.values()) {
			s += "\n" + division.toString();
		}
		return s;
	}

}

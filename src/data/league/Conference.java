package data.league;

import data.league.Division;
import data.team.Team;
import java.util.HashMap;

public class Conference {
	private String name;
	private HashMap<String, Division> divisions;

	public Conference(String string) {
		this.name = string;
		this.divisions = new HashMap<String, Division>();
	}

	public HashMap<String, Division> getDivisions() {
		return this.divisions;
	}

	public void setDivisions(HashMap<String, Division> hashMap) {
		this.divisions = hashMap;
	}

	public void addTeam(Team team, String string) {
		Division division = this.divisions.get(string);
		division.addTeam(team);
	}

	public void addDivision(Division division) {
		this.divisions.put(division.getName(), division);
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		String string = this.name;
		for (Division division : this.divisions.values()) {
			string = string + "\n" + division.toString();
		}
		return string;
	}
}

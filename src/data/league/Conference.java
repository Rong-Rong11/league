package data.league;


import data.team.Team;
import java.util.HashMap;

public class Conference {
	private String name;
	private HashMap<String, Division> divisions;

	public Conference(String name) {
		this.name = name;
		this.divisions = new HashMap<String, Division>();
	}

	public HashMap<String, Division> getDivisions() {
		return this.divisions;
	}

	public void setDivisions(HashMap<String, Division> hashMap) {
		this.divisions = hashMap;
	}

	public void addTeam(Team team, String divisionName) {
		Division division = this.divisions.get(divisionName);
		division.addTeam(team);
	}

	public void addDivision(Division division) {
		this.divisions.put(division.getName(), division);
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		String text = this.name;
		for (Division division : this.divisions.values()) {
			text = text + "\n" + division.toString();
		}
		return text;
	}
}

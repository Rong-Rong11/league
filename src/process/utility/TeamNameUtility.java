package process.utility;

import data.team.Team;

public final class TeamNameUtility {

	private TeamNameUtility() {
	}

	public static String getAbbreviation(Team team) {
		if (team == null || team.getAbbreviation() == null || team.getAbbreviation().equals("")) {
			return "---";
		}
		return team.getAbbreviation();
	}

	public static String getShortName(Team team) {
		if (team == null || team.getShortName() == null || team.getShortName().equals("")) {
			return "-";
		}
		return team.getShortName();
	}

	public static String getCityName(Team team) {
		if (team == null || team.getCity() == null || team.getCity().equals("")) {
			return "-";
		}
		return team.getCity();
	}

	public static String getConferenceName(Team team) {
		if (team == null || team.getConference() == null || team.getConference().equals("")) {
			return "-";
		}
		if ("Ouest".equals(team.getConference())) {
			return "Ouest";
		}
		if ("Est".equals(team.getConference())) {
			return "Est";
		}
		return "-";
	}

	public static String getDivisionName(Team team) {
		if (team == null || team.getDivision() == null || team.getDivision().equals("")) {
			return "-";
		}
		return team.getDivision();
	}
}

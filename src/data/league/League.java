package data.league;

import java.util.ArrayList;

import config.CalendarConfiguration;
import data.player.Player;
import data.team.Team;

public class League {
	private Conference westernConference;
	private Conference easternConference;
	private LeagueFinance leagueFinance = null ; 
	private RegularSeason regularSeason;
	private Playoff playoff;

	public League() {
		westernConference = new Conference("West");
		easternConference = new Conference("East") ; 
		regularSeason = new RegularSeason(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE,
				CalendarConfiguration.REGULAR_SEASON_END_DATE);
		playoff = new Playoff(CalendarConfiguration.PLAYOFF_DEBUT_DATE, CalendarConfiguration.PLAYOFF_END_DATE);
	}

	public Conference getWesternConference() {
		return westernConference;
	}

	public void setWesternConference(Conference westernConference) {
		this.westernConference = westernConference;
	}

	public Conference getEasternConference() {
		return easternConference;
	}

	public void setEasternConfernce(Conference easternConfernce) {
		this.easternConference = easternConfernce;
	}


	public RegularSeason getReagularSeason() {
		return regularSeason;
	}

	public void setReagularSeason(RegularSeason reagularSeason) {
		this.regularSeason = reagularSeason;
	}

	public Playoff getPlayoff() {
		return playoff;
	}

	public void setPlayoff(Playoff playoff) {
		this.playoff = playoff;
	}

	public void addTeamWesternConference(Team team, String divisionName) {
		westernConference.addTeam(team, divisionName);
	}

	public void addTeamEasternConference(Team team, String divisionName) {
		easternConference.addTeam(team, divisionName);
	}

	public void addDivisionWesternConference(Division division) {
		westernConference.addDivision(division);
	}

	public void addDivisionEasternConference(Division division) {
		easternConference.addDivision(division);
	}

	public void addPlayerWesternConference(Player player, String divisionName, String teamName) {
		westernConference.getDivisions().get(divisionName).getTeams().get(teamName).addPlayer(player);
	}

	public void addPlayerEasternConference(Player player, String divisionName, String teamName) {
		easternConference.getDivisions().get(divisionName).getTeams().get(teamName).addPlayer(player);
	}

	public ArrayList<Team> getAllTeam() {
		ArrayList<Team> teams = new ArrayList<Team>();
		for (Division division : westernConference.getDivisions().values()) {
			teams.addAll(division.getTeams().values());
		}
		for (Division division : easternConference.getDivisions().values()) {
			teams.addAll(division.getTeams().values());
		}
		return teams;

	}

	public void setLeagueFinance(LeagueFinance leagueFinance) {
		this.leagueFinance = leagueFinance;
	}

	public LeagueFinance getLeagueFinance() {
		return leagueFinance;
	}
	
	
	
	

}

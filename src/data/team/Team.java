/*
	* Decompiled with CFR 0.152.
	*/
package data.team;

import java.util.HashMap;

import data.player.Player;
import data.sport.setup.Game;
import data.team.calendar.Schedule;
import data.team.finance.TeamFinance;

public class Team {
	private String name;
	private String city;
	private String shortName;
	private String abbreviation;
	private String conference;
	private String division;
	private String rival;
	private double formerPopularity;
	private double currentPopularity;
	private TeamPerformance teamPerformance = new TeamPerformance();
	private HashMap<String, Player> formerPlayers = new HashMap<>();
	private HashMap<String, Player> currentPlayers = new HashMap<>();
	private Schedule schedule;
	private Player starPlayer = null;
	private TeamFinance teamFinance;
	private Stadium stadium;

	public Team(String name, String rival, double popularity, TeamFinance teamFinance, Stadium stadium) {
		this.name = name;
		this.rival = rival;
		this.schedule = new Schedule();
		this.formerPopularity = popularity;
		this.currentPopularity = popularity;
		this.schedule = new Schedule();
		this.starPlayer = null;
		this.teamFinance = teamFinance;
		this.stadium = stadium;
	}

	public String getName() {
		return this.name;
	}

	public void setNom(String name) {
		this.name = name;
	}

	public String getRival() {
		return this.rival;
	}

	public void setRival(String rival) {
		this.rival = rival;
	}

	public Schedule getSchedule() {
		return this.schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public boolean hasStarPlayer() {
		return this.starPlayer != null;
	}

	public void addGame(Game game) {
		this.schedule.addGame(game);
	}

	public Player getStarPlayer() {
		return this.starPlayer;
	}

	public void setStarPlayer(Player starPlayer) {
		this.starPlayer = starPlayer;
	}

	public TeamFinance getTeamFinance() {
		return this.teamFinance;
	}

	public Stadium getStadium() {
		return this.stadium;
	}

	public TeamPerformance getTeamPerformance() {
		return this.teamPerformance;
	}

	public void setTeamPerformance(TeamPerformance teamPerformance) {
		this.teamPerformance = teamPerformance;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getConference() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference = conference;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public void setTeamFinance(TeamFinance teamFinance) {
		this.teamFinance = teamFinance;
	}

	public void setStadium(Stadium stadium) {
		this.stadium = stadium;
	}

	public HashMap<String, Player> getFormerPlayers() {
		return formerPlayers;
	}

	public void setFormerPlayers(HashMap<String, Player> formerPlayers) {
		this.formerPlayers = formerPlayers;
	}

	public HashMap<String, Player> getCurrentPlayers() {
		return currentPlayers;
	}

	public void setCurrentPlayers(HashMap<String, Player> currentPlayers) {
		this.currentPlayers = currentPlayers;
	}

	public void addFirstPlayer(Player player) {
		formerPlayers.put(player.getName(), player);
		currentPlayers.put(player.getName(), player);
	}

	public double getFormerPopularity() {
		return formerPopularity;
	}

	public void setFormerPopularity(double formerPopularity) {
		this.formerPopularity = formerPopularity;
	}

	public double getCurrentPopularity() {
		return currentPopularity;
	}

	public void setCurrentPopularity(double currentPopularity) {
		this.currentPopularity = currentPopularity;
	}

}

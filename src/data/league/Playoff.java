/*
	* Decompiled with CFR 0.152.
	*/
package data.league;

import java.time.LocalDate;
import java.util.ArrayList;

import data.sport.setup.PlayoffSeries;
import data.team.Team;

public class Playoff
		extends Season {
	private PlayoffRound currentRound;
	private ArrayList<Team> qualifiedEastTeams = new ArrayList<>();
	private ArrayList<Team> qualifiedWestTeams = new ArrayList<>();

	private ArrayList<PlayoffSeries> eastFirstRound = new ArrayList<>();
	private ArrayList<PlayoffSeries> westFirstRound = new ArrayList<>();

	private ArrayList<PlayoffSeries> eastConferenceSemis = new ArrayList<>();
	private ArrayList<PlayoffSeries> westConferenceSemis = new ArrayList<>();

	private ArrayList<PlayoffSeries> eastConferenceFinals = new ArrayList<>();
	private ArrayList<PlayoffSeries> westConferenceFinals = new ArrayList<>();

	private ArrayList<PlayoffSeries> nbaFinals = new ArrayList<>();

	public Playoff(LocalDate localDate, LocalDate localDate2) {
		super(localDate, localDate2);
	}

	public ArrayList<Team> getQualifiedEastTeams() {
		return qualifiedEastTeams;
	}

	public PlayoffRound getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(PlayoffRound currentRound) {
		this.currentRound = currentRound;
	}

	public void setQualifiedEastTeams(ArrayList<Team> qualifiedEastTeams) {
		this.qualifiedEastTeams = qualifiedEastTeams;
	}

	public ArrayList<Team> getQualifiedWestTeams() {
		return qualifiedWestTeams;
	}

	public void setQualifiedWestTeams(ArrayList<Team> qualifiedWestTeams) {
		this.qualifiedWestTeams = qualifiedWestTeams;
	}

	public ArrayList<PlayoffSeries> getEastFirstRound() {
		return eastFirstRound;
	}

	public void setEastFirstRound(ArrayList<PlayoffSeries> eastFirstRound) {
		this.eastFirstRound = eastFirstRound;
	}

	public ArrayList<PlayoffSeries> getWestFirstRound() {
		return westFirstRound;
	}

	public void setWestFirstRound(ArrayList<PlayoffSeries> westFirstRound) {
		this.westFirstRound = westFirstRound;
	}

	public ArrayList<PlayoffSeries> getEastConferenceSemis() {
		return eastConferenceSemis;
	}

	public void setEastConferenceSemis(ArrayList<PlayoffSeries> eastConferenceSemis) {
		this.eastConferenceSemis = eastConferenceSemis;
	}

	public ArrayList<PlayoffSeries> getWestConferenceSemis() {
		return westConferenceSemis;
	}

	public void setWestConferenceSemis(ArrayList<PlayoffSeries> westConferenceSemis) {
		this.westConferenceSemis = westConferenceSemis;
	}

	public ArrayList<PlayoffSeries> getEastConferenceFinals() {
		return eastConferenceFinals;
	}

	public void setEastConferenceFinals(ArrayList<PlayoffSeries> eastConferenceFinals) {
		this.eastConferenceFinals = eastConferenceFinals;
	}

	public ArrayList<PlayoffSeries> getWestConferenceFinals() {
		return westConferenceFinals;
	}

	public void setWestConferenceFinals(ArrayList<PlayoffSeries> westConferenceFinals) {
		this.westConferenceFinals = westConferenceFinals;
	}

	public ArrayList<PlayoffSeries> getNbaFinals() {
		return nbaFinals;
	}

	public void setNbaFinals(ArrayList<PlayoffSeries> nbaFinals) {
		this.nbaFinals = nbaFinals;
	}

	public void addQualifiedEastTeam(Team team) {
		qualifiedEastTeams.add(team);
	}

	public void addQualifiedWestTeam(Team team) {
		qualifiedWestTeams.add(team);
	}

}

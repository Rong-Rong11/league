/*
 * Decompiled with CFR 0.152.
 */
package data.league;

import java.util.ArrayList;

import config.CalendarConfiguration;
import data.league.finance.LeagueFinance;
import data.player.Player;
import data.team.Team;

public class League {
    private Conference westernConference = new Conference("West");
    private Conference easternConference = new Conference("East");
    private LeagueFinance leagueFinance = null;
    private RegularSeason regularSeason = new RegularSeason(CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE,
            CalendarConfiguration.REGULAR_SEASON_END_DATE);
    private Playoff playoff = new Playoff(CalendarConfiguration.PLAYOFF_DEBUT_DATE,
            CalendarConfiguration.PLAYOFF_END_DATE);

    public Conference getWesternConference() {
        return this.westernConference;
    }

    public void setWesternConference(Conference conference) {
        this.westernConference = conference;
    }

    public Conference getEasternConference() {
        return this.easternConference;
    }

    public void setEasternConfernce(Conference conference) {
        this.easternConference = conference;
    }

    public RegularSeason getReagularSeason() {
        return this.regularSeason;
    }

    public void setReagularSeason(RegularSeason regularSeason) {
        this.regularSeason = regularSeason;
    }

    public Playoff getPlayoff() {
        return this.playoff;
    }

    public void setPlayoff(Playoff playoff) {
        this.playoff = playoff;
    }

    public void addTeamWesternConference(Team team, String string) {
        this.westernConference.addTeam(team, string);
    }

    public void addTeamEasternConference(Team team, String string) {
        this.easternConference.addTeam(team, string);
    }

    public void addDivisionWesternConference(Division division) {
        this.westernConference.addDivision(division);
    }

    public void addDivisionEasternConference(Division division) {
        this.easternConference.addDivision(division);
    }

    public void addPlayerWesternConference(Player player, String string, String string2) {
        this.westernConference.getDivisions().get(string).getTeams().get(string2).addFirstPlayer(player);
    }

    public void addPlayerEasternConference(Player player, String string, String string2) {
        this.easternConference.getDivisions().get(string).getTeams().get(string2).addFirstPlayer(player);
    }

    public ArrayList<Team> getAllTeam() {
        ArrayList<Team> arrayList = new ArrayList<Team>();
        for (Division division : this.westernConference.getDivisions().values()) {
            arrayList.addAll(division.getTeams().values());
        }
        for (Division division : this.easternConference.getDivisions().values()) {
            arrayList.addAll(division.getTeams().values());
        }
        return arrayList;
    }

    public void setLeagueFinance(LeagueFinance leagueFinance) {
        this.leagueFinance = leagueFinance;
    }

    public LeagueFinance getLeagueFinance() {
        return this.leagueFinance;
    }
}

package data.league;

import data.team.Team;
import java.util.HashMap;


public class Division {
    private String name;
    private HashMap<String, Team> teams;

    public Division(String string) {
        this.name = string;
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

    public void setName(String string) {
        this.name = string;
    }

    public String toString() {
        String string = this.name;
        for (Team team : this.teams.values()) {
            string = string + "\n" + team.toString();
        }
        return string;
    }
}

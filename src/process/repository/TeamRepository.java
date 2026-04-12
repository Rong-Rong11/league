/*
 * Decompiled with CFR 0.152.
 */
package process.repository;

import java.util.ArrayList;
import java.util.HashMap;

import data.team.Team;

public class TeamRepository {
    private HashMap<String, Team> teams = new HashMap<>();
    private static TeamRepository instance = new TeamRepository();

    private TeamRepository() {
    }

    public static TeamRepository getInstance() {
        return instance;
    }

    public void register(String string, Team team) {
        this.teams.put(string, team);
    }

    public Team getTeam(String string) {
        if (this.teams.containsKey(string)) {
            return this.teams.get(string);
        }
        return null;
    }

    public ArrayList<Team> getAllTeams() {
        ArrayList<Team> arrayList = new ArrayList<Team>(this.teams.values());
        return arrayList;
    }

    public void clear() {
        this.teams.clear();
    }
}

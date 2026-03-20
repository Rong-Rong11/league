/*
 * Decompiled with CFR 0.152.
 */
package process.repositery;

import data.team.Team;
import java.util.ArrayList;
import java.util.HashMap;

public class TeamRepositery {
    private HashMap<String, Team> teams = new HashMap();
    private static TeamRepositery instance = new TeamRepositery();

    private TeamRepositery() {
    }

    public static TeamRepositery getInstance() {
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

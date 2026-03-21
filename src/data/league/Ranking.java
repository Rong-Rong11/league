/*
 * Decompiled with CFR 0.152.
 */
package data.league;

import data.team.Team;
import java.util.TreeMap;

public class Ranking {
    public TreeMap<Integer, Team> ranking = new TreeMap();

    public TreeMap<Integer, Team> getRanking() {
        return this.ranking;
    }

    public void setRanking(TreeMap<Integer, Team> treeMap) {
        this.ranking = treeMap;
    }
}

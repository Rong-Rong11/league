/*
 * Decompiled with CFR 0.152.
 */
package data.league;

import java.time.LocalDate;
import java.util.ArrayList;

import data.team.Team;

public class Playoff
        extends Season {
    private ArrayList<Team> qualifiedEastTeams = new ArrayList<>();
    private ArrayList<Team> qualifiedWestTeams = new ArrayList<>();

    public Playoff(LocalDate localDate, LocalDate localDate2) {
        super(localDate, localDate2);
    }
}

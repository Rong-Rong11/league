/*
 * Decompiled with CFR 0.152.
 */
package data.sport.setup;

import data.team.Team;
import process.utility.CalendarUtilitary;

public class GameContext {
    private Team homeTeam;
    private Team awayTeam;
    private int typeGame;
    private boolean isScheduled;
    private boolean isRivalry;

    public GameContext(Team team, Team team2, int n) {
        this.setAwayTeam(team2);
        this.setHomeTeam(team);
        this.isRivalry = CalendarUtilitary.isRivalry(this);
    }

    public boolean isScheduled() {
        return this.isScheduled;
    }

    public void setScheduled(boolean bl) {
        this.isScheduled = bl;
    }

    public Team getHomeTeam() {
        return this.homeTeam;
    }

    public void setHomeTeam(Team team) {
        this.homeTeam = team;
    }

    public Team getAwayTeam() {
        return this.awayTeam;
    }

    public void setAwayTeam(Team team) {
        this.awayTeam = team;
    }

    public int getTypeGame() {
        return this.typeGame;
    }

    public void setTypeGame(int n) {
        this.typeGame = n;
    }

    public boolean isRivalry() {
        return this.isRivalry;
    }
}

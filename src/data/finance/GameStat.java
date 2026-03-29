/*
 * Decompiled with CFR 0.152.
 */
package data.finance;

import data.finance.TeamGameFinance;
import data.sport.setup.Game;

public class GameStat {
    private Game game;
    private int attendees = 0;
    private double ticketPrice = 0.0;
    private double attendanceRate = 0.0;
    private double popularity = 0.0;
    private TeamGameFinance homeFinance = new TeamGameFinance();
    private TeamGameFinance awayFinance = new TeamGameFinance();
    
    public GameStat(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public TeamGameFinance getHomeFinance() {
        return this.homeFinance;
    }

    public void setHomeFinance(TeamGameFinance teamGameFinance) {
        this.homeFinance = teamGameFinance;
    }

    public TeamGameFinance getAwayFinance() {
        return this.awayFinance;
    }

    public void setAwayFinance(TeamGameFinance teamGameFinance) {
        this.awayFinance = teamGameFinance;
    }

    public int getAttendees() {
        return this.attendees;
    }

    public void setAttendees(int n) {
        this.attendees = n;
    }

    public double getTicketPrice() {
        return this.ticketPrice;
    }

    public void setTicketPrice(double d) {
        this.ticketPrice = d;
    }

    public double getAttendanceRate() {
        return this.attendanceRate;
    }

    public void setAttendanceRate(double d) {
        this.attendanceRate = d;
    }

    public double getPopularity() {
        return this.popularity;
    }

    public void setPopularity(double d) {
        this.popularity = d;
    }
}

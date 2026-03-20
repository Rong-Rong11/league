/*
 * Decompiled with CFR 0.152.
 */
package process.manager.financetools;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Stadium;
import data.team.Team;
import java.time.LocalDate;
import process.utilitary.CalendarUtilitary;

public class GameRevenueSimulator {
    private GameStat gameStat;

    public GameRevenueSimulator(GameStat gameStat) {
        this.gameStat = gameStat;
    }

    public void calculateGameRevenue(Game game, LocalDate localDate) {
        Team team = game.getGameContext().getHomeTeam();
        double d = this.calculatePopularityRate(game, localDate);
        Stadium stadium = team.getStadium();
        int n = stadium.getCapacity();
        double d2 = this.calculateAttendanceRate(localDate, team, d);
        int n2 = this.calculateAttendees(n, d2);
        int n3 = this.calculateTicketPrice(stadium, d);
        this.calculateTicketRevenue(n2, n3);
        this.calculateConcessionsRevenue(n2);
        this.calculateParkingRevenue(n2);
        this.calculateTVRevenue();
        this.calculateMerchRevenue(d, n2);
    }

    private double calculatePopularityRate(Game game, LocalDate localDate) {
        double d = CalendarUtilitary.popularityScoreGame(game, localDate);
        double d2 = d / 800.0;
        double d3 = (game.getGameContext().getHomeTeam().getTeamPerformance().getPerformanceRating() + game.getGameContext().getAwayTeam().getTeamPerformance().getPerformanceRating()) / 2.0;
        double d4 = d2 * 0.6 + d3 * 0.4;
        this.gameStat.setPopularity(d4);
        return Math.max(0.2, Math.min(1.0, d4));
    }

    private int calculateTicketPrice(Stadium stadium, double d) {
        double d2 = stadium.getTicketPrice();
        double d3 = 0.5;
        int n = (int)(d2 * (1.0 + (d - 0.5) * d3));
        this.gameStat.setTicketPrice(n);
        return n;
    }

    private int calculateAttendees(int n, double d) {
        int n2 = (int)((double)n * d);
        this.gameStat.setAttendees(n2);
        return n2;
    }

    private double calculateAttendanceRate(LocalDate localDate, Team team, double d) {
        double d2 = CalendarUtilitary.isImportantDay(localDate) ? 0.04 : 0.0;
        double d3 = Math.random() * 0.05 - 0.025;
        double d4 = 0.5 + d * 0.35 + d2 + d3;
        d4 = Math.max(0.55, Math.min(0.98, d4));
        this.gameStat.setAttendanceRate(d4);
        return d4;
    }

    private void calculateTicketRevenue(int n, double d) {
        double d2 = (double)n * d / 1000000.0;
        this.gameStat.getHomeFinance().setTicketRevenue(d2);
    }

    private void calculateConcessionsRevenue(int n) {
        double d = 0.7;
        double d2 = 18.0;
        double d3 = (double)n * d * d2 / 1000000.0;
        this.gameStat.getHomeFinance().setConcessionsRevenue(d3);
    }

    private void calculateParkingRevenue(int n) {
        double d = 0.35;
        double d2 = 25.0;
        double d3 = 2.3;
        double d4 = (double)n / d3;
        double d5 = d4 * d * d2 / 1000000.0;
        this.gameStat.getHomeFinance().setParkingRevenue(d5);
    }

    private void calculateTVRevenue() {
        double d = 1.2;
        double d2 = d * 0.6;
        double d3 = d * 0.4;
        this.gameStat.getHomeFinance().setTvRevenue(d2);
        this.gameStat.getAwayFinance().setTvRevenue(d3);
    }

    private void calculateMerchRevenue(double d, int n) {
        double d2 = 0.03 + d * 0.04;
        double d3 = 40.0;
        double d4 = (double)n * d2 * d3 / 1000000.0;
        this.gameStat.getHomeFinance().setMerchRevenue(d4);
    }
}

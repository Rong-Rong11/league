package process.simulator;

import java.time.LocalDate;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Stadium;
import data.team.Team;
import process.utilitary.CalendarUtilitary;

public class GameRevenueSimulator {

    private GameStat gameStat;

    public GameRevenueSimulator(GameStat gameStat) {
        this.gameStat = gameStat;
    }

    public void calculateGameRevenue(Game game, LocalDate date) {
        Team homeTeam = game.getGameContext().getHomeTeam();
        double popularityRate = calculatePopularityRate(game, date);
        Stadium stadium = homeTeam.getStadium();
        int capacity = stadium.getCapacity();
        int attendees = calculateAttendees(capacity, popularityRate);
        calculateAttendanceRate(attendees, capacity);
        int ticketPrice = calculateTicketPrice(stadium, popularityRate);
        calculateTicketRevenue(attendees, ticketPrice);
        calculateConcessionsRevenue(attendees);
        calculateParkingRevenue(attendees);
        calculateTVRevenue();
        calculateMerchRevenue(popularityRate, attendees);

    }

    private double calculatePopularityRate(Game game, LocalDate date) {
        double gamePopularity = CalendarUtilitary.popularityScoreGame(game, date);
        double gameScore = gamePopularity / 1200;
        double performatingRate = (game.getGameContext().getHomeTeam().getTeamPerformance().getPerformanceRating() +
                game.getGameContext().getAwayTeam().getTeamPerformance().getPerformanceRating())
                / 2;
        double popularityRate = (gameScore * 0.6) + (performatingRate * 0.4);
        gameStat.setPopularity(popularityRate);
        return Math.max(0.2, Math.min(1.0, popularityRate));
    }

    private int calculateTicketPrice(Stadium stadium, double popularityRate) {
        double base = stadium.getTicketPrice();
        double coefficient = 0.5;
        int newPrice = (int) (base * (1 + (popularityRate - 0.5) * coefficient));
        gameStat.setTicketPrice(newPrice);
        return newPrice;
    }

    private int calculateAttendees(int capacity, double popularityRate) {
        int attendees = (int) (capacity * popularityRate);
        gameStat.setAttendees(attendees);
        return attendees;
    }

    private void calculateAttendanceRate(int attendees, int capacity) {
        double attendanceRate = (double) attendees / capacity;
        gameStat.setAttendanceRate(attendanceRate);
    }

    private void calculateTicketRevenue(int attendees, double ticketPrice) {
        double ticketRevenue = (attendees * ticketPrice) / 1000000;
        gameStat.getHomeFinance().setTicketRevenue(ticketRevenue);
    }

    private void calculateConcessionsRevenue(int attendees) {
        double purchaseRate = 0.7;
        double averageSpend = 18;

        double revenue = (attendees * purchaseRate * averageSpend) / 1000000;

        gameStat.getHomeFinance().setConcessionsRevenue(revenue);
    }

    private void calculateParkingRevenue(int attendees) {
        double parkingRate = 0.35;
        double parkingPrice = 25;
        double peoplePerCar = 2.3;

        double cars = attendees / peoplePerCar;
        double revenue = (cars * parkingRate * parkingPrice) / 1000000;

        gameStat.getHomeFinance().setParkingRevenue(revenue);
    }

    private void calculateTVRevenue() {
        double leagueTVPerGame = 1.2;

        double homeShare = leagueTVPerGame * 0.6;
        double awayShare = leagueTVPerGame * 0.4;

        gameStat.getHomeFinance().setTvRevenue(homeShare);
        gameStat.getAwayFinance().setTvRevenue(awayShare);
    }

    private void calculateMerchRevenue(double popularityRate, int attendees) {
        double purchaseRate = 0.03 + (popularityRate * 0.04);
        double averageSpend = 40;
        double revenue = (attendees * purchaseRate * averageSpend) / 1000000;
        gameStat.getHomeFinance().setMerchRevenue(revenue);
    }

}

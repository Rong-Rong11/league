package process.manager.financetools;

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
        double attendanceRate = calculateAttendanceRate(date, homeTeam, popularityRate);
        int attendees = calculateAttendees(capacity, attendanceRate);
        int ticketPrice = calculateTicketPrice(stadium, popularityRate);
        calculateTicketRevenue(attendees, ticketPrice);
        calculateConcessionsRevenue(attendees);
        calculateParkingRevenue(attendees);
        calculateTVRevenue();
        calculateMerchRevenue(popularityRate, attendees);

    }

    private double calculatePopularityRate(Game game, LocalDate date) {
        double gamePopularity = CalendarUtilitary.popularityScoreGame(game, date);
        double gameScore = gamePopularity / 800;
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

    private int calculateAttendees(int capacity, double attendanceRate) {
        int attendees = (int) (capacity * attendanceRate);
        gameStat.setAttendees(attendees);
        return attendees;
    }

    private double calculateAttendanceRate(LocalDate date, Team homeTeam, double popularityRate) {
        double importantDayBonus = CalendarUtilitary.isImportantDay(date) ? 0.04 : 0.0;
        double randomVariation = (Math.random() * 0.05) - 0.025;

        double attendanceRate = 0.50
                + (popularityRate * 0.35)
                + importantDayBonus
                + randomVariation;
        attendanceRate = Math.max(0.55, Math.min(0.98, attendanceRate));
        gameStat.setAttendanceRate(attendanceRate);
        return attendanceRate;
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

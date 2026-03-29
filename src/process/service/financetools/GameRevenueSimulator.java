package process.service.financetools;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Stadium;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import java.time.LocalDate;
import process.utilitary.CalendarUtilitary;
import process.utilitary.FinanceUtilitary;
import process.visitor.marketsize.CalculateBaseTicketVisitor;

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
        int ticketPrice = calculateTicketPrice(homeTeam, stadium, popularityRate, attendees);

        calculateTicketRevenue(attendees, ticketPrice);
        calculateConcessionsRevenue(homeTeam, attendees, popularityRate);
        calculateParkingRevenue(homeTeam, attendees);
        calculateTVRevenue();
        calculateMerchRevenue(homeTeam, popularityRate, attendees);
    }

    private double calculatePopularityRate(Game game, LocalDate date) {
        Team homeTeam = game.getGameContext().getHomeTeam();

        double gamePopularity = CalendarUtilitary.popularityScoreGame(game, date);
        double gameScore = gamePopularity / 800;

        double performatingRate = (game.getGameContext().getHomeTeam().getTeamPerformance().getPerformanceRating()
                + game.getGameContext().getAwayTeam().getTeamPerformance().getPerformanceRating()) / 2;

        double popularityRate = (gameScore * 0.6) + (performatingRate * 0.4);

        int winStreak = homeTeam.getTeamPerformance().getCurrentWinStreak();
        popularityRate += Math.min(winStreak, 8) * 0.01;

        popularityRate = Math.max(0.2, Math.min(1.0, popularityRate));
        gameStat.setPopularity(popularityRate);
        return popularityRate;
    }

    private int calculateTicketPrice(Team homeTeam, Stadium stadium, double popularityRate, int attendees) {
        MarketSize marketSize = homeTeam.getTeamFinance().getMarketSize();
        MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
        EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();
        double teamValueFactor = FinanceUtilitary.getNormalizedTeamValue(homeTeam);

        double base = stadium.getTicketPrice();
        base = marketSize.accept(new CalculateBaseTicketVisitor());

        double popularityFactor = 1 + (popularityRate - 0.5) * 0.35;
        double price = base * popularityFactor;

        price *= (1 + mediaMarket.getPricingPowerModifier() * 0.12);

        price *= (1 + economicProfil.getHistoricalPrestige() * 0.05);
        price *= (1 - economicProfil.getPriceElasticity() * 0.18);
        price *= (1 + teamValueFactor * 0.08);

        if (stadium.getCapacity() > 0) {
            double occupancyRate = (double) attendees / stadium.getCapacity();
            if (occupancyRate > 0.9) {
                price *= 1.05;
            }
        }

        int newPrice = (int) Math.max(5, Math.round(price));
        gameStat.setTicketPrice(newPrice);
        return newPrice;
    }

    private int calculateAttendees(int capacity, double attendanceRate) {
        int attendees = (int) (capacity * attendanceRate);
        gameStat.setAttendees(attendees);
        return attendees;
    }

    private double calculateAttendanceRate(LocalDate date, Team homeTeam, double popularityRate) {
        MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
        EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();
        double teamValueFactor = FinanceUtilitary.getNormalizedTeamValue(homeTeam);

        double importantDayBonus = CalendarUtilitary.isImportantDay(date) ? 0.04 : 0.0;

        double attendanceRate = (0.45
                + (popularityRate * 0.4)
                + importantDayBonus);

        attendanceRate += mediaMarket.getFanBaseModifier() * 0.08;

        attendanceRate += economicProfil.getFanLoyalty() * 0.10;
        attendanceRate += economicProfil.getHistoricalPrestige() * 0.03;
        attendanceRate += teamValueFactor * 0.04;

        double volatility = 0.15;

        if (popularityRate < 0.4) {
            volatility = 0.25;
        } else if (popularityRate > 0.8) {
            volatility = 0.05;
        }

        double randomFactor = 1 - volatility + (Math.random() * 2 * volatility);

        if (Math.random() < 0.60) {
            randomFactor -= Math.random() * 0.06;
        }

        attendanceRate *= randomFactor;

        attendanceRate = Math.max(0.50, Math.min(0.95, attendanceRate));
        gameStat.setAttendanceRate(attendanceRate);
        return attendanceRate;
    }

    private void calculateTicketRevenue(int attendees, double ticketPrice) {
        double ticketRevenue = (attendees * ticketPrice) / 1000000;
        gameStat.getHomeFinance().setTicketRevenue(ticketRevenue);
    }

    private void calculateConcessionsRevenue(Team homeTeam, int attendees, double popularityRate) {
        EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();
        MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();

        double purchaseRate = 0.55;
        double averageSpend = 14;

        if (economicProfil != null) {
            purchaseRate += economicProfil.getFanLoyalty() * 0.05;
            averageSpend *= (1 + economicProfil.getHistoricalPrestige() * 0.04);
        }

        if (mediaMarket != null) {
            averageSpend *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.05);
        }

        averageSpend *= (1 + popularityRate * 0.03);
        double revenue = (attendees * purchaseRate * averageSpend) / 1000000;

        gameStat.getHomeFinance().setConcessionsRevenue(revenue);
    }

    private void calculateParkingRevenue(Team homeTeam, int attendees) {
        MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
        EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

        double parkingRate = 0.28;
        double parkingPrice = 18;
        double peoplePerCar = 2.3;

        if (mediaMarket != null) {
            parkingPrice *= (1 + mediaMarket.getPricingPowerModifier() * 0.05);
        }

        if (economicProfil != null) {
            parkingRate += economicProfil.getFanLoyalty() * 0.015;
        }

        double cars = attendees / peoplePerCar;
        double revenue = (cars * parkingRate * parkingPrice) / 1000000;

        gameStat.getHomeFinance().setParkingRevenue(revenue);
    }

    private void calculateTVRevenue() {
        double leagueTVPerGame = 0.7;

        double homeShare = leagueTVPerGame * 0.6;
        double awayShare = leagueTVPerGame * 0.4;

        gameStat.getHomeFinance().setTvRevenue(homeShare);
        gameStat.getAwayFinance().setTvRevenue(awayShare);
    }

    private void calculateMerchRevenue(Team homeTeam, double popularityRate, int attendees) {
        MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
        EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();
        double teamValueFactor = FinanceUtilitary.getNormalizedTeamValue(homeTeam);

        double purchaseRate = 0.015 + (popularityRate * 0.025);
        double averageSpend = 28;

        if (economicProfil != null) {
            purchaseRate += economicProfil.getFanLoyalty() * 0.008;
            purchaseRate += economicProfil.getHistoricalPrestige() * 0.012;
            averageSpend *= (1 + economicProfil.getHistoricalPrestige() * 0.05);
        }

        if (mediaMarket != null) {
            purchaseRate += mediaMarket.getPrestigeModifier() * 0.005;
            averageSpend *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.04);
        }

        purchaseRate += teamValueFactor * 0.01;
        averageSpend *= (1 + teamValueFactor * 0.06);

        double revenue = (attendees * purchaseRate * averageSpend) / 1000000;
        gameStat.getHomeFinance().setMerchRevenue(revenue);
    }

}

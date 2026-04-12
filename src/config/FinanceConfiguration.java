package config;

import java.time.LocalDate;

public class FinanceConfiguration {
    // les chiffres en millions

    public static final double INITIAL_LEAGUE_BUDGET = 9278.82;
    public static final double INITIAL_LEAGUE_VALUE = 120000.0;
    public static final int NUMBER_OF_FINANCIAL_MONTHS = 12;
    public static final double LUXURYTAX_THRESHOLD_RATE = 1.215;
    public static final double LUXURY_TAX_RATE_BASE = 1.5;
    public static final double PLAYER_SHARE = 0.50;
    public static final double MINIMUM_TEAM_SALARY_RATE = 0.85;
    public static final double LEAGUE_ADMINISTRATIVE_COST = 0.35;
    public static final double LEAGUE_MEDIA_COST = 0.25;
    public static final double LEAGUE_MARKETING_COST = 0.20;
    public static final double LEAGUE_OFFICIATING_COST = 0.15;

    public static final double MARKET_SIZE_SMALL_MULTIPLIER = 0.7;
    public static final double MARKET_SIZE_MEDIUM_MULTIPLIER = 1;
    public static final double MARKET_SIZE_LARGE_MULTIPLIER = 1.3;

    public static final double BASE_TEAM_BUDGET = 200;
    public static final double BASE_STADIUM_COSTS = 0.2;
    public static final double BASE_TICKET_PRICE = 45; // pas en millions
    public static final double ATTENDANCE_RATE_BASE = 0.5;
    public static final double BASE_TRAVEL_INTRA_DIVISION_COST = 0.02;
    public static final double BASE_TRAVEL_INTRA_CONFERENCE_COST = 0.05;
    public static final double BASE_TRAVEL_INTER_CONFERENCE_COST = 0.09;

    public static final int MAX_PRESEASON_BIG_TRADE = 3;
    public static final int MAX_TRADE_PER_TEAM = 5;
    public static final int MAX_TRADE_ATTEMPTS_PER_TEAM = 5;
    public static final LocalDate PRESEASON_TRADE = LocalDate.of(CalendarConfiguration.SEASON_YEAR, 8, 1);
    public static final String SEASON_TRADE_INTENT_BUYER = "buyer";
    public static final String SEASON_TRADE_INTENT_SELLER = "seller";
    public static final String SEASON_TRADE_INTENT_STABLE = "stable";

    public static final double REVENUE_SHARING_REDISTRIBUTION_RATE = 0.25;
    public static final double LEAGUE_OPERATING_RATE = 0.07;

    public static final double REGULAR_SEASON_LOCAL_SPONSORING_RATE = 1.00;
    public static final double PLAYOFF_LOCAL_SPONSORING_RATE = 1.05;
    public static final double REGULAR_SEASON_LOCAL_MERCH_RATE = 1.00;
    public static final double PLAYOFF_LOCAL_MERCH_RATE = 1.15;
    public static final double REGULAR_SEASON_OTHER_LOCAL_RATE = 1.00;
    public static final double PLAYOFF_OTHER_LOCAL_RATE = 1.10;
    public static final double REGULAR_SEASON_CENTRAL_TV_RATE = 1.08;
    public static final double PLAYOFF_CENTRAL_TV_RATE = 1.25;
    public static final double REGULAR_SEASON_CENTRAL_SPONSORING_RATE = 1.08;
    public static final double PLAYOFF_CENTRAL_SPONSORING_RATE = 1.20;
    public static final double REGULAR_SEASON_CENTRAL_MERCH_RATE = 1.12;
    public static final double PLAYOFF_CENTRAL_MERCH_RATE = 1.28;

    public static final double INFLATION_RATE = 1.5;
    public static final double CAP_GROWTH_RATE = 0.1;
}

package config;

import java.time.LocalDate;

public class FinanceConfiguration {
    // les chiffres en millions

    public static final double INITIAL_LEAGUE_BUDGET = 10_500;
    public static final int NUMBER_OF_FINANCIAL_MONTHS = 12;
    public static final double LUXURYTAX_THRESHOLD_RATE = 1.20;
    public static final double LUXURY_TAX_RATE_BASE = 1.5;
    public static final double PLAYER_SHARE = 0.45;
    public static final double MINIMUM_TEAM_SALARY_RATE = 0.9;

    public static final String INCOME_TYPE_NATIONAL_TV = "national TV";
    public static final String INCOME_TYPE_LOCAL_TV = "local TV";
    public static final String INCOME_TYPE_NATIONAL_SPONSORING = "national sponsoring";
    public static final String INCOME_TYPE_LOCAL_SPONSORING = "local sponsoring";
    public static final String INCOME_TYPE_NATIONAL_MERCHANDISING = "national merchandising";
    public static final String INCOME_TYPE_LOCAL_MERCHANDISING = "local merchandising";
    public static final String INCOME_TYPE_TICKET_OFFICE = "ticket office";
    public static final String INCOME_TYPE_REVENUE_SHARING = "income revenue sharing";
    public static final String INCOME_TYPE_CONCESSIONS = "concessions";
    public static final String INCOME_TYPE_PARKING = "parking";

    public static final String INCOME_TYPE_OTHER = "others";
    public static final String INCOME_TYPE_CENTRAL_SHARE = "central share";
    public static final String INCOME_TYPE_POOL_SHARE = "pool share";

    public static final String EXPENSE_TYPE_REVENUE_SHARING = "expense revenue sharing";
    public static final String EXPENSE_TYPE_PLAYER_SALARY = "expense player salary";
    public static final String EXPENSE_TYPE_STADIUM_COST = "expense stadium cost";
    public static final String EXPENSE_TYPE_STAFF_COST = "expense stAff cost";
    public static final String EXPENSE_TYPE_ADMINISTRATIVE_COST = "expense administrative cost";
    public static final String EXPENSE_TYPE_SECURITY_COST = "expense security cost";
    public static final String EXPENSE_TYPE_LOGISTIC_COST = "expense logistic cost";
    public static final String EXPENSE_TYPE_TRAVEL_COST = "expense travel cost";
    public static final String EXPENSE_TYPE_LUXURY_TAX_PAID = "luxurytaxpaid";

    public static final String MARKET_SIZE_SMALL = "small";
    public static final String MARKET_SIZE_MEDIUM = "medium";
    public static final String MARKET_SIZE_LARGE = "large";

    public static final double MARKET_SIZE_SMALL_MULTIPLIER = 0.7;
    public static final double MARKET_SIZE_MEDIUM_MULTIPLIER = 1;
    public static final double MARKET_SIZE_LARGE_MULTIPLIER = 1.3;

    public static final double BASE_TEAM_BUDGET = 100;
    public static final double MAX_BUDGET_TEAM = 350;
    public static final double BASE_STADIUM_COSTS = 0.2;
    public static final double BASE_TICKET_PRICE = 45; // pas en millions
    public static final double ATTENDANCE_RATE_BASE = 0.5;
    public static final double BASE_TRAVEL_INTRA_DIVISION_COST = 0.02;
    public static final double BASE_TRAVEL_INTRA_CONFERENCE_COST = 0.05;
    public static final double BASE_TRAVEL_INTER_CONFERENCE_COST = 0.09;

    public static final int MAX_PRESEASON_BIG_TRADE = 3;
    public static final int MAX_TRADE_PER_TEAM = 3;
    public static final LocalDate PRESEASON_TRADE = LocalDate.of(CalendarConfiguration.SEASON_YEAR, 8, 1);
    public static final String SEASON_TRADE_INTENT_BUYER = "buyer";
    public static final String SEASON_TRADE_INTENT_SELLER = "seller";
    public static final String SEASON_TRADE_INTENT_STABLE = "stable";

    public static final double REVENUE_SHARING_REDISTRIBUTION_RATE = 0.25;
    public static final double LEAGUE_OPERATING_RATE = 0.10;

    public static final double INFLATION_RATE = 1.5;
    public static final double CAP_GROWTH_RATE = 0.1;
}

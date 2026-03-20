/*
 * Decompiled with CFR 0.152.
 */
package config;

import java.time.LocalDate;

public class CalendarConfiguration {
    public static final int NUMBER_OF_TEAM = 30;
    public static final int SEASON_YEAR = 2026;
    public static final LocalDate REGULAR_SEASON_DEBUT_DATE = LocalDate.of(2025, 10, 21);
    public static final LocalDate REGULAR_SEASON_END_DATE = LocalDate.of(2026, 4, 12);
    public static final LocalDate PLAYOFF_DEBUT_DATE = LocalDate.of(2026, 4, 18);
    public static final LocalDate PLAYOFF_END_DATE = LocalDate.of(2026, 6, 15);
    public static final LocalDate CHRISTMAS_DAY = LocalDate.of(2025, 12, 25);
    public static final int MAX_GAMES_PER_DAY = 10;

    private CalendarConfiguration() {
    }
}

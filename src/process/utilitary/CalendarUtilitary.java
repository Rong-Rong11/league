/*
 * Decompiled with CFR 0.152.
 */
package process.utilitary;

import config.CalendarConfiguration;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.team.Team;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class CalendarUtilitary {
    public static boolean isWeekend(LocalDate localDate) {
        return localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public static boolean isImportantDay(LocalDate localDate) {
        return CalendarUtilitary.isWeekend(localDate) || localDate.getDayOfWeek() == DayOfWeek.WEDNESDAY;
    }

    public static boolean isSpecialEvent(RegularSeason regularSeason, LocalDate localDate) {
        return localDate.isEqual(CalendarConfiguration.CHRISTMAS_DAY) || localDate.isEqual(regularSeason.getDebutDate()) || localDate.isEqual(regularSeason.getEndDate()) || localDate.isEqual(CalendarUtilitary.getMLKDay());
    }

    public static boolean playedYesterday(Team team, LocalDate localDate) {
        return team.getSchedule().isPlayingOn(localDate.minusDays(1L));
    }

    public static LocalDate getMLKDay() {
        LocalDate localDate = LocalDate.of(2026, Month.JANUARY, 1);
        int n = 0;
        while (localDate.getMonth() == Month.JANUARY) {
            if (localDate.getDayOfWeek() == DayOfWeek.MONDAY && ++n == 3) {
                return localDate;
            }
            localDate = localDate.plusDays(1L);
        }
        return null;
    }

    public static double popularityScoreGame(Game game, LocalDate localDate) {
        double d = 0.0;
        Team team = game.getGameContext().getHomeTeam();
        Team team2 = game.getGameContext().getAwayTeam();
        d += (team.getPopularity() + team2.getPopularity()) * 5.0;
        if (game.getGameContext().isRivalry()) {
            d += 40.0;
        }
        if (team.hasStarPlayer()) {
            d += 30.0;
        }
        if (team2.hasStarPlayer()) {
            d += 30.0;
        }
        switch (game.getGameContext().getTypeGame()) {
            case 2: {
                d += 15.0;
                break;
            }
            case 1: {
                d += 10.0;
                break;
            }
            case 0: {
                d += 5.0;
            }
        }
        int n = team.getSchedule().daysSinceLastGame(localDate);
        int n2 = team2.getSchedule().daysSinceLastGame(localDate);
        if (n < 3) {
            d -= 5.0;
        }
        if (n2 < 3) {
            d -= 5.0;
        }
        if (CalendarUtilitary.playedYesterday(game.getGameContext().getHomeTeam(), localDate) || CalendarUtilitary.playedYesterday(game.getGameContext().getAwayTeam(), localDate)) {
            d -= 100.0;
        }
        return d;
    }

    public static boolean isRivalry(GameContext gameContext) {
        if (gameContext.getHomeTeam().getRival() == null || gameContext.getAwayTeam().getRival() == null) {
            return false;
        }
        String string = gameContext.getHomeTeam().getRival();
        String string2 = gameContext.getAwayTeam().getRival();
        return string.equals(string2);
    }

    public static boolean checkDate(LocalDate localDate, LocalDate localDate2, LocalDate localDate3) {
        return !(!localDate.isEqual(localDate2) && !localDate.isAfter(localDate2) || !localDate.isBefore(localDate3) && !localDate.isEqual(localDate3));
    }
}

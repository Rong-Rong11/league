package process.utilitary;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import config.CalendarConfiguration;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.team.Team;

public class CalendarUtilitary {

    public static boolean isWeekend(LocalDate localDate) {
        return localDate.getDayOfWeek() == DayOfWeek.SATURDAY
                || localDate.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public static boolean isImportantDay(LocalDate localDate) {
        return isWeekend(localDate)
                || localDate.getDayOfWeek() == DayOfWeek.WEDNESDAY;
    }

    public static boolean isSpecialEvent(RegularSeason regularSeason, LocalDate localDate) {
        return localDate.isEqual(CalendarConfiguration.CHRISTMAS_DAY)
                || localDate.isEqual(regularSeason.getDebutDate())
                || localDate.isEqual(regularSeason.getEndDate())
                || localDate.isEqual(getMLKDay());
    }

    public static boolean playedYesterday(Team team, LocalDate localDate) {
        return team.getSchedule().isPlayingOn(localDate.minusDays(1));
    }

    public static LocalDate getMLKDay() {
        LocalDate localDate = LocalDate.of(2026, Month.JANUARY, 1);
        int mondayCount = 0;

        while (localDate.getMonth() == Month.JANUARY) {
            if (localDate.getDayOfWeek() == DayOfWeek.MONDAY) {
                mondayCount++;
                if (mondayCount == 3) {
                    return localDate;
                }
            }
            localDate = localDate.plusDays(1);
        }
        return null;
    }

    public static double popularityScoreGame(Game game, LocalDate localDate) {
        double score = 0.0;

        Team home = game.getGameContext().getHomeTeam();
        Team away = game.getGameContext().getAwayTeam();

        // Popularité des équipes
        score += (home.getPopularity() + away.getPopularity()) * 5.0;

        // Rivalité
        if (game.getGameContext().isRivalry()) {
            score += 40.0;
        }

        // Star players
        if (home.hasStarPlayer()) {
            score += 30.0;
        }
        if (away.hasStarPlayer()) {
            score += 30.0;
        }

        // Type de match
        switch (game.getGameContext().getTypeGame()) {
            case 2:
                score += 15.0;
                break;
            case 1:
                score += 10.0;
                break;
            case 0:
                score += 5.0;
                break;
            default:
                break;
        }

        return score;
    }

    public static boolean isRivalry(GameContext gameContext) {
        if (gameContext.getHomeTeam().getRival() == null
                || gameContext.getAwayTeam().getRival() == null) {
            return false;
        }

        String homeRival = gameContext.getHomeTeam().getRival();
        String awayRival = gameContext.getAwayTeam().getRival();

        return homeRival.equals(awayRival);
    }

    public static boolean checkDate(LocalDate date, LocalDate start, LocalDate end) {
        return (date.isEqual(start) || date.isAfter(start))
                && (date.isEqual(end) || date.isBefore(end));
    }

    public static boolean isImportantMonth(int month) {
        return month == 1 || month == 6 || month == 10;
    }
}

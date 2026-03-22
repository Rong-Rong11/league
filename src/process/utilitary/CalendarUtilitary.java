package process.utilitary;

import config.CalendarConfiguration;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import process.visitor.marketsize.CalculateGamePopularityVisitor;

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
        score += (home.getPopularity() + away.getPopularity()) * 0.2;
        if (game.getGameContext().isRivalry()) {
            score += 40.0;
        }
        if (home.hasStarPlayer()) {
            score += 30.0;
        }
        if (away.hasStarPlayer()) {
            score += 30.0;
        }
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
        }

        MediaMarket homeMedia = home.getTeamFinance().getMediaMarket();
        MediaMarket awayMedia = away.getTeamFinance().getMediaMarket();

        score += homeMedia.getPrestigeModifier() * 20;
        score += homeMedia.getFanBaseModifier() * 10;
        score += awayMedia.getPrestigeModifier() * 20;
        score += awayMedia.getFanBaseModifier() * 10;

        EconomicProfil homeEconomicProfile = home.getTeamFinance().getEconomicProfil();
        EconomicProfil awayEconomicProfile = away.getTeamFinance().getEconomicProfil();

        score += homeEconomicProfile.getFanLoyalty() * 15;
        score += homeEconomicProfile.getHistoricalPrestige() * 10;

        score += awayEconomicProfile.getFanLoyalty() * 15;
        score += awayEconomicProfile.getHistoricalPrestige() * 10;

        MarketSize homeMarket = home.getTeamFinance().getMarketSize();
        MarketSize awayMarket = away.getTeamFinance().getMarketSize();

        score += homeMarket.accept(new CalculateGamePopularityVisitor()) * 10;
        score += awayMarket.accept(new CalculateGamePopularityVisitor()) * 10;

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

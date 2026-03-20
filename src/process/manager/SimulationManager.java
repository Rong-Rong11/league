/*
 * Decompiled with CFR 0.152.
 */
package process.manager;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.league.League;
import data.sport.setup.Game;
import java.time.LocalDate;
import java.time.Month;
import java.util.TreeMap;
import process.manager.LeagueManager;
import process.utilitary.CalendarUtilitary;

public class SimulationManager {
    private LeagueManager leagueManager = new LeagueManager();
    private int month = 1;
    private Month debutMonthDate = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE.getMonth();
    private Month currentMonthDate = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE.getMonth();
    private LocalDate date = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE;

    public void randomFinance() {
        this.leagueManager.randomFinancialProfil();
    }

    public void randomMarketSize() {
        this.leagueManager.randomMarketSize();
    }

    public void startSeason() {
        this.leagueManager.startSeason();
        this.simulateCurrentSeason();
        this.resetCalendarCursor();
    }

    public void nextDay() {
        this.date = this.date.plusDays(1L);
        this.currentMonthDate = this.date.getMonth();
    }

    private void verifyMonth() {
        int n;
        int n2 = this.currentMonthDate.getValue() - this.debutMonthDate.getValue();
        if (n2 < 0) {
            n2 += 12;
        }
        if ((n = n2 + 1) != this.month) {
            this.month = n;
            this.leagueManager.newMonth(this.month);
        }
    }

    private void simulateDay() {
        if (CalendarUtilitary.checkDate(this.date, CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE, CalendarConfiguration.REGULAR_SEASON_END_DATE)) {
            this.leagueManager.simulateRegularSeasonDay(this.date, this.month);
        }
        if (CalendarUtilitary.checkDate(this.date, CalendarConfiguration.PLAYOFF_DEBUT_DATE, CalendarConfiguration.PLAYOFF_END_DATE)) {
            // empty if block
        }
    }

    public void simulateCurrentSeason() {
        block3: {
            block2: {
                if (!CalendarUtilitary.checkDate(this.date, CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE, CalendarConfiguration.REGULAR_SEASON_END_DATE)) break block2;
                while (!this.date.equals(CalendarConfiguration.REGULAR_SEASON_END_DATE)) {
                    this.simulateDay();
                    this.nextDay();
                }
                break block3;
            }
            if (!CalendarUtilitary.checkDate(this.date, CalendarConfiguration.PLAYOFF_DEBUT_DATE, CalendarConfiguration.PLAYOFF_END_DATE)) break block3;
            while (!this.date.equals(CalendarConfiguration.PLAYOFF_END_DATE)) {
                this.simulateDay();
                this.nextDay();
            }
        }
    }

    private void resetCalendarCursor() {
        this.date = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE;
        this.currentMonthDate = this.debutMonthDate = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE.getMonth();
        this.month = 1;
    }

    public void displayGameDay(LocalDate localDate) {
        if (localDate == null) {
            return;
        }
        GameDay gameDay = this.leagueManager.getLeague().getReagularSeason().getCalendar().getCalendar().get(localDate);
        if (gameDay != null) {
            gameDay.setDisplayed(true);
            for (Game game : gameDay.getGames()) {
                game.setDisplayed(true);
            }
        }
    }

    public void displayWeek(LocalDate localDate) {
        if (localDate == null) {
            return;
        }
        for (int i = 0; i < 7; ++i) {
            this.displayGameDay(localDate.plusDays(i));
        }
    }

    public void displayCurrentSeason() {
        TreeMap<LocalDate, GameDay> treeMap = this.leagueManager.getLeague().getReagularSeason().getCalendar().getCalendar();
        for (GameDay gameDay : treeMap.values()) {
            gameDay.setDisplayed(true);
            for (Game game : gameDay.getGames()) {
                game.setDisplayed(true);
            }
        }
    }

    public League getLeague() {
        return this.leagueManager.getLeague();
    }

    public LeagueManager getLeagueManager() {
        return this.leagueManager;
    }

    public LocalDate getCurrentDate() {
        return this.date;
    }
}

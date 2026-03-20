/*
 * Decompiled with CFR 0.152.
 */
package process.manager.financetools;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.marketsize.MarketSize;
import process.utilitary.CalendarUtilitary;
import process.visitor.marketsize.CalculateStadiumCostVisitor;

public class GameExpenseSimulator {
    private GameStat gameStat;

    public GameExpenseSimulator(GameStat gameStat) {
        this.gameStat = gameStat;
    }

    public void calculateGameExpenses(Game game) {
        Team team = game.getGameContext().getHomeTeam();
        double d = this.gameStat.getPopularity();
        int n = this.gameStat.getAttendees();
        this.calculateStadiumCosts(team, n, d);
        this.calculateStaffCosts();
        this.calculateSecurityCosts(n);
        this.calculateLogisticCosts(game);
        this.calculateAwayTravelCost(game);
    }

    private void calculateStadiumCosts(Team team, int n, double d) {
        MarketSize marketSize = team.getTeamFinance().getMarketSize();
        double d2 = marketSize.accept(new CalculateStadiumCostVisitor());
        double d3 = n / 200000;
        d2 *= 1.0 + d3 * 0.25;
        this.gameStat.getHomeFinance().setArenaCosts(d2 *= 1.0 + d * 0.15);
    }

    private void calculateSecurityCosts(int n) {
        double d = 5.0;
        double d2 = n > 15000 ? 1.3 : 1.0;
        double d3 = (double)n * d * d2 / 1000000.0;
        this.gameStat.getHomeFinance().setSecurityCosts(d3);
    }

    private void calculateStaffCosts() {
        double d = 0.15;
        double d2 = 1.0;
        if (this.gameStat.getAttendanceRate() > 0.9) {
            d2 = 1.2;
        }
        if (this.gameStat.getAttendanceRate() < 0.4) {
            d2 = 0.9;
        }
        double d3 = d * d2;
        this.gameStat.getHomeFinance().setStaffCosts(d3);
    }

    private void calculateAwayTravelCost(Game game) {
        double d = 0.0;
        int n = game.getGameContext().getTypeGame();
        d = n == 2 ? 0.02 : (n == 1 ? 0.05 : 0.09);
        this.gameStat.getAwayFinance().setTravelCosts(d);
    }

    private void calculateLogisticCosts(Game game) {
        double d = 0.05;
        double d2 = 0.04;
        double d3 = 0.03;
        double d4 = CalendarUtilitary.isRivalry(game.getGameContext()) ? 1.15 : 1.0;
        double d5 = (d + d2 + d3) * d4;
        this.gameStat.getHomeFinance().setLogisticsCosts(d5);
    }
}

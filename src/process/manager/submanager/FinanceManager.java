/*
 * Decompiled with CFR 0.152.
 */
package process.manager.submanager;

import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import java.time.LocalDate;
import process.manager.financetools.CentralRevenueDistributor;
import process.manager.financetools.GameFinanceProcessor;
import process.manager.financetools.MonthlyTeamFinanceCalculator;
import process.manager.financetools.RevenueSharingManager;
import process.repositery.TeamRepositery;

public class FinanceManager {
    private TeamRepositery teamRepositery = TeamRepositery.getInstance();
    private RevenueSharingManager revenueSharingManager;
    private MonthlyTeamFinanceCalculator monthlyTeamFinanceCalculator;
    private CentralRevenueDistributor centralRevenueDistributor;
    private GameFinanceProcessor gameFinanceProcessor;

    public FinanceManager(League league) {
        this.revenueSharingManager = new RevenueSharingManager(league);
        this.monthlyTeamFinanceCalculator = new MonthlyTeamFinanceCalculator();
        this.centralRevenueDistributor = new CentralRevenueDistributor(league);
        this.gameFinanceProcessor = new GameFinanceProcessor();
    }

    public void applyMonthlyFinance(int n) {
        this.applyMonthlyFinanceForAllTeams(n);
        this.distributeMonthlyCentralRevenue(n);
        this.applyRevenueSharing(n);
    }

    private void distributeMonthlyCentralRevenue(int n) {
        this.centralRevenueDistributor.distributeMonthlyCentralRevenue(n);
    }

    private void applyRevenueSharing(int n) {
        this.revenueSharingManager.applyRevenueSharing(n);
    }

    public void calculateGame(Game game, LocalDate localDate, int n) {
        this.gameFinanceProcessor.calculateGame(game, localDate, n);
    }

    private void applyMonthlyFinanceForTeam(Team team, int n) {
        this.monthlyTeamFinanceCalculator.applyMonthlyFinance(team, n);
    }

    private void applyMonthlyFinanceForAllTeams(int n) {
        for (Team team : this.teamRepositery.getAllTeams()) {
            this.applyMonthlyFinanceForTeam(team, n);
        }
    }

    public GameStat getGameStat(Game game) {
        return this.gameFinanceProcessor.getGameStat(game);
    }
}

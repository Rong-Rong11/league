package process.service.submanager;

import java.time.LocalDate;

import data.finance.GameStat;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.marketsize.MarketSize;
import process.repositery.TeamRepositery;
import process.service.financetools.CentralRevenueDistributor;
import process.service.financetools.FinanceInitializer;
import process.service.financetools.GameFinanceProcessor;
import process.service.financetools.LeagueExpenseCalculator;
import process.service.financetools.MonthlyTeamFinanceCalculator;
import process.service.financetools.RevenueSharingManager;
import process.utilitary.TeamUtilitary;
import process.visitor.financialprofil.ChooseTransferStrategyVisitor;

public class FinanceManager {
    private TeamRepositery teamRepositery = TeamRepositery.getInstance();
    private FinanceInitializer financeInitializer = new FinanceInitializer();
    private RevenueSharingManager revenueSharingManager;
    private MonthlyTeamFinanceCalculator monthlyTeamFinanceCalculator;
    private CentralRevenueDistributor centralRevenueDistributor;
    private GameFinanceProcessor gameFinanceProcessor;
    private LeagueExpenseCalculator leagueExpenseCalculator;

    public FinanceManager(League league) {
        revenueSharingManager = new RevenueSharingManager(league);
        monthlyTeamFinanceCalculator = new MonthlyTeamFinanceCalculator();
        centralRevenueDistributor = new CentralRevenueDistributor(league);
        gameFinanceProcessor = new GameFinanceProcessor();
        leagueExpenseCalculator = new LeagueExpenseCalculator(league);
    }

    public void initializeFinance() {
        financeInitializer.initializeFinance();
    }

    public void applyMonthlyFinance(int month) {
        applyMonthlyFinanceForAllTeams(month);
        distributeMonthlyCentralRevenue(month);
        applyLeagueMonthlyExpenses(month);
        applyRevenueSharing(month);
    }

    private void distributeMonthlyCentralRevenue(int month) {
        centralRevenueDistributor.distributeMonthlyCentralRevenue(month);
    }

    private void applyRevenueSharing(int month) {
        revenueSharingManager.applyRevenueSharing(month);
    }

    private void applyLeagueMonthlyExpenses(int month) {
        leagueExpenseCalculator.applyMonthlyExpenses(month);
    }

    public void calculateGame(Game game, LocalDate date, int month) {
        gameFinanceProcessor.calculateGame(game, date, month);
    }

    private void applyMonthlyFinanceForTeam(Team team, int month) {
        monthlyTeamFinanceCalculator.applyMonthlyFinance(team, month);
    }

    private void applyMonthlyFinanceForAllTeams(int month) {
        for (Team team : teamRepositery.getAllTeams()) {
            applyMonthlyFinanceForTeam(team, month);
        }
    }

    public GameStat getGameStat(Game game) {
        return gameFinanceProcessor.getGameStat(game);
    }

    public void randomFinancialPolicy() {
        for (Team team : TeamRepositery.getInstance().getAllTeams()) {
            FinancialPolicy financialProfil = TeamUtilitary.randomFinancialProfil();
            chooseFinancialPolicy(team, financialProfil);
        }
    }

    public void chooseFinancialPolicy(Team team, FinancialPolicy financialProfil) {
        team.getTeamFinance().setFinancialProfil(financialProfil);
        team.getTeamFinance()
                .setTeamTransferStrategy(financialProfil.accept(new ChooseTransferStrategyVisitor(team.getRival())));
    }

    public void chooseMarketSize(Team team, MarketSize marketSize) {
        team.getTeamFinance().setMarketSize(marketSize);
    }

    public void randomMarketSize() {
        for (Team team : TeamRepositery.getInstance().getAllTeams()) {
            MarketSize marketSize = TeamUtilitary.randomMarketSize();
            chooseMarketSize(team, marketSize);
        }
    }

}

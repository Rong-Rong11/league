package process.service.finance;

import java.time.LocalDate;

import data.finance.GameStat;
import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.marketsize.MarketSize;
import process.repositery.TeamRepositery;
import process.service.finance.tools.CentralRevenueDistributor;
import process.service.finance.tools.FinanceInitializer;
import process.service.finance.tools.LeagueExpenseCalculator;
import process.service.finance.tools.MonthlyTeamFinanceCalculator;
import process.service.finance.tools.game.processor.PlayoffGameFinanceProcessor;
import process.service.finance.tools.game.processor.RegularSeasonGameFinanceProcessor;
import process.utility.TeamUtilitary;
import process.visitor.financialprofil.ChooseTransferStrategyVisitor;

public class FinanceManager {
    private TeamRepositery teamRepositery = TeamRepositery.getInstance();
    private FinanceInitializer financeInitializer = new FinanceInitializer();
    private RevenueSharingManager revenueSharingManager;
    private MonthlyTeamFinanceCalculator monthlyTeamFinanceCalculator;
    private CentralRevenueDistributor centralRevenueDistributor;
    private LeagueExpenseCalculator leagueExpenseCalculator;

    private RegularSeasonGameFinanceProcessor regularSeasonGameFinanceProcessor;
    private PlayoffGameFinanceProcessor playoffGameFinanceProcessor;

    public FinanceManager(League league) {
        revenueSharingManager = new RevenueSharingManager(league);
        monthlyTeamFinanceCalculator = new MonthlyTeamFinanceCalculator();
        centralRevenueDistributor = new CentralRevenueDistributor(league);
        leagueExpenseCalculator = new LeagueExpenseCalculator(league);

        regularSeasonGameFinanceProcessor = new RegularSeasonGameFinanceProcessor();
    }

    public void initializeFinance() {
        financeInitializer.initializeFinance();
    }

    public void applyMonthlyFinance(int month) {
        applyMonthlyFinanceForAllTeams(month);
        distributeMonthlyCentralRevenue(month);
        applyLeagueMonthlyExpenses(month);

        // changer que ça pour les playoffs
        applyRevenueSharing(month);
    }

    public void applyPlayoffMonthlyFinance(int month) {
        applyMonthlyFinanceForAllTeams(month);
        distributeMonthlyCentralRevenue(month);
        applyLeagueMonthlyExpenses(month);
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

    public void calculateRegularSeasonGame(Game game, LocalDate date, int month) {
        regularSeasonGameFinanceProcessor.calculateGame(game, date, month);
    }

    public void calculatePlayoffGame(Game game, LocalDate date, int month, PlayoffRound round) {
        if (playoffGameFinanceProcessor == null || playoffGameFinanceProcessor.getRound() != round) {
            playoffGameFinanceProcessor = new PlayoffGameFinanceProcessor(round);
        }

        playoffGameFinanceProcessor.calculateGame(game, date, month);
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
        GameStat gameStat = regularSeasonGameFinanceProcessor.getGameStat(game);

        if (gameStat != null) {
            return gameStat;
        }

        if (playoffGameFinanceProcessor != null) {
            return playoffGameFinanceProcessor.getGameStat(game);
        }

        return null;
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

    public double getTeamCurrentPayroll(Team team) {
        return team.getTeamFinance().getCurrentPayroll();
    }
}

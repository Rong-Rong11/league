package process.manager;

import config.CalendarConfiguration;
import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.financialprofil.FinancialProfil;
import data.team.finance.marketsize.MarketSize;
import java.time.LocalDate;
import java.time.Month;
import process.builder.CalendarBuilder;
import process.builder.LeagueBuilder;
import process.builder.SimulationBuilder;
import process.manager.submanager.FinanceManager;
import process.manager.submanager.GameManager;
import process.manager.submanager.TradeManager;
import process.repositery.TeamRepositery;
import process.utilitary.FinanceUtilitary;
import process.utilitary.TeamUtilitary;
import process.visitor.financialprofil.ChooseTransferStrategyVisitor;

public class LeagueManager {
    private League league;
    private LeagueBuilder leagueBuilder = new LeagueBuilder();
    private CalendarBuilder calendarBuilder;
    private SimulationBuilder simulationBuilder = new SimulationBuilder(); 
    private GameManager gameManager = null;
    private TradeManager tradeManager;
    private FinanceManager financeManager;

    public LeagueManager() {
		league = leagueBuilder.build();
		FinanceUtilitary.updateLeaguePayroll();
		
		calendarBuilder = new CalendarBuilder(league) ; 
		financeManager = new FinanceManager(league);
		gameManager = new GameManager(league, financeManager);
		tradeManager = new TradeManager(league.getLeagueFinance().getSalaryCap());

	}

    public void startSeason() {
        simulationBuilder.build();
        simulatePreSeasonTrade();
		buildRegularSeasonCalendar();
		league.getLeagueFinance().getBudget().getInitialAmount();
    }

    private void simulatePreSeasonTrade() {
        tradeManager.simulatePreSeasonTrade();
    }

    private void buildRegularSeasonCalendar() {
        calendarBuilder.buildRegulaSeasonCalendar();
    }

    public boolean simulateRegularSeasonDay(LocalDate date, int month) {
        return gameManager.simulateRegularSeasonDay(date, month);
    }

    public void newMonth(int month) {
        financeManager.applyMonthlyFinance(month);
    }

    public void randomFinancialProfil() {
        for (Team team : TeamRepositery.getInstance().getAllTeams()) {
            FinancialProfil financialProfil = TeamUtilitary.randomFinancialProfil();
            chooseFinancialProfil(team, financialProfil);
        }
    }

    public void chooseFinancialProfil(Team team, FinancialProfil financialProfil) {
        team.getTeamFinance().setFinancialProfil(financialProfil);
        team.getTeamFinance().setTeamTransferStrategy(financialProfil.accept(new ChooseTransferStrategyVisitor(team.getRival())));
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

    public League getLeague() {
        return league;
    }

    public FinanceManager getFinanceManager() {
        return financeManager;
    }

    public boolean simulateGameDay(LocalDate date, int month) {
        return gameManager.simulateGameDay(date, month);
    }

    public boolean simulateGame(Game game, LocalDate date) {
        return gameManager.simulateGame(game, date, this.computeMonth(date));
    }

    private int computeMonth(LocalDate date) {
        Month debutMonth = CalendarConfiguration.REGULAR_SEASON_DEBUT_DATE.getMonth();
        Month currentMonth = date.getMonth();
        int monthsBetween = currentMonth.getValue() - debutMonth.getValue();
        if (monthsBetween < 0) {
            monthsBetween += 12;
        }
        return monthsBetween + 1;
    }
}

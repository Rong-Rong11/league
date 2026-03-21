package process.manager.submanager;

import java.time.LocalDate;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.league.League;
import data.league.Ranking;
import data.league.RegularSeason;
import data.sport.setup.Game;
import process.simulator.GameSimulator;

public class GameManager {

    private League league;
    private GameSimulator gameSimulator = new GameSimulator();
    private FinanceManager financeManager;
    private RegularSeasonRankingManager regularSeasonRankingManager = new RegularSeasonRankingManager();

    public GameManager(League league, FinanceManager financeManager) {
        this.league = league;
        this.financeManager = financeManager;
    }

    public boolean simulateGameDay(LocalDate date, int month) {
        RegularSeason regularSeason = league.getReagularSeason();
        TreeMap<LocalDate, GameDay> regularSeasonCalendar = regularSeason.getCalendar().getCalendar();
        return simulateRegularSeasonDay(date, month);
    }

    public boolean simulateRegularSeasonDay(LocalDate date, int month) {
        RegularSeason regularSeason = league.getReagularSeason();
        TreeMap<LocalDate, GameDay> regularSeasonCalendar = regularSeason.getCalendar().getCalendar();
        Ranking ranking = regularSeason.getRanking();
        GameDay gameDay = regularSeasonCalendar.get(date);
        if (gameDay != null && !gameDay.isSimulated()) {
            simulateGameDay(gameDay, date, month);
            regularSeasonRankingManager.addSimulatedGameDay(gameDay);
            regularSeasonRankingManager.updateRanking(league, ranking, regularSeasonCalendar, date);
            return true;
        }
        return false;

    }

    private void simulateGameDay(GameDay gameDay, LocalDate date, int month) {
        for (Game game : gameDay.getGames()) {
            gameSimulator.simulateGame(game);
            financeManager.calculateGame(game, date, month);
        }
        gameDay.setSimulated(true);

    }

    public boolean simulateGame(Game game, LocalDate date, int month) {
        if (game == null) {
            return false;
        }
        gameSimulator.simulateGame(game);
        financeManager.calculateGame(game, date, month);
        return true;
    }

    public void setLeague(League league) {
        this.league = league;
    }

}

package process.manager.submanager;

import data.calendar.GameDay;
import data.league.League;
import data.league.Playoff;
import data.league.Ranking;
import data.league.RegularSeason;
import data.sport.setup.Game;
import java.time.LocalDate;
import java.util.TreeMap;
import process.manager.submanager.FinanceManager;
import process.manager.submanager.RegularSeasonRankingManager;
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

    public boolean simulateRegularSeasonDay(LocalDate localDate, int n) {
        RegularSeason regularSeason = this.league.getReagularSeason();
        Playoff playoff = this.league.getPlayoff();
        TreeMap<LocalDate, GameDay> treeMap = regularSeason.getCalendar().getCalendar();
        Ranking ranking = regularSeason.getRanking();
        if (this.simulateGameDay(treeMap, localDate, n)) {
            regularSeasonRankingManager.updateRanking(ranking);
            return true;
        }
        return false;
    }

    private boolean simulateGameDay(TreeMap<LocalDate, GameDay> treeMap, LocalDate localDate, int n) {
        GameDay gameDay = treeMap.get(localDate);
        if (gameDay != null && !gameDay.isSimulated()) {
            for (Game game : gameDay.getGames()) {
                this.gameSimulator.simulateGame(game);
                this.financeManager.calculateGame(game, localDate, n);
            }
            gameDay.setSimulated(true);
            return true;
        }
        return false;
    }

    public boolean simulateGameDay(LocalDate localDate, int n) {
        RegularSeason regularSeason = this.league.getReagularSeason();
        TreeMap<LocalDate, GameDay> treeMap = regularSeason.getCalendar().getCalendar();
        return this.simulateGameDay(treeMap, localDate, n);
    }

    public boolean simulateGame(Game game, LocalDate localDate, int n) {
        if (game == null) {
            return false;
        }
        this.gameSimulator.simulateGame(game);
        this.financeManager.calculateGame(game, localDate, n);
        return true;
    }

    public void setLeague(League league) {
        this.league = league;
    }
}

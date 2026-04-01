package process.service.submanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.league.League;
import data.league.Playoff;
import data.league.Ranking;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.team.Team;
import process.builder.CalendarBuilder;
import process.builder.FirstRoundCalendarBuilder;
import process.builder.PlayoffBuilder;
import process.simulator.GameSimulator;
import process.utilitary.LeagueUtility;

public class GameManager {

    private League league;
    private GameSimulator gameSimulator = new GameSimulator();
    private FinanceManager financeManager;
    private RegularSeasonRankingManager regularSeasonRankingManager;
    private FirstRoundPlayoffManager firstRoundPlayoffManager;

    public GameManager(League league, FinanceManager financeManager, CalendarBuilder calendarBuilder,
            PlayoffBuilder playoffBuilder, FirstRoundCalendarBuilder firstRoundCalendarBuilder) {
        this.league = league;
        ArrayList<Team> eastTeams = new ArrayList<>();
        ArrayList<Team> westTeams = new ArrayList<>();
        LeagueUtility.getConferenceTeams(league, eastTeams, westTeams);
        regularSeasonRankingManager = new RegularSeasonRankingManager(westTeams, eastTeams);
        this.financeManager = financeManager;
        this.firstRoundPlayoffManager = new FirstRoundPlayoffManager(league,
                firstRoundCalendarBuilder,
                playoffBuilder);
    }

    public boolean simulateRegularSeasonDay(LocalDate date, int month) {
        RegularSeason regularSeason = league.getReagularSeason();
        TreeMap<LocalDate, GameDay> regularSeasonCalendar = regularSeason.getNbaCalendar().getCalendar();
        Ranking ranking = regularSeason.getRanking();
        GameDay gameDay = regularSeasonCalendar.get(date);
        if (gameDay != null && !gameDay.isSimulated()) {
            simulateGameDay(gameDay, date, month);
            regularSeasonRankingManager.addSimulatedGameDay(gameDay);
            regularSeason.setRanking(
                    regularSeasonRankingManager.updateRanking(league, ranking, regularSeasonCalendar, date));
            return true;
        }
        return false;

    }

    public void simulateFirstRoundDay(LocalDate date, int month) {
        Playoff playoff = league.getPlayoff();
        TreeMap<LocalDate, GameDay> playoffCalendar = playoff.getNbaCalendar().getCalendar();
        GameDay gameDay = playoffCalendar.get(date);
        if (gameDay != null && !gameDay.isSimulated()) {
            simulateGameDay(gameDay, date, month);
            for (Game game : gameDay.getGames()) {
                firstRoundPlayoffManager.handlePlayedGame(game, date);
            }
        }
    }

    private void simulateGameDay(GameDay gameDay, LocalDate date, int month) {
        for (Game game : gameDay.getGames()) {
            gameSimulator.simulateGame(game);
            financeManager.calculateGame(game, date, month);
        }
        gameDay.setSimulated(true);

    }
}

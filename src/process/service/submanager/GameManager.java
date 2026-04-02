package process.service.submanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import config.CalendarConfiguration;
import data.calendar.GameDay;
import data.league.League;
import data.league.Playoff;
import data.league.Ranking;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.team.Team;
import process.builder.calendar.CalendarBuilder;
import process.builder.calendar.ConferenceFinalCalendarBuilder;
import process.builder.calendar.FirstRoundCalendarBuilder;
import process.builder.calendar.NbaFinalCalendarBuilder;
import process.builder.calendar.SemiCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.playoff.ConferenceFinalPlayoffManager;
import process.service.playoff.FirstRoundPlayoffManager;
import process.service.playoff.NbaFinalPlayoffManager;
import process.service.playoff.PlayoffManager;
import process.service.playoff.SemiPlayoffManager;
import process.simulator.GameSimulator;
import process.utility.LeagueUtility;

public class GameManager {

    private League league;
    private GameSimulator gameSimulator = new GameSimulator();
    private FinanceManager financeManager;
    private RegularSeasonRankingManager regularSeasonRankingManager;
    private FirstRoundPlayoffManager firstRoundPlayoffManager;
    private SemiPlayoffManager semiPlayoffManager;
    private ConferenceFinalPlayoffManager conferenceFinalPlayoffManager;
    private NbaFinalPlayoffManager nbaFinalPlayoffManager;

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
        this.semiPlayoffManager = new SemiPlayoffManager(league,
                new SemiCalendarBuilder(league, CalendarConfiguration.PLAYOFF_DEBUT_DATE),
                playoffBuilder);
        this.conferenceFinalPlayoffManager = new ConferenceFinalPlayoffManager(league,
                new ConferenceFinalCalendarBuilder(league, CalendarConfiguration.PLAYOFF_DEBUT_DATE),
                playoffBuilder);
        this.nbaFinalPlayoffManager = new NbaFinalPlayoffManager(league,
                new NbaFinalCalendarBuilder(league, CalendarConfiguration.PLAYOFF_DEBUT_DATE),
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
        simulatePlayoffDay(date, month, firstRoundPlayoffManager);
    }

    public void simulateSemiRoundDay(LocalDate date, int month) {
        simulatePlayoffDay(date, month, semiPlayoffManager);
    }

    public void simulateConferenceFinalRoundDay(LocalDate date, int month) {
        simulatePlayoffDay(date, month, conferenceFinalPlayoffManager);
    }

    public void simulateNbaFinalRoundDay(LocalDate date, int month) {
        simulatePlayoffDay(date, month, nbaFinalPlayoffManager);
    }

    private void simulatePlayoffDay(LocalDate date, int month, PlayoffManager playoffManager) {
        Playoff playoff = league.getPlayoff();
        TreeMap<LocalDate, GameDay> playoffCalendar = playoff.getNbaCalendar().getCalendar();
        GameDay gameDay = playoffCalendar.get(date);
        if (gameDay != null && !gameDay.isSimulated()) {
            simulateGameDay(gameDay, date, month);
            for (Game game : gameDay.getGames()) {
                playoffManager.handlePlayedGame(game, date);
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

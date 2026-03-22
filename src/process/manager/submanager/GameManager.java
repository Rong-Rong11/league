package process.manager.submanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.league.Conference;
import data.league.League;
import data.league.Ranking;
import data.league.RegularSeason;
import data.sport.setup.Game;
import data.team.Team;
import process.repositery.TeamRepositery;
import process.simulator.GameSimulator;
import process.utilitary.TeamUtilitary;

public class GameManager {

    private League league;
    private GameSimulator gameSimulator = new GameSimulator();
    private FinanceManager financeManager;
    private RegularSeasonRankingManager regularSeasonRankingManager;
    private TeamRepositery teamRepositery = TeamRepositery.getInstance();

    public GameManager(League league, FinanceManager financeManager) {
        this.league = league;
        ArrayList<Team> eastTeams = new ArrayList<>();
        ArrayList<Team> westTeams = new ArrayList<>();
        getConferenceTeams(eastTeams, westTeams);
        regularSeasonRankingManager = new RegularSeasonRankingManager(westTeams, eastTeams);

        this.financeManager = financeManager;
    }

    private void getConferenceTeams(ArrayList<Team> eastTeams, ArrayList<Team> westTeams) {
        Conference easternConference = league.getEasternConference();
        for (Team team : teamRepositery.getAllTeams()) {
            if (TeamUtilitary.getConferenceOfTeam(league, team).equals(league.getEasternConference())) {
                eastTeams.add(team);
            } else {
                westTeams.add(team);
            }
        }

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

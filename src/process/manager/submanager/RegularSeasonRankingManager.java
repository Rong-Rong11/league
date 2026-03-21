package process.manager.submanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import data.calendar.GameDay;
import data.league.League;
import data.league.Ranking;
import data.sport.setup.Game;
import data.team.Team;
import process.manager.submanager.rankingtools.NbaRegularSeasonTeamComparator;
import process.repositery.TeamRepositery;

public class RegularSeasonRankingManager {
    private TeamRepositery teamRepositery = TeamRepositery.getInstance();
    private ArrayList<GameDay> simulatedGameDay = new ArrayList<>();

    public RegularSeasonRankingManager() {

    }

    public void updateRanking(League league, Ranking ranking, TreeMap<LocalDate, GameDay> regularSeasonCalendar,
            LocalDate date) {
        TreeMap<Integer, Team> newRanking = new TreeMap<Integer, Team>();
        ArrayList<Team> teams = new ArrayList<Team>(teamRepositery.getAllTeams());
        Collections.sort(teams, new NbaRegularSeasonTeamComparator(getSimulatedGames(), league));

        int rank = 1;
        for (Team team : teams) {
            newRanking.put(rank, team);
            rank++;
        }
        ranking.setRanking(newRanking);
    }

    public void addSimulatedGameDay(GameDay gameDay) {
        simulatedGameDay.add(gameDay);
    }

    private ArrayList<Game> getSimulatedGames() {
        ArrayList<Game> games = new ArrayList<>();
        for (GameDay gameDay : simulatedGameDay) {
            games.addAll(gameDay.getGames());
        }
        return games;
    }
}

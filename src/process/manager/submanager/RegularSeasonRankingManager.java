package process.manager.submanager;

import data.calendar.GameDay;
import data.league.League;
import data.league.Ranking;
import data.sport.setup.Game;
import data.team.Team;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import process.manager.rankingtools.NbaRegularSeasonTeamComparator;
import process.repositery.TeamRepositery;

public class RegularSeasonRankingManager {
    private TeamRepositery teamRepositery = TeamRepositery.getInstance();
    private ArrayList<GameDay> simulatedGameDay = new ArrayList<>();
    private ArrayList<Team> westTeams;
    private ArrayList<Team> eastTeams;

    public RegularSeasonRankingManager(ArrayList<Team> westTeams, ArrayList<Team> eastTeams) {
        this.westTeams = westTeams;
        this.eastTeams = eastTeams;
    }

    public Ranking updateRanking(League league, Ranking ranking, TreeMap<LocalDate, GameDay> regularSeasonCalendar,
            LocalDate date) {
        TreeMap<Integer, Team> newEastRanking = new TreeMap<Integer, Team>();
        TreeMap<Integer, Team> newWestRanking = new TreeMap<Integer, Team>();

        Collections.sort(westTeams, new NbaRegularSeasonTeamComparator(getSimulatedGames(), league));
        Collections.sort(eastTeams, new NbaRegularSeasonTeamComparator(getSimulatedGames(), league));

        createNewRanking(newWestRanking, westTeams);
        createNewRanking(newEastRanking, eastTeams);
        ranking.setWestRanking(newWestRanking);
        ranking.setEastRanking(newEastRanking);
        return ranking;
    }

    private void createNewRanking(TreeMap<Integer, Team> newRanking, ArrayList<Team> sortedTeams) {
        int rank = 1;
        for (Team team : sortedTeams) {
            newRanking.put(rank, team);
            rank++;
        }
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

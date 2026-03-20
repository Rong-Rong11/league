package process.manager.submanager;

import data.league.Ranking;
import data.team.Team;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.TreeMap;
import process.repositery.TeamRepositery;

public class RegularSeasonRankingManager {
    private TeamRepositery teamRepositery = TeamRepositery.getInstance();

    public void updateRanking(Ranking ranking) {
        TreeMap<Integer, Team> treeMap = new TreeMap<Integer, Team>();
        ArrayList<Team> arrayList = new ArrayList<Team>(this.teamRepositery.getAllTeams());
        Collections.sort(arrayList, new Comparator<Team>(){
            @Override
            public int compare(Team team, Team team2) {
                double d = team.getTeamPerformance().getNumberWin() / team.getTeamPerformance().getNumberPlayedGames();
                double d2 = team2.getTeamPerformance().getNumberWin() / team2.getTeamPerformance().getNumberPlayedGames();
                return Double.compare(d2, d);
            }
        });
        int n = 1;
        for (Team team : arrayList) {
            treeMap.put(n, team);
            n++;
        }
        ranking.setRanking(treeMap);
    }
}

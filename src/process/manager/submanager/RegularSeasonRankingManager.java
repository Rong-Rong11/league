package process.manager.submanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

import data.league.Ranking;
import data.team.Team;
import process.repositery.TeamRepositery;

public class RegularSeasonRankingManager {
    private TeamRepositery teamRepositery = TeamRepositery.getInstance();

    public void updateRanking(Ranking ranking) {
        TreeMap<Integer, Team> treeMap = new TreeMap<Integer, Team>();
        ArrayList<Team> arrayList = new ArrayList<Team>(this.teamRepositery.getAllTeams());
        Collections.sort(arrayList, new Comparator<Team>() {
            @Override
            public int compare(Team team1, Team team2) {
                int games1 = team1.getTeamPerformance().getNumberPlayedGames();
                int games2 = team2.getTeamPerformance().getNumberPlayedGames();

                double pct1 = games1 == 0 ? 0.0 : (double) team1.getTeamPerformance().getNumberWin() / games1;

                double pct2 = games2 == 0 ? 0.0 : (double) team2.getTeamPerformance().getNumberWin() / games2;

                return Double.compare(pct2, pct1);
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

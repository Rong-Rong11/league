package process.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

import data.league.Ranking;
import data.team.Team;
import process.repositery.TeamRepositery;

public class RegularSeasonRankingManager {
	private TeamRepositery teamRepositery = TeamRepositery.getInstance();

	public RegularSeasonRankingManager() {

	}

	public void updateRanking(Ranking ranking) {
		TreeMap<Integer, Team> newRanking = new TreeMap<Integer, Team>();
		ArrayList<Team> teams = new ArrayList<Team>(teamRepositery.getAllTeams());
		Collections.sort(teams, new Comparator<Team>() {
			public int compare(Team teamA, Team teamB) {
				double scoreA = (double) (teamA.getTeamPerformance().getNumberWin() /
						teamA.getTeamPerformance().getNumberPlayedGames());
				double scoreB = (double) (teamB.getTeamPerformance().getNumberWin() /
						teamB.getTeamPerformance().getNumberPlayedGames());
				return Double.compare(scoreB, scoreA);
			}
		});
		int rank = 1;
		for (Team team : teams) {
			newRanking.put(rank, team);
			rank++;
		}
		ranking.setRanking(newRanking);
	}
}

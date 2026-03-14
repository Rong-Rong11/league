package data.league;

import java.util.TreeMap;

import data.team.Team;

public class Ranking {
	
	public TreeMap<Integer, Team> ranking = new TreeMap<Integer, Team>(); 
	
	public Ranking() {
		
	}

	public TreeMap<Integer, Team> getRanking() {
		return ranking;
	}

	public void setRanking(TreeMap<Integer, Team> ranking) {
		this.ranking = ranking;
	}
	
	
}

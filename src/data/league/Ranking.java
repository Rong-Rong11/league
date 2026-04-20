package data.league;

import java.util.TreeMap;

import data.team.Team;

public class Ranking {
	public TreeMap<Integer, Team> eastRanking = new TreeMap<>();
	public TreeMap<Integer, Team> westRanking = new TreeMap<>();

	public Ranking() {
	}

	public TreeMap<Integer, Team> getEastRanking() {
		return eastRanking;
	}

	public void setEastRanking(TreeMap<Integer, Team> eastRanking) {
		this.eastRanking = eastRanking;
	}

	public TreeMap<Integer, Team> getWestRanking() {
		return westRanking;
	}

	public void setWestRanking(TreeMap<Integer, Team> westRanking) {
		this.westRanking = westRanking;
	}

}

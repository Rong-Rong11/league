package data.league;

import java.util.TreeMap;

import data.team.Team;



public class Ranking {
	
	public TreeMap<Integer, Team> ranking ; 
	
	public Ranking() {
		ranking = new TreeMap<Integer, Team>() ; 
	}
}

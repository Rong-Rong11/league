package data.team;

import java.util.HashMap;

import config.SimulationConfiguration;
import data.player.Player;
import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.team.calendar.Schedule;
import data.team.finance.FinancialProfil;
import data.team.finance.TeamFinance;

public class Team {
	private String name ; 
	private String rival ; 
	private double popularity ;
	
	private HashMap<String, Player> players = new HashMap<String, Player>() ;  
	private Schedule schedule ;
	private Player starPlayer ; 
	
	private TeamFinance teamFinance ; 
	
	
	
	
	
	public Team(String name, String rival, double popularity, TeamFinance teamFinance) {
		this.name = name;
		this.rival = rival;
		schedule = new Schedule() ; 
		this.popularity = popularity ; 
		schedule = new Schedule() ; 
		starPlayer = null ; 
		this.teamFinance = teamFinance ; 
		
	}
	
	public String getName() {
		return name;
	}
	public void setNom(String name) {
		this.name = name;
	}
	
	public String getRival() {
		return rival;
	}
	public void setRival(String rival) {
		this.rival = rival;
	}
	public double getPopularity() {
		return popularity;
	}
	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}
	public HashMap<String, Player> getPlayers() {
		return players;
	}
	public void setPlayers(HashMap<String, Player> players) {
		this.players = players;
	}
	
	public void addPlayer(Player player) {
		players.put(player.getName(), player) ; 
	}
	public Schedule getSchedule() {
		return schedule;
	}
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	} 
	

	
	public boolean hasStarPlayer () {
		return starPlayer == null ; 
	}
	
	public void addGame(Game game) {
		schedule.addGame(game) ; 
	}
	//à enlever car pas une méthode de donnée
	public void setStarPlayer() {
		for(Player player : players.values()) {
			if (player.isStar()) {
				starPlayer = player ; 
				return ; 
			}
		}
		starPlayer = null ; 
	}

	@Override
	public String toString() {
		String s = "Team [name=" + name + ", "
				+ // "financialProfil=" + financialProfil +
				", rival=" + rival + ", popularity=" + popularity ;
		for(Player player : players.values()) {
			s+= "\n" + player.toString() ; 
		}
		s += "\n " + "schedule=" + schedule; 
		if(starPlayer != null) {
			s+= "starPLayer =" + starPlayer.toString() ; 
		}
		return s  ; 
		
	}

	public Player getStarPlayer() {
		return starPlayer;
	}

	public void setStarPlayer(Player starPlayer) {
		this.starPlayer = starPlayer;
	}

	public TeamFinance getTeamFinance() {
		return teamFinance;
	}
	
	
	
	
	
	
	
	
}

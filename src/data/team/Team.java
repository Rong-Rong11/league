package data.team;

import java.util.HashMap;

import config.SimulationConfiguration;
import data.player.Player;
import data.sport.setup.Game;
import data.sport.setup.GameContext;
import data.team.calendar.Schedule;
<<<<<<< HEAD
import data.team.finance.FinancialProfil;
import data.team.finance.TeamFinance;
=======
import data.team.finance.TeamFinance;
import data.team.finance.financialprofil.FinancialProfil;
import data.team.finance.transfer.TeamTransferStrategy;
>>>>>>> Fatima2

public class Team {
	private String name ; 
	private String rival ; 
	private double popularity ;
<<<<<<< HEAD
	
	private HashMap<String, Player> players = new HashMap<String, Player>() ;  
	private Schedule schedule ;
	private Player starPlayer ; 
	
	private TeamFinance teamFinance ; 
	
	
	
	
	
	public Team(String name, String rival, double popularity, TeamFinance teamFinance) {
=======
	private TeamPerformance teamPerformance = new TeamPerformance() ; 
	
	private HashMap<String, Player> players = new HashMap<String, Player>() ;  
	private Schedule schedule ;
	private Player starPlayer = null ; 
	
	private TeamFinance teamFinance ; 
	private Stadium stadium ; 

	
	public Team(String name, String rival, double popularity, TeamFinance teamFinance, Stadium stadium) {
>>>>>>> Fatima2
		this.name = name;
		this.rival = rival;
		schedule = new Schedule() ; 
		this.popularity = popularity ; 
		schedule = new Schedule() ; 
		starPlayer = null ; 
		this.teamFinance = teamFinance ; 
<<<<<<< HEAD
=======
		this.stadium = stadium ; 
>>>>>>> Fatima2
		
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
<<<<<<< HEAD
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
=======
	
	
>>>>>>> Fatima2

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
<<<<<<< HEAD
=======

	public Stadium getStadium() {
		return stadium;
	}

	public TeamPerformance getTeamPerformance() {
		return teamPerformance;
	}

	public void setTeamPerformance(TeamPerformance teamPerformance) {
		this.teamPerformance = teamPerformance;
	}
	
	
	
	
	
	
>>>>>>> Fatima2
	
	
	
	
	
	
	
	
}

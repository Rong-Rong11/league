package data.sport.setup;

import java.text.Normalizer;

import data.team.Team;
<<<<<<< HEAD
import process.GameManager;
=======
import process.utilitary.CalendarUtilitary;
>>>>>>> Fatima2

public class GameContext {
	private Team homeTeam ; 
	private Team awayTeam ; 
	private int typeGame ; 
	private boolean isScheduled ; 
	private boolean isRivalry ; 
	
	public GameContext (Team homeTeam, Team awayTeam, int typeGame) {
		setAwayTeam(awayTeam);
		setHomeTeam(homeTeam);
<<<<<<< HEAD
		isRivalry = GameManager.isRivalry(this) ; 
=======
		isRivalry = CalendarUtilitary.isRivalry(this) ; 
>>>>>>> Fatima2
	}
	

	public boolean isScheduled() {
		return isScheduled;
	}

	public void setScheduled(boolean scheduled) {
		this.isScheduled = scheduled;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	public Team getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}

	public int getTypeGame() {
		return typeGame;
	}

	public void setTypeGame(int typeGame) {
		this.typeGame = typeGame;
	}


	public boolean isRivalry() {
		return isRivalry;
	}

	
	
	
	
	
	
	
}

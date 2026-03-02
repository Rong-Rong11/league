package data.sport.setup;

import java.util.ArrayList;
import java.util.TreeMap;

import data.player.Player;
import data.sport.play.action.ActionResult;
import data.team.Team;

public class GameResult {
	private Team winner = null ; 
	private Team loser = null ; 
	private int scorehomeTeam ; 
	private int scoreAwayTeam; 
	
	private int twoPointsHomeTeam ; 
	private int twoPointsAwayTeam ; 
	
	private int threePointsHomeTeam;
	private int threePointsAwayTeam ; 
	
	private int reboundHomeTeam ; 
	private int reboundAwayTeam ; 
	
	private int turnoverHomeTeam ; 
	private int turnoverAwayTeam ; 
	
	private int blockHomeTeam ; 
	private int blockAwayTeam ; 
	
	private int freeThrowHomeTeam ; 
	private int freeThrowAwayTeam ; 
	 
	private ArrayList<ActionResult> actions ; 
	
	public GameResult(int scorehomeTeam, int scoreAwayTeam, int twoPointsHomeTeam, int twoPointsAwayTeam,
			int threePointsHomeTeam, int threePointsAwayTeam, int reboundHomeTeam, int reboundAwayTeam,
			int turnoverHomeTeam, int turnoverAwayTeam, int blockHomeTeam, int blockAwayTeam, int freeThrowHomeTeam,
			int freeThrowAwayTeam) {
		
		this.scorehomeTeam = scorehomeTeam;
		this.scoreAwayTeam = scoreAwayTeam;
		this.twoPointsHomeTeam = twoPointsHomeTeam;
		this.twoPointsAwayTeam = twoPointsAwayTeam;
		this.threePointsHomeTeam = threePointsHomeTeam;
		this.threePointsAwayTeam = threePointsAwayTeam;
		this.reboundHomeTeam = reboundHomeTeam;
		this.reboundAwayTeam = reboundAwayTeam;
		this.turnoverHomeTeam = turnoverHomeTeam;
		this.turnoverAwayTeam = turnoverAwayTeam;
		this.blockHomeTeam = blockHomeTeam;
		this.blockAwayTeam = blockAwayTeam;
		this.freeThrowHomeTeam = freeThrowHomeTeam;
		this.freeThrowAwayTeam = freeThrowAwayTeam;
		actions = new ArrayList<ActionResult>() ; 
	}
	
	public GameResult() {
		this(0,0,0,0,0,0,0,0,0,0,0,0,0,0) ; 
	}
	
	public void addActions(ActionResult action) {
		actions.add(action) ; 
	}

	public int getScorehomeTeam() {
		return scorehomeTeam;
	}

	public void setScorehomeTeam(int scorehomeTeam) {
		this.scorehomeTeam = scorehomeTeam;
	}

	public int getScoreAwayTeam() {
		return scoreAwayTeam;
	}

	public void setScoreAwayTeam(int scoreAwayTeam) {
		this.scoreAwayTeam = scoreAwayTeam;
	}

	public int getTwoPointsHomeTeam() {
		return twoPointsHomeTeam;
	}

	public void setTwoPointsHomeTeam(int twoPointsHomeTeam) {
		this.twoPointsHomeTeam = twoPointsHomeTeam;
	}

	public int getTwoPointsAwayTeam() {
		return twoPointsAwayTeam;
	}

	public void setTwoPointsAwayTeam(int twoPointsAwayTeam) {
		this.twoPointsAwayTeam = twoPointsAwayTeam;
	}

	public int getThreePointsHomeTeam() {
		return threePointsHomeTeam;
	}

	public void setThreePointsHomeTeam(int threePointsHomeTeam) {
		this.threePointsHomeTeam = threePointsHomeTeam;
	}

	public int getThreePointsAwayTeam() {
		return threePointsAwayTeam;
	}

	public void setThreePointsAwayTeam(int threePointsAwayTeam) {
		this.threePointsAwayTeam = threePointsAwayTeam;
	}

	public int getReboundHomeTeam() {
		return reboundHomeTeam;
	}

	public void setReboundHomeTeam(int reboundHomeTeam) {
		this.reboundHomeTeam = reboundHomeTeam;
	}

	public int getReboundAwayTeam() {
		return reboundAwayTeam;
	}

	public void setReboundAwayTeam(int reboundAwayTeam) {
		this.reboundAwayTeam = reboundAwayTeam;
	}

	public int getTurnoverHomeTeam() {
		return turnoverHomeTeam;
	}

	public void setTurnoverHomeTeam(int turnoverHomeTeam) {
		this.turnoverHomeTeam = turnoverHomeTeam;
	}

	public int getTurnoverAwayTeam() {
		return turnoverAwayTeam;
	}

	public void setTurnoverAwayTeam(int turnoverAwayTeam) {
		this.turnoverAwayTeam = turnoverAwayTeam;
	}

	public ArrayList<ActionResult> getActions() {
		return actions;
	}

	public void setActions(ArrayList<ActionResult> actions) {
		this.actions = actions;
	}

	public int getBlockHomeTeam() {
		return blockHomeTeam;
	}

	public void setBlockHomeTeam(int blockHomeTeam) {
		this.blockHomeTeam = blockHomeTeam;
	}

	public int getBlockAwayTeam() {
		return blockAwayTeam;
	}

	public void setBlockAwayTeam(int blockAwayTeam) {
		this.blockAwayTeam = blockAwayTeam;
	}

	public int getFreeThrowHomeTeam() {
		return freeThrowHomeTeam;
	}

	public void setFreeThrowHomeTeam(int freeThrowHomeTeam) {
		this.freeThrowHomeTeam = freeThrowHomeTeam;
	}

	public int getFreeThrowAwayTeam() {
		return freeThrowAwayTeam;
	}

	public void setFreeThrowAwayTeam(int freeThrowAwayTeam) {
		this.freeThrowAwayTeam = freeThrowAwayTeam;
	}
	
	
	
	
	
	
}

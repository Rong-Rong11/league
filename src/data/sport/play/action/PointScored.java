package data.sport.play.action;

import data.player.Player;
import process.visitor.actionresult.ActionResultVisitor;

public class PointScored extends ActionResult {
	int pointsScored ;
	Player scorerPlayer ; 
	Player assistPlayer ; 

	public PointScored(String name, int pointsScored, Player scorerPlayer, Player assistPlayer) {
		super(name);
		this.pointsScored = pointsScored ; 
		this.scorerPlayer = scorerPlayer ; 
		this.assistPlayer = assistPlayer ; 
	}

	public int getPointsScored() {
		return pointsScored;
	}

	public Player getScorerPlayer() {
		return scorerPlayer;
	}

	public void setScorerPlayer(Player scorerPlayer) {
		this.scorerPlayer = scorerPlayer;
	}

	public Player getAssistPlayer() {
		return assistPlayer;
	}

	public void setAssistPlayer(Player assistPlayer) {
		this.assistPlayer = assistPlayer;
	}

	public void setPointsScored(int pointsScored) {
		this.pointsScored = pointsScored;
	}
	
	public <A> A accept(ActionResultVisitor<A> visitor) {
	    return visitor.visit(this);
	}
	
	

	
	
	
	

}

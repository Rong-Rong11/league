package data.sport.play.action;

import data.player.Player;
import process.visitor.actionresult.ActionResultVisitor;

public class Rebound extends ActionResult {
	
	private Player reboundPlayer ; 
	private Player missedPlayer ; 
	
	public Rebound(String name, Player reboundPlayer, Player missedPlayer) {
		super(name) ; 
		this.reboundPlayer = reboundPlayer ; 
		this.missedPlayer = missedPlayer ; 
	}

	public Player getReboundPlayer() {
		return reboundPlayer;
	}

	public void setReboundPlayer(Player reboundPlayer) {
		this.reboundPlayer = reboundPlayer;
	}

	public Player getMissedPlayer() {
		return missedPlayer;
	}

	public void setMissedPlayer(Player missedPlayer) {
		this.missedPlayer = missedPlayer;
	}
	public <A> A accept(ActionResultVisitor<A> visitor) {
	    return visitor.visit(this);
	}
	
	

}

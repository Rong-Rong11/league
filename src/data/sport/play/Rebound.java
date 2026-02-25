package data.sport.play;

import data.player.Player;

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
	
	

}

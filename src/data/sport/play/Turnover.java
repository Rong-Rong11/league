package data.sport.play;

import data.player.Player;

public class Turnover extends ActionResult {
	
	private Player interceptedPlayer ; 
	private Player defensePlayer  ;
	
	public Turnover(String name, Player interceptedPlayer, Player defensePlayer) {
		super(name);
		this.interceptedPlayer = interceptedPlayer ; 
		this.defensePlayer = defensePlayer ; 
	}

	public Player getInterceptedPlayer() {
		return interceptedPlayer;
	}


	public Player getDefensePlayer() {
		return defensePlayer;
	}
	
	

}

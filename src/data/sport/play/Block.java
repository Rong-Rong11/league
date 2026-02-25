package data.sport.play;

import data.player.Player;

public class Block extends ActionResult {
	
	private Player blockingPlayer ; 
	public Block(String name, Player blockingPlayer) {
		super(name);
		this.blockingPlayer = blockingPlayer ; 
	}
	public Player getBlockingPlayer() {
		return blockingPlayer;
	}
	public void setBlockingPlayer(Player blockingPlayer) {
		this.blockingPlayer = blockingPlayer;
	}
	
	

}

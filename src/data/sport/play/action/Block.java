package data.sport.play.action;

import data.player.Player;
import process.visitor.actionresult.ActionResultVisitor;

public class Block extends ActionResult{
	
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
	public <A> A accept(ActionResultVisitor<A> visitor) {
	    return visitor.visit(this);
	}
	
	

}

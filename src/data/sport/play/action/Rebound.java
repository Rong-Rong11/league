package data.sport.play.action;

import data.player.Player;
import process.visitor.actionresult.ActionResultVisitor;

public class Rebound
extends ActionResult {
	private Player reboundPlayer;
	private Player missedPlayer;

	public Rebound(String name, Player reboundPlayer, Player missedPlayer) {
		super(name);
		this.reboundPlayer = reboundPlayer;
		this.missedPlayer = missedPlayer;
	}

	public Player getReboundPlayer() {
		return this.reboundPlayer;
	}

	public void setReboundPlayer(Player player) {
		this.reboundPlayer = player;
	}

	public Player getMissedPlayer() {
		return this.missedPlayer;
	}

	public void setMissedPlayer(Player player) {
		this.missedPlayer = player;
	}

	@Override
	public <A> A accept(ActionResultVisitor<A> actionResultVisitor) {
		return actionResultVisitor.visit(this);
	}
}

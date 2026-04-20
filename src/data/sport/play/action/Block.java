/*
	* Decompiled with CFR 0.152.
	*/
package data.sport.play.action;

import data.player.Player;
import process.visitor.actionresult.ActionResultVisitor;

public class Block
extends ActionResult {
	private Player blockingPlayer;

	public Block(String string, Player player) {
		super(string);
		this.blockingPlayer = player;
	}

	public Player getBlockingPlayer() {
		return this.blockingPlayer;
	}

	public void setBlockingPlayer(Player player) {
		this.blockingPlayer = player;
	}

	@Override
	public <A> A accept(ActionResultVisitor<A> actionResultVisitor) {
		return actionResultVisitor.visit(this);
	}
}

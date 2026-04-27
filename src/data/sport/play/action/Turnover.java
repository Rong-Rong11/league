package data.sport.play.action;

import data.player.Player;
import process.visitor.actionresult.ActionResultVisitor;

public class Turnover
extends ActionResult {
	private Player interceptedPlayer;
	private Player defensePlayer;

	public Turnover(String name, Player interceptedPlayer, Player defensePlayer) {
		super(name);
		this.interceptedPlayer = interceptedPlayer;
		this.defensePlayer = defensePlayer;
	}

	public Player getInterceptedPlayer() {
		return this.interceptedPlayer;
	}

	public Player getDefensePlayer() {
		return this.defensePlayer;
	}

	@Override
	public <A> A accept(ActionResultVisitor<A> actionResultVisitor) {
		return actionResultVisitor.visit(this);
	}
}

package data.sport.play.action;

import data.player.Player;
import process.visitor.actionresult.ActionResultVisitor;

public class MissedShot extends ActionResult {
	private Player shooter;

	public MissedShot(String name, Player shooter) {
		super(name);
		this.shooter = shooter;
	}

	public Player getShooter() {
		return shooter;
	}

	public <A> A accept(ActionResultVisitor<A> visitor) {
		return visitor.visit(this);
	}
}

package data.sport.play.action;

import data.player.Player;
import process.visitor.actionresult.ActionResultVisitor;

public class PointScored
extends ActionResult {
	int pointsScored;
	Player scorerPlayer;
	Player assistPlayer;

	public PointScored(String name, int pointsScored, Player scorerPlayer, Player assistPlayer) {
		super(name);
		this.pointsScored = pointsScored;
		this.scorerPlayer = scorerPlayer;
		this.assistPlayer = assistPlayer;
	}

	public int getPointsScored() {
		return this.pointsScored;
	}

	public Player getScorerPlayer() {
		return this.scorerPlayer;
	}

	public void setScorerPlayer(Player player) {
		this.scorerPlayer = player;
	}

	public Player getAssistPlayer() {
		return this.assistPlayer;
	}

	public void setAssistPlayer(Player player) {
		this.assistPlayer = player;
	}

	public void setPointsScored(int pointsScored) {
		this.pointsScored = pointsScored;
	}

	@Override
	public <A> A accept(ActionResultVisitor<A> actionResultVisitor) {
		return actionResultVisitor.visit(this);
	}
}

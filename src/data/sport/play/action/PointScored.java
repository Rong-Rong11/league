/*
	* Decompiled with CFR 0.152.
	*/
package data.sport.play.action;

import data.player.Player;
import data.sport.play.action.ActionResult;
import process.visitor.actionresult.ActionResultVisitor;

public class PointScored
extends ActionResult {
	int pointsScored;
	Player scorerPlayer;
	Player assistPlayer;

	public PointScored(String string, int n, Player player, Player player2) {
		super(string);
		this.pointsScored = n;
		this.scorerPlayer = player;
		this.assistPlayer = player2;
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

	public void setPointsScored(int n) {
		this.pointsScored = n;
	}

	@Override
	public <A> A accept(ActionResultVisitor<A> actionResultVisitor) {
		return actionResultVisitor.visit(this);
	}
}

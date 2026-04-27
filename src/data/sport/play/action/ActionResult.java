package data.sport.play.action;

import data.sport.play.OffensiveTry;
import process.visitor.actionresult.ActionResultVisitor;

public abstract class ActionResult {
	private String name;
	private int actionTime;
	private OffensiveTry offensiveTry;

	public ActionResult(String name) {
		this.name = name;
		this.actionTime = 0;
		this.offensiveTry = null;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setActionTime(int n) {
		this.actionTime = n;
	}

	public int getActionTime() {
		return this.actionTime;
	}

	public OffensiveTry getOffensiveAction() {
		return this.offensiveTry;
	}

	public void setOffensiveAction(OffensiveTry offensiveTry) {
		this.offensiveTry = offensiveTry;
	}

	public abstract <A> A accept(ActionResultVisitor<A> visitor);
}

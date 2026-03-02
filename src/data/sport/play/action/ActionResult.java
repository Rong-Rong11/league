package data.sport.play.action;

import data.sport.play.OffensiveTry;
import process.visitor.actionresult.ActionResultVisitor;

public abstract class ActionResult {
	
	private String name ; 
	private int actionTime ; 
	private OffensiveTry offensiveTry ; 
	
	public ActionResult(String name) {
		this.name = name ; 
		actionTime = 0 ; 
		offensiveTry = null ; 
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name ; 
	}
	
	public void setActionTime(int actionTime) {
		this.actionTime = actionTime ; 
	}

	public int getActionTime() {
		return actionTime;
	}

	public OffensiveTry getOffensiveAction() {
		return offensiveTry;
	}

	public void setOffensiveAction(OffensiveTry offensiveTry) {
		this.offensiveTry = offensiveTry;
	}
	
	public abstract <A> A accept(ActionResultVisitor<A> visitor);
	
	
	
	
	
	
}

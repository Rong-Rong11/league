package data.sport.play.action;

import process.visitor.actionresult.ActionResultVisitor;

public class EndOfTime
extends ActionResult {
	public EndOfTime(String name) {
		super(name);
	}

	@Override
	public <A> A accept(ActionResultVisitor<A> actionResultVisitor) {
		return actionResultVisitor.visit(this);
	}
}

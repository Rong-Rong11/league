/*
	* Decompiled with CFR 0.152.
	*/
package data.sport.play.action;

import process.visitor.actionresult.ActionResultVisitor;

public class EndOfTime
extends ActionResult {
	public EndOfTime(String string) {
		super(string);
	}

	@Override
	public <A> A accept(ActionResultVisitor<A> actionResultVisitor) {
		return actionResultVisitor.visit(this);
	}
}

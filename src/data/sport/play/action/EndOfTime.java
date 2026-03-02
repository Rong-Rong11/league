package data.sport.play.action;

import java.awt.event.ActionEvent;

import process.visitor.actionresult.ActionResultVisitor;

public class EndOfTime extends ActionResult {

	public EndOfTime(String name) {
		super(name) ; 
	}
	
	public <A> A accept(ActionResultVisitor<A> visitor) {
	    return visitor.visit(this);
	}
	

}

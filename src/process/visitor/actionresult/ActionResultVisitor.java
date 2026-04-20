/*
	* Decompiled with CFR 0.152.
	*/
package process.visitor.actionresult;

import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.MissedShot;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;

public interface ActionResultVisitor<A> {
	public A visit(Block var1);

	public A visit(PointScored var1);

	public A visit(MissedShot var1);

	public A visit(Rebound var1);

	public A visit(Turnover var1);

	public A visit(EndOfTime var1);
}

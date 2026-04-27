package process.visitor.actionresult;

import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.MissedShot;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;

public interface ActionResultVisitor<A> {
	public A visit(Block block);

	public A visit(PointScored pointScored);

	public A visit(MissedShot missedShot);

	public A visit(Rebound rebound);

	public A visit(Turnover turnover);

	public A visit(EndOfTime endOfTime);
}

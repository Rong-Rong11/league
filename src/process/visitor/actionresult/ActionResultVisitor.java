package process.visitor.actionresult;

import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.MissedShot;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;

public interface ActionResultVisitor<A> {

	A visit(Block block);

	A visit(PointScored pointScored);

	A visit(MissedShot missedShot);

	A visit(Rebound rebound);

	A visit(Turnover turnover);

	A visit(EndOfTime endOfTime);

}

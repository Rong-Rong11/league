package data.team.finance.marketsize;
import process.visitor.marketsize.MarketSizeVisitor;

public class SmallSize
implements MarketSize {
	@Override
	public <M> M accept(MarketSizeVisitor<M> marketSizeVisitor) {
		return marketSizeVisitor.visit(this);
	}
}

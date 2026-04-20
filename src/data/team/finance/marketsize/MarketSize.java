package data.team.finance.marketsize;

import process.visitor.marketsize.MarketSizeVisitor;

public interface MarketSize {
	public <M> M accept(MarketSizeVisitor<M> var1);
}

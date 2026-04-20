/*
	* Decompiled with CFR 0.152.
	*/
package data.team.finance.marketsize;

import data.team.finance.marketsize.MarketSize;
import process.visitor.marketsize.MarketSizeVisitor;

public class LargeSize
implements MarketSize {
	@Override
	public <M> M accept(MarketSizeVisitor<M> marketSizeVisitor) {
		return marketSizeVisitor.visit(this);
	}
}

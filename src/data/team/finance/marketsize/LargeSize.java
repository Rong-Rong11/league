package data.team.finance.marketsize;

import process.visitor.marketsize.MarketSizeVisitor;

public class LargeSize extends MarketSize {

	public LargeSize(String size) {
		super(size);
		// TODO Auto-generated constructor stub
	}
	
	public <M> M accept(MarketSizeVisitor<M> marketSizeVisitor) {
		return marketSizeVisitor.visit(this) ; 
	}
	
	
}

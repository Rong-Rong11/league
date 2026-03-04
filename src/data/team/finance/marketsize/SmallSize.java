package data.team.finance.marketsize;

import process.visitor.marketsize.MarketSizeVisitor;

public class SmallSize extends MarketSize {

	public SmallSize(String size) {
		super(size);
		// TODO Auto-generated constructor stub
	}
	
	public <M> M accept(MarketSizeVisitor<M> marketSizeVisitor) {
		return marketSizeVisitor.visit(this) ; 
	}
}

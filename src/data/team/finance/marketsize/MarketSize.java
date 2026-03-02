package data.team.finance.marketsize;

import process.visitor.marketsize.MarketSizeVisitor;

public abstract class MarketSize {
	private String size ; 
	
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public MarketSize(String size) {
		this.size = size;
	} 
	
	public abstract <M> M accept(MarketSizeVisitor<M> marketSizeVisitor) ; 
	
	
	
	
}

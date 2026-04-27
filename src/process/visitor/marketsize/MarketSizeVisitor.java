package process.visitor.marketsize;

import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public interface MarketSizeVisitor<M> {
	public M visit(LargeSize marketSize);

	public M visit(MediumSize marketSize);

	public M visit(SmallSize marketSize);
}

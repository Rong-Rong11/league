package process.visitor.marketsize;

import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public interface MarketSizeVisitor<M> {
	M visit(LargeSize largeSize);

	M visit(MediumSize mediumSize);

	M visit(SmallSize smallSize);
}

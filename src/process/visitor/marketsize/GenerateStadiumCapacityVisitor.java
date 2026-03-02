package process.visitor.marketsize;

import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public class GenerateStadiumCapacityVisitor implements MarketSizeVisitor<Integer> {

	public GenerateStadiumCapacityVisitor() {

	}

	public Integer visit(LargeSize largeSize) {
		return 40000 + (int) (Math.random() * 40000);
	}

	public Integer visit(MediumSize mediumSize) {
		return 15000 + (int) (Math.random() * 20000);
	}

	public Integer visit(SmallSize smallSize) {
		return 5000 + (int) (Math.random() * 5000);
	}
}

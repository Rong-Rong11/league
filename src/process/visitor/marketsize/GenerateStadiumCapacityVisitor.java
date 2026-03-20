/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.marketsize;

import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;
import process.visitor.marketsize.MarketSizeVisitor;

public class GenerateStadiumCapacityVisitor
implements MarketSizeVisitor<Integer> {
    @Override
    public Integer visit(LargeSize largeSize) {
        return 40000 + (int)(Math.random() * 40000.0);
    }

    @Override
    public Integer visit(MediumSize mediumSize) {
        return 15000 + (int)(Math.random() * 20000.0);
    }

    @Override
    public Integer visit(SmallSize smallSize) {
        return 5000 + (int)(Math.random() * 5000.0);
    }
}

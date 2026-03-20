/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.marketsize;

import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public interface MarketSizeVisitor<M> {
    public M visit(LargeSize var1);

    public M visit(MediumSize var1);

    public M visit(SmallSize var1);
}

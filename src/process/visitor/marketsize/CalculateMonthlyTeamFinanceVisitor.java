/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.marketsize;

import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public class CalculateMonthlyTeamFinanceVisitor
        implements MarketSizeVisitor<Double> {
    private double baseMarketMultiplier = 1.0;

    @Override
    public Double visit(LargeSize largeSize) {
        return this.baseMarketMultiplier * 1.3;
    }

    @Override
    public Double visit(MediumSize mediumSize) {
        return this.baseMarketMultiplier * 1.0;
    }

    @Override
    public Double visit(SmallSize smallSize) {
        return this.baseMarketMultiplier * 0.7;
    }
}

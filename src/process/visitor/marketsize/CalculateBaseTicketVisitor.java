/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.marketsize;

import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;
import process.visitor.marketsize.MarketSizeVisitor;

public class CalculateBaseTicketVisitor
implements MarketSizeVisitor<Double> {
    private double baseTicketPrice = 100.0;

    @Override
    public Double visit(LargeSize largeSize) {
        return this.baseTicketPrice * 1.3;
    }

    @Override
    public Double visit(MediumSize mediumSize) {
        return this.baseTicketPrice * 1.0;
    }

    @Override
    public Double visit(SmallSize smallSize) {
        return this.baseTicketPrice * 0.7;
    }
}

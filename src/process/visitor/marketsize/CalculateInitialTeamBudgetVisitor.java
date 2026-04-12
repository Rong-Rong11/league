/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.marketsize;

import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public class CalculateInitialTeamBudgetVisitor
        implements MarketSizeVisitor<Double> {
    private double baseBudget;
    private double popularity;
    private EconomicProfil economicProfil;

    public CalculateInitialTeamBudgetVisitor(double baseBudget, double popularity, EconomicProfil economicProfil) {
        this.baseBudget = baseBudget;
        this.popularity = popularity;
        this.economicProfil = economicProfil;
    }

    @Override
    public Double visit(LargeSize largeSize) {
        return this.computeBudget(1.6);
    }

    @Override
    public Double visit(MediumSize mediumSize) {
        return this.computeBudget(1.4);
    }

    @Override
    public Double visit(SmallSize smallSize) {
        return this.computeBudget(1.1);
    }

    private double computeBudget(double marketMultiplier) {
        double budget = this.baseBudget;
        budget *= marketMultiplier;
        double popularityFactor = 0.85 + popularity / 100.0 * 0.3;
        budget *= popularityFactor;
        double prestigeFactor = 0.85 + economicProfil.getHistoricalPrestige() * 0.3;
        budget *= prestigeFactor;
        double mediaFactor = 1.25;
        budget *= mediaFactor;
        double commercialFactor = 0.9 + economicProfil.getCommercialAggressiveness() * 0.2;
        budget *= commercialFactor;
        double ownerFactor = 0.7 + economicProfil.getOwnerDeficitTolerance() * 0.6;
        return budget *= ownerFactor;
    }
}

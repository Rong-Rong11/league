/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.financialprofil;

import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;
import data.team.finance.marketsize.MarketSize;
import process.visitor.marketsize.MarketSizePayrollTargetVisitor;

public class ValidateTradeVisitor
        implements FinancialProfilVisitor<Boolean> {
    private double payroll;
    private double salaryCap;
    private double marketSizeFactor;

    public ValidateTradeVisitor(double payroll, double salaryCap, MarketSize marketSize) {
        this.payroll = payroll;
        this.salaryCap = salaryCap;
        if (marketSize == null) {
            this.marketSizeFactor = 1.0;
        } else {
            this.marketSizeFactor = marketSize.accept(new MarketSizePayrollTargetVisitor());
        }
    }

    @Override
    public Boolean visit(ThriftyPolicy thriftyProfil) {
        if (this.payroll <= this.salaryCap * 1.05 * this.marketSizeFactor) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean visit(BalancedPolicy balancedProfil) {
        if (this.payroll <= this.salaryCap * 1.15 * this.marketSizeFactor) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean visit(AmbitiousPolicy ambitiousProfil) {
        if (this.payroll <= this.salaryCap * 1.35 * this.marketSizeFactor) {
            return true;
        }
        return false;
    }
}

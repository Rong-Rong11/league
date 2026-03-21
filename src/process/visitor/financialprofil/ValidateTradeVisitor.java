/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.financialprofil;

import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;

public class ValidateTradeVisitor
        implements FinancialProfilVisitor<Boolean> {
    private double payroll;
    private double salaryCap;

    public ValidateTradeVisitor(double payroll, double salaryCap) {
        this.payroll = payroll;
        this.salaryCap = salaryCap;
    }

    @Override
    public Boolean visit(ThriftyPolicy thriftyProfil) {
        if (this.payroll <= this.salaryCap * 1.05) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean visit(BalancedPolicy balancedProfil) {
        if (this.payroll <= this.salaryCap * 1.15) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean visit(AmbitiousPolicy ambitiousProfil) {
        if (this.payroll <= this.salaryCap * 1.35) {
            return true;
        }
        return false;
    }
}

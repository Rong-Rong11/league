/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.financialprofil;

import data.team.finance.financialprofil.AmbitiousProfil;
import data.team.finance.financialprofil.BalancedProfil;
import data.team.finance.financialprofil.ThriftyProfil;
import process.visitor.financialprofil.FinancialProfilVisitor;

public class ValidateTradeVisitor
implements FinancialProfilVisitor<Boolean> {
    private double payroll;
    private double salaryCap;

    public ValidateTradeVisitor(double payroll, double salaryCap) {
        this.payroll = payroll;
        this.salaryCap = salaryCap;
    }

    @Override
    public Boolean visit(ThriftyProfil thriftyProfil) {
        if (this.payroll <= this.salaryCap * 1.05) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean visit(BalancedProfil balancedProfil) {
        if (this.payroll <= this.salaryCap * 1.15) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean visit(AmbitiousProfil ambitiousProfil) {
        if (this.payroll <= this.salaryCap * 1.35) {
            return true;
        }
        return false;
    }
}

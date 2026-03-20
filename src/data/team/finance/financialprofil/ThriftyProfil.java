/*
 * Decompiled with CFR 0.152.
 */
package data.team.finance.financialprofil;

import process.visitor.financialprofil.FinancialProfilVisitor;

public class ThriftyProfil
        extends FinancialProfil {
    public ThriftyProfil() {
        super();
    }

    @Override
    public <F> F accept(FinancialProfilVisitor<F> financialProfilVisitor) {
        return financialProfilVisitor.visit(this);
    }
}

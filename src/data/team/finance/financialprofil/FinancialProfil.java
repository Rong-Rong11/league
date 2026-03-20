/*
 * Decompiled with CFR 0.152.
 */
package data.team.finance.financialprofil;

import process.visitor.financialprofil.FinancialProfilVisitor;

public abstract class FinancialProfil {

    public FinancialProfil() {

    }

    public abstract <F> F accept(FinancialProfilVisitor<F> var1);
}

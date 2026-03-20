/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.financialprofil;

import data.team.finance.financialprofil.AmbitiousProfil;
import data.team.finance.financialprofil.BalancedProfil;
import data.team.finance.financialprofil.ThriftyProfil;

public interface FinancialProfilVisitor<F> {
    public F visit(ThriftyProfil var1);

    public F visit(BalancedProfil var1);

    public F visit(AmbitiousProfil var1);
}

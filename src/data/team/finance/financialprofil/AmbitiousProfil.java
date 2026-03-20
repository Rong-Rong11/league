package data.team.finance.financialprofil;

import process.visitor.financialprofil.FinancialProfilVisitor;

public class AmbitiousProfil
        extends FinancialProfil {
    public AmbitiousProfil() {
        super();
    }

    @Override
    public <F> F accept(FinancialProfilVisitor<F> financialProfilVisitor) {
        return financialProfilVisitor.visit(this);
    }
}

/*
 * Decompiled with CFR 0.152.
 */
package data.team.finance;

import data.finance.budget.Budget;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import data.team.finance.transfer.TeamTransferStrategy;

public class TeamFinance {
    private FinancialPolicy financialProfil;
    private EconomicProfil economicProfil = new EconomicProfil();
    private MediaMarket mediaMarket = new MediaMarket();
    private MarketSize marketSize;

    private Budget budget;
    private double payroll;
    private double luxuryTaxPaid;
    private int transferMade = 0;
    private TeamTransferStrategy teamTransferStrategy;

    public TeamFinance(FinancialPolicy financialProfil, Budget budget, MarketSize marketSize,
            TeamTransferStrategy teamTransferStrategy) {
        this.financialProfil = financialProfil;
        this.budget = budget;
        this.payroll = 0.0;
        this.marketSize = marketSize;
        this.luxuryTaxPaid = 0.0;
        this.transferMade = 0;
        this.teamTransferStrategy = teamTransferStrategy;
    }

    public FinancialPolicy getFinancialProfil() {
        return this.financialProfil;
    }

    public double getPayroll() {
        return this.payroll;
    }

    public void setPayroll(double payroll) {
        this.payroll = payroll;
    }

    public void incrementTransferMade() {
        ++this.transferMade;
    }

    public Budget getBudget() {
        return this.budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public TeamTransferStrategy getTeamTransferStrategy() {
        return this.teamTransferStrategy;
    }

    public void setTeamTransferStrategy(TeamTransferStrategy teamTransferStrategy) {
        this.teamTransferStrategy = teamTransferStrategy;
    }

    public int getTransferMade() {
        return this.transferMade;
    }

    public MarketSize getMarketSize() {
        return this.marketSize;
    }

    public void setMarketSize(MarketSize marketSize) {
        this.marketSize = marketSize;
    }

    public double getLuxuryTaxPaid() {
        return this.luxuryTaxPaid;
    }

    public void setLuxuryTaxPaid(double luxuryTaxPaid) {
        this.luxuryTaxPaid = luxuryTaxPaid;
    }

    public void setFinancialProfil(FinancialPolicy financialProfil) {
        this.financialProfil = financialProfil;
    }

    public void setTransferMade(int transferMade) {
        this.transferMade = transferMade;
    }

    public EconomicProfil getEconomicProfil() {
        return economicProfil;
    }

    public void setEconomicProfil(EconomicProfil economicProfil) {
        this.economicProfil = economicProfil;
    }

    public MediaMarket getMediaMarket() {
        return mediaMarket;
    }

}

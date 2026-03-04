package data.team.finance;

import data.finance.budget.Budget;
<<<<<<< HEAD
=======
import data.team.finance.financialprofil.FinancialProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.transfer.TeamTransferStrategy;
>>>>>>> Fatima2

public class TeamFinance {
	
	private FinancialProfil financialProfil ; 
	private Budget budget ; 
	private double payroll ; 
	private MarketSize marketSize ; 
	private double luxuryTaxPaid ;
<<<<<<< HEAD
	
	public TeamFinance(FinancialProfil financialProfil, Budget budget, MarketSize marketSize) {
		super();
		this.financialProfil = financialProfil;
		this.budget = budget;
		this.payroll = 0 ; 
		this.marketSize = marketSize;
		this.luxuryTaxPaid = 0 ;
=======
	private int transferMade = 0 ; 
	private TeamTransferStrategy teamTransferStrategy ; 
	
	public TeamFinance(FinancialProfil financialProfil, Budget budget, MarketSize marketSize, TeamTransferStrategy teamTransferStrategy) {
		super();
		this.financialProfil = financialProfil;
		this.budget = budget;
		payroll = 0 ; 
		this.marketSize = marketSize;
		luxuryTaxPaid = 0 ;
		transferMade = 0 ; 
		this.teamTransferStrategy = teamTransferStrategy ; 
>>>>>>> Fatima2
	}

	public FinancialProfil getFinancialProfil() {
		return financialProfil;
<<<<<<< HEAD
	} 
	
=======
	}

	public double getPayroll() {
		return payroll;
	}

	public void setPayroll(double payroll) {
		this.payroll = payroll;
	} 
	
	public void incrementTransferMade() {
		transferMade ++ ; 
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public TeamTransferStrategy getTeamTransferStrategy() {
		return teamTransferStrategy;
	}

	public void setTeamTransferStrategy(TeamTransferStrategy teamTransferStrategy) {
		this.teamTransferStrategy = teamTransferStrategy;
	}

	public int getTransferMade() {
		return transferMade;
	}

	public MarketSize getMarketSize() {
		return marketSize;
	}

	public void setMarketSize(MarketSize marketSize) {
		this.marketSize = marketSize;
	}

	public double getLuxuryTaxPaid() {
		return luxuryTaxPaid;
	}

	public void setLuxuryTaxPaid(double luxuryTaxPaid) {
		this.luxuryTaxPaid = luxuryTaxPaid;
	}

	public void setFinancialProfil(FinancialProfil financialProfil) {
		this.financialProfil = financialProfil;
	}

	public void setTransferMade(int transferMade) {
		this.transferMade = transferMade;
	}
	
	

	
	
	
	
	
	
	
	
	
>>>>>>> Fatima2
	
	
	
}

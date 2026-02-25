package data.league;

import config.FinanceConfiguration;
import config.SimulationConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.Income;
import process.FinanceManager;

public class LeagueFinance {
	private Budget budget ; 
	private double salaryCap ; 
	private double luxuryTaxLine ; 
	private double minimumTeamSalary ;
	
	public LeagueFinance(Budget budget, double salaryCap, double luxuryTaxLine, double minimumTeamSalary) {
		super();
		this.budget = budget;
		this.salaryCap = salaryCap;
		this.luxuryTaxLine = luxuryTaxLine;
		this.minimumTeamSalary = minimumTeamSalary;
	} 
	
	//les sommes en millions 
	
	
	
	
}

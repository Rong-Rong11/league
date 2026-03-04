package data.league;

import config.FinanceConfiguration;
import config.SimulationConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.Income;
<<<<<<< HEAD
import process.FinanceManager;

public class LeagueFinance {
	private Budget budget ; 
	private double salaryCap ; 
	private double luxuryTaxLine ; 
	private double minimumTeamSalary ;
=======
import process.utilitary.FinanceUtilitary;

public class LeagueFinance {
	private Budget budget ; 
	public static double salaryCap ; 
	public static double luxuryTaxLine ; 
	public static double minimumTeamSalary ;
>>>>>>> Fatima2
	
	public LeagueFinance(Budget budget, double salaryCap, double luxuryTaxLine, double minimumTeamSalary) {
		super();
		this.budget = budget;
		this.salaryCap = salaryCap;
		this.luxuryTaxLine = luxuryTaxLine;
		this.minimumTeamSalary = minimumTeamSalary;
<<<<<<< HEAD
	} 
	
=======
	}

	public Budget getBudget() {
		return budget;
	}

	public double getSalaryCap() {
		return salaryCap;
	}

	public double getLuxuryTaxLine() {
		return luxuryTaxLine;
	}

	public double getMinimumTeamSalary() {
		return minimumTeamSalary;
	} 
	
	
	
	
	
>>>>>>> Fatima2
	//les sommes en millions 
	
	
	
	
}

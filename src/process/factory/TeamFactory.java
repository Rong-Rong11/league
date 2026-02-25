package process.factory;

import config.FinanceConfiguration;
import config.SimulationConfiguration;
import data.finance.budget.Budget;
import data.team.Team;
import data.team.finance.AmbitiousProfil;
import data.team.finance.BalancedProfil;
import data.team.finance.EconomicalProfil;
import data.team.finance.FinancialProfil;
import data.team.finance.MarketSize;
import data.team.finance.TeamFinance;

public class TeamFactory {
	
	private static String checkRivalTeam(String rivalTeam) {
		if(rivalTeam.equals("")) {
			return SimulationConfiguration.NO_RIVAL ; 
		}
		return rivalTeam ; 
	}
	
	public static Team createTeam(String line) {
		FinancialProfil financialProfil = randomFinancialProfil() ; 
		String[] data = line.split(",", -1) ; 
		
		String teamName = data[2] ; 
		String rivalTeamName = checkRivalTeam(data[11])  ; 
		double teamPopularity = Float.valueOf(data[12]) ; 
		
		MarketSize marketSize = randomMarketSize() ; 
		Budget budget = new Budget(calculateBudget(marketSize, teamPopularity)) ; 
		TeamFinance teamFinance = new TeamFinance(financialProfil, budget, marketSize) ; 
		
		Team team = new Team(teamName, rivalTeamName, teamPopularity, teamFinance) ; 
		return team ; 
		
	}
	
	private static double calculateBudget(MarketSize marketSize, double popularity) {
		double budget = FinanceConfiguration.BASE_TEAM_BUDGET ; 
		if (popularity <= 70) {
			budget *= 0.9 ; 
		}
		else if(popularity <= 80) {
			budget *= 1.1 ; 
		}
		else if(popularity <= 90) {
			budget *= 1.3 ; 
		}
		else {
			budget *= 1.5 ; 
		}
		switch (marketSize.getSize()) {
			case(FinanceConfiguration.MARKET_SIZE_SMALL) : 
				budget *= 0.8 ; break ; 
			case(FinanceConfiguration.MARKET_SIZE_MEDIUM) : 
				budget *= 1 ; break ; 
			case (FinanceConfiguration.MARKET_SIZE_LARGE) : 
				budget *= 1.2 ; 
		}
		return budget ; 
	}
	
	private static FinancialProfil randomFinancialProfil() {
		double r = Math.random();
		if (r < 0.3)
			return new AmbitiousProfil(FinanceConfiguration.FINANCE_PROFIL_AMBITIOUS);
		if (r < 0.6)
			return new EconomicalProfil(FinanceConfiguration.FINANCE_PROFIL_ECONOMIC);
		return new BalancedProfil(FinanceConfiguration.FINANCE_PROFIL_BALANCED);
	}
	
	private static MarketSize randomMarketSize () {
	    double random = Math.random();
	    if (random < 0.25)
	        return new MarketSize(FinanceConfiguration.MARKET_SIZE_LARGE);
	    if (random < 0.75)
	        return new MarketSize(FinanceConfiguration.MARKET_SIZE_MEDIUM);
	    return new MarketSize(FinanceConfiguration.MARKET_SIZE_SMALL);
	}
}

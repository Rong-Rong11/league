package process.factory;

import config.FinanceConfiguration;
<<<<<<< HEAD
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
=======
import config.FinancialPolicy;
import config.SimulationConfiguration;
import data.finance.budget.Budget;
import data.team.Stadium;
import data.team.Team;
import data.team.finance.TeamFinance;
import data.team.finance.financialprofil.BalancedProfil;
import data.team.finance.financialprofil.FinancialProfil;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;
import data.team.finance.transfer.TeamTransferStrategy;
import process.utilitary.FinanceUtilitary;
import process.utilitary.TransferStrategyUtilitary;
import process.visitor.marketsize.CalculateBaseTicketVisitor;
import process.visitor.marketsize.CalculateInitialTeamBudgetVisitor;
import process.visitor.marketsize.GenerateStadiumCapacityVisitor;

public class TeamFactory {

	private static String checkRivalTeam(String rivalTeam) {
		if (rivalTeam.equals("")) {
			return SimulationConfiguration.NO_RIVAL;
		}
		return rivalTeam;
	}

	public static Team createTeam(String line) {
		String[] data = line.split(",", -1);

		String teamName = data[2];
		String rivalTeamName = checkRivalTeam(data[11]);
		double teamPopularity = Float.valueOf(data[12]);

		MarketSize marketSize = randomMarketSize();
		Budget budget = new Budget(calculateInitialBudget(marketSize, teamPopularity));
		FinanceUtilitary.initiateBudget(budget);
		FinancialProfil financialProfil = new BalancedProfil(FinancialPolicy.FINANCE_PROFIL_BALANCED);
		TeamTransferStrategy teamTransferStrategy = TransferStrategyUtilitary.chooseTransferStrategy(financialProfil,
				rivalTeamName);
		TeamFinance teamFinance = new TeamFinance(financialProfil, budget, marketSize, teamTransferStrategy);

		String stadiumName = data[33];
		Stadium stadium = new Stadium(stadiumName, calculateBaseTicketPrice(marketSize), generateCapacity(marketSize));

		Team team = new Team(teamName, rivalTeamName, teamPopularity, teamFinance, stadium);
		return team;

	}

	private static double calculateInitialBudget(MarketSize marketSize, double popularity) {
		double budget = FinanceConfiguration.BASE_TEAM_BUDGET;
		if (popularity <= 70) {
			budget *= 0.9;
		} else if (popularity <= 80) {
			budget *= 1.1;
		} else if (popularity <= 90) {
			budget *= 1.3;
		} else {
			budget *= 1.5;
		}
		CalculateInitialTeamBudgetVisitor calculateInitialTeamBudgetVisitor = new CalculateInitialTeamBudgetVisitor(
				budget);
		return marketSize.accept(calculateInitialTeamBudgetVisitor);

	}

	private static int generateCapacity(MarketSize marketSize) {
		GenerateStadiumCapacityVisitor generateStadiumCapacityVisitor = new GenerateStadiumCapacityVisitor();
		return marketSize.accept(generateStadiumCapacityVisitor);
	}

	private static MarketSize randomMarketSize() {
		double random = Math.random();
		if (random < 0.25)
			return new LargeSize(FinanceConfiguration.MARKET_SIZE_LARGE);
		if (random < 0.75)
			return new MediumSize(FinanceConfiguration.MARKET_SIZE_MEDIUM);
		return new SmallSize(FinanceConfiguration.MARKET_SIZE_SMALL);
	}

	private static double calculateBaseTicketPrice(MarketSize marketSize) {
		CalculateBaseTicketVisitor calculateBaseTicketVisitor = new CalculateBaseTicketVisitor();
		return marketSize.accept(calculateBaseTicketVisitor);
	}

>>>>>>> Fatima2
}

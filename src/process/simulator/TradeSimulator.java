package process.simulator;

import java.util.ArrayList;
import java.util.HashMap;

import config.FinanceConfiguration;
import config.FinancialPolicy;
import data.finance.budget.Budget;
import data.finance.budget.Expense;
import data.league.LeagueFinance;
import data.player.Player;
import data.team.Team;
import data.team.finance.financialprofil.FinancialProfil;
import process.simulator.tradetools.TradeApplier;
import process.simulator.tradetools.TradeImpact;
import process.simulator.tradetools.TradeValidator;
import process.utilitary.FinanceUtilitary;

public class TradeSimulator {
	
	private TradeValidator tradeValidator = new TradeValidator() ; 
	private TradeApplier tradeApplier = new TradeApplier() ; 
	private TradeImpact tradeImpact = new TradeImpact() ; 
	
	public TradeSimulator() {
		
	}

	public boolean validateTrade(Team teamA, Team teamB, ArrayList<Player> teamAIncoming,
			ArrayList<Player> teamBIncoming, int month) {
		double salaryCap = LeagueFinance.salaryCap;
		double luxuryTaxLine = LeagueFinance.luxuryTaxLine;
		if (!tradeValidator.validateTrade(teamA, teamB, teamAIncoming, teamBIncoming, salaryCap)) {
			return false;
		}
		tradeApplier.applyTrade(teamA, teamAIncoming);
		tradeApplier.applyTrade(teamB, teamBIncoming);
		tradeImpact.applyFinanceImpact(teamA, luxuryTaxLine, month);
		tradeImpact.applyFinanceImpact(teamB, luxuryTaxLine, month);
		return true;
	}


}

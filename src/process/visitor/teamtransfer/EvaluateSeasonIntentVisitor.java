package process.visitor.teamtransfer;

import config.FinanceConfiguration;
import data.player.Player;
import data.team.Team;
import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;
import process.simulator.TradeSimulator;
import process.simulator.tradetools.TradeValidator;

public class EvaluateSeasonIntentVisitor implements TeamTransferVisitor<String> {

	private Team team;
	private double teamPerformatingRate;
	private double salaryCap;

	public EvaluateSeasonIntentVisitor(Team team, double salaryCap) {
		super();
		this.team = team;
		this.salaryCap = salaryCap;
		teamPerformatingRate = team.getTeamPerformance().getPerformanceRating();
	}

	public String visit(AllIn allIn) {		
		if (teamPerformatingRate > 0.70) {
			return FinanceConfiguration.SEASON_TRADE_INTENT_BUYER;
		}
		if (teamPerformatingRate < 0.45) {
			return FinanceConfiguration.SEASON_TRADE_INTENT_SELLER;
		}
		return FinanceConfiguration.SEASON_TRADE_INTENT_STABLE;
	}

	public String visit(SuperstarBuild superstarBuild) {
		if (teamPerformatingRate < 0.5) {
			return FinanceConfiguration.SEASON_TRADE_INTENT_SELLER;
		} else if (teamPerformatingRate < 0.65) {
			return FinanceConfiguration.SEASON_TRADE_INTENT_BUYER;
		} 		
		return FinanceConfiguration.SEASON_TRADE_INTENT_STABLE;

	}

	public String visit(SmallAdjust smallAdjust) {
		if (teamPerformatingRate < 0.40) {
			return FinanceConfiguration.SEASON_TRADE_INTENT_SELLER;
		}

		return FinanceConfiguration.SEASON_TRADE_INTENT_STABLE;
	}

	public String visit(Balanced balanced) {
		if (teamPerformatingRate > 0.70) {
			return FinanceConfiguration.SEASON_TRADE_INTENT_STABLE;
		}
		if (teamPerformatingRate < 0.35) {
			return FinanceConfiguration.SEASON_TRADE_INTENT_SELLER;
		}
		return FinanceConfiguration.SEASON_TRADE_INTENT_BUYER;
	}

	public String visit(Rebuild rebuild) {
		if (teamPerformatingRate > 0.75) {
			return FinanceConfiguration.SEASON_TRADE_INTENT_BUYER;
		}
		return FinanceConfiguration.SEASON_TRADE_INTENT_SELLER;
	}

	public String visit(SalaryDump salaryDump) {
		if (!TradeValidator.respectEconomicPayroll(team.getTeamFinance().getPayroll(), salaryCap)) {
			return FinanceConfiguration.SEASON_TRADE_INTENT_SELLER;
		}
		if (teamPerformatingRate > 0.75) {
			return FinanceConfiguration.SEASON_TRADE_INTENT_STABLE;
		}
		return FinanceConfiguration.SEASON_TRADE_INTENT_SELLER;
	}

}

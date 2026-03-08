package process.visitor.teamtransfer;

import config.FinanceConfiguration;
import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;

public class SeasonTradeSatisfactionVisitor implements TeamTransferVisitor<Boolean> {
	private int transfersMade;
    private String seasonIntent ; 
    
	
	public SeasonTradeSatisfactionVisitor(int transfersMade, String seasonIntent) {
		super();
		this.transfersMade = transfersMade;
		this.seasonIntent = seasonIntent;
	}

	public Boolean visit(AllIn allIn) {
		if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER) && transfersMade < 7) {
            return false;
        }
        return true ; 
	}
	
	public Boolean visit(SuperstarBuild superstarBuild) {
		if(seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER) && transfersMade < 3) {
			return false ; 
		}
		if(seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_BUYER) && transfersMade < 2) {
			return false ; 
		}
		return true ; 
	}
	
	public Boolean visit(SmallAdjust smallAdjust) {
        return transfersMade >= 3;
    }
	
	public Boolean visit(Balanced balanced) {
        if (transfersMade >= 4) {
            return true;
        }
        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER) && transfersMade < 3) {
            return false;
        }
        return true;
    }
	
	public Boolean visit(Rebuild rebuild) {
		return transfersMade >= 6 ; 
	}
	 
	public Boolean visit(SalaryDump salaryDump) {
		return transfersMade >= 5 ;
	}
	
	
	 
	 
	 
	 
	
	
    
    
}

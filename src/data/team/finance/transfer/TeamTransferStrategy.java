package data.team.finance.transfer;

import config.FinanceConfiguration;
import data.player.Player;
import process.visitor.teamtransfer.TeamTransferVisitor;

public abstract class TeamTransferStrategy {
	private String name ;
	private String seasonIntent = FinanceConfiguration.SEASON_TRADE_INTENT_STABLE ; 
	

	public TeamTransferStrategy(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	} 
	
	public String getSeasonIntent() {
		return seasonIntent;
	}

	public void setSeasonIntent(String seasonIntent) {
		this.seasonIntent = seasonIntent;
	}

	public abstract <T> T accept(TeamTransferVisitor<T> visitor);
	
	
	
	
	
}

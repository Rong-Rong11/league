package data.team.finance.transfer;

import data.player.Player;
import process.visitor.teamtransfer.TeamTransferVisitor;

public abstract class TeamTransferStrategy {
	private String name ;
	

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
	
	public abstract <T> T accept(TeamTransferVisitor<T> visitor);
	
	
	
	
	
}

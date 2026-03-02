package data.team.finance.transfer;

import data.player.Player;
import process.visitor.teamtransfer.TeamTransferVisitor;

public class SuperstarBuild extends TeamTransferStrategy {

	public SuperstarBuild(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public <T> T accept(TeamTransferVisitor<T> visitor) {
		return visitor.visit(this) ; 
	}
	
}

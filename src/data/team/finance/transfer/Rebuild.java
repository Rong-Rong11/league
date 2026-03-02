package data.team.finance.transfer;

import data.player.Player;
import process.visitor.teamtransfer.TeamTransferVisitor;

public class Rebuild extends TeamTransferStrategy {

	public Rebuild(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public <T> T accept(TeamTransferVisitor<T> visitor) {
		return visitor.visit(this) ; 
	}
}

package data.finance.transfer;

import java.time.LocalDate;

import data.player.Player;
import data.team.Team;

public class Trade {
	
	private Player playerA ; 
	private Team teamPlayerA ; 
	
	private Player playerB ; 
	private Team teamPlayerB ; 
	private LocalDate dateOfTransfer ;
	public Trade(Player playerA, Team teamPlayerA, Player playerB, Team teamPlayerB, LocalDate dateOfTransfer) {
		super();
		this.playerA = playerA;
		this.teamPlayerA = teamPlayerA;
		this.playerB = playerB;
		this.teamPlayerB = teamPlayerB;
		this.dateOfTransfer = dateOfTransfer;
	}
	public Player getPlayerA() {
		return playerA;
	}
	public void setPlayerA(Player playerA) {
		this.playerA = playerA;
	}
	public Team getTeamPlayerA() {
		return teamPlayerA;
	}
	public void setTeamPlayerA(Team teamPlayerA) {
		this.teamPlayerA = teamPlayerA;
	}
	public Player getPlayerB() {
		return playerB;
	}
	public void setPlayerB(Player playerB) {
		this.playerB = playerB;
	}
	public Team getTeamPlayerB() {
		return teamPlayerB;
	}
	public void setTeamPlayerB(Team teamPlayerB) {
		this.teamPlayerB = teamPlayerB;
	}
	public LocalDate getDateOfTransfer() {
		return dateOfTransfer;
	}
	public void setDateOfTransfer(LocalDate dateOfTransfer) {
		this.dateOfTransfer = dateOfTransfer;
	}
	
	
	
	
	
	
	
}

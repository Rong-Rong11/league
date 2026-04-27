package data.finance.transfer;

import data.player.Player;
import data.team.Team;
import java.time.LocalDate;

public class Trade {
	private Player playerA;
	private Team teamPlayerA;
	private Player playerB;
	private Team teamPlayerB;
	private LocalDate dateOfTransfer;

	public Trade(Player playerA, Team teamPlayerA, Player playerB, Team teamPlayerB, LocalDate dateOfTransfer) {
		this.playerA = playerA;
		this.teamPlayerA = teamPlayerA;
		this.playerB = playerB;
		this.teamPlayerB = teamPlayerB;
		this.dateOfTransfer = dateOfTransfer;
	}

	public Player getPlayerA() {
		return this.playerA;
	}

	public void setPlayerA(Player player) {
		this.playerA = player;
	}

	public Team getTeamPlayerA() {
		return this.teamPlayerA;
	}

	public void setTeamPlayerA(Team team) {
		this.teamPlayerA = team;
	}

	public Player getPlayerB() {
		return this.playerB;
	}

	public void setPlayerB(Player player) {
		this.playerB = player;
	}

	public Team getTeamPlayerB() {
		return this.teamPlayerB;
	}

	public void setTeamPlayerB(Team team) {
		this.teamPlayerB = team;
	}

	public LocalDate getDateOfTransfer() {
		return this.dateOfTransfer;
	}

	public void setDateOfTransfer(LocalDate dateOfTransfer) {
		this.dateOfTransfer = dateOfTransfer;
	}
}

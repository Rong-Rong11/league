/*
	* Decompiled with CFR 0.152.
	*/
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

	public Trade(Player player, Team team, Player player2, Team team2, LocalDate localDate) {
		this.playerA = player;
		this.teamPlayerA = team;
		this.playerB = player2;
		this.teamPlayerB = team2;
		this.dateOfTransfer = localDate;
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

	public void setDateOfTransfer(LocalDate localDate) {
		this.dateOfTransfer = localDate;
	}
}

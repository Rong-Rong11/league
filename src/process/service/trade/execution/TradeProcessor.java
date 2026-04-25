/*
	* Decompiled with CFR 0.152.
	*/
package process.service.trade.execution;

import java.util.ArrayList;

import data.player.Player;
import data.team.Team;

public class TradeProcessor {
	private TradeValidator tradeValidator = new TradeValidator();
	private TradeApplier tradeApplier = new TradeApplier();
	private TradeImpact tradeImpact = new TradeImpact();

	public boolean processTrade(Team teamA, Team teamB, ArrayList<Player> teamAIncoming,
			ArrayList<Player> teamBIncoming, int month, double salaryCap, double luxuryTaxLine) {
		if (!this.tradeValidator.validateTrade(teamA, teamB, teamAIncoming, teamBIncoming, salaryCap)) {
			return false;
		}
		this.tradeApplier.applyTrade(teamA, teamAIncoming);
		this.tradeApplier.applyTrade(teamB, teamBIncoming);
		this.tradeImpact.applyFinanceImpact(teamA, luxuryTaxLine, month);
		this.tradeImpact.applyFinanceImpact(teamB, luxuryTaxLine, month);
		return true;
	}
}

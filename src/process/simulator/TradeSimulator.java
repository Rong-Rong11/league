/*
 * Decompiled with CFR 0.152.
 */
package process.simulator;

import data.player.Player;
import data.team.Team;
import java.util.ArrayList;
import process.simulator.tradetools.TradeApplier;
import process.simulator.tradetools.TradeImpact;
import process.simulator.tradetools.TradeValidator;

public class TradeSimulator {
    private TradeValidator tradeValidator = new TradeValidator();
    private TradeApplier tradeApplier = new TradeApplier();
    private TradeImpact tradeImpact = new TradeImpact();

    public boolean validateTrade(Team teamA, Team teamB, ArrayList<Player> teamAIncoming,
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

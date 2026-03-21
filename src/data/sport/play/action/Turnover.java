/*
 * Decompiled with CFR 0.152.
 */
package data.sport.play.action;

import data.player.Player;
import data.sport.play.action.ActionResult;
import process.visitor.actionresult.ActionResultVisitor;

public class Turnover
extends ActionResult {
    private Player interceptedPlayer;
    private Player defensePlayer;

    public Turnover(String string, Player player, Player player2) {
        super(string);
        this.interceptedPlayer = player;
        this.defensePlayer = player2;
    }

    public Player getInterceptedPlayer() {
        return this.interceptedPlayer;
    }

    public Player getDefensePlayer() {
        return this.defensePlayer;
    }

    @Override
    public <A> A accept(ActionResultVisitor<A> actionResultVisitor) {
        return actionResultVisitor.visit(this);
    }
}

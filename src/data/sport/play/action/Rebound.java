/*
 * Decompiled with CFR 0.152.
 */
package data.sport.play.action;

import data.player.Player;
import data.sport.play.action.ActionResult;
import process.visitor.actionresult.ActionResultVisitor;

public class Rebound
extends ActionResult {
    private Player reboundPlayer;
    private Player missedPlayer;

    public Rebound(String string, Player player, Player player2) {
        super(string);
        this.reboundPlayer = player;
        this.missedPlayer = player2;
    }

    public Player getReboundPlayer() {
        return this.reboundPlayer;
    }

    public void setReboundPlayer(Player player) {
        this.reboundPlayer = player;
    }

    public Player getMissedPlayer() {
        return this.missedPlayer;
    }

    public void setMissedPlayer(Player player) {
        this.missedPlayer = player;
    }

    @Override
    public <A> A accept(ActionResultVisitor<A> actionResultVisitor) {
        return actionResultVisitor.visit(this);
    }
}

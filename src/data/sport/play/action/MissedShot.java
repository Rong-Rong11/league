/*
 * Decompiled with CFR 0.152.
 */
package data.sport.play.action;

import data.player.Player;
import data.sport.play.action.ActionResult;
import process.visitor.actionresult.ActionResultVisitor;

public class MissedShot
extends ActionResult {
    private Player shooter;

    public MissedShot(String string, Player player) {
        super(string);
        this.shooter = player;
    }

    public Player getShooter() {
        return this.shooter;
    }

    @Override
    public <A> A accept(ActionResultVisitor<A> actionResultVisitor) {
        return actionResultVisitor.visit(this);
    }
}

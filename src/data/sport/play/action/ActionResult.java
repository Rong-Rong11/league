/*
 * Decompiled with CFR 0.152.
 */
package data.sport.play.action;

import data.sport.play.OffensiveTry;
import process.visitor.actionresult.ActionResultVisitor;

public abstract class ActionResult {
    private String name;
    private int actionTime;
    private OffensiveTry offensiveTry;

    public ActionResult(String string) {
        this.name = string;
        this.actionTime = 0;
        this.offensiveTry = null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String string) {
        this.name = string;
    }

    public void setActionTime(int n) {
        this.actionTime = n;
    }

    public int getActionTime() {
        return this.actionTime;
    }

    public OffensiveTry getOffensiveAction() {
        return this.offensiveTry;
    }

    public void setOffensiveAction(OffensiveTry offensiveTry) {
        this.offensiveTry = offensiveTry;
    }

    public abstract <A> A accept(ActionResultVisitor<A> var1);
}

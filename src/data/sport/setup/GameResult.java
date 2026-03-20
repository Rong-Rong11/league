/*
 * Decompiled with CFR 0.152.
 */
package data.sport.setup;

import data.sport.play.action.ActionResult;
import data.team.Team;
import java.util.ArrayList;

public class GameResult {
    private Team winner = null;
    private Team loser = null;
    private int scorehomeTeam;
    private int scoreAwayTeam;
    private int twoPointsHomeTeam;
    private int twoPointsAwayTeam;
    private int threePointsHomeTeam;
    private int threePointsAwayTeam;
    private int reboundHomeTeam;
    private int reboundAwayTeam;
    private int turnoverHomeTeam;
    private int turnoverAwayTeam;
    private int blockHomeTeam;
    private int blockAwayTeam;
    private int freeThrowHomeTeam;
    private int freeThrowAwayTeam;
    private ArrayList<ActionResult> actions;

    public GameResult(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12, int n13, int n14) {
        this.scorehomeTeam = n;
        this.scoreAwayTeam = n2;
        this.twoPointsHomeTeam = n3;
        this.twoPointsAwayTeam = n4;
        this.threePointsHomeTeam = n5;
        this.threePointsAwayTeam = n6;
        this.reboundHomeTeam = n7;
        this.reboundAwayTeam = n8;
        this.turnoverHomeTeam = n9;
        this.turnoverAwayTeam = n10;
        this.blockHomeTeam = n11;
        this.blockAwayTeam = n12;
        this.freeThrowHomeTeam = n13;
        this.freeThrowAwayTeam = n14;
        this.actions = new ArrayList();
    }

    public GameResult() {
        this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public void addActions(ActionResult actionResult) {
        this.actions.add(actionResult);
    }

    public int getScorehomeTeam() {
        return this.scorehomeTeam;
    }

    public void setScorehomeTeam(int n) {
        this.scorehomeTeam = n;
    }

    public int getScoreAwayTeam() {
        return this.scoreAwayTeam;
    }

    public void setScoreAwayTeam(int n) {
        this.scoreAwayTeam = n;
    }

    public int getTwoPointsHomeTeam() {
        return this.twoPointsHomeTeam;
    }

    public void setTwoPointsHomeTeam(int n) {
        this.twoPointsHomeTeam = n;
    }

    public int getTwoPointsAwayTeam() {
        return this.twoPointsAwayTeam;
    }

    public void setTwoPointsAwayTeam(int n) {
        this.twoPointsAwayTeam = n;
    }

    public int getThreePointsHomeTeam() {
        return this.threePointsHomeTeam;
    }

    public void setThreePointsHomeTeam(int n) {
        this.threePointsHomeTeam = n;
    }

    public int getThreePointsAwayTeam() {
        return this.threePointsAwayTeam;
    }

    public void setThreePointsAwayTeam(int n) {
        this.threePointsAwayTeam = n;
    }

    public int getReboundHomeTeam() {
        return this.reboundHomeTeam;
    }

    public void setReboundHomeTeam(int n) {
        this.reboundHomeTeam = n;
    }

    public int getReboundAwayTeam() {
        return this.reboundAwayTeam;
    }

    public void setReboundAwayTeam(int n) {
        this.reboundAwayTeam = n;
    }

    public int getTurnoverHomeTeam() {
        return this.turnoverHomeTeam;
    }

    public void setTurnoverHomeTeam(int n) {
        this.turnoverHomeTeam = n;
    }

    public int getTurnoverAwayTeam() {
        return this.turnoverAwayTeam;
    }

    public void setTurnoverAwayTeam(int n) {
        this.turnoverAwayTeam = n;
    }

    public ArrayList<ActionResult> getActions() {
        return this.actions;
    }

    public void setActions(ArrayList<ActionResult> arrayList) {
        this.actions = arrayList;
    }

    public int getBlockHomeTeam() {
        return this.blockHomeTeam;
    }

    public void setBlockHomeTeam(int n) {
        this.blockHomeTeam = n;
    }

    public int getBlockAwayTeam() {
        return this.blockAwayTeam;
    }

    public void setBlockAwayTeam(int n) {
        this.blockAwayTeam = n;
    }

    public int getFreeThrowHomeTeam() {
        return this.freeThrowHomeTeam;
    }

    public void setFreeThrowHomeTeam(int n) {
        this.freeThrowHomeTeam = n;
    }

    public int getFreeThrowAwayTeam() {
        return this.freeThrowAwayTeam;
    }

    public void setFreeThrowAwayTeam(int n) {
        this.freeThrowAwayTeam = n;
    }
}

/*
 * Decompiled with CFR 0.152.
 */
package data.sport.setup;

import data.sport.setup.GameContext;
import data.sport.setup.GameResult;

public class Game {
    private GameContext gameContext;
    private GameResult[] quarterResults;
    private int homeFinalScore;
    private int awayFinalScore;
    private boolean displayed;

    public Game(GameContext gameContext) {
        this.gameContext = gameContext;
        this.quarterResults = new GameResult[4];
        this.homeFinalScore = 0;
        this.awayFinalScore = 0;
        this.displayed = false;
    }

    public GameContext getGameContext() {
        return this.gameContext;
    }

    public void setGameContext(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public GameResult[] getQuarterResults() {
        return this.quarterResults;
    }

    public void setQuarterResults(GameResult[] gameResultArray) {
        this.quarterResults = gameResultArray;
    }

    public int getHomeFinalScore() {
        return this.homeFinalScore;
    }

    public void setHomeFinalScore(int n) {
        this.homeFinalScore = n;
    }

    public int getAwayFinalScore() {
        return this.awayFinalScore;
    }

    public void setAwayFinalScore(int n) {
        this.awayFinalScore = n;
    }

    public boolean isDisplayed() {
        return this.displayed;
    }

    public void setDisplayed(boolean bl) {
        this.displayed = bl;
    }
}

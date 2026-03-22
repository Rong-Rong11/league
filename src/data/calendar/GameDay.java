/*
 * Decompiled with CFR 0.152.
 */
package data.calendar;

import java.time.LocalDate;
import java.util.ArrayList;

import data.sport.setup.Game;

public class GameDay {
    private ArrayList<Game> games = new ArrayList();
    private LocalDate date;
    private boolean isSimulated;
    private boolean isDisplayed;

    public GameDay(LocalDate localDate) {
        this.date = localDate;
        this.isSimulated = false;
        this.isDisplayed = false;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public boolean isEmpty() {
        return this.games.size() == 0;
    }

    public ArrayList<Game> getGames() {
        return this.games;
    }

    public void setGames(ArrayList<Game> arrayList) {
        this.games = arrayList;
    }

    public boolean isSimulated() {
        return this.isSimulated;
    }

    public void setSimulated(boolean bl) {
        this.isSimulated = bl;
    }

    public boolean isDisplayed() {
        return this.isDisplayed;
    }

    public void setDisplayed(boolean bl) {
        this.isDisplayed = bl;
    }

    public void addGame(Game game) {
        games.add(game);
    }
}

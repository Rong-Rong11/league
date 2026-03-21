/*
 * Decompiled with CFR 0.152.
 */
package data.team.calendar;

import data.sport.setup.Game;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.TreeMap;

public class Schedule {
    private int numberOfPlayedGames;
    private int numberOfAwayGames;
    private int numberOfHomeGames;
    private ArrayList<Game> games;
    private TreeMap<LocalDate, Game> scheduledGames;
    private TreeMap<LocalDate, Game> playedGames;

    public Schedule() {
        this.setNumberOfAwayGames(0);
        this.setNumberOfHomeGames(0);
        this.setNumberOfPlayedGames(0);
        this.games = new ArrayList();
        this.scheduledGames = new TreeMap();
        this.playedGames = new TreeMap();
    }

    public int getNumberOfPlayedGames() {
        return this.numberOfPlayedGames;
    }

    public void setNumberOfPlayedGames(int n) {
        this.numberOfPlayedGames = n;
    }

    public int getNumberOfAwayGames() {
        return this.numberOfAwayGames;
    }

    public void setNumberOfAwayGames(int n) {
        this.numberOfAwayGames = n;
    }

    public int getNumberOfHomeGames() {
        return this.numberOfHomeGames;
    }

    public void setNumberOfHomeGames(int n) {
        this.numberOfHomeGames = n;
    }

    public ArrayList<Game> getGames() {
        return this.games;
    }

    public void setGames(ArrayList<Game> arrayList) {
        this.games = arrayList;
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    public void incrementNumberOfAwayGames() {
        ++this.numberOfAwayGames;
    }

    public void incrementNumberOfHomeGames() {
        ++this.numberOfHomeGames;
    }

    public boolean isPlayingOn(LocalDate localDate) {
        return this.scheduledGames.containsKey(localDate);
    }

    public int daysSinceLastGame(LocalDate localDate) {
        if (this.playedGames.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        LocalDate localDate2 = this.playedGames.lowerKey(localDate);
        return (int)ChronoUnit.DAYS.between(localDate2, localDate);
    }

    public void scheduleGame(LocalDate localDate, Game game) {
        this.scheduledGames.put(localDate, game);
    }

    public void clearGames() {
        this.games.clear();
    }

    public void clearScheduledGames() {
        this.scheduledGames.clear();
    }

    public TreeMap<LocalDate, Game> getScheduledGames() {
        return this.scheduledGames;
    }
}

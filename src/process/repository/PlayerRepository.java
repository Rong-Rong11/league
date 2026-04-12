/*
 * Decompiled with CFR 0.152.
 */
package process.repository;

import java.util.ArrayList;
import java.util.HashMap;

import data.player.Player;

public class PlayerRepository {
    private HashMap<String, Player> players = new HashMap<>();
    private static PlayerRepository instance = new PlayerRepository();

    private PlayerRepository() {
    }

    public static PlayerRepository getInstance() {
        return instance;
    }

    public void register(String string, Player player) {
        this.players.put(string, player);
    }

    public Player getPlayer(String string) {
        if (this.players.containsKey(string)) {
            return this.players.get(string);
        }
        return null;
    }

    public ArrayList<Player> getAllPlayers() {
        return new ArrayList<Player>(this.players.values());
    }

    public void clear() {
        this.players.clear();
    }
}

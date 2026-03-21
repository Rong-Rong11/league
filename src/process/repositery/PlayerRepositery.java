/*
 * Decompiled with CFR 0.152.
 */
package process.repositery;

import data.player.Player;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerRepositery {
    private HashMap<String, Player> players = new HashMap();
    private static PlayerRepositery instance = new PlayerRepositery();

    private PlayerRepositery() {
    }

    public static PlayerRepositery getInstance() {
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

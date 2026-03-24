/*
 * Decompiled with CFR 0.152.
 */
package process.simulator.tradetools;

import java.util.ArrayList;
import java.util.HashMap;

import data.player.Player;
import data.team.Team;
import process.utilitary.FinanceUtilitary;

public class TradeApplier {
    public void applyTrade(Team team, ArrayList<Player> arrayList) {
        HashMap<String, Player> hashMap = team.getCurrentPlayers();
        HashMap<String, Player> hashMap2 = new HashMap<String, Player>();
        for (Player player : arrayList) {
            if (!hashMap.containsKey(player.getName())) {
                player.setTransfered(true);
            }
            hashMap2.put(player.getName(), player);
        }
        team.setCurrentPlayers(hashMap2);
        TradeApplier.updateStarPlayer(team, arrayList);
        FinanceUtilitary.updateTeamPayroll(team);
        team.getTeamFinance().incrementTransferMade();
    }

    public static void updateStarPlayer(Team team, ArrayList<Player> arrayList) {
        for (Player player : arrayList) {
            if (!player.isStar())
                continue;
            team.setStarPlayer(player);
            return;
        }
        team.setStarPlayer(null);
    }
}

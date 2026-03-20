/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.teamtransfer;

import java.util.TreeMap;

import data.player.Player;
import data.team.Team;
import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;
import process.utilitary.PlayerUtilitary;

public class PreSeasonPlayerToTradeVisitor
        implements TeamTransferVisitor<Player> {
    private Team team;

    public PreSeasonPlayerToTradeVisitor(Team team) {
        this.team = team;
    }

    @Override
    public Player visit(AllIn allIn) {
        TreeMap<Double, Player> treeMap = new TreeMap<Double, Player>();
        for (Player object : this.team.getCurrentPlayers().values()) {
            treeMap.put(PlayerUtilitary.getPlayerOverAllNote(object), object);
        }
        int n = 0;
        for (Double d : treeMap.descendingKeySet()) {
            if (n < 3) {
                ++n;
                continue;
            }
            Player player = (Player) treeMap.get(d);
            if (player.isTransfered())
                continue;
            return player;
        }
        return (Player) treeMap.get(treeMap.lastKey());
    }

    @Override
    public Player visit(SuperstarBuild superstarBuild) {
        TreeMap<Double, Player> treeMap = new TreeMap<Double, Player>();
        for (Player object : this.team.getCurrentPlayers().values()) {
            treeMap.put(PlayerUtilitary.getPlayerOverAllNote(object), object);
        }
        int n = 0;
        for (Double d : treeMap.descendingKeySet()) {
            if (n < 1) {
                ++n;
                continue;
            }
            Player player = (Player) treeMap.get(d);
            if (player.isTransfered())
                continue;
            return player;
        }
        return (Player) treeMap.get(treeMap.lastKey());
    }

    @Override
    public Player visit(SmallAdjust smallAdjust) {
        TreeMap<Double, Player> treeMap = new TreeMap<Double, Player>();
        for (Player object : this.team.getCurrentPlayers().values()) {
            treeMap.put(PlayerUtilitary.getPlayerOverAllNote(object), object);
        }
        int n = 0;
        for (Double d : treeMap.keySet()) {
            Player player = (Player) treeMap.get(d);
            if (n == 0) {
                ++n;
                continue;
            }
            if (player.isTransfered())
                continue;
            return player;
        }
        return (Player) treeMap.get(treeMap.firstKey());
    }

    @Override
    public Player visit(Balanced balanced) {
        TreeMap<Double, Player> treeMap = new TreeMap<Double, Player>();
        for (Player player : this.team.getCurrentPlayers().values()) {
            treeMap.put(PlayerUtilitary.getPlayerOverAllNote(player), player);
        }
        int n = treeMap.size() / 2;
        int n2 = n + 3;
        int n3 = 0;
        for (Double d : treeMap.keySet()) {
            Player player = (Player) treeMap.get(d);
            if (player.isTransfered())
                continue;
            if (n3 >= n && n3 < n2) {
                return player;
            }
            ++n3;
        }
        return (Player) treeMap.get(treeMap.firstKey());
    }

    @Override
    public Player visit(Rebuild rebuild) {
        TreeMap<Double, Player> treeMap = new TreeMap<Double, Player>();
        for (Player object : this.team.getCurrentPlayers().values()) {
            treeMap.put(PlayerUtilitary.getPlayerOverAllNote(object), object);
        }
        for (Double d : treeMap.descendingKeySet()) {
            Player player = (Player) treeMap.get(d);
            if (player.isTransfered())
                continue;
            return player;
        }
        return (Player) treeMap.get(treeMap.lastKey());
    }

    @Override
    public Player visit(SalaryDump salaryDump) {
        TreeMap<Double, Player> treeMap = new TreeMap<Double, Player>();
        for (Player object : this.team.getCurrentPlayers().values()) {
            treeMap.put(object.getSalary(), object);
        }
        for (Double d : treeMap.descendingKeySet()) {
            Player player = (Player) treeMap.get(d);
            if (player.isTransfered())
                continue;
            return player;
        }
        return (Player) treeMap.get(treeMap.lastKey());
    }
}

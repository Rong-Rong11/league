package process.visitor.teamtransfer;

import java.util.TreeMap;

import config.FinanceConfiguration;
import data.player.Player;
import data.team.Team;
import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.Balanced;
import data.team.finance.transfer.Rebuild;
import data.team.finance.transfer.SalaryDump;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;
import process.utility.PlayerUtilitary;

public class SeasonPlayerToTradeVisitor implements TeamTransferVisitor<Player> {
    private Team team;
    private String seasonIntent;
    private double salaryCap;

    public SeasonPlayerToTradeVisitor(Team team, String seasonIntent, double salaryCap) {
        super();
        this.team = team;
        this.seasonIntent = seasonIntent;
        this.salaryCap = salaryCap;
    }

    private TreeMap<Double, Player> getPlayersSortedByOverall() {
        TreeMap<Double, Player> sorted = new TreeMap<>();
        for (Player p : team.getCurrentPlayers().values()) {
            sorted.put(PlayerUtilitary.getPlayerOverAllNote(p), p);
        }
        return sorted;
    }

    public Player visit(AllIn allIn) {
        TreeMap<Double, Player> sorted = getPlayersSortedByOverall();
        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_BUYER)) {
            int skipTop = 4;
            int count = 0;

            for (Double key : sorted.descendingKeySet()) {
                if (count < skipTop) {
                    count++;
                    continue;
                }
                Player player = sorted.get(key);
                if (!player.isTransfered()) {
                    return player;
                }
            }
        }
        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER)) {
            for (Double key : sorted.descendingKeySet()) {
                Player player = sorted.get(key);
                if (!player.isTransfered() && !player.isStar() && !player.getHealthStatus().isInjured()) {
                    if (player.getSalary() > salaryCap * 0.1) {
                        return player;
                    }
                }
            }
            for (Double key : sorted.keySet()) {
                Player player = sorted.get(key);
                if (!player.isTransfered() && !player.isStar()) {
                    return player;
                }
            }
        }
        return null;
    }

    public Player visit(SuperstarBuild superstarBuild) {
        TreeMap<Double, Player> sorted = getPlayersSortedByOverall();
        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER)) {
            for (Double key : sorted.keySet()) {
                Player player = sorted.get(key);
                if (!player.isTransfered() && !player.isStar()) {

                    if (player.getHealthStatus().isInjured() || player.getSalary() > salaryCap * 0.1) {
                        return player;
                    }
                }
            }

            for (Double key : sorted.keySet()) {
                Player player = sorted.get(key);
                if (!player.isTransfered() && !player.isStar()) {
                    return player;
                }
            }
        }

        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_BUYER)) {
            int skipTop = 1;
            int count = 0;
            for (Double key : sorted.descendingKeySet()) {
                if (count < skipTop) {
                    count++;
                    continue;
                }
                Player player = sorted.get(key);
                if (!player.isTransfered() && !player.isStar())
                    return player;
            }
        }
        return null;
    }

    public Player visit(SmallAdjust smallAdjust) {
        if (!seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER)) {
            return null;
        }
        TreeMap<Double, Player> sorted = getPlayersSortedByOverall();
        int size = sorted.size();
        int start = size / 3;
        int end = size * (2 / 3);
        int count = 0;
        for (Double key : sorted.keySet()) {
            if (count >= start && count <= end) {
                Player player = sorted.get(key);
                if (!player.isTransfered())
                    return player;
            }
            count++;
        }

        return null;
    }

    public Player visit(Balanced balanced) {
        TreeMap<Double, Player> sorted = getPlayersSortedByOverall();
        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER)) {
            int skipTop = 2;
            int count = 0;
            for (Double key : sorted.descendingKeySet()) {
                if (count < skipTop) {
                    count++;
                    continue;
                }
                Player player = sorted.get(key);
                if (!player.isTransfered()) {
                    return player;
                }
            }
        }
        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_BUYER)) {
            int size = sorted.size();
            int start = size / 3;
            int end = size * (2 / 3);
            int count = 0;
            for (Double key : sorted.keySet()) {
                if (count >= start && count <= end) {
                    Player player = sorted.get(key);
                    if (!player.isTransfered()) {
                        return player;
                    }
                }
                count++;
            }
        }
        return null;
    }

    public Player visit(Rebuild rebuild) {
        TreeMap<Double, Player> sorted = getPlayersSortedByOverall();
        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER)) {
            int skipTop = 1;
            int count = 0;
            for (Double key : sorted.descendingKeySet()) {
                if (count < skipTop) {
                    count++;
                    continue;
                }
                Player player = sorted.get(key);
                if (!player.isTransfered()) {
                    return player;
                }
            }
        }
        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_BUYER)) {
            int size = sorted.size();
            int start = size / 2;
            int count = 0;
            for (Double key : sorted.keySet()) {
                if (count >= start) {
                    Player player = sorted.get(key);
                    if (!player.isTransfered()) {
                        return player;
                    }
                }
                count++;
            }
        }
        return null;
    }

    public Player visit(SalaryDump salaryDump) {
        Player candidate = null;
        double maxSalary = 0;
        for (Player player : team.getCurrentPlayers().values()) {
            if (player.isTransfered()) {
                continue;
            }
            if (player.getSalary() > maxSalary) {
                maxSalary = player.getSalary();
                candidate = player;
            }
        }
        return candidate;
    }
}

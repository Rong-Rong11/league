package process.visitor.teamtransfer;

import java.time.LocalDate;
import java.util.TreeMap;

import config.FinanceConfiguration;
import data.player.Player;
import data.team.Team;
import data.team.finance.transfer.AllIn;
import data.team.finance.transfer.SmallAdjust;
import data.team.finance.transfer.SuperstarBuild;
import process.utilitary.PlayerUtilitary;

public class SeasonPlayerToTradeVisitor implements TeamTransferVisitor<Player> {
    private Team team;
    private double performance;
    private String seasonIntent;
    private LocalDate currentDate;
    private LocalDate deadLine;

    public SeasonPlayerToTradeVisitor(Team team, double performance, String seasonIntent, LocalDate currentDate,
            LocalDate deadLine) {
        super();
        this.team = team;
        this.performance = performance;
        this.seasonIntent = seasonIntent;
        this.currentDate = currentDate;
        this.deadLine = deadLine;
    }

    private TreeMap<Double, Player> getPlayersSortedByOverall() {
        TreeMap<Double, Player> sorted = new TreeMap<>();
        for (Player p : team.getPlayers().values()) {
            sorted.put(PlayerUtilitary.getPlayerOverAllNote(p), p);
        }
        return sorted;
    }

    public Player visit(AllIn allIn) {
        TreeMap<Double, Player> sorted = getPlayersSortedByOverall();
        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_BUYER)) {
            int skipTop = 2;
            int count = 0;

            for (Double key : sorted.descendingKeySet()) {
                if (count < skipTop) {
                    count++;
                    continue;
                }

                Player p = sorted.get(key);
                if (!p.isTransfered()) {
                    return p;
                }
            }
        }
        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER)) {
            for (Double key : sorted.descendingKeySet()) {
                Player p = sorted.get(key);
                if (!p.isTransfered()) {
                    return p;
                }
            }
        }
        return null;
    }

    public Player visit(SuperstarBuild superstarBuild) {
        TreeMap<Double, Player> sorted = getPlayersSortedByOverall();
        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_BUYER)) {
            int count = 0;
            for (Double key : sorted.descendingKeySet()) {
                if (count < 1) {
                    count++;
                    continue;
                }
                Player p = sorted.get(key);
                if (!p.isTransfered())
                    return p;
            }
        }

        if (seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_SELLER)) {
            return sorted.lastEntry().getValue();
        }
        return null;
    }

    public Player visit(SmallAdjust smallAdjust) {
        if (!seasonIntent.equals(FinanceConfiguration.SEASON_TRADE_INTENT_BUYER)) {
            return null;
        }
        TreeMap<Double, Player> sorted = getPlayersSortedByOverall();
        int mid = sorted.size() / 2;
        int count = 0;
        for (Double key : sorted.keySet()) {
            if (count == mid) {
                Player p = sorted.get(key);
                if (!p.isTransfered())
                    return p;
            }
            count++;
        }

        return null;
    }

}

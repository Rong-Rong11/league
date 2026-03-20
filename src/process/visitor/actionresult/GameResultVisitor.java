/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.actionresult;

import data.player.Player;
import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.MissedShot;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;
import data.sport.setup.GameResult;
import java.util.ArrayList;
import process.visitor.actionresult.ActionResultVisitor;

public class GameResultVisitor
implements ActionResultVisitor<Void> {
    private GameResult gameResult;
    private ArrayList<Player> homeTeamPlayers;
    private ArrayList<Player> awayTeamPlayers;

    public GameResultVisitor(GameResult gameResult, ArrayList<Player> arrayList, ArrayList<Player> arrayList2) {
        this.gameResult = gameResult;
        this.homeTeamPlayers = arrayList;
        this.awayTeamPlayers = arrayList2;
    }

    @Override
    public Void visit(PointScored pointScored) {
        Player player = pointScored.getScorerPlayer();
        boolean bl = this.homeTeamPlayers.contains(player);
        if (pointScored.getOffensiveAction().getName().equals("threepoint")) {
            if (bl) {
                this.gameResult.setThreePointsHomeTeam(this.gameResult.getThreePointsHomeTeam() + 1);
                this.gameResult.setScorehomeTeam(this.gameResult.getScorehomeTeam() + 3);
            } else {
                this.gameResult.setThreePointsAwayTeam(this.gameResult.getThreePointsAwayTeam() + 1);
                this.gameResult.setScoreAwayTeam(this.gameResult.getScoreAwayTeam() + 3);
            }
        } else if (pointScored.getOffensiveAction().getName().equals("twopoint")) {
            if (bl) {
                this.gameResult.setTwoPointsHomeTeam(this.gameResult.getTwoPointsHomeTeam() + 1);
                this.gameResult.setScorehomeTeam(this.gameResult.getScorehomeTeam() + 2);
            } else {
                this.gameResult.setTwoPointsAwayTeam(this.gameResult.getTwoPointsAwayTeam() + 1);
                this.gameResult.setScoreAwayTeam(this.gameResult.getScoreAwayTeam() + 2);
            }
        } else if (bl) {
            this.gameResult.setFreeThrowHomeTeam(this.gameResult.getFreeThrowHomeTeam() + 1);
            this.gameResult.setScorehomeTeam(this.gameResult.getScorehomeTeam() + 1);
        } else {
            this.gameResult.setFreeThrowAwayTeam(this.gameResult.getFreeThrowAwayTeam() + 1);
            this.gameResult.setScoreAwayTeam(this.gameResult.getScoreAwayTeam() + 1);
        }
        return null;
    }

    @Override
    public Void visit(MissedShot missedShot) {
        return null;
    }

    @Override
    public Void visit(Turnover turnover) {
        Player player = turnover.getDefensePlayer();
        if (this.homeTeamPlayers.contains(player)) {
            this.gameResult.setTurnoverHomeTeam(this.gameResult.getTurnoverHomeTeam() + 1);
        } else {
            this.gameResult.setTurnoverAwayTeam(this.gameResult.getTurnoverAwayTeam() + 1);
        }
        return null;
    }

    @Override
    public Void visit(Block block) {
        Player player = block.getBlockingPlayer();
        if (this.homeTeamPlayers.contains(player)) {
            this.gameResult.setBlockHomeTeam(this.gameResult.getBlockHomeTeam() + 1);
        } else {
            this.gameResult.setBlockAwayTeam(this.gameResult.getBlockAwayTeam() + 1);
        }
        return null;
    }

    @Override
    public Void visit(Rebound rebound) {
        Player player = rebound.getReboundPlayer();
        if (this.homeTeamPlayers.contains(player)) {
            this.gameResult.setReboundHomeTeam(this.gameResult.getReboundHomeTeam() + 1);
        } else {
            this.gameResult.setReboundAwayTeam(this.gameResult.getReboundAwayTeam() + 1);
        }
        return null;
    }

    @Override
    public Void visit(EndOfTime endOfTime) {
        return null;
    }
}

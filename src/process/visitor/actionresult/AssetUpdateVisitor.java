/*
 * Decompiled with CFR 0.152.
 */
package process.visitor.actionresult;

import data.player.Asset;
import data.player.Player;
import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.MissedShot;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;
import java.util.HashMap;
import process.visitor.actionresult.ActionResultVisitor;

public class AssetUpdateVisitor
implements ActionResultVisitor<Void> {
    private HashMap<Player, Asset> playersNewAssets;

    public AssetUpdateVisitor(HashMap<Player, Asset> hashMap) {
        this.playersNewAssets = hashMap;
    }

    @Override
    public Void visit(PointScored pointScored) {
        Player player = pointScored.getScorerPlayer();
        this.incrementShootingAttempt(player, pointScored.getOffensiveAction().getName());
        this.playersNewAssets.get(player).setPointPerMatch(this.playersNewAssets.get(player).getPointPerMatch() + (double)pointScored.getPointsScored());
        Player player2 = pointScored.getAssistPlayer();
        if (player2 != null) {
            this.playersNewAssets.get(player2).setAssistPerMatch(this.playersNewAssets.get(player2).getAssistPerMatch() + 1.0);
        }
        return null;
    }

    @Override
    public Void visit(MissedShot missedShot) {
        this.incrementShootingAttempt(missedShot.getShooter(), missedShot.getOffensiveAction().getName());
        return null;
    }

    @Override
    public Void visit(Turnover turnover) {
        Player player = turnover.getInterceptedPlayer();
        this.playersNewAssets.get(player).setLostBallPerMatch(this.playersNewAssets.get(player).getLostBallPerMatch() + 1.0);
        Player player2 = turnover.getDefensePlayer();
        this.playersNewAssets.get(player2).setInterceptionPerMatch(this.playersNewAssets.get(player2).getInterceptionPerMatch() + 1.0);
        return null;
    }

    @Override
    public Void visit(Block block) {
        Player player = block.getBlockingPlayer();
        this.playersNewAssets.get(player).setBlockPerMatch(this.playersNewAssets.get(player).getBlockPerMatch() + 1.0);
        return null;
    }

    @Override
    public Void visit(Rebound rebound) {
        Player player = rebound.getReboundPlayer();
        this.playersNewAssets.get(player).setReboundPerMatch(this.playersNewAssets.get(player).getReboundPerMatch() + 1.0);
        return null;
    }

    @Override
    public Void visit(EndOfTime endOfTime) {
        return null;
    }

    private void incrementShootingAttempt(Player player, String string) {
        Asset asset = this.playersNewAssets.get(player);
        if ("threepoint".equals(string)) {
            asset.setThreePointAttemptPerMatch(asset.getThreePointAttemptPerMatch() + 1.0);
        } else if ("twopoint".equals(string)) {
            asset.setTwoPointAttemptPerMatch(asset.getTwoPointAttemptPerMatch() + 1.0);
        } else if ("fouldraw".equals(string)) {
            asset.setFreeThrowAttemptPerMatch(asset.getFreeThrowAttemptPerMatch() + 1.0);
        }
    }
}

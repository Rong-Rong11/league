package process.visitor.actionresult;

import java.util.HashMap;

import data.player.Asset;
import data.player.Player;
import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.MissedShot;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;

public class AssetUpdateVisitor implements ActionResultVisitor<Void> {
    private HashMap<Player, Asset> playersNewAssets;

    public AssetUpdateVisitor(HashMap<Player, Asset> playersNewAssets) {
        this.playersNewAssets = playersNewAssets;
    }

    @Override
    public Void visit(PointScored pointScored) {
        Player scorer = pointScored.getScorerPlayer();
        playersNewAssets.get(scorer)
                .setPointPerMatch(playersNewAssets.get(scorer).getPointPerMatch()
                        + pointScored.getPointsScored());

        Player assist = pointScored.getAssistPlayer();
        if (assist != null) {
            playersNewAssets.get(assist)
                    .setAssistPerMatch(playersNewAssets.get(assist).getAssistPerMatch() + 1);
        }
        return null;
    }

    @Override
    public Void visit(MissedShot missedShot) {
        return null;
    }

    @Override
    public Void visit(Turnover turnover) {
        Player intercepted = turnover.getInterceptedPlayer();
        playersNewAssets.get(intercepted)
                .setLostBallPerMatch(playersNewAssets.get(intercepted).getLostBallPerMatch() + 1);

        Player defender = turnover.getDefensePlayer();
        playersNewAssets.get(defender)
                .setInterceptionPerMatch(playersNewAssets.get(defender).getInterceptionPerMatch() + 1);
        return null;
    }

    @Override
    public Void visit(Block block) {
        Player blocker = block.getBlockingPlayer();
        playersNewAssets.get(blocker)
                .setBlockPerMatch(playersNewAssets.get(blocker).getBlockPerMatch() + 1);
        return null;
    }

    @Override
    public Void visit(Rebound rebound) {
        Player rebounder = rebound.getReboundPlayer();
        playersNewAssets.get(rebounder)
                .setReboundPerMatch(playersNewAssets.get(rebounder).getReboundPerMatch() + 1);
        return null;
    }

    @Override
    public Void visit(EndOfTime endOfTime) {
        return null;
    }
}

package process.visitor.actionresult;

import java.util.ArrayList;

import config.SimulationConfiguration;
import data.player.Player;
import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;
import data.sport.setup.GameResult;

public class GameResultVisitor implements ActionResultVisitor<Void> {
    private GameResult gameResult;
    private ArrayList<Player> homeTeamPlayers;
    private ArrayList<Player> awayTeamPlayers;

    public GameResultVisitor(GameResult gameResult, ArrayList<Player> homeTeamPlayers,
            ArrayList<Player> awayTeamPlayers) {
        this.gameResult = gameResult;
        this.homeTeamPlayers = homeTeamPlayers;
        this.awayTeamPlayers = awayTeamPlayers;
    }

    @Override
    public Void visit(PointScored pointScored) {
        Player scorer = pointScored.getScorerPlayer();
        boolean home = homeTeamPlayers.contains(scorer);

        if (pointScored.getOffensiveAction().getName().equals(SimulationConfiguration.THREEPOINT)) {
            if (home) {
                gameResult.setThreePointsHomeTeam(gameResult.getThreePointsHomeTeam() + 1);
                gameResult.setScorehomeTeam(gameResult.getScorehomeTeam() + 3);
            } else {
                gameResult.setThreePointsAwayTeam(gameResult.getThreePointsAwayTeam() + 1);
                gameResult.setScoreAwayTeam(gameResult.getScoreAwayTeam() + 3);
            }
        } else if (pointScored.getOffensiveAction().getName().equals(SimulationConfiguration.TWOPOINT)) {
            if (home) {
                gameResult.setTwoPointsHomeTeam(gameResult.getTwoPointsHomeTeam() + 1);
                gameResult.setScorehomeTeam(gameResult.getScorehomeTeam() + 2);
            } else {
                gameResult.setTwoPointsAwayTeam(gameResult.getTwoPointsAwayTeam() + 1);
                gameResult.setScoreAwayTeam(gameResult.getScoreAwayTeam() + 2);
            }
        } else {
            if (home) {
                gameResult.setFreeThrowHomeTeam(gameResult.getFreeThrowHomeTeam() + 1);
                gameResult.setScorehomeTeam(gameResult.getScorehomeTeam() + 1);
            } else {
                gameResult.setFreeThrowAwayTeam(gameResult.getFreeThrowAwayTeam() + 1);
                gameResult.setScoreAwayTeam(gameResult.getScoreAwayTeam() + 1);
            }
        }
        return null;
    }

    @Override
    public Void visit(Turnover turnover) {
        Player defensePlayer = turnover.getDefensePlayer();
        if (homeTeamPlayers.contains(defensePlayer)) {
            gameResult.setTurnoverHomeTeam(gameResult.getTurnoverHomeTeam() + 1);
        } else {
            gameResult.setTurnoverAwayTeam(gameResult.getTurnoverAwayTeam() + 1);
        }
        return null;
    }

    @Override
    public Void visit(Block block) {
        Player blockerPlayer = block.getBlockingPlayer();
        if (homeTeamPlayers.contains(blockerPlayer)) {
            gameResult.setBlockHomeTeam(gameResult.getBlockHomeTeam() + 1);
        } else {
            gameResult.setBlockAwayTeam(gameResult.getBlockAwayTeam() + 1);
        }
        return null;
    }

    @Override
    public Void visit(Rebound rebound) {
        Player reboundPlayer = rebound.getReboundPlayer();
        if (homeTeamPlayers.contains(reboundPlayer)) {
            gameResult.setReboundHomeTeam(gameResult.getReboundHomeTeam() + 1);
        } else {
            gameResult.setReboundAwayTeam(gameResult.getReboundAwayTeam() + 1);
        }
        return null;
    }

    @Override
    public Void visit(EndOfTime end) {
        return null;
    }

}

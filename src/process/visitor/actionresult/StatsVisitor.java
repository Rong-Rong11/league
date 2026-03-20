package process.visitor.actionresult;

import data.player.Player;
import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.MissedShot;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;
import data.sport.setup.Game;
import gui.management.LiveMatchStatistics;
import java.util.HashMap;

public class StatsVisitor
        implements ActionResultVisitor<Void> {
    private LiveMatchStatistics liveMatchStatistics;

    public StatsVisitor(LiveMatchStatistics liveMatchStatistics) {
        this.liveMatchStatistics = liveMatchStatistics;
    }

    @Override
    public Void visit(PointScored pointScored) {
        Player player = pointScored.getScorerPlayer();
        boolean bl = this.isHomePlayer(player, this.liveMatchStatistics.getGame());
        String string = pointScored.getOffensiveAction() == null ? "" : pointScored.getOffensiveAction().getName();
        int n = 1;
        if ("threepoint".equals(string)) {
            n = 3;
        } else if ("twopoint".equals(string)) {
            n = 2;
        }
        if (bl) {
            this.liveMatchStatistics.setHomePoints(this.liveMatchStatistics.getHomePoints() + n);
            this.liveMatchStatistics.getHomePlayerPoints().put(player.getName(),
                    this.getPlayerPoints(this.liveMatchStatistics.getHomePlayerPoints(), player.getName()) + n);
            this.liveMatchStatistics.getHomePlayers().put(player.getName(), player);
        } else {
            this.liveMatchStatistics.setAwayPoints(this.liveMatchStatistics.getAwayPoints() + n);
            this.liveMatchStatistics.getAwayPlayerPoints().put(player.getName(),
                    this.getPlayerPoints(this.liveMatchStatistics.getAwayPlayerPoints(), player.getName()) + n);
            this.liveMatchStatistics.getAwayPlayers().put(player.getName(), player);
        }
        if ("threepoint".equals(string)) {
            if (bl) {
                this.liveMatchStatistics.setHomeThreeMade(this.liveMatchStatistics.getHomeThreeMade() + 1);
                this.liveMatchStatistics.setHomeThreeAttempts(this.liveMatchStatistics.getHomeThreeAttempts() + 1);
                this.liveMatchStatistics.setHomeFgAttempts(this.liveMatchStatistics.getHomeFgAttempts() + 1);
            } else {
                this.liveMatchStatistics.setAwayThreeMade(this.liveMatchStatistics.getAwayThreeMade() + 1);
                this.liveMatchStatistics.setAwayThreeAttempts(this.liveMatchStatistics.getAwayThreeAttempts() + 1);
                this.liveMatchStatistics.setAwayFgAttempts(this.liveMatchStatistics.getAwayFgAttempts() + 1);
            }
        } else if ("twopoint".equals(string)) {
            if (bl) {
                this.liveMatchStatistics.setHomeTwoMade(this.liveMatchStatistics.getHomeTwoMade() + 1);
                this.liveMatchStatistics.setHomeFgAttempts(this.liveMatchStatistics.getHomeFgAttempts() + 1);
            } else {
                this.liveMatchStatistics.setAwayTwoMade(this.liveMatchStatistics.getAwayTwoMade() + 1);
                this.liveMatchStatistics.setAwayFgAttempts(this.liveMatchStatistics.getAwayFgAttempts() + 1);
            }
        }
        Player player2 = pointScored.getAssistPlayer();
        if (player2 != null) {
            if (this.isHomePlayer(player2, this.liveMatchStatistics.getGame())) {
                this.liveMatchStatistics.setHomeAssists(this.liveMatchStatistics.getHomeAssists() + 1);
            } else {
                this.liveMatchStatistics.setAwayAssists(this.liveMatchStatistics.getAwayAssists() + 1);
            }
        }
        return null;
    }

    @Override
    public Void visit(MissedShot missedShot) {
        String string;
        Player player = missedShot.getShooter();
        boolean bl = this.isHomePlayer(player, this.liveMatchStatistics.getGame());
        String string2 = string = missedShot.getOffensiveAction() == null ? ""
                : missedShot.getOffensiveAction().getName();
        if ("threepoint".equals(string)) {
            if (bl) {
                this.liveMatchStatistics.setHomeThreeAttempts(this.liveMatchStatistics.getHomeThreeAttempts() + 1);
                this.liveMatchStatistics.setHomeFgAttempts(this.liveMatchStatistics.getHomeFgAttempts() + 1);
            } else {
                this.liveMatchStatistics.setAwayThreeAttempts(this.liveMatchStatistics.getAwayThreeAttempts() + 1);
                this.liveMatchStatistics.setAwayFgAttempts(this.liveMatchStatistics.getAwayFgAttempts() + 1);
            }
        } else if ("twopoint".equals(string)) {
            if (bl) {
                this.liveMatchStatistics.setHomeFgAttempts(this.liveMatchStatistics.getHomeFgAttempts() + 1);
            } else {
                this.liveMatchStatistics.setAwayFgAttempts(this.liveMatchStatistics.getAwayFgAttempts() + 1);
            }
        }
        return null;
    }

    @Override
    public Void visit(Turnover turnover) {
        if (this.isHomePlayer(turnover.getDefensePlayer(), this.liveMatchStatistics.getGame())) {
            this.liveMatchStatistics.setHomeTurnovers(this.liveMatchStatistics.getHomeTurnovers() + 1);
        } else {
            this.liveMatchStatistics.setAwayTurnovers(this.liveMatchStatistics.getAwayTurnovers() + 1);
        }
        return null;
    }

    @Override
    public Void visit(Block block) {
        return null;
    }

    @Override
    public Void visit(Rebound rebound) {
        if (this.isHomePlayer(rebound.getReboundPlayer(), this.liveMatchStatistics.getGame())) {
            this.liveMatchStatistics.setHomeRebounds(this.liveMatchStatistics.getHomeRebounds() + 1);
        } else {
            this.liveMatchStatistics.setAwayRebounds(this.liveMatchStatistics.getAwayRebounds() + 1);
        }
        return null;
    }

    @Override
    public Void visit(EndOfTime endOfTime) {
        return null;
    }

    private int getPlayerPoints(HashMap<String, Integer> hashMap, String string) {
        Integer n = hashMap.get(string);
        return n == null ? 0 : n;
    }

    private boolean isHomePlayer(Player player, Game game) {
        return game.getGameContext().getHomeTeam().getPlayers().containsKey(player.getName());
    }
}

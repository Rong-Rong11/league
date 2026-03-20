package process.utilitary;

import data.player.Player;
import data.team.Team;
import data.team.finance.financialprofil.AmbitiousProfil;
import data.team.finance.financialprofil.BalancedProfil;
import data.team.finance.financialprofil.FinancialProfil;
import data.team.finance.financialprofil.ThriftyProfil;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public class TeamUtilitary {
    private static double getTeamAttackNote(Team team) {
        double sumOfNote = 0.0;
        double numberOfPlayer = 0.0;
        for (Player player : team.getPlayers().values()) {
            sumOfNote += PlayerUtilitary.getPlayerAttackNote(player);
            numberOfPlayer += 1.0;
        }
        double note = sumOfNote / numberOfPlayer;
        return note;
    }

    private static double getTeamDefenseNote(Team team) {
        double sumOfNote = 0.0;
        double numberOfPlayer = 0.0;
        for (Player player : team.getPlayers().values()) {
            sumOfNote += PlayerUtilitary.getPlayerDefenseNote(player);
            numberOfPlayer += 1.0;
        }
        double note = sumOfNote / numberOfPlayer;
        return Math.min(note, 3.0);
    }

    public static String getTeamSportProfile(Team team) {
        double attackNote = TeamUtilitary.getTeamAttackNote(team);
        double defenseNote = TeamUtilitary.getTeamDefenseNote(team);
        if (defenseNote <= 0.0) {
            defenseNote = 1.0;
        }
        if (attackNote / defenseNote > 1.1) {
            return "offensive";
        }
        if (attackNote / defenseNote < 0.9) {
            return "defensive";
        }
        return "balanced";
    }

    public static void setStarPlayer(Team team) {
        for (Player player : team.getPlayers().values()) {
            if (!player.isStar())
                continue;
            team.setStarPlayer(player);
            return;
        }
        team.setStarPlayer(null);
    }

    public static void updatePerformanceRating(Team team, Team opponent, int result, int scoreDifference,
            double opponentPopularity) {
        double opponentRating = (TeamUtilitary.getTeamAttackNote(opponent) + TeamUtilitary.getTeamDefenseNote(opponent))
                / 2.0;
        double performanceRating = team.getTeamPerformance().getPerformanceRating();
        double resultBonus = 0.0;
        resultBonus = result == 1 ? 0.2 : (result == -1 ? -0.1 : 0.05);
        double marginBonus = Math.min(0.1, (double) scoreDifference / 50.0);
        double opponentFactor = opponentRating > 0.6 ? 1.2 : 0.8;
        double gameImpact = (resultBonus + marginBonus) * opponentFactor;
        performanceRating = performanceRating * 0.85 + gameImpact * 0.15;
        performanceRating = Math.max(0.0, Math.min(1.0, performanceRating));
        team.getTeamPerformance().setPerformanceRating(performanceRating);
    }

    public static FinancialProfil randomFinancialProfil() {
        double random = Math.random();
        if (random < 0.3) {
            return new AmbitiousProfil();
        }
        if (random < 0.6) {
            return new ThriftyProfil();
        }
        return new BalancedProfil();
    }

    public static MarketSize randomMarketSize() {
        double random = Math.random();
        if (random < 0.25) {
            return new LargeSize();
        }
        if (random < 0.75) {
            return new MediumSize();
        }
        return new SmallSize();
    }
}

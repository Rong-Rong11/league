package process.utility;

import config.GameConfiguration;
import data.league.Conference;
import data.league.Division;
import data.league.League;
import data.player.Player;
import data.team.Team;
import data.team.TeamPerformance;
import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.financialpolicy.ThriftyPolicy;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public class TeamUtilitary {

    private static double getTeamAttackNote(Team team) {
        double sumOfNote = 0;
        double numberOfPlayer = 0;
        double note;
        for (Player player : team.getCurrentPlayers().values()) {
            sumOfNote += PlayerUtilitary.getPlayerAttackNote(player);
            numberOfPlayer++;
        }
        note = sumOfNote / numberOfPlayer;
        return note;

    }

    private static double getTeamDefenseNote(Team team) {
        double sumOfNote = 0;
        double numberOfPlayer = 0;
        double note;
        for (Player player : team.getCurrentPlayers().values()) {
            sumOfNote += PlayerUtilitary.getPlayerDefenseNote(player);
            numberOfPlayer++;
        }
        note = sumOfNote / numberOfPlayer;
        return Math.min(note, 3);
    }

    public static String getTeamSportProfile(Team team) {
        double attackNote = getTeamAttackNote(team);
        double defenseNote = getTeamDefenseNote(team);
        if (defenseNote <= 0)
            defenseNote = 1;
        if ((attackNote / defenseNote) > 1.1) {
            return GameConfiguration.TEAM_OFFENSIVE_MATCH_PROFIL;
        } else if ((attackNote / defenseNote) < 0.9) {
            return GameConfiguration.TEAM_DEFENSIVE_MATCH_PROFIL;
        } else {
            return GameConfiguration.TEAM_BALANCED_MATCH_PROFIL;
        }
    }

    public static void setStarPlayer(Team team) {
        for (Player player : team.getCurrentPlayers().values()) {
            if (player.isStar()) {
                team.setStarPlayer(player);
                return;
            }
        }
        team.setStarPlayer(null);
    }

    public static void updatePerformanceRating(Team team, Team opponent, int result, int scoreDifference,
            double opponentPopularity) {
        TeamPerformance teamPerformance = team.getTeamPerformance();
        double opponentRating = (getTeamAttackNote(opponent) + getTeamDefenseNote(opponent)) / 2;

        double performanceRating = teamPerformance.getPerformanceRating();
        double resultBonus = 0;
        if (result == 1) {
            resultBonus = 0.2;
        } else if (result == -1) {
            resultBonus = -0.1;
        } else {
            resultBonus = 0.05;
        }
        double marginBonus = Math.min(0.1, scoreDifference / 50.0);
        double opponentFactor = (opponentRating > 0.6) ? 1.2 : 0.8;
        double gameImpact = (resultBonus + marginBonus) * opponentFactor;

        performanceRating = (performanceRating * 0.85) + (gameImpact * 0.15);
        performanceRating = Math.max(0, Math.min(1, performanceRating));
        teamPerformance.setPerformanceRating(performanceRating);
    }

    public static FinancialPolicy randomFinancialProfil() {
        double random = Math.random();
        if (random < 0.3) {
            return new AmbitiousPolicy();
        }
        if (random < 0.6) {
            return new ThriftyPolicy();
        }
        return new BalancedPolicy();
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

    public static void updateStreak(Team team, boolean win) {
        if (win) {
            handleWin(team);
        } else {
            handleLose(team);
        }
    }

    private static void handleWin(Team team) {
        TeamPerformance teamPerformance = team.getTeamPerformance();

        int currentLoseStreak = teamPerformance.getCurrentLoseStreak();
        if (currentLoseStreak > teamPerformance.getMaxLoseStreak()) {
            teamPerformance.setMaxLoseStreak(currentLoseStreak);
        }

        teamPerformance.setCurrentLoseStreak(0);
        teamPerformance.incrementCurrentWinStreak();
    }

    private static void handleLose(Team team) {
        TeamPerformance teamPerformance = team.getTeamPerformance();
        int currentWinStreak = teamPerformance.getCurrentWinStreak();
        if (currentWinStreak > teamPerformance.getMaxWinsStreak()) {
            teamPerformance.setMaxWinsStreak(currentWinStreak);
        }
        teamPerformance.setCurrentWinStreak(0);
        teamPerformance.incrementCurrentLoseStreak();
    }

    public static Conference getConferenceOfTeam(League league, Team team) {
        Conference easternConference = league.getEasternConference();
        for (Division division : easternConference.getDivisions().values()) {
            for (Team divisionTeam : division.getTeams().values()) {
                if (divisionTeam.equals(team)) {
                    return easternConference;
                }
            }
        }

        Conference westernConference = league.getWesternConference();
        for (Division division : westernConference.getDivisions().values()) {
            for (Team divisionTeam : division.getTeams().values()) {
                if (divisionTeam.equals(team)) {
                    return westernConference;
                }
            }
        }

        return null;
    }

    public static Division getDivisionOfTeam(League league, Team team) {
        Conference easternConference = league.getEasternConference();
        Conference westernConference = league.getWesternConference();

        for (Division division : easternConference.getDivisions().values()) {
            for (Team divisionTeam : division.getTeams().values()) {
                if (divisionTeam.equals(team)) {
                    return division;
                }
            }
        }

        for (Division division : westernConference.getDivisions().values()) {
            for (Team divisionTeam : division.getTeams().values()) {
                if (divisionTeam.equals(team)) {
                    return division;
                }
            }
        }

        return null;
    }

}

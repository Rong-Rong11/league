package process.service.financetools;

import java.util.ArrayList;
import java.util.List;

import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import process.repositery.TeamRepositery;
import process.utilitary.FinanceUtilitary;

public class MonthlyCentralRevenueCalculator {

    private TeamRepositery teamRepositery = TeamRepositery.getInstance();

    public double calculateNationalTvRevenue() {
        ArrayList<Team> teams = teamRepositery.getAllTeams();
        int teamCount = teams.size();

        double averagePopularity = calculateAveragePopularity(teams);
        double averagePerformance = calculateAveragePerformance(teams);
        double averagePrestige = calculateAverageHistoricalPrestige(teams);
        double averageTeamValue = calculateAverageTeamValue(teams);
        int starTeams = countTeamsWithStarPlayer(teams);

        return (0.15 * teamCount)
                + (averagePopularity * 0.025)
                + (averagePerformance * 1.8)
                + (averagePrestige * 1.2)
                + (averageTeamValue * 0.9)
                + (starTeams * 0.04);
    }

    public double calculateNationalSponsoringRevenue() {
        ArrayList<Team> teams = teamRepositery.getAllTeams();
        int teamCount = teams.size();

        double averagePopularity = calculateAveragePopularity(teams);
        double averageCommercialAggressiveness = calculateAverageCommercialAggressiveness(teams);
        double averageBusinessOpportunity = calculateAverageBusinessOpportunity(teams);
        double averageTeamValue = calculateAverageTeamValue(teams);
        int starTeams = countTeamsWithStarPlayer(teams);

        return (0.08 * teamCount)
                + (averagePopularity * 0.02)
                + (averageCommercialAggressiveness * 1.1)
                + (averageBusinessOpportunity * 0.8)
                + (averageTeamValue * 0.7)
                + (starTeams * 0.06);
    }

    public double calculateNationalMerchandisingRevenue() {
        ArrayList<Team> teams = teamRepositery.getAllTeams();
        int teamCount = teams.size();

        double averagePopularity = calculateAveragePopularity(teams);
        double averageFanLoyalty = calculateAverageFanLoyalty(teams);
        double averagePrestige = calculateAverageHistoricalPrestige(teams);
        double averageTeamValue = calculateAverageTeamValue(teams);
        int starTeams = countTeamsWithStarPlayer(teams);

        return (0.05 * teamCount)
                + (averagePopularity * 0.015)
                + (averageFanLoyalty * 0.9)
                + (averagePrestige * 0.7)
                + (averageTeamValue * 0.5)
                + (starTeams * 0.05);
    }

    private double calculateAveragePopularity(List<Team> teams) {
        double total = 0.0;
        for (Team team : teams) {
            total += team.getCurrentPopularity();
        }
        return total / teams.size();
    }

    private double calculateAveragePerformance(List<Team> teams) {
        double total = 0.0;
        for (Team team : teams) {
            total += team.getTeamPerformance().getPerformanceRating();
        }
        return total / teams.size();
    }

    private double calculateAverageHistoricalPrestige(List<Team> teams) {
        double total = 0.0;
        for (Team team : teams) {
            EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
            total += profil.getHistoricalPrestige();

        }
        return total / teams.size();
    }

    private double calculateAverageFanLoyalty(List<Team> teams) {
        double total = 0.0;
        for (Team team : teams) {
            EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
            total += profil.getFanLoyalty();
        }
        return total / teams.size();
    }

    private double calculateAverageCommercialAggressiveness(List<Team> teams) {
        double total = 0.0;
        for (Team team : teams) {
            EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
            total += profil.getCommercialAggressiveness();

        }
        return total / teams.size();
    }

    private double calculateAverageBusinessOpportunity(List<Team> teams) {
        double total = 0.0;
        for (Team team : teams) {
            MediaMarket mediaMarket = team.getTeamFinance().getMediaMarket();
            total += mediaMarket.getBusinessOpportunityModifier();
        }
        return total / teams.size();
    }

    private double calculateAverageTeamValue(List<Team> teams) {
        double total = 0.0;
        for (Team team : teams) {
            total += FinanceUtilitary.getNormalizedTeamValue(team);
        }
        return total / teams.size();
    }

    private int countTeamsWithStarPlayer(List<Team> teams) {
        int count = 0;
        for (Team team : teams) {
            if (team.getStarPlayer() != null) {
                count++;
            }
        }
        return count;
    }
}

package process.service.finance.distribution;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.league.League;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import process.repository.TeamRepository;
import process.service.finance.FinanceManager;
import process.utility.FinanceUtility;

public class CentralRevenueDistributor {
    private static final String TV_SHARE_TYPE = "tv";
    private static final String SPONSORING_SHARE_TYPE = "sponsoring";
    private static final String MERCHANDISING_SHARE_TYPE = "merchandising";

    private League league;
    private TeamRepository teamRepositery = TeamRepository.getInstance();
    private MonthlyCentralRevenueCalculator monthlyCentralRevenueCalculator;

    public CentralRevenueDistributor(League league) {
        this.league = league;
        monthlyCentralRevenueCalculator = new MonthlyCentralRevenueCalculator(league);
    }

    public void setFinanceManager(FinanceManager financeManager) {
        monthlyCentralRevenueCalculator.setFinanceManager(financeManager);
    }

    public void distributeMonthlyCentralRevenue(int month) {
        CentralRevenueProfile revenueProfile = getRevenueProfile(month);
        double tvRevenue = monthlyCentralRevenueCalculator.calculateNationalTvRevenue(revenueProfile, month);
        double globalSponsors = monthlyCentralRevenueCalculator.calculateNationalSponsoringRevenue(revenueProfile,
                month);
        double merchandisingRevenue = monthlyCentralRevenueCalculator
                .calculateNationalMerchandisingRevenue(revenueProfile, month);
        distribute(tvRevenue, globalSponsors, merchandisingRevenue, month);
    }

    private CentralRevenueProfile getRevenueProfile(int month) {
        if (isPlayoffMonth(month)) {
            return new CentralRevenueProfile(
                    FinanceConfiguration.PLAYOFF_CENTRAL_TV_RATE,
                    FinanceConfiguration.PLAYOFF_CENTRAL_SPONSORING_RATE,
                    FinanceConfiguration.PLAYOFF_CENTRAL_MERCH_RATE);
        }
        return new CentralRevenueProfile(
                FinanceConfiguration.REGULAR_SEASON_CENTRAL_TV_RATE,
                FinanceConfiguration.REGULAR_SEASON_CENTRAL_SPONSORING_RATE,
                FinanceConfiguration.REGULAR_SEASON_CENTRAL_MERCH_RATE);
    }

    private void distribute(double tvRevenue, double globalSponsors, double merchandisingRevenue, int month) {
        Budget leagueBudget = league.getLeagueFinance().getBudget();

        double distributableTv = retainLeagueCut(
                leagueBudget,
                tvRevenue,
                IncomeType.NATIONAL_TV,
                month);

        double distributableSponsors = retainLeagueCut(
                leagueBudget,
                globalSponsors,
                IncomeType.NATIONAL_SPONSORING,
                month);

        double distributableMerchandising = retainLeagueCut(
                leagueBudget,
                merchandisingRevenue,
                IncomeType.NATIONAL_MERCHANDISING,
                month);

        distributeTvShare(distributableTv, month);
        distributeNationalSponsoringShare(distributableSponsors, month);
        distributeMerchandisingShare(distributableMerchandising, month);

        FinanceUtility.updateBudget(leagueBudget);
    }

    private double retainLeagueCut(Budget leagueBudget, double revenue, IncomeType incomeType, int month) {
        double leagueCut = revenue * FinanceConfiguration.LEAGUE_OPERATING_RATE;
        FinanceUtility.addIncome(leagueBudget, new Income(incomeType, leagueCut), month);
        return revenue - leagueCut;
    }

    private void distributeEqualShare(double amount, IncomeType incomeType, int month) {
        double share = amount / teamRepositery.getAllTeams().size();

        for (Team team : teamRepositery.getAllTeams()) {
            Budget budget = team.getTeamFinance().getBudget();
            FinanceUtility.addIncome(budget, new Income(incomeType, share), month);
            FinanceUtility.updateBudget(budget);
        }
    }

    private void distributeTvShare(double tvRevenue, int month) {
        double equalPart = tvRevenue * 0.90;
        double weightedPart = tvRevenue * 0.10;
        distributeEqualShare(equalPart, IncomeType.CENTRAL_SHARE, month);
        distributeWeightedShare(weightedPart, month, TV_SHARE_TYPE);
    }

    private void distributeNationalSponsoringShare(double sponsoringRevenue, int month) {
        double equalPart = sponsoringRevenue * 0.80;
        double weightedPart = sponsoringRevenue * 0.20;
        distributeEqualShare(equalPart, IncomeType.CENTRAL_SHARE, month);
        distributeWeightedShare(weightedPart, month, SPONSORING_SHARE_TYPE);
    }

    private void distributeMerchandisingShare(double merchandisingRevenue, int month) {
        double equalPart = merchandisingRevenue * 0.7;
        double weightedPart = merchandisingRevenue * 0.3;
        distributeEqualShare(equalPart, IncomeType.CENTRAL_SHARE, month);
        distributeWeightedShare(weightedPart, month, MERCHANDISING_SHARE_TYPE);
    }

    private void distributeWeightedShare(double weightedPart, int month, String shareType) {
        double totalScore = 0.0;
        for (Team team : teamRepositery.getAllTeams()) {
            totalScore += calculateShareScore(team, shareType);
        }

        if (totalScore <= 0) {
            distributeEqualShare(weightedPart, IncomeType.CENTRAL_SHARE, month);
            return;
        }

        for (Team team : teamRepositery.getAllTeams()) {
            double score = calculateShareScore(team, shareType);
            double share = weightedPart * (score / totalScore);

            Budget budget = team.getTeamFinance().getBudget();
            FinanceUtility.addIncome(budget, new Income(IncomeType.CENTRAL_SHARE, share),
                    month);
            FinanceUtility.updateBudget(budget);
        }
    }

    private double calculateShareScore(Team team, String shareType) {
        if (TV_SHARE_TYPE.equals(shareType)) {
            return calculateTvShareScore(team);
        }

        if (SPONSORING_SHARE_TYPE.equals(shareType)) {
            return calculateSponsoringShareScore(team);
        }

        return FinanceUtility.calculateMerchandisingScore(team);
    }

    private double calculateTvShareScore(Team team) {
        EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
        double score = 1.0;
        score += team.getCurrentPopularity() / 250.0;
        score += profil.getHistoricalPrestige() * 0.8;
        score += FinanceUtility.getNormalizedTeamValue(team) * 0.7;

        if (team.hasStarPlayer()) {
            score += 0.5;
        }

        return score;
    }

    private double calculateSponsoringShareScore(Team team) {
        EconomicProfil profil = team.getTeamFinance().getEconomicProfil();
        double score = 1.0;
        score += team.getCurrentPopularity() / 200.0;
        score += profil.getCommercialAggressiveness() * 0.8;
        score += profil.getHistoricalPrestige() * 0.5;
        score += FinanceUtility.getNormalizedTeamValue(team) * 0.5;

        if (team.hasStarPlayer()) {
            score += 0.4;
        }

        return score;
    }

    private boolean isPlayoffMonth(int month) {
        return month >= 8;
    }

}

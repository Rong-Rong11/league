package process.service.finance;

import data.finance.budget.Budget;
import data.finance.budget.expense.Expense;
import data.finance.budget.expense.ExpenseType;
import data.finance.budget.income.Income;
import data.finance.budget.income.IncomeType;
import data.league.League;
import data.league.finance.LeagueRedistributionPolicy;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import process.repository.TeamRepository;
import process.utility.FinanceUtility;
import process.visitor.marketsize.CalculateMonthlyTeamFinanceVisitor;

public class RevenueSharingManager {
    private TeamRepository teamRepositery = TeamRepository.getInstance();
    private League league;

    public RevenueSharingManager(League league) {
        this.league = league;
    }

    public void applyRevenueSharing(int month) {
        LeagueRedistributionPolicy leagueRedistributionPolicy = league.getLeagueFinance()
                .getLeagueRedistributionPolicy();
        double leagueAverage = calculateLeagueLocalAverage(month);
        double redistributionRate = calculateEffectiveRedistributionRate(leagueRedistributionPolicy, month);

        double pool = collectFromRichTeams(leagueAverage, redistributionRate, month);

        double leagueKeeps = pool * leagueRedistributionPolicy.getBaseLeagueRetentionRate();
        FinanceUtility.addIncome(
                league.getLeagueFinance().getBudget(),
                new Income(IncomeType.LEAGUE_KEEPS, leagueKeeps),
                month);

        double remainingPool = pool - leagueKeeps;
        double equalSharePool = remainingPool * leagueRedistributionPolicy.getBaseEqualShareRate();
        double weightedSharePool = remainingPool * leagueRedistributionPolicy.getBaseWeightedShareRate();

        distributeEqualShare(equalSharePool, month);
        distributeToSmallTeams(leagueAverage, weightedSharePool, month);
    }

    private double calculateLeagueLocalAverage(int month) {
        double total = 0.0;

        for (Team team : teamRepositery.getAllTeams()) {
            total += calculateAdjustedLocalRevenue(team, month);
        }

        return total / teamRepositery.getAllTeams().size();
    }

    private double collectFromRichTeams(double leagueAverage, double redistributionRate, int month) {
        double pool = 0.0;

        for (Team team : teamRepositery.getAllTeams()) {
            double localRevenue = getRegularSeasonRevenueBase(team, month);
            double contextFactor = calculateRevenueContextFactor(team);
            double adjustedLocalRevenue = calculateAdjustedLocalRevenue(team, month);
            Budget budget = team.getTeamFinance().getBudget();

            if (adjustedLocalRevenue > leagueAverage) {
                double expectedRevenueAtLeagueAverage = leagueAverage * contextFactor;
                double excess = localRevenue - expectedRevenueAtLeagueAverage;

                if (excess > 0) {
                    double contribution = excess * redistributionRate;

                    FinanceUtility.addExpense(
                            budget,
                            new Expense(ExpenseType.REVENUE_SHARING_CONTRIBUTION,
                                    contribution),
                            month);

                    FinanceUtility.updateBudget(budget);
                    pool += contribution;
                }
            }
        }

        return pool;
    }

    private void distributeEqualShare(double pool, int month) {
        if (pool <= 0) {
            return;
        }
        int teamCount = teamRepositery.getAllTeams().size();
        double share = pool / teamCount;
        for (Team team : teamRepositery.getAllTeams()) {
            Budget budget = team.getTeamFinance().getBudget();
            FinanceUtility.addIncome(
                    budget,
                    new Income(IncomeType.EQUAL_SHARE, share),
                    month);
            FinanceUtility.updateBudget(budget);
        }
    }

    private void distributeToSmallTeams(double leagueAverage, double pool, int month) {
        if (pool <= 0) {
            return;
        }
        double totalNeed = 0;
        for (Team team : teamRepositery.getAllTeams()) {
            double adjustedLocalRevenue = calculateAdjustedLocalRevenue(team, month);
            if (adjustedLocalRevenue < leagueAverage) {
                totalNeed += (leagueAverage - adjustedLocalRevenue);
            }
        }
        if (totalNeed <= 0) {
            return;
        }
        for (Team team : teamRepositery.getAllTeams()) {
            double adjustedLocalRevenue = calculateAdjustedLocalRevenue(team, month);
            Budget budget = team.getTeamFinance().getBudget();

            if (adjustedLocalRevenue < leagueAverage) {
                double need = leagueAverage - adjustedLocalRevenue;
                double share = (need / totalNeed) * pool;

                FinanceUtility.addIncome(
                        budget,
                        new Income(IncomeType.EQUAL_SHARE, share),
                        month);

                FinanceUtility.updateBudget(budget);
            }
        }
    }

    private double calculateEffectiveRedistributionRate(LeagueRedistributionPolicy leagueRedistributionPolicy,
            int month) {
        double rate = leagueRedistributionPolicy.getBaseRedistributionRate();

        double leagueAveragePopularity = calculateLeagueAveragePopularity();
        double inequality = calculateRevenueInequality(month);

        if (leagueAveragePopularity < 65) {
            rate += 0.03;
        }

        if (inequality > 0.30) {
            rate += 0.05;
        }

        if (inequality < 0.15) {
            rate -= 0.03;
        }

        return Math.max(
                leagueRedistributionPolicy.getMinimumRedistributionRate(),
                Math.min(leagueRedistributionPolicy.getMaximumRedistributionRate(), rate));
    }

    private double calculateLeagueAveragePopularity() {
        double total = 0.0;

        for (Team team : teamRepositery.getAllTeams()) {
            total += team.getCurrentPopularity();
        }

        return total / teamRepositery.getAllTeams().size();
    }

    private double calculateRevenueInequality(int month) {
        double minRevenue = Double.MAX_VALUE;
        double maxRevenue = Double.MIN_VALUE;

        for (Team team : teamRepositery.getAllTeams()) {
            double adjustedLocalRevenue = calculateAdjustedLocalRevenue(team, month);
            if (adjustedLocalRevenue < minRevenue) {
                minRevenue = adjustedLocalRevenue;
            }
            if (adjustedLocalRevenue > maxRevenue) {
                maxRevenue = adjustedLocalRevenue;
            }
        }
        if (maxRevenue <= 0) {
            return 0.0;
        }
        return (maxRevenue - minRevenue) / maxRevenue;
    }

    private double calculateAdjustedLocalRevenue(Team team, int month) {
        double localRevenue = getRegularSeasonRevenueBase(team, month);
        double contextFactor = calculateRevenueContextFactor(team);

        if (contextFactor <= 0) {
            return localRevenue;
        }

        return localRevenue / contextFactor;
    }

    private double getRegularSeasonRevenueBase(Team team, int month) {
        return FinanceUtility.getTeamIncomeOfMonthForRegularSeason(team, month)
                + FinanceUtility.getTeamIncomeOfMonthForBoth(team, month);
    }

    private double calculateRevenueContextFactor(Team team) {
        double factor = 0.75;
        double valueFactor = FinanceUtility.getNormalizedTeamValue(team);

        MarketSize marketSize = team.getTeamFinance().getMarketSize();
        MediaMarket mediaMarket = team.getTeamFinance().getMediaMarket();
        EconomicProfil economicProfil = team.getTeamFinance().getEconomicProfil();

        factor *= getMarketMultiplier(marketSize);

        double mediaFactor = 0.0;
        double ecomonicFactor = 0.0;

        mediaFactor += mediaMarket.getBusinessOpportunityModifier() * 0.20;
        mediaFactor += mediaMarket.getPrestigeModifier() * 0.10;
        mediaFactor += mediaMarket.getFanBaseModifier() * 0.10;
        mediaFactor += mediaMarket.getPricingPowerModifier() * 0.10;

        ecomonicFactor += economicProfil.getFanLoyalty() * 0.15;
        ecomonicFactor += economicProfil.getCommercialAggressiveness() * 0.15;
        ecomonicFactor += economicProfil.getHistoricalPrestige() * 0.10;

        double combinedFactor = 1 + (mediaFactor * 0.6 + ecomonicFactor * 0.4);
        factor *= combinedFactor;
        factor *= (1 + valueFactor * 0.12);

        return Math.max(0.75, factor);
    }

    private double getMarketMultiplier(MarketSize marketSize) {
        if (marketSize == null) {
            return 1.0;
        }
        return marketSize.accept(new CalculateMonthlyTeamFinanceVisitor());
    }
}

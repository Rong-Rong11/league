package process.manager.financetools;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.finance.budget.Expense;
import data.finance.budget.Income;
import data.team.Team;
import data.team.finance.TeamFinance;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import process.utilitary.FinanceUtilitary;
import process.visitor.financialprofil.StaffCostMultiplierVisitor;
import process.visitor.marketsize.CalculateMonthlyTeamFinanceVisitor;

public class MonthlyTeamFinanceCalculator {
        public void applyMonthlyFinance(Team team, int month) {
                Budget budget = team.getTeamFinance().getBudget();
                TeamFinance teamFinance = team.getTeamFinance();
                MarketSize marketSize = teamFinance.getMarketSize();
                MediaMarket mediaMarket = teamFinance.getMediaMarket();
                EconomicProfil economicProfil = teamFinance.getEconomicProfil();
                FinancialPolicy financialPolicy = teamFinance.getFinancialProfil();

                double marketMultiplier = getMarketMultiplier(marketSize);
                double popularityFactor = team.getPopularity() / 100.0;
                double starFactor = 1;
                if (team.hasStarPlayer()) {
                        starFactor = 1.1;
                }
                double performanceFactor = 0.90 + (team.getTeamPerformance().getPerformanceRating() * 0.20);

                double localSponsoring = 1.20 * marketMultiplier * popularityFactor * starFactor;
                double localMerchandising = 0.75 * marketMultiplier * popularityFactor * starFactor;
                double otherRevenue = 0.25 * marketMultiplier * performanceFactor;

                localSponsoring *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.35);
                localMerchandising *= (1 + mediaMarket.getPrestigeModifier() * 0.20);
                otherRevenue *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.10);

                localSponsoring *= (1 + economicProfil.getCommercialAggressiveness() * 0.30);
                localSponsoring *= (1 + economicProfil.getHistoricalPrestige() * 0.15);

                localMerchandising *= (1 + economicProfil.getFanLoyalty() * 0.25);
                localMerchandising *= (1 + economicProfil.getHistoricalPrestige() * 0.20);

                otherRevenue *= (1 + economicProfil.getOwnerDeficitTolerance() * 0.08);

                double monthlyPayroll = team.getTeamFinance().getPayroll()
                                / FinanceConfiguration.NUMBER_OF_FINANCIAL_MONTHS;
                double stadiumMaintenance = calculateStadiumMaintenance(team, marketMultiplier, mediaMarket,
                                economicProfil);
                double staffCost = calculateStaffCost(team, marketMultiplier, economicProfil, financialPolicy);
                double administrativeCost = calculateAdministrativeCost(marketMultiplier, mediaMarket, economicProfil);

                FinanceUtilitary.addIncome(budget,
                                new Income(FinanceConfiguration.INCOME_TYPE_LOCAL_SPONSORING, localSponsoring), month);
                FinanceUtilitary.addIncome(budget,
                                new Income(FinanceConfiguration.INCOME_TYPE_LOCAL_MERCHANDISING, localMerchandising),
                                month);
                FinanceUtilitary.addIncome(budget, new Income(FinanceConfiguration.INCOME_TYPE_OTHER, otherRevenue),
                                month);

                FinanceUtilitary.addExpense(budget,
                                new Expense(FinanceConfiguration.EXPENSE_TYPE_PLAYER_SALARY, monthlyPayroll), month);
                FinanceUtilitary.addExpense(budget,
                                new Expense(FinanceConfiguration.EXPENSE_TYPE_STADIUM_COST, stadiumMaintenance), month);
                FinanceUtilitary.addExpense(budget,
                                new Expense(FinanceConfiguration.EXPENSE_TYPE_STAFF_COST, staffCost),
                                month);
                FinanceUtilitary.addExpense(budget,
                                new Expense(FinanceConfiguration.EXPENSE_TYPE_ADMINISTRATIVE_COST, administrativeCost),
                                month);

                FinanceUtilitary.updateBudget(budget);
        }

        private double calculateStadiumMaintenance(Team team, double marketMultiplier, MediaMarket mediaMarket,
                        EconomicProfil economicProfil) {
                double capacityFactor = team.getStadium().getCapacity() / 20000.0;
                double maintenance = 0.22 * marketMultiplier * capacityFactor;

                maintenance *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.10);
                maintenance *= (1 + economicProfil.getHistoricalPrestige() * 0.05);

                return maintenance;
        }

        private double calculateStaffCost(Team team, double marketMultiplier, EconomicProfil economicProfil,
                        FinancialPolicy financialPolicy) {
                int numberOfPlayers = team.getPlayers().size();
                double popularityFactor = 0.80 + (team.getPopularity() / 500.0);
                double staffCost = ((0.015 * numberOfPlayers) + 0.10) * marketMultiplier * popularityFactor;

                staffCost *= (1 + economicProfil.getFanLoyalty() * 0.08);
                staffCost *= (1 + economicProfil.getCommercialAggressiveness() * 0.05);

                return staffCost * financialPolicy.accept(new StaffCostMultiplierVisitor());
        }

        private double calculateAdministrativeCost(double marketMultiplier, MediaMarket mediaMarket,
                        EconomicProfil economicProfil) {
                double administrativeCost = 0.18 * marketMultiplier;

                administrativeCost *= (1 + mediaMarket.getBusinessOpportunityModifier() * 0.10);
                administrativeCost *= (1 + economicProfil.getCommercialAggressiveness() * 0.10);

                return administrativeCost;
        }

        private double getMarketMultiplier(MarketSize marketSize) {
                return marketSize.accept(new CalculateMonthlyTeamFinanceVisitor());
        }
}

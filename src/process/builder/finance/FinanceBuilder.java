package process.builder.finance;

import config.FinanceConfiguration;
import data.finance.budget.Budget;
import data.team.Stadium;
import data.team.Team;
import data.team.finance.TeamFinance;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.financialpolicy.FinancialPolicy;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.mediamarket.MediaMarket;
import process.factory.EconomicProfileFactory;
import process.repository.TeamRepository;
import process.utility.FinanceUtility;
import process.visitor.marketsize.CalculateBaseTicketVisitor;
import process.visitor.marketsize.CalculateInitialTeamBudgetVisitor;
import process.visitor.marketsize.CalculateInitialTeamValue;
import process.visitor.marketsize.CreateMediaMarketVisitor;
import process.visitor.marketsize.GenerateStadiumCapacityVisitor;

public class FinanceBuilder {
    private TeamRepository teamRepositery = TeamRepository.getInstance();

    // deja un marketSize et un profil financier au moment de l'appel car choisi en
    // random
    public static TeamFinance buildTeamFinance(Team team) {
        Budget budget = team.getTeamFinance().getBudget();
        TeamFinance teamFinance = team.getTeamFinance();

        EconomicProfil economicProfil = teamFinance.getEconomicProfil();
        MarketSize marketSize = teamFinance.getMarketSize();
        MediaMarket mediaMarket = teamFinance.getMediaMarket();
        FinancialPolicy financialProfil = teamFinance.getFinancialProfil();

        double popularity = team.getFormerPopularity();
        Stadium stadium = team.getStadium();

        createMediaMarket(mediaMarket, marketSize);
        EconomicProfileFactory.create(economicProfil, popularity, mediaMarket, financialProfil,
                teamFinance.getTeamTransferStrategy());

        calculateInitialBudget(budget, marketSize, economicProfil, popularity);
        teamFinance.setTeamValue(calculateInitialTeamValue(team, marketSize, budget));
        FinanceUtility.initiateBudget(budget);
        FinanceUtility.updateTeamPayroll(team);
        stadium.setCapacity(generateCapacity(marketSize));
        stadium.setTicketPrice(calculateBaseTicketPrice(marketSize));

        return teamFinance;
    }

    private static void calculateInitialBudget(Budget budget, MarketSize marketSize, EconomicProfil economicProfil,
            double popularity) {
        calculateBaseBudget(budget, popularity);
        CalculateInitialTeamBudgetVisitor calculateInitialTeamBudgetVisitor = new CalculateInitialTeamBudgetVisitor(
                budget.getInitialAmount(), popularity, economicProfil);
        double initialAmount = marketSize.accept(calculateInitialTeamBudgetVisitor);
        budget.setInitialAmount(initialAmount);
        budget.setRemainingAmount(initialAmount);
    }

    private static double calculateInitialTeamValue(Team team, MarketSize marketSize, Budget budget) {
        double baseValue = 250.0;
        double popularityBonus = team.getFormerPopularity() * 2.0;
        double marketBonus = getMarketValueBonus(marketSize);
        double stadiumBonus = 25.0;

        return baseValue + budget.getRemainingAmount() + popularityBonus + marketBonus + stadiumBonus;
    }

    private static double getMarketValueBonus(MarketSize marketSize) {
        return marketSize.accept(new CalculateInitialTeamValue());
    }

    private static void calculateBaseBudget(Budget budget, double popularity) {
        double initialAmount = FinanceConfiguration.BASE_TEAM_BUDGET;
        if (popularity <= 70) {
            initialAmount *= 1.1;
        } else if (popularity <= 80) {
            initialAmount *= 1.3;
        } else if (popularity <= 90) {
            initialAmount *= 1.45;
        } else {
            initialAmount *= 1.6;
        }
        budget.setInitialAmount(initialAmount);
    }

    private static int generateCapacity(MarketSize marketSize) {
        GenerateStadiumCapacityVisitor generateStadiumCapacityVisitor = new GenerateStadiumCapacityVisitor();
        return marketSize.accept(generateStadiumCapacityVisitor);
    }

    private static double calculateBaseTicketPrice(MarketSize marketSize) {
        CalculateBaseTicketVisitor calculateBaseTicketVisitor = new CalculateBaseTicketVisitor();
        return marketSize.accept(calculateBaseTicketVisitor);
    }

    private static void createMediaMarket(MediaMarket mediaMarket, MarketSize marketSize) {
        marketSize.accept(new CreateMediaMarketVisitor(mediaMarket));
    }
}

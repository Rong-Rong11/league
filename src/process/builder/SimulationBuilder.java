package process.builder;

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
import process.repositery.TeamRepositery;
import process.utilitary.FinanceUtilitary;
import process.visitor.marketsize.CalculateBaseTicketVisitor;
import process.visitor.marketsize.CalculateInitialTeamBudgetVisitor;
import process.visitor.marketsize.CreateMediaMarketVisitor;
import process.visitor.marketsize.GenerateStadiumCapacityVisitor;

public class SimulationBuilder {
    private TeamRepositery teamRepositery = TeamRepositery.getInstance();

    public void build() {
        buildFinance();
    }

    private void buildFinance() {
        for (Team team : this.teamRepositery.getAllTeams()) {
            buildTeamFinance(team);
        }
    }

    // déjà un marketSize et un profil financier car choisi en random
    private void buildTeamFinance(Team team) {
        Budget budget = team.getTeamFinance().getBudget();
        TeamFinance teamFinance = team.getTeamFinance();

        EconomicProfil economicProfil = teamFinance.getEconomicProfil();
        MarketSize marketSize = teamFinance.getMarketSize();
        MediaMarket mediaMarket = teamFinance.getMediaMarket();
        FinancialPolicy financialProfil = teamFinance.getFinancialProfil();

        double popularity = team.getPopularity();
        Stadium stadium = team.getStadium();

        createMediaMarket(mediaMarket, marketSize);
        EconomicProfileFactory.create(economicProfil, popularity, mediaMarket, financialProfil,
                teamFinance.getTeamTransferStrategy());

        calculateInitialBudget(budget, marketSize, economicProfil, popularity);
        FinanceUtilitary.initiateBudget(budget);
        stadium.setCapacity(generateCapacity(marketSize));
        stadium.setTicketPrice(calculateBaseTicketPrice(marketSize));
    }

    private void calculateInitialBudget(Budget budget, MarketSize marketSize, EconomicProfil economicProfil,
            double popularity) {
        calculateBaseBudget(budget, popularity);
        CalculateInitialTeamBudgetVisitor calculateInitialTeamBudgetVisitor = new CalculateInitialTeamBudgetVisitor(
                budget.getInitialAmount(), popularity, economicProfil);
        budget.setInitialAmount(marketSize.accept(calculateInitialTeamBudgetVisitor));
    }

    private void calculateBaseBudget(Budget budget, double popularity) {
        double initialAmount = FinanceConfiguration.BASE_TEAM_BUDGET;
        if (popularity <= 70) {
            initialAmount *= 0.9;
        } else if (popularity <= 80) {
            initialAmount *= 1.1;
        } else if (popularity <= 90) {
            initialAmount *= 1.3;
        } else {
            initialAmount *= 1.5;
        }
        budget.setInitialAmount(initialAmount);
    }

    private int generateCapacity(MarketSize marketSize) {
        GenerateStadiumCapacityVisitor generateStadiumCapacityVisitor = new GenerateStadiumCapacityVisitor();
        return marketSize.accept(generateStadiumCapacityVisitor);
    }

    private double calculateBaseTicketPrice(MarketSize marketSize) {
        CalculateBaseTicketVisitor calculateBaseTicketVisitor = new CalculateBaseTicketVisitor();
        return marketSize.accept(calculateBaseTicketVisitor);
    }

    private void createMediaMarket(MediaMarket mediaMarket, MarketSize marketSize) {
        marketSize.accept(new CreateMediaMarketVisitor(mediaMarket));
    }
}

package process.service.finance.game.expense;

import config.FinanceConfiguration;
import config.GameConfiguration;
import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;

public class TravelCostCalculator {

	private GameStat gameStat;
	private GameExpenseBonusProvider bonusProvider;

	public TravelCostCalculator(GameStat gameStat, GameExpenseBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateAwayTravelCost(Team awayTeam, Game game) {
		double travelCost;
		int typeGame = game.getGameContext().getTypeGame();
		EconomicProfil economicProfil = awayTeam.getTeamFinance().getEconomicProfil();

		if (typeGame == GameConfiguration.GAME_INTRA_DIVISION) {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTRA_DIVISION_COST;
		} else if (typeGame == GameConfiguration.GAME_INTRA_CONFERENCE) {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTRA_CONFERENCE_COST;
		} else {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTER_CONFERENCE_COST;
		}

		double bonusRate = bonusProvider.getTravelBonusRate(game);
		travelCost = travelCost * (1 + bonusRate);
		travelCost *= (1 + economicProfil.getFanLoyalty() * 0.32);

		gameStat.getAwayFinance().setTravelCosts(travelCost);
	}
}

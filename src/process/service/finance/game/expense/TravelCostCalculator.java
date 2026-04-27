package process.service.finance.game.expense;

import org.apache.log4j.Logger;

import config.FinanceConfiguration;
import config.GameConfiguration;
import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofile.EconomicProfile;
import log.LoggerUtility;

public class TravelCostCalculator {
	private static final Logger logger = LoggerUtility.getLogger(TravelCostCalculator.class, "text");

	private GameStat gameStat;
	private GameExpenseBonusProvider bonusProvider;

	public TravelCostCalculator(GameStat gameStat, GameExpenseBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateAwayTravelCost(Team awayTeam, Game game) {
		if (awayTeam == null) {
			logger.warn("Skipping travel cost calculation because away team is null");
			return;
		}
		if (game == null) {
			logger.warn("Skipping travel cost calculation because game is null");
			return;
		}
		if (gameStat == null) {
			logger.warn("Skipping travel cost calculation because game stat is null");
			return;
		}
		double travelCost;
		int typeGame = game.getGameContext().getTypeGame();
		EconomicProfile economicProfile = awayTeam.getTeamFinance().getStructure().getEconomicProfile();
		logger.trace("Calculating away travel cost for " + awayTeam.getName() + " with game type " + typeGame);

		if (typeGame == GameConfiguration.GAME_INTRA_DIVISION) {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTRA_DIVISION_COST;
			logger.trace("Using intra-division base travel cost " + travelCost);
		} else if (typeGame == GameConfiguration.GAME_INTRA_CONFERENCE) {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTRA_CONFERENCE_COST;
			logger.trace("Using intra-conference base travel cost " + travelCost);
		} else {
			travelCost = FinanceConfiguration.BASE_TRAVEL_INTER_CONFERENCE_COST;
			logger.trace("Using inter-conference base travel cost " + travelCost);
		}

		double bonusRate = bonusProvider.getTravelBonusRate(game);
		travelCost = travelCost * (1 + bonusRate);
		logger.trace("Applied travel bonus rate " + bonusRate);
		travelCost *= (1 + economicProfile.getFanLoyalty() * 0.32);
		logger.trace("Applied away fan loyalty travel modifier with loyalty " + economicProfile.getFanLoyalty());

		gameStat.getAwayFinance().setTravelCosts(travelCost);
		logger.debug("Calculated away travel cost " + travelCost + " for " + awayTeam.getName());
	}
}

package process.service.finance.game.expense;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import data.team.finance.mediamarket.MediaMarket;
import process.utility.CalendarUtility;

public class LogisticsCostCalculator {

	private GameStat gameStat;
	private GameExpenseBonusProvider bonusProvider;

	public LogisticsCostCalculator(GameStat gameStat, GameExpenseBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateLogisticCosts(Game game) {
		Team homeTeam = game.getGameContext().getHomeTeam();
		MediaMarket mediaMarket = homeTeam.getTeamFinance().getMediaMarket();
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double baseTransport = 0.06;
		double mediaSetup = 0.045;
		double equipment = 0.035;
		double baseLogisticCost = baseTransport + mediaSetup + equipment;

		double modifier = 0.0;

		if (CalendarUtility.isRivalry(game.getGameContext())) {
			modifier += 0.15;
		}

		modifier += mediaMarket.getBusinessOpportunityModifier() * 0.08;
		modifier += economicProfil.getCommercialAggressiveness() * 0.10;
		modifier += economicProfil.getHistoricalPrestige() * 0.05;

		modifier += bonusProvider.getLogisticBonusRate(game, homeTeam);

		double logisticCost = baseLogisticCost * (1 + modifier);
		gameStat.getHomeFinance().setLogisticsCosts(logisticCost);
	}
}

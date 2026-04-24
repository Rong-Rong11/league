package process.service.finance.game.expense;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;

public class SecurityCostCalculator {

	private GameStat gameStat;
	private GameExpenseBonusProvider bonusProvider;

	public SecurityCostCalculator(GameStat gameStat, GameExpenseBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateSecurityCosts(Team homeTeam, int attendees, Game game) {
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double costPerFan = 5.5;
		double modifier = 0.0;

		if (attendees > 15000) {
			modifier += 0.30;
		}

		if (economicProfil.getFanLoyalty() > 0.5) {
			modifier += 0.05;
		}
		modifier += economicProfil.getFanLoyalty() * 0.18;
		modifier += bonusProvider.getSecurityBonusRate(game, homeTeam, attendees);
		if (homeTeam.hasStarPlayer()) {
			modifier += 0.1;
		}

		double securityCost = (attendees * costPerFan * (1 + modifier)) / 1000000.0;
		gameStat.getHomeFinance().setSecurityCosts(securityCost);
	}
}

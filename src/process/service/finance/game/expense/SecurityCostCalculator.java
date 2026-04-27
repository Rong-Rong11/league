package process.service.finance.game.expense;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofile.EconomicProfile;
import log.LoggerUtility;

public class SecurityCostCalculator {
	private static final Logger logger = LoggerUtility.getLogger(SecurityCostCalculator.class, "text");

	private GameStat gameStat;
	private GameExpenseBonusProvider bonusProvider;

	public SecurityCostCalculator(GameStat gameStat, GameExpenseBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateSecurityCosts(Team homeTeam, int attendees, Game game) {
		if (homeTeam == null) {
			logger.warn("Skipping security cost calculation because home team is null");
			return;
		}
		if (gameStat == null) {
			logger.warn("Skipping security cost calculation because game stat is null");
			return;
		}
		EconomicProfile economicProfile = homeTeam.getTeamFinance().getStructure().getEconomicProfile();
		logger.trace("Calculating security costs for " + homeTeam.getName() + " with attendees " + attendees);

		double costPerFan = 5.5;
		double modifier = 0.0;
		logger.trace("Security cost per fan is " + costPerFan);

		if (attendees > 15000) {
			logger.trace("Applying high attendance security modifier");
			modifier += 0.30;
		}

		if (economicProfile.getFanLoyalty() > 0.5) {
			logger.trace("Applying loyal fanbase security modifier");
			modifier += 0.05;
		}
		modifier += economicProfile.getFanLoyalty() * 0.18;
		logger.trace("Security modifier after fan loyalty is " + modifier);
		double bonusRate = bonusProvider.getSecurityBonusRate(game, homeTeam, attendees);
		modifier += bonusRate;
		logger.trace("Applied security bonus rate " + bonusRate);
		if (homeTeam.hasStarPlayer()) {
			logger.trace("Applying star player security modifier for " + homeTeam.getName());
			modifier += 0.1;
		}

		double securityCost = (attendees * costPerFan * (1 + modifier)) / 1000000.0;
		gameStat.getHomeFinance().setSecurityCosts(securityCost);
		logger.debug("Calculated security cost "
				+ securityCost
				+ " for "
				+ homeTeam.getName()
				+ " with modifier "
				+ modifier);
	}
}

package process.service.finance.game.expense;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;
import log.LoggerUtility;

public class StaffCostCalculator {
	private static final Logger logger = LoggerUtility.getLogger(StaffCostCalculator.class, "text");

	private GameStat gameStat;
	private GameExpenseBonusProvider bonusProvider;

	public StaffCostCalculator(GameStat gameStat, GameExpenseBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
		logger.debug("Staff cost calculator initialized");
	}

	public void calculateStaffCosts(Team homeTeam, Game game) {
		if (homeTeam == null) {
			logger.warn("Skipping staff cost calculation because home team is null");
			return;
		}
		if (gameStat == null) {
			logger.warn("Skipping staff cost calculation because game stat is null");
			return;
		}
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getStructure().getEconomicProfil();
		logger.trace("Calculating staff costs for " + homeTeam.getName());

		double baseStaffCost = 0.17;
		double modifier = 0.0;
		double attendanceRate = gameStat.getAttendanceRate();
		logger.trace("Base staff cost is " + baseStaffCost + " with attendance rate " + attendanceRate);

		if (attendanceRate > 0.9) {
			logger.trace("Applying high attendance staff modifier");
			modifier += 0.20;
		} else if (attendanceRate < 0.6) {
			logger.trace("Applying low attendance staff modifier");
			modifier -= 0.10;
		}

		modifier += economicProfil.getFanLoyalty() * 0.05;
		logger.trace("Staff modifier after fan loyalty is " + modifier);
		double bonusRate = bonusProvider.getStaffBonusRate(game, homeTeam);
		modifier += bonusRate;
		logger.trace("Applied staff bonus rate " + bonusRate);

		double staffCost = baseStaffCost * (1 + modifier);
		gameStat.getHomeFinance().setStaffCosts(staffCost);
		logger.debug("Calculated staff cost "
				+ staffCost
				+ " for "
				+ homeTeam.getName()
				+ " with modifier "
				+ modifier);
	}
}

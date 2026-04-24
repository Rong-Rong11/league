package process.service.finance.game.expense;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import data.team.finance.economicprofil.EconomicProfil;

public class StaffCostCalculator {

	private GameStat gameStat;
	private GameExpenseBonusProvider bonusProvider;

	public StaffCostCalculator(GameStat gameStat, GameExpenseBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateStaffCosts(Team homeTeam, Game game) {
		EconomicProfil economicProfil = homeTeam.getTeamFinance().getEconomicProfil();

		double baseStaffCost = 0.17;
		double modifier = 0.0;
		double attendanceRate = gameStat.getAttendanceRate();

		if (attendanceRate > 0.9) {
			modifier += 0.20;
		} else if (attendanceRate < 0.6) {
			modifier -= 0.10;
		}

		modifier += economicProfil.getFanLoyalty() * 0.05;
		modifier += bonusProvider.getStaffBonusRate(game, homeTeam);

		double staffCost = baseStaffCost * (1 + modifier);
		gameStat.getHomeFinance().setStaffCosts(staffCost);
	}
}

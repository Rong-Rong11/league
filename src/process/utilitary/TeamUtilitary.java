package process.utilitary;

import config.FinanceConfiguration;
import config.FinancialPolicy;
import config.SimulationConfiguration;
import data.player.Player;
import data.team.Team;
import data.team.finance.financialprofil.AmbitiousProfil;
import data.team.finance.financialprofil.BalancedProfil;
import data.team.finance.financialprofil.EconomicalProfil;
import data.team.finance.financialprofil.FinancialProfil;
import data.team.finance.marketsize.LargeSize;
import data.team.finance.marketsize.MarketSize;
import data.team.finance.marketsize.MediumSize;
import data.team.finance.marketsize.SmallSize;

public class TeamUtilitary {

	private static double getTeamAttackNote(Team team) {
		double sumOfNote = 0;
		double numberOfPlayer = 0;
		double note;
		for (Player player : team.getPlayers().values()) {
			sumOfNote += PlayerUtilitary.getPlayerAttackNote(player);
			numberOfPlayer++;
		}
		note = sumOfNote / numberOfPlayer;
		return note;

	}

	private static double getTeamDefenseNote(Team team) {
		double sumOfNote = 0;
		double numberOfPlayer = 0;
		double note;
		for (Player player : team.getPlayers().values()) {
			sumOfNote += PlayerUtilitary.getPlayerDefenseNote(player);
			numberOfPlayer++;
		}
		note = sumOfNote / numberOfPlayer;
		return Math.min(note, 3);
	}

	public static String getTeamSportProfile(Team team) {
		double attackNote = getTeamAttackNote(team);
		double defenseNote = getTeamDefenseNote(team);
		if (defenseNote <= 0)
			defenseNote = 1;
		if ((attackNote / defenseNote) > 1.1) {
			return SimulationConfiguration.TEAM_OFFENSIVE_MATCH_PROFIL;
		} else if ((attackNote / defenseNote) < 0.9) {
			return SimulationConfiguration.TEAM_DEFENSIVE_MATCH_PROFIL;
		} else {
			return SimulationConfiguration.TEAM_BALANCED_MATCH_PROFIL;
		}
	}

	public static void setStarPlayer(Team team) {
		for (Player player : team.getPlayers().values()) {
			if (player.isStar()) {
				team.setStarPlayer(player);
				return;
			}
		}
		team.setStarPlayer(null);
	}

	public static void updatePerformanceRating(Team team, Team opponent, int result, int scoreDifference,
			double opponentPopularity) {
		double opponentRating = (getTeamAttackNote(opponent) + getTeamDefenseNote(opponent)) / 2;

		double performanceRating = team.getTeamPerformance().getPerformanceRating();
		double resultBonus = 0;
		if (result == 1) {
			resultBonus = 0.2;
		} else if (result == -1) {
			resultBonus = -0.1;
		} else {
			resultBonus = 0.05;
		}
		double marginBonus = Math.min(0.1, scoreDifference / 50.0);
		double opponentFactor = (opponentRating > 0.6) ? 1.2 : 0.8;
		double gameImpact = (resultBonus + marginBonus) * opponentFactor;

		performanceRating = (performanceRating * 0.85) + (gameImpact * 0.15);
		performanceRating = Math.max(0, Math.min(1, performanceRating));
		team.getTeamPerformance().setPerformanceRating(performanceRating);
	}

	public static FinancialProfil randomFinancialProfil() {
		double random = Math.random();
		if (random < 0.3)
			return new AmbitiousProfil(FinancialPolicy.FINANCE_PROFIL_AMBITIOUS);
		if (random < 0.6)
			return new EconomicalProfil(FinancialPolicy.FINANCE_PROFIL_ECONOMIC);
		return new BalancedProfil(FinancialPolicy.FINANCE_PROFIL_BALANCED);
	}

	public static MarketSize randomMarketSize() {
		double random = Math.random();
		if (random < 0.25)
			return new LargeSize(FinanceConfiguration.MARKET_SIZE_LARGE);
		if (random < 0.75)
			return new MediumSize(FinanceConfiguration.MARKET_SIZE_MEDIUM);
		return new SmallSize(FinanceConfiguration.MARKET_SIZE_SMALL);
	}

}

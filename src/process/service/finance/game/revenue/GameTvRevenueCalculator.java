package process.service.finance.game.revenue;

import data.finance.GameStat;
import data.sport.setup.Game;

public class GameTvRevenueCalculator {

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameTvRevenueCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateTVRevenue(Game game) {
		double leagueTVPerGame = 0.7;

		double homeShare = leagueTVPerGame * 0.6;
		double awayShare = leagueTVPerGame * 0.4;

		if (game.getGameContext().getHomeTeam().hasStarPlayer()) {
			homeShare *= 1.10;
		}
		if (game.getGameContext().getAwayTeam().hasStarPlayer()) {
			awayShare *= 1.08;
		}

		homeShare *= (1 + bonusProvider.getHomeTvBonusRate(game));
		awayShare *= (1 + bonusProvider.getAwayTvBonusRate(game));

		gameStat.getHomeFinance().setTvRevenue(homeShare);
		gameStat.getAwayFinance().setTvRevenue(awayShare);
	}
}

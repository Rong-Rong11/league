package process.service.finance.game.revenue;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.sport.setup.Game;
import log.LoggerUtility;

public class GameTvRevenueCalculator {
	private static final Logger logger = LoggerUtility.getLogger(GameTvRevenueCalculator.class, "text");

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameTvRevenueCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateTVRevenue(Game game) {
		if (game == null) {
			logger.warn("Skipping TV revenue calculation because game is null");
			return;
		}
		if (gameStat == null) {
			logger.warn("Skipping TV revenue calculation because game stat is null");
			return;
		}
		double leagueTVPerGame = 0.7;

		double homeShare = leagueTVPerGame * 0.6;
		double awayShare = leagueTVPerGame * 0.4;
		logger.trace("Base TV revenue shares are home="
				+ homeShare
				+ " and away="
				+ awayShare
				+ " from league TV per game "
				+ leagueTVPerGame);

		if (game.getGameContext().getHomeTeam().hasStarPlayer()) {
			logger.trace("Applying home star player TV revenue modifier");
			homeShare *= 1.10;
		}
		if (game.getGameContext().getAwayTeam().hasStarPlayer()) {
			logger.trace("Applying away star player TV revenue modifier");
			awayShare *= 1.08;
		}

		double homeBonusRate = bonusProvider.getHomeTvBonusRate(game);
		double awayBonusRate = bonusProvider.getAwayTvBonusRate(game);
		homeShare *= (1 + homeBonusRate);
		awayShare *= (1 + awayBonusRate);
		logger.trace("Applied TV bonus rates home=" + homeBonusRate + " and away=" + awayBonusRate);

		gameStat.getHomeFinance().setTvRevenue(homeShare);
		gameStat.getAwayFinance().setTvRevenue(awayShare);
		logger.debug("Calculated TV revenue home=" + homeShare + " and away=" + awayShare);
	}
}

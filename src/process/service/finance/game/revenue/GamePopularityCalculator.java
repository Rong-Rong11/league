package process.service.finance.game.revenue;

import java.time.LocalDate;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import log.LoggerUtility;
import process.utility.CalendarUtility;

public class GamePopularityCalculator {
	private static final Logger logger = LoggerUtility.getLogger(GamePopularityCalculator.class, "text");

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GamePopularityCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public double calculatePopularityRate(Game game, LocalDate date) {
		if (game == null) {
			logger.warn("Skipping popularity rate calculation because game is null");
			return 0.0;
		}
		if (date == null) {
			logger.warn("Skipping popularity rate calculation because date is null");
			return 0.0;
		}
		if (gameStat == null) {
			logger.warn("Skipping popularity rate calculation because game stat is null");
			return 0.0;
		}
		Team homeTeam = game.getGameContext().getHomeTeam();
		logger.trace("Calculating popularity rate for " + homeTeam.getName() + " on " + date);

		double gamePopularity = CalendarUtility.popularityScoreGame(game, date);
		double gameScore = gamePopularity / 800;
		logger.trace("Game popularity score is " + gamePopularity + " and normalized score is " + gameScore);

		double performatingRate = (game.getGameContext().getHomeTeam().getTeamPerformance().getPerformanceRating()
				+ game.getGameContext().getAwayTeam().getTeamPerformance().getPerformanceRating()) / 2;
		logger.trace("Average game performance rate is " + performatingRate);

		double popularityRate = (gameScore * 0.5) + (performatingRate * 0.5);

		int winStreak = homeTeam.getTeamPerformance().getCurrentWinStreak();
		popularityRate += Math.min(winStreak, 10) * 0.015;
		logger.trace("Applied home win streak popularity bonus from streak " + winStreak);

		double bonusRate = bonusProvider.getPopularityBonusRate(game, date, homeTeam);
		popularityRate += bonusRate;
		logger.trace("Applied popularity bonus rate " + bonusRate);

		popularityRate = Math.max(0.2, Math.min(1.0, popularityRate));
		gameStat.setPopularity(popularityRate);
		logger.debug("Calculated popularity rate " + popularityRate + " for " + homeTeam.getName());
		return popularityRate;
	}
}

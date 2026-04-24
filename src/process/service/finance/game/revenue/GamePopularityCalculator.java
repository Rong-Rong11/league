package process.service.finance.game.revenue;

import java.time.LocalDate;

import data.finance.GameStat;
import data.sport.setup.Game;
import data.team.Team;
import process.utility.CalendarUtility;

public class GamePopularityCalculator {

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GamePopularityCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public double calculatePopularityRate(Game game, LocalDate date) {
		Team homeTeam = game.getGameContext().getHomeTeam();

		double gamePopularity = CalendarUtility.popularityScoreGame(game, date);
		double gameScore = gamePopularity / 800;

		double performatingRate = (game.getGameContext().getHomeTeam().getTeamPerformance().getPerformanceRating()
				+ game.getGameContext().getAwayTeam().getTeamPerformance().getPerformanceRating()) / 2;

		double popularityRate = (gameScore * 0.5) + (performatingRate * 0.5);

		int winStreak = homeTeam.getTeamPerformance().getCurrentWinStreak();
		popularityRate += Math.min(winStreak, 10) * 0.015;

		popularityRate += bonusProvider.getPopularityBonusRate(game, date, homeTeam);

		popularityRate = Math.max(0.2, Math.min(1.0, popularityRate));
		gameStat.setPopularity(popularityRate);
		return popularityRate;
	}
}

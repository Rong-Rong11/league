package process.simulator;

import java.util.HashMap;

import data.player.Asset;
import data.player.Player;
import data.sport.setup.Game;
import data.sport.setup.GameResult;
import data.team.Team;
import process.repository.PlayerRepository;
import process.simulator.game.asset.GameAssetManager;
import process.simulator.game.health.HealthManager;
import process.simulator.game.outcome.GameOutcomeUpdater;
import process.simulator.game.quarter.QuarterSimulator;

public class GameSimulator {

	private final PlayerRepository playerRepository = PlayerRepository.getInstance();
	private final HealthManager healthManager = new HealthManager();
	private final QuarterSimulator quarterSimulator = new QuarterSimulator(healthManager);
	private final GameAssetManager gameAssetManager = new GameAssetManager();
	private final GameOutcomeUpdater gameOutcomeUpdater = new GameOutcomeUpdater();

	public void simulateGame(Game game) {
		GameResult[] quarterResults = new GameResult[4];
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();
		HashMap<Player, Asset> playersNewAssets = gameAssetManager.createNewAssets(playerRepository.getAllPlayers());

		healthManager.initializeHealth(homeTeam, awayTeam);

		for (int index = 0; index < 4; index++) {
			quarterResults[index] = quarterSimulator.simulateQuarter(homeTeam, awayTeam, playersNewAssets);
			if (index == 1) {
				healthManager.updateRest(15, homeTeam, awayTeam);
			} else {
				healthManager.updateRest(2, homeTeam, awayTeam);
			}
		}

		game.setQuarterResults(quarterResults);
		gameOutcomeUpdater.updateFinalOutcome(game);
		gameAssetManager.updateTrueShootingPercentages(homeTeam, awayTeam, playersNewAssets);
		gameAssetManager.updateCurrentSeasonAssets(homeTeam, playersNewAssets);
		gameAssetManager.updateCurrentSeasonAssets(awayTeam, playersNewAssets);
	}
}

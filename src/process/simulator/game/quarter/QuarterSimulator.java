package process.simulator.game.quarter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import config.GameConfiguration;
import data.player.Asset;
import data.player.Player;
import data.sport.play.action.ActionResult;
import data.sport.setup.GameResult;
import data.team.Team;
import process.simulator.game.action.PossessionSimulator;
import process.simulator.game.asset.GameAssetManager;
import process.simulator.game.event.PossessionPlayerSelector;
import process.simulator.game.health.HealthManager;
import process.simulator.game.lineup.LineupSelector;
import process.simulator.game.outcome.GameOutcomeUpdater;

public class QuarterSimulator {

	private static final String HOME_POSSESSION = "home";
	private static final String AWAY_POSSESSION = "away";

	private final PossessionPlayerSelector possessionPlayerSelector = new PossessionPlayerSelector();
	private final HealthManager healthManager;
	private final LineupSelector lineupSelector = new LineupSelector();
	private final PossessionSimulator possessionSimulator = new PossessionSimulator();
	private final GameAssetManager gameAssetManager = new GameAssetManager();
	private final GameOutcomeUpdater gameOutcomeUpdater = new GameOutcomeUpdater();

	public QuarterSimulator(HealthManager healthManager) {
		this.healthManager = healthManager;
	}

	public GameResult simulateQuarter(Team homeTeam, Team awayTeam, HashMap<Player, Asset> playersNewAssets) {
		ArrayList<Player> homeTeamPlayers = lineupSelector.choosePlayerToPlay(homeTeam, awayTeam);
		ArrayList<Player> awayTeamPlayers = lineupSelector.choosePlayerToPlay(awayTeam, homeTeam);
		HashMap<Player, Asset> homePlayersAssetsOfMatch = gameAssetManager.createPlayerAssetMap(homeTeamPlayers);
		HashMap<Player, Asset> awayPlayersAssetsOfMatch = gameAssetManager.createPlayerAssetMap(awayTeamPlayers);

		int quarterTime = GameConfiguration.QUARTER_DURATION;
		String possession = Math.random() < 0.5 ? HOME_POSSESSION : AWAY_POSSESSION;
		GameResult gameResult = new GameResult();

		while (quarterTime > 0) {
			PossessionContext possessionContext = buildPossessionContext(possession, homeTeamPlayers, awayTeamPlayers,
					homePlayersAssetsOfMatch, awayPlayersAssetsOfMatch);
			Player attackingPlayer = possessionPlayerSelector.chooseAttackingPlayer(possessionContext.attackingPlayers);

			ArrayList<ActionResult> actionResults = possessionSimulator.simulatePossession(attackingPlayer,
					possessionContext.attackingPlayers, possessionContext.defensivePlayers, quarterTime,
					possessionContext.attackPlayersAssetsOfMatch, possessionContext.defensivePlayersAssetsOfMatch,
					playersNewAssets);
			ActionResult terminalAction = actionResults.get(actionResults.size() - 1);

			if (gameOutcomeUpdater.shouldChangePossession(terminalAction)) {
				possession = switchPossession(possession);
			}
			healthManager.updateFatigue(homeTeamPlayers, awayTeamPlayers, terminalAction.getActionTime());
			healthManager.addMinutesPlayed(homeTeamPlayers, awayTeamPlayers, playersNewAssets,
					terminalAction.getActionTime());

			lineupSelector.updatePlayers(awayTeam, awayTeamPlayers);
			awayPlayersAssetsOfMatch = gameAssetManager.createPlayerAssetMap(awayTeamPlayers);
			lineupSelector.updatePlayers(homeTeam, homeTeamPlayers);
			homePlayersAssetsOfMatch = gameAssetManager.createPlayerAssetMap(homeTeamPlayers);

			for (ActionResult actionResult : actionResults) {
				gameAssetManager.updateAssetAfterAction(actionResult, playersNewAssets);
				gameOutcomeUpdater.recordAction(gameResult, actionResult, homeTeamPlayers, awayTeamPlayers);
			}

			if (terminalAction.getName().equals(GameConfiguration.END_OF_TIME_ACTION)) {
				quarterTime = 0;
			} else {
				quarterTime -= terminalAction.getActionTime();
			}
		}
		return gameResult;
	}

	private PossessionContext buildPossessionContext(String possession, ArrayList<Player> homeTeamPlayers,
			ArrayList<Player> awayTeamPlayers, HashMap<Player, Asset> homePlayersAssetsOfMatch,
			HashMap<Player, Asset> awayPlayersAssetsOfMatch) {
		if (possession.equals(HOME_POSSESSION)) {
			return new PossessionContext(
					lineupSelector.sortPlayersAccordingToAttack(homeTeamPlayers),
					lineupSelector.sortPlayersAccordingToDefense(awayTeamPlayers),
					homePlayersAssetsOfMatch,
					awayPlayersAssetsOfMatch);
		}
		return new PossessionContext(
				lineupSelector.sortPlayersAccordingToAttack(awayTeamPlayers),
				lineupSelector.sortPlayersAccordingToDefense(homeTeamPlayers),
				awayPlayersAssetsOfMatch,
				homePlayersAssetsOfMatch);
	}

	private String switchPossession(String possession) {
		return possession.equals(HOME_POSSESSION) ? AWAY_POSSESSION : HOME_POSSESSION;
	}

	private static class PossessionContext {
		private final TreeMap<Double, Player> attackingPlayers;
		private final TreeMap<Double, Player> defensivePlayers;
		private final HashMap<Player, Asset> attackPlayersAssetsOfMatch;
		private final HashMap<Player, Asset> defensivePlayersAssetsOfMatch;

		private PossessionContext(TreeMap<Double, Player> attackingPlayers, TreeMap<Double, Player> defensivePlayers,
				HashMap<Player, Asset> attackPlayersAssetsOfMatch,
				HashMap<Player, Asset> defensivePlayersAssetsOfMatch) {
			this.attackingPlayers = attackingPlayers;
			this.defensivePlayers = defensivePlayers;
			this.attackPlayersAssetsOfMatch = attackPlayersAssetsOfMatch;
			this.defensivePlayersAssetsOfMatch = defensivePlayersAssetsOfMatch;
		}
	}
}

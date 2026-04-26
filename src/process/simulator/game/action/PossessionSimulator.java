package process.simulator.game.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import config.GameConfiguration;
import data.player.Asset;
import data.player.Player;
import data.sport.play.OffensiveTry;
import data.sport.play.action.ActionResult;
import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.MissedShot;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;
import log.LoggerUtility;
import process.simulator.game.event.OffensiveActionSelector;
import process.simulator.game.event.PossessionPlayerSelector;
import process.simulator.game.event.ShotOutcomePlayerSelector;
import process.simulator.game.health.InjuryManager;

public class PossessionSimulator {
	private static final Logger logger = LoggerUtility.getLogger(PossessionSimulator.class, "text");

	private final PossessionPlayerSelector possessionPlayerSelector = new PossessionPlayerSelector();
	private final OffensiveActionSelector offensiveActionSelector = new OffensiveActionSelector();
	private final ShotOutcomePlayerSelector shotOutcomePlayerSelector = new ShotOutcomePlayerSelector();
	private final InjuryManager injuryManager = new InjuryManager();
	private final ActionResolutionCalculator actionResolutionCalculator = new ActionResolutionCalculator();

	public ArrayList<ActionResult> simulatePossession(Player attackingPlayer, TreeMap<Double, Player> attackingPlayers,
			TreeMap<Double, Player> defensivePlayers, int quarterTimeRemaining,
			HashMap<Player, Asset> attackPlayersAssetsOfMatch, HashMap<Player, Asset> defensivePlayersAssetsOfMatch,
			HashMap<Player, Asset> playersNewAssets) {

		ArrayList<ActionResult> actionResults = new ArrayList<ActionResult>();

		if (attackingPlayer == null || attackingPlayers == null || defensivePlayers == null
				|| attackPlayersAssetsOfMatch == null || defensivePlayersAssetsOfMatch == null
				|| playersNewAssets == null) {
			logger.warn("Returning empty possession result because player, players map or assets map is null");
			return actionResults;
		}

		int actionTime = 10 + (int) (Math.random() * 10);

		if (quarterTimeRemaining <= actionTime) {
			actionResults.add(new EndOfTime(GameConfiguration.END_OF_TIME_ACTION));
		} else {
			buildPossessionResults(attackingPlayer, attackingPlayers, defensivePlayers, attackPlayersAssetsOfMatch,
					defensivePlayersAssetsOfMatch, playersNewAssets, actionResults);
		}

		for (ActionResult actionResult : actionResults) {
			actionResult.setActionTime(actionTime);
		}

		return actionResults;
	}

	private void buildPossessionResults(Player attackingPlayer, TreeMap<Double, Player> attackingPlayers,
			TreeMap<Double, Player> defensivePlayers, HashMap<Player, Asset> attackPlayersAssetsOfMatch,
			HashMap<Player, Asset> defensivePlayersAssetsOfMatch, HashMap<Player, Asset> playersNewAssets,
			ArrayList<ActionResult> actionResults) {

		Player defendingPlayer = possessionPlayerSelector.chooseDefendingPlayer(defensivePlayers);

		if (actionResolutionCalculator.isTurnover(attackingPlayer, defendingPlayer)) {
			actionResults.add(new Turnover(GameConfiguration.TURNOVER_ACTION, attackingPlayer, defendingPlayer));
			return;
		}

		OffensiveTry offensiveTry = offensiveActionSelector.chooseOffensiveAction(attackingPlayer,
				attackPlayersAssetsOfMatch);

		if (offensiveTry == null) {
			logger.warn("Stopping possession because offensive action is null");
			return;
		}

		if (offensiveTry.getName().equals(GameConfiguration.FOULDRAW)) {
			injuryManager.simulateInjury(playersNewAssets, attackingPlayer, GameConfiguration.FOULDRAW);
		}

		if (actionResolutionCalculator.isShotMade(attackingPlayer, offensiveTry, defensivePlayers)) {
			actionResults.add(createPointScored(attackingPlayer, offensiveTry, attackPlayersAssetsOfMatch));
		} else {
			addMissedShotConsequence(attackingPlayer, attackingPlayers, attackPlayersAssetsOfMatch,
					defensivePlayersAssetsOfMatch, playersNewAssets, actionResults);
		}

		for (ActionResult actionResult : actionResults) {
			actionResult.setOffensiveAction(offensiveTry);
		}
	}

	private PointScored createPointScored(Player attackingPlayer, OffensiveTry offensiveTry,
			HashMap<Player, Asset> attackPlayersAssetsOfMatch) {
		Player assistPlayer = null;

		if ((offensiveTry.getName().equals(GameConfiguration.THREEPOINT)
				|| offensiveTry.getName().equals(GameConfiguration.TWOPOINT)) && isAssist()) {
			assistPlayer = shotOutcomePlayerSelector.chooseAssistPlayer(attackingPlayer, attackPlayersAssetsOfMatch);
		}

		if (offensiveTry.getName().equals(GameConfiguration.THREEPOINT)) {
			return new PointScored(GameConfiguration.SCORED_ACTION, 3, attackingPlayer, assistPlayer);
		}
		if (offensiveTry.getName().equals(GameConfiguration.FOULDRAW)) {
			return new PointScored(GameConfiguration.SCORED_ACTION, 1, attackingPlayer, assistPlayer);
		}
		return new PointScored(GameConfiguration.SCORED_ACTION, 2, attackingPlayer, assistPlayer);
	}

	private void addMissedShotConsequence(Player attackingPlayer, TreeMap<Double, Player> attackingPlayers,
			HashMap<Player, Asset> attackPlayersAssetsOfMatch, HashMap<Player, Asset> defensivePlayersAssetsOfMatch,
			HashMap<Player, Asset> playersNewAssets, ArrayList<ActionResult> actionResults) {
		actionResults.add(new MissedShot("missed shot", attackingPlayer));

		if (isBlock()) {
			Player blockingPlayer = shotOutcomePlayerSelector.chooseBlockingPlayer(defensivePlayersAssetsOfMatch);
			actionResults.add(new Block(GameConfiguration.BLOCK_ACTION, blockingPlayer));
			return;
		}

		Player reboundPlayer = shotOutcomePlayerSelector.chooseRebounder(attackPlayersAssetsOfMatch,
				defensivePlayersAssetsOfMatch);

		if (reboundPlayer == null) {
			logger.warn("Skipping rebound consequence because rebound player is null");
			return;
		}

		if (attackingPlayers.containsValue(reboundPlayer)) {
			actionResults.add(new Rebound(GameConfiguration.OFFENSIVE_REBOUND_ACTION, reboundPlayer, attackingPlayer));
			injuryManager.simulateInjury(playersNewAssets, reboundPlayer, GameConfiguration.OFFENSIVE_REBOUND_ACTION);
		} else {
			actionResults.add(new Rebound(GameConfiguration.DEFENSIVE_REBOUND_ACTION, reboundPlayer, attackingPlayer));
			injuryManager.simulateInjury(playersNewAssets, reboundPlayer, GameConfiguration.DEFENSIVE_REBOUND_ACTION);
		}
	}

	private static boolean isAssist() {
		return Math.random() < GameConfiguration.ASSIST_PROBABILITY;
	}

	private static boolean isBlock() {
		return Math.random() < GameConfiguration.BLOCK_PROBABILTY;
	}
}

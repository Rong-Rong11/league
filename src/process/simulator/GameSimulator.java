package process.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

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
import data.sport.setup.Game;
import data.sport.setup.GameResult;
import data.team.Team;
import data.team.TeamPerformance;
import process.repositery.PlayerRepositery;
import process.simulator.gametools.ActionSimulator;
import process.simulator.gametools.EventSimulator;
import process.simulator.gametools.HealthManager;
import process.simulator.gametools.InjuryManager;
import process.simulator.gametools.LineupSelector;
import process.utilitary.PlayerUtilitary;
import process.utilitary.TeamUtilitary;
import process.visitor.actionresult.ActionResultVisitor;
import process.visitor.actionresult.AssetUpdateVisitor;
import process.visitor.actionresult.GameResultVisitor;

public class GameSimulator {

	private PlayerRepositery playerRepositery = PlayerRepositery.getInstance();
	private EventSimulator eventSimulator = new EventSimulator();
	private InjuryManager injuryManager = new InjuryManager();
	private ActionSimulator actionSimulator = new ActionSimulator();
	private HealthManager healthManager = new HealthManager();
	private LineupSelector lineupSelector = new LineupSelector();

	public GameSimulator() {

	}

	private static boolean isAssist() {
		return Math.random() < GameConfiguration.ASSIST_PROBABILITY;
	}

	private static boolean isBlock() {
		return Math.random() < GameConfiguration.BLOCK_PROBABILTY;
	}

	private ArrayList<ActionResult> simulateAction(Player attackingPlayer, TreeMap<Double, Player> attackingPlayers,
			TreeMap<Double, Player> defensivePlayers, int quarterTimeRemaining,
			HashMap<Player, Asset> attackPlayersAssetsOfMatch, HashMap<Player, Asset> defensivePlayersAssetsOfMatch,
			HashMap<Player, Asset> playersNewAssets) {

		int actionTime = 10 + (int) (Math.random() * 10);
		ArrayList<ActionResult> actionResults = new ArrayList<ActionResult>();
		if (quarterTimeRemaining <= actionTime) {
			actionResults.add(new EndOfTime(GameConfiguration.END_OF_TIME_ACTION));
		}

		else {
			Player defendingPlayer = eventSimulator.chooseDefendingPlayer(defensivePlayers);

			if (actionSimulator.effectiveTurnover(attackingPlayer, defendingPlayer)) {
				actionResults.add(new Turnover(GameConfiguration.TURNOVER_ACTION, attackingPlayer, defendingPlayer));
			} else {
				OffensiveTry offensiveTry = eventSimulator.chooseOffensiveAction(attackingPlayer,
						attackPlayersAssetsOfMatch);

				if (offensiveTry.getName().equals(GameConfiguration.FOULDRAW)) {
					injuryManager.simulateInjury(playersNewAssets, attackingPlayer, GameConfiguration.FOULDRAW);
				}
				if (actionSimulator.simulateShot(attackingPlayer, offensiveTry, defensivePlayers)) {
					Player assistPlayer = null;
					if (offensiveTry.getName().equals(GameConfiguration.THREEPOINT)
							|| offensiveTry.getName().equals(GameConfiguration.TWOPOINT)) {
						if (isAssist()) {
							assistPlayer = eventSimulator.chooseAssistPlayer(attackingPlayer, attackPlayersAssetsOfMatch);
						}
					}

					if (offensiveTry.getName().equals(GameConfiguration.THREEPOINT)) {
						actionResults.add(new PointScored(GameConfiguration.SCORED_ACTION, 3, attackingPlayer,
								assistPlayer));
					} else {
						actionResults.add(new PointScored(GameConfiguration.SCORED_ACTION, 2, attackingPlayer,
								assistPlayer));
					}

				}

				else {
					actionResults.add(new MissedShot("missed shot", attackingPlayer));
					if (isBlock()) {
						Player blockingPlayer = eventSimulator.chooseBlockingPlayer(defensivePlayersAssetsOfMatch);
						actionResults.add(new Block(GameConfiguration.BLOCK_ACTION, blockingPlayer));
					} else {
						Player reboundPlayer = eventSimulator.chooseRebounder(attackPlayersAssetsOfMatch,
								defensivePlayersAssetsOfMatch);
						if (attackingPlayers.containsValue(reboundPlayer)) {
							actionResults.add(new Rebound(GameConfiguration.OFFENSIVE_REBOUND_ACTION, reboundPlayer,
									attackingPlayer));
							injuryManager.simulateInjury(playersNewAssets, reboundPlayer,
									GameConfiguration.OFFENSIVE_REBOUND_ACTION);
						}

						else {
							actionResults.add(new Rebound(GameConfiguration.DEFENSIVE_REBOUND_ACTION, reboundPlayer,
									attackingPlayer));
							injuryManager.simulateInjury(playersNewAssets, reboundPlayer,
									GameConfiguration.DEFENSIVE_REBOUND_ACTION);
						}
					}
				}

				for (ActionResult actionResult : actionResults) {
					actionResult.setOffensiveAction(offensiveTry);
				}
			}
		}

		for (ActionResult actionResult : actionResults) {
			actionResult.setActionTime(actionTime);
		}

		return actionResults;
	}

	private static HashMap<Player, Asset> createMapOfPlayerAsset(ArrayList<Player> players) {
		HashMap<Player, Asset> assets = new HashMap<Player, Asset>();
		for (Player player : players) {
			Asset asset = player.getCurrentSeasonAssets().getMinutesPlayedPerMatch() != 0 ? player.getCurrentSeasonAssets()
					: player.getPreSeasonAssets();
			assets.put(player, asset);
		}
		return assets;
	}

	private void updateAssestAfterAction(ActionResult actionResult, HashMap<Player, Asset> playersNewAssets) {
		ActionResultVisitor<Void> assetUpdateVisitor = new AssetUpdateVisitor(playersNewAssets);
		actionResult.accept(assetUpdateVisitor);
	}

	private static HashMap<Player, Asset> createMapNewAssets(ArrayList<Player> players) {
		HashMap<Player, Asset> teamPlayersNewAsset = new HashMap<Player, Asset>();
		for (Player player : players) {
			teamPlayersNewAsset.put(player, new Asset());
		}
		return teamPlayersNewAsset;
	}

	private static void updateGameResult(GameResult gameResult, ActionResult actionResult,
			ArrayList<Player> homeTeamPlayers, ArrayList<Player> awayTeamPlayers) {
		GameResultVisitor visitor = new GameResultVisitor(gameResult, homeTeamPlayers, awayTeamPlayers);
		actionResult.accept(visitor);
	}

	private GameResult simulateQuarter(Team homeTeam, Team awayTeam, HashMap<Player, Asset> playersNewAssets) {
		ArrayList<Player> homeTeamPlayers = lineupSelector.choosePlayerToPlay(homeTeam, awayTeam);
		ArrayList<Player> awayTeamPlayers = lineupSelector.choosePlayerToPlay(awayTeam, homeTeam);
		HashMap<Player, Asset> homePlayersAssetsOfMatch = createMapOfPlayerAsset(homeTeamPlayers);
		HashMap<Player, Asset> awayPlayersAssetsOfMatch = createMapOfPlayerAsset(awayTeamPlayers);

		int quarterTime = GameConfiguration.QUARTER_DURATION;
		boolean homeTeamStart = (Math.random() < 0.5);
		String possession;
		if (homeTeamStart) {
			possession = "home";

		} else {
			possession = "away";
		}
		TreeMap<Double, Player> attackingPlayers;
		TreeMap<Double, Player> defensivePlayers;
		HashMap<Player, Asset> attackPlayersAssetsOfMatch;
		HashMap<Player, Asset> defensivePlayersAssetsOfMatch;

		GameResult gameResult = new GameResult();

		while (quarterTime > 0) {
			if (possession.equals("home")) {
				attackingPlayers = lineupSelector.sortPlayersAccordingToAttack(homeTeamPlayers);
				defensivePlayers = lineupSelector.sortPlayersAccordingToDefense(awayTeamPlayers);

				attackPlayersAssetsOfMatch = homePlayersAssetsOfMatch;
				defensivePlayersAssetsOfMatch = awayPlayersAssetsOfMatch;

			} else {
				attackingPlayers = lineupSelector.sortPlayersAccordingToAttack(awayTeamPlayers);
				defensivePlayers = lineupSelector.sortPlayersAccordingToDefense(homeTeamPlayers);

				attackPlayersAssetsOfMatch = awayPlayersAssetsOfMatch;
				defensivePlayersAssetsOfMatch = homePlayersAssetsOfMatch;

			}
			Player attackingPlayer = eventSimulator.chooseAttackingPlayer(attackingPlayers);

			ArrayList<ActionResult> actionResults = simulateAction(attackingPlayer, attackingPlayers, defensivePlayers,
					quarterTime,
					attackPlayersAssetsOfMatch, defensivePlayersAssetsOfMatch, playersNewAssets);
			ActionResult terminalAction = actionResults.get(actionResults.size() - 1);

			if (terminalAction.getName().equals(GameConfiguration.SCORED_ACTION)
					|| terminalAction.getName().equals(GameConfiguration.DEFENSIVE_REBOUND_ACTION)
					|| terminalAction.getName().equals(GameConfiguration.TURNOVER_ACTION)) {
				if (possession.equals("home")) {
					possession = "away";
				} else {
					possession = "home";
				}
			}
			healthManager.updateFatigue(homeTeamPlayers, awayTeamPlayers, terminalAction.getActionTime());
			healthManager.addMinutesPlayed(homeTeamPlayers, awayTeamPlayers, playersNewAssets,
					terminalAction.getActionTime());

			lineupSelector.updatePlayers(awayTeam, awayTeamPlayers);
			awayPlayersAssetsOfMatch = createMapOfPlayerAsset(awayTeamPlayers);
			lineupSelector.updatePlayers(homeTeam, homeTeamPlayers);
			homePlayersAssetsOfMatch = createMapOfPlayerAsset(homeTeamPlayers);

			for (ActionResult actionResult : actionResults) {
				updateAssestAfterAction(actionResult, playersNewAssets);
				updateGameResult(gameResult, actionResult, homeTeamPlayers, awayTeamPlayers);
				gameResult.addActions(actionResult);
			}

			if (terminalAction.getName().equals(GameConfiguration.END_OF_TIME_ACTION)) {
				quarterTime = 0;
			} else {
				quarterTime -= terminalAction.getActionTime();
			}

		}
		return gameResult;
	}

	private void updateCurrentSeasonAsset(Team team, HashMap<Player, Asset> playersNewAssets) {
		for (Player player : team.getCurrentPlayers().values()) {
			PlayerUtilitary.updateAsset(player, playersNewAssets.get(player));
			// faire màj note du joeur
		}
	}

	public void simulateGame(Game game) {
		GameResult[] quarterResults = new GameResult[4];
		Team homeTeam = game.getGameContext().getHomeTeam();
		Team awayTeam = game.getGameContext().getAwayTeam();
		HashMap<Player, Asset> playersNewAssets = createMapNewAssets(playerRepositery.getAllPlayers());

		healthManager.initializeHealth(homeTeam, awayTeam);

		for (int index = 0; index < 4; index++) {
			quarterResults[index] = simulateQuarter(homeTeam, awayTeam, playersNewAssets);
			if (index == 1) {
				healthManager.updateRest(15, homeTeam, awayTeam);
			} else {
				healthManager.updateRest(2, homeTeam, awayTeam);
			}
		}
		game.setQuarterResults(quarterResults);
		int totalHome = totalHome(game);
		int totalAway = totalAway(game);
		game.setHomeFinalScore(totalHome);
		game.setAwayFinalScore(totalAway);

		TeamPerformance homePerformance = homeTeam.getTeamPerformance();
		TeamPerformance awayPerformance = awayTeam.getTeamPerformance();

		homeTeam.getTeamPerformance().incrementNmberPlayedGames();
		awayTeam.getTeamPerformance().incrementNmberPlayedGames();
		if (totalHome > totalAway) {
			int scoreDifference = totalHome - totalAway;
			homePerformance.incrementNumberWin();
			awayPerformance.incrementNumberLose();
			TeamUtilitary.updatePerformanceRating(homeTeam, awayTeam, 1, scoreDifference, awayTeam.getCurrentPopularity());
			TeamUtilitary.updatePerformanceRating(awayTeam, homeTeam, -1, scoreDifference,
					homeTeam.getCurrentPopularity());
			TeamUtilitary.updateStreak(homeTeam, true);
			TeamUtilitary.updateStreak(awayTeam, false);
		} else if (totalAway > totalHome) {
			int scoreDifference = totalAway - totalHome;
			homeTeam.getTeamPerformance().incrementNumberLose();
			awayTeam.getTeamPerformance().incrementNumberWin();
			TeamUtilitary.updatePerformanceRating(homeTeam, awayTeam, -1, scoreDifference,
					awayTeam.getCurrentPopularity());
			TeamUtilitary.updatePerformanceRating(awayTeam, homeTeam, 1, scoreDifference, homeTeam.getCurrentPopularity());
			TeamUtilitary.updateStreak(homeTeam, false);
			TeamUtilitary.updateStreak(awayTeam, true);
		} else {
			TeamUtilitary.updatePerformanceRating(homeTeam, awayTeam, 0, 0, awayTeam.getCurrentPopularity());
			TeamUtilitary.updatePerformanceRating(awayTeam, homeTeam, 0, 0, homeTeam.getCurrentPopularity());
		}

		updateCurrentSeasonAsset(homeTeam, playersNewAssets);
		updateCurrentSeasonAsset(awayTeam, playersNewAssets);

	}

	private static int totalHome(Game game) {
		int totalHome = 0;
		for (GameResult gameResult : game.getQuarterResults())
			if (gameResult != null)
				totalHome += gameResult.getScorehomeTeam();
		return totalHome;
	}

	private static int totalAway(Game game) {
		int totalAway = 0;
		for (GameResult gameResult : game.getQuarterResults())
			if (gameResult != null)
				totalAway += gameResult.getScoreAwayTeam();
		return totalAway;
	}

}

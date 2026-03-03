package process.simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

import config.SimulationConfiguration;
import data.player.Asset;
import data.player.HealthStatus;
import data.player.Injury;
import data.player.Player;
import data.sport.play.OffensiveTry;
import data.sport.play.action.ActionResult;
import data.sport.play.action.Block;
import data.sport.play.action.EndOfTime;
import data.sport.play.action.PointScored;
import data.sport.play.action.Rebound;
import data.sport.play.action.Turnover;
import data.sport.setup.Game;
import data.sport.setup.GameResult;
import data.team.Team;
import data.team.finance.financialprofil.AmbitiousProfil;
import data.team.finance.financialprofil.BalancedProfil;
import data.team.finance.financialprofil.FinancialProfil;
import process.repositery.CurrentSeasonAssetRepositery;
import process.repositery.PlayerRepositery;
import process.repositery.PreSeasonAssetRepositery;
import process.utilitary.FinanceUtilitary;
import process.utilitary.PlayerUtilitary;
import process.utilitary.TeamUtilitary;
import process.visitor.actionresult.ActionResultVisitor;
import process.visitor.actionresult.AssetUpdateVisitor;
import process.visitor.actionresult.GameResultVisitor;

public class GameSimulator {

	private PlayerRepositery playerRepositery = PlayerRepositery.getInstance();
	private PreSeasonAssetRepositery preSeasonAssetRepositery = PreSeasonAssetRepositery.getInstance();
	private CurrentSeasonAssetRepositery currentSeasonAssetRepositery = CurrentSeasonAssetRepositery.getInstance();

	private static ArrayList<Player> choosePlayerToPlay(Team team, Team opponent) {
		String opponentProfile = TeamUtilitary.getTeamSportProfile(opponent);
		double averageSalary = FinanceUtilitary.getAverageSalary(team);
		TreeMap<Double, Player> scoredPlayers = new TreeMap<Double, Player>(Collections.reverseOrder());

		FinancialProfil teamFinancialProfil = team.getTeamFinance().getFinancialProfil();
		double ecoWeight, matchProfileWeight;
		if (teamFinancialProfil instanceof AmbitiousProfil) {
			ecoWeight = 0.6;
			matchProfileWeight = 0.4;
		} else if (teamFinancialProfil instanceof BalancedProfil) {
			ecoWeight = 0.4;
			matchProfileWeight = 0.6;
		} else {
			ecoWeight = 0.2;
			matchProfileWeight = 0.8;
		}

		for (Player player : team.getPlayers().values()) {
			double economicFactor = player.getSalary() / averageSalary;

			double playerAttackNote = PlayerUtilitary.getPlayerAttackNote(player);
			double playerDefenseNote = PlayerUtilitary.getPlayerDefenseNote(player);

			double matchProfileScore;
			switch (opponentProfile) {
				case SimulationConfiguration.TEAM_DEFENSIVE_MATCH_PROFIL:
					matchProfileScore = playerDefenseNote;
					break;
				case SimulationConfiguration.TEAM_OFFENSIVE_MATCH_PROFIL:
					matchProfileScore = playerAttackNote;
					break;
				default:
					matchProfileScore = (playerAttackNote + playerDefenseNote) / 2;
			}

			double selectionScore = economicFactor * ecoWeight + matchProfileScore * matchProfileWeight;
			scoredPlayers.put(selectionScore, player);
		}

		ArrayList<Player> chosenPlayers = new ArrayList<Player>();
		int numberOfChosenPlayer = 0;
		for (Player player : scoredPlayers.values()) {
			if (player.getHealthStatus().isInjured()) {
				continue;
			}
			chosenPlayers.add(player);
			numberOfChosenPlayer++;
			if (numberOfChosenPlayer >= 5) {
				break;
			}
		}
		return chosenPlayers;
	}

	private static Player chooseDefendingPlayer(TreeMap<Double, Player> defensivePlayers) {
		ArrayList<Player> players = new ArrayList<Player>(defensivePlayers.values());
		int randomIndex = (int) (Math.random() * players.size());
		return players.get(randomIndex);
	}

	private static boolean effectiveTurnover(Player attackingPlayer, Player defendingPlayer) {
		double playerDefenseNote = Math.min(PlayerUtilitary.getPlayerDefenseNote(defendingPlayer), 2);
		double playerAttackNote = Math.min(PlayerUtilitary.getPlayerAttackNote(attackingPlayer), 2);

		if (playerDefenseNote > playerAttackNote) {
			if (Math.random() < 0.7) {
				return true;
			}
		}
		return false;
	}

	private static OffensiveTry chooseOffensiveAction(Player attackingPlayer,
			HashMap<Player, Asset> attackPlayersAssetsOfMatch) {
		Asset asset = attackPlayersAssetsOfMatch.get(attackingPlayer);
		double pointsPerMinute = asset.getMinutesPlayedPerMatch() > 0
				? asset.getPointPerMatch() / asset.getMinutesPlayedPerMatch()
				: 0.0;
		double scoringFactor = 1.0 + (asset.getPointPerMatch() / 30);
		double turnoverFactor = 1.0 + (asset.getLostBallPerMatch() / SimulationConfiguration.MAX_TURNOVER_PER_MATCH);

		double threePointWeight = (SimulationConfiguration.THREEPOINT_PROBABILITY * scoringFactor) * 1.0;
		double twoPointWeight = (SimulationConfiguration.TWOPOINT_PROBABILITY * scoringFactor) * 1.2;
		double foulDrawWeight = (SimulationConfiguration.FOULDRAW_PROBABILITY * turnoverFactor);

		switch (attackingPlayer.getPosition()) {
			case SimulationConfiguration.PLAYER_POSITION_CENTER:
				threePointWeight *= 0.5;
				twoPointWeight *= 1.4;
				foulDrawWeight *= 1.2;
				break;

			case SimulationConfiguration.PLAYER_POSITION_POINT_GUARD:
				threePointWeight *= 1.3;
				twoPointWeight *= 1;
				foulDrawWeight *= 1.1;
				break;

			case SimulationConfiguration.PLAYER_POSITION_POWER_FORWARD:
				threePointWeight *= 0.8;
				twoPointWeight *= 1.4;
				foulDrawWeight *= 1.15;
				break;

			case SimulationConfiguration.PLAYER_POSITION_SHOOTING_GUARD:
				threePointWeight *= 1.4;
				twoPointWeight *= 1;
				foulDrawWeight *= 1;
				break;

			case SimulationConfiguration.PLAYER_POSITION_SMALL_FORWARD:
				threePointWeight *= 1.1;
				twoPointWeight *= 1.1;
				foulDrawWeight *= 1.1;
				break;
		}
		double total = threePointWeight + foulDrawWeight + twoPointWeight;

		double random = Math.random() * total;
		if (random < threePointWeight) {
			return new OffensiveTry(SimulationConfiguration.THREEPOINT);
		}
		if (random < foulDrawWeight + threePointWeight) {
			return new OffensiveTry(SimulationConfiguration.FOULDRAW);
		}
		return new OffensiveTry(SimulationConfiguration.TWOPOINT);

	}

	private static void updateFatigue(int minutesPlayed, ArrayList<Player> homePlayers, ArrayList<Player> awayPlayers) {
		for (Player attackPlayer : homePlayers) {
			PlayerUtilitary.updateFatigue(minutesPlayed, attackPlayer);
		}
		for (Player defensePlayer : awayPlayers) {
			PlayerUtilitary.updateFatigue(minutesPlayed, defensePlayer);
		}
	}

	public static void updateRest(int restMinutes, Team homeTeam, Team awayTeam) {
		for (Player player : homeTeam.getPlayers().values()) {
			PlayerUtilitary.updateRest(restMinutes, player);
		}
		for (Player player : awayTeam.getPlayers().values()) {
			PlayerUtilitary.updateRest(restMinutes, player);
		}
	}

	private static Player chooseAssistPlayer(Player scorerPlayer, HashMap<Player, Asset> attackPlayersAssetsOfMatch) {
		HashMap<Player, Double> assistPlayers = new HashMap<Player, Double>();
		double totalWeight = 0;

		for (Player player : attackPlayersAssetsOfMatch.keySet()) {
			Asset asset = attackPlayersAssetsOfMatch.get(player);
			double assistProbability = SimulationConfiguration.ASSIST_PROBABILITY;
			if (player.getName().equals(scorerPlayer.getName())) {
				continue;
			}
			switch (player.getPosition()) {
				case SimulationConfiguration.PLAYER_POSITION_POINT_GUARD:
					assistProbability *= 1.4;
					break;

				case SimulationConfiguration.PLAYER_POSITION_SHOOTING_GUARD:
					assistProbability *= 1.2;
					break;

				case SimulationConfiguration.PLAYER_POSITION_SMALL_FORWARD:
					assistProbability *= 1.0;
					break;

				case SimulationConfiguration.PLAYER_POSITION_POWER_FORWARD:
					assistProbability *= 0.8;
					break;

				case SimulationConfiguration.PLAYER_POSITION_CENTER:
					assistProbability *= 0.6;
					break;
			}
			assistProbability += asset.getAssistPerMatch() / SimulationConfiguration.MAX_ASSIST_PER_MATCH;
			assistPlayers.put(player, assistProbability);
			totalWeight += assistProbability;
		}
		double random = Math.random() * totalWeight;
		double cumulative = 0.0;
		Player lastPlayer = null;
		for (Player player : assistPlayers.keySet()) {
			lastPlayer = player;
			cumulative += assistPlayers.get(player) / totalWeight;
			if (random <= cumulative) {
				return player;
			}
		}
		return lastPlayer;
	}

	private static Player chooseAttackingPlayer(TreeMap<Double, Player> attackingPlayers) {
		double random = Math.random();
		for (Double key : attackingPlayers.keySet()) {
			if (random <= key) {
				return attackingPlayers.get(key);
			}
		}
		return attackingPlayers.lastEntry().getValue();
	}

	private static double defensingPlayersNote(TreeMap<Double, Player> defensivePlayers) {
		double sumOfNote = 0;
		double numberOfPlayer = 0;
		double note;
		for (Player player : defensivePlayers.values()) {
			sumOfNote += PlayerUtilitary.getPlayerDefenseNote(player);
			numberOfPlayer++;
		}
		note = sumOfNote / numberOfPlayer;
		return note;
	}

	private static boolean simulateShot(Player attackingPlayer, OffensiveTry action,
			TreeMap<Double, Player> defensivePlayers) {
		Asset asset = attackingPlayer.getCurrentSeasonAssets().getMinutesPlayedPerMatch() > 0
				? attackingPlayer.getCurrentSeasonAssets()
				: attackingPlayer.getPreSeasonAssets();
		double trueShootingPercentage = asset.getTrueShootingPercentage();
		double shotProbability;
		if (action.getName().equals(SimulationConfiguration.THREEPOINT)) {
			shotProbability = SimulationConfiguration.THREEPOINT_PROBABILITY_SUCCESS;
		} else if (action.getName().equals(SimulationConfiguration.TWOPOINT)) {
			shotProbability = SimulationConfiguration.TWO_PROBABILITY_SUCCESS;
		} else {
			shotProbability = SimulationConfiguration.FOULDRAW_PROBABILITY_SUCESS;
		}
		shotProbability += (trueShootingPercentage * 0.5);

		double defenseNote = defensingPlayersNote(defensivePlayers);
		shotProbability -= defenseNote * 0.05;

		shotProbability -= attackingPlayer.getHealthStatus().getFatigue();

		return Math.random() < shotProbability;

	}

	private static Player chooseRebounder(HashMap<Player, Asset> attackPlayersAssetsOfMatch,
			HashMap<Player, Asset> defensivePlayersAssetsOfMatch) {
		boolean offensiveRebound = Math.random() < SimulationConfiguration.OFFENSIVE_REBOUND_PROBABILITY;
		if (offensiveRebound) {
			return chooseOffensiveRebounder(attackPlayersAssetsOfMatch);
		}
		return chooseDefensiveRebounder(defensivePlayersAssetsOfMatch);
	}

	private static Player chooseOffensiveRebounder(HashMap<Player, Asset> attackPlayersAssetsOfMatch) {
		HashMap<Player, Double> offensiveRebounderPLayers = new HashMap<Player, Double>();
		double total = 0;
		for (Player player : attackPlayersAssetsOfMatch.keySet()) {

			Asset asset = attackPlayersAssetsOfMatch.get(player);
			double reboundProbability = SimulationConfiguration.OFFENSIVE_REBOUND_PROBABILITY;

			switch (player.getPosition()) {
				case SimulationConfiguration.PLAYER_POSITION_POINT_GUARD:
					reboundProbability *= 0.6;
					break;

				case SimulationConfiguration.PLAYER_POSITION_SHOOTING_GUARD:
					reboundProbability *= 0.8;
					break;

				case SimulationConfiguration.PLAYER_POSITION_SMALL_FORWARD:
					reboundProbability *= 1.0;
					break;

				case SimulationConfiguration.PLAYER_POSITION_POWER_FORWARD:
					reboundProbability *= 1.2;
					break;

				case SimulationConfiguration.PLAYER_POSITION_CENTER:
					reboundProbability *= 1.4;
					break;
			}

			reboundProbability += (asset.getReboundPerMatch() / SimulationConfiguration.MAX_REBOUND_PER_MATCH);
			offensiveRebounderPLayers.put(player, reboundProbability);
			total += reboundProbability;
		}
		double random = Math.random() * total;
		double cumulative = 0;
		Player lastPlayer = null;
		for (Player player : offensiveRebounderPLayers.keySet()) {
			lastPlayer = player;
			cumulative += offensiveRebounderPLayers.get(player);
			if (random <= cumulative) {
				return player;
			}
		}
		return lastPlayer;
	}

	private static Player chooseDefensiveRebounder(HashMap<Player, Asset> defensivePlayersAssetsOfMatch) {
		HashMap<Player, Double> defensiveRebounderPLayers = new HashMap<Player, Double>();
		double total = 0;
		for (Player player : defensivePlayersAssetsOfMatch.keySet()) {

			Asset asset = defensivePlayersAssetsOfMatch.get(player);
			double reboundProbability = SimulationConfiguration.DEFENSIVE_REBOUND_PROBABILITY;

			switch (player.getPosition()) {
				case SimulationConfiguration.PLAYER_POSITION_POINT_GUARD:
					reboundProbability *= 0.6;
					break;

				case SimulationConfiguration.PLAYER_POSITION_SHOOTING_GUARD:
					reboundProbability *= 0.8;
					break;

				case SimulationConfiguration.PLAYER_POSITION_SMALL_FORWARD:
					reboundProbability *= 1.0;
					break;

				case SimulationConfiguration.PLAYER_POSITION_POWER_FORWARD:
					reboundProbability *= 1.2;
					break;

				case SimulationConfiguration.PLAYER_POSITION_CENTER:
					reboundProbability *= 1.4;
					break;
			}
			reboundProbability += (asset.getReboundPerMatch() / SimulationConfiguration.MAX_REBOUND_PER_MATCH);
			defensiveRebounderPLayers.put(player, reboundProbability);
			total += reboundProbability;
		}
		double random = Math.random() * total;
		double cumulative = 0;
		Player lastPlayer = null;
		for (Player player : defensiveRebounderPLayers.keySet()) {
			cumulative += defensiveRebounderPLayers.get(player);
			if (random <= cumulative) {
				return player;
			}
		}
		return lastPlayer;
	}

	private static void simulateInjury(Player player, String typeAction, HashMap<Player, Asset> playersNewAssets) {
		Asset asset = playersNewAssets.get(player);
		double fatigueFactor = 1.0 + player.getHealthStatus().getFatigue();
		double minutesFactor = 1.0 + asset.getMinutesPlayedPerMatch();
		double typeActionFactor;
		switch (typeAction) {
			case SimulationConfiguration.FOULDRAW:
				typeActionFactor = 1.05;
				break;
			case SimulationConfiguration.DEFENSIVE_REBOUND_ACTION:
				typeActionFactor = 1.5;
				break;
			case SimulationConfiguration.OFFENSIVE_REBOUND_ACTION:
				typeActionFactor = 1.5;
				break;
			default:
				typeActionFactor = 1.0;
		}
		double injuryProbability = SimulationConfiguration.INJURY_PROBABILITY * fatigueFactor * minutesFactor
				* typeActionFactor;
		if (Math.random() < injuryProbability) {
			injurePlayer(player);
		}
	}

	private static void injurePlayer(Player player) {
		double random = Math.random();
		Injury injury;
		if (random < 0.7) {
			injury = new Injury(SimulationConfiguration.MINOR_INJURY, SimulationConfiguration.MINOR_INJURY_DURATION);
		} else if (random < 0.90) {
			injury = new Injury(SimulationConfiguration.MEDIUM_INJURY, SimulationConfiguration.MEDIUM_INJURY_DURATION);
		} else {
			injury = new Injury(SimulationConfiguration.SERIOUS_INJURY, SimulationConfiguration.SERIOUS_INJURY_DURATION);
		}
		player.getHealthStatus().setInjured(true);
		player.getHealthStatus().setInjury(injury);
	}

	private void addMinutesPlayed(ArrayList<Player> homeTeamPlayers, ArrayList<Player> awayTeamPlayers, int actionTime,
			HashMap<Player, Asset> playersNewAssets) {
		for (Player player : homeTeamPlayers) {
			Asset asset = playersNewAssets.get(player);
			asset.setMinutesPlayedPerMatch(asset.getMinutesPlayedPerMatch() + (actionTime / 60));
		}
		for (Player player : awayTeamPlayers) {
			Asset asset = playersNewAssets.get(player);
			asset.setMinutesPlayedPerMatch(asset.getMinutesPlayedPerMatch() + (actionTime / 60));
		}

	}

	private static boolean isAssist() {
		return Math.random() < SimulationConfiguration.ASSIST_PROBABILITY;
	}

	private static boolean isBlock() {
		return Math.random() < SimulationConfiguration.BLOCK_PROBABILTY;
	}

	private static Player chooseBlockingPlayer(HashMap<Player, Asset> defensivePlayersAssetsOfMatch) {
		HashMap<Player, Double> blockingPlayers = new HashMap<Player, Double>();
		double total = 0;

		for (Player player : defensivePlayersAssetsOfMatch.keySet()) {
			Asset asset = defensivePlayersAssetsOfMatch.get(player);
			double blockProbability = SimulationConfiguration.BLOCK_PROBABILTY;
			switch (player.getPosition()) {
				case SimulationConfiguration.PLAYER_POSITION_POINT_GUARD:
					blockProbability *= 0.3;
					break;
				case SimulationConfiguration.PLAYER_POSITION_SHOOTING_GUARD:
					blockProbability *= 0.5;
					break;
				case SimulationConfiguration.PLAYER_POSITION_SMALL_FORWARD:
					blockProbability *= 0.7;
					break;
				case SimulationConfiguration.PLAYER_POSITION_POWER_FORWARD:
					blockProbability *= 1.0;
					break;
				case SimulationConfiguration.PLAYER_POSITION_CENTER:
					blockProbability *= 1.3;
					break;
			}

			blockProbability += (asset.getBlockPerMatch() / SimulationConfiguration.MAX_BLOCK_PER_MATCH);
			blockingPlayers.put(player, blockProbability);
			total += blockProbability;
		}

		double random = Math.random() * total;
		double cumulative = 0;
		Player lastPlayer = null;

		for (Player player : blockingPlayers.keySet()) {
			lastPlayer = player;
			cumulative += blockingPlayers.get(player);
			if (random <= cumulative) {
				return player;
			}
		}

		return lastPlayer;
	}

	private ActionResult simulateAction(Player attackingPlayer, TreeMap<Double, Player> attackingPlayers,
			TreeMap<Double, Player> defensivePlayers, int quarterTimeRemaining,
			HashMap<Player, Asset> attackPlayersAssetsOfMatch, HashMap<Player, Asset> defensivePlayersAssetsOfMatch,
			HashMap<Player, Asset> playersNewAssets) {
		int actionTime = 14 + (int) (Math.random() * 10);
		ActionResult actionResult;
		if (quarterTimeRemaining <= actionTime) {
			actionResult = new EndOfTime(SimulationConfiguration.END_OF_TIME_ACTION);
		}

		else {
			Player defendingPlayer = chooseDefendingPlayer(defensivePlayers);

			if (effectiveTurnover(attackingPlayer, defendingPlayer)) {
				actionResult = new Turnover(SimulationConfiguration.TURNOVER_ACTION, attackingPlayer, defendingPlayer);
			} else {

				OffensiveTry offensiveTry = chooseOffensiveAction(attackingPlayer, attackPlayersAssetsOfMatch);
				if (offensiveTry.getName().equals(SimulationConfiguration.FOULDRAW)) {
					simulateInjury(attackingPlayer, SimulationConfiguration.FOULDRAW, playersNewAssets);
				}
				if (simulateShot(attackingPlayer, offensiveTry, defensivePlayers)) {
					Player assistPlayer = null;
					if (offensiveTry.getName().equals(SimulationConfiguration.THREEPOINT)
							|| offensiveTry.getName().equals(SimulationConfiguration.TWOPOINT)) {
						if (isAssist()) {
							assistPlayer = chooseAssistPlayer(attackingPlayer, attackPlayersAssetsOfMatch);
						}
					}

					if (offensiveTry.getName().equals(SimulationConfiguration.THREEPOINT)) {
						actionResult = new PointScored(SimulationConfiguration.SCORED_ACTION, 3, attackingPlayer,
								assistPlayer);
					} else {
						actionResult = new PointScored(SimulationConfiguration.SCORED_ACTION, 2, attackingPlayer,
								assistPlayer);
					}

				}

				else {
					if (isBlock()) {
						Player blockingPlayer = chooseBlockingPlayer(defensivePlayersAssetsOfMatch);
						actionResult = new Block(SimulationConfiguration.BLOCK_ACTION, blockingPlayer);
					} else {
						Player reboundPlayer = chooseRebounder(attackPlayersAssetsOfMatch, defensivePlayersAssetsOfMatch);
						if (attackingPlayers.containsValue(reboundPlayer)) {
							actionResult = new Rebound(SimulationConfiguration.OFFENSIVE_REBOUND_ACTION, reboundPlayer,
									attackingPlayer);
							simulateInjury(reboundPlayer, SimulationConfiguration.OFFENSIVE_REBOUND_ACTION, playersNewAssets);
						}

						else {
							actionResult = new Rebound(SimulationConfiguration.DEFENSIVE_REBOUND_ACTION, reboundPlayer,
									attackingPlayer);
							simulateInjury(reboundPlayer, SimulationConfiguration.DEFENSIVE_REBOUND_ACTION, playersNewAssets);
						}
					}
				}

				actionResult.setOffensiveAction(offensiveTry);
			}
		}

		actionResult.setActionTime(actionTime);

		return actionResult;
	}

	private static void updatePlayers(Team team, ArrayList<Player> players, HashMap<Player, Asset> assets) {
		ArrayList<Player> playersToRemove = new ArrayList<Player>();
		for (Player player : players) {
			if (player.getHealthStatus().isInjured()) {
				playersToRemove.add(player);
			}
		}
		players.removeAll(playersToRemove);
		for (Player player : team.getPlayers().values()) {
			if (!player.getHealthStatus().isInjured() && !players.contains(player)) {
				players.add(player);
			}
		}
		assets.clear();
		assets.putAll(createMapOfPlayerAsset(players));
	}

	private static TreeMap<Double, Player> sortPlayersAccordingToAttack(ArrayList<Player> players) {
		TreeMap<Double, Player> attackingPlayers = new TreeMap<Double, Player>();
		double total = 0;

		for (Player player : players) {
			total += PlayerUtilitary.getPlayerAttackNote(player);
		}

		double cumulative = 0;

		for (Player player : players) {
			double attackNote = PlayerUtilitary.getPlayerAttackNote(player) / total;
			cumulative += attackNote;
			attackingPlayers.put(cumulative, player);
		}
		return attackingPlayers;
	}

	private static TreeMap<Double, Player> sortPlayersAccordingToDefense(ArrayList<Player> players) {
		TreeMap<Double, Player> defensivePlayers = new TreeMap<Double, Player>();
		double total = 0;

		for (Player player : players) {
			total += PlayerUtilitary.getPlayerDefenseNote(player);
		}

		double cumulative = 0;

		for (Player player : players) {
			double defenseNote = PlayerUtilitary.getPlayerDefenseNote(player) / total;
			cumulative += defenseNote;
			defensivePlayers.put(cumulative, player);
		}
		return defensivePlayers;
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
		ArrayList<Player> homeTeamPlayers = choosePlayerToPlay(homeTeam, awayTeam);
		ArrayList<Player> awayTeamPlayers = choosePlayerToPlay(awayTeam, homeTeam);
		HashMap<Player, Asset> homePlayersAssetsOfMatch = createMapOfPlayerAsset(homeTeamPlayers);
		HashMap<Player, Asset> awayPlayersAssetsOfMatch = createMapOfPlayerAsset(awayTeamPlayers);

		int quarterTime = SimulationConfiguration.QUARTER_DURATION;
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
				attackingPlayers = sortPlayersAccordingToAttack(homeTeamPlayers);
				defensivePlayers = sortPlayersAccordingToDefense(awayTeamPlayers);

				attackPlayersAssetsOfMatch = homePlayersAssetsOfMatch;
				defensivePlayersAssetsOfMatch = awayPlayersAssetsOfMatch;

			} else {
				attackingPlayers = sortPlayersAccordingToAttack(awayTeamPlayers);
				defensivePlayers = sortPlayersAccordingToDefense(homeTeamPlayers);

				attackPlayersAssetsOfMatch = awayPlayersAssetsOfMatch;
				defensivePlayersAssetsOfMatch = homePlayersAssetsOfMatch;

			}

			Player attackingPlayer = chooseAttackingPlayer(attackingPlayers);
			ActionResult actionResult = simulateAction(attackingPlayer, attackingPlayers, defensivePlayers, quarterTime,
					attackPlayersAssetsOfMatch, defensivePlayersAssetsOfMatch, playersNewAssets);

			if (actionResult.getName().equals(SimulationConfiguration.DEFENSIVE_REBOUND_ACTION) ||
					actionResult.getName().equals(SimulationConfiguration.TURNOVER_ACTION)) {
				if (possession.equals("home")) {
					possession = "away";
				} else {
					possession = "home";
				}
			}
			updateFatigue(actionResult.getActionTime() / 60, homeTeamPlayers, awayTeamPlayers);
			updatePlayers(awayTeam, awayTeamPlayers, awayPlayersAssetsOfMatch);
			updatePlayers(homeTeam, homeTeamPlayers, homePlayersAssetsOfMatch);
			updateAssestAfterAction(actionResult, playersNewAssets);
			updateGameResult(gameResult, actionResult, homeTeamPlayers, awayTeamPlayers);
			addMinutesPlayed(homeTeamPlayers, awayTeamPlayers, actionResult.getActionTime(), playersNewAssets);
			gameResult.addActions(actionResult);
			OffensiveTry action = actionResult.getOffensiveAction();
			if (action != null) {
			} else {
			}

			if (actionResult.getName().equals(SimulationConfiguration.END_OF_TIME_ACTION)) {
				quarterTime = 0;
			} else {
				quarterTime -= actionResult.getActionTime();
			}

		}
		return gameResult;
	}

	private void updateCurrentSeasonAsset(Team team, HashMap<Player, Asset> playersNewAssets) {

		for (Player player : team.getPlayers().values()) {
			PlayerUtilitary.updateAsset(player, playersNewAssets.get(player));
			// faire màj note du joeur
		}
	}

	private static void initializeHealth(Team team) {
		for (Player player : team.getPlayers().values()) {
			HealthStatus healthStatus = player.getHealthStatus();
			healthStatus.setFatigue(0);
			healthStatus.getInjury().setInjuryDuration(healthStatus.getInjury().getInjuryDuration() - 1);
			if (healthStatus.getInjury().getInjuryDuration() <= 0) {
				healthStatus.setInjured(false);
			}
		}
	}

	public void simulateGame(Game game) {
		GameResult[] quarterResults = new GameResult[4];
		Team homeTeam = game.getGameContext().getHomeTeam();
		initializeHealth(homeTeam);
		Team awayTeam = game.getGameContext().getAwayTeam();
		initializeHealth(awayTeam);
		HashMap<Player, Asset> playersNewAssets = createMapNewAssets(playerRepositery.getAllPlayers());

		for (int index = 0; index < 4; index++) {
			quarterResults[index] = simulateQuarter(homeTeam, awayTeam, playersNewAssets);
			if (index == 1) {
				updateRest(15, homeTeam, awayTeam);
			} else {
				updateRest(2, homeTeam, awayTeam);
			}
		}
		game.setQuarterResults(quarterResults);
		int totalHome = totalHome(game);
		int totalAway = totalAway(game);
		game.setHomeFinalScore(totalHome);
		game.setAwayFinalScore(totalAway);

		homeTeam.getTeamPerformance().incrementNmberPlayedGames();
		awayTeam.getTeamPerformance().incrementNmberPlayedGames();
		if (totalHome > totalAway) {
			int scoreDifference = totalHome - totalAway;
			homeTeam.getTeamPerformance().incrementNumberWin();
			awayTeam.getTeamPerformance().incrementNumberLose();
			TeamUtilitary.updatePerformanceRating(homeTeam, awayTeam, 1, scoreDifference, awayTeam.getPopularity());
			TeamUtilitary.updatePerformanceRating(awayTeam, homeTeam, -1, scoreDifference, homeTeam.getPopularity());
		} else if (totalAway < totalHome) {
			int scoreDifference = totalAway - totalHome;
			homeTeam.getTeamPerformance().incrementNumberLose();
			awayTeam.getTeamPerformance().incrementNumberWin();
			TeamUtilitary.updatePerformanceRating(homeTeam, awayTeam, -1, scoreDifference, awayTeam.getPopularity());
			TeamUtilitary.updatePerformanceRating(awayTeam, homeTeam, 1, scoreDifference, homeTeam.getPopularity());
		} else {
			TeamUtilitary.updatePerformanceRating(homeTeam, awayTeam, 0, 0, awayTeam.getPopularity());
			TeamUtilitary.updatePerformanceRating(awayTeam, homeTeam, 0, 0, homeTeam.getPopularity());
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

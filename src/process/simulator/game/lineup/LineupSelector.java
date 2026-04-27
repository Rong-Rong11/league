package process.simulator.game.lineup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import config.GameConfiguration;
import data.player.Player;
import data.team.Team;
import data.team.finance.financialpolicy.FinancialPolicy;
import log.LoggerUtility;
import process.utility.FinanceUtility;
import process.utility.PlayerUtility;
import process.utility.TeamUtility;
import process.visitor.financialpolicy.GameEcoWeightVisitor;
import process.visitor.financialpolicy.GameMatchProfilWeightVisitor;

public class LineupSelector {
	private static final Logger logger = LoggerUtility.getLogger(LineupSelector.class, "text");

	public LineupSelector() {
	}

	public ArrayList<Player> choosePlayerToPlay(Team team, Team opponent) {
		ArrayList<Player> chosenPlayers = new ArrayList<Player>();

		if (team == null || opponent == null) {
			logger.warn("Returning empty lineup because team or opponent is null");
			return chosenPlayers;
		}

		String opponentProfile = TeamUtility.getTeamSportProfile(opponent);
		double averageSalary = FinanceUtility.getAverageSalary(team);
		TreeMap<Double, Player> scoredPlayers = new TreeMap<Double, Player>(Collections.reverseOrder());

		FinancialPolicy teamFinancialPolicy = team.getTeamFinance().getBehavior().getFinancialPolicy();
		double ecoWeight, matchProfileWeight;

		ecoWeight = teamFinancialPolicy.accept(new GameEcoWeightVisitor());
		matchProfileWeight = teamFinancialPolicy.accept(new GameMatchProfilWeightVisitor());

		for (Player player : team.getCurrentPlayers().values()) {
			if (player == null) {
				continue;
			}

			double economicFactor = player.getSalary() / averageSalary;
			double playerAttackNote = PlayerUtility.getPlayerAttackNote(player);
			double playerDefenseNote = PlayerUtility.getPlayerDefenseNote(player);
			double matchProfileScore;

			switch (opponentProfile) {
				case GameConfiguration.TEAM_DEFENSIVE_MATCH_PROFIL:
					matchProfileScore = playerDefenseNote;
					break;
				case GameConfiguration.TEAM_OFFENSIVE_MATCH_PROFIL:
					matchProfileScore = playerAttackNote;
					break;
				default:
					matchProfileScore = (playerAttackNote + playerDefenseNote) / 2;
			}

			double selectionScore = economicFactor * ecoWeight + matchProfileScore * matchProfileWeight;
			scoredPlayers.put(selectionScore, player);
		}

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

		if (chosenPlayers.size() < 5) {
			logger.warn("Selected less than 5 players for " + team.getName());
		}

		return chosenPlayers;
	}

	public void updatePlayers(Team team, ArrayList<Player> players) {
		if (team == null || players == null) {
			logger.warn("Skipping lineup update because team or players list is null");
			return;
		}

		ArrayList<Player> playersToRemove = new ArrayList<Player>();

		for (Player player : players) {
			if (player == null || player.getHealthStatus() == null) {
				continue;
			}

			if (player.getHealthStatus().isInjured()) {
				playersToRemove.add(player);
			}
		}

		players.removeAll(playersToRemove);

		for (Player player : team.getCurrentPlayers().values()) {
			if (players.size() >= 5) {
				break;
			}

			if (player == null || player.getHealthStatus() == null) {
				continue;
			}

			if (!player.getHealthStatus().isInjured() && !players.contains(player)) {
				players.add(player);
			}
		}

		if (players.size() < 5) {
			logger.warn("Lineup still has less than 5 players after update for " + team.getName());
		}
	}

	public TreeMap<Double, Player> sortPlayersAccordingToAttack(ArrayList<Player> players) {
		TreeMap<Double, Player> attackingPlayers = new TreeMap<Double, Player>();

		if (players == null || players.isEmpty()) {
			logger.warn("Returning empty attacking player map because players list is null or empty");
			return attackingPlayers;
		}

		double total = 0;

		for (Player player : players) {
			if (player == null) {
				continue;
			}
			total += PlayerUtility.getPlayerAttackNote(player);
		}

		if (total <= 0) {
			logger.warn("Returning empty attacking player map because total attack score is non-positive");
			return attackingPlayers;
		}

		double cumulative = 0;

		for (Player player : players) {
			if (player == null) {
				continue;
			}

			double attackNote = PlayerUtility.getPlayerAttackNote(player) / total;
			cumulative += attackNote;
			attackingPlayers.put(cumulative, player);
		}

		return attackingPlayers;
	}

	public TreeMap<Double, Player> sortPlayersAccordingToDefense(ArrayList<Player> players) {
		TreeMap<Double, Player> defensivePlayers = new TreeMap<Double, Player>();

		if (players == null || players.isEmpty()) {
			logger.warn("Returning empty defensive player map because players list is null or empty");
			return defensivePlayers;
		}

		double total = 0;

		for (Player player : players) {
			if (player == null) {
				continue;
			}
			total += PlayerUtility.getPlayerDefenseNote(player);
		}

		if (total <= 0) {
			logger.warn("Returning empty defensive player map because total defense score is non-positive");
			return defensivePlayers;
		}

		double cumulative = 0;

		for (Player player : players) {
			if (player == null) {
				continue;
			}

			double defenseNote = PlayerUtility.getPlayerDefenseNote(player) / total;
			cumulative += defenseNote;
			defensivePlayers.put(cumulative, player);
		}

		return defensivePlayers;
	}
}

package process.simulator.gametools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import config.GameConfiguration;
import data.player.Player;
import data.team.Team;
import data.team.finance.financialpolicy.AmbitiousPolicy;
import data.team.finance.financialpolicy.BalancedPolicy;
import data.team.finance.financialpolicy.FinancialPolicy;
import process.utility.FinanceUtilitary;
import process.utility.PlayerUtilitary;
import process.utility.TeamUtilitary;

public class LineupSelector {

	public LineupSelector() {

	}

	public ArrayList<Player> choosePlayerToPlay(Team team, Team opponent) {
		String opponentProfile = TeamUtilitary.getTeamSportProfile(opponent);
		double averageSalary = FinanceUtilitary.getAverageSalary(team);
		TreeMap<Double, Player> scoredPlayers = new TreeMap<Double, Player>(Collections.reverseOrder());

		FinancialPolicy teamFinancialProfil = team.getTeamFinance().getFinancialProfil();
		double ecoWeight, matchProfileWeight;
		if (teamFinancialProfil instanceof AmbitiousPolicy) {
			ecoWeight = 0.6;
			matchProfileWeight = 0.4;
		} else if (teamFinancialProfil instanceof BalancedPolicy) {
			ecoWeight = 0.4;
			matchProfileWeight = 0.6;
		} else {
			ecoWeight = 0.2;
			matchProfileWeight = 0.8;
		}

		for (Player player : team.getCurrentPlayers().values()) {
			double economicFactor = player.getSalary() / averageSalary;
			double playerAttackNote = PlayerUtilitary.getPlayerAttackNote(player);
			double playerDefenseNote = PlayerUtilitary.getPlayerDefenseNote(player);
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

	public void updatePlayers(Team team, ArrayList<Player> players) {
		ArrayList<Player> playersToRemove = new ArrayList<Player>();
		for (Player player : players) {
			if (player.getHealthStatus().isInjured()) {
				playersToRemove.add(player);
			}
		}
		players.removeAll(playersToRemove);
		for (Player player : team.getCurrentPlayers().values()) {
			if (!player.getHealthStatus().isInjured() && !players.contains(player)) {
				players.add(player);
			}
		}
	}

	public TreeMap<Double, Player> sortPlayersAccordingToAttack(ArrayList<Player> players) {
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

	public TreeMap<Double, Player> sortPlayersAccordingToDefense(ArrayList<Player> players) {
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

}

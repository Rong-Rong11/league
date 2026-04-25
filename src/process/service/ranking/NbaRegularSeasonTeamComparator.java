package process.service.ranking;

import java.util.ArrayList;
import java.util.Comparator;

import data.league.League;
import data.sport.setup.Game;
import data.team.Team;

public class NbaRegularSeasonTeamComparator implements Comparator<Team> {

	private final RegularSeasonRankingCriteriaCalculator criteriaCalculator;

	public NbaRegularSeasonTeamComparator(ArrayList<Game> simulatedGames, League league) {
		this.criteriaCalculator = new RegularSeasonRankingCriteriaCalculator(simulatedGames, league);
	}

	@Override
	public int compare(Team teamA, Team teamB) {
		int result = Double.compare(criteriaCalculator.getWinRate(teamB), criteriaCalculator.getWinRate(teamA));
		if (result != 0) {
			return result;
		}

		result = Integer.compare(
				criteriaCalculator.getHeadToHeadWins(teamB, teamA),
				criteriaCalculator.getHeadToHeadWins(teamA, teamB));
		if (result != 0) {
			return result;
		}

		result = Boolean.compare(
				criteriaCalculator.isDivisionChampion(teamB),
				criteriaCalculator.isDivisionChampion(teamA));
		if (result != 0) {
			return result;
		}

		if (criteriaCalculator.isSameDivision(teamA, teamB)) {
			result = Double.compare(
					criteriaCalculator.getDivisionWinRate(teamB),
					criteriaCalculator.getDivisionWinRate(teamA));
			if (result != 0) {
				return result;
			}
		}

		result = Double.compare(
				criteriaCalculator.getConferenceWinRate(teamB),
				criteriaCalculator.getConferenceWinRate(teamA));
		if (result != 0) {
			return result;
		}

		result = Integer.compare(
				criteriaCalculator.getPointDifferential(teamB),
				criteriaCalculator.getPointDifferential(teamA));
		if (result != 0) {
			return result;
		}

		result = Integer.compare(
				teamB.getTeamPerformance().getNumberWin(),
				teamA.getTeamPerformance().getNumberWin());
		if (result != 0) {
			return result;
		}

		return teamA.getName().compareTo(teamB.getName());
	}
}

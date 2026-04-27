package process.service.ranking;

import java.util.ArrayList;
import java.util.Comparator;

import org.apache.log4j.Logger;

import data.league.League;
import data.sport.setup.Game;
import data.team.Team;
import log.LoggerUtility;

public class NbaRegularSeasonTeamComparator implements Comparator<Team> {
	private static final Logger logger = LoggerUtility.getLogger(NbaRegularSeasonTeamComparator.class, "text");

	private final RegularSeasonRankingCriteriaCalculator criteriaCalculator;

	public NbaRegularSeasonTeamComparator(ArrayList<Game> simulatedGames, League league) {
		this.criteriaCalculator = new RegularSeasonRankingCriteriaCalculator(simulatedGames, league);
		if (league == null) {
			logger.warn("NBA regular season comparator initialized with null league");
		}
	}

	@Override
	public int compare(Team teamA, Team teamB) {
		if (teamA == null || teamB == null) {
			logger.warn("Comparing teams with null value");
			return 0;
		}

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

		int finalResult = teamA.getName().compareTo(teamB.getName());

		logger.trace("Tie-breaker by team name between " + teamA.getName() + " and " + teamB.getName());

		return finalResult;
	}
}

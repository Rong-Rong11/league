package process.service.ranking;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import data.calendar.GameDay;
import data.league.League;
import data.league.Ranking;
import data.sport.setup.Game;
import data.team.Team;
import log.LoggerUtility;

public class RegularSeasonRankingManager {
	private static final Logger logger = LoggerUtility.getLogger(RegularSeasonRankingManager.class, "text");

	private ArrayList<GameDay> simulatedGameDay = new ArrayList<>();
	private ArrayList<Team> westTeams;
	private ArrayList<Team> eastTeams;

	public RegularSeasonRankingManager(ArrayList<Team> westTeams, ArrayList<Team> eastTeams) {
		this.westTeams = westTeams;
		this.eastTeams = eastTeams;
		logger.debug("Regular season ranking manager initialized with "
				+ (westTeams == null ? 0 : westTeams.size())
				+ " west teams and "
				+ (eastTeams == null ? 0 : eastTeams.size())
				+ " east teams");
	}

	public Ranking updateRanking(League league, Ranking ranking, TreeMap<LocalDate, GameDay> regularSeasonCalendar,
			LocalDate date) {
		if (ranking == null) {
			logger.warn("Skipping ranking update because ranking is null");
			return null;
		}
		if (westTeams == null || eastTeams == null) {
			logger.warn("Skipping ranking update because west or east teams list is null");
			return ranking;
		}
		logger.debug("Updating regular season ranking for " + date);
		TreeMap<Integer, Team> newEastRanking = new TreeMap<Integer, Team>();
		TreeMap<Integer, Team> newWestRanking = new TreeMap<Integer, Team>();

		Collections.sort(westTeams, new NbaRegularSeasonTeamComparator(getSimulatedGames(), league));
		Collections.sort(eastTeams, new NbaRegularSeasonTeamComparator(getSimulatedGames(), league));

		createNewRanking(newWestRanking, westTeams);
		createNewRanking(newEastRanking, eastTeams);
		ranking.setWestRanking(newWestRanking);
		ranking.setEastRanking(newEastRanking);
		logger.debug("Regular season ranking updated with "
				+ newWestRanking.size()
				+ " west teams and "
				+ newEastRanking.size()
				+ " east teams");
		return ranking;
	}

	private void createNewRanking(TreeMap<Integer, Team> newRanking, ArrayList<Team> sortedTeams) {
		int rank = 1;
		for (Team team : sortedTeams) {
			newRanking.put(rank, team);
			logger.trace("Ranked " + (team == null ? "<none>" : team.getName()) + " at position " + rank);
			rank++;
		}
	}

	public void addSimulatedGameDay(GameDay gameDay) {
		if (gameDay == null) {
			logger.warn("Skipping simulated game day registration because game day is null");
			return;
		}
		simulatedGameDay.add(gameDay);
		logger.debug("Registered simulated game day with " + gameDay.getGames().size() + " games");
	}

	public ArrayList<Team> getGlobalRanking(League league) {
		ArrayList<Team> globalRanking = new ArrayList<Team>();
		globalRanking.addAll(westTeams);
		globalRanking.addAll(eastTeams);
		Collections.sort(globalRanking, new NbaRegularSeasonTeamComparator(getSimulatedGames(), league));
		logger.debug("Built global ranking with " + globalRanking.size() + " teams");
		return globalRanking;
	}

	public ArrayList<Team> getEastRanking() {
		logger.trace("Returning east ranking with " + eastTeams.size() + " teams");
		return new ArrayList<Team>(eastTeams);
	}

	public ArrayList<Team> getWestRanking() {
		logger.trace("Returning west ranking with " + westTeams.size() + " teams");
		return new ArrayList<Team>(westTeams);
	}

	private ArrayList<Game> getSimulatedGames() {
		ArrayList<Game> games = new ArrayList<>();
		for (GameDay gameDay : simulatedGameDay) {
			games.addAll(gameDay.getGames());
		}
		logger.trace("Collected " + games.size() + " simulated games for ranking");
		return games;
	}
}

package process.orchestrator.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.calendar.GameDay;
import data.league.League;
import data.league.Playoff;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import process.builder.calendar.FirstRoundCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.repository.TeamRepository;
import process.service.finance.FinanceManager;
import process.service.league.TeamPopularityUpdater;
import process.utility.CalendarUtility;
import process.utility.PlayoffUtility;
import process.utility.TeamNameUtility;

class PlayoffManager {
	interface PlayoffDayRunner {
		void run(GameDay gameDay);
	}

	private final League league;
	private final PlayoffBuilder playoffBuilder;
	private final FirstRoundCalendarBuilder firstRoundCalendarBuilder;
	private final FinanceManager financeManager;
	private final TeamPopularityUpdater teamPopularityUpdater;
	private boolean userConfirmedPlayoffs;

	PlayoffManager(League league, PlayoffBuilder playoffBuilder, FirstRoundCalendarBuilder firstRoundCalendarBuilder,
			FinanceManager financeManager, TeamPopularityUpdater teamPopularityUpdater) {
		this.league = league;
		this.playoffBuilder = playoffBuilder;
		this.firstRoundCalendarBuilder = firstRoundCalendarBuilder;
		this.financeManager = financeManager;
		this.teamPopularityUpdater = teamPopularityUpdater;
	}

	Playoff getPlayoff() {
		return league == null ? null : league.getPlayoff();
	}

	PlayoffRound getCurrentPlayoffRound() {
		Playoff playoff = getPlayoff();
		return playoff == null ? null : playoff.getCurrentRound();
	}

	boolean hasPlayoffsStarted() {
		return getCurrentPlayoffRound() != null;
	}

	boolean hasPlayoffData() {
		Playoff playoff = getPlayoff();
		return playoff != null
				&& (!playoff.getEastFirstRound().isEmpty()
						|| !playoff.getWestFirstRound().isEmpty()
						|| !playoff.getNbaFinals().isEmpty());
	}

	boolean arePlayoffsFinished() {
		return getCurrentPlayoffRound() == PlayoffRound.FINISHED;
	}

	boolean hasUserConfirmedPlayoffs() {
		return userConfirmedPlayoffs;
	}

	void setUserConfirmedPlayoffs(boolean confirmed) {
		userConfirmedPlayoffs = confirmed;
	}

	void initializePlayoffs(int currentMonth) {
		if (!hasPlayoffsStarted()) {
			startPlayoffs(currentMonth);
		}
	}

	Map<String, String> getPlayoffPositionMap() {
		HashMap<String, String> positions = new HashMap<String, String>();
		Playoff playoff = getPlayoff();
		if (playoff == null) {
			return positions;
		}

		fillFirstRoundPositions(positions, playoff.getEastFirstRound(), 1, 1);
		fillFirstRoundPositions(positions, playoff.getWestFirstRound(), 9, 5);
		fillSemifinalPositions(positions, playoff.getEastConferenceSemis(), 1, 1);
		fillSemifinalPositions(positions, playoff.getWestConferenceSemis(), 5, 3);
		fillConferenceFinalPositions(positions, playoff.getEastConferenceFinals(), 1, 1);
		fillConferenceFinalPositions(positions, playoff.getWestConferenceFinals(), 3, 2);
		fillNbaFinalPositions(positions, playoff.getNbaFinals());
		if (playoff.getChampion() != null) {
			positions.put("e1", getTeamShortCode(playoff.getChampion()));
		}
		return positions;
	}

	int getPlayoffQualifiedTeamCount() {
		Playoff playoff = getPlayoff();
		if (playoff == null) {
			return 0;
		}
		return playoff.getQualifiedEastTeams().size() + playoff.getQualifiedWestTeams().size();
	}

	int getPlayoffSeriesCount() {
		return getAllPlayoffSeries().size();
	}

	String getCurrentPlayoffRoundLabel() {
		PlayoffRound round = getCurrentPlayoffRound();
		if (round == null) {
			return "A venir";
		}
		switch (round) {
			case FIRST_ROUND:
				return "Premier tour";
			case CONFERENCE_SEMIFINALS:
				return "Demies";
			case CONFERENCE_FINALS:
				return "Finales conf.";
			case NBA_FINALS:
				return "Finales NBA";
			default:
				return round.name();
		}
	}

	String getPlayoffChampionName() {
		Playoff playoff = getPlayoff();
		if (playoff == null || playoff.getChampion() == null) {
			return "";
		}
		return TeamNameUtility.getShortName(playoff.getChampion());
	}

	String getPlayoffGameLabel(Game game) {
		if (game == null || game.getPlayoffRound() == null || league == null || league.getPlayoff() == null) {
			return "";
		}
		for (PlayoffSeries series : getAllPlayoffSeries()) {
			if (PlayoffUtility.getGameNumber(series, game) > 0) {
				return PlayoffUtility.getBestOfLabel(series, game);
			}
		}
		return "";
	}

	void simulateNextPlayoffRound(PlayoffDayRunner dayRunner) {
		PlayoffRound startRound = getCurrentPlayoffRound();
		if (startRound == null || startRound == PlayoffRound.FINISHED || dayRunner == null) {
			return;
		}
		int safety = 0;
		GameDay gameDay = getNextUnsimulatedPlayoffGameDay(startRound);
		while (startRound == getCurrentPlayoffRound() && gameDay != null && safety < 80) {
			dayRunner.run(gameDay);
			gameDay = getNextUnsimulatedPlayoffGameDay(startRound);
			safety++;
		}
	}

	void startPlayoffs(int currentMonth) {
		league.setPlayoff(playoffBuilder.buldFirstRoundPlayoffs());
		applyPlayoffQualificationBonuses(currentMonth);
		applyPlayoffQualificationPopularityBonuses();
		applyMissedPlayoffPenalties();
		league.getPlayoff().setCurrentRound(PlayoffRound.FIRST_ROUND);
		league.getPlayoff().setNbaCalendar(firstRoundCalendarBuilder.buildCalendar());
	}

	private ArrayList<PlayoffSeries> getAllPlayoffSeries() {
		ArrayList<PlayoffSeries> series = new ArrayList<PlayoffSeries>();
		Playoff playoff = league.getPlayoff();
		if (playoff == null) {
			return series;
		}
		series.addAll(playoff.getEastFirstRound());
		series.addAll(playoff.getWestFirstRound());
		series.addAll(playoff.getEastConferenceSemis());
		series.addAll(playoff.getWestConferenceSemis());
		series.addAll(playoff.getEastConferenceFinals());
		series.addAll(playoff.getWestConferenceFinals());
		series.addAll(playoff.getNbaFinals());
		return series;
	}

	private GameDay getNextUnsimulatedPlayoffGameDay(PlayoffRound round) {
		if (league == null || league.getPlayoff() == null || league.getPlayoff().getNbaCalendar() == null) {
			return null;
		}
		for (GameDay gameDay : league.getPlayoff().getNbaCalendar().getCalendar().values()) {
			if (!gameDay.isSimulated() && hasPlayoffRoundGame(gameDay, round)) {
				return gameDay;
			}
		}
		return null;
	}

	private boolean hasPlayoffRoundGame(GameDay gameDay, PlayoffRound round) {
		if (gameDay == null || gameDay.isEmpty() || round == null) {
			return false;
		}
		for (Game game : gameDay.getGames()) {
			if (round.equals(game.getPlayoffRound())) {
				return true;
			}
		}
		return false;
	}

	private void fillFirstRoundPositions(HashMap<String, String> positions, ArrayList<PlayoffSeries> seriesList,
			int startAIndex, int startBIndex) {
		int[] visualOrder = { 0, 3, 1, 2 };
		for (int visualIndex = 0; visualIndex < visualOrder.length; visualIndex++) {
			int seriesIndex = visualOrder[visualIndex];
			if (seriesIndex >= seriesList.size()) {
				continue;
			}
			PlayoffSeries series = seriesList.get(seriesIndex);
			int aIndex = startAIndex + visualIndex * 2;
			positions.put("a" + aIndex, getTeamShortCode(series.getHigherTeam()));
			positions.put("a" + (aIndex + 1), getTeamShortCode(series.getLowerTeam()));
			Team winner = getFinishedSeriesWinner(series);
			if (winner != null) {
				positions.put("b" + (startBIndex + visualIndex), getTeamShortCode(winner));
			}
		}
	}

	private void fillSemifinalPositions(HashMap<String, String> positions, ArrayList<PlayoffSeries> seriesList,
			int startBIndex, int startCIndex) {
		for (int i = 0; i < seriesList.size(); i++) {
			PlayoffSeries series = seriesList.get(i);
			int bIndex = startBIndex + i * 2;
			putTeamPosition(positions, "b" + bIndex, series.getHigherTeam());
			putTeamPosition(positions, "b" + (bIndex + 1), series.getLowerTeam());
			putWinnerPosition(positions, "c" + (startCIndex + i), series);
		}
	}

	private void fillConferenceFinalPositions(HashMap<String, String> positions, ArrayList<PlayoffSeries> seriesList,
			int startCIndex, int dIndex) {
		if (seriesList.isEmpty()) {
			return;
		}
		PlayoffSeries series = seriesList.get(0);
		putTeamPosition(positions, "c" + startCIndex, series.getHigherTeam());
		putTeamPosition(positions, "c" + (startCIndex + 1), series.getLowerTeam());
		putWinnerPosition(positions, "d" + dIndex, series);
	}

	private void fillNbaFinalPositions(HashMap<String, String> positions, ArrayList<PlayoffSeries> seriesList) {
		if (seriesList.isEmpty()) {
			return;
		}
		PlayoffSeries series = seriesList.get(0);
		putTeamPosition(positions, "d1", series.getHigherTeam());
		putTeamPosition(positions, "d2", series.getLowerTeam());
		putWinnerPosition(positions, "e1", series);
	}

	private void putTeamPosition(HashMap<String, String> positions, String position, Team team) {
		if (team != null) {
			positions.put(position, getTeamShortCode(team));
		}
	}

	private void putWinnerPosition(HashMap<String, String> positions, String position, PlayoffSeries series) {
		Team winner = getFinishedSeriesWinner(series);
		if (winner != null) {
			positions.put(position, getTeamShortCode(winner));
		}
	}

	private Team getFinishedSeriesWinner(PlayoffSeries series) {
		if (series == null || !series.isFinished()) {
			return null;
		}
		if (series.getHigherTeamWins() > series.getLowerTeamWins()) {
			return series.getHigherTeam();
		}
		return series.getLowerTeam();
	}

	private String getTeamShortCode(Team team) {
		if (team == null) {
			return "";
		}
		if (team.getAbbreviation() != null && !team.getAbbreviation().equals("")) {
			return team.getAbbreviation();
		}
		if (team.getShortName() != null && !team.getShortName().equals("")) {
			return team.getShortName();
		}
		return team.getName();
	}

	private void applyPlayoffQualificationBonuses(int month) {
		ArrayList<Team> qualifiedTeams = new ArrayList<Team>();
		qualifiedTeams.addAll(league.getPlayoff().getQualifiedEastTeams());
		qualifiedTeams.addAll(league.getPlayoff().getQualifiedWestTeams());
		financeManager.applyPlayoffQualificationBonus(qualifiedTeams, month);
	}

	private void applyPlayoffQualificationPopularityBonuses() {
		ArrayList<Team> qualifiedTeams = new ArrayList<Team>();
		qualifiedTeams.addAll(league.getPlayoff().getQualifiedEastTeams());
		qualifiedTeams.addAll(league.getPlayoff().getQualifiedWestTeams());

		for (Team team : qualifiedTeams) {
			teamPopularityUpdater.applyPlayoffQualificationBonus(team);
		}
	}

	private void applyMissedPlayoffPenalties() {
		ArrayList<Team> qualifiedTeams = new ArrayList<Team>();
		qualifiedTeams.addAll(league.getPlayoff().getQualifiedEastTeams());
		qualifiedTeams.addAll(league.getPlayoff().getQualifiedWestTeams());

		for (Team team : TeamRepository.getInstance().getAllTeams()) {
			if (!qualifiedTeams.contains(team)) {
				teamPopularityUpdater.applyMissedPlayoffPenalty(team);
			}
		}
	}
}

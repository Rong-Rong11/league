package process.builder.league;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import data.league.League;
import data.league.Playoff;
import data.league.Ranking;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import log.LoggerUtility;

public class PlayoffBuilder {
	private static final Logger logger = LoggerUtility.getLogger(PlayoffBuilder.class, "text");
	private League league;

	public PlayoffBuilder(League league) {
		this.league = league;
	}

	public Playoff buldFirstRoundPlayoffs() {
		if (league == null || league.getRegularSeason() == null || league.getPlayoff() == null) {
			logger.warn("Skipping first round playoff build because league, regular season or playoff is null");
			return null;
		}

		logger.info("Building first round playoffs");
		Ranking ranking = league.getRegularSeason().getRanking();
		if (ranking == null) {
			logger.warn("Skipping first round playoff build because ranking is null");
			return league.getPlayoff();
		}
		Playoff playoff = league.getPlayoff();
		playoff.getQualifiedEastTeams().clear();
		playoff.getQualifiedWestTeams().clear();
		playoff.getEastFirstRound().clear();
		playoff.getWestFirstRound().clear();
		logger.debug("Cleared previous qualified teams and first round series");

		TreeMap<Integer, Team> eastRanking = ranking.getEastRanking();
		TreeMap<Integer, Team> westRanking = ranking.getWestRanking();
		logger.debug("Loaded east and west rankings for first round playoff build");

		addEastQualifiedTeam(eastRanking, playoff);
		addWestQualifiedTeam(westRanking, playoff);

		Team east1 = eastRanking.get(1);
		Team east2 = eastRanking.get(2);
		Team east3 = eastRanking.get(3);
		Team east4 = eastRanking.get(4);
		Team east5 = eastRanking.get(5);
		Team east6 = eastRanking.get(6);
		Team east7 = eastRanking.get(7);
		Team east8 = eastRanking.get(8);

		Team west1 = westRanking.get(1);
		Team west2 = westRanking.get(2);
		Team west3 = westRanking.get(3);
		Team west4 = westRanking.get(4);
		Team west5 = westRanking.get(5);
		Team west6 = westRanking.get(6);
		Team west7 = westRanking.get(7);
		Team west8 = westRanking.get(8);

		ArrayList<PlayoffSeries> firstWestRound = new ArrayList<>();
		firstWestRound.add(new PlayoffSeries(west1, west8));
		firstWestRound.add(new PlayoffSeries(west2, west7));
		firstWestRound.add(new PlayoffSeries(west3, west6));
		firstWestRound.add(new PlayoffSeries(west4, west5));

		ArrayList<PlayoffSeries> firstEastRound = new ArrayList<>();
		firstEastRound.add(new PlayoffSeries(east1, east8));
		firstEastRound.add(new PlayoffSeries(east2, east7));
		firstEastRound.add(new PlayoffSeries(east3, east6));
		firstEastRound.add(new PlayoffSeries(east4, east5));

		playoff.setEastFirstRound(firstEastRound);
		playoff.setWestFirstRound(firstWestRound);
		logger.debug("First round built with "
				+ firstEastRound.size()
				+ " eastern series and "
				+ firstWestRound.size()
				+ " western series");
		logger.info("First round playoffs built");
		return playoff;
	}

	public Playoff buldSecondRoundPlayoffs() {
		if (league == null || league.getPlayoff() == null) {
			logger.warn("Skipping conference semifinals build because league or playoff is null");
			return null;
		}

		logger.info("Building conference semifinals playoffs");
		Playoff playoff = league.getPlayoff();

		ArrayList<PlayoffSeries> eastSemis = new ArrayList<PlayoffSeries>();
		ArrayList<PlayoffSeries> westSemis = new ArrayList<PlayoffSeries>();

		Team eastWinner1 = getSeriesWinner(playoff.getEastFirstRound().get(0));
		Team eastWinner2 = getSeriesWinner(playoff.getEastFirstRound().get(1));
		Team eastWinner3 = getSeriesWinner(playoff.getEastFirstRound().get(2));
		Team eastWinner4 = getSeriesWinner(playoff.getEastFirstRound().get(3));

		Team westWinner1 = getSeriesWinner(playoff.getWestFirstRound().get(0));
		Team westWinner2 = getSeriesWinner(playoff.getWestFirstRound().get(1));
		Team westWinner3 = getSeriesWinner(playoff.getWestFirstRound().get(2));
		Team westWinner4 = getSeriesWinner(playoff.getWestFirstRound().get(3));

		eastSemis.add(new PlayoffSeries(eastWinner1, eastWinner4));
		eastSemis.add(new PlayoffSeries(eastWinner2, eastWinner3));

		westSemis.add(new PlayoffSeries(westWinner1, westWinner4));
		westSemis.add(new PlayoffSeries(westWinner2, westWinner3));

		playoff.setEastConferenceSemis(eastSemis);
		playoff.setWestConferenceSemis(westSemis);
		logger.debug("Conference semifinals built with "
				+ eastSemis.size()
				+ " eastern series and "
				+ westSemis.size()
				+ " western series");
		logger.info("Conference semifinals playoffs built");
		return playoff;
	}

	public Playoff buildConferenceFinalsPlayoffs() {
		if (league == null || league.getPlayoff() == null) {
			logger.warn("Skipping conference finals build because league or playoff is null");
			return null;
		}

		logger.info("Building conference finals playoffs");
		Playoff playoff = league.getPlayoff();

		ArrayList<PlayoffSeries> eastConferenceFinals = new ArrayList<PlayoffSeries>();
		ArrayList<PlayoffSeries> westConferenceFinals = new ArrayList<PlayoffSeries>();

		Team eastWinner1 = getSeriesWinner(playoff.getEastConferenceSemis().get(0));
		Team eastWinner2 = getSeriesWinner(playoff.getEastConferenceSemis().get(1));

		Team westWinner1 = getSeriesWinner(playoff.getWestConferenceSemis().get(0));
		Team westWinner2 = getSeriesWinner(playoff.getWestConferenceSemis().get(1));

		eastConferenceFinals.add(new PlayoffSeries(eastWinner1, eastWinner2));
		westConferenceFinals.add(new PlayoffSeries(westWinner1, westWinner2));

		playoff.setEastConferenceFinals(eastConferenceFinals);
		playoff.setWestConferenceFinals(westConferenceFinals);
		logger.debug("Conference finals built with "
				+ eastConferenceFinals.size()
				+ " eastern series and "
				+ westConferenceFinals.size()
				+ " western series");
		logger.info("Conference finals playoffs built");
		return playoff;
	}

	public Playoff buildNbaFinalsPlayoffs() {
		if (league == null || league.getPlayoff() == null) {
			logger.warn("Skipping NBA finals build because league or playoff is null");
			return null;
		}

		logger.info("Building NBA finals playoffs");
		Playoff playoff = league.getPlayoff();

		ArrayList<PlayoffSeries> nbaFinals = new ArrayList<PlayoffSeries>();

		Team eastWinner = getSeriesWinner(playoff.getEastConferenceFinals().get(0));
		Team westWinner = getSeriesWinner(playoff.getWestConferenceFinals().get(0));

		nbaFinals.add(new PlayoffSeries(eastWinner, westWinner));

		playoff.setNbaFinals(nbaFinals);
		logger.debug("NBA finals built with matchup " + eastWinner.getName() + " vs " + westWinner.getName());
		logger.info("NBA finals playoffs built");
		return playoff;
	}

	private Team getSeriesWinner(PlayoffSeries series) {
		if (series == null) {
			logger.warn("Unable to determine series winner because series is null");
			return null;
		}
		if (series.getHigherTeamWins() > series.getLowerTeamWins()) {
			logger.trace("Series winner is higher seed " + series.getHigherTeam().getName());
			return series.getHigherTeam();
		}
		logger.trace("Series winner is lower seed " + series.getLowerTeam().getName());
		return series.getLowerTeam();
	}

	private void addEastQualifiedTeam(TreeMap<Integer, Team> eastRanking, Playoff playoff) {
		if (eastRanking == null || playoff == null) {
			logger.warn("Skipping east qualified team registration because ranking or playoff is null");
			return;
		}
		for (int i = 1; i <= 8; i++) {
			Team qualifiedTeam = eastRanking.get(i);
			playoff.addQualifiedEastTeam(qualifiedTeam);
			if (qualifiedTeam != null) {
				logger.trace("Registered east qualified team " + qualifiedTeam.getName() + " at seed " + i);
			}
		}
	}

	private void addWestQualifiedTeam(TreeMap<Integer, Team> westRanking, Playoff playoff) {
		if (westRanking == null || playoff == null) {
			logger.warn("Skipping west qualified team registration because ranking or playoff is null");
			return;
		}
		for (int i = 1; i <= 8; i++) {
			Team qualifiedTeam = westRanking.get(i);
			playoff.addQualifiedWestTeam(qualifiedTeam);
			if (qualifiedTeam != null) {
				logger.trace("Registered west qualified team " + qualifiedTeam.getName() + " at seed " + i);
			}
		}
	}
}

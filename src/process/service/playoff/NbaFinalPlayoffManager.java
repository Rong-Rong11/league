package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.PlayoffSeries;
import data.team.Team;
import log.LoggerUtility;
import process.builder.calendar.NbaFinalCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.league.TeamPopularityUpdater;

public class NbaFinalPlayoffManager extends PlayoffManager {
	private static final Logger logger = LoggerUtility.getLogger(NbaFinalPlayoffManager.class, "text");

	public NbaFinalPlayoffManager(League league,
			NbaFinalCalendarBuilder nbaFinalCalendarBuilder,
			PlayoffBuilder playoffBuilder,
			FinanceManager financeManager,
			TeamPopularityUpdater teamPopularityUpdater) {
		super(league, nbaFinalCalendarBuilder, playoffBuilder, financeManager, teamPopularityUpdater);
		if (league == null) {
			logger.warn("NBA final playoff manager initialized with null league");
		}
	}

	@Override
	public ArrayList<PlayoffSeries> getManagedSeries() {
		logger.trace("Getting managed NBA final playoff series");

		ArrayList<PlayoffSeries> managedSeries = new ArrayList<PlayoffSeries>();
		managedSeries.addAll(getLeague().getPlayoff().getNbaFinals());

		logger.trace("Found " + managedSeries.size() + " NBA final playoff series");
		return managedSeries;
	}

	@Override
	public void advanceToNextRound(LocalDate roundEndDate) {
		League league = getLeague();
		ArrayList<PlayoffSeries> finals = getManagedSeries();

		if (finals.isEmpty()) {
			logger.warn("Unable to complete playoffs because NBA finals series list is empty");
			return;
		}

		Team champion = getSeriesWinner(finals.get(0));
		league.getPlayoff().setChampion(champion);
		league.getPlayoff().setCurrentRound(PlayoffRound.FINISHED);
		logger.info("NBA finals completed, champion is " + champion.getName());
	}
}

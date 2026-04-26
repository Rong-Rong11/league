package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.league.League;
import data.sport.setup.PlayoffSeries;
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
		logger.info("NBA finals completed, no next playoff round to advance to");
	}
}

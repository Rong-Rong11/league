package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.PlayoffSeries;
import log.LoggerUtility;
import process.builder.calendar.ConferenceFinalCalendarBuilder;
import process.builder.calendar.NbaFinalCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.league.TeamPopularityUpdater;

public class ConferenceFinalPlayoffManager extends PlayoffManager {
	private static final Logger logger = LoggerUtility.getLogger(ConferenceFinalPlayoffManager.class, "text");

	public ConferenceFinalPlayoffManager(League league,
			ConferenceFinalCalendarBuilder conferenceFinalCalendarBuilder,
			PlayoffBuilder playoffBuilder,
			FinanceManager financeManager,
			TeamPopularityUpdater teamPopularityUpdater) {
		super(league, conferenceFinalCalendarBuilder, playoffBuilder, financeManager, teamPopularityUpdater);
		if (league == null) {
			logger.warn("Conference final playoff manager initialized with null league");
		}
	}

	@Override
	public ArrayList<PlayoffSeries> getManagedSeries() {
		logger.trace("Getting managed conference final playoff series");

		ArrayList<PlayoffSeries> managedSeries = new ArrayList<PlayoffSeries>();
		managedSeries.addAll(getLeague().getPlayoff().getEastConferenceFinals());
		managedSeries.addAll(getLeague().getPlayoff().getWestConferenceFinals());

		logger.trace("Found " + managedSeries.size() + " conference final playoff series");
		return managedSeries;
	}

	@Override
	public void advanceToNextRound(LocalDate roundEndDate) {
		if (roundEndDate == null) {
			logger.warn("Advancing to NBA finals with null round end date");
		}

		logger.debug("Advancing from conference finals to NBA finals");

		League league = getLeague();
		league.setPlayoff(getPlayoffBuilder().buildNbaFinalsPlayoffs());
		league.getPlayoff().setCurrentRound(PlayoffRound.NBA_FINALS);

		NbaFinalCalendarBuilder nbaFinalCalendarBuilder = new NbaFinalCalendarBuilder(league, roundEndDate);
		league.getPlayoff().setNbaCalendar(nbaFinalCalendarBuilder.buildCalendar());

		logger.debug("NBA finals playoffs and calendar created");
	}
}

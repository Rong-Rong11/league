package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.PlayoffSeries;
import log.LoggerUtility;
import process.builder.calendar.ConferenceFinalCalendarBuilder;
import process.builder.calendar.SemiCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.league.TeamPopularityUpdater;

public class SemiPlayoffManager extends PlayoffManager {
	private static final Logger logger = LoggerUtility.getLogger(SemiPlayoffManager.class, "text");

	public SemiPlayoffManager(League league,
			SemiCalendarBuilder semiCalendarBuilder,
			PlayoffBuilder playoffBuilder,
			FinanceManager financeManager,
			TeamPopularityUpdater teamPopularityUpdater) {
		super(league, semiCalendarBuilder, playoffBuilder, financeManager, teamPopularityUpdater);
		if (league == null) {
			logger.warn("Semi playoff manager initialized with null league");
		}
	}

	@Override
	public ArrayList<PlayoffSeries> getManagedSeries() {
		logger.trace("Getting managed conference semifinal playoff series");

		ArrayList<PlayoffSeries> managedSeries = new ArrayList<PlayoffSeries>();
		managedSeries.addAll(getLeague().getPlayoff().getEastConferenceSemis());
		managedSeries.addAll(getLeague().getPlayoff().getWestConferenceSemis());

		logger.trace("Found " + managedSeries.size() + " conference semifinal playoff series");
		return managedSeries;
	}

	@Override
	public void advanceToNextRound(LocalDate roundEndDate) {
		if (roundEndDate == null) {
			logger.warn("Advancing to conference finals with null round end date");
		}

		logger.debug("Advancing from conference semifinals to conference finals");

		League league = getLeague();
		league.setPlayoff(getPlayoffBuilder().buildConferenceFinalsPlayoffs());
		league.getPlayoff().setCurrentRound(PlayoffRound.CONFERENCE_FINALS);

		ConferenceFinalCalendarBuilder conferenceFinalCalendarBuilder = new ConferenceFinalCalendarBuilder(league,
				roundEndDate);
		mergePlayoffCalendar(conferenceFinalCalendarBuilder.buildCalendar());

		logger.debug("Conference finals playoffs and calendar created");
	}
}

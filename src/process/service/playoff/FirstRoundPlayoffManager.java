package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.league.League;
import data.league.PlayoffRound;
import data.sport.setup.PlayoffSeries;
import log.LoggerUtility;
import process.builder.calendar.FirstRoundCalendarBuilder;
import process.builder.calendar.SemiCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.league.TeamPopularityUpdater;

public class FirstRoundPlayoffManager extends PlayoffManager {
	private static final Logger logger = LoggerUtility.getLogger(FirstRoundPlayoffManager.class, "text");

	public FirstRoundPlayoffManager(League league,
			FirstRoundCalendarBuilder firstRoundPlayoffCalendarBuilder,
			PlayoffBuilder playoffBuilder,
			FinanceManager financeManager,
			TeamPopularityUpdater teamPopularityUpdater) {
		super(league, firstRoundPlayoffCalendarBuilder, playoffBuilder, financeManager, teamPopularityUpdater);
		if (league == null) {
			logger.warn("First round playoff manager initialized with null league");
		}
	}

	@Override
	public ArrayList<PlayoffSeries> getManagedSeries() {
		logger.trace("Getting managed first round playoff series");

		ArrayList<PlayoffSeries> managedSeries = new ArrayList<PlayoffSeries>();
		managedSeries.addAll(getLeague().getPlayoff().getEastFirstRound());
		managedSeries.addAll(getLeague().getPlayoff().getWestFirstRound());

		logger.trace("Found " + managedSeries.size() + " first round playoff series");
		return managedSeries;
	}

	@Override
	public void advanceToNextRound(LocalDate roundEndDate) {
		if (roundEndDate == null) {
			logger.warn("Advancing to conference semifinals with null round end date");
		}

		logger.debug("Advancing from first round to conference semifinals");

		League league = getLeague();
		league.setPlayoff(getPlayoffBuilder().buildSecondRoundPlayoffs());
		league.getPlayoff().setCurrentRound(PlayoffRound.CONFERENCE_SEMIFINALS);

		SemiCalendarBuilder semiCalendarBuilder = new SemiCalendarBuilder(league, roundEndDate);
		mergePlayoffCalendar(semiCalendarBuilder.buildCalendar());

		logger.debug("Conference semifinals playoffs and calendar created");
	}
}

package process.service.playoff;

import java.time.LocalDate;
import java.util.ArrayList;

import data.league.League;
import data.sport.setup.PlayoffSeries;
import process.builder.calendar.NbaFinalCalendarBuilder;
import process.builder.league.PlayoffBuilder;
import process.service.finance.FinanceManager;
import process.service.league.TeamPopularityUpdater;

public class NbaFinalPlayoffManager extends PlayoffManager {

	public NbaFinalPlayoffManager(League league,
			NbaFinalCalendarBuilder nbaFinalCalendarBuilder,
			PlayoffBuilder playoffBuilder,
			FinanceManager financeManager,
			TeamPopularityUpdater teamPopularityUpdater) {
		super(league, nbaFinalCalendarBuilder, playoffBuilder, financeManager, teamPopularityUpdater);
	}

	@Override
	public ArrayList<PlayoffSeries> getManagedSeries() {
		ArrayList<PlayoffSeries> managedSeries = new ArrayList<PlayoffSeries>();
		managedSeries.addAll(getLeague().getPlayoff().getNbaFinals());
		return managedSeries;
	}

	@Override
	public void advanceToNextRound(LocalDate roundEndDate) {
	}
}

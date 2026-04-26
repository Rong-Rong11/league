package process.service.game.tools;

import java.time.LocalDate;

import data.calendar.GameDay;
import data.league.PlayoffRound;
import data.sport.setup.Game;
import process.service.finance.FinanceManager;
import process.service.playoff.PlayoffManager;
import process.simulator.GameSimulator;

public class PlayoffGameDaySimulationProcessor extends GameDaySimulationProcessor {

	private PlayoffManager playoffManager;
	private PlayoffRound round;

	public PlayoffGameDaySimulationProcessor(
			GameSimulator gameSimulator,
			FinanceManager financeManager,
			PlayoffManager playoffManager,
			PlayoffRound round) {
		super(gameSimulator, financeManager);
		this.playoffManager = playoffManager;
		this.round = round;
	}

	@Override
	protected void applyFinance(Game game, LocalDate date, int month) {
		financeManager.calculatePlayoffGame(game, date, month, round);
	}

	@Override
	protected void afterGame(Game game, LocalDate date) {
		playoffManager.handlePlayedGame(game, date);
	}

	@Override
	protected void afterGameDay(GameDay gameDay, LocalDate date, int month) {

	}

}

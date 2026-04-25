package process.service.finance.game.revenue;

import org.apache.log4j.Logger;

import data.finance.GameStat;
import data.sport.setup.Game;
import log.LoggerUtility;

public class GameTicketRevenueCalculator {
	private static final Logger logger = LoggerUtility.getLogger(GameTicketRevenueCalculator.class, "text");

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameTicketRevenueCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
		logger.debug("Game ticket revenue calculator initialized");
	}

	public void calculateTicketRevenue(int attendees, double ticketPrice, Game game) {
		if (gameStat == null) {
			logger.warn("Skipping ticket revenue calculation because game stat is null");
			return;
		}
		double ticketRevenue = (attendees * ticketPrice * 1.05) / 1000000;
		logger.trace("Base ticket revenue is "
				+ ticketRevenue
				+ " with attendees "
				+ attendees
				+ " and ticket price "
				+ ticketPrice);
		double bonusRate = bonusProvider.getTicketRevenueBonusRate(game, attendees, ticketPrice);
		ticketRevenue *= (1 + bonusRate);
		logger.trace("Applied ticket revenue bonus rate " + bonusRate);
		gameStat.getHomeFinance().setTicketRevenue(ticketRevenue);
		logger.debug("Calculated ticket revenue " + ticketRevenue);
	}
}

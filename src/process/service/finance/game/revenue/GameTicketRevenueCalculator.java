package process.service.finance.game.revenue;

import data.finance.GameStat;
import data.sport.setup.Game;

public class GameTicketRevenueCalculator {

	private GameStat gameStat;
	private GameRevenueBonusProvider bonusProvider;

	public GameTicketRevenueCalculator(GameStat gameStat, GameRevenueBonusProvider bonusProvider) {
		this.gameStat = gameStat;
		this.bonusProvider = bonusProvider;
	}

	public void calculateTicketRevenue(int attendees, double ticketPrice, Game game) {
		double ticketRevenue = (attendees * ticketPrice * 1.05) / 1000000;
		ticketRevenue *= (1 + bonusProvider.getTicketRevenueBonusRate(game, attendees, ticketPrice));
		gameStat.getHomeFinance().setTicketRevenue(ticketRevenue);
	}
}

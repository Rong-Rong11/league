package process.service.trade.selection;

import org.apache.log4j.Logger;

import data.player.Player;
import data.team.Team;
import data.team.finance.transfer.TeamTransferStrategy;
import log.LoggerUtility;
import process.visitor.teamtransfer.PreSeasonPlayerToTradeVisitor;
import process.visitor.teamtransfer.SeasonPlayerToTradeVisitor;

public class TradePlayerSelector {
	private static final Logger logger = LoggerUtility.getLogger(TradePlayerSelector.class, "text");

	public static Player selectPlayerToTrade(Team team, boolean season, double salaryCap) {
		if (team == null || team.getTeamFinance() == null) {
			logger.warn("Unable to select player to trade because team or team finance is null");
			return null;
		}

		TeamTransferStrategy teamTransferStrategy = team.getTeamFinance().getBehavior().getTeamTransferStrategy();

		if (teamTransferStrategy == null) {
			logger.warn("Unable to select player to trade because transfer strategy is null");
			return null;
		}

		if (season) {
			logger.trace("Selecting regular season player to trade for " + team.getName());

			SeasonPlayerToTradeVisitor seasonPlayerToTradeVisitor = new SeasonPlayerToTradeVisitor(team,
					teamTransferStrategy.getSeasonIntent(), salaryCap);

			Player player = teamTransferStrategy.accept(seasonPlayerToTradeVisitor);

			if (player == null) {
				logger.trace("No regular season player selected to trade for " + team.getName());
			} else {
				logger.trace("Selected regular season player to trade for " + team.getName() + ": "
						+ player.getName());
			}

			return player;
		}

		logger.trace("Selecting preseason player to trade for " + team.getName());

		PreSeasonPlayerToTradeVisitor preSeasonPlayerToTradeVisitor = new PreSeasonPlayerToTradeVisitor(team);
		Player player = teamTransferStrategy.accept(preSeasonPlayerToTradeVisitor);

		if (player == null) {
			logger.trace("No preseason player selected to trade for " + team.getName());
		} else {
			logger.trace("Selected preseason player to trade for " + team.getName() + ": " + player.getName());
		}

		return player;
	}
}

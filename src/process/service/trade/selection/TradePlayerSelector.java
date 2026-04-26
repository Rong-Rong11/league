package process.service.trade.selection;

import data.player.Player;
import data.team.Team;
import data.team.finance.transfer.TeamTransferStrategy;
import process.visitor.teamtransfer.PreSeasonPlayerToTradeVisitor;
import process.visitor.teamtransfer.SeasonPlayerToTradeVisitor;

public class TradePlayerSelector {

	public static Player selectPlayerToTrade(Team team, boolean season, double salaryCap) {
		if (season) {
			TeamTransferStrategy teamTransferStrategy = team.getTeamFinance().getBehavior().getTeamTransferStrategy();
			SeasonPlayerToTradeVisitor seasonPlayerToTradeVisitor = new SeasonPlayerToTradeVisitor(team,
					teamTransferStrategy.getSeasonIntent(), salaryCap);
			return teamTransferStrategy.accept(seasonPlayerToTradeVisitor);
		} else {
			TeamTransferStrategy teamTransferStrategy = team.getTeamFinance().getBehavior().getTeamTransferStrategy();
			PreSeasonPlayerToTradeVisitor preSeasonPlayerToTradeVisitor = new PreSeasonPlayerToTradeVisitor(team);
			return teamTransferStrategy.accept(preSeasonPlayerToTradeVisitor);
		}
	}
}

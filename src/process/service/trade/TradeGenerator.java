package process.service.trade;

import data.player.Player;
import data.team.Team;
import data.team.finance.transfer.TeamTransferStrategy;
import process.visitor.teamtransfer.PreSeasonPlayerToTradeVisitor;
import process.visitor.teamtransfer.SeasonPlayerToTradeVisitor;

public class TradeGenerator {

	public static Player generatePlayersToTrade(Team team, boolean season, double salaryCap) {
	  if (season) {
		 TeamTransferStrategy teamTransferStrategy = team.getTeamFinance().getTeamTransferStrategy();
		 SeasonPlayerToTradeVisitor seasonPlayerToTradeVisitor = new SeasonPlayerToTradeVisitor(team,
			   teamTransferStrategy.getSeasonIntent(), salaryCap);
		 return teamTransferStrategy.accept(seasonPlayerToTradeVisitor);
	  } else {
		 TeamTransferStrategy teamTransferStrategy = team.getTeamFinance().getTeamTransferStrategy();
		 PreSeasonPlayerToTradeVisitor preSeasonPlayerToTradeVisitor = new PreSeasonPlayerToTradeVisitor(team);
		 return teamTransferStrategy.accept(preSeasonPlayerToTradeVisitor);
	  }
	}
}

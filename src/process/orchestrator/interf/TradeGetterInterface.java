package process.orchestrator.interf;

import java.util.ArrayList;

import data.finance.transfer.Trade;
import data.team.Team;

public interface TradeGetterInterface {

	ArrayList<Trade> getTradesForTeam(Team team);
}

package process.simulator.tradetools;

import data.team.Team;
import process.utility.FinanceUtilitary;

public class TradeImpact {
    public void applyFinanceImpact(Team team, double d, int n) {
        FinanceUtilitary.updateTeamPayroll(team);
    }
}

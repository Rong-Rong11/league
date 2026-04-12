package process.simulator.tradetools;

import data.team.Team;
import process.utility.FinanceUtility;

public class TradeImpact {
    public void applyFinanceImpact(Team team, double d, int n) {
        FinanceUtility.updateTeamPayroll(team);
    }
}

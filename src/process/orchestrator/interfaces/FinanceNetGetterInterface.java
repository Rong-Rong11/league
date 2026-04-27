package process.orchestrator.interfaces;

import data.team.Team;

public interface FinanceNetGetterInterface {

	double getLeagueNetForMonth(int month);

	double getTeamNetForMonth(Team team, int month);

	double getLeagueTotalNet();

	double getTeamTotalNet(Team team);
}

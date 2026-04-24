package process.service.finance.initialization;

import data.team.Team;
import process.builder.finance.FinanceBuilder;
import process.repository.TeamRepository;

public class FinanceInitializer {
	private TeamRepository teamRepositery = TeamRepository.getInstance();

	public FinanceInitializer() {
	}

	public void initializeFinance() {
		for (Team team : teamRepositery.getAllTeams()) {
			team.setTeamFinance(FinanceBuilder.buildTeamFinance(team));
		}
	}
}

package process.service.finance.initialization;

import org.apache.log4j.Logger;

import data.team.Team;
import log.LoggerUtility;
import process.builder.finance.FinanceBuilder;
import process.repository.TeamRepository;

public class FinanceInitializer {
	private static final Logger logger = LoggerUtility.getLogger(FinanceInitializer.class, "text");

	private TeamRepository teamRepositery = TeamRepository.getInstance();

	public FinanceInitializer() {
		logger.debug("Finance initializer created");
	}

	public void initializeFinance() {
		logger.info("Initializing team finance data");
		int initializedTeams = 0;
		for (Team team : teamRepositery.getAllTeams()) {
			if (team == null) {
				logger.warn("Skipping finance initialization because team is null");
				continue;
			}
			logger.trace("Initializing finance data for team " + team.getName());
			team.setTeamFinance(FinanceBuilder.buildTeamFinance(team));
			initializedTeams++;
		}
		logger.info("Team finance data initialized for " + initializedTeams + " teams");
	}
}

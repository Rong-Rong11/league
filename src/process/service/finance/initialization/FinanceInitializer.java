package process.service.finance.initialization;

import data.team.Team;
import process.builder.FinanceBuilder;
import process.repositery.TeamRepositery;

public class FinanceInitializer {
   private TeamRepositery teamRepositery = TeamRepositery.getInstance();

   public FinanceInitializer() {
   }

   public void initializeFinance() {
      for (Team team : teamRepositery.getAllTeams()) {
         team.setTeamFinance(FinanceBuilder.buildTeamFinance(team));
      }
   }
}

package process.manager.financetools;

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

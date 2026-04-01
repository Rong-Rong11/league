package process.orchestrator;

import java.time.LocalDate;

import data.team.Team;

public interface SimulationInterface {

   // methodes pour la presaison
   // pour page de garde
   void randomFinance();

   void chooseAmbitiousPolicy(Team team);

   void chooseBalancedPolicy(Team team);

   void chooseThriftyPolicy(Team team);

   void chooseLargeMarketSize(Team team);

   void chooseMediumMarketSize(Team team);

   void chooseSmallMarketSize(Team team);

   void startSeason();

   void simulateRegularSeasonDay(LocalDate date);

   void endRegularSeason();

   void simulateRegularSeason();

}

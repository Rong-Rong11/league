package process.orchestrator.interf;

import java.time.LocalDate;

import data.sport.setup.Game;
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

   void simulateDay(LocalDate date);

   void simulateAndDisplayDay(LocalDate date);

   boolean makeLiveMatchAvailable(Game game, LocalDate date);

   void simulateWeek(LocalDate startDate);

   void simulateSeasonFrom(LocalDate startDate);

   void endRegularSeason();

   void simulateRegularSeason();

}

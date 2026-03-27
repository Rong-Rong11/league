package process;

import data.league.League;
import data.team.Team;
import java.time.LocalDate;
import process.manager.LeagueManager;

public interface SimulationInterface {

   // methddes pour la presaison
   // pour page de garde
   void randomFinance();

   void chooseAmbitiousPolicy(Team team);

   void chooseBalancedPolicy(Team team);

   void chooseThriftyPolicy(Team team);

   void chooseLargeMarketSize(Team team);

   void chooseMediumMarketSize(Team team);

   void chooseSmallMarketSize(Team team);

   // méthode à utiliser pour lancer la saison
   void startSeason();

   // passe le prochain jour, méthode à utiliser pour la simulation et tout se fais
   // tous seul
   void nextDay();

   void simulateRegularSeasonDay(LocalDate date);

   void endRegularSeason();

   // simuler la fin de saison régulière ou fin playoff
   void simulateRegularSeason();

   League getLeague();

   LeagueManager getLeagueManager();

   LocalDate getCurrentDate();

   // à enkever les plus tard
   void displayGameDay(LocalDate date);

}
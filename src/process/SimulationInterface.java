package process;

import java.time.LocalDate;

public interface SimulationInterface {

   // méthode à utiliser pour lancer la saison
   void startSeason();

   // passe le prochain jour, méthode à utiliser pour la simulation et tout se fais
   // tous seul
   void nextDay();

   League getLeague();

   LocalDate getCurrentDate();

}
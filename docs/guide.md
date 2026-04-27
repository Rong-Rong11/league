# Guide de lecture

## 1. Par où commencer

Commencer par [`LeagueManager.java`](../src/process/LeagueManager.java).

Cette classe donne la meilleure vue d’ensemble du projet :

- construction de la ligue ;
- génération du calendrier ;
- simulation d’une journée.

Ensuite, lire [`LeagueBuilder.java`](../src/process/builder/LeagueBuilder.java), car c’est lui qui transforme les données sources en objets métier.

## 2. Point d’entrée du programme

Le point d’entrée exécutable actuellement repéré est [`TestGui.java`](../src/test/TestGui.java), avec une méthode `main`.

Pour comprendre l’application, la classe la plus importante reste toutefois [`MainGui.java`](../src/gui/frame/MainGui.java), car c’est elle qui construit réellement la fenêtre Swing.

En pratique :

- `TestGui` lance ;
- `MainGui` organise l’interface.

## 3. Comment fonctionne l’interface graphique

Lire d’abord [`MainGui.java`](../src/gui/frame/MainGui.java).

La fenêtre utilise deux `CardLayout` :

- un pour passer de l’écran d’ouverture à l’application ;
- un pour naviguer entre les dashboards.

Puis lire :

- [`OpeningDashboard.java`](../src/gui/dashboard/OpeningDashboard.java) pour l’écran initial ;
- [`SidebarPanel.java`](../src/gui/layout/SidebarPanel.java) pour la navigation latérale ;
- les dashboards de `src/gui/dashboard` pour les vues métier ;
- les composants de `src/gui/components` pour les briques visuelles réutilisées.

## 4. Comment les données sont organisées

Le cœur du modèle se trouve dans `src/data`.

Ordre conseillé :

1. [`League.java`](../src/data/league/League.java)
2. [`Conference.java`](../src/data/league/Conference.java)
3. [`Division.java`](../src/data/league/Division.java)
4. [`Team.java`](../src/data/team/Team.java)
5. [`Player.java`](../src/data/player/Player.java)

Ensuite, lire les objets satellites :

- [`Season.java`](../src/data/league/Season.java), [`RegularSeason.java`](../src/data/league/RegularSeason.java), [`Playoff.java`](../src/data/league/Playoff.java)
- [`NBACalendar.java`](../src/data/calendar/NBACalendar.java), [`GameDay.java`](../src/data/calendar/GameDay.java), [`SpecialEvent.java`](../src/data/calendar/SpecialEvent.java)
- [`Game.java`](../src/data/sport/setup/Game.java), [`GameContext.java`](../src/data/sport/setup/GameContext.java), [`GameResult.java`](../src/data/sport/setup/GameResult.java)

## 5. Comment la logique métier est implémentée

La logique métier est concentrée dans `src/process`.

Ordre de compréhension :

1. [`LeagueBuilder.java`](../src/process/builder/LeagueBuilder.java)
2. [`PlayerFactory.java`](../src/process/factory/PlayerFactory.java)
3. [`TeamFactory.java`](../src/process/factory/TeamFactory.java)
4. [`CalendarBuilder.java`](../src/process/builder/CalendarBuilder.java)
5. [`GameGenerator.java`](../src/process/GameGenerator.java)
6. [`GameManager.java`](../src/process/GameManager.java)
7. [`GameSimulator.java`](../src/process/GameSimulator.java)

À retenir :

- `LeagueBuilder` lit le CSV et crée les objets ;
- `CalendarBuilder` place les matchs ;
- `GameGenerator` fabrique les rencontres ;
- `GameManager` fournit les règles de sélection et de calendrier ;
- `GameSimulator` joue le match et met à jour l’état.

## 5.1 Pseudo-code de construction du calendrier

Le mécanisme de construction repose sur la classe abstraite `CalendarBuilder`, puis sur des spécialisations pour la saison régulière et les playoffs.

### Pseudo-code général

```text
ALGORITHME buildCalendar
    réinitialiser les schedules des équipes
    générer la liste des matchs à programmer
    construire le calendrier final
    retourner le calendrier
FIN
```

### Pseudo-code correspondant à `CalendarBuilder`

```text
fonction buildCalendar()
    resetSchedule()
    generateGames()
    calendrier <- build()
    retourner calendrier
fin fonction
```

### Pseudo-code pour la saison régulière

```text
fonction buildRegularSeasonCalendar(league)
    placer les événements spéciaux dans la saison
    calendrier <- vide
    dateCourante <- date de début de saison

    tant que dateCourante <= date de fin de saison
        gameDay <- nouvelle journée pour dateCourante

        configurer le sélecteur sur dateCourante
        matchsDuJour <- sélectionner les matchs jouables à cette date
        associer matchsDuJour à gameDay

        trier les matchs par importance
        pour chaque match
            si match très important
                moment <- Evening
            sinon si match important
                moment <- Night
            sinon
                moment <- Afternoon
            fin si
        fin pour

        pour chaque match de matchsDuJour
            notifier le schedule des équipes
        fin pour

        si gameDay n'est pas vide
            ajouter gameDay au calendrier
        fin si

        dateCourante <- dateCourante + 1 jour
    fin tant que

    retourner NBACalendar(calendrier)
fin fonction
```

### Pseudo-code pour les playoffs

```text
fonction buildPlayoffCalendar(round, dateDebut)
    calendrier <- vide

    générer les matchs attendus de la série de playoffs

    pour chaque série du tour
        récupérer les 4 premiers matchs théoriques
        calculer un décalage de départ pour alterner les séries

        programmer :
            match 1 à dateDebut + 0 ou 1 jour
            match 2 à dateDebut + 2 ou 3 jours
            match 3 à dateDebut + 4 ou 5 jours
            match 4 à dateDebut + 7 ou 8 jours

        pour chaque match programmé
            l'ajouter au GameDay correspondant
            fixer le moment du match à Night
            notifier le schedule
        fin pour
    fin pour

    retourner NBACalendar(calendrier)
fin fonction
```

### Cas des différents builders

```text
si tour = First Round
    dateDebut <- PLAYOFF_DEBUT_DATE

si tour = Conference Semis
    dateDebut <- dateFinTourPrecedent + 2 jours

si tour = Conference Finals
    dateDebut <- dateFinTourPrecedent + 2 jours

si tour = NBA Finals
    dateDebut <- dateFinTourPrecedent + 2 jours
```

### Idée clé

```text
Le builder de calendrier suit toujours la même logique :
1. remettre à zéro l'état de planification
2. générer les matchs
3. répartir les matchs dans le temps
4. créer les objets GameDay
5. retourner un NBACalendar prêt à être utilisé par la simulation
```

## 6. Ordre de lecture recommandé

Pour un nouveau développeur, le meilleur parcours est le suivant :

1. [`LeagueManager.java`](../src/process/LeagueManager.java)
2. [`LeagueBuilder.java`](../src/process/builder/LeagueBuilder.java)
3. [`League.java`](../src/data/league/League.java)
4. [`Team.java`](../src/data/team/Team.java)
5. [`Player.java`](../src/data/player/Player.java)
6. [`CalendarBuilder.java`](../src/process/builder/CalendarBuilder.java)
7. [`GameGenerator.java`](../src/process/GameGenerator.java)
8. [`GameManager.java`](../src/process/GameManager.java)
9. [`GameSimulator.java`](../src/process/GameSimulator.java)
10. [`MainGui.java`](../src/gui/frame/MainGui.java)

## 7. Points d’attention

- Le chargement initial dépend d’un CSV situé dans `src/test`, même si les classes de test ne font pas partie du périmètre principal de documentation.

- Les classes `repositery` sont des registres globaux.
  Elles simplifient la lecture du builder, mais augmentent le couplage.

- `FinanceBuilder` est encore très peu développé.

- Une grande partie de `data` porte l’état, tandis que l’intelligence métier est surtout dans `process`.

README - Simulation NBA (Projet Java)

1. Prérequis
----------------
- Java JDK (version 8 ou supérieure)
- Eclipse IDE 

2. Import du projet
----------------
- Ouvrir Eclipse
- Créer un projet
- Sélectionner les dossiers du projet
- Les importer au dossier racine

3. Librairies utilisées
----------------
Le projet utilise les bibliothèques suivantes :

- JFreeChart (version 1.0.19) : affichage des graphiques financiers
- JCommon (version 1.0.23) : dépendance nécessaire à JFreeChart
- Log4j (version 1.2.17) : gestion des logs de l’application

Ils doivent être ajoutés au build path si Eclipse ne les détecte pas automatiquement :

Right click sur le projet > Build Path > Add External JARs

Un fichier de configuration log4j (log4j.properties) est également inclus.

4. Vérification du projet
----------------
- Vérifier que le JDK est bien configuré :
  Right click sur le projet > Properties > Java Build Path

- Vérifier que les librairies sont bien présentes dans le projet

5. Lancement du programme
----------------
- Ouvrir la classe principale App
- Vérifier qu’elle contient une méthode main
- Lancer :
  Right click > Run As > Java Application

6. Utilisation de l’application
----------------
- Une fenêtre d’accueil, cliquer sur continuer
- Une fenêre de commencement s’ouvre avec une carte des équipes
- L’utilisateur sélectionne une équipe
- Il configure les paramètres financiers initiaux
- Il lance la simulation de la saison

Une fois la simulation lancée, l’utilisateur peut :
- consulter le calendrier
- visualiser les matchs et leurs statistiques
- suivre le classement
- observer les finances des équipes
- accéder à une simulation de match en direct

7. Problèmes possibles
----------------
- Erreur de librairie manquante :
  → vérifier que les fichiers JAR sont bien ajoutés

- Aucun affichage :
  → vérifier que la bonne classe principale est lancée

- Erreur de compilation :
  → vérifier la version du JDK

- Problème de logs :
  → vérifier la présence du fichier log4j.properties

8. Auteur
----------------
Projet réalisé dans le cadre d’un projet universitaire (Licence 2).
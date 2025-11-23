## Enchainement des steps 

Cette étape : 
- extrait les bénéficiaires en fonction des paramètres d'entrée
- génère un fichier CSV

## Liste des répertoires d'entrée/sortie

**Répertoire d'entrée sur la PEFB**

/pefb/<Chemin Client/Couloir>/EG/PG/BDD/Input/
Configurable.

**Répertoire de sortie sur la PEFB** 

/pefb/<Chemin Client/Couloir>/EG/PG/BDD/Output/
Configurable.

## Fonctionnement technique

### Vue d'ensemble

Le batch 635 traite chaque fichier du dossier d'entrée PEFB individuellement. Le processus comprend plusieurs étapes : contrôle des paramètres, lecture des fichiers, et extraction des données AMC éligibles. Les erreurs sont enregistrées dans un fichier Arl, tandis que les lignes valides sont stockées dans un fichier temporaire pour l'extraction.

### Traitement parallèle (Asynchrone)

Pour permettre un traitement plus rapide et efficace, le batch parcourt tous les fichiers d'entrée PEFB, et pour chaque fichier, lance les extractions AMC de manière synchronisée.
Afin de mettre en place un traitement asynchrone, la classe Java ExecutorService est utilisée et permet d'exécuter des fonctions dans des Threads séparés, sans bloquer le Thread principal.

### Reprise après plantage

Lors d'un plantage sur cet OMU, il n'y a pas de manipulation de fichiers à effectuer.

Etapes de relance :

- Relancer le traitement

Si replantage, suite à 1ère relance, contacter support niveau 1 équipe BDDS (Hors prod: communication par Zoom, en prod: ouverture d'une néocase)

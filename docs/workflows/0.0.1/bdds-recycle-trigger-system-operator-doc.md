### Enchaînement des étapes

L’OMU contient une seule étape “recyclage-trigger-job”

### Liste des répertoires d'entrée/sortie

Aucun

### Fonctionnement technique

Le traitement se lance à la demande avec en entrée un fichier déposé sur l'espace personnel.

### Déroulement du traitement

- Récupération d'un fichier sur l'espace personnel, contenant la liste des déclencheurs à recycler
- Recyclage des déclencheurs via l'appel d'une API

### Procédure de reprise
En cas de plantage: 
- refaire une requête pour récupérer la liste des déclencheurs en anomalie
- poser le fichier sur l'espace personnel
- lancer l'OMU

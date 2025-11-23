### Enchaînement des étapes

L’OMU contient une seule étape “extractbenefsansdroitstp-job”

### Liste des répertoires d'entrée/sortie

Aucun

### Fonctionnement technique

Le traitement prend en entrée l'année à partir de laquelle on souhaite connaître les personnes n'ayant pas de droits TP.
Le traitement n'est lançable qu'une seule fois par jour.

### Déroulement du traitement

- Envoi d'un user-event indiquant que le traitement se lance et qu'il s'agit d'un traitement long
- Vérification de la dernière exécution du traitement : s'il y a déjà eu une exécution ce jour-là, le traitement ne se réexécute pas
- Lancement d'une agrégation qui trouve les assurés sans droits TP
- Pour chaque assuré trouvé, lancement d'une agrégation permettant de trouver les autres contrats htp (ouverts) de l'assuré
- Extraction des résultats dans un fichier csv dans l'espace personnel
- Envoi d'un user-event indiquant que le traitement est terminé

### Procédure de reprise
En cas de plantage: 
- vérifier s'il existe un histoExec concernant ce plantage et le supprimer
- relancer l'OMU

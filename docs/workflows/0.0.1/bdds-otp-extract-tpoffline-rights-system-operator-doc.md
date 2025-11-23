### Enchaînement des étapes

L’OMU ne contient 1 étape :

- Traitement “step-extract-rights” : Recherche et extraction des droits TP offline sur les clients PUC

### Liste des répertoires d'entrée/sortie

En entrée :

- Espace personnel du gestionnaire, retrouvé grâce au paramètre d'entré "filePath"

En sortie :

- Espace personnel du gestionnaire, retrouvé grâce au paramètre d'entré "filePath"

### Fonctionnement technique

Le traitement :

- Récupère le fichiers CSV déposé par le gestionnaire sur son espace personnel
- Contrôle le fichier (présence des bonnes colonnes, format des données) 
    - NB : si une donnée est KO (exemple AAAAA dans une date) : on traite quand même les autres

- Pour les lignes dont le N° d’AMC est vide : recherche dans la base Aiguillage le ou les AMC correspondantes aux critères fournis
- Interroge MLS pour identifier le Beyond de chaque client PUC à interroger par AMC
- Interroge par lot les Beyond Client pour récupérer les droits des bénéficiaires
- Agrège les résultats dans le CSV de sortie et le dépose dans l’espace de travail du gestionnaire

### Procédure de reprise

- Pas de procédure particulière de reprise : le traitement peut être rejoué directement

    

## Enchainement des steps

### Cet OMU comporte 4 steps

- Les fichiers sont récupérés d’un serveur de fichiers puis déposés sur le workdir.
- Les fichiers sont ensuite récupérés du workdir et traités. On prend l'émetteur passé en paramètre et applique la règle suivante : si un bénéficiaire dispose de plusieurs produits et si la date de sortie des deux produits est dans la même année civile, il ne faut générer qu'un seul produit d'ordre 1 en sortie.
- Les fichiers sont ensuite fusionnés par AMC si la variable ACTIVATED est true, sinon les fichiers sont simplement récupérés unitairement. Le fichier mergé ou les fichiers unitaires sont redéposés dans le workdir.
- Enfin les fichiers sont récupérés du workdir, et selon la variable STRATEGY, il sont soit déposés sur le serveur de fichiers, soit sur la PEFB.

## Liste des répertoires d'entrée/sortie

**Répertoire d'entrée sur le serveur de fichier**

/XCHANGE/<REPERTOIRE_ENV>/files/ALMV3/tmpout/XML/

**Répertoire de sortie sur la PEFB**

/pefb/<path_client>/EG/PG/BDD/Output/

**Répertoire de sortie sur le serveur de fichier**

/XCHANGE/<REPERTOIRE_ENV>/files/out/ALMV3/

**Répertoire d'archive**

/workdir/ALMV3/archive/

**Paramétrage d'environnement :**

- Booléen permettant d'activer la fusion des fichiers par AMC (3ème step) : ACTIVATED
- le répertoire d'entrée sur le workdir : WORKDIR_INPUT_FOLDER
- le répertoire de sortie sur le workdir : WORKDIR_OUTPUT_FOLDER
- le répertoire d'archive : ARCHIVE_PATH

## Fonctionnement technique

Lors d'un plantage sur cet OMU, il n'y a pas de manipulation de fichiers à effectuer.

Il suffit de relancer le traitement.

Si replantage, suite à 1ère relance, contacter support niveau 1 équipe BDDS (Hors prod: communication par Zoom, en prod: ouverture d'une néocase)

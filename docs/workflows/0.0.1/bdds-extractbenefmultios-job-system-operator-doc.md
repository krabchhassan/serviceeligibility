## Enchainement des steps

Cette étape :

- Récupère les bénéficiaires ayant plusieurs sociétés émettrices
- Calcul parmi ces bénéficiaires, lesquels ont des sociétés émettrices différentes pendant des périodes qui se
  chevauchent
- Génère 1 fichier json sur le workdir

## Liste des répertoires d'entrée/sortie

**Répertoire de sortie sur le workdir**

Configurable par le paramétrage d'environnement OUTPUT_PATH

## Fonctionnement technique

**Format du fichier de sortie :**

Export_Benef_Multi_OS.json

**Paramétrage d'environnement :**

- Le répertoire de sauvegarde sur le workdir : OUTPUT_PATH
- Booléen permettant d'activer l'extraction sur l'environnement  : EXTRACT_FLUX
## Enchainement des steps

Cette étape :

- Récupère les historiques de consolidation contrat vieux de plus d'un an
- Les extrait dans un fichier poussé sur s3
- Purge ces historisations de la base elastic

## Liste des répertoires d'entrée/sortie

**Répertoire de sortie sur s3**

Configurable par le paramétrage d'environnement s3_extraction_histo_consos.

## Fonctionnement technique

**Format du fichier de sortie :**

Export_Histo_Contrat_yyyy-MM-dd.json

**Paramétrage d'appel**

- Le nombre de jour à partir duquel faire la purge : --DAYS

**Paramétrage d'environnement :**

- Le répertoire de sauvegarde sur s3 : s3_extraction_histo_consos
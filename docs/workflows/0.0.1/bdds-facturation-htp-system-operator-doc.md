## Enchainement des steps

Cette étape :

Pour un client de type _INSURER_ :
- récupère le mois et l'année paramétrés ou s'il n'y a aucun paramétrage le mois précédant la date courante
- fait le calcul, pour le mois de facturation, du nombre de bénéficiaires des clients HTP déléguant le TP
- génère 1 fichier CSV par déclarant, 1 ligne par société émettrice

Pour un client de type _OTP_ :
- récupère le mois et l'année paramétrés ou s'il n'y a aucun paramétrage le mois précédant la date courante
- récupère la liste des AMC (séparées par des virgules)
- fait le calcul, pour cette liste d'AMC et, pour le mois de facturation, du nombre de bénéficiaires ayant des droits TP
- génère 1 seul fichier CSV pour toutes les AMC, 1 ligne par AMC

## Liste des répertoires d'entrée/sortie

**Répertoire de sortie sur la PEFB**

/pefb/<Chemin_client>/EG/PG/BDD/Output/

Configurable par le paramétrage d'environnement CSV_OUTPUT_DIRECTORY.

## Fonctionnement technique

**Format du fichier de sortie :**

Pour un client de type _INSURER_ :

- facturation_Cetip_<idDeclarant>_<date_traitement>.csv

Pour un client de type _OTP_ :

- facturation_Cetip_<date_traitement>.csv

**Entête fichier de sortie :**

Pour un client de type _INSURER_ :

- idDeclarant
- Société émettrice
- Nombre de bénéficiaires
- Mois de facturation

Pour un client de type _OTP_ :

- idDeclarant
- Nombre de bénéficiaires
- Mois de facturation

**Paramétrage d'appel**

- le mois cible pour le calcul (MM) : --MOIS
- l'année cible pour le calcul (yyyy) : --ANNEE

Pour un client de type _OTP_, il y a un paramètre supplémentaire obligatoire :

- la liste des AMC (séparées par des virgules) : --AMC_LIST

**Paramétrage d'environnement :**

- le répertoire de sortie sur la PEFB : CSV_OUTPUT_DIRECTORY
- le séparateur des fichier csv (par défaut ";") : CSV_DELIMITER
- le type de l'environnement client (par défaut INSURER) : CLIENT_TYPE
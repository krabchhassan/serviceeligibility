## Enchainement des steps

Cette étape :

- récupère tous les contrats dans la collection servicePrestation
- si le format demandé est CSV :
  - le traitement génère 1 fichier contenant les données de niveau contrat, 1 contenant les données de niveau assurés et 1
    contenant les données de niveau garanties
- si le format demandé est JSON :
  - le traitement génère 1 fichier comportant, à la fois, les données de niveau contrat, assurés et garanties. La limite maximale 
  de nombre de contrat par flux JSON est fixée à 1 000 000. Cette limite est paramétrable via le paramétrage d'environnement MAX_CONTRACTS_PER_FILE mais ne pourra pas excéder
  la limite maximale autorisée de 1 000 000 de contrats par flux.

## Liste des répertoires d'entrée/sortie

**Répertoire de sortie sur la PEFB**

Exemple:
/pefb/<Chemin_client>/extractpopulation/out/

Configurable par le paramétrage d'environnement OUTPUT_DIRECTORY.

## Fonctionnement technique

### **Paramétrage d'appel**

- FORMAT (obligatoire) : le format des fichiers générés, CSV ou JSON

### **Format des fichiers de sortie en mode CSV :**

Extraction_Contrats_<date_traitement>.csv

**Entête :**

- N° AMC
- N° Adhérent
- N° de contrat

Extraction_Contrats_Assures_<date_traitement>.csv

**Entête :**

- N° AMC
- N° Adhérent
- N° de contrat
- N° de la personne rattachée au contrat
- isSouscripteur
- Nir principal
- Date de naissance
- Rang de naissance
- Nom de famille
- Nom d’usage
- Prénom

Extraction_Contrats_Assures_Garanties_<date_traitement>.csv

**Entête :**

- N° AMC
- N° Adhérent
- N° de contrat
- N° de la personne rattachée au contrat
- Code assureur
- Code GT
- Date début
- Date fin

### **Format des fichiers de sortie en mode JSON :**

Extraction_Contrats_<date_traitement>_<index_du_fichier>.json

- N° AMC
- N° Adhérent
- N° de contrat
- Date de souscription
- Date de résiliation
- Société emettrice
- Liste des assurés
  - N° de la personne rattachée au contrat
  - isSouscripteur
  - Nir principal
  - Liste des affiliations RO de l'assuré
    - Nir
    - Code Régime
    - Code Caisse
    - Code Centre
    - Date début
    - Date fin
  - Date de naissance
  - Rang de naissance
  - Code de la qualité de l'assuré
  - Rang administratif
  - Nom de famille
  - Nom d’usage
  - Prénom
  - Date de debut d'adhesion individuelle
  - Date de radiation
  - Liste des garanties de l'assuré
    - Code assureur
    - Code GT
    - Date début
    - Date fin

### **Paramétrage d'environnement :**

- le répertoire de sortie sur la PEFB : OUTPUT_DIRECTORY
- le séparateur des fichier csv (par défaut ";") : CSV_DELIMITER
- le nombre maximal de contrats par fichier JSON (limite maximale autorisée fixée à 1 000 000) : MAX_CONTRACTS_PER_FILE
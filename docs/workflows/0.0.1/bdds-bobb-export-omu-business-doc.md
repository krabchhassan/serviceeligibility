# BOBB - Export des fichiers de correspondance

## Description :

L'OMU “BOBBExport” consiste à exporter des fichiers de correspondance produit BO/Beyond.

## Schéma des intéractions :

Ci-dessous un schéma de synthèse repéresentant les échanges et intéractions de ce traitement:

![SchemaBobbExport.png](SchemaBobbExport.png)

Cette OMU bobb-export-omu (is-bdds-bobb-export) est déclenché via Scheduling par utilisateur, le fichier BOBB exporté est déposé dans l'espace personnel de l'utilisateur.
Il limite la taille des fichiers avec un nombre maximal de lignes par fichier.

### Les valeurs des paramétres en entrée :
- Nombre max de lignes par fichier

### Les données à récupérer des variables d’environnement
- Limite: Taille du fichier a ne pas dépasser: Valeur par défaut "50 000 lignes"

### Eléments du CREX :
- Nombre de fichier produits
- Liste des fichiers produits
- Nombre de lignes par fichier
- Nombre total de ligne exportées

### La liste des évènements envoyée:
- bobb-export-file-succeeded-event.schema.json
- bobb-export-file-failed-event.schema.json


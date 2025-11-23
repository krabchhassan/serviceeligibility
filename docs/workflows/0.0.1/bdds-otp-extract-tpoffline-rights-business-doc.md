### Description détaillée

Cet OMU permet à un Beyond OTP d'interroger les Beyond des clients PUC afin de récupérer les données TP Offline présentes dans leur BDDS.

Le traitement :

- Récupère le fichiers CSV déposé par le gestionnaire sur son espace personnel
- Contrôle le fichier (présence des bonnes colonnes, format des données) 
    - NB : si une donnée est KO (exemple AAAAA dans une date) : on traite quand même les autres

- Pour les lignes dont le N° d’AMC est vide : recherche dans la base Aiguillage le ou les AMC correspondantes aux critères fournis
- Interroge MLS pour identifier le Beyond de chaque client PUC à interroger par AMC
- Interroge par lot les Beyond Client pour récupérer les droits des bénéficiaires
- Agrège les résultats dans le CSV de sortie et le dépose dans l’espace de travail du gestionnaire

#### Entrées

Un fichier CSV dans le répertoire d'entrée :

- Format csv avec séparateur “;”
- Le fichier doit contenir 4 colonnes :
    - **nir** (obligatoire) : sur 13 caractères, sans la clé. Le traitement remonte tous les droits TP offline des bénéficiaires ayant ce NIR en NIR principal, ou bien comme NIR ouvrant droits.
    - **date_naissance** (facultatif) : date de naissance du bénéficiaire au format YYMMDD. Le traitement remonte en priorité tous les droits TP offline des bénéficiaires dont la date de naissance correspond (au 19ème siècle ou au 20ème siècle). Si aucun bénéficiaire n'est trouvé, le traitement remonte également les bénéficiaires dont le jour, ou le mois, ou l'année de naissance correspond.
    - **date_soins** (obligatoire) : date de recherche des droits, au format SSAAMMDD.
    - **amc** (facultatif) : numéro d'AMC sur 10 caractères.

#### Sorties

A la fin du traitement, le traitement dépose le fichier contenant les droits TP offline extraits :

- Une ligne par AMC / Bénéficiaire / Produit / Domaine TP (donc N lignes en sortie pour une ligne en entrée), avec : 
    - ligne : N° de la ligne dans le fichier en entrée
    - nir_demande : NIR dans le fichier en entrée
    - date_naissance_demande : Date de naissance dans le fichier en entrée, s'il était précisé
    - date_soins_demande : Date de recherche dans le fichier en entrée
    - amc_demande : N° d'AMC, s'il était précisé
    - amc : N° d'AMC où on été récoltés les droits sur le client PUC
    - nir_benef : NIR principal du bénéficaire 
    - nir_od1 : NIR de l'ouvrant droit 1
    - nir_od2 : NIR de l'ouvrant droit 2
    - date_naissance : date de de naissance retenue pour la recherche (SSAAMMDD)
    - rang_naissance : rang de naissance
    - adherent : N° d'adhérent
    - contrat : N° d econtrat
    - produit : code du produit
    - domaine : domaine TP
    - debut_droits : date de début des droits TP offline
    - fin_droits : date de fin des droits TP offline
    - reference_couverture : référence couverture
    - numero_sts_formule : N° STS de la formule de remboursement
    - formule_remboursement : Code de la formule de remboursement
    - taux_remboursement : Taux de remboursement
    - unite_taux_remboursement : Unité du taux
    - date_donnees : date de création ou bien de mise à jour de l’enregistrement dans BDDS  (SSAAMMDD)
    - code_erreur : Si une erreur a empêché de remonter les droits du bénéficiaire elle est indiquée ici
    - libelle_erreur : Description de l'erreur

La liste des erreurs possibles est la suivante :

|      | **Niveau**             | **Code** | **Libellé erreur**                           | **Données complémentaires** |
| :--- | :--------------------- | :------- | :------------------------------------------- | --------------------------- |
| 1    | vérification du schéma | JSONKO   | Erreur lors de la vérification du JSON shéma |                             |



### Schéma des interactions



### Evènements au journal applicatif

Les évènements suivants sont tracés au journal :

- extract-tpoffline-start-event
- extract-tpoffline-end-event

### 
## Enchainement des steps

Ce job :

- s'execute pour les AMC pour lesquelles au moins l'un des services suivants sont ouverts à la date du traitement
    - CARTE-PAPIER
    - CARTE-DEMATERIALISEE
- analyse toutes les déclarations de droits enregistrées depuis :
    - une date de synchronisation, paramétrée par AMC (paramétrage Base De Droits du service carte dématérialisée)
    - la date du dernier traitement de consolidation (date + heure par service/AMC) si la date de synchronisation n'est
      pas paramétrée
- sélectionne les bénéficiaires avec droits :
    - ouverts à la date du traitement
    - ou ouverts en anticipé
    - ou résiliés en anticipé
- sélectionne, pour ces bénéficiaires, les contrats qualifiés « B » (contrat de base) ou « C » (contrat base +
  surcomplémentaire). Les contrats en surcomplémentaire (qualification « S ») sont ignorés
- invalide les cartes enregistrées précédemment ("isLastCarteDemat" à false) concernant les contrats sélectionnés (et
  supprime les "declarationsConsolideesCarteDemat")
- consolide (ou agrège) les droits et garanties d’un bénéficiaire par domaine de droits sous conditions :
    - les unités de couverture soient compatibles (voir cahier des charges pour plus d'infos)
    - Si l’unité du taux de couverture est XX (Texte), il y a compatibilité uniquement lorsque la valeur du taux de
      couverture du domaine est identique entre garanties à consolider.
    - Si l’unité est PO (Pourcentage), FO (Forfait), TA (Taux) ou NN (Numérique) :
        - un taux de couverture de valeur zéro est cumulable
        - si le résultat du cumul des taux de couverture dans le domaine est toujours de valeur zéro, le domaine n’est
          pas reporté dans le résultat de la consolidation
        - si aucun domaine ne peut être reporté pour un bénéficiaire, le contrat est rejeté (tous taux à zéro).
    - les priorités droits sont différentes (garantie de base / garantie optionnelle)
- enregistre une carte dématérialisée / une carte papier par :
    - contrat : inscription des bénéficiaires du contrat
    - dates de fin de droits identiques : communauté de droits pour les bénéficiaires du contrat. Une autre carte
      dématérialisée est préparée pour la période de droits suivante

## Liste des répertoires d'entrée/sortie

**Répertoire de sortie sur la PEFB**

Répertoire de stockage de l’ARL :

Configuration par le paramétrage de l’environnement via la variable ARL_FOLDER

## Fonctionnement technique

**Nommage du fichier ARL :**

ARL_620_<codePartenaireAMC>_<numeroAMC>_<OperateurPrincipalAMC>_AAAAMMDD_HHMMSS.csv

**Entête fichier d'ARL :**

- TRAITEMENT_LE
- SERVICE
- NO_AMC
- CONVENTION
- REGROUPEMENT
- REGROUPEMENT_DETAILLE
- NO_PERSONNE
- NIR
- DATE_NAISSANCE
- RANG_NAISSANCE
- NOM
- PRENOM
- NO_CONTRAT
- GESTIONNAIRE_CONTRAT
- GROUPE_ASSURES
- DROITS
- MVT
- DATE_DECLARATION
- TYPE_REJET
- NIVEAU_REJET
- REJET
- MOTIF_REJET
- VALEUR_REJET
- NOM_FICHIER_NO_DECLARATION
- EMETTEUR_DROITS
- CODE_CIRCUIT
- LIBELLE_CIRCUIT
- DESTINATAIRE_DROITS
- BO_EMETTEUR_DROITS

**Paramétrage d'appel**

Tous les paramètres sont optionnels.

- couloirClient :  Couloir client défini dans les pilotages des amcs.

**Paramétrage d'environnement :**

- le répertoire de sortie sur la PEFB : ARL_FOLDER
- le paramétrage S3 du fichier de configuration pour les cartes papier

**Cas de reprise :**

L'OMU lance plusieurs threads par AMC et par groupe de contrats.
Si l'OMU tombe en erreur sur la step "step-620-java" :

- vérifier la collection historiqueExecutions, il faut voir si une AMC n'a pas de date de fin de cartes renseignés :
  Requete : { "dateFinCartes" : { $exists : false } }

Si c'est le cas, il faut relancer le batch, celui va reprendre cette AMC pour la terminer, sinon il n'y a rien à faire.




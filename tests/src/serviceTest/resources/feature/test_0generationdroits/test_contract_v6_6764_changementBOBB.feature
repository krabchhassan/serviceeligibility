Feature: BLUE-6764 Génération des droits avec résiliation + changement BOBB qui ne ferme pas les droits de l'ancien produit à tort + annulDroitsOffline

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS |
      | libelle | IS |
    Given I create a declarant from a file "declarantbaloo"
    Given I create a contract element from a file "gt_6764"
    And I create a service prestation from a file "servicePrestation-6764"
    And I create TP card parameters from file "parametrageTP6764-2023"
    Then I renew the rights on "%%CURRENT_YEAR%%-02-18" with mode "RDO"
    Then I wait for 1 declaration
    # V - Renouvellement %%LAST2_YEARS%%
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | codeProduit       | debut                 | fin                   | finOnline | periodeFermetureDebut | periodeFermetureFin |
      | GEAS20998B | HOSP    | 1472_AJOUT_01_P01 | %%LAST2_YEARS%%-01-01 | %%LAST2_YEARS%%-12-31 | null      | null                  | null                |
      | GEAS20998B | AUDI    | 1472_AJOUT_01_P01 | %%LAST2_YEARS%%-01-01 | %%LAST2_YEARS%%-12-31 | null      | null                  | null                |
      | GEAS20998B | DENT    | 1472_AJOUT_01_P01 | %%LAST2_YEARS%%-01-01 | %%LAST2_YEARS%%-12-31 | null      | null                  | null                |
      | GEAS20998B | PHAR    | 1472_AJOUT_01_P01 | %%LAST2_YEARS%%-01-01 | %%LAST2_YEARS%%-12-31 | null      | null                  | null                |
    And I create TP card parameters from file "parametrageTP6764-2024"
    Then I renew the rights on "%%CURRENT_YEAR%%-02-18" with mode "RDO"
    Then I wait for 3 declaration
    # R - Fermeture à fin %%LAST2_YEARS%% avant de réouvrir en %%LAST_YEAR%%
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | codeProduit       | debut                 | fin                   | finOnline             | periodeFermetureDebut | periodeFermetureFin   |
      | GEAS20998B | HOSP    | 1472_AJOUT_01_P01 | %%LAST2_YEARS%%-01-01 | %%LAST2_YEARS%%-12-31 | %%LAST2_YEARS%%-12-31 | %%LAST_YEAR%%-01-01   | %%LAST2_YEARS%%-12-31 |
      | GEAS20998B | AUDI    | 1472_AJOUT_01_P01 | %%LAST2_YEARS%%-01-01 | %%LAST2_YEARS%%-12-31 | %%LAST2_YEARS%%-12-31 | %%LAST_YEAR%%-01-01   | %%LAST2_YEARS%%-12-31 |
      | GEAS20998B | DENT    | 1472_AJOUT_01_P01 | %%LAST2_YEARS%%-01-01 | %%LAST2_YEARS%%-12-31 | %%LAST2_YEARS%%-12-31 | %%LAST_YEAR%%-01-01   | %%LAST2_YEARS%%-12-31 |
      | GEAS20998B | PHAR    | 1472_AJOUT_01_P01 | %%LAST2_YEARS%%-01-01 | %%LAST2_YEARS%%-12-31 | %%LAST2_YEARS%%-12-31 | %%LAST_YEAR%%-01-01   | %%LAST2_YEARS%%-12-31 |
    # V - Renouvellement %%LAST_YEAR%%
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | codeProduit       | debut               | fin                 | finOnline | periodeFermetureDebut | periodeFermetureFin |
      | GEAS20998B | HOSP    | 1472_AJOUT_01_P01 | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | AUDI    | 1472_AJOUT_01_P01 | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | DENT    | 1472_AJOUT_01_P01 | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | PHAR    | 1472_AJOUT_01_P01 | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | null      | null                  | null                |
    And I create TP card parameters from file "parametrageTP6764-2025"
    Then I renew the rights on "%%CURRENT_YEAR%%-02-18" with mode "RDO"
    Then I wait for 6 declarations
    # R - Fermeture à fin %%LAST_YEAR%% avant de réouvrir en 2025
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie   | domaine | codeProduit       | debut               | fin                 | finOnline           | periodeFermetureDebut  | periodeFermetureFin |
      | GEAS20998B | HOSP    | 1472_AJOUT_01_P01 | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GEAS20998B | AUDI    | 1472_AJOUT_01_P01 | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GEAS20998B | DENT    | 1472_AJOUT_01_P01 | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GEAS20998B | PHAR    | 1472_AJOUT_01_P01 | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
    # V - Renouvellement 2025
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie   | domaine | codeProduit       | debut                  | fin                    | finOnline | periodeFermetureDebut | periodeFermetureFin |
      | GEAS20998B | HOSP    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | AUDI    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | DENT    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | PHAR    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
    Given I create a contract element from a file "gt_6764_newBOBBSansChangementPW"

  @smokeTests @6764 @caseConsolidation
  Scenario: BLUE-6764 - Test changement de BOBB : Renouvellement %%LAST2_YEARS%% + %%LAST_YEAR%% + %%CURRENT_YEAR%% puis changement de produit + envoi event contrat + annulDroitsOffline doit fermer les droits de l'ancien produit
    And I create TP card parameters from file "parametrageTP6764-PilotageBO"
    Given I send a test contract from file "servicePrestation-6764"
    Then I wait for 10 declarations
    # R - Fermeture du ONLINE de l'ancien produit
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 8
      | garantie   | domaine | codeProduit       | debut                  | fin                 | finOnline           | periodeFermetureDebut  | periodeFermetureFin    |
      | GEAS20998B | HOSP    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | GEAS20998B | AUDI    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | GEAS20998B | DENT    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | GEAS20998B | PHAR    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
    # V - Ouverture du nouveau produit
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 9
      | garantie   | domaine | codeProduit       | debut                  | fin                    | finOnline | periodeFermetureDebut | periodeFermetureFin |
      | GEAS20998B | HOSP    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | AUDI    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | DENT    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | PHAR    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
    And I create TP card parameters from file "parametrageTP6764_annulDroitOffline"
    Then I renew the rights on "%%CURRENT_YEAR%%-02-18" with mode "RDO"
    Then I wait for 13 declaration
    # R - Fermeture du ONLINE & OFFLINE de l'ancien produit suite au forçage de l'annulation des droits offline
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 10
      | garantie   | domaine | codeProduit       | debut                  | fin                 | finOnline           | periodeFermetureDebut  | periodeFermetureFin |
      | GEAS20998B | HOSP    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GEAS20998B | AUDI    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GEAS20998B | DENT    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GEAS20998B | PHAR    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
    # R - Fermeture du ONLINE & OFFLINE du nouveau produit suite au forçage de l'annulation des droits offline
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 11
      | garantie   | domaine | codeProduit       | debut                  | fin                 | finOnline           | periodeFermetureDebut  | periodeFermetureFin |
      | GEAS20998B | HOSP    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GEAS20998B | AUDI    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GEAS20998B | DENT    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GEAS20998B | PHAR    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
    # V - Ouverture du nouveau produit
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 12
      | garantie   | domaine | codeProduit       | debut                  | fin                    | finOnline | periodeFermetureDebut | periodeFermetureFin |
      | GEAS20998B | HOSP    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | AUDI    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | DENT    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | PHAR    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-6764-2" content

  @smokeTests @6764 @caseConsolidation
  Scenario: BLUE-6764 - Test changement de BOBB : Renouvellement %%LAST2_YEARS%% + %%LAST_YEAR%% + %%CURRENT_YEAR%% puis changement de produit + annulDroitsOffline doit fermer les droits de l'ancien produit
    And I create TP card parameters from file "parametrageTP6764_annulDroitOffline"
    Then I renew the rights on "%%CURRENT_YEAR%%-02-18" with mode "RDO"
    Then I wait for 8 declarations
    # R - Fermeture du ONLINE & OFFLINE de l'ancien produit suite au forçage de l'annulation des droits offline
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 6
      | garantie   | domaine | codeProduit       | debut                  | fin                 | finOnline           | periodeFermetureDebut  | periodeFermetureFin |
      | GEAS20998B | HOSP    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GEAS20998B | AUDI    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GEAS20998B | DENT    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GEAS20998B | PHAR    | 1472_AJOUT_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
    # V - Ouverture du nouveau produit
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 7
      | garantie   | domaine | codeProduit       | debut                  | fin                    | finOnline | periodeFermetureDebut | periodeFermetureFin |
      | GEAS20998B | HOSP    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | AUDI    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | DENT    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |
      | GEAS20998B | PHAR    | 1472_AJOUT_01_P03 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | null                  | null                |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-6764-2" content

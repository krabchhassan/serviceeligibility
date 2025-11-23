Feature: Test prioritaires pour cause de plantage aléatoire

  @smokeTests @caseResiliation4937 @release
  Scenario: JIRA 4937 : I send a contract with resiliation in %%CURRENT_YEAR%% : TP rights up to resiliation date
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "generationDroitsTP_4943/4937/resil_en_2025_contrat0"
    Then I wait for 2 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a test contract from file "generationDroitsTP_4943/4937/resil_en_2025_contrat2"
    When I get triggers with contract number "MBA1476" and amc "0000401166"
    Then I wait for 4 declarations
    Then I get one more trigger with contract number "MBA1476" and amc "0000401166" and indice "1" for benef
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut                  | fin                 | finOnline           | periodeFermetureDebut  | periodeFermetureFin    |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut                  | fin                    | finOnline              | periodeFermetureDebut  | periodeFermetureFin    |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-03-09 | %%CURRENT_YEAR%%-03-09 | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-03-09 | %%CURRENT_YEAR%%-03-09 | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 |
    Then I wait for 1 contract

  @smokeTests @caseConsolidation @caseRenouvellement
  Scenario: I renew a contract in 2021, 2022 and 2023 with 1 benef and resiliation date in 2023
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a contract element from a file "gtaxa_cgdiv"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2021Prio"
    When I create a service prestation from a file "servicePrestationGIB2"

    When I renew the rights today with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 1 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | 2021-01-01 | 2021-12-31 | 2022-12-31 |
      | AXASCCGDIV | HOSP    | 2021-01-01 | 2021-12-31 | 2022-12-31 |
    # fin online -> produit dans pw qui se termine au 31/12/2022
    When I remove all TP card parameters from database
    And I create manual TP card parameters from file "parametrageCarteTPManuel2022Prio"
    And I renew the rights today with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | 2022-01-01 | 2022-12-31 | 2022-12-31 |
      | AXASCCGDIV | HOSP    | 2022-01-01 | 2022-12-31 | 2022-12-31 |

    When I remove all TP card parameters from database
    And I create manual TP card parameters from file "parametrageCarteTPManuel2023Prio"
    And I renew the rights today with mode "RDO"
    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 4 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | 2023-01-01 | 2023-03-01 | 2023-03-01 |
      | AXASCCGDIV | HOSP    | 2023-01-01 | 2023-03-01 | 2023-03-01 |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "4937/contrattpaleatoire" content

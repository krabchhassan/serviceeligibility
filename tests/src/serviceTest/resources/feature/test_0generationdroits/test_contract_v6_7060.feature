Feature: Fin de GT 7060

  @smokeTests @caseConsolidation
  Scenario: Fin de GT année dernière + Generation année courante
    And I create a contract element from a file "gtaxa_ok"
    Given I create a contract element from a file "gtaxa"
    Given I create TP card parameters from file "parametrageTPBaloo_pilotageBO"
    Given I create a declarant from a file "declarantbaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a contract from file "contratV6/7060_base" to version "V6"
    Then I wait for 1 declaration
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut               | fin                 | finOnline |
      | AXASCCGDIM | OPAU    | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | null      |
    When I send a contract from file "contratV6/7060_gt_fin_nov_2024" to version "V6"
    Then I wait for 3 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut               | fin                   | finOnline             |
      | AXASCCGDIM | OPAU    | %%LAST_YEAR%%-01-01 | %%LAST2_YEARS%%-12-31 | %%LAST2_YEARS%%-12-31 |
      | AXASCCGDIM | HOSP    | %%LAST_YEAR%%-01-01 | %%LAST2_YEARS%%-12-31 | %%LAST2_YEARS%%-12-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut               | fin                 | finOnline           |
      | AXASCCGDIM | OPAU    | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-11-30 |
      | AXASCCGDIM | HOSP    | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-11-30 |
    When I send a contract from file "contratV6/7060_2025" to version "V6"
    Then I wait for 5 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut               | fin                 | finOnline           |
      | AXASCCGDIM | OPAU    | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-11-30 |
      | AXASCCGDIM | HOSP    | %%LAST_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-11-30 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIV | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIV | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And I wait for 1 contract
    And the expected contract TP is identical to "contrattp_7060" content

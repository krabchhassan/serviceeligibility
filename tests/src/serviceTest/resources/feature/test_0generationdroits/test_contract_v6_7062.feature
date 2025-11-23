Feature: Fin de GT avant radiation

  Background:
    Given I create a contract element from a file "gtaxa"
    Given I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests
  Scenario: Fin de GT avant fin annee
    When I send a contract from file "contratV6/7062_base" to version "V6"
    Then I wait for 1 declaration
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a contract from file "contratV6/7062_fin_gt_avant_fin_annee" to version "V6"
    Then I wait for 3 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-07-31 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-07-31 |
    Then I wait for 1 contract
    And the expected contract TP is identical to "gt_avant_fin_annee" content

  @smokeTests
  Scenario: Fin de GT avant radiation
    When I send a contract from file "contratV6/7062_base" to version "V6"
    Then I wait for 1 declaration
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a contract from file "contratV6/7062_fin_gt_avant_radiation" to version "V6"
    Then I wait for 4 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-15 | %%CURRENT_YEAR%%-07-31 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-15 | %%CURRENT_YEAR%%-07-31 |
    Then I wait for 1 contract
    And the expected contract TP is identical to "gt_avant_radiation" content

  @smokeTests
  Scenario: Fin de GT apres radiation
    When I send a contract from file "contratV6/7062_base" to version "V6"
    Then I wait for 1 declaration
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a contract from file "contratV6/7062_fin_gt_apres_radiation" to version "V6"
    Then I wait for 4 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-15 | %%CURRENT_YEAR%%-09-15 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-15 | %%CURRENT_YEAR%%-09-15 |
    Then I wait for 1 contract
    And the expected contract TP is identical to "gt_apres_radiation" content


  @smokeTests
  Scenario: 2 benefs, 1 benef resilie avant gt et 1 benef resilie apres gt
    When I send a contract from file "contratV6/7062_2_benefs" to version "V6"
    Then I wait for 2 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a contract from file "contratV6/7062_2_benefs_resilies" to version "V6"
    Then I wait for 8 declarations
    # R du benef resilie avant la fin de gt
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-15 | %%CURRENT_YEAR%%-09-15 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-15 | %%CURRENT_YEAR%%-09-15 |
    # R du benef resilie apres la fin de gt
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 7
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-15 | %%CURRENT_YEAR%%-07-31 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-15 | %%CURRENT_YEAR%%-07-31 |
    Then I wait for 1 contract
    And the expected contract TP is identical to "2_benefs_resilies" content

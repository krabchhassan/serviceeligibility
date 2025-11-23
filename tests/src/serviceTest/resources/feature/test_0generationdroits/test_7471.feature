Feature: Test génération droits v6 BLUE-7471

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @7471
  Scenario: 7471
    And I create a declarant from a file "declarantbaloo"
    Given I create a contract element from a file "gt7119"
    Given I create TP card parameters from file "parametrageTPBaloo_pilotageBO"
    And I send a test contract v6 from file "contratV6/7471-1"
    And I send a test contract v6 from file "contratV6/7471-2"
    Then I wait for 4 declarations
    And I send a test contract v6 from file "contratV6/7471-3"
    Then I wait for 6 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | periodeFermetureFin | finOnline |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-12-31 | null                | null      |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-12-31 | null                | null      |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-12-31 | null                | null      |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-12-31 | null                | null      |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | periodeFermetureFin    | finOnline              |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-05-21 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-05-21 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-05-21 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-05-21 |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-05-21 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-05-21 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-05-21 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-05-21 |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut                  | fin                    | periodeFermetureFin | finOnline              |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-12-31 | null                | %%CURRENT_YEAR%%-05-30 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-12-31 | null                | %%CURRENT_YEAR%%-05-30 |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-12-31 | null                | %%CURRENT_YEAR%%-05-30 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-12-31 | null                | %%CURRENT_YEAR%%-05-30 |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut                  | fin                    | periodeFermetureFin    | finOnline              |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-05-30 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-05-30 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-05-30 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-05-30 |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-05-30 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-05-30 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-05-30 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-05-30 |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie   | domaine | debut                  | fin                    | periodeFermetureFin | finOnline              |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-12-31 | null                | %%CURRENT_YEAR%%-05-30 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-12-31 | null                | %%CURRENT_YEAR%%-05-30 |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-12-31 | null                | %%CURRENT_YEAR%%-05-30 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-12-31 | null                | %%CURRENT_YEAR%%-05-30 |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie   | domaine | debut                  | fin                    | periodeFermetureFin    | finOnline              |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-05-30 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-05-30 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-05-30 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-05-30 |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-05-30 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-05-30 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-05-22 | %%CURRENT_YEAR%%-05-30 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-05-30 |
    Then I wait for 1 contract
    And the expected contract TP with indice 0 is identical to "contrattp-7471" content
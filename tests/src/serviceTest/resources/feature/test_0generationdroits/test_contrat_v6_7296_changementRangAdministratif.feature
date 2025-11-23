Feature: Test changement rang administratif

  Background:
    Given I create a declarant from a file "declarantbaloo"
    And I create a contract element from a file "gt7296"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSanté |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |

  @smokeTests
  Scenario: JIRA-7296 : changement rang administratif
    Given I create TP card parameters from file "parametrageBalooGenerique"
    When I send a contract from file "7296-Contrat1" to version "V6"
    And I wait for 3 declarations
    When I send a contract from file "7296-Contrat2" to version "V6"
    And I wait for 7 declarations
    When I send a contract from file "7296-Contrat3" to version "V6"
    And I wait for 11 declarations
    And I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-7296-1" content

  @smokeTests
  Scenario: JIRA-7296 : changement rang administratif cas n°2 avec sans effet assure du rangAdmin 11
    Given I create TP card parameters from file "parametrageBalooGenerique"
    When I send a contract from file "7296-2-Contrat1" to version "V6"
    Then I wait for 2 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And I send a contract from file "7296-2-Contrat2" to version "V6"
    Then I wait for 6 declarations
    # Déclaration assuré principal
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    # Déclaration rangAdmin 10 R
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut                  | fin                 | finOnline           |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
    # Déclaration rangAdmin 10 V
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-03 |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-03 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-03 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-03 |
    # Déclaration rangAdmin 11
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-12-31 | null      |
    Then I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-7296-2" content
    And I send a contract from file "7296-2-Contrat3" to version "V6"
    Then I wait for 9 declarations
    # Déclaration assuré principal
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 6
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    # Déclaration rangAdmin 11
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 7
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-06-03 | %%CURRENT_YEAR%%-06-03 |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-06-03 | %%CURRENT_YEAR%%-06-03 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-06-03 | %%CURRENT_YEAR%%-06-03 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-06-03 | %%CURRENT_YEAR%%-06-03 |
    # Déclaration rangAdmin 10
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 8
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-03 | %%CURRENT_YEAR%%-06-03 |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-03 | %%CURRENT_YEAR%%-06-03 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-03 | %%CURRENT_YEAR%%-06-03 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-03 | %%CURRENT_YEAR%%-06-03 |
    Then I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-7296-3SansEffetDu11" content

  @smokeTests
  Scenario: JIRA-7296 : changement rang administratif cas n°3 avec sans effet assure du rangAdmin 10
    Given I create TP card parameters from file "parametrageBalooGenerique"
    When I send a contract from file "7296-2-Contrat1" to version "V6"
    Then I wait for 2 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And I send a contract from file "7296-2-Contrat2" to version "V6"
    Then I wait for 6 declarations
    # Déclaration assuré principal
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    # Déclaration rangAdmin 10 R
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut                  | fin                 | finOnline           |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
    # Déclaration rangAdmin 10 V
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-03 |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-03 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-03 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-03 |
    # Déclaration rangAdmin 11
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-12-31 | null      |
    Then I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-7296-2" content
    And I send a contract from file "7296-2-Contrat3bis" to version "V6"
    Then I wait for 10 declarations
    # Déclaration assuré principal
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 6
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    # Déclaration rangAdmin 10 R
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 7
      | garantie   | domaine | debut                  | fin                 | finOnline           |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
    # Déclaration rangAdmin 10 R
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 8
      | garantie   | domaine | debut                  | fin                 | finOnline           |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
    # Déclaration rangAdmin 11
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 9
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | GT_5497_A1 | DENT    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | HOSP    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | AUDI    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_5497_A1 | PHAR    | %%CURRENT_YEAR%%-06-04 | %%CURRENT_YEAR%%-12-31 | null      |
    Then I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-7296-3SansEffetDu10" content
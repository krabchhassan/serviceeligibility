Feature: Test génération droits v6 BLUE-7119

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @radiationDansLePasse @7119 @release
  Scenario: BLUE-7119 création d'un contrat puis radiation d'un benef, puis changement periode carteTP puis radiation dans le passé
    Given I create a contract element from a file "gt7119"
    Given I create TP card parameters from file "parametrageTPBaloo_pilotageBO"
    Given I create a declarant from a file "declarantbaloo"
    When I send a test contract v6 from file "contratV6/7119-1"
    Then I wait for 2 declarations
    When I send a test contract v6 from file "contratV6/7119-2"
    Then I wait for 6 declarations
    When I send a test contract v6 from file "contratV6/7119-3"
    Then I wait for 9 declarations
    When I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "7119/contrat-1" content

    When I send a test contract v6 from file "contratV6/7119-4"
    Then I wait for 14 declarations
    When I wait "2" seconds in order to consume the data
    When I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "7119/contrat-2" content

    When I send a test contract v6 from file "contratV6/7119-5"
    Then I wait for 19 declarations
    When I wait "2" seconds in order to consume the data
    When I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "7119/contrat-3" content

  @smokeTests @radiationDansLePasse @7119
  Scenario: BLUE-7119 test 2
    Given I create a contract element from a file "gt7119"
    And I create a declarant from a file "declarantbaloo"
    And I create a service prestation from a file "7119-test2-1"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2023"
    Then I renew the rights today with mode "RDO"
    Then I wait for 1 declaration
    When I create TP card parameters from file "parametrageTPBaloo_pilotageBO"
    And I send a test contract v6 from file "contratV6/7119-test2-1"
    And I wait for 2 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | finOnline  |
      | GT_5497_A1 | HOSP    | 2023-01-01 | 2023-12-11 | 2023-12-11 |
      | GT_5497_A1 | AUDI    | 2023-01-01 | 2023-12-11 | 2023-12-11 |
      | GT_5497_A1 | DENT    | 2023-01-01 | 2023-12-11 | 2023-12-11 |
      | GT_5497_A1 | PHAR    | 2023-01-01 | 2023-12-11 | 2023-12-11 |
    And I send a test contract v6 from file "contratV6/7119-test2-2"
    And I wait for 4 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut      | fin        | finOnline  |
      | GT_5497_A1 | HOSP    | 2024-01-01 | 2024-12-11 | 2024-12-11 |
      | GT_5497_A1 | AUDI    | 2024-01-01 | 2024-12-11 | 2024-12-11 |
      | GT_5497_A1 | DENT    | 2024-01-01 | 2024-12-11 | 2024-12-11 |
      | GT_5497_A1 | PHAR    | 2024-01-01 | 2024-12-11 | 2024-12-11 |


  @smokeTests @radiationDansLePasse @7119
  Scenario: BLUE-7119 test 3
    Given I create a contract element from a file "gt7119"
    And I create a declarant from a file "declarantbaloo"
    And I create a service prestation from a file "7119-test3-1"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2023"
    Then I renew the rights today with mode "RDO"
    Then I wait for 1 declaration
    When I create TP card parameters from file "parametrageTPBaloo_pilotageBO"
    And I send a test contract v6 from file "contratV6/7119-test3-2"
    And I send a test contract v6 from file "contratV6/7119-test3-3"
    And I send a test contract v6 from file "contratV6/7119-test3-4"
    Then I wait for the first trigger with contract number "0082820S1" and amc "0000401166" to be "ProcessedWithWarnings"
    Then I send a test contract v6 from file "contratV6/7119-test3-5"
    Then I wait for 5 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut      | fin        | finOnline  |
      | GT_5497_A1 | HOSP    | 2023-01-01 | 2023-12-11 | 2023-12-11 |
      | GT_5497_A1 | AUDI    | 2023-01-01 | 2023-12-11 | 2023-12-11 |
      | GT_5497_A1 | DENT    | 2023-01-01 | 2023-12-11 | 2023-12-11 |
      | GT_5497_A1 | PHAR    | 2023-01-01 | 2023-12-11 | 2023-12-11 |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | periodeFermetureFin | finOnline  |
      | GT_5497_A1 | HOSP    | 2023-01-01 | 2023-12-11 | 2023-12-11          | 2023-12-11 |
      | GT_5497_A1 | AUDI    | 2023-01-01 | 2023-12-11 | 2023-12-11          | 2023-12-11 |
      | GT_5497_A1 | DENT    | 2023-01-01 | 2023-12-11 | 2023-12-11          | 2023-12-11 |
      | GT_5497_A1 | PHAR    | 2023-01-01 | 2023-12-11 | 2023-12-11          | 2023-12-11 |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | periodeFermetureFin | finOnline  |
      | GT_5497_A1 | HOSP    | 2023-01-01 | 2023-12-11 | 2023-12-11          | 2023-12-11 |
      | GT_5497_A1 | AUDI    | 2023-01-01 | 2023-12-11 | 2023-12-11          | 2023-12-11 |
      | GT_5497_A1 | DENT    | 2023-01-01 | 2023-12-11 | 2023-12-11          | 2023-12-11 |
      | GT_5497_A1 | PHAR    | 2023-01-01 | 2023-12-11 | 2023-12-11          | 2023-12-11 |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie   | domaine | debut      | fin        | finOnline  |
      | GT_5497_A1 | HOSP    | 2024-01-01 | 2024-12-11 | 2024-12-11 |
      | GT_5497_A1 | AUDI    | 2024-01-01 | 2024-12-11 | 2024-12-11 |
      | GT_5497_A1 | DENT    | 2024-01-01 | 2024-12-11 | 2024-12-11 |
      | GT_5497_A1 | PHAR    | 2024-01-01 | 2024-12-11 | 2024-12-11 |
    When I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "7119/contrat-4" content
#    Then there is 4 domaineDroits and the different domaineDroits has this values for benef 0
#      | code | debut      | fin        | typePeriode |
#      | AUDI | 2023/01/01 | 2023/12/11 | ONLINE      |
#      | AUDI | 2023/01/01 | 2023/12/11 | OFFLINE     |
#      | AUDI | 2024/01/01 | 2024/12/11 | ONLINE      |
#      | AUDI | 2024/01/01 | 2024/12/11 | OFFLINE     |
#      | DENT | 2023/01/01 | 2023/12/11 | ONLINE      |
#      | DENT | 2023/01/01 | 2023/12/11 | OFFLINE     |
#      | DENT | 2024/01/01 | 2024/12/11 | ONLINE      |
#      | DENT | 2024/01/01 | 2024/12/11 | OFFLINE     |
#      | HOSP | 2023/01/01 | 2023/12/11 | ONLINE      |
#      | HOSP | 2023/01/01 | 2023/12/11 | OFFLINE     |
#      | HOSP | 2024/01/01 | 2024/12/11 | ONLINE      |
#      | HOSP | 2024/01/01 | 2024/12/11 | OFFLINE     |
#      | PHAR | 2023/01/01 | 2023/12/11 | ONLINE      |
#      | PHAR | 2023/01/01 | 2023/12/11 | OFFLINE     |
#      | PHAR | 2024/01/01 | 2024/12/11 | ONLINE      |
#      | PHAR | 2024/01/01 | 2024/12/11 | OFFLINE     |


  @smokeTests @7119
  Scenario: 7119 Cas bizarre recyclage
    And I create a declarant from a file "declarantbaloo"
    Given I create a contract element from a file "gt7119"
    Given I create a contract element from a file "gt7119_bad_D1"
    And I send a test contract v6 from file "contratV6/7119-test4-1"
    And I create a service prestation from a file "7119-test4-2"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2025"
    Then I renew the rights today with mode "NO_RDO"
    Given I create a contract element from a file "gt7119_D1"
    And I wait for the first renewal trigger with contract number "0082820S1" and amc "0000401166" to be "ProcessedWithErrors"
    And I recycle the trigger
  # Cas bizarre apres le recyclage un des deux triggerBenef
  # est bien traite mais l autre est mis a l etat ToProcess sans etre traite.
  # Le trigger quand a lui est bien sauvegarder avec son etat final,
  # comme si au moment du recyclage on ne traitait qu un contrat sur les deux.
    And I wait for 1 declaration
    And I wait for 1 contract

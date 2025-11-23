Feature: Test création contrat envoyé fermé

  @redondantsmokeTests
  Scenario: Add a contract with radiation inside
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a contract element from a file "gtaxa_ok"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    When I send a test contract from file "contratV5/5833"

    Then I wait for 1 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-31 | %%CURRENT_YEAR%%-05-31 |
      | AXASCCGDIV | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-31 | %%CURRENT_YEAR%%-05-31 |

    Then I wait for 1 contract

    Then the expected contract TP with indice 0 is identical to "5833/contrat1" content




Feature: Periode carte manquante en pilotageBO

  Background:
    Given I create a contract element from a file "gtaxa"
    Given I create TP card parameters from file "parametrageTPBaloo_pilotageBO"
    Given I create a declarant from a file "declarantbaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @7322
  Scenario: 7322 periode carte manquante
    When I send a contract from file "contratV6/7322" to version "V6"
    Then I wait for the last trigger with contract number "5894-01" and amc "0000401166" to be "ProcessedWithWarnings"


  @smokeTests @7322
  Scenario: 7322 context tier payant manquant
    When I send a contract from file "contratV6/7322_no_context" to version "V6"
    Then I wait for the last trigger with contract number "5894-01" and amc "0000401166" to be "ProcessedWithWarnings"

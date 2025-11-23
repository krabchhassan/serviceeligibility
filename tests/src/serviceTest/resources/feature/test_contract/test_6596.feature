Feature: 6596

  @smokeTests
  Scenario: Cas 6596
    Given I create a contract element from a file "gtaxa_ok"
    And I create a contract element from a file "gtaxa"
    And I create a contract element from a file "gt_ignored"
    When I create a lot from a file "lot_6596"
    And I create TP card parameters from file "parametrageTP6596"
    And I create a declarant from a file "declarantbaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract v6 from file "6596"
    Then I wait for 1 declaration
    When I send a test contract v6 from file "6596"
    Then I wait for 2 declarations
    Then I wait for 1 contract

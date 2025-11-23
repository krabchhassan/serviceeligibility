Feature: 6602

  @smokeTests @6602 @caseConsolidation
  Scenario: Cas 6602
    Given I create a contract element from a file "gtbasebaloo6602"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    When I send a test contract from file "6602"
    Then I wait for 1 declaration
    When I send a test contract from file "6602-resil"
    Then I wait for 3 declarations
    Then I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-6602" content

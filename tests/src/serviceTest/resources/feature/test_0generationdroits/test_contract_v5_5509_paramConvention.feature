Feature: Test contract V5 avec la nouvelle gestion des conventions

  @smokeTests @NewParamConvention
  Scenario: I send a contract with conventions
    And I import the file "parametrageConventionnement" for parametrage
    And I create a contract element from a file "gtaxa_5458"
    And I create a declarant from a file "declarantbaloo_5458"
    And I create TP card parameters from file "parametrageTPBaloo_5458"
    When I send a test contract from file "contratV5/5458"
    When I get triggers with contract number "93000808" and amc "0000401166"
    Then I wait for 1 declarations
    Then The right number 0 has theses conventions
      | priorite | code |
      | 0        | IS   |
    Then The right number 1 has theses conventions
      | priorite | code |
      | 0        | IT   |
      | 1        | IS   |
    Then I wait for 1 contract

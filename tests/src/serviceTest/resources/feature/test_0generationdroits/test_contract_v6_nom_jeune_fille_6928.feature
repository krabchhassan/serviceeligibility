Feature: Nom de famille doit etre le nom de jeune fille

  @smokeTests @6928
  Scenario: Nom jeune fille bien retourne dans nomPatronumique carte demat
    Given I create a declarant from a file "declarantbalooEditable"
    And I create TP card parameters from file "parametrageTPBalooEditable"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a contract element from a file "gt10"
    And I send a test contract v6 from file "contratV6/6928"
    Then I wait for 1 declaration
    And The declaration has this values
      | nom             | PIPO  |
      | nomPatronymique | EUDES |
      | nomMarital      | PIPO  |
    Then I process declarations for carteDemat the "2025-01-01"
    And I wait for 1 card
    Then the card at index 0 has this values
      | nom             | PIPO  |
      | nomPatronymique | EUDES |
      | nomMarital      | PIPO  |

Feature: Force papier/demat by UI in declarations

  @smokeTests @forcePapier @forceDemat @release @sendContract
  Scenario: I send by UI
    And I import the file "parametrageConventionnement" for parametrage
    And I create a contract element from a file "gtaxaui"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    When I send a test contract from file "generation_droits_force_papier_demat/contrat"
    Then I wait for 1 declaration
    And The declaration has carteTPaEditerOuDigitale "0"
    When I create a trigger by UI from "trigger_by_ui_force_papier_demat"
    Then I wait for 2 declarations
    And The declaration with indice 1 has carteTPaEditerOuDigitale "3"
    When I create a trigger by UI from "trigger_by_ui_force_papier"
    Then I wait for 3 declarations
    And The declaration with indice 2 has carteTPaEditerOuDigitale "1"
    When I create a trigger by UI from "trigger_by_ui_force_demat"
    Then I wait for 4 declarations
    And The declaration with indice 3 has carteTPaEditerOuDigitale "2"
    # pour laisser finir le test
    Then I wait for 1 contract


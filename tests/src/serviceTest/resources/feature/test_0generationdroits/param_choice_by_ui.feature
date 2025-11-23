Feature: Choix du parametre lors d une generation par l ui

  @smokeTests @sendContract
  Scenario: I send by UI choix parametrage
    Given I create a contract element from a file "gtaxaui"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBalooPrio4000"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "generation_droits_force_papier_demat/contrat"
    Then I wait for 1 declaration
    And The declaration has carteTPaEditerOuDigitale "0"
    And I create TP card parameters from file "parametrageTPBalooPilotageBo"
    When I create a trigger by UI from "trigger_by_ui_force_papier_demat"
    Then I wait for the last trigger with contract number "MBA1476" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut               | Processed     |
      | nir                  | 2730781111166 |
      | numeroPersonne       | MBA14762      |
      | parametrageCarteTPId | prio4000      |
    # pour laisser finir le test
    Then I wait for 1 contract

Feature: Réception d'une image contrat avec plusieurs adresse sur 2025

  @smokeTests
  Scenario: Réception d'une image contrat avec plusieurs adresse sur 2025
    Given I create a contract element from a file "gt10"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract v6 from file "contratV6/6869"
    Then I wait for 1 declaration
    Then The declaration has this adresse
      | ligne1                | ligne4            | ligne6         | codePostal |
      | Mr EUDES Jean - DRP01 | 420 rue des Lilas | 31000 Toulouse | 31002      |

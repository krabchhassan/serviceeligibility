Feature:  Réception d'un contrat v6 sans context tier payant avec une parametrage PilotageBO

  @smokeTests @pilotageBO
  Scenario: Réception d'un contrat v6 sans context tiers payant avec un parametrage PilotageBO
    Given I create a contract element from a file "gt10"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo_pilotageBO"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract v6 from file "contratV6/6490_no_contexte"
    And I wait "2" seconds in order to consume the data
    Then I wait for the first trigger with contract number "6490" and amc "0000401166" to be "ProcessedWithWarnings"

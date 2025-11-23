Feature:  Réception d'une image contrat avec une résiliation antérieure à l'année courante

  @smokeTests
  Scenario: Réception d'une image contrat avec une résiliation antérieure à l'année courante
    Given I create a contract element from a file "gt10"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract v6 from file "contratV6/6205"
    When I get triggers with contract number "6205-01" and amc "0000401166"
    Then I get 1 trigger with contract number "6205-01" and amc "0000401166"
    Then the trigger of indice "0" has this values
      | status         | ProcessedWithWarnings |
      | amc            | 0000401166            |
      | nbBenef        | 1                     |
      | nbBenefKO      | 0                     |
      | nbBenefWarning | 1                     |

    When I send a test contract v6 from file "contratV6/6205-changeResil"
    When I get triggers with contract number "6205-01" and amc "0000401166"
    Then I get 2 trigger with contract number "6205-01" and amc "0000401166"
    Then the trigger of indice "1" has this values
      | status         | ProcessedWithWarnings |
      | amc            | 0000401166            |
      | nbBenef        | 1                     |
      | nbBenefKO      | 0                     |
      | nbBenefWarning | 1                     |
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut           | Warning                                              |
      | derniereAnomalie | Toutes les garanties sont annulées ou hors périmètre |
      | nir              | 1800692014015                                        |
      | numeroPersonne   | 1234567896205-01                                     |

    Then I wait for 0 declarations

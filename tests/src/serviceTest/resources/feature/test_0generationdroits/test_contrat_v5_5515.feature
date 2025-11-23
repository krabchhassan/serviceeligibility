Feature: Test renewal with batch

  @smokeTests @caseConsolidation @caseRenouvellement

  Scenario: I select the right TP card parameter
    Given I create a contract element from a file "gtbaloo5515"
    And I create a contract element from a file "gtbaloo5515-1"
    And I create a contract element from a file "gt10"
    And I create a contract element from a file "gt20"
    And I create a declarant from a file "declarantbaloo"
    And I create an automatic TP card parameters from file "parametrageBalooGenerique"
    And I create an automatic TP card parameters from file "paramTPBaloo5515-1"
    And I create an automatic TP card parameters from file "paramTPBaloo5515-2"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I create a service prestation from a file "5515-Contrat1"
    When I create a service prestation from a file "5515-Contrat2"
    When I create a service prestation from a file "5515-Contrat3"
    When I create a service prestation from a file "5515-Contrat4"
    When I create a lot from a file "lot_123"

    When I renew the rights today
    When I wait "3" seconds in order to consume the data

    Then I wait for the last renewal trigger with contract number "5515-01" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut               | Processed        |
      | nir                  | 1800531001001    |
      | numeroPersonne       | 1234567895515-01 |
      | parametrageCarteTPId | idAvecLot123     |

    Then I wait for the last renewal trigger with contract number "5515-02" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut               | Processed             |
      | nir                  | 1800531001001         |
      | numeroPersonne       | 1234567895515-02      |
      | parametrageCarteTPId | idAvecLot123EtGT_BLO1 |

    Then I wait for the last renewal trigger with contract number "5515-03" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger with the index "1"
    Then the triggerBenef has this values
      | statut               | Processed             |
      | nir                  | 1800531001001         |
      | numeroPersonne       | 1234567895515-03      |
      | parametrageCarteTPId | idAvecLot123EtGT_BLO1 |

    Then I wait for the last renewal trigger with contract number "5515-04" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut               | Processed        |
      | nir                  | 1800531001001    |
      | numeroPersonne       | 1234567895515-04 |
      | parametrageCarteTPId | GENERIQUE        |

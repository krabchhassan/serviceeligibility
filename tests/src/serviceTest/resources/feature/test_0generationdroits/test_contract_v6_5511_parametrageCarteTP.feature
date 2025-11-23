Feature: Test contract V6 with lot et gt

  Background:
    Given I create a contract element from a file "gtFSBC49567A"
    And I create a contract element from a file "gtFSOC49568A"
    And I create a contract element from a file "gtFSBC48516A"
    And I create a contract element from a file "gtFSOC49568C"
    And I create a contract element from a file "gtFSBC49567C"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @caselot
  Scenario: I send a contract to test which TP Card parameters is good
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageBaloo5511-0"
    And I create TP card parameters from file "parametrageBaloo5511-1"
    And I create TP card parameters from file "parametrageBalooGenerique"
    When I create a lot from a file "lot_ASSU00060"
    When I create a lot from a file "lot_ASSU00187"
    When I send a test contract from file "contratV5/5511"
    When I get triggers with contract number "1086171S1" and amc "0000401166"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut               | Error  |
      | numeroPersonne       | 493908 |
      | nir                  | null   |
      | parametrageCarteTPId | 2      |

    Given I delete trigger and sas
    When I send a test contract from file "contratV5/5511-1"
    When I get triggers with contract number "1086171S2" and amc "0000401166"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut               | Error  |
      | numeroPersonne       | 493908 |
      | nir                  | null   |
      | parametrageCarteTPId | 2      |

    When I send a test contract from file "contratV5/5511-2"
    When I get triggers with contract number "1086171S3" and amc "0000401166"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut               | Error     |
      | numeroPersonne       | 493908    |
      | nir                  | null      |
      | parametrageCarteTPId | GENERIQUE |


  @smokeTests @caselot
  Scenario: I send a contract to test which TP Card parameters is good : GENERIQUE
    Given I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageBaloo5511-0"
    And I create TP card parameters from file "parametrageBaloo5511-1"
    And I create TP card parameters from file "parametrageBalooGenerique"
    And I create TP card parameters from file "parametrageBaloo2LotsNoGt"
    When I create a lot from a file "lot_ASSU00060"
    When I create a lot from a file "lot_ASSU00187"
    When I send a test contract from file "contratV5/5511-3"
    When I get triggers with contract number "1086171S1" and amc "0000401166"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut               | Error     |
      | numeroPersonne       | 493908    |
      | nir                  | null      |
      | parametrageCarteTPId | GENERIQUE |

  @smokeTests @caselot
  Scenario: I send a contract to test which TP Card parameters is good : 2
    Given I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageBaloo5511-0"
    And I create TP card parameters from file "parametrageBaloo5511-1"
    And I create TP card parameters from file "parametrageBalooGenerique"
    And I create TP card parameters from file "parametrageBaloo5511-2"
    When I create a lot from a file "lot_ASSU00060"
    When I create a lot from a file "lot_ASSU00187"
    When I send a test contract from file "contratV5/5511-5"
    When I get triggers with contract number "1086171AA" and amc "0000401166"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut               | Error  |
      | numeroPersonne       | 493908 |
      | nir                  | null   |
      | parametrageCarteTPId | 2      |

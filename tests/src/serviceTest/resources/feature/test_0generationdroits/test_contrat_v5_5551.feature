Feature: Test renewal with batch

  @smokeTests @caseConsolidation @caseRenouvellement

  Scenario: JIRA-5551
    # LOT 123
    Given I create a contract element from a file "gt10"
    And I create a contract element from a file "gt20"
    # LOT 456
    And I create a contract element from a file "bt10"
    And I create a contract element from a file "bt20"

    And I create a contract element from a file "br10"

    When I create a lot from a file "lot_123"
    When I create a lot from a file "lot_456"
    And I create a declarant from a file "declarantbaloo"
    And I create an automatic TP card parameters from file "parametrageBalooGenerique"
    And I create an automatic TP card parameters from file "paramTPBaloo5551-Lot123"
    And I create an automatic TP card parameters from file "paramTPBaloo5551-Lot456"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I create a service prestation from a file "5551-Contrat1"
    When I create a service prestation from a file "5551-Contrat2"
    When I create a service prestation from a file "5551-Contrat3"
    When I create a service prestation from a file "5551-Contrat4"

    When I renew the rights today
    When I wait "3" seconds in order to consume the data

    # cas du contrat 1, le souscripteur n'a pas les droits, il n'y a que le 2nd assuré qui remonte sur le param avec lot 123.
    Then I wait for the last renewal trigger with contract number "5551-01" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut               | Processed         |
      | nir                  | 1900531001001     |
      | numeroPersonne       | 1234567895551-02  |
      | parametrageCarteTPId | paramIdAvecLot123 |


    # cas du contrat 2, le souscripteur a les droits sur le param avec lot 456, le 2nd assuré sur le param avec lot 123.
    Then I wait for the last renewal trigger with contract number "5551-02" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut               | Processed         |
      | nir                  | 1800531001002     |
      | numeroPersonne       | PERS-01           |
      | parametrageCarteTPId | paramIdAvecLot456 |

    When I get the triggerBenef on the trigger with the index "1"
    Then the triggerBenef has this values
      | statut               | Processed         |
      | nir                  | 1900531001001     |
      | numeroPersonne       | PERS-02           |
      | parametrageCarteTPId | paramIdAvecLot123 |

    # cas du contrat 3, Les 2 sont sur le 456, il n'y a pas de souscripteur mais le rang administrateur le plus c'est PERS-302
    Then I wait for the last renewal trigger with contract number "5551-03" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger with the index "2"
    Then the triggerBenef has this values
      | statut               | Processed         |
      | nir                  | 1800531001003     |
      | numeroPersonne       | PERS-301          |
      | parametrageCarteTPId | paramIdAvecLot456 |

    When I get the triggerBenef on the trigger with the index "3"
    Then the triggerBenef has this values
      | statut               | Processed         |
      | nir                  | 1900531001003     |
      | numeroPersonne       | PERS-302          |
      | parametrageCarteTPId | paramIdAvecLot456 |


    # cas du contrat 4, un est sur le 456 et l'autre sur aucun lot (GENERIQUE)
    Then I wait for the last renewal trigger with contract number "5551-04" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut               | Processed     |
      | nir                  | 1800531001004 |
      | numeroPersonne       | PERS-401      |
      | parametrageCarteTPId | GENERIQUE     |

    When I get the triggerBenef on the trigger with the index "1"
    Then the triggerBenef has this values
      | statut               | Processed         |
      | nir                  | 1900531001004     |
      | numeroPersonne       | PERS-402          |
      | parametrageCarteTPId | paramIdAvecLot456 |

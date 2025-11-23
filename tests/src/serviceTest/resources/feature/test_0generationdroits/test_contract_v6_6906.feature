Feature: creation trigger quand reception contrat sans parametrage carte TP

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSanté |

  @smokeTests
  Scenario: I send a contract with missing parametrageCarteTP, add it then recycle
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I delete the contract histo for this contract 01599324
    When I send a test contract from file "contratV6/6906"
    # pas de paramétrage carte tp
    When I wait for 0 declaration

    And I create manual TP card parameters from file "parametrageCarteTPAutomatique6906"

    Then I get one more trigger with contract number "01599324" and amc "0000401166" and indice "0" for benef
    And I recycle the trigger

    When I wait for 1 declaration


  @smokeTests
  Scenario: I send a contract with missing parametrageCarteTP, recycle and get same anomalies
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    When I send a test contract from file "contratV6/6906_2_benefs"
    Then I wait for 0 declaration
    When I wait for the last trigger with contract number "5894-01" and amc "0000401166" to be "ProcessedWithErrors"
    Then I get the triggerBenef on the trigger with the index "0"
    And the triggerBenef has this values
      | statut           | Error                                |
      | derniereAnomalie | Aucun paramétrage de carte TP trouvé |
      | nir              | 1800692014015                        |
      | numeroPersonne   | 1234567895894-01                     |
    And I get the triggerBenef on the trigger with the index "1"
    And the triggerBenef has this values
      | statut           | Error                      |
      | derniereAnomalie | Sas trouvé pour ce contrat |
      | nir              | 1000611111111              |
      | numeroPersonne   | 1234567895894-02           |

    When I recycle the trigger
    Then I wait for 0 declaration
    # Should have same anomaly
    When I wait for the last trigger with contract number "5894-01" and amc "0000401166" to be "ProcessedWithErrors"
    Then I get the triggerBenef on the trigger with the index "0"
    And the triggerBenef has this values
      | statut           | Error                                |
      | derniereAnomalie | Aucun paramétrage de carte TP trouvé |
      | nir              | 1800692014015                        |
      | numeroPersonne   | 1234567895894-01                     |
    And I get the triggerBenef on the trigger with the index "1"
    And the triggerBenef has this values
      | statut           | Error                      |
      | derniereAnomalie | Sas trouvé pour ce contrat |
      | nir              | 1000611111111              |
      | numeroPersonne   | 1234567895894-02           |


  @smokeTests
  Scenario: I send a contract without contextTierPayant and BO paramter, then add lower priority automatic parameter and recycle
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP card parameters from file "parametrageCarteTPBO6906"
    When I send a test contract from file "contratV6/6906_2_benefs"
    Then I wait for 0 declaration

    When I wait for the last trigger with contract number "5894-01" and amc "0000401166" to be "ProcessedWithWarnings"
    Then I get the triggerBenef on the trigger with the index "0"
    And the triggerBenef has this values
      | statut           | Warning                                               |
      | derniereAnomalie | Période d'édition des cartes manquante sur le contrat |
      | nir              | 1800692014015                                         |
      | numeroPersonne   | 1234567895894-01                                      |
    And I get the triggerBenef on the trigger with the index "1"
    And the triggerBenef has this values
      | statut           | Warning                                               |
      | derniereAnomalie | Période d'édition des cartes manquante sur le contrat |
      | nir              | 1000611111111                                         |
      | numeroPersonne   | 1234567895894-02                                      |

    # Lower priority and automatic, should generate declaration without contextTierPayant
    And I create manual TP card parameters from file "parametrageCarteTPAutomatique6906"
    When I send a test contract from file "contratV6/6906_2_benefs"
    Then I wait for 2 declarations

Feature: Test contract V5 with  consolidation

  @smokeTests @caseConsolidation
  Scenario: I send a contract with consolidation in 2023 : TP rights with no resiliation date
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2021"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "generationDroitsTP_4943/consolidation_sur_plusieurs_annees/consolidation"
    When I get 1 trigger with contract number "MBA1077" and amc "0000401166"

    Then I wait for the first trigger with contract number "MBA1077" and amc "0000401166" to be "ProcessedWithErrors"
    Given I abandon the trigger
    When I renew the rights today with mode "RDO"
    When I wait "3" seconds in order to consume the data
#
    Then I wait for the last renewal trigger with contract number "MBA1077" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 2730781111166 |
      | numeroPersonne | MBA10772      |

    Then I wait for 1 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIM | OPAU    | 2021-01-01 | 2021-12-31 | null      |
      | AXASCCGDIM | HOSP    | 2021-01-01 | 2021-12-31 | null      |

    When I remove all TP card parameters from database
    And I create manual TP card parameters from file "parametrageCarteTPManuel2022"
    And I renew the rights today with mode "RDO"
    When I wait "1" seconds in order to consume the data
    Then I wait for the last renewal trigger with contract number "MBA1077" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 2730781111166 |
      | numeroPersonne | MBA10772      |

    Then I wait for 2 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIM | OPAU    | 2022-01-01 | 2022-12-31 | null      |
      | AXASCCGDIM | HOSP    | 2022-01-01 | 2022-12-31 | null      |

    When I remove all TP card parameters from database
    And I create manual TP card parameters from file "parametrageCarteTPManuel2023"
    And I renew the rights today with mode "RDO"
    When I wait "1" seconds in order to consume the data
    Then I wait for the last renewal trigger with contract number "MBA1077" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 2730781111166 |
      | numeroPersonne | MBA10772      |

    Then I wait for 3 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-12-31 | null      |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-12-31 | null      |
    And I wait for 1 contract


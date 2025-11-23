Feature: Search contract PAU V5 BLUE-4838

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Get PAU V5 TP_OFFLINE almost like TP_ONLINE
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/cas4-contratEvent"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauCas4"
    When I get contrat PAUV5 for 1591275115460 '19700101' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | number | C00040 |

    When I get contrat PAUV5 for 1591275115460 '19700201' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | number | C00040 |

    When I get contrat PAUV5 for 1591275115460 '19700201' 2 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | number | C00040 |

    When I get contrat PAUV5 for 1591275115460 '19700301' 2 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then I have a beneficiary without subscriber not found exception

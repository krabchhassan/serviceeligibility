Feature: Search contract PAU V5 BLUE-4839

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Get PAU V5 TP_OFFLINE almost like TP_ONLINE
    Given I create a contrat from file "contractForPauV5/cas5-contratEvent1"
    And I create a contrat from file "contractForPauV5/cas5-contratEvent2"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauCas5"
    When I get contrat PAUV5 for 1591275115460 '19700101' 1 '2023-12-01' '2023-12-01' 0000401166 TP_OFFLINE for domains MEDG
    Then the contract number 0 data has values
      | number | A00082 |

    When I get contrat PAUV5 for 1591275115460 '19700101' 1 '2023-01-02' '2023-01-02' 0000401166 TP_OFFLINE for domains OPTI
    Then the contract number 0 data has values
      | number | A00081 |

    # le 2 est le plus proche au niveau de la recherche adhérent
    When I get contrat PAUV5 for 1591275115460 '19700101' 1 '2023-04-02' '2023-04-02' 0000401166 TP_OFFLINE for domains PHAR
    Then the contract number 0 data has values
      | number | A00082 |

    When I get contrat PAUV5 for 1591275115461 '19700101' 1 '2023-01-02' '2023-01-02' 0000401166 TP_OFFLINE A00082
    Then With this request I have a contract resiliated exception

    When I get contrat PAUV5 for 1591275115461 '19700101' 1 '2023-02-02' '2023-02-02' 0000401166 TP_OFFLINE
    Then I have a beneficiary without subscriber not found exception

    When I get contrat PAUV5 for 1591275115461 '19700101' 1 '2023-02-02' '2023-02-02' 0000401166 TP_OFFLINE for domains MEDG
    Then I have a beneficiary without subscriber not found exception

    When I get contrat PAUV5 for 1591275115461 '19700101' 1 '2023-02-02' '2023-02-02' 0000401166 TP_OFFLINE A00082 for domains MEDG
    Then With this request I have a contract without any rights open exception

    When I get contrat PAUV5 for 1591275115461 '19700101' 1 '2023-02-02' '2023-02-02' 0000401166 TP_OFFLINE A00082 for domains OPTI
    Then With this request I have a contract without any rights open for this domain exception

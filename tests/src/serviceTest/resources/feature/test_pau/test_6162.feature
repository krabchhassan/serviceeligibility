Feature: Search contract PAU V5 BLUE-6162

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Get PAU V5 with benef with 4 contracts
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a declarant from a file "declarantBalooTranscoDomaine"
    And I create a contrat from file "contractForPauV5/contrat1-6162"
    And I create a contrat from file "contractForPauV5/contrat2-6162"
    And I create a contrat from file "contractForPauV5/contrat3-6162"
    And I create a contrat from file "contractForPauV5/contrat4-6162"
    And I create a beneficiaire from file "contractForPauV5/benef-6162"

    When I get contrat PAUV5 for 1720899353180 '20221201' 1 '2024-07-02' '2024-07-02' 0000401166 TP_OFFLINE for domains PHAR
    Then the contract number 0 data has values
      | number | 0255256 |

    When I get contrat PAUV5 for 1720899353180 '20221201' 1 '2024-07-02' '2024-07-02' 0000401166 TP_OFFLINE for domains OPAU
    Then the contract number 0 data has values
      | number | 0255256 |

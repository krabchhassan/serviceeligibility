Feature: Search contract PAU V5 BLUE-6920

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Get PAU V5 without end date
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/6920/contrat1_benef1"
    And I create a contrat from file "contractForPauV5/6920/contrat2_benef2"
    And I create a beneficiaire from file "contractForPauV5/6920/benef1_nir1"
    And I create a beneficiaire from file "contractForPauV5/6920/benef2_nir1"
    # EndDate inside farthest contract D00012
    When I get contrat PAUV5 without endDate for 1591275115460 '19621023' 1 '2022-12-12' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | number | D00012 |

    # EndDate outside of both contract. Should return the closest startDate contract D00011
    When I get contrat PAUV5 without endDate for 1591275115460 '19621023' 1 '2022-12-20' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | number | D00011 |

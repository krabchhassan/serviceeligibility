Feature: Search contract PAU V5 restit nature by context BLUE-5049

  @smokeTests @pauv5
  Scenario: Get contract PAUV5, not same natureOffline but same natureOnline
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/resistNatureContext/cas1-contratEvent"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00012"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2023-03-15' '2023-04-15' 0000401166 TP_ONLINE for beneficiaryId P00012
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | Pharmacie  |
      | start       | 2023-03-15 |
      | end         | 2023-04-15 |
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2023-03-15' '2023-04-15' 0000401166 TP_OFFLINE for beneficiaryId P00012
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | PHARMACICAL |
      | start       | 2023-03-15  |
      | end         | 2023-03-31  |
    Then the benfitType 1 of product 0 of right 0 data has values
      | benefitType | PHARMACIE  |
      | start       | 2023-04-01 |
      | end         | 2023-04-15 |

  @smokeTests @pauv5
  Scenario: Get contract PAUV5, not same natureOffline, same nature for online
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/resistNatureContext/cas2-contratEvent"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00012"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2023-03-15' '2023-04-15' 0000401166 TP_ONLINE for beneficiaryId P00012
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | Pharmacie  |
      | start       | 2023-03-15 |
      | end         | 2023-04-15 |
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2023-03-15' '2023-04-15' 0000401166 TP_OFFLINE for beneficiaryId P00012
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | PHARMACICAL |
      | start       | 2023-03-15  |
      | end         | 2023-03-20  |
    Then the benfitType 1 of product 0 of right 0 data has values
      | benefitType | Pharmacie  |
      | start       | 2023-03-21 |
      | end         | 2023-04-15 |

  @smokeTests @pauv5
  Scenario: Get contract PAUV5, same natureOffline, same natureOnline but no overlapping dates
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/resistNatureContext/cas3-contratEvent"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00012"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2023-03-15' '2023-04-15' 0000401166 TP_ONLINE for beneficiaryId P00012
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | Pharmacie  |
      | start       | 2023-03-15 |
      | end         | 2023-03-20 |
    Then the benfitType 1 of product 0 of right 0 data has values
      | benefitType | Pharmacie  |
      | start       | 2023-03-30 |
      | end         | 2023-04-15 |
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2023-03-15' '2023-04-15' 0000401166 TP_OFFLINE for beneficiaryId P00012
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | PHARMACIE  |
      | start       | 2023-03-30 |
      | end         | 2023-04-15 |
    Then the benfitType 1 of product 0 of right 0 data has values
      | benefitType | PHARMACIE  |
      | start       | 2023-03-15 |
      | end         | 2023-03-20 |

  @smokeTests @pauv5
  Scenario: Get contract PAUV5, offline end before online
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/resistNatureContext/cas4-contratEvent"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00012"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2023-07-01' '2023-07-15' 0000401166 TP_ONLINE for beneficiaryId P00012
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | Clinique   |
      | start       | 2023-07-01 |
      | end         | 2023-07-15 |
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2023-07-01' '2023-07-15' 0000401166 TP_OFFLINE for beneficiaryId P00012
    Then With this request I have a contract resiliated exception

  @smokeTests @pauv5
  Scenario: Get contract PAUV5, offline present but not online. Same result for both online and offline context
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/resistNatureContext/cas5-contratEvent"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00012"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2023-07-01' '2023-07-15' 0000401166 TP_ONLINE for beneficiaryId P00012
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | HOSPITALISATION |
      | start       | 2023-07-01      |
      | end         | 2023-07-15      |
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2023-07-01' '2023-07-15' 0000401166 TP_OFFLINE for beneficiaryId P00012
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | HOSPITALISATION |
      | start       | 2023-07-01      |
      | end         | 2023-07-15      |

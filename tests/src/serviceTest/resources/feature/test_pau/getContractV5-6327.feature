Feature: Search contract PAU V5 BLUE-6327

  @smokeTests @pauv5 @6237
  Scenario: Get PAU V5 without end date and without end date for product
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat6327"
    And I create a beneficiaire from file "contractForPauV5/benef6327"
    When I get contrat PAUV5 without endDate for 1701062498046 '19700603' 1 '2024-09-11' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | number | 0123456 |
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | null       |
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | null       |
    Then the benfitType 0 of product 0 of right 1 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | null       |
    Then the contract and the right 1 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | null       |

  @smokeTests @pauv5 @6237
  Scenario: Get PAU V5 without end date and with resiliation date for product period then get pau with end date
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat6327-resiliation"
    And I create a beneficiaire from file "contractForPauV5/benef6327"
    When I get contrat PAUV5 without endDate for 1701062498046 '19700603' 1 '2024-09-11' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | number | 0123456 |
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | 2024-09-15 |
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | 2024-09-15 |
    Then the benfitType 0 of product 0 of right 1 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | 2024-09-15 |
    Then the contract and the right 1 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | 2024-09-15 |
    # Get PAU with end date
    When I get contrat PAUV5 for 1701062498046 '19700603' 1 '2024-09-11' '2024-09-18' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | number | 0123456 |
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | 2024-09-15 |
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | 2024-09-15 |
    Then the benfitType 0 of product 0 of right 1 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | 2024-09-15 |
    Then the contract and the right 1 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | 2024-09-15 |

  @smokeTests @pauv5 @6237
  Scenario: Get PAU V5 without end date with nominal contract then get pau with end date
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat6327-nominal"
    And I create a beneficiaire from file "contractForPauV5/benef6327"
    When I get contrat PAUV5 without endDate for 1701062498046 '19700603' 1 '2024-09-11' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | number | 0123456 |
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | null       |
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | null       |
    Then the benfitType 0 of product 0 of right 1 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | null       |
    Then the contract and the right 1 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | null       |
    # Get PAU with end date
    When I get contrat PAUV5 for 1701062498046 '19700603' 1 '2024-09-11' '2024-09-18' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | number | 0123456 |
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | 2024-09-18 |
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | 2024-09-18 |
    Then the benfitType 0 of product 0 of right 1 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | 2024-09-18 |
    Then the contract and the right 1 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | 2024-09-18 |

  @smokeTests @pauv5 @6237
  Scenario: Get PAU V5 without end date with resiliation date in 2024 and request startDate in 2022
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat6327-resiliation"
    And I create a beneficiaire from file "contractForPauV5/benef6327"
    When I get contrat PAUV5 without endDate for 1701062498046 '19700603' 1 '2022-09-11' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | number | 0123456 |
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2022-09-11 |
      | end         | 2024-09-15 |
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2022-09-11 |
      | fin   | 2024-09-15 |
    Then the benfitType 0 of product 0 of right 1 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2022-09-11 |
      | end         | 2024-09-15 |
    Then the contract and the right 1 data has this period on the product 0
      | debut | 2022-09-11 |
      | fin   | 2024-09-15 |

  @smokeTests @pauv5
  Scenario: Get PAU V5 without end date with resiliation then with end date
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat6327-simpleWithResil"
    And I create a beneficiaire from file "contractForPauV5/benef6327"
    When I get contrat PAUV5 without endDate for 1701062498046 '19700603' 1 '2024-09-11' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | number | 0123456 |
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | 2024-09-15 |
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | 2024-09-15 |
    Then the benfitType 0 of product 0 of right 1 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | 2024-09-15 |
    Then the contract and the right 1 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | 2024-09-15 |
    # Get PAU with end date and have same product periods
    When I get contrat PAUV5 for 1701062498046 '19700603' 1 '2024-09-11' '2024-09-18' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | number | 0123456 |
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | 2024-09-15 |
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | 2024-09-15 |
    Then the benfitType 0 of product 0 of right 1 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2024-09-11 |
      | end         | 2024-09-15 |
    Then the contract and the right 1 data has this period on the product 0
      | debut | 2024-09-11 |
      | fin   | 2024-09-15 |

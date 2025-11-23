Feature: Search contract PAU V5 BLUE-6700

  @smokeTests @pauv5
  Scenario: Get PAU V5 without end date and without end date for product
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat6700"
    And I create a beneficiaire from file "contractForPauV5/benef6700"
    When I get contrat PAUV5 without endDate for 1701062498046 '19700603' 1 '2025-01-01' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | number | 0123456 |
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | null       |
    Then the benfitType 1 of product 0 of right 0 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2025-01-01 |
      | end         | 2025-01-02 |
    Then the benfitType 0 of product 0 of right 0 data has values
      | benefitType | BIOLOGIE   |
      | start       | 2025-01-03 |
      | end         | null       |

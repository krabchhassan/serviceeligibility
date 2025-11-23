Feature: Search contract PAU V5 HTP BLUE-6448

  @smokeTests @smokeTestsWithoutKafka @pauv5 @6448 @release
  Scenario: Get PAU V5 HTP
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a service prestation from a file "servicePrestation-6448"
    And I create a contract element from a file "gt_6448"
    And I create a beneficiaire from file "contractForPauV5/benef6448"
    When I get contrat PAUV5 without endDate for 2646523001247 '19991013' 1 '2024-06-15' 0000401166 HTP
    Then the contract number 0 data has values
      | number | 1541325 |
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2024-07-03 |
      | fin   | null       |
    Then the contract and the right 0 data has this period on the product 1
      | debut | 2024-06-15 |
      | fin   | 2024-07-02 |

  @smokeTests @smokeTestsWithoutKafka @pauv5 @6448
  Scenario: Get PAU V5 HTP with endDate
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a service prestation from a file "servicePrestation-6448"
    And I create a contract element from a file "gt_6448"
    And I create a beneficiaire from file "contractForPauV5/benef6448"
    When I get contrat PAUV5 for 2646523001247 '19991013' 1 '2024-06-15' '2024-07-20' 0000401166 HTP
    Then the contract number 0 data has values
      | number | 1541325 |
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2024-07-03 |
      | fin   | 2024-07-20 |
    Then the contract and the right 0 data has this period on the product 1
      | debut | 2024-06-15 |
      | fin   | 2024-07-02 |

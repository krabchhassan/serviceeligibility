Feature: Search contract PAU V5 HTP BLUE-6536

  @smokeTests @smokeTestsWithoutKafka @pauv5 @6536
  Scenario: Get PAU V5 HTP with 2 offers within the period
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a service prestation from a file "servicePrestation6536"
    And I create a contract element from a file "gtbaloo6536"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire-6536"
    When I get contrat PAUV5 without endDate for 1806201254854 '19751211' 1 '2023-03-29' 0000401166 HTP
    Then the contract number 0 data has values
      | number | 1105866 |
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2023-03-29 |
      | fin   | null       |


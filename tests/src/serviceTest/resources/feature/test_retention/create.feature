Feature: Create a retention

  Background:
    Given I create a contract element from a file "gtdnt"
    Given I create a declarant from a file "declarantTestBlue"

  @smokeTests @smokeTestsWithoutKafka @retention
  Scenario: Send an active contract with continuous periods and verify that the retention is not created
    When I send a test contract v6 from file "retention/contract1"
    When I send a test contract v6 from file "retention/contract2"
    Then I wait for 0 retention

  @smokeTests @smokeTestsWithoutKafka @retention
  Scenario: Send a non active contract and verify that the retention is not created
    When I send a test contract v6 from file "retention/contract1"
    When I send a test contract v6 from file "retention/contract4"
    Then I wait for 0 retention

  @smokeTests @smokeTestsWithoutKafka @retention @release
  Scenario: Send a resiliated contract and verify that the retention is created
    When I send a test contract v6 from file "retention/contract1"
    When I send a test contract v6 from file "retention/contract5"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate | 2025-02-11 |
      | status         | TO_PROCESS |
    When I send a test contract v6 from file "retention/contract1"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate | null       |
      | status         | CANCELLED  |

  @smokeTests @smokeTestsWithoutKafka @retention
  Scenario: Send an active contract with radiated insured and verify that the retention is created
    When I send a test contract v6 from file "retention/contract1"
    When I send a test contract v6 from file "retention/contract6"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate | 2025-01-15 |
      | status         | TO_PROCESS |
    When I send a test contract v6 from file "retention/contract1"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate | null       |
      | status         | CANCELLED  |

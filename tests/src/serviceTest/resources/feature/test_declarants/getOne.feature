Feature: Get one declaration

  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: Search declarant that doesn't exist
    When I try to get a declarant for UI with ID "BAD_ID"
    Then an error "404" is returned

  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: Search declarant that
    And I create a declarant from a file "declarantAon"
    When I get a declarant for UI with ID "0000401182"
    Then the declarant for UI with id "0000401182" exists

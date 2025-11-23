Feature: Create a declaration

  @todosmokeTests @smokeTestsWithoutKafka @card
  Scenario: Create a card
    # we use nominalCard.json and replace parameters with below table
    When I create a card with parameters
      | codeEtat | XXX |
    Then the response has an HTTP code "201"
    Then I get all cards
    Then 1 card is returned



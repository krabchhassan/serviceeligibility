Feature: Get card

  @todosmokeTests @smokeTestsWithoutKafka @card
  Scenario: Get card by ID
    Given I create a card with parameters
      | _id | 123 |
    Then I get card with id "123"
    Then the card with id "123" exists

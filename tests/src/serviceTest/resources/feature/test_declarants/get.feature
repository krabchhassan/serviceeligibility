Feature: Get one declaration

  Background:
    Given I import a complete file for parametrage

  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: Get declarant by ID
    # there is a declarant with ID 0000733931
    Then I get declarant with id "123"
    Then the declarant with id "123" does not exist
    Then I get declarant with id "0000733931"
    Then the declarant with id "0000733931" exists


  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: Search Declarant by amc number
    When I search declarant with values
      | numero | 0000733931 |
    Then the declarant searched result contains 1 results
    Then I search declarant with values
      | numero | 0000733930 |
    When the declarant searched result contains 0 results

  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: Search Declarant by amc name
    When I search declarant with values
      | nom | isante |
    Then the declarant searched result contains 1 results
    When I search declarant with values
      | nom | bad_id |
    Then the declarant searched result contains 0 results

  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: Search Declarant by service and couloir
    When I search declarant with values
      | service | TPG-IS |
      | couloir | MAI    |
    Then the declarant searched result contains 1 results
    When I search declarant with values
      | service | NO_SERVICE |
      | couloir | MAI        |
    Then the declarant searched result contains 0 results

  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: Search Declarant by non valid parameter
    When I try to search declarant with values
      | par | TPG-IS |
    Then an error "400" is returned

Feature: Import data

  @@todosmokeTests @smokeTestsWithoutKafka @import @importData
  Scenario: Import data
    Given I create a declarant from a file "declarantTest"
    When I import a complete file
    Then I wait for 11 declarations
    When I wait for 1 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | processus    | Aiguillage |
      | numberByPage | 10         |
    Then 2 flux are returned
    When I get all volumetrie data
    Then 2 volumetrie entities are returned
    Then the volumetry data structure fetched is correct


  @@todosmokeTests @smokeTestsWithoutKafka @import @importData
  Scenario: Import parametrage data
    Given I create a declarant from a file "declarantTest"
    When I get all entities circuits
    Then 0 entities circuits are returned
    Given I import a complete file for parametrage
    When I get all entities circuits
    Then 4 entities circuits are returned
    Then the entities circuits search result is correct
    When I get all entities services
    Then 9 entities services are returned
    Then the entities services search result is correct
    When I get all entities processes
    Then 9 entities processes are returned
    Then the entities processes search result is correct
    When I get all entities filetypes
    Then 11 entities filetypes are returned
    Then the entities processes search result is correct
    When I get all entities conventions
    Then 24 entities conventions are returned
    Then the entities processes search result is correct
    When I get all entities transco_services
    Then 6 entities transco_services are returned
    Then the entities transco_services search result is correct

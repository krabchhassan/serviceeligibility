Feature: Service for transcoding parameters

  Background:
    Given I import a complete file for parametrage

  @@todosmokeTests @smokeTestsWithoutKafka @servicedroits
  Scenario: Insert and get all services for transcoding parameters
    When I insert a new service with code "NEW_SERVICE" and a transcoding object with code "Domaine_Droits"
    Then I search for all serviceDroits the service with code "NEW_SERVICE" is on the list

  @@todosmokeTests @smokeTestsWithoutKafka @servicedroits
  Scenario: Delete a service
    And I delete the service with code "NEW_SERVICE"
    Then the service code with "NEW_SERVICE" is not present

  @@todosmokeTests @smokeTestsWithoutKafka @servicedroits
  Scenario: Insert and get all services for transcoding parameters
    Then I search for all serviceDroits the service with code "CARTE-TP" is on the list

  @@todosmokeTests @smokeTestsWithoutKafka @servicedroits
  Scenario: Update a service
    And I insert a new service with code "NEW_SERVICE" and a transcoding object with code "Domaine_Droits"
    And I update service with code "NEW_SERVICE" with new transcoding object with code "Mode_Paiement"
    Then the service with code "NEW_SERVICE" gets a transcoding object with code "Mode_Paiement"

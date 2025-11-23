Feature: Update transco keys

  @smokeTests @smokeTestsWithoutKafka @transco
  Scenario: Get card by ID
    Given I import a complete file for parametrage
    When I search transco per services with service code ALMV3 and transco code Code_Mouvement
    Then The transco per service contains 37 keys
    When I update the transco par service keeping 5 records
    When I search transco per services with service code ALMV3 and transco code Code_Mouvement
    Then The transco per service contains 5 keys

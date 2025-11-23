Feature: Search transco service

  @smokeTests @smokeTestsWithoutKafka @transco
  Scenario: Get search transco
    Given I import a complete file for parametrage
    When I search transco per services with service code ALMV3 and transco code Lien_Juridique
    Then The transco per service is correct

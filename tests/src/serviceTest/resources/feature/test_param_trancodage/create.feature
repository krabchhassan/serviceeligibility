Feature: Transcoding parameters

  Background:
    Given I import a complete file for parametrage

  @@todosmokeTests @smokeTestsWithoutKafka @paramtransco
  Scenario: Inserting new transcoding parameter
    And I search for all transcoding parameters
    When I insert a new transcoding parameter with code "NEW_TRANSCO" and name "NEW TRANSCO" and one column with name "Column1"
    When I search for all transcoding parameters after insertion
    Then the list of transcoding parameters contains one more item
    Then the transcoding parameter list contains the previously inserted transcoding parameter

  @@todosmokeTests @smokeTestsWithoutKafka @paramtransco
  Scenario: Delete previously created transco parameter
    When I delete the transcoding parameter with code "Domaine_Droits"
    Then the transcoding parameter is not present on the list

  @@todosmokeTests @smokeTestsWithoutKafka @paramtransco
  Scenario: Search transcoding parameters by code
    When I search for transcoding parameter with code "Domaine_Droits"
    Then the returned transcoding parameter is correct

  @@todosmokeTests @smokeTestsWithoutKafka @paramtransco
  Scenario: Updating a parameter of an existing transcoding
    And I search for all transcoding parameters
    When I update a parameter with code "Lien_Juridique" and change the name to "new lien"
    Then the modified parameter's name is "new lien"


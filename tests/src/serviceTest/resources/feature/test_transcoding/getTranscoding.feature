Feature: Transcoding parameters

  Background:
    Given I import a complete file for parametrage

  @smokeTests @smokeTestsWithoutKafka @paramtransco @release
  Scenario: Inserting new transcoding parameter
    Given I search for all transcoding parameters
    When I insert a new transcoding parameter with code "NEW_TRANSCO" and name "NEW TRANSCO" and one column with name "Column1"
    Given I search for all transcoding parameters
    Then The list of transcoding parameters contains one more item
    Then The transcoding parameter list contains the previously inserted transcoding parameter

  @smokeTests @smokeTestsWithoutKafka @paramtransco
  Scenario: Delete previously created transco parameter
    When I delete the transcoding parameter with code "Domaine_Droits"
    Then The transcoding parameter is not present on the list

  @smokeTests @smokeTestsWithoutKafka @paramtransco
  Scenario: Search transcoding parameters by code
    When I search for transcoding parameter with code "Domaine_Droits"
    Then The returned transcoding parameter is correct

  @smokeTests @smokeTestsWithoutKafka @paramtransco
  Scenario: Updating a parameter of an existing transcoding
    When I update a parameter with code "Lien_Juridique" and change the name to "new lien"
    Then The modified parameter's name is "new lien"


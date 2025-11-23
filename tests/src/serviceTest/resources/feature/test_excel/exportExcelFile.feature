Feature: Export an Excel file from a JSON

  @todosmokeTests @smokeTestsWithoutKafka @export
  Scenario: Export a file with a valid format without precision
    When I export a file "../../resources/excel/referential.json"
    Then The file is properly exported

  @todosmokeTests @smokeTestsWithoutKafka @export
  Scenario: Export a file with the highest valid precision value
    When I export a file "../../resources/excel/precisionAvaible.json"
    Then The file is properly exported

  @todosmokeTests @smokeTestsWithoutKafka @export
  Scenario: Export a file with a precision under the range
    When I export a file "../../resources/excel/precisionLowRange.json"
    Then an error "400" is returned

  @todosmokeTests @smokeTestsWithoutKafka @export
  Scenario: Export a file with a precision above the range
    When I export a file "../../resources/excel/precisionAboveRange.json"
    Then an error "400" is returned

  @todosmokeTests @smokeTestsWithoutKafka @export
  Scenario: Export a file with a precision invalid format
    When I export a file "../../resources/excel/precisionInvalidFormat.json"
    Then an error "500" is returned

  @todosmokeTests @smokeTestsWithoutKafka @export
  Scenario: Export a file with the smallest valid precision value
    When I export a file "../../resources/excel/precisionAvaibleLow.json"
    Then The file is properly exported

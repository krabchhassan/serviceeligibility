Feature: Export all

  @todosmokeTests @smokeTestsWithoutKafka @export
  Scenario: export data
    Given I import a complete file
    When I export all
    Then The exported data is correct

  @todosmokeTests @smokeTestsWithoutKafka @export
  Scenario: export parametrage data
    Given I try to import a complete file for parametrage
    When I export all parametrage
    Then The exported parametrage data is correct

    #@todosmokeTests @smokeTestsWithoutKafka @export
   #   Scenario: export xls file volumetrie
   #   Given I import a complete file
   #   When I export the volumetrie xls file without parameter
   #   Then the exported file has the same content as the file "volumetryNotFiltered.xlsx"
#
   # @todosmokeTests @smokeTestsWithoutKafka @export
   # Scenario: export xls file volumetrie with parameter
   #   Given I import a complete file
    #  When I export the volumetrie xls file with parameter
    #  | amc               | 0000733931|
    #  | codePartenaire    | TNR |
   #   Then the exported file has the same content as the file "volumetryFiltered.xlsx"
